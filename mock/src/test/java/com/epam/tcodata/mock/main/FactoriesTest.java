package com.epam.tcodata.mock.main;

import com.epam.tcodata.mock.MockFactoryAbilities;
import com.epam.tcodata.mock.secure.storage.dal.factory.impl.MockSecretStorageFactory;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.Account;
import com.epam.tcodata.sql.dal.service.pumps.IAccountService;
import org.junit.Test;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.*;

public class FactoriesTest {


    public static final String TESTDATA_DBBACKUPS_PUMP = "local-data/db-backup/pump";

    @Test
    public void factoriesByEntityType() throws Exception {

        Instant currentMoment = Instant.now();
        String secureStoragePath = "local-data/secret-storage.properties";
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put(MockFactoryAbilities.SECURE_STORE_BACKUP.name(), secureStoragePath);
        ISecretStorage secretStorage = new MockSecretStorageFactory(initParameters).createSecretStorage(new Properties());
        Factories factories = Factories.factoriesByEntityType(null, secretStorage, currentMoment, EntityType.POSITION);
        assertNotNull(factories);

        IDaoFactory extPumpDaoFactory = factories.getExternalFactories().get(0).createPumpDaoFactory(secretStorage);
        extPumpDaoFactory.restore(Paths.get(TESTDATA_DBBACKUPS_PUMP));
        assertNotNull(extPumpDaoFactory);

        IAccountService accountServiceExt = IDaoFactory.service(extPumpDaoFactory, Account.class);
        List<Account> accountsExt = accountServiceExt.readAll();
//        extPumpDaoFactory.close();

        IDaoFactory intPumpDaoFactory = factories.getInternalFactories().get(0).createPumpDaoFactory(secretStorage);
        assertNotNull(intPumpDaoFactory);

        IAccountService accountServiceInt = IDaoFactory.service(intPumpDaoFactory, Account.class);
        assertNotNull(accountServiceInt);
        assertNotSame(accountServiceExt, accountServiceInt);

        List<Account> accountsInt = accountServiceInt.readAll();
        assertTrue(accountsInt.size() > 0);

        assertEquals(accountsExt, accountsInt);
    }
}