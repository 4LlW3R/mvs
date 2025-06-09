package com.epam.tcodata.hive.dal.exception;

public class RepositoryNotExistException extends RuntimeException {

    public RepositoryNotExistException(String message) {
        super(message);
    }

    public RepositoryNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
