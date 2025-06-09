package com.epam.tcodata.mock.secure.storage.dal.factory.impl;

import com.epam.tcodata.common.ResourceUtils;
import com.epam.tcodata.mock.MockFactoryAbilities;
import com.epam.tcodata.mock.secure.storage.dal.impl.MockSecretStorage;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.factory.ISecretStorageFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MockSecretStorageFactory implements ISecretStorageFactory {

    private static final long serialVersionUID = 7565578904155722220L;
    protected Map<String, String> parameters = new HashMap<>();

    public MockSecretStorageFactory(Map<String, String> parameters) {
        this.parameters.putAll(parameters);
    }

    @Override
    public ISecretStorage createSecretStorage(Properties properties) {

        Properties backupProperties = ResourceUtils.readProperties(this.parameters.get(MockFactoryAbilities.SECURE_STORE_BACKUP.name()));
        properties.putAll(backupProperties);

        MockSecretStorage mockSecretStorage = new MockSecretStorage();
        mockSecretStorage.init(properties);
        return mockSecretStorage;
    }

    private void writeObject(java.io.ObjectOutputStream stream)
            throws IOException {
        stream.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
    }
}
