package ru.bbpax.keeper.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Vlad Rakhlinskii
 * Created on 24.05.2019.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class FileServiceIsNotAvailable extends RuntimeException {
    public FileServiceIsNotAvailable(Exception e) {
        super(e);
    }
}
