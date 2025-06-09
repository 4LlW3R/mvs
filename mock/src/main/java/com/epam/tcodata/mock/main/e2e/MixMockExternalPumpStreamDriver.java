package com.epam.tcodata.mock.main.e2e;

import com.epam.tcodata.external.pump.driver.ExternalPumpStreamDriver;
import com.epam.tcodata.mock.external.pump.util.misc.MockExpectationInitializer;
import com.epam.tcodata.mock.external.pump.util.misc.RestMockUtil;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.mockserver.configuration.ConfigurationProperties;
import org.mockserver.integration.ClientAndServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.epam.tcodata.mock.main.e2e.DriverUtils.*;

public class MixMockExternalPumpStreamDriver {

    private static final Logger LOGGER = LoggerFactory.getLogger(MixMockExternalPumpStreamDriver.class);

    /**
     * Main starting method for e2e with mocked MiX.
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Options options = prepareOptions();
        options.addOption(createMandatoryOption(BATCH_INTERVAL_SECONDS, BATCH_INTERVAL_SECONDS));
        CommandLine commandLine = parseAsCommandLine(args, options);
        String restDirectory = commandLine.getOptionValue(REST_DIRECTORY);
        MockExpectationInitializer.setBase(restDirectory);
        ConfigurationProperties.initializationClass(MockExpectationInitializer.class.getName());
        try (ClientAndServer mockServer = ClientAndServer.startClientAndServer(RestMockUtil.PORT)) {
            LOGGER.info("Mock rest server is running: {}", mockServer.isRunning());
            ExternalPumpStreamDriver.main(args);
        }
    }

}
