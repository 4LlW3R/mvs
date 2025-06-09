package com.epam.tcodata.raw.prepared.etl;

import com.epam.tcodata.common.FactoryUtil;
import com.epam.tcodata.hive.dal.IHive;
import com.epam.tcodata.hive.dal.domain.IHiveEntityType;
import com.epam.tcodata.hive.dal.repository.IHiveRepository;
import com.epam.tcodata.hive.dal.util.HiveCommon;
import com.epam.tcodata.models.EntitySuperType;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.datalake.prepared.PreparedEntity;
import com.epam.tcodata.models.datalake.raw.RawEntity;
import com.epam.tcodata.models.datalake.raw.fact.RawPosition;
import com.epam.tcodata.raw.prepared.etl.converter.ISingleDomainModelConverter;
import com.epam.tcodata.raw.prepared.etl.factory.ISDMFactory;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.HiveOffset;
import com.epam.tcodata.sql.dal.service.pumps.IHiveOffsetService;
import org.apache.commons.cli.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.*;
import org.apache.spark.storage.StorageLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.WeekFields;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Driver {
    private static final Logger LOGGER = LoggerFactory.getLogger(Driver.class);

//    dim_asset
//    dim_asset_norm                -> dim_vehicle
//    dim_driver
//    dim_driver_norm               -> dim_driver
//    dim_library_event
//    dim_library_event_norm        -> dim_event_description
//    dim_location
//    dim_location_norm             -> dim_location
//    dim_organisation_group
//    dim_organisation_group_norm   -> dim_group
//
//    fact_event                    -> fact_event
//                                  -> fact_event_video
//    fact_position                 -> fact_position
//    fact_subtrip                  -> fact_subtrip
//    fact_trip                     -> fact_trip
//    fact_tacho                    -> fact_tacho


    /**
     * Main entry point for Driver.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) throws Exception {
        Options options = new Options();
        Option factoryClassNameOption = addOption(options, true, "factoryClassName", "type",
                "Factory class name");

        Option appNameOption = addOption(options, true, "appName", "type",
                "App name");

        Option helpOption = addOption(options, false, "help", null,
                "Print this message.");

        CommandLineParser parser = new PosixParser();
        CommandLine line;

        //---------------------------------------------------------------------------------------------------------------------
        // parsing main options that can lead to exit immediately
        //---------------------------------------------------------------------------------------------------------------------
        try {
            // parse the command line arguments
            line = parser.parse(options, args);
        } catch (ParseException exp) {
            // oops, something went wrong
            System.out.println("Incorrect parameters: " + exp.getMessage());
            printHelp(options);
            System.exit(2);
            return;
        }

        if (line.hasOption(helpOption.getOpt())) {
            printHelp(options);
            System.exit(0);
        }

        //---------------------------------------------------------------------------------------------------------------------
        // prepare all parameters
        //---------------------------------------------------------------------------------------------------------------------
        String factoryClassName = line.getOptionValue(factoryClassNameOption.getOpt());
        String appName = line.getOptionValue(appNameOption.getOpt());

        ISDMFactory sdmFactory = FactoryUtil.loadFactory(ISDMFactory.class, factoryClassName);


        //---------------------------------------------------------------------------------------------------------------------
        // the main part of work is performed here
        //---------------------------------------------------------------------------------------------------------------------
        SparkSession sparkSession = SparkSession.builder()
                .config(new SparkConf().setAppName(appName))
                .config("spark.sql.hive.metastore.version", "2.3.9")
                .config("spark.sql.hive.metastore.jars", "builtin")
                .config("hive.metastore.schema.verification", "false")
                .config("hive.metastore.schema.verification.record.version", "false")
                .config("hive.exec.dynamic.partition", "true")
                .config("hive.exec.dynamic.partition.mode", "nonstrict")
                .enableHiveSupport()
                .getOrCreate();

        LOGGER.info("spark context created");

        tuneFactoriesAndPerformBatch(sdmFactory, sparkSession);
    }

    /**
     * This is a main function that does all work. It is public because we need to test the Driver from Mock module.
     *
     * @param sdmFactory   instance of ISingleDomainModuleFactory.
     * @param sparkSession Spark session.
     * @param <T>          class for raw area entity.
     * @param <U>          class for prepared area entity.
     * @throws Exception
     */
    public static <T extends RawEntity, U extends PreparedEntity> void tuneFactoriesAndPerformBatch(ISDMFactory<T, U> sdmFactory,
                                                                                                    SparkSession sparkSession) throws Exception {
        try (IDaoFactory pumpDaoFactory = sdmFactory.createPumpDaoFactory(sdmFactory.createSecretStorage())) {

            IHive rawHive = sdmFactory.createRawHive(sparkSession);
            IHive preparedHive = sdmFactory.createPreparedHive(sparkSession);

            EntityType entityType = sdmFactory.getEntityType();
            // let's use normalized entities for dimensions
            boolean norm = entityType.getSuperType() == EntitySuperType.DIMENSION;

            Class<T> rawEntityClass = sdmFactory.getRawEntityClass();
            Class<U> preparedEntityClass = sdmFactory.getPreparedEntityClass();

            IHiveEntityType rawEntityType = rawHive.databaseConfig().entityTypeByEntity(rawEntityClass, norm);
            IHiveRepository<T> rawRepository = rawHive.repository(rawEntityType);

            IHiveEntityType preparedEntityType = preparedHive.databaseConfig().entityTypeByEntity(preparedEntityClass, false);
            IHiveRepository<U> preparedRepository = preparedHive.repository(preparedEntityType);

            IHiveOffsetService service = IDaoFactory.service(pumpDaoFactory, HiveOffset.class);

            ReferenceSupplier referenceSupplier = new ReferenceSupplier(rawHive);
            LOGGER.info("ReferenceSupplier: " +  referenceSupplier);
            ISingleDomainModelConverter<T, U> converter = sdmFactory.createConverter(referenceSupplier);

            performBatch(entityType, converter, preparedEntityClass, rawRepository, preparedRepository, service, sparkSession);
        }
    }

    private static <T extends RawEntity, U extends PreparedEntity> void performBatch(EntityType entityType,
                                                                                     ISingleDomainModelConverter<T, U> converter,
                                                                                     Class<U> preparedEntityClass,
                                                                                     IHiveRepository<T> rawRepository,
                                                                                     IHiveRepository<U> preparedRepository,
                                                                                     IHiveOffsetService offsetService,
                                                                                     SparkSession sparkSession) {
        Class<T> entityClass = (Class<T>) entityType.getRawDataLakeClass();
        Dataset<Row> rawDataSet;

        if (entityType.getSuperType() == EntitySuperType.FACT) {
            Map<String, Object> filter = new HashMap<>();
            filter.put(HiveOffset.Fields.ENTITY_TYPE, entityType.getCode());
            filter.put(HiveOffset.Fields.PREPARED_DATE_UTC, null);
            List<HiveOffset> hiveOffsets = offsetService.readFiltered(filter);
            if (!hiveOffsets.isEmpty()) {
                try {
                    //get first 1000 offsets (restriction)
                    hiveOffsets = hiveOffsets.stream().sorted(Comparator.comparing(HiveOffset::getPersistedDateUtc)).limit(1000).collect(Collectors.toList());
                    Timestamp minPersistedDateUtc = hiveOffsets.stream().map(HiveOffset::getPersistedDateUtc).min(Timestamp::compareTo).get();
                    Timestamp maxPersistedDateUtc = hiveOffsets.stream().map(HiveOffset::getPersistedDateUtc).max(Timestamp::compareTo).get();
                    Column persistedDateUtcColumn = new Column(RawEntity.Fields.PERSISTED_DATE_UTC);
                    Column condition = persistedDateUtcColumn.$greater$eq(minPersistedDateUtc)
                            .and(persistedDateUtcColumn.$less$eq(maxPersistedDateUtc));
                    Column yearColumn = new Column(RawPosition.Fields.YEAR);
                    Column weekNumberColumn = new Column(RawPosition.Fields.WEEK_NUMBER);
                    condition = condition.and(yearColumn.$greater$eq(minPersistedDateUtc.toLocalDateTime().getYear()))
                            .and(yearColumn.$less$eq(maxPersistedDateUtc.toLocalDateTime().getYear()))
                            .and(weekNumberColumn.$greater$eq(minPersistedDateUtc.toLocalDateTime().get(WeekFields.ISO.weekOfWeekBasedYear())))
                            .and(weekNumberColumn.$less$eq(maxPersistedDateUtc.toLocalDateTime().get(WeekFields.ISO.weekOfWeekBasedYear())));

                    rawDataSet = rawRepository.read(condition);
                    JavaRDD<T> rawEntityRDD = HiveCommon.rowRddToEntityRdd(rawDataSet.javaRDD(), entityClass);
                    JavaRDD<T> rawEntityPersistedRDD = rawEntityRDD.persist(StorageLevel.MEMORY_ONLY());
                    JavaRDD<U> preparedEntityRDD = converter.convertRDD(rawEntityPersistedRDD);
                    JavaRDD<Row> preparedRowRDD = HiveCommon.entityRddToRowRdd(preparedEntityRDD);
                    Dataset<Row> preparedDataset = HiveCommon.rowRddToRowDataset(preparedRowRDD, preparedEntityClass, sparkSession);
                    preparedRepository.write(preparedDataset, SaveMode.Append);

                    Timestamp currentTime = Timestamp.from(Instant.now());
                    hiveOffsets.forEach(hiveOffset -> hiveOffset.setPreparedDateUtc(currentTime));
                    offsetService.update(hiveOffsets);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            } else {
                LOGGER.warn("Can't find unhandled batches for facts.");
            }
        } else {
            try {
                rawDataSet = rawRepository.read();

                JavaRDD<T> rawEntityRDD = HiveCommon.rowRddToEntityRdd(rawDataSet.javaRDD(), entityClass);
                JavaRDD<T> rawEntityPersistedRDD = rawEntityRDD.persist(StorageLevel.MEMORY_ONLY());
                JavaRDD<U> preparedEntityRDD = converter.convertRDD(rawEntityPersistedRDD);
                JavaRDD<Row> preparedRowRDD = HiveCommon.entityRddToRowRdd(preparedEntityRDD);
                Dataset<Row> preparedDataset = HiveCommon.rowRddToRowDataset(preparedRowRDD, preparedEntityClass, sparkSession);

                preparedRepository.makeBackup(preparedRepository.getHiveEntityType().tableName() + "_temp");
                preparedRepository.write(preparedDataset, SaveMode.Overwrite);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }

    private static Option addOption(Options options, boolean required, String name, String parameter, String description) {
        Option option = new Option(name, name, parameter != null, description);
        if (parameter != null) {
            option.setArgName(parameter);
        }
        option.setRequired(required);
        options.addOption(option);
        return option;
    }

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(
                "java -jar " + Driver.class.getSimpleName() + ".jar",
                "Options", options,
                "Exit code:\n"
                        + "   0 : job successfully have passed\n"
                        + "   1 : job has failed\n",
                true);
    }
}
