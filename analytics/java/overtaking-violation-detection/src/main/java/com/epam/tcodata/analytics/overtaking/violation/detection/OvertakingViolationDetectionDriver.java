package com.epam.tcodata.analytics.overtaking.violation.detection;

import com.epam.tcodata.analytics.overtaking.violation.detection.driver.OvertakingViolationDetectionStreamDriver;
import com.epam.tcodata.analytics.overtaking.violation.detection.factory.IOvertakingViolationFactory;
import com.epam.tcodata.common.FactoryUtil;
import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.models.ApplicationType;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.microsoft.azure.eventhubs.EventData;
import org.apache.commons.cli.*;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OvertakingViolationDetectionDriver {

    private static final Logger LOGGER = LoggerFactory.getLogger(OvertakingViolationDetectionDriver.class);

    private static final String OPTION_FACTORY_CLASS_NAME = "factoryClassName";
    private static final String OPTION_APP_NAME = "appName";
    private static final String OPTION_BATCH_INTERVAL_SECONDS = "batchIntervalSeconds";

    /**
     * Entry point to Overtaking Violation Detection job.
     */
    public static void main(String[] args) throws Exception {
        CommandLine commandLine = parseInputArgs(args);
        String factoryClassName = commandLine.getOptionValue(OPTION_FACTORY_CLASS_NAME);
        IOvertakingViolationFactory factory = FactoryUtil.loadFactory(IOvertakingViolationFactory.class, factoryClassName);
        JavaStreamingContext jsc = createStreamingContext(commandLine);
        ISecretStorage secretStorage = factory.createSecretStorage();
        IEventHub overtakingEventHub = factory.createOvertakingEventHub(secretStorage);
        JavaDStream<EventData> overtakingEventDataDStream = overtakingEventHub.receiveStream(
                jsc, ApplicationType.OVERTAKING_VIOLATION_DETECTION.getConsumerGroup());
        OvertakingViolationDetectionStreamDriver.handleOvertakingEventDataDStream(factory, overtakingEventDataDStream);
        runStream(jsc);
    }

    /**
     * Parse input arguments.
     *
     * @param args input arguments.
     * @return command line.
     * @throws ParseException parse exception.
     */
    private static CommandLine parseInputArgs(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption(createMandatoryOption(OPTION_FACTORY_CLASS_NAME, OPTION_FACTORY_CLASS_NAME));
        options.addOption(createMandatoryOption(OPTION_APP_NAME, OPTION_APP_NAME));
        options.addOption(createMandatoryOption(OPTION_BATCH_INTERVAL_SECONDS, OPTION_BATCH_INTERVAL_SECONDS));

        CommandLineParser clParser = new DefaultParser();
        return clParser.parse(options, args);
    }

    private static Option createMandatoryOption(String name, String description) {
        Option option = new Option(name, name, true, description);
        option.setRequired(true);
        return option;
    }

    private static JavaStreamingContext createStreamingContext(CommandLine commandLine) {
        LOGGER.info("Input params {} {} {}", (Object[]) commandLine.getArgs());

        long batchIntervalSeconds = Long.parseLong(commandLine.getOptionValue(OPTION_BATCH_INTERVAL_SECONDS));
        SparkConf conf = new SparkConf()
                .setAppName(commandLine.getOptionValue(OPTION_APP_NAME));
        return new JavaStreamingContext(conf, Durations.seconds(batchIntervalSeconds));
    }

    private static void runStream(JavaStreamingContext jsc) throws InterruptedException {
        jsc.start();
        jsc.awaitTermination();
        jsc.close();
    }
}
