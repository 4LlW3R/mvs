package com.epam.tcodata.mock.secure.storage.dal.impl;

import com.epam.tcodata.secure.storage.dal.exception.SecretStorageException;
import com.epam.tcodata.secure.storage.dal.impl.AbstractSecretStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class MockSecretStorage extends AbstractSecretStorage {

    private Map<String, String> secrets;

    public MockSecretStorage() {
        this.secrets = new HashMap<>();
    }

    @Override
    public void init(Properties properties) {
        super.init(properties);

        for (String name : properties.stringPropertyNames()) {
            String value = properties.getProperty(name);
            value = value == null ? "" : value;
            this.secrets.put(name, value);
        }
    }

    @Override
    protected Set<String> retrieveAllNames() {
        return this.secrets.keySet();
    }

    @Override
    protected String retrieveSecretByName(String name) {
        String secret = this.secrets.get(name);
        if (secret == null) {
            throw new SecretStorageException("Can't receive secret with id " + name);
        }
        return secret;
    }

    @Override
    protected void storeSecretByName(String name, String value) {
        this.secrets.put(name, value);
    }
}
