package com.epam.tcodata.mock.secure.storage.dal.factory.impl;

import com.epam.tcodata.common.ResourceUtils;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.Secret;
import com.epam.tcodata.secure.storage.dal.factory.ISecretStorageFactory;
import org.junit.Test;

import java.util.HashMap;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MockSecretStorageFactoryTest {

    ISecretStorageFactory factory = new MockSecretStorageFactory(new HashMap<>());

    @Test
    public void createSecretStorage() {
        Properties properties = ResourceUtils.readProperties("mock-secret-storage.properties");
        ISecretStorage secretStorage = this.factory.createSecretStorage(properties);

        assertNotNull(secretStorage);

        String actualUser = secretStorage.retrieveSecret(Secret.Sql.MDM.user);
        assertEquals("value-Sql-MDM-user", actualUser);
    }
}