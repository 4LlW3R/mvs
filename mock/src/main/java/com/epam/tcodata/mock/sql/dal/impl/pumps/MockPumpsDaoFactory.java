package com.epam.tcodata.mock.sql.dal.impl.pumps;

import com.epam.tcodata.mock.MockFactoryAbilities;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.impl.pumps.PumpsDaoFactory;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

public class MockPumpsDaoFactory extends PumpsDaoFactory {

    /**
     * Public main constructor.
     *
     * @param secretStorage secret store
     */
    public MockPumpsDaoFactory(ISecretStorage secretStorage) {
        this(secretStorage, Collections.emptyMap());
    }

    /**
     * Constructor with backup/restore abilities.
     *
     * @param secretStorage secret store
     * @param parameters additional parameters
     */
    public MockPumpsDaoFactory(ISecretStorage secretStorage, Map<String, String> parameters) {
        super(secretStorage);

        String backupDir = parameters.get(MockFactoryAbilities.PUMP_DAO_FACTORY_BACKUP.name());
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
