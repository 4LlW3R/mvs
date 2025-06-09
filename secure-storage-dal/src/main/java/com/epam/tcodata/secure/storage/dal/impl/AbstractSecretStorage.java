package com.epam.tcodata.secure.storage.dal.impl;

import com.epam.tcodata.secure.storage.dal.ISecretIdentity;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.Secret;
import com.epam.tcodata.secure.storage.dal.exception.SecretStorageException;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractSecretStorage implements ISecretStorage {

    protected AbstractSecretStorage() {
        /***  Default implementation ***/
    }

    @Override
    public void storeSecret(ISecretIdentity identity, String value) {
        if (identity == null) {
            throw new SecretStorageException("Identity is null ");
        }
        String name = identity.buildSecretFullName();
        storeSecretByName(name, value);
    }

    @Override
    public void init(Properties properties) {
        if (properties == null) {
            throw new SecretStorageException("Properties is null ");
        }
    }

    @Override
    public String retrieveSecret(ISecretIdentity identity) throws SecretStorageException {
        if (identity == null) {
            throw new SecretStorageException("Identity is null ");
        }
        String name = identity.buildSecretFullName();
        return retrieveSecretByName(name);
    }

    @Override
    public Set<ISecretIdentity> collectPossibleIdentities() {
        return Secret.collectAllIdentifiers();
    }

    @Override
    public Map<String, String> retrieveStoredSecrets() {
        Set<String> iSecretIdentities = retrieveAllNames();
        return iSecretIdentities.stream()
                .collect(Collectors.toMap(name -> name, name -> retrieveSecretByName(name)));
    }

    protected abstract Set<String> retrieveAllNames();

    protected abstract String retrieveSecretByName(String name);

    protected abstract void storeSecretByName(String name, String value);
}
