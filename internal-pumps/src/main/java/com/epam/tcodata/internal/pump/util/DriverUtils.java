package com.epam.tcodata.internal.pump.util;

import com.epam.tcodata.internal.pump.exception.ArgsException;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.sql.dal.domain.pumps.Signal;
import com.epam.tcodata.sql.dal.service.pumps.ISignalService;
import org.apache.commons.cli.*;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epam.tcodata.models.ApplicationType.INTERNAL_PUMP;

public class DriverUtils {
    private DriverUtils() {
        /***  Default conmstructor ***/
    }

    public static final Logger LOGGER = LoggerFactory.getLogger(DriverUtils.class);

    public static final String BATCH_INTERVAL_SECONDS = "batchIntervalSeconds";
    public static final String FACTORY_CLASS_NAME = "factoryClassName";
    public static final String HELP = "help";

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
     * Creates SparkSession object.
     *
     * @param appName application name.
     * @return SparkSession.
     */
    public static SparkSession getSparkSession(String appName) {
        return SparkSession.builder()
                .config(getSparkConf(appName))
                .config("spark.sql.hive.metastore.version", "2.3.9")
                .config("spark.sql.hive.metastore.jars", "builtin")
                .config("hive.metastore.schema.verification", "false")
                .config("hive.metastore.schema.verification.record.version", "false")
                .config("hive.exec.dynamic.partition", "true")
                .config("hive.exec.dynamic.partition.mode", "nonstrict")
                .enableHiveSupport()
                .getOrCreate();
    }

    private static SparkConf getSparkConf(String appName) {
        return new SparkConf()
                .setAppName(appName)
                .set("spark.sql.catalogImplementation", "hive");
    }

    /**
     * Receives signals for this entity type.
     *
     * @param signalService signal service.
     * @param entityType    entity type
     * @return List of signals.
     */
    public static List<Signal> receiveSignals(ISignalService signalService, EntityType entityType) {
        Map<String, Object> signalFilter = new HashMap<>();
        signalFilter.put(Signal.Fields.APPLICATION_TYPE, INTERNAL_PUMP.getCode());
        signalFilter.put(Signal.Fields.ENTITY_TYPE, entityType.getCode());
        return signalService.readFiltered(signalFilter);
    }

    /**
     * Prepares options for pump.
     *
     * @return Options.
     */
    public static Options prepareOptions() {
        Options options = new Options();

        options.addOption(createMandatoryOption(FACTORY_CLASS_NAME, FACTORY_CLASS_NAME));
        options.addOption(new Option(HELP, false, HELP));
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
            CommandLine commandLine = clParser.parse(options, args);

            if (commandLine.hasOption(HELP)) {
                printHelp(options);
                System.exit(1);
            }
            return commandLine;
        } catch (ParseException e) {
            String msg = "Error parsing input args. Args: " + Arrays.toString(args);
            printHelp(options);

            LOGGER.error(msg, e);
            throw new ArgsException(msg);
        }
    }

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(
                "java -jar InternalPumpDriver.jar",
                "Options", options,
                "\n",
                true);
    }
}
