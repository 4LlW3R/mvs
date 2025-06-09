package com.epam.tcodata.mock.external.pump.driver;

import com.epam.tcodata.external.pump.driver.ExternalPumpStreamDriver;
import com.epam.tcodata.mock.MockFactoryAbilities;
import com.epam.tcodata.mock.external.pump.factory.impl.MockExternalPositionFactory;
import com.epam.tcodata.mock.external.pump.util.misc.MockExpectationInitializer;
import com.epam.tcodata.mock.external.pump.util.misc.RestMockUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockserver.configuration.ConfigurationProperties;
import org.mockserver.integration.ClientAndServer;


public class PositionExternalPumpDriverIT {

    static {
        ConfigurationProperties.initializationClass(MockExpectationInitializer.class.getName());
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

        String applicationName = "ExternalPump_Position";
        String batchIntervalSeconds = "100";
        String factoryClassName = MockExternalPositionFactory.class.getName();
        String dumpRdd = "false";

        String[] args = new String[] {
                "--appName=" + applicationName,
                "--batchIntervalSeconds=" + batchIntervalSeconds,
                "--factoryClassName=" + factoryClassName,
                "--additionalLogging=false",
                "--testFactoryAbilities=" + MockFactoryAbilities.MDM_DAO_FACTORY_BACKUP + "=src/test/resources/dbbackups/test1",
                "--dumpRdd=" + dumpRdd,
                "--launchTime=" + 100
        };

        ExternalPumpStreamDriver.main(args);
    }
}
