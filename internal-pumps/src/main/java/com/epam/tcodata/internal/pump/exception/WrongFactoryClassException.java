package com.epam.tcodata.internal.pump.exception;

public class WrongFactoryClassException extends RuntimeException {

    public WrongFactoryClassException(String message) {
        super(message);
    }

    public WrongFactoryClassException(String message, Throwable cause) {
        super(message, cause);
    }
}
