package com.github.tokscull.shopbackend.service;

import com.github.tokscull.channel.spi.TvChannelAdvertisementProvider;
import com.github.tokscull.shopbackend.exception.EntityAlreadyExistsException;
import com.github.tokscull.shopbackend.exception.EntityNotFoundException;
import com.github.tokscull.shopbackend.model.TvChannel;
import com.github.tokscull.shopbackend.model.dto.TvChannelAdvertisementRequest;
import com.github.tokscull.shopbackend.model.dto.TvChannelAdvertisementResponse;
import com.github.tokscull.shopbackend.repository.TvChannelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

@Service
@AllArgsConstructor
public class TvChannelService {

    private final TvChannelRepository tvChannelRepository;
    private final FileStorageService fileStorageService;

    public TvChannel createChannel(String channelName, MultipartFile file) {
        if(Boolean.TRUE.equals(tvChannelRepository.existsByName(channelName))) {
            throw new EntityAlreadyExistsException("Channel already exist: " + channelName);
        }

        String pluginPath = fileStorageService.storeFile(file);
        return tvChannelRepository.save(new TvChannel(channelName, pluginPath));
    }

    public TvChannelAdvertisementResponse getAdvertisementByChannelId(Long channelId, TvChannelAdvertisementRequest request) throws IOException {
        TvChannel tvChannel = tvChannelRepository.findById(channelId)
                .orElseThrow(() -> new EntityNotFoundException("TvChannel not found for id: "+ channelId));

        ClassLoader cl = new URLClassLoader(new URL[]{Path.of(tvChannel.getPluginPath()).toUri().toURL()});
        ServiceLoader<TvChannelAdvertisementProvider> serviceLoader = ServiceLoader.load(TvChannelAdvertisementProvider.class, cl);
        for (TvChannelAdvertisementProvider service : serviceLoader) {
            if (service.getName().equals(tvChannel.getName())) {
                return new TvChannelAdvertisementResponse(service.getName(),
                        service.getAdvertisementPrice(request.getDuration(), request.getRepeatCount()));
            }
        }
        throw new EntityNotFoundException("Advertisement not found for tvChannel: " + tvChannel.getName());
    }

    public List<TvChannelAdvertisementResponse> getAdvertisements(TvChannelAdvertisementRequest request) {
        List<TvChannel> tvChannels = tvChannelRepository.findAll();

        URL[] urls = tvChannels.stream().map(tv -> {
            try {
                return Path.of(tv.getPluginPath()).toUri().toURL();
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        }).toArray(URL[]::new);
        URLClassLoader urlClassLoader = new URLClassLoader(urls);

        ServiceLoader<TvChannelAdvertisementProvider> serviceLoader = ServiceLoader.load(TvChannelAdvertisementProvider.class, urlClassLoader);
        List<TvChannelAdvertisementResponse> prices = new ArrayList<>();
        for (TvChannelAdvertisementProvider service : serviceLoader) {
            prices.add(new TvChannelAdvertisementResponse(service.getName(),
                    service.getAdvertisementPrice(request.getDuration(), request.getRepeatCount())));
        }
        return prices;
    }

}
