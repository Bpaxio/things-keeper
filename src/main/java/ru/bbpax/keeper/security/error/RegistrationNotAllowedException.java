package ru.bbpax.keeper.security.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Vlad Rakhlinskii
 * Created on 17.05.2019.
 */
@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
public class RegistrationNotAllowedException extends RuntimeException {
    public RegistrationNotAllowedException() {
        super("Registration not allowed yet, pls contact to admin to give a permission");
    }
}
