package com.epam.tcodata.secure.storage.dal;

import com.epam.tcodata.secure.storage.dal.exception.SecretStorageException;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * This interface determines set of methods for Secure Storage. The storage is read-only.
 * An instance of such storage must be created by ISecureStorageFactory. As it is read-only storage, so it isn't
 * mandatory keep such object as singleton because each instance can keep the same immutable data.
 * All other modules should use SecretStorage to gain all sensitive data.
 */
public interface ISecretStorage {
    /**
     * Initializes instance of secret storage. This method must be invoked right next after creation of the
     * SecureStorage object. Usually it done by factory.
     * Each implementation of ISecretStorage interface can consider these properties at its discretion.
     *
     * @param properties properties object.
     */
    void init(Properties properties);

    /**
     * This method is needed only for tests. So it shouldn't be accessible from other parts of code.
     *
     * @param identity secret identity.
     * @param value storing secret.
     */
    void storeSecret(ISecretIdentity identity, String value);

    /**
     * Gains a secret by its identity. The secret that is returned is decrypted if needed.
     *
     * @param identity identity object that describe subsystem, section, parameter.
     * @return value of retrieved secret.
     */
    String retrieveSecret(ISecretIdentity identity) throws SecretStorageException;


    /**
     * Collects all possible identifier for all subsystems, sections, parameters.
     * Can be useful to check that all parameters are added into SecretStorage and to get all of them.
     *
     * @return set of identities.
     */
    Set<ISecretIdentity> collectPossibleIdentities();

    /**
     * Collects all secrets (both names and values) into a map with keys as full secret names and
     * values as their values.
     * Notice, that these map can contain not only secrets defined by proper identities, but all existing.
     *
     * @return map of retrieved secret values
     */
    Map<String, String> retrieveStoredSecrets();
}
