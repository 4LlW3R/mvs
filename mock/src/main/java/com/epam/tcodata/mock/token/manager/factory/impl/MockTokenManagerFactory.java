package com.epam.tcodata.mock.token.manager.factory.impl;

import com.epam.tcodata.mock.secure.storage.dal.factory.impl.MockSecretStorageFactory;
import com.epam.tcodata.mock.sql.dal.impl.pumps.MockPumpsDaoFactory;
import com.epam.tcodata.mock.token.manager.repository.MockMixIdentityRepository;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.exception.SecretStorageException;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.token.manager.factory.impl.TokenManagerFactory;
import com.epam.tcodata.token.manager.repository.IMixIdentityRepository;
import com.epam.tcodata.token.manager.service.IMainService;
import com.epam.tcodata.token.manager.service.MainService;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MockTokenManagerFactory extends TokenManagerFactory {

    private static final long serialVersionUID = 3887966850382569954L;

    private Map<String, String> parameters = new HashMap<>();

    public MockTokenManagerFactory() {
        /***  Default implementation ***/
    }

    @Override
    public void setInitParameters(Map<String, String> parameters) {
        this.parameters.putAll(parameters);
    }

    @Override
    public ISecretStorage createSecretStorage() throws SecretStorageException {
        return new MockSecretStorageFactory(this.parameters).createSecretStorage(new Properties());
    }

    @Override
    public IDaoFactory createPumpDaoFactory(ISecretStorage secretStorage) throws SecretStorageException {
        return new MockPumpsDaoFactory(secretStorage, this.parameters);
    }

    @Override
    public IMainService createMainService(IDaoFactory daoFactory, ISecretStorage secretStorage) throws SecretStorageException {
        IMixIdentityRepository mixIdentityRepository = createMixIdentityRepository(secretStorage);
        return new MainService(mixIdentityRepository, daoFactory, secretStorage);
    }

    @Override
    public IMixIdentityRepository createMixIdentityRepository(ISecretStorage secretStorage) {
        return new MockMixIdentityRepository(secretStorage);
    }


}
