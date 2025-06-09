package com.epam.tcodata.token.manager.util;

import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.Account;
import com.epam.tcodata.token.manager.factory.ITokenManagerFactory;
import com.epam.tcodata.token.manager.service.IMainService;
import org.apache.commons.cli.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DriverUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DriverUtil.class);

    public static final String FACTORY_CLASS_NAME = "factoryClassName";
    public static final String APP_NAME = "appName";
    public static final String BATCH_INTERVAL_SECONDS = "batchIntervalSeconds";
    public static final String LAUNCH_TIME = "launchTime";
    public static final String TEST_FACTORY_ABILITIES = "testFactoryAbilities";
    public static final int MILLIS_IN_SECOND = 1000;
    private DriverUtil(){  /***  Default implementation ***/  }

    /**
     * Creates SparkSession object.
     *
     * @param appName application name.
     * @return SparkSession.
     */
    public static SparkSession getSparkSession(String appName) {
        return SparkSession.builder()
                .config(new SparkConf().setAppName(appName))
                .getOrCreate();
    }

    /**
     * Returns function that handles AccountTokens.
     *
     * @param factory factory.
     * @return {@link VoidFunction}.
     */
    public static VoidFunction<Iterator<Account>> handleAccountTokens(ITokenManagerFactory factory) {
        return rddPartitionIterator -> {
            ISecretStorage secretStorage = factory.createSecretStorage();
            try (IDaoFactory daoFactory = factory.createPumpDaoFactory(secretStorage)) {
                IMainService mainService = factory.createMainService(daoFactory, secretStorage);
                mainService.handle(rddPartitionIterator);
            }
        };
    }

    /**
     * Prepares options for driver.
     *
     * @return Options.
     */
    public static Options prepareOptions() {
        Options options = new Options();
        options.addOption(createMandatoryOption(FACTORY_CLASS_NAME, FACTORY_CLASS_NAME));
        options.addOption(createMandatoryOption(APP_NAME, APP_NAME));
        options.addOption(createNonMandatoryOption(LAUNCH_TIME, LAUNCH_TIME));
        options.addOption(createNonMandatoryOption(TEST_FACTORY_ABILITIES, TEST_FACTORY_ABILITIES));

        return options;
    }

    /**
     * Parses options from command line.
     *
     * @param args    command line arguments.
     * @param options options to to parse command line arguments.
     * @return {@link CommandLine} command line.
     */
    public static CommandLine parseAsCommandLine(String[] args, Options options) {
        try {
            CommandLineParser clParser = new PosixParser();
            return clParser.parse(options, args);
        } catch (ParseException e) {
            String msg = "Error parsing input args.";
            LOGGER.error(msg, e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Creates mandatory option.
     *
     * @param name        option name.
     * @param description description.
     * @return Option.
     */
    public static Option createMandatoryOption(String name, String description) {
        Option option = new Option(name, name, true, description);
        option.setRequired(true);
        return option;
    }

    /**
     * Creates non-mandatory option.
     *
     * @param name        option name.
     * @param description description.
     * @return Option.
     */
    public static Option createNonMandatoryOption(String name, String description) {
        Option option = createMandatoryOption(name, description);
        option.setRequired(false);
        option.setValueSeparator(',');
        option.setArgs(10);
        return option;
    }

    /**
     * Only for tests.
     */
    public static Map<String, String> extractTestFactoryAbilities(CommandLine commandLine) {
        Map<String, String> parameters = new HashMap<>();
        String[] optionValues = commandLine.getOptionValues(TEST_FACTORY_ABILITIES);
        for (String value : optionValues) {
            String[] keyValue = value.split("=");
            String v = null;
            if (keyValue.length > 0) {
                if (keyValue.length > 1) {
                    v = keyValue[1];
                }
                String key = keyValue[0];
                parameters.put(key, v);
            }

        }
        return parameters;
    }
}
