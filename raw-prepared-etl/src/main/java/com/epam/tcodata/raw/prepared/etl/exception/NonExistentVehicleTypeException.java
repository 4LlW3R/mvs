package com.epam.tcodata.raw.prepared.etl.exception;

public class NonExistentVehicleTypeException extends RuntimeException {

    private static final long serialVersionUID = 4437613312888082318L;

    public NonExistentVehicleTypeException(String message) {
        super(message);
    }

    public NonExistentVehicleTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
