package com.epam.tcodata.models.exception;

public class InvalidAvroTypeException extends RuntimeException {

    private static final long serialVersionUID = -3846510036587783261L;

    public InvalidAvroTypeException(String message) {
        super(message);
    }

    public InvalidAvroTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
