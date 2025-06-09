package com.epam.tcodata.mdm.base;

import com.epam.tcodata.mdm.IKeyFactory;
import com.epam.tcodata.mdm.IKeyManager;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.mdm.KeyManagerVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractKeyFactory implements IKeyFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractKeyFactory.class);

    private IDaoFactory mdmDaoFactory;

    @Override
    public IKeyManager createKeyManager(KeyManagerVersion version, ISecretStorage secretStorage) {
        try {
            this.mdmDaoFactory = createServiceFactory(secretStorage);
            return createKeyManager(version, mdmDaoFactory);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new IllegalArgumentException(e);
        }
    }

    public IDaoFactory getMdmDaoFactory() {
        return this.mdmDaoFactory;
    }


    protected abstract IDaoFactory createServiceFactory(ISecretStorage secretStorage) throws Exception;

    protected abstract IKeyManager createKeyManager(KeyManagerVersion version, IDaoFactory mdmDaoFactory);
}
