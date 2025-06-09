package com.epam.tcodata.overtaking.violation.stream.datalake;

import com.epam.tcodata.common.FactoryUtil;
import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.hive.dal.domain.prepared.PreparedAreaEntityType;
import com.epam.tcodata.hive.dal.domain.raw.RawAreaEntityType;
import com.epam.tcodata.hive.dal.repository.IHiveRepository;
import com.epam.tcodata.hive.dal.util.HiveCommon;
import com.epam.tcodata.models.ApplicationType;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.avro.util.AvroSerDeUtil;
import com.epam.tcodata.models.datalake.prepared.analytics.PreparedConfirmedOvertakingViolation;
import com.epam.tcodata.models.datalake.raw.fact.RawDetectedEvent;
import com.epam.tcodata.overtaking.violation.stream.datalake.converter.IConfirmedOvertakingConverter;
import com.epam.tcodata.overtaking.violation.stream.datalake.exception.ArgsException;
import com.epam.tcodata.overtaking.violation.stream.datalake.factory.IConfirmedOvertakingFactory;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.HiveOffset;
import com.epam.tcodata.sql.dal.service.pumps.IHiveOffsetService;
import com.epam.tcodata.storage.avro.entities.events.overtaking.otdb.AvroConfirmedOvertakingViolation;
import com.microsoft.azure.eventhubs.EventData;
import org.apache.commons.cli.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;

public class OvertakingViolationStreamDataLakeDriver {

    private static final Logger LOGGER = LoggerFactory.getLogger(OvertakingViolationStreamDataLakeDriver.class);

    private static final String FACTORY_CLASS_NAME = "factoryClassName";
    private static final String APP_NAME = "appName";
    private static final String BATCH_INTERVAL_SECONDS = "batchIntervalSeconds";

    private static final Long EVENT_TYPE_ID = 800L;

    /**
     * Main driver class of OvertakingViolationStreamDatalake job.
     *
     * @param args expected arguments (appName, batchIntervalSeconds, databaseName).
     * @throws InterruptedException for spark context.
     */
    public static void main(String[] args) throws Exception {
        CommandLine commandLine = parseAsCommandLine(args);
        long batchIntervalSeconds = Long.parseLong(commandLine.getOptionValue(BATCH_INTERVAL_SECONDS));
        String appName = commandLine.getOptionValue(APP_NAME);
        String factoryClassName = commandLine.getOptionValue(FACTORY_CLASS_NAME);
        IConfirmedOvertakingFactory factory = FactoryUtil.loadFactory(IConfirmedOvertakingFactory.class, factoryClassName);

        JavaStreamingContext javaStreamingContext = handleJavaStreamingContext(factory, appName, batchIntervalSeconds);
        runPump(javaStreamingContext);
    }

    private static JavaStreamingContext handleJavaStreamingContext(IConfirmedOvertakingFactory factory,
                                                                   String appName,
                                                                   long batchIntervalSeconds) throws Exception {
        SparkSession sparkSession = getSparkSession(appName);
        JavaStreamingContext jsc = createJavaStreamingContext(sparkSession, batchIntervalSeconds);
        ISecretStorage secretStorage = factory.createSecretStorage();
        IEventHub eventHub = factory.createEventHub(secretStorage);

        JavaDStream<EventData> eventDataDStream = eventHub
                .receiveStream(jsc, ApplicationType.OVERTAKING_VIOLATION_STREAM_DATALAKE.getConsumerGroup());
        eventDataDStream
                .foreachRDD(eventDataRDD -> handle(factory, secretStorage, sparkSession, eventDataRDD));
        return jsc;
    }

