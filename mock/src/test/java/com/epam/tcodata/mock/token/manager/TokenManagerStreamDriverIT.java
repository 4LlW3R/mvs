package com.epam.tcodata.mock.token.manager;

import com.epam.tcodata.mock.MockFactoryAbilities;
import com.epam.tcodata.mock.external.pump.util.misc.MockExpectationInitializer;
import com.epam.tcodata.mock.external.pump.util.misc.RestMockUtil;
import com.epam.tcodata.mock.token.manager.factory.impl.MockTokenManagerFactory;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.token.manager.driver.TokenManagerStreamDriver;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockserver.configuration.ConfigurationProperties;
import org.mockserver.integration.ClientAndServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.nio.file.Paths;

import static com.epam.tcodata.token.manager.util.DriverUtil.*;


public class TokenManagerStreamDriverIT {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenManagerStreamDriverIT.class);
    static {
        ConfigurationProperties.initializationClass(MockExpectationInitializer.class.getName());
        try {
            MockExpectationInitializer.setBase("local-data/rest");
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage());
        }

    }

    private static ClientAndServer mockServer = null;

    @BeforeClass
    public static void setup() {
        mockServer = ClientAndServer.startClientAndServer(RestMockUtil.PORT);
    }

    @AfterClass
    public static void cleanup() {
        mockServer.stop();
    }

    @Test
    public void testDriver() throws Exception {

        try {
            String applicationName = "TokenManager";
            String batchIntervalSeconds = "10";
            String factoryClassName = MockTokenManagerFactory.class.getName();

            String[] args = new String[]{
                    combineOption(APP_NAME, applicationName),
                    combineOption(BATCH_INTERVAL_SECONDS, batchIntervalSeconds),
                    combineOption(FACTORY_CLASS_NAME, factoryClassName),
                    combineOption(TEST_FACTORY_ABILITIES, MockFactoryAbilities.PUMP_DAO_FACTORY_BACKUP + "=local-data/db-backup/pump"),
                    combineOption(LAUNCH_TIME, "30")
            };

            TokenManagerStreamDriver.main(args);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        MockTokenManagerFactory factory = new MockTokenManagerFactory();
        try(IDaoFactory pumpDaoFactory = factory.createPumpDaoFactory(factory.createSecretStorage())){
            pumpDaoFactory.backup(Paths.get("temp", "tokenmanager"));
        }
    }

    private static String combineOption(String name, String value) {
        return "--" + name + "=" + value;
    }
}
