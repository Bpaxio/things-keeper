package ru.bbpax.keeper.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_MODIFIED)
public class TagIsUsedException extends RuntimeException {
    public TagIsUsedException() {
        super("Remove all usages before deletion");
    }
}
