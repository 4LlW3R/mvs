package com.epam.tcodata.models.exception;

public class NonEnrichedEntityException extends RuntimeException {

    private static final long serialVersionUID = 186057359198940592L;

    public NonEnrichedEntityException(String message) {
        super(message);
    }

    public NonEnrichedEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
