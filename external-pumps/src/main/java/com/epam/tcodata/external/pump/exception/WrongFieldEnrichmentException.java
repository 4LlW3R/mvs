package com.epam.tcodata.external.pump.exception;

public class WrongFieldEnrichmentException extends RuntimeException {

    public WrongFieldEnrichmentException(String message) {
        super(message);
    }

    public WrongFieldEnrichmentException(String message, Throwable cause) {
        super(message, cause);
    }
}
