package com.epam.tcodata.secure.storage.dal.exception;

public class SecretStorageException extends RuntimeException {
    public SecretStorageException() {
    }

    public SecretStorageException(String message) {
        super(message);
    }

    public SecretStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
