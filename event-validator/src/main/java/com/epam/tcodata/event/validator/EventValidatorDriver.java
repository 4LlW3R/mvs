package com.epam.tcodata.event.validator;


import com.epam.tcodata.common.FactoryUtil;
import com.epam.tcodata.event.validator.converter.IEventConverter;
import com.epam.tcodata.event.validator.domain.EnrichedPreparedEvent;
import com.epam.tcodata.event.validator.exception.ArgsException;
import com.epam.tcodata.event.validator.factory.IEventValidatorFactory;
import com.epam.tcodata.event.validator.logic.EventAnalyzer;
import com.epam.tcodata.event.validator.logic.IRuleChain;
import com.epam.tcodata.event.validator.logic.SpeedValues;
import com.epam.tcodata.event.validator.logic.complex.ComplexDetector;
import com.epam.tcodata.event.validator.logic.problem.vehicle.ProblemVehicleRuleFactory;
import com.epam.tcodata.event.validator.logic.validation.status.ValidationStatusRuleFactory;
import com.epam.tcodata.hive.dal.IHive;
import com.epam.tcodata.hive.dal.domain.IHiveEntityType;
import com.epam.tcodata.hive.dal.repository.IHiveRepository;
import com.epam.tcodata.hive.dal.util.HiveCommon;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.datalake.prepared.analytics.PreparedValidatedEvent;
import com.epam.tcodata.models.datalake.prepared.dimension.PreparedEventDescription;
import com.epam.tcodata.models.datalake.prepared.fact.PreparedEvent;
import com.epam.tcodata.models.datalake.raw.RawEntity;
import com.epam.tcodata.models.datalake.raw.fact.RawEvent;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.HiveOffset;
import com.epam.tcodata.sql.dal.service.pumps.IHiveOffsetService;
import org.apache.commons.cli.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;
import org.apache.spark.storage.StorageLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;


