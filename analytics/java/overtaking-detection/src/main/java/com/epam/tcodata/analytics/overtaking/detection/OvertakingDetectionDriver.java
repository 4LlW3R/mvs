package com.epam.tcodata.analytics.overtaking.detection;

import com.epam.tcodata.analytics.overtaking.detection.drivers.IDriver;
import com.epam.tcodata.analytics.overtaking.detection.drivers.OvertakingDetectionHistoricalDriver;
import com.epam.tcodata.analytics.overtaking.detection.drivers.OvertakingDetectionStreamDriver;
import com.epam.tcodata.analytics.overtaking.detection.drivers.OvertakingDetectionTestDriver;
import com.epam.tcodata.analytics.overtaking.detection.factory.IOvertakingDetectionFactory;
import com.epam.tcodata.common.FactoryUtil;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.epam.tcodata.analytics.overtaking.detection.RunMode.getModeFromCmd;

public class OvertakingDetectionDriver {

    private static final Logger LOGGER = LoggerFactory.getLogger(OvertakingDetectionDriver.class);

    private static final String OPTION_FACTORY_CLASS_NAME = "factoryClassName";
    private static final String OPTION_RUN_MODE = "mode";

    /**
     * Entry point to Overtaking Detection job.
     */
    public static void main(String[] args) throws Exception {
        CommandLine commandLine = parseInputArgs(args);
        String factoryClassName = commandLine.getOptionValue(OPTION_FACTORY_CLASS_NAME);
        IOvertakingDetectionFactory factory = FactoryUtil.loadFactory(IOvertakingDetectionFactory.class, factoryClassName);
        RunMode mode = getModeFromCmd(commandLine.getOptionValue(OPTION_RUN_MODE));
        LOGGER.info("Running in {} mode...", mode.getMode());
        mode.getDriver().runJob(commandLine, factory);
    }

    /**
     * Parse input arguments.
     *
     * @param args input arguments.
     * @return command line.
     */
    private static CommandLine parseInputArgs(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption(createMandatoryOption(IDriver.OPTION_APP_NAME, IDriver.OPTION_APP_NAME));
        options.addOption(createMandatoryOption(OPTION_FACTORY_CLASS_NAME, OPTION_FACTORY_CLASS_NAME));
        options.addOption(createMandatoryOption(OPTION_RUN_MODE, OPTION_RUN_MODE));
        options.addOption(createOptionalOption(OvertakingDetectionStreamDriver.OPTION_BATCH_INTERVAL_SECONDS, OvertakingDetectionStreamDriver.OPTION_BATCH_INTERVAL_SECONDS));
        options.addOption(createOptionalOption(OvertakingDetectionTestDriver.OPTION_INPUT_CSV_FILE_PATH, OvertakingDetectionTestDriver.OPTION_INPUT_CSV_FILE_PATH));
        options.addOption(createOptionalOption(OvertakingDetectionTestDriver.OPTION_OUTPUT_CSV_FILE_PATH, OvertakingDetectionTestDriver.OPTION_OUTPUT_CSV_FILE_PATH));
        options.addOption(createOptionalOption(OvertakingDetectionHistoricalDriver.OPTION_TIMESTAMP_FROM, OvertakingDetectionHistoricalDriver.OPTION_TIMESTAMP_FROM));
        options.addOption(createOptionalOption(OvertakingDetectionHistoricalDriver.OPTION_TIMESTAMP_TO, OvertakingDetectionHistoricalDriver.OPTION_TIMESTAMP_TO));

        CommandLineParser clParser = new DefaultParser();
        return clParser.parse(options, args);
    }

    private static Option createMandatoryOption(String name, String description) {
        Option option = new Option(name, name, true, description);
        option.setRequired(true);
        return option;
    }

    private static Option createOptionalOption(String name, String description) {
        Option option = new Option(name, name, true, description);
        option.setRequired(false);
        return option;
    }
}
