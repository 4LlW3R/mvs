package com.epam.tcodata.mock.eventhub.dal.exception;

public class WrongOffsetsException extends RuntimeException {

    public WrongOffsetsException(String message) {
        super(message);
    }

    public WrongOffsetsException(String message, Throwable cause) {
        super(message, cause);
    }
}
