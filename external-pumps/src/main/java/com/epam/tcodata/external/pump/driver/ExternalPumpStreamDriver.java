package com.epam.tcodata.external.pump.driver;

import com.epam.tcodata.common.FactoryUtil;
import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.dto.DtoInputStream;
import com.epam.tcodata.external.pump.dto.maker.IDtoMaker;
import com.epam.tcodata.external.pump.factory.IExternalFactory;
import com.epam.tcodata.external.pump.source.IMixSource;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.SignalType;
import com.epam.tcodata.models.mix.Entity;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.Signal;
import com.epam.tcodata.sql.dal.service.pumps.ISignalService;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.util.LongAccumulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.reflect.ClassManifestFactory$;
import scala.reflect.ClassTag;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static com.epam.tcodata.external.pump.util.DriverUtils.*;

public class ExternalPumpStreamDriver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalPumpStreamDriver.class);

    private static volatile boolean stopSignalReceived = false;

    /**
     * Rest Pump Drivers.
     *
     * @param args expected arguments (appName, batchIntervalSeconds, factoryClassName)
     * @throws InterruptedException for spark context.
     */
    public static void main(String[] args) throws Exception {
        Options options = prepareOptions();
        options.addOption(createMandatoryOption(BATCH_INTERVAL_SECONDS, BATCH_INTERVAL_SECONDS));
        CommandLine commandLine = parseAsCommandLine(args, options);

        LOGGER.info("CommandLine options");
        for (Option option : commandLine.getOptions()) {
            LOGGER.info("{} - {}", option.getOpt(), option.getValue());
        }
        String appName = commandLine.getOptionValue(APP_NAME);
        String batchIntervalStr = commandLine.getOptionValue(BATCH_INTERVAL_SECONDS);
        String factoryClassName = commandLine.getOptionValue(FACTORY_CLASS_NAME);
        String dumpRddDirectory = null;
        if (commandLine.hasOption(DUMP_RDD_DIRECTORY)) {
            dumpRddDirectory = commandLine.getOptionValue(DUMP_RDD_DIRECTORY);
        }
        String additionalLoggingStr = commandLine.getOptionValue(ADDITIONAL_LOGGING);

        IExternalFactory externalFactory = FactoryUtil.loadFactory(IExternalFactory.class, factoryClassName);

        if (commandLine.hasOption(CURRENT_MOMENT)) {
            externalFactory.setCurrentMoment(Instant.parse(commandLine.getOptionValue(CURRENT_MOMENT)));
        }

        int launchTime = 0;
        if (commandLine.hasOption(LAUNCH_TIME)) {
            launchTime = MILLIS_IN_SECOND * Integer.parseInt(commandLine.getOptionValue(LAUNCH_TIME));
        }
        if (commandLine.hasOption(TEST_FACTORY_ABILITIES)) {
            Map<String, String> parameters = extractTestFactoryAbilities(commandLine);
            externalFactory.setInitParameters(parameters);
        }

        SparkSession sparkSession = getSparkSession(appName);
        JavaSparkContext sparkContext = new JavaSparkContext(sparkSession.sparkContext());

        int batchInterval = Integer.parseInt(batchIntervalStr);
        boolean additionalLogging = Boolean.parseBoolean(additionalLoggingStr);

        try (JavaStreamingContext jsc = new JavaStreamingContext(sparkContext, Durations.seconds(batchInterval))) {
            handleJavaStreamingContext(jsc, sparkSession, externalFactory, externalFactory.getEntityType(), dumpRddDirectory, additionalLogging);
            jsc.start();
            if (launchTime == 0) {
                jsc.awaitTermination();
            } else {
                jsc.awaitTerminationOrTimeout(launchTime);
            }
        }
    }

    /**
     * Creates JavaStreamingContext and defines operation on it's DStream.
     */
    public static <T extends Entity> void handleJavaStreamingContext(JavaStreamingContext jsc,
                                                                     SparkSession sparkSession,
                                                                     IExternalFactory factory,
                                                                     EntityType entityType,
                                                                     String dumpRddDirectory,
                                                                     boolean additionalLogging) throws Exception {
        JavaInputDStream<AbstractDto<T>> dtoDStream;
        ISecretStorage driverSecretStorage = factory.createSecretStorage();
        try (IDaoFactory daoFactory = factory.createPumpDaoFactory(driverSecretStorage)) {
            IDtoMaker<T> dtoMaker = factory.createDtoMaker(daoFactory, sparkSession);
            dtoDStream = createJavaInputDStream(jsc, factory, dtoMaker);
        }

        LongAccumulator mixRequestCount = jsc.sparkContext().sc().longAccumulator("MIX REQUEST COUNT");
        LongAccumulator mixSuccessfulRequest = jsc.sparkContext().sc().longAccumulator("MIX SUCCESSFUL REQUEST COUNT");
        LongAccumulator mixReceivedRecords = jsc.sparkContext().sc().longAccumulator("MIX RECEIVED RECORDS");
        LongAccumulator preparedToSendToEHRecords = jsc.sparkContext().sc().longAccumulator("PREPARED_TO_SEND_TO_EH_RECORDS");

        String driverHost = SparkSession.builder().getOrCreate().sparkContext().getConf().get("spark.driver.host");

        dtoDStream.foreachRDD(dtoRDD -> {
            ISecretStorage secretStorage = factory.createSecretStorage();
            try (IDaoFactory pumpsDaoFactory = factory.createPumpDaoFactory(secretStorage)) {

                IDtoMaker dtoMaker = factory.createDtoMaker(pumpsDaoFactory, sparkSession);
                JavaRDD<AbstractDto<T>> preparedRDD = dtoMaker.fillNonStaticInfo(dtoRDD, entityType, factory);

                IMixSource mixSource = factory.createMixSource();
                JavaRDD<AbstractDto<T>> mixRDD = preparedRDD.mapPartitions(requestData(mixSource,
                        driverHost,
                        factory,
                        mixRequestCount,
                        mixSuccessfulRequest,
                        mixReceivedRecords));

                JavaRDD<AbstractDto<T>> enrichedRDD = mixRDD.mapPartitions(enrichDto(factory));

                if (shouldHandleBatch(jsc, pumpsDaoFactory, factory.getEntityType())) {
                    handleEnrichedDto(enrichedRDD,
                            factory,
                            dumpRddDirectory,
                            sparkSession,
                            entityType,
                            additionalLogging,
                            preparedToSendToEHRecords);
                }
            }
        });
    }

    /**
     * Creates custom input DStream by paralleling DTOs, containing information which is needed
     * to make request to MIX REST API.
     *
     * @param jsc      javaStreamingContext.
     * @param factory  factory.
     * @param dtoMaker dtoMaker.
     * @return Input DStream.
     */
    private static <T extends Entity> JavaInputDStream<AbstractDto<T>> createJavaInputDStream(JavaStreamingContext jsc,
                                                                                              IExternalFactory factory,
                                                                                              IDtoMaker dtoMaker) {
        ClassTag<AbstractDto> dtoClassTag = ClassManifestFactory$.MODULE$.fromClass(AbstractDto.class);
        DtoInputStream<T> dtoInputStream = factory.createInputStream(jsc, dtoClassTag, dtoMaker);
        return new JavaInputDStream(dtoInputStream, dtoClassTag);
    }

    private static boolean shouldHandleBatch(JavaStreamingContext jsc, IDaoFactory pumpsDaoFactory, EntityType entityType) {
        boolean result = true;

        if (stopSignalReceived) {
            LOGGER.info("Stopping immediately...");
            jsc.stop(true, false);
            result = false;
        } else {
            ISignalService signalService = IDaoFactory.service(pumpsDaoFactory, Signal.class);
            List<Signal> signals = receiveSignals(signalService, entityType);
            LOGGER.info("Received signals: " + signals);
            if (!signals.isEmpty()) {
                signalService.deleteAll(signals);
                switch (SignalType.byCode(signals.get(0).getSignalType())) {
                    case STOP:
                        LOGGER.info("Stop signal received. Stopping immediately...");
                        jsc.stop(true, false);
                        result = false;
                        break;
                    case ONE_BATCH_STOP:
                        LOGGER.info("One batch stop signal received. One more batch...");
                        stopSignalReceived = true;
                        break;
                    default:
                        LOGGER.error("Wrong signal received...");
                        break;
                }
            }
        }

        return result;
    }
}
