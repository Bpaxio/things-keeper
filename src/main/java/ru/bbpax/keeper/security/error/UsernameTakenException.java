package ru.bbpax.keeper.security.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Vlad Rakhlinskii
 * Created on 17.05.2019.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class UsernameTakenException extends RuntimeException {
    public UsernameTakenException(String username) {
        super(username + " is taken");
    }
}
