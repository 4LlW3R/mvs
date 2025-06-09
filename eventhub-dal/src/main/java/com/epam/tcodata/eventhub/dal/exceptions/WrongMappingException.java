package com.epam.tcodata.eventhub.dal.exceptions;

public class WrongMappingException extends RuntimeException {

    public WrongMappingException(String message) {
        super(message);
    }

    public WrongMappingException(String message, Throwable cause) {
        super(message, cause);
    }
}
