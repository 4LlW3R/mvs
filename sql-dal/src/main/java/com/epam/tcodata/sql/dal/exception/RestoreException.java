package com.epam.tcodata.sql.dal.exception;

public class RestoreException extends RuntimeException {
    public RestoreException() {
    }

    public RestoreException(String message) {
        super(message);
    }

    public RestoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