    private static void handle(IConfirmedOvertakingFactory factory,
                               ISecretStorage secretStorage,
                               SparkSession sparkSession,
                               JavaRDD<EventData> eventDataRDD) throws Exception {

        JavaRDD<AvroConfirmedOvertakingViolation> avroViolationRDD = eventDataRDD
                .map(EventData::getBytes)
                .map(bytes -> AvroSerDeUtil.deserialize(AvroConfirmedOvertakingViolation.class, bytes))
                .persist(StorageLevel.MEMORY_AND_DISK_SER());

        IConfirmedOvertakingConverter converter = factory.createConfirmedOvertakingConverter();
        JavaRDD<PreparedConfirmedOvertakingViolation> violationRDD = avroViolationRDD
                .map(converter::convertToConfirmedOvertakingViolation)
                .coalesce(1)
                .persist(StorageLevel.MEMORY_AND_DISK_SER());
        Timestamp persistedDateUtc = Timestamp.from(Instant.now());
        JavaRDD<RawDetectedEvent> dataLakeEventRDD = avroViolationRDD
                .map(confirmed -> converter.convertToDataLakeEvent(confirmed, EVENT_TYPE_ID, persistedDateUtc))
                .coalesce(1)
                .persist(StorageLevel.MEMORY_AND_DISK_SER());

        avroViolationRDD.unpersist();

        if (!violationRDD.isEmpty()) {
            IHiveRepository confirmedOvertakingRepository = factory.createPreparedHive(sparkSession).repository(PreparedAreaEntityType.CONFIRMED_OVERTAKING_VIOLATION);
            JavaRDD<Row> violationRowRDD = HiveCommon.entityRddToRowRdd(violationRDD);
            Dataset<Row> violationRowDataset = HiveCommon.rowRddToRowDataset(violationRowRDD, PreparedConfirmedOvertakingViolation.class, sparkSession);
            confirmedOvertakingRepository.write(violationRowDataset, SaveMode.Append);
        }
        violationRDD.unpersist();

        if (!dataLakeEventRDD.isEmpty()) {
            IHiveRepository detectedEventRepository = factory.createRawHive(sparkSession).repository(RawAreaEntityType.DETECTED_EVENT);
            JavaRDD<Row> detectedRowRDD = HiveCommon.entityRddToRowRdd(dataLakeEventRDD);
            Dataset<Row> detectedRowDataset = HiveCommon.rowRddToRowDataset(detectedRowRDD, RawDetectedEvent.class, sparkSession);
            detectedEventRepository.write(detectedRowDataset, SaveMode.Append);
            // write hive offset records
            LOGGER.info("Storing in HiveOffset...");
            long count = dataLakeEventRDD.count();
            try (IDaoFactory pumpDaoFactory = factory.createPumpDaoFactory(secretStorage)) {
                IHiveOffsetService hiveOffsetService = IDaoFactory.service(pumpDaoFactory, HiveOffset.class);
                insertHiveOffsetRecord(hiveOffsetService, persistedDateUtc, count);
            }
        }
        dataLakeEventRDD.unpersist();
    }

    private static void insertHiveOffsetRecord(IHiveOffsetService hiveOffsetService, Timestamp persistedDateUtc, long count) {
        HiveOffset hiveOffset = new HiveOffset();
        hiveOffset.setEntityType(EntityType.DETECTED_EVENT.getCode());
        hiveOffset.setPersistedDateUtc(persistedDateUtc);
        hiveOffset.setElementCount(count);
        hiveOffsetService.insert(Collections.singletonList(hiveOffset));
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

    private static CommandLine parseAsCommandLine(String[] args) {
        try {
            Options options = new Options();
            options.addOption(createMandatoryOption(FACTORY_CLASS_NAME, FACTORY_CLASS_NAME));
            options.addOption(createMandatoryOption(APP_NAME, APP_NAME));
            options.addOption(createMandatoryOption(BATCH_INTERVAL_SECONDS, BATCH_INTERVAL_SECONDS));

            CommandLineParser clParser = new PosixParser();
            return clParser.parse(options, args);
        } catch (ParseException e) {
            String msg = "Error parsing input args.";
            LOGGER.error(msg, e);
            throw new ArgsException(msg);
        }
    }

    private static Option createMandatoryOption(String name, String description) {
        Option option = new Option(name, true, description);
        option.setRequired(true);
        return option;
    }

    private static void runPump(JavaStreamingContext jsc) throws InterruptedException {
        jsc.start();
        jsc.awaitTermination();
        jsc.close();
    }
}
