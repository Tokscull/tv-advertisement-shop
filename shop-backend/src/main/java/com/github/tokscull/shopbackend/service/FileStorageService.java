package com.github.tokscull.shopbackend.service;


import com.github.tokscull.shopbackend.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.Objects;

@Service
public class FileStorageService {

    @Value("${file.storage.plugin-path}")
    private Path pluginDir;

    public String storeFile(MultipartFile file) {

        if(file.isEmpty()) {
            throw new FileStorageException("File is empty");
        }

        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            Path targetLocation = this.pluginDir.resolve(Instant.now().getEpochSecond() + "_" + filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return targetLocation.toString();
        } catch (IOException e) {
            throw new FileStorageException("Could not store file " + filename);
        }
    }
}
