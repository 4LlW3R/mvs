package com.epam.tcodata.mock.mdm.base.impl;

import com.epam.tcodata.mdm.IKeyFactory;
import com.epam.tcodata.mdm.IKeyManager;
import com.epam.tcodata.mdm.base.AbstractKeyFactory;
import com.epam.tcodata.mock.sql.dal.impl.mdm.MockMdmDaoFactory;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.mdm.KeyManagerVersion;

import java.util.HashMap;
import java.util.Map;

public class MockKeyFactory extends AbstractKeyFactory {
    private Map<String, String> parameters = new HashMap<>();
    private static volatile IKeyFactory singleton;

    /**
     * Singleton entry point.
     *
     * @return instance of IKeyFactory
     */
    public static IKeyFactory instance() {
        if (singleton == null) {
            synchronized (MockKeyFactory.class) {
                if (singleton == null) {
                    singleton = new MockKeyFactory();
                }
            }
        }
        return singleton;
    }

    private MockKeyFactory() {
    }

    @Override
    public void setInitParameters(Map<String, String> parameters) {
        this.parameters.putAll(parameters);
    }

    @Override
    protected IDaoFactory createServiceFactory(ISecretStorage secretStorage) throws Exception {
        return new MockMdmDaoFactory(secretStorage, this.parameters);
    }

    @Override
    protected IKeyManager createKeyManager(KeyManagerVersion version, IDaoFactory mdmDaoFactory) {
        return new MockKeyManager(version, mdmDaoFactory);
    }
}
