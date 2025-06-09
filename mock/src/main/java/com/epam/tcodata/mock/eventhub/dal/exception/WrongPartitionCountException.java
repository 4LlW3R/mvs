package com.epam.tcodata.mock.eventhub.dal.exception;

public class WrongPartitionCountException extends RuntimeException {

    public WrongPartitionCountException(String message) {
        super(message);
    }

    public WrongPartitionCountException(String message, Throwable cause) {
        super(message, cause);
    }
}
