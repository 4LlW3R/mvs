package com.epam.tcodata.token.manager.factory.impl;

import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.exception.SecretStorageException;
import com.epam.tcodata.secure.storage.dal.factory.ISecretStorageFactory;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.impl.pumps.PumpsDaoFactory;
import com.epam.tcodata.token.manager.factory.ITokenManagerFactory;
import com.epam.tcodata.token.manager.repository.IMixIdentityRepository;
import com.epam.tcodata.token.manager.repository.MixIdentityRepository;
import com.epam.tcodata.token.manager.service.IMainService;
import com.epam.tcodata.token.manager.service.MainService;

import java.io.Serializable;
import java.util.Properties;

public class TokenManagerFactory implements ITokenManagerFactory, Serializable {

    private static final long serialVersionUID = 7701895427425947278L;

    private ISecretStorageFactory defaultFactory = ISecretStorageFactory.createDefaultFactory();

    public TokenManagerFactory() {
        /***  Default implementation ***/
    }

    @Override
    public ISecretStorage createSecretStorage() throws SecretStorageException {
        Properties emptyProperty = new Properties();
        return defaultFactory.createSecretStorage(emptyProperty);
    }

    @Override
    public IDaoFactory createPumpDaoFactory(ISecretStorage secretStorage) throws SecretStorageException {
        IDaoFactory daoFactory = new PumpsDaoFactory(secretStorage);
        return daoFactory;
    }

    @Override
    public IMainService createMainService(IDaoFactory daoFactory, ISecretStorage secretStorage) throws SecretStorageException {
        IMixIdentityRepository mixIdentityRepository = createMixIdentityRepository(secretStorage);
        return new MainService(mixIdentityRepository, daoFactory, secretStorage);
    }

    @Override
    public IMixIdentityRepository createMixIdentityRepository(ISecretStorage secretStorage) {
        return new MixIdentityRepository(secretStorage);
    }


}
