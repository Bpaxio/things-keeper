package ru.bbpax.keeper.service.client.exception;

import org.springframework.http.HttpStatus;

public class SaveFileException extends RuntimeException {
    public SaveFileException(HttpStatus statusCode, int statusCodeValue) {
        super("Failed save file in File Service: [" + statusCode + "] - " + statusCodeValue);
    }
}
