package com.epam.tcodata.secure.storage.dal.exception;

public class SystemVarNotFoundException extends RuntimeException {
    public SystemVarNotFoundException() {
    }

    public SystemVarNotFoundException(String message) {
        super(message);
    }

    public SystemVarNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
