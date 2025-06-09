package com.epam.tcodata.models.exception;

public class NonMatchedSubTripEnrichedListSizeException extends RuntimeException {

    private static final long serialVersionUID = 186057359198940592L;

    public NonMatchedSubTripEnrichedListSizeException(String message) {
        super(message);
    }

    public NonMatchedSubTripEnrichedListSizeException(String message, Throwable cause) {
        super(message, cause);
    }
}
