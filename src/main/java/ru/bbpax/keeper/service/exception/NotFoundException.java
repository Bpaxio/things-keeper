package ru.bbpax.keeper.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    public NotFoundException(String entityType, String id) {
        super("Entity " + entityType + "[id = " + id + "] was not found");
    }
}
