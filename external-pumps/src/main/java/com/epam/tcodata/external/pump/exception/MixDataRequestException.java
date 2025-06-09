package com.epam.tcodata.external.pump.exception;

public class MixDataRequestException extends RuntimeException {

    public MixDataRequestException(String message) {
        super(message);
    }

    public MixDataRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
