package com.epam.tcodata.mdm;

import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.mdm.KeyManagerVersion;

import java.util.Map;

/**
 * IKeyFactory interface determines main entry point for the KeyManager.
 */
public interface IKeyFactory {

    /**
     * Set extra parameters, that this factory may use for its own purposes.
     *
     * @param parameters
     */
    default void setInitParameters(Map<String, String> parameters) {
    }

    IKeyManager createKeyManager(KeyManagerVersion version, ISecretStorage secretStorage);

    IDaoFactory getMdmDaoFactory();
}

