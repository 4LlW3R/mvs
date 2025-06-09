package com.epam.tcodata.mock.util;

import com.epam.tcodata.mock.MockFactoryAbilities;
import com.epam.tcodata.mock.secure.storage.dal.factory.impl.MockSecretStorageFactory;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.factory.ISecretStorageFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Utility class with collection useful for mock methods.
 *
 */
public final class MockUtils {

    private static final String PROPERTY_FILE_NAME = "mock-secret-storage.properties";

    private MockUtils() {
    }

    /**
     * Creates instance of MockSecretStorage through creation of MockSecretStorageFactory.
     * Fills the storage with data from predefined property file.
     * @return
     */
    public static ISecretStorage createDefaultMockSecretStorage() {
        Map<String, String> init = new HashMap<>();
        init.put(MockFactoryAbilities.SECURE_STORE_BACKUP.name(), PROPERTY_FILE_NAME);
        ISecretStorageFactory secretStorageFactory = new MockSecretStorageFactory(init);
        return secretStorageFactory.createSecretStorage(new Properties());
    }
}
