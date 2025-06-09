package com.epam.tcodata.analytics.road.condition.violation.detection;

import com.epam.tcodata.analytics.road.condition.violation.detection.factory.IRoadConditionViolationFactory;
import com.epam.tcodata.analytics.road.condition.violation.detection.handler.DataHandler;
import com.epam.tcodata.common.FactoryUtil;
import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.models.ApplicationType;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.microsoft.azure.eventhubs.EventData;
import org.apache.commons.cli.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

public class RoadConditionViolationDetectionDriver {

    private static final String OPTION_FACTORY_CLASS_NAME = "factoryClassName";
    private static final String OPTION_BATCH_INTERVAL_SECONDS = "batchIntervalSeconds";
    private static final String OPTION_APP_NAME = "appName";

    /**
     * Main point of entry to GPS consuming violations detection job.
     *
     * @param args - batch interval, window and slide lengths.
     */
    public static void main(String[] args) throws Exception {
        CommandLine cmd = parseInputArgs(args);
        String factoryClassName = cmd.getOptionValue(OPTION_FACTORY_CLASS_NAME);
        IRoadConditionViolationFactory factory = FactoryUtil.loadFactory(IRoadConditionViolationFactory.class, factoryClassName);
        long batchIntervalSeconds = Long.parseLong(cmd.getOptionValue(OPTION_BATCH_INTERVAL_SECONDS));
        String appName = cmd.getOptionValue(OPTION_APP_NAME);
        JavaStreamingContext jsc = handleJavaStreamingContext(factory, appName, batchIntervalSeconds);
        runGpsStream(jsc);
    }

    private static JavaStreamingContext handleJavaStreamingContext(IRoadConditionViolationFactory factory,
                                                                   String appName,
                                                                   long batchIntervalSeconds) throws Exception {
        SparkSession sparkSession = getSparkSession(appName);
        JavaStreamingContext jsc = createJavaStreamingContext(sparkSession, batchIntervalSeconds);
        ISecretStorage secretStorage = factory.createSecretStorage();
        IEventHub eventHub = factory.createPositionEventHub(secretStorage);
        JavaDStream<EventData> eventDataDStream = eventHub
                .receiveStream(jsc, ApplicationType.ROAD_CONDITION_VIOLATION_DETECTION.getConsumerGroup());
        DataHandler.handle(eventDataDStream, factory);
        return jsc;
    }

    private static SparkSession getSparkSession(String appName) {
        return SparkSession.builder()
                .config(new SparkConf().setAppName(appName))
                .enableHiveSupport()
                .getOrCreate();
    }

    private static JavaStreamingContext createJavaStreamingContext(SparkSession sparkSession, long batchIntervalSeconds) {
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkSession.sparkContext());
        return new JavaStreamingContext(javaSparkContext, Durations.seconds(batchIntervalSeconds));
    }

    /**
     * Parse input arguments.
     *
     * @param args input arguments.
     * @return command line.
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

    private static void runGpsStream(JavaStreamingContext jsc) throws InterruptedException {
        jsc.start();
        jsc.awaitTermination();
        jsc.close();
    }
}
