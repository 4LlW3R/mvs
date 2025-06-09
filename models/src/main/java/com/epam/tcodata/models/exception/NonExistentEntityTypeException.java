package com.epam.tcodata.models.exception;

public class NonExistentEntityTypeException extends RuntimeException {

    private static final long serialVersionUID = 186057359198940592L;

    public NonExistentEntityTypeException(String message) {
        super(message);
    }

    public NonExistentEntityTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
