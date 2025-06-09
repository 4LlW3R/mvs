package com.epam.tcodata.sql.dal.exception;

public class OperationIsNotSupportedException extends RuntimeException {
    public OperationIsNotSupportedException() {
    }

    public OperationIsNotSupportedException(String message) {
        super(message);
    }
}
