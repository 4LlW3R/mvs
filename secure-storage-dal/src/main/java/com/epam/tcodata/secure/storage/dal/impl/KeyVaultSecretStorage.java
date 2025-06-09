package com.epam.tcodata.secure.storage.dal.impl;

import com.epam.tcodata.secure.storage.dal.exception.SecretStorageException;
import com.epam.tcodata.secure.storage.dal.factory.impl.CustomKeyValueCredentials;
import com.microsoft.azure.PagedList;
import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.keyvault.models.KeyOperationResult;
import com.microsoft.azure.keyvault.models.KeyVaultErrorException;
import com.microsoft.azure.keyvault.models.SecretBundle;
import com.microsoft.azure.keyvault.models.SecretItem;
import com.microsoft.azure.keyvault.requests.SetSecretRequest;
import com.microsoft.azure.keyvault.webkey.JsonWebKeyEncryptionAlgorithm;
import com.microsoft.rest.credentials.ServiceClientCredentials;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class KeyVaultSecretStorage extends AbstractSecretStorage {
    public static final String CLIENT_ID = "clientId";
    public static final String CLIENT_SECRET = "clientSecret";
    public static final String VAULT_BASE_URL = "vaultBaseUrl";
    public static final String ENCRYPTION_KEY_ID = "encryptionKeyId";
    public static final String UTF_8 = "UTF-8";

    private KeyVaultClient client;
    private String vaultBaseUrl;
    private String encryptionKeyId;

    public KeyVaultSecretStorage() {
        /***  Default implementation ***/
    }

    @Override
    public void init(Properties properties) {
        super.init(properties);

        String clientId = properties.getProperty(CLIENT_ID);
        String clientSecret = properties.getProperty(CLIENT_SECRET);
        this.vaultBaseUrl = properties.getProperty(VAULT_BASE_URL);
        this.encryptionKeyId = properties.getProperty(ENCRYPTION_KEY_ID);
        ServiceClientCredentials credentials = new CustomKeyValueCredentials(clientId, clientSecret);
        this.client = new KeyVaultClient(credentials);
    }

    @Override
    protected Set<String> retrieveAllNames() {
        PagedList<SecretItem> secretItems = this.client.listSecrets(this.vaultBaseUrl);
        return Arrays.stream(secretItems.toArray(new SecretItem[] {}))
                .map(s -> s.identifier().name())
                .collect(Collectors.toSet());
    }

    @Override
    protected String retrieveSecretByName(String name) {
        SecretBundle secretBundle = this.client.getSecret(vaultBaseUrl, name);
        if (secretBundle == null || secretBundle.value() == null) {
            throw new SecretStorageException("Can't receive secret with id " + name);
        }
        return decodeSecret(secretBundle);
    }

    @Override
    protected void storeSecretByName(String name, String value) {
        String encodedValue = encodeSecret(value);
        SetSecretRequest secretRequest = new SetSecretRequest.Builder(this.vaultBaseUrl, name, encodedValue).build();
        this.client.setSecret(secretRequest);
    }


    private String decodeSecret(SecretBundle secretBundle) {
        try {
            byte[] data = Base64.getDecoder().decode(secretBundle.value());
            KeyOperationResult decryptResult = this.client.decrypt(this.encryptionKeyId, JsonWebKeyEncryptionAlgorithm.RSA_OAEP, data);
            return new String(decryptResult.result(), Charset.forName(UTF_8));
        } catch (KeyVaultErrorException exception) {
            throw new SecretStorageException("Can't decrypt data from secret " + secretBundle.id(), exception);
        }
    }

    private String encodeSecret(String data) {
        byte[] inputData = data.getBytes(Charset.forName(UTF_8));
        KeyOperationResult encryptResult = client.encrypt(this.encryptionKeyId, JsonWebKeyEncryptionAlgorithm.RSA_OAEP, inputData);
        byte[] encryptedTextBytes = Base64.getEncoder().encode(encryptResult.result());
        String encrypted = new String(encryptedTextBytes, Charset.forName(UTF_8));
        return encrypted;
    }
}
