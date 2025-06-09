package com.epam.tcodata.mdm.base.impl;

import com.epam.tcodata.mdm.IKeyManager;
import com.epam.tcodata.mdm.base.AbstractKeyFactory;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.mdm.KeyManagerVersion;
import com.epam.tcodata.sql.dal.impl.mdm.MdmDaoFactory;

public class KeyFactory extends AbstractKeyFactory {

    public KeyFactory() {
        /***  Default implementation ***/
    }

    @Override
    protected IDaoFactory createServiceFactory(ISecretStorage secretStorage) throws Exception {
        return new MdmDaoFactory(secretStorage);
    }

    @Override
    protected IKeyManager createKeyManager(KeyManagerVersion version, IDaoFactory mdmDaoFactory) {
        return new KeyManager(version, mdmDaoFactory);
    }
}
