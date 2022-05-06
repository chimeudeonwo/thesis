package com.bepastem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FOUND)
public class UserNameExistException  extends RuntimeException {

    public UserNameExistException(String message) {
        super(message);
    }

    public UserNameExistException(String message, Throwable cause) {
        super(message, cause);

    }
}
