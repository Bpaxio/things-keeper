package ru.bbpax.keeper.service.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Component
public class FilesMockClient implements FilesClient {
    @Override
    public String saveFile(File file) {
        return "xm/" + UUID.randomUUID().toString();
    }

    @Override
    public File getFile(String query) {
        log.info("Query: {}", query);
        Paths.get("Bug.jpg");
        return Paths.get("/Users/Bpaxio/Documents/workspace/otus/things-keeper/src/main/resources/Bug.jpg").toFile();
    }
}
