package com.epam.tcodata.mock.eventhub.dal.exception;

public class NullOffsetRangeException extends RuntimeException {

    public NullOffsetRangeException(String message) {
        super(message);
    }

    public NullOffsetRangeException(String message, Throwable cause) {
        super(message, cause);
    }
}
