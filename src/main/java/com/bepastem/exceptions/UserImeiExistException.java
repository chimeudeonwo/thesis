package com.bepastem.exceptions;

public class UserImeiExistException extends RuntimeException {
    public UserImeiExistException(String message) {
        super(message);
    }

    public UserImeiExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
