package ru.bbpax.keeper.service.client;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Component
@Profile("mock")
public class FilesMockClient implements FilesClient {
    @Value("${filer.host}")
    private String host;

    @Value("${filer.port}")
    private String port;

    @Override
    public String saveFile(String id, String imageId, MultipartFile file) {
        return host + ":" + port + "/" + LocalDate.now().toString() + "/" + UUID.randomUUID().toString();
    }

    @Override
    public String updateFile(String link, MultipartFile file) {
        return link;
    }

    @Override
    @SneakyThrows
    public File getFile(String link) {
        log.info("get file by link: {}", link);
        return Paths.get(getClass().getClassLoader().getResource("Bug.jpg").toURI()).toFile();
    }

    @Override
    public void deleteFile(String query) {
        log.info("file, locate in {} was deleted", query);
    }
}
