package com.epam.tcodata.mock.mdm.base.impl;

import com.epam.tcodata.mdm.base.impl.KeyManager;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.mdm.KeyManagerVersion;

import java.util.UUID;

public class MockKeyManager extends KeyManager {
    /**
     * Main public constructor.
     *
     * @param version
     * @param factory - an instance of IDaoFactory for storing and restoring needed data.
     */
    public MockKeyManager(KeyManagerVersion version, IDaoFactory factory) {
        super(version, factory);
    }

    @Override
    public UUID newDurableKey(EntityType entityType, String naturalKeyValue) {
        String template = String.format("%08d-0000-0000-0000-%012d", entityType.getCode(), naturalKeyValue.hashCode() & 0x7FFFFFFF);
        return UUID.fromString(template);
    }
}
