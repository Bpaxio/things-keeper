package ru.bbpax.keeper.service.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class FilesMockClient implements FilesClient {
    @Override
    public String saveFile(Object file) {
        return "xm/" + UUID.randomUUID().toString();
    }

    @Override
    public Object getFile(String query) {
        log.info("Query: {}", query);
        return null;
    }
}
