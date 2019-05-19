package ru.bbpax.keeper.security.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Vlad Rakhlinskii
 * Created on 17.05.2019.
 */
@ResponseStatus(HttpStatus.NOT_MODIFIED)
public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException() {
        super("Wrong password");
    }
}
