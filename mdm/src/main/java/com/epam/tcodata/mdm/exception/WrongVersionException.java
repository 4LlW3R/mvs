package com.epam.tcodata.mdm.exception;

import com.epam.tcodata.sql.dal.domain.mdm.KeyManagerVersion;

public class WrongVersionException extends RuntimeException {
    public WrongVersionException(KeyManagerVersion expectedVersion, KeyManagerVersion actualVersion) {
        this(expectedVersion, actualVersion, null);
    }

    public WrongVersionException(KeyManagerVersion expectedVersion, KeyManagerVersion actualVersion, Throwable cause) {
        super("Expected version: " + expectedVersion + " actual version: " + actualVersion, cause);
    }
}
