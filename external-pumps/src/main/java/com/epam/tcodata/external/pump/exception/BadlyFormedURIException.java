package com.epam.tcodata.external.pump.exception;

public class BadlyFormedURIException extends RuntimeException {

    public BadlyFormedURIException(String message) {
        super(message);
    }

    public BadlyFormedURIException(String message, Throwable cause) {
        super(message, cause);
    }
}
