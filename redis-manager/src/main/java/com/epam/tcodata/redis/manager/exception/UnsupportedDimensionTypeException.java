package com.epam.tcodata.redis.manager.exception;

public class UnsupportedDimensionTypeException extends RuntimeException {

    public UnsupportedDimensionTypeException(String message) {
        super(message);
    }

    public UnsupportedDimensionTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
