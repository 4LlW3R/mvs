package com.epam.tcodata.external.pump.driver;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.junit.Test;

import static com.epam.tcodata.external.pump.util.DriverUtils.*;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;

public class ExternalPumpDriverTest {

    private static final String APP_NAME = "appName";
    private static final String BATCH_INTERVAL_SECONDS = "batchIntervalSeconds";
    private static final String FACTORY_CLASS_NAME = "factoryClassName";
    private static final String ADDITIONAL_LOGGING = "additionalLogging";
    private static final String LAUNCH_TIME = "launchTime";
    private static final String DUMP_RDD_DIRECTORY = "dumpRddDirectory";
    private static final String APP_NAME_VALUE = "app_name";
    private static final String APP_NAME_KEY = "--appName";
    private static final String BATCH_INTERVAL_SECONDS_VALUE = "100";
    private static final String BATCH_INTERVAL_SECONDS_KEY = "--batchIntervalSeconds";
    private static final String FACTORY_CLASS_NAME_VALUE = "factory_class_name";
    private static final String FACTORY_CLASS_NAME_KEY = "--factoryClassName";
    private static final String ADDITIONAL_LOGGING_VALUE = "false";
    private static final String DUMP_RDD_DIRECTORY_VALUE = "dump_path";
    private static final String LAUNCH_TIME_VALUE = "100";

    @Test
    public void testParseAsCommandLineAllArgsFilled_parsingSuccessful() {
        String[] args = new String[]{APP_NAME_KEY, APP_NAME_VALUE,
                BATCH_INTERVAL_SECONDS_KEY, BATCH_INTERVAL_SECONDS_VALUE,
                FACTORY_CLASS_NAME_KEY, FACTORY_CLASS_NAME_VALUE,
                "--additionalLogging", ADDITIONAL_LOGGING_VALUE,
                "--dumpRddDirectory", DUMP_RDD_DIRECTORY_VALUE,
                "--launchTime", LAUNCH_TIME_VALUE};
        Options options = prepareOptions();
        options.addOption(createMandatoryOption(BATCH_INTERVAL_SECONDS, BATCH_INTERVAL_SECONDS));
        CommandLine commandLine = parseAsCommandLine(args, options);
        assertEquals(APP_NAME_VALUE, commandLine.getOptionValue(APP_NAME));
        assertEquals(BATCH_INTERVAL_SECONDS_VALUE, commandLine.getOptionValue(BATCH_INTERVAL_SECONDS));
        assertEquals(FACTORY_CLASS_NAME_VALUE, commandLine.getOptionValue(FACTORY_CLASS_NAME));
        assertEquals(ADDITIONAL_LOGGING_VALUE, commandLine.getOptionValue(ADDITIONAL_LOGGING));
        assertEquals(DUMP_RDD_DIRECTORY_VALUE, commandLine.getOptionValue(DUMP_RDD_DIRECTORY));
        assertEquals(LAUNCH_TIME_VALUE, commandLine.getOptionValue(LAUNCH_TIME));
    }

    @Test
    public void testParseAsCommandLineMandatoryArgsFilled_parsingSuccessful() {
        String[] args = new String[]{APP_NAME_KEY, APP_NAME_VALUE,
                BATCH_INTERVAL_SECONDS_KEY, BATCH_INTERVAL_SECONDS_VALUE,
                FACTORY_CLASS_NAME_KEY, FACTORY_CLASS_NAME_VALUE,
                "--additionalLogging", ADDITIONAL_LOGGING_VALUE};
        Options options = prepareOptions();
        options.addOption(createMandatoryOption(BATCH_INTERVAL_SECONDS, BATCH_INTERVAL_SECONDS));
        CommandLine commandLine = parseAsCommandLine(args, options);
        assertEquals(APP_NAME_VALUE, commandLine.getOptionValue(APP_NAME));
        assertEquals(BATCH_INTERVAL_SECONDS_VALUE, commandLine.getOptionValue(BATCH_INTERVAL_SECONDS));
        assertEquals(FACTORY_CLASS_NAME_VALUE, commandLine.getOptionValue(FACTORY_CLASS_NAME));
        assertEquals(ADDITIONAL_LOGGING_VALUE, commandLine.getOptionValue(ADDITIONAL_LOGGING));
        assertFalse(commandLine.hasOption(DUMP_RDD_DIRECTORY));
        assertFalse(commandLine.hasOption(LAUNCH_TIME));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseAsCommandLineMandatoryArgsNotFilled_parsingWithException() {
        String[] args = new String[]{APP_NAME_KEY, APP_NAME_VALUE,
                BATCH_INTERVAL_SECONDS_KEY, BATCH_INTERVAL_SECONDS_VALUE,
                FACTORY_CLASS_NAME_KEY, FACTORY_CLASS_NAME_VALUE};
        Options options = prepareOptions();
        options.addOption(createMandatoryOption(BATCH_INTERVAL_SECONDS, BATCH_INTERVAL_SECONDS));
        parseAsCommandLine(args, prepareOptions());
    }
}
