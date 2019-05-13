package ru.bbpax.keeper.service.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Component
public class FilesMockClient implements FilesClient {
    @Value("${filer.host}")
    private String host;

    @Value("${filer.port}")
    private String port;

    @Override
    public String saveFile(MultipartFile file) {
        return host + ":" + port + "/" + LocalDate.now().toString() + "/" + UUID.randomUUID().toString();
    }

    @Override
    public File getFile(String link) {
        log.info("get file by link: {}", link);
        return Paths.get("/Users/Bpaxio/Documents/workspace/otus/things-keeper/src/main/resources/Bug.jpg").toFile();
    }

    @Override
    public void deleteFile(String query) {
        log.info("file, locate in {} was deleted", query);
    }
}
