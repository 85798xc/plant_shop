package com.example.plant_shop.service.files;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
@Slf4j
public class StorageService {

    @Value("${storage.location}")
    private Path rootLocation;

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(rootLocation);
    }

    public String store(MultipartFile file) {
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        if (file.isEmpty())
            throw new FileFormatException(String.format("Не удалось сохранить пустой файл {}!" + filename));

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, this.rootLocation.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            log.error("Не удалось сохранить файл {}! Error: [{}]", filename, e);
            throw new FileStorageException(String.format("Не удалось сохранить файл %s! Error: [%s]", filename, e));
        }

        return filename;
    }

    public Resource loadAsResource(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (!resource.exists() || resource.isReadable())
                throw new FileFormatException(String.format("Не удалось прочитать файл %s!", filename));
            return resource;

        } catch (MalformedURLException e) {
            throw new FileStorageException(String.format("Не удалось загрузить файл %s! Error: [%s].", filename, e));
        }
    }
}

