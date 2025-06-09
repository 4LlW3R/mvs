package com.epam.tcodata.secure.storage.dal.factory.impl;

import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.exception.SystemVarNotFoundException;
import com.epam.tcodata.secure.storage.dal.factory.ISecretStorageFactory;
import com.epam.tcodata.secure.storage.dal.impl.KeyVaultSecretStorage;

import java.util.Properties;

public class KeyVaultSecretStorageFactory implements ISecretStorageFactory {

    private static final long serialVersionUID = -8035647589888030083L;

    public static final String  SYSTEM_VAR_VAULT_BASE_URL = "VAULT_BASE_URL";
    public static final String  SYSTEM_VAR_CLIENT_ID = "CLIENT_ID";
    public static final String  SYSTEM_VAR_CLIENT_SECRET = "CLIENT_SECRET";
    public static final String  SYSTEM_VAR_ENCRYPTION_KEY_ID = "ENCRYPTION_KEY_ID";

    private String baseUrl;
    private String clientId;
    private String clientSecret;
    private String encryptionKeyId;

    /**
     * Main public constructor.
     */
    public KeyVaultSecretStorageFactory() {
        this.baseUrl = getSystemVarOrException(SYSTEM_VAR_VAULT_BASE_URL);
        this.clientId = getSystemVarOrException(SYSTEM_VAR_CLIENT_ID);
        this.clientSecret = getSystemVarOrException(SYSTEM_VAR_CLIENT_SECRET);
        this.encryptionKeyId = getSystemVarOrException(SYSTEM_VAR_ENCRYPTION_KEY_ID);
    }

    @Override
    public ISecretStorage createSecretStorage(Properties properties) {
        setPropertyIfNotExist(properties, KeyVaultSecretStorage.VAULT_BASE_URL, this.baseUrl);
        setPropertyIfNotExist(properties, KeyVaultSecretStorage.CLIENT_ID,  this.clientId);
        setPropertyIfNotExist(properties, KeyVaultSecretStorage.CLIENT_SECRET, this.clientSecret);
        setPropertyIfNotExist(properties, KeyVaultSecretStorage.ENCRYPTION_KEY_ID, this.encryptionKeyId);

        KeyVaultSecretStorage keyVaultSecretStorage = new KeyVaultSecretStorage();
        keyVaultSecretStorage.init(properties);
        return keyVaultSecretStorage;
    }

    private static String getSystemVarOrException(String varName) {
        String value = System.getenv(varName);
        if (value == null) {
            throw new SystemVarNotFoundException("System var " + varName + " is not set.");
        }
        return value;
    }

    private void setPropertyIfNotExist(Properties properties, String propertyName, String value) {
        if (properties.getProperty(propertyName) == null) {
            properties.setProperty(propertyName, value);
        }
    }
}
