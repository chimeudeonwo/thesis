package com.bepastem.exceptions;

public class CannotBeNullException extends RuntimeException {

    public CannotBeNullException(String message) {
        super(message);

    }

    public CannotBeNullException(String message, Throwable cause) {
        super(message, cause);

    }
}
