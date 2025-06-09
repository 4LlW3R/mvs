package com.epam.tcodata.external.pump.util;

import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.factory.IExternalFactory;
import com.epam.tcodata.external.pump.service.IOffsetService;
import com.epam.tcodata.external.pump.source.IMixSource;
import com.epam.tcodata.mdm.IKeyFactory;
import com.epam.tcodata.mdm.IKeyManager;
import com.epam.tcodata.models.ApplicationType;
import com.epam.tcodata.models.EntitySuperType;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.avro.util.AvroSerDeUtil;
import com.epam.tcodata.models.enriched.IEnrichable;
import com.epam.tcodata.models.enriched.fact.EnrichedSubTrip;
import com.epam.tcodata.models.enriched.fact.EnrichedTrip;
import com.epam.tcodata.models.mix.Entity;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.mdm.KeyManagerVersion;
import com.epam.tcodata.sql.dal.domain.pumps.Signal;
import com.epam.tcodata.sql.dal.service.pumps.ISignalService;
import com.microsoft.azure.eventhubs.EventData;
import org.apache.avro.specific.SpecificRecord;
import org.apache.commons.cli.*;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.http.HttpStatus;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.util.LongAccumulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DriverUtils {

    private DriverUtils(){
        /***  Default conmstructor ***/
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(DriverUtils.class);

    public static final String APP_NAME = "appName";
    public static final String BATCH_INTERVAL_SECONDS = "batchIntervalSeconds";
    public static final String FACTORY_CLASS_NAME = "factoryClassName";
    public static final String TEST_FACTORY_ABILITIES = "testFactoryAbilities";
    public static final String ADDITIONAL_LOGGING = "additionalLogging";
    public static final String REST_DIRECTORY = "restDirectory";
    public static final String CURRENT_MOMENT = "currentMoment";
    public static final String LAUNCH_TIME = "launchTime";
    public static final String DUMP_RDD_DIRECTORY = "dumpRddDirectory";
    public static final int MILLIS_IN_SECOND = 1000;

    /**
     * Enriches dto with additional info.
     *
     * @param dto        dto.
     * @param converter  converter.
     * @param keyManager key manager.
     */
    public static <T extends Entity> void enrichDto(AbstractDto<T> dto,
                                                    IConverter converter,
                                                    IKeyManager keyManager) {
        dto.setEnrichedEntityList(
                dto.getEntityList().stream()
                        .map(entity -> converter.convertToEnriched(entity, dto, keyManager))
                        .collect(Collectors.toList()));
    }

    /**
     * Converts dto into EventData that can be sent to Event Hub.
     *
     * @param converter converter.
     * @param dtoList   list of dto.
     * @return list of EventData.
     */
    public static List<EventData> getEventDataBatch(IConverter converter, List<AbstractDto> dtoList) {
        return dtoList.stream()
                .map(AbstractDto::getEnrichedEntityList)
                .flatMap(enrichedList -> {
                    Stream<SpecificRecord> recordBaseStream =
                            enrichedList.stream().map((Function<IEnrichable, SpecificRecord>) converter::convertToAvro);
                    return recordBaseStream.map(AvroSerDeUtil::serialize)
                            .map(EventData::create);
                })
                .collect(Collectors.toList());
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
     * Receives all signals from SQL for the given EntityType.
     *
     * @param signalService service provided operations for signals.
     * @param entityType    EntityType.
     * @return list of Signal.
     */
    public static List<Signal> receiveSignals(ISignalService signalService, EntityType entityType) {
        Map<String, Object> signalFilter = new HashMap<>();
        signalFilter.put(Signal.Fields.APPLICATION_TYPE, ApplicationType.EXTERNAL_PUMP.getCode());
        signalFilter.put(Signal.Fields.ENTITY_TYPE, entityType.getCode());
        return signalService.readFiltered(signalFilter);
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

    /**
     * Only for tests.
     */
    public static <T extends Entity> void dumpRdd(SparkSession sparkSession, JavaRDD<AbstractDto<T>> dtoRdd, IConverter converter,
                                                  EntityType entityType, String dumpRddDirectory) throws Exception {
        JavaRDD<IEnrichable> enrichedEntityRdd = dtoRdd
                .map(AbstractDto::getEnrichedEntityList)
                .flatMap(List::iterator);
        Dataset<Row> entityDataset = converter.convertToDataset(sparkSession, enrichedEntityRdd);
        String appName = sparkSession.sparkContext().appName();
        entityDataset
                .coalesce(1)
                .write()
                .option("header", "true")
                .mode(SaveMode.Append)
                .csv(dumpRddDirectory
                        + File.separator + appName);

        if (entityType == EntityType.TRIP) {
            JavaRDD<EnrichedSubTrip> enrichedSubTripRDD = enrichedEntityRdd
                    .flatMap(enriched -> ((EnrichedTrip) enriched).getEnrichedSubTripList().iterator());
            if (!enrichedSubTripRDD.isEmpty()) {
                IConverter subTripConverter = IExternalFactory.createConverter(EntityType.SUBTRIP);
                Dataset<Row> subTripDataset = subTripConverter.convertToDataset(sparkSession, enrichedSubTripRDD);
                subTripDataset
                        .coalesce(1)
                        .write()
                        .option("header", "true")
                        .mode(SaveMode.Append)
                        .csv(dumpRddDirectory
                                + File.separator + appName.replace("Trip", "SubTrip"));
            }
        }
    }

    /**
     * Only for monitoring.
     *
     * @param enrichedDtoList           List of DTOs with enriched entities.
     * @param preparedToSendToEHRecords Accumulator which counts records prepared to be sent to EH.
     */
    public static void logDurableIds(List<AbstractDto> enrichedDtoList, LongAccumulator preparedToSendToEHRecords) {
        List<String> durableIdList = enrichedDtoList.stream()
                .flatMap((Function<AbstractDto, Stream<IEnrichable>>) dto -> {
                    preparedToSendToEHRecords.add(dto.getEnrichedEntityList().size());
                    return dto.getEnrichedEntityList().stream();
                })
                .map(IEnrichable::getDurableId)
                .collect(Collectors.toList());
        LOGGER.info("#output-EH# {}", durableIdList);
    }

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
        options.addOption(createNonMandatoryOption(REST_DIRECTORY, "Directory to take data from if MiX is mocked. Used in e2e testing only."));
        options.addOption(createNonMandatoryOption(CURRENT_MOMENT, "Current moment for e2e mix mock pipeline. Used in e2e testing only."));
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
     * Returns function that requests the data from MIX.
     *
     * @param mixSource            MIX source object.
     * @param driverHost           driver host address.
     * @param factory              factory.
     * @param mixRequestCount      mixRequestCount accumulator.
     * @param mixSuccessfulRequest mixSuccessfulRequest accumulator.
     * @param mixReceivedRecords   mixReceivedRecords accumulator.
     * @return FlatMapFunction.
     */
    public static <T extends Entity> FlatMapFunction<Iterator<AbstractDto<T>>, AbstractDto<T>> requestData(
            IMixSource mixSource,
            String driverHost,
            IExternalFactory factory,
            LongAccumulator mixRequestCount,
            LongAccumulator mixSuccessfulRequest,
            LongAccumulator mixReceivedRecords) {

        return prepDtoIterator -> {
            List<AbstractDto<T>> prepDtoList = IteratorUtils.toList(prepDtoIterator);
            mixSource.requestDataAndFillDto(prepDtoList, driverHost, factory.getCurrentMoment());
            return prepDtoList.stream()
                    .peek(dto -> {
                        if (dto.getLastSyncResultCode() == HttpStatus.SC_OK
                                || dto.getLastSyncResultCode() == HttpStatus.SC_NO_CONTENT
                                || dto.getLastSyncResultCode() == HttpStatus.SC_PARTIAL_CONTENT) {
                            mixSuccessfulRequest.add(1);
                        }
                        mixRequestCount.add(1);
                        mixReceivedRecords.add(dto.getLastSyncElementCount());
                    })
                    .iterator();
        };
    }

    /**
     * Returns function that enriches dto by key manager.
     *
     * @param factory factory.
     * @return FlatMapFunction.
     */
    public static <T extends Entity> FlatMapFunction<Iterator<AbstractDto<T>>, AbstractDto<T>> enrichDto(
            IExternalFactory factory) {

        return mixDtoIterator -> {
            List<AbstractDto<T>> mixDtoList = IteratorUtils.toList(mixDtoIterator);
            IConverter converter = factory.createConverter();
            IKeyFactory keyFactory = factory.createKeyFactory();
            ISecretStorage secretStorage = factory.createSecretStorage();
            try (IKeyManager keyManager = keyFactory.createKeyManager(KeyManagerVersion.VERSION_1_0, secretStorage)) {
                return mixDtoList.stream()
                        .peek(dto -> {
                            List<T> entityList = dto.getEntityList();
                            LOGGER.debug("#mdm# count of dto before enrichment: {}",
                                    entityList == null ? "empty list" : entityList.size());
                            enrichDto(dto, converter, keyManager);
                        })
                        .iterator();
            }
        };
    }

    /**
     * Handles given rdd.
     *
     * @param enrichedDtoRdd            rdd.
     * @param factory                   factory.
     * @param dumpRddDirectory          dump Rdd directory.
     * @param sparkSession              SparkSession object.
     * @param entityType                EntityType.
     * @param additionalLogging         additionalLogging flag.
     * @param preparedToSendToEHRecords preparedToSendToEHRecords accumulator.
     */
    public static <T extends Entity> void handleEnrichedDto(JavaRDD<AbstractDto<T>> enrichedDtoRdd,
                                                            IExternalFactory factory,
                                                            String dumpRddDirectory,
                                                            SparkSession sparkSession,
                                                            EntityType entityType,
                                                            Boolean additionalLogging,
                                                            LongAccumulator preparedToSendToEHRecords) throws Exception {

        JavaRDD<AbstractDto<T>> persistedEnrichedDtoRdd = enrichedDtoRdd.persist(StorageLevel.MEMORY_ONLY_SER());
        IConverter converter = factory.createConverter();

        // only for tests
        if (dumpRddDirectory != null) {
            dumpRdd(sparkSession, persistedEnrichedDtoRdd, converter, entityType, dumpRddDirectory);
        }
        // only for tests

        persistedEnrichedDtoRdd.foreachPartition(enrichedDtoRddIterator -> {
            ISecretStorage theSameSecretStorage = factory.createSecretStorage();
            List<AbstractDto> enrichedDtoList = IteratorUtils.toList(enrichedDtoRddIterator);

            // only for monitoring
            if (additionalLogging) {
                logDurableIds(enrichedDtoList, preparedToSendToEHRecords);
            }
            // only for monitoring

            List<EventData> bytesBatchList = getEventDataBatch(converter, enrichedDtoList);
            IEventHub eventHub = factory.createEventHub(theSameSecretStorage);
            long startTime = System.currentTimeMillis();
            eventHub.send(bytesBatchList);
            long endTime = System.currentTimeMillis();
            LOGGER.info("#EH-send-time# entity type: {} time: {} s ", entityType, (endTime - startTime) / 1000);
            if (entityType.getSuperType() == EntitySuperType.FACT) {
                try (IDaoFactory daoFactory = factory.createPumpDaoFactory(theSameSecretStorage)) {
                    IOffsetService offsetService = factory.createOffsetService(daoFactory);
                    offsetService.updateOffsets(enrichedDtoList);
                }
            }
        });
    }
}
