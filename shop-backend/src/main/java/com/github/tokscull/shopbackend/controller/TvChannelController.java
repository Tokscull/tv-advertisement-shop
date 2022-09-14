package com.github.tokscull.shopbackend.controller;

import com.github.tokscull.shopbackend.exceptions.EntityAlreadyExistsException;
import com.github.tokscull.shopbackend.exceptions.EntityNotFoundException;
import com.github.tokscull.shopbackend.model.TvChannel;
import com.github.tokscull.shopbackend.model.dto.TvChannelAdvertisementRequest;
import com.github.tokscull.shopbackend.model.dto.TvChannelAdvertisementResponse;
import com.github.tokscull.shopbackend.repository.TvChannelRepository;
import com.github.tokscull.shopbackend.service.FileStorageService;
import com.github.tokscull.shopbackend.service.TvChannelService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


@RestController
@RequestMapping("/api/tv-channels")
@AllArgsConstructor
@Slf4j
public class TvChannelController {

    private final FileStorageService fileStorageService;
    private final TvChannelRepository tvChannelRepository;
    private final TvChannelService tvChannelService;


    /**
     * Create a TvChannel entity and store plugin on disk
     *
     * @param channelName the TvChannel name
     * @param file the pluginable jar file
     * @return the ResponseEntity with status 201 (CREATED) and the TvChannel in body
     * @throws EntityAlreadyExistsException when entity with channelName already exist
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<TvChannel> createChannel(@RequestParam("channelName") String channelName,
                                                   @RequestParam("file") MultipartFile file) {
        log.info("Received request to save new channel: {}", channelName);

        if(tvChannelRepository.existsByName(channelName)) {
            throw new EntityAlreadyExistsException("Channel already exist");
        }

        String pluginPath = fileStorageService.storeFile(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(tvChannelRepository.save(new TvChannel(channelName, pluginPath)));
    }

    /**
     * Loads and process plugins for each channel
     * (!doesn't check compliance between channel name in db and in plugin, always return name from plugin)
     *
     * @param request include duration and repeatCount
     * @return the ResponseEntity with status 200 (OK) and the List of TvChannelAdvertisementResponse in body
     */
    @PostMapping("/advertisement")
    public ResponseEntity<List<TvChannelAdvertisementResponse>> getAdvertisements(@RequestBody TvChannelAdvertisementRequest request) {
        log.info("Received request to get advertisement price for channels");
        return ResponseEntity.ok().body(tvChannelService.getAdvertisements(request));
    }

    /**
     * Loads and process plugin for specific channel
     * (!channel name in db and in plugin should be the same)
     *
     * @param channelId the TvChannel id which the requested entity should match
     * @param request include duration and repeatCount
     * @return the ResponseEntity with status 200 (OK) and the TvChannelAdvertisementResponse in body
     * @throws EntityNotFoundException when TvChannel with provided id not exist
     */
    @PostMapping("/{channelId}/advertisement")
    public ResponseEntity<TvChannelAdvertisementResponse> getAdvertisementByChannelId(@PathVariable Long channelId,
                                                                                      @RequestBody TvChannelAdvertisementRequest request)
                                                                                      throws IOException {
        log.info("Received request to get advertisement price for channel with id: {}", channelId);
        return ResponseEntity.ok().body(tvChannelService.getAdvertisementByChannelId(channelId, request));
    }

}