public class EventValidatorDriver {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventValidatorDriver.class);

    private static final String APP_NAME = "appName";
    private static final String FACTORY_CLASS_NAME = "factoryClassName";
    private static final String HELP = "help";

    /**
     * Run event validator.
     *
     * @param args expected arguments
     */
    public static void main(String[] args) throws Exception {
        LOGGER.info("Parsing and Loading parameters...");
        CommandLine commandLine = parseAsCommandLine(args);

        String factoryClassName = commandLine.getOptionValue(FACTORY_CLASS_NAME);
        IEventValidatorFactory eventValidatorFactory =
                FactoryUtil.loadFactory(IEventValidatorFactory.class, factoryClassName);

        ISecretStorage secretStorage = eventValidatorFactory.createSecretStorage();

        try (IDaoFactory pumpDaoFactory = eventValidatorFactory.createPumpDaoFactory(secretStorage)) {
            SparkSession sparkSession = getSparkSession(commandLine.getOptionValue(APP_NAME));

            IHiveOffsetService hiveOffsetService = IDaoFactory.service(pumpDaoFactory, HiveOffset.class);
            List<HiveOffset> hiveOffsets = getHiveOffsets(hiveOffsetService);

            if (!hiveOffsets.isEmpty()) {
                tryBlock(eventValidatorFactory, sparkSession, hiveOffsets, hiveOffsetService);
            } else {
                LOGGER.warn("Can't find unhandled batches.");
            }
        } catch (Exception e) {
            String msg = "Exception during runtime.";
            LOGGER.error(msg, e);
        }
    }
    private static void tryBlock(IEventValidatorFactory eventValidatorFactory, SparkSession sparkSession,
                                 List<HiveOffset> hiveOffsets, IHiveOffsetService hiveOffsetService) {
        try {
            IHive preparedHive = eventValidatorFactory.createPreparedHive(sparkSession);
            Dataset<PreparedEventDescription> preparedEventDescriptionDataset = getPreparedEventDescription(preparedHive)
                    .persist(StorageLevel.MEMORY_AND_DISK());
            Map<String, String> eventDescriptions = convertToMap(preparedEventDescriptionDataset);

            //get first 1000 offsets (restriction)
            hiveOffsets = hiveOffsets.stream().sorted(Comparator.comparing(HiveOffset::getPersistedDateUtc)).limit(1000).collect(Collectors.toList());

            Dataset<PreparedEvent> preparedEventDataset = receivePreparedEvents(
                    hiveOffsets,
                    sparkSession,
                    preparedHive);

            applyRules(preparedEventDataset, eventValidatorFactory, sparkSession, preparedHive, eventDescriptions);
            Timestamp currentTime = Timestamp.from(Instant.now());
            hiveOffsets.forEach(hiveOffset -> hiveOffset.setValidatedDateUtc(currentTime));
            hiveOffsetService.update(hiveOffsets);

            preparedEventDescriptionDataset.unpersist();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private static List<HiveOffset> getHiveOffsets(IHiveOffsetService hiveOffsetService) {
        Map<String, Object> filter = new HashMap<>();
        filter.put(HiveOffset.Fields.ENTITY_TYPE, EntityType.EVENT.getCode());
        filter.put(HiveOffset.Fields.VALIDATED_DATE_UTC, null);
        return hiveOffsetService.readFiltered(filter)
                .stream()
                .filter(hiveOffset -> hiveOffset.getPreparedDateUtc() != null)
                .collect(Collectors.toList());
    }

    private static Dataset<PreparedEventDescription> getPreparedEventDescription(IHive hive) {
        IHiveEntityType eventDescriptionEntityType = hive.databaseConfig().entityTypeByEntity(PreparedEventDescription.class, false);
        IHiveRepository<PreparedEventDescription> eventDescriptionRepository = hive.repository(eventDescriptionEntityType);
        return HiveCommon.rowDatasetToEntityDataset(
                eventDescriptionRepository.read(),
                PreparedEventDescription.class,
                hive.getSparkSession())
                .filter((FilterFunction<PreparedEventDescription>) preparedEventDescription ->
                        EventAnalyzer.isHandledDescription(preparedEventDescription.getDescription()));
    }

    private static Map<String, String> convertToMap(Dataset<PreparedEventDescription> preparedEventDescriptionDataset) {
        return new HashSet<>(preparedEventDescriptionDataset.collectAsList())
                .stream()
                .collect(Collectors.toMap(
                        PreparedEventDescription::getDurableId,
                        PreparedEventDescription::getDescription));
    }

    private static Dataset<PreparedEvent> receivePreparedEvents(
            List<HiveOffset> hiveOffsets,
            SparkSession sparkSession,
            IHive preparedHive) {
        Timestamp minPersistedDateUtc = hiveOffsets.stream().map(HiveOffset::getPersistedDateUtc).min(Timestamp::compareTo).get();
        Timestamp maxPersistedDateUtc = hiveOffsets.stream().map(HiveOffset::getPersistedDateUtc).max(Timestamp::compareTo).get();

        IHiveEntityType eventEntityType = preparedHive.databaseConfig().entityTypeByEntity(PreparedEvent.class, false);
        IHiveRepository<PreparedEvent> eventRepository = preparedHive.repository(eventEntityType);

        Column persistedDateUtcColumn = new Column(RawEntity.Fields.PERSISTED_DATE_UTC);
        Column yearColumn = new Column(RawEvent.Fields.YEAR);
        Column weekNumberColumn = new Column(RawEvent.Fields.WEEK_NUMBER);

        Column condition = persistedDateUtcColumn.$greater$eq(minPersistedDateUtc)
                .and(persistedDateUtcColumn.$less$eq(maxPersistedDateUtc))
                .and(yearColumn.$greater$eq(minPersistedDateUtc.toLocalDateTime().getYear()))
                .and(yearColumn.$less$eq(maxPersistedDateUtc.toLocalDateTime().getYear()))
                .and(weekNumberColumn.$greater$eq(minPersistedDateUtc.toLocalDateTime().get(WeekFields.ISO.weekOfWeekBasedYear())))
                .and(weekNumberColumn.$less$eq(maxPersistedDateUtc.toLocalDateTime().get(WeekFields.ISO.weekOfWeekBasedYear())));

        return HiveCommon.rowDatasetToEntityDataset(
                eventRepository.read(condition),
                PreparedEvent.class,
                sparkSession)
                .persist(StorageLevel.MEMORY_AND_DISK());
    }

    private static void applyRules(
            Dataset<PreparedEvent> preparedEventDataset,
            IEventValidatorFactory eventValidatorFactory,
            SparkSession sparkSession,
            IHive preparedHive,
            Map<String, String> eventDescriptions) {
        IEventConverter eventConverter = eventValidatorFactory.createConverter();

        Dataset<EnrichedPreparedEvent> enrichedPreparedEventDataset = convertToEnriched(preparedEventDataset, eventConverter);

        //apply rules 1
        Dataset<EnrichedPreparedEvent> analyzedEventDataset = applyRules(enrichedPreparedEventDataset, eventDescriptions);

        //apply rules 2
        Dataset<EnrichedPreparedEvent> complexAnalyzedEventDataset = new ComplexDetector().detect(analyzedEventDataset);

        Dataset<PreparedValidatedEvent> preparedValidatedEventDataset =
                convertToValidated(complexAnalyzedEventDataset, eventConverter);

        Dataset<Row> prepared = HiveCommon.entityDatasetToRowDataset(
                preparedValidatedEventDataset,
                PreparedValidatedEvent.class,
                sparkSession);
        write(prepared, preparedHive);

        preparedEventDataset.unpersist();
    }

    private static Dataset<EnrichedPreparedEvent> convertToEnriched(Dataset<PreparedEvent> preparedEventDataset,
                                                                    IEventConverter eventConverter) {

        return preparedEventDataset.map((MapFunction<PreparedEvent, EnrichedPreparedEvent>) event ->
                eventConverter.convertToEnriched(event), Encoders.bean(EnrichedPreparedEvent.class));
    }

    private static Dataset<EnrichedPreparedEvent> applyRules(
            Dataset<EnrichedPreparedEvent> enrichedPreparedEventDataset,
            Map<String, String> eventDescriptions) {

        return enrichedPreparedEventDataset
                .map((MapFunction<EnrichedPreparedEvent, EnrichedPreparedEvent>) enrichedValidatedEvent -> {

                    String eventTypeDurableKey = enrichedValidatedEvent.getEventTypeDurableKey(); //to do null!?
                    String description = eventDescriptions.get(eventTypeDurableKey);

                    Integer velocity = enrichedValidatedEvent.getStartPositionSpeedKilometresPerHour() == null ? null : enrichedValidatedEvent.getStartPositionSpeedKilometresPerHour().intValue(); // to do double to int
                    Integer speed = enrichedValidatedEvent.getValue() == null ? null : enrichedValidatedEvent.getValue().intValue();
                    SpeedValues speedValues = new SpeedValues(
                            velocity == null ? -1 : velocity,
                            speed == null ? -1 : speed,
                            description);

                    IRuleChain problemVehicleCodeAnalyzer = new ProblemVehicleRuleFactory(speedValues).createProblemVehicleCodeAnalyzer();
                    IRuleChain validationStatusAnalyzer = new ValidationStatusRuleFactory(speedValues).createValidationStatusAnalyzer();

                    enrichedValidatedEvent.setValidationCode(validationStatusAnalyzer.apply());
                    enrichedValidatedEvent.setProblemVehicle(problemVehicleCodeAnalyzer.apply());

                    return enrichedValidatedEvent;
                }, Encoders.bean(EnrichedPreparedEvent.class));
    }

    private static Dataset<PreparedValidatedEvent> convertToValidated(Dataset<EnrichedPreparedEvent> complexAnalyzedEventDataset,
                                                                      IEventConverter eventConverter) {

        return complexAnalyzedEventDataset.map((MapFunction<EnrichedPreparedEvent, PreparedValidatedEvent>) event ->
                eventConverter.convertToPreparedValidated(event), Encoders.bean(PreparedValidatedEvent.class));
    }

    private static void write(Dataset<Row> prepared, IHive hive) {
        IHiveEntityType validatedEventEntityType = hive.databaseConfig().entityTypeByEntity(PreparedValidatedEvent.class, false);
        IHiveRepository<PreparedValidatedEvent> validatedEventRepository = hive.repository(validatedEventEntityType);
        validatedEventRepository.write(prepared.coalesce(3), SaveMode.Append);
    }

    private static SparkSession getSparkSession(String appName) {
        return SparkSession
                .builder()
                .config(getSparkConf(appName))
                .config("spark.sql.hive.metastore.version", "2.3.9")
                .config("spark.sql.hive.metastore.jars", "builtin")
                .config("hive.metastore.schema.verification", "false")
                .config("hive.metastore.schema.verification.record.version", "false")
                .config("hive.exec.dynamic.partition.mode", "nonstrict")
                .config("hive.exec.dynamic.partition", "true")
                .enableHiveSupport()
                .getOrCreate();
    }

    private static SparkConf getSparkConf(String appName) {
        return new SparkConf()
                .setAppName(appName);
    }

    private static CommandLine parseAsCommandLine(String[] args) {
        Options options = new Options();
        try {
            options.addOption(createMandatoryOption(APP_NAME, APP_NAME));
            options.addOption(createMandatoryOption(FACTORY_CLASS_NAME, FACTORY_CLASS_NAME));
            options.addOption(new Option(HELP, false, HELP));

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

    private static Option createMandatoryOption(String name, String description) {
        Option option = new Option(name, true, description);
        option.setRequired(true);
        return option;
    }

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(
                "java -jar " + EventValidatorDriver.class.getSimpleName() + ".jar",
                "Options", options,
                "\n",
                true);
    }

}
