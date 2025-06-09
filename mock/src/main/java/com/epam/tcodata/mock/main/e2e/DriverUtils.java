package com.epam.tcodata.mock.main.e2e;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DriverUtils {
    private DriverUtils() {}

    private static final Logger LOGGER = LoggerFactory.getLogger(DriverUtils.class);

    public static final String APP_NAME = "appName";
    public static final String BATCH_INTERVAL_SECONDS = "batchIntervalSeconds";
    public static final String FACTORY_CLASS_NAME = "factoryClassName";
    public static final String TEST_FACTORY_ABILITIES = "testFactoryAbilities";
    public static final String ADDITIONAL_LOGGING = "additionalLogging";
    public static final String LAUNCH_TIME = "launchTime";
    public static final String DUMP_RDD_DIRECTORY = "dumpRddDirectory";
    public static final String REST_DIRECTORY = "restDirectory";
    public static final String CURRENT_MOMENT = "currentMoment";

    /**
     * Prepares options for pump.
     *
     * @return Options.
     */
    public static Options prepareOptions() {
        Options options = new Options();

        options.addOption(createMandatoryOption(APP_NAME, APP_NAME));
        options.addOption(createMandatoryOption(FACTORY_CLASS_NAME, "Class name of the pump driver factory"));
        options.addOption(createMandatoryOption(ADDITIONAL_LOGGING, ADDITIONAL_LOGGING));
        options.addOption(createMandatoryOption(REST_DIRECTORY, "Directory to take data from if MiX is mocked. Used in e2e testing only."));
        options.addOption(createMandatoryOption(CURRENT_MOMENT, "Current moment for e2e mix mock pipeline. Used in e2e testing only."));
        options.addOption(createNonMandatoryOption(DUMP_RDD_DIRECTORY, "Directory for dumping data before sending to EH (only e2e use it)"));
        options.addOption(createNonMandatoryOption(LAUNCH_TIME, "Launch time in sec. Use it for testing only."));

        Option testAbilitiesOption = createNonMandatoryOption(TEST_FACTORY_ABILITIES,
                "Additional parameters for MockFactory. See the MockFactoryAbilities enum.");
        testAbilitiesOption.setArgs(10); // no more then 10 abilities at the moment
        options.addOption(testAbilitiesOption);
        return options;
    }

    /**
     * Parses command line args.
     *
     * @param args    command line args.
     * @param options options.
     * @return CommandLine.
     */
    public static CommandLine parseAsCommandLine(String[] args, Options options) {
        try {
            CommandLineParser clParser = new PosixParser();
            return clParser.parse(options, args);
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(
                    "java -jar ExternalPumpDriver.jar",
                    "Options", options, "");

            String msg = "Error parsing input args.";
            LOGGER.error(msg, e);
            throw new IllegalArgumentException(msg);
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
        Option option = new Option(name, true, description);
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
        Option option = new Option(name, true, description);
        option.setRequired(false);
        option.setValueSeparator(',');
        return option;
    }

}
