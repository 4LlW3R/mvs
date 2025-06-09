package com.epam.tcodata.token.manager.factory;

import com.epam.tcodata.secure.storage.dal.exception.SecretStorageException;
import com.epam.tcodata.token.manager.repository.IMixIdentityRepository;
import com.epam.tcodata.token.manager.service.IMainService;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;

import java.util.Map;

public interface ITokenManagerFactory {

    /**
     * Set extra parameters, that this factory may use for its own purposes.
     *
     * @param parameters
     */
    default void setInitParameters(Map<String, String> parameters) {
    }

    ISecretStorage createSecretStorage() throws SecretStorageException;

    IDaoFactory createPumpDaoFactory(ISecretStorage secretStorage) throws SecretStorageException;

    IMainService createMainService(IDaoFactory daoFactory, ISecretStorage secretStorage) throws SecretStorageException;

    IMixIdentityRepository createMixIdentityRepository(ISecretStorage secretStorage);
}
