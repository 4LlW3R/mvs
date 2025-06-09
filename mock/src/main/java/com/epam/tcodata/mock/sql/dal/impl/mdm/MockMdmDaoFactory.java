package com.epam.tcodata.mock.sql.dal.impl.mdm;

import com.epam.tcodata.mock.MockFactoryAbilities;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.impl.mdm.MdmDaoFactory;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

public class MockMdmDaoFactory extends MdmDaoFactory {
    /**
     * Main pubic constructor.
     *
     * @param secretStorage
     */
    public MockMdmDaoFactory(ISecretStorage secretStorage) {
        this(secretStorage, Collections.emptyMap());
    }

    /**
     * Constructor with backup/restore abilities.
     *
     * @param secretStorage secret store
     * @param parameters additional parameters
     */
    public MockMdmDaoFactory(ISecretStorage secretStorage, Map<String, String> parameters) {
        super(secretStorage);

        String backupDir = parameters.get(MockFactoryAbilities.MDM_DAO_FACTORY_BACKUP.name());
        if (backupDir != null) {
            restore(Paths.get(backupDir));
        }
    }


    @Override
    public String buildURL(String hostName, String port, String database) {
        return new StringBuilder("jdbc:h2:mem:")
                .append(database)
                .toString();
    }

    @Override
    protected boolean isNeededToCreate() {
        return true;
    }
}
