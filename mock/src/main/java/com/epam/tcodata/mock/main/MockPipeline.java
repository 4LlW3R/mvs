package com.epam.tcodata.mock.main;

import com.epam.tcodata.external.pump.driver.ExternalPumpBatchDriver;
import com.epam.tcodata.external.pump.factory.IExternalFactory;
import com.epam.tcodata.internal.pump.driver.InternalPumpStreamDriver;
import com.epam.tcodata.internal.pump.factory.IInternalFactory;
import com.epam.tcodata.internal.pump.handler.IDataHandler;
import com.epam.tcodata.mock.MockFactoryAbilities;
import com.epam.tcodata.mock.external.pump.util.misc.MockExpectationInitializer;
import com.epam.tcodata.mock.external.pump.util.misc.RestMockUtil;
import com.epam.tcodata.mock.secure.storage.dal.factory.impl.MockSecretStorageFactory;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.raw.prepared.etl.Driver;
import com.epam.tcodata.raw.prepared.etl.factory.ISDMFactory;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import org.apache.commons.cli.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.mockserver.configuration.ConfigurationProperties;
import org.mockserver.integration.ClientAndServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MockPipeline {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockPipeline.class);

    private static final int DEFAULT_LAUNCH_FACTOR = 10;

    /**
     * Main entry point for MockPipeline.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        int exitCode = 0;
        SparkSession sparkSession = null;
        try {
            Options options = new Options();
            Option currentMomentOption = addOption(options, true, "currentMoment", "datetime",
                    "moment for which the testing is actual.");
            Option intervalOption = addOption(options, true, "interval", "sec",
                    "batch interval in seconds.");
            Option launchFactorOption = addOption(options, false, "launchFactor", "n",
                    "factor to which interval is multiplied to determine the whole launch time. default: " + DEFAULT_LAUNCH_FACTOR);
            Option entityOption = addOption(options, false, "entity", "type",
                    "entity type, one from these: " + Factories.ENTITY_TYPES);
            Option mockPortOption = addOption(options, false, "mockPort", "port",
                    "port for mock rest server. default: " + RestMockUtil.PORT);
            Option restDirOption = addOption(options, false, "restDir", "dir",
                    "base dir for rest source test data. default: " + MockExpectationInitializer.BASE_STR);
            Option pumpBackupDirOption = addOption(options, false, "pumpBackup", "dir",
                    "backup for PUMP database that will be restored before tests.");
            Option mdmBackupDirOption = addOption(options, false, "mdmBackup", "dir",
                    "backup for MDM database that will be restored before tests.");
            Option speedlayerBackupDirOption = addOption(options, false, "speedlayerBackup", "dir",
                    "backup for SPEEDLAYER database that will be restored before tests.");
            Option secureStoragePathOption = addOption(options, false, "secureStorage", "dir",
                    "content of the mock secure storage.");
            Option configOption = addOption(options, false, "config", "file",
                    "json file that contains parameters for checking output results.");
            Option dumpOption = addOption(options, false, "dumpRddDirectory", "dir",
                    "directory for dumps of data before sending to EventHub.");

            Option helpOption = addOption(options, false, "help", null,
                    "Print this message.");

            CommandLineParser parser = new DefaultParser();
            //---------------------------------------------------------------------------------------------------------------------
            // parsing main options that can lead to exit immediately
            //---------------------------------------------------------------------------------------------------------------------
            CommandLine line = parseLine(parser, options, helpOption, args);
            //---------------------------------------------------------------------------------------------------------------------
            // prepare all parameters
            //---------------------------------------------------------------------------------------------------------------------
            String currentMomentStr = line.getOptionValue(currentMomentOption.getOpt());
            String intervalStr = line.getOptionValue(intervalOption.getOpt());
            String launchFactorStr = line.getOptionValue(launchFactorOption.getOpt(), "" + DEFAULT_LAUNCH_FACTOR);
            String entityStr = line.getOptionValue(entityOption.getOpt());
            String mockPortStr = line.getOptionValue(mockPortOption.getOpt(), "" + RestMockUtil.PORT);
            String restDir = line.getOptionValue(restDirOption.getOpt(), MockExpectationInitializer.BASE_STR);
            String pumpBackupDir = line.getOptionValue(pumpBackupDirOption.getOpt(), "");
            String mdmBackupDir = line.getOptionValue(mdmBackupDirOption.getOpt(), "");
            String speedLayerBackupDir = line.getOptionValue(speedlayerBackupDirOption.getOpt(), "");
            String secureStoragePath = line.getOptionValue(secureStoragePathOption.getOpt(), "");
            String configPath = line.getOptionValue(configOption.getOpt(), "");
            String dumpRddDirectory = line.getOptionValue(dumpOption.getOpt(), "");

            Instant currentMoment = Instant.parse(currentMomentStr);
            int batchInterval = Integer.parseInt(intervalStr);
            int launchFactor = Integer.parseInt(launchFactorStr);
            int mockPort = Integer.parseInt(mockPortStr);

            //---------------------------------------------------------------------------------------------------------------------
            // collect all golden set before the running spark streaming to avoid wasting time in case of error
            //---------------------------------------------------------------------------------------------------------------------
            DataHolder expectedData = new DataHolder(configPath);

            sparkSession = createSparkSession();

            Factories factories = getFactories(sparkSession, currentMoment, pumpBackupDir, mdmBackupDir, speedLayerBackupDir, secureStoragePath, entityStr);

            //---------------------------------------------------------------------------------------------------------------------
            // the main part of work is performed here
            //---------------------------------------------------------------------------------------------------------------------
            DataHolder actualData = proceedAllWork(factories, mockPort, restDir, dumpRddDirectory, batchInterval, batchInterval * launchFactor * 1000);

            //---------------------------------------------------------------------------------------------------------------------
            // compare actual and expected
            //---------------------------------------------------------------------------------------------------------------------
            boolean result = expectedData.compareActualExpected(actualData);
            exitCode = result ? 0 : 1;

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            LOGGER.error("Error: {}", e.getMessage());
            if (sparkSession != null) {
                sparkSession.close();
            }
            exitCode = 2;
        }
        System.exit(exitCode);
    }

    private  static CommandLine parseLine(CommandLineParser parser, Options options, Option helpOption, String[] args) {
        CommandLine line = null;
        try {
            // parse the command line arguments
            line = parser.parse(options, args);
        } catch (ParseException exp) {
            // oops, something went wrong
            LOGGER.error("Incorrect parameters: {}", exp.getMessage());
            printHelp(options);
            System.exit(2);
        }
        if (line.hasOption(helpOption.getOpt())) {
            printHelp(options);
            System.exit(0);
        }
        return line;
    }

    private static Factories getFactories(SparkSession sparkSession, Instant currentMoment, String pumpBackupDir, String mdmBackupDir, String speedlayerBackupDir, String secureStoragePath, String entityStr) throws Exception {
        Map<String, String> initParameters = new HashMap<>();
        if (!secureStoragePath.isEmpty()) {
            initParameters.put(MockFactoryAbilities.SECURE_STORE_BACKUP.name(), secureStoragePath);
        }
        ISecretStorage secretStorage = new MockSecretStorageFactory(initParameters).createSecretStorage(new Properties());

        Factories factories = null;
        if (entityStr == null || entityStr.isEmpty()) {
            factories = Factories.allFactories(sparkSession, secretStorage, currentMoment);
        } else {
            EntityType entityType = EntityType.valueOf(entityStr);
            factories = Factories.factoriesByEntityType(sparkSession, secretStorage, currentMoment, entityType);
        }

        factories.initDatabases(pumpBackupDir, mdmBackupDir, speedlayerBackupDir);
        factories.setInitParameters(initParameters);
        return factories;
    }

    private static DataHolder proceedAllWork(Factories factories,
                                             int mixPort,
                                             String restDir,
                                             String dumpRddDirectory,
                                             int batchInterval,
                                             int launchTime) throws Exception {

        MockExpectationInitializer.setBase(restDir);
        ConfigurationProperties.initializationClass(MockExpectationInitializer.class.getName());

        SparkSession sparkSession = factories.getSparkSession();
        JavaSparkContext sparkContext = new JavaSparkContext(sparkSession.sparkContext());

        List<IExternalFactory> externalFactories = factories.getExternalFactories();
        List<IInternalFactory> internalFactories = factories.getInternalFactories();
        List<ISDMFactory> sdmFactories = factories.getSdmFactories();

        try (ClientAndServer mockServer = ClientAndServer.startClientAndServer(mixPort);
                JavaStreamingContext jsc = new JavaStreamingContext(sparkContext, Durations.seconds(batchInterval))) {

            LOGGER.info("Mock rest server is running: {}", mockServer.isRunning());

            ISecretStorage secretStorage = factories.getSecretStorage();

            for (IExternalFactory externalFactory : externalFactories) {
//                ExternalPumpStreamDriver.handleJavaStreamingContext(jsc, sparkSession, externalFactory, externalFactory.getEntityType(), dumpRddDirectory, false);
                ExternalPumpBatchDriver.handle(sparkContext, sparkSession, externalFactory, externalFactory.getEntityType(), dumpRddDirectory, false);
            }

            for (IInternalFactory internalFactory : internalFactories) {
                IDaoFactory pumpsDaoFactory = internalFactory.createPumpDaoFactory(secretStorage);
                IDataHandler handler = internalFactory.createEventDataHandler(sparkSession);
                InternalPumpStreamDriver.handleJavaStreamContext(jsc, handler, internalFactory, pumpsDaoFactory, secretStorage);
            }
            jsc.start();

            if (launchTime == 0) {
                jsc.awaitTermination();
            } else {
                jsc.awaitTerminationOrTimeout(launchTime);
            }

            for (ISDMFactory sdmFactory : sdmFactories) {
                Driver.tuneFactoriesAndPerformBatch(sdmFactory, sparkSession);
            }

            LOGGER.info("==== before collection data ====");
            return factories.collectDataInto();
        }
    }

    private static SparkSession createSparkSession() {
        String appName = "Pipeline";
        return SparkSession.builder()
                .config(new SparkConf()
                        .setMaster("local[*]")
                        .setAppName(appName))
                .getOrCreate();
    }

    private static Option addOption(Options options, boolean required, String name, String parameter, String description) {
        Option option = new Option(name, name, parameter != null, description);
        if (parameter != null) {
            option.setArgName(parameter);
        }
        option.setRequired(required);
        options.addOption(option);
        return option;
    }

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(
                "java -jar " + MockKeyManagerStarter.class.getSimpleName() + ".jar",
                "Options", options,
                "Exit code:\n"
                        + "   0 : all testcases have passed\n"
                        + "   1 : one or more testcases have failed\n"
                        + "   2 : other errors\n",
                true);
    }


}
