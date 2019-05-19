package ru.bbpax.keeper.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.ResponseEntity.status;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity vehicleNotFound(BadCredentialsException ex, WebRequest request) {
        log.debug("handling BadCredentialsException...");
        return status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
}
