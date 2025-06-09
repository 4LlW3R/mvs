package com.epam.tcodata.redis.manager;

import com.epam.tcodata.common.FactoryUtil;
import com.epam.tcodata.hive.dal.IHive;
import com.epam.tcodata.hive.dal.domain.raw.RawAreaEntityType;
import com.epam.tcodata.hive.dal.repository.IHiveRepository;
import com.epam.tcodata.hive.dal.util.HiveCommon;
import com.epam.tcodata.models.datalake.raw.RawEntity;
import com.epam.tcodata.models.datalake.raw.dimension.RawAsset;
import com.epam.tcodata.models.datalake.raw.dimension.RawDriver;
import com.epam.tcodata.redis.dal.IRedis;
import com.epam.tcodata.redis.manager.factory.IRedisFailoverManagerFactory;
import com.epam.tcodata.redis.manager.util.RedisUtil;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import org.apache.commons.cli.*;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.ForeachPartitionFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Spark Driver Job responsible for uploading data from Azure Data Lake into Redis.
 * Entities:
 * * Drivers:[Name]
 * * Vehicles:[Registration Number]
 */
public class RedisFailoverManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisFailoverManager.class);

    /**
     * Main method invoking business logic.
     *
     * @param args list of command line arguments
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

        IRedisFailoverManagerFactory factory = FactoryUtil.loadFactory(IRedisFailoverManagerFactory.class, factoryClassName);
        SparkSession sparkSession = RedisUtil.getSparkSession(RedisFailoverManager.class.getName(), appName);

        IHive rawHive = factory.createRawHive(sparkSession);
        IHiveRepository<RawDriver> rawDriverRepository = rawHive.repository(RawAreaEntityType.DRIVER_NORM);
        IHiveRepository<RawAsset> rawAssetRepository = rawHive.repository(RawAreaEntityType.ASSET_NORM);

        //---------------------------------------------------------------------------------------------------------------------
        // perform transformations
        //---------------------------------------------------------------------------------------------------------------------
        Dataset<Row> driverRowDataset = rawDriverRepository
                .read()
                .repartition(sparkSession.sparkContext().getConf().getInt("redis.connections", 10));

        Dataset<Row> assetRowDataset = rawAssetRepository
                .read()
                .repartition(sparkSession.sparkContext().getConf().getInt("redis.connections", 10));

        JavaRDD<RawDriver> rawDriverJavaRDD = HiveCommon.rowRddToEntityRdd(driverRowDataset.rdd().toJavaRDD(), RawDriver.class);
        Dataset<RawDriver> driverDataset = HiveCommon.entityRddToEntityDataset(rawDriverJavaRDD, RawDriver.class, sparkSession);

        JavaRDD<RawAsset> rawAssetJavaRDD = HiveCommon.rowRddToEntityRdd(assetRowDataset.rdd().toJavaRDD(), RawAsset.class);
        Dataset<RawAsset> assetDataset = HiveCommon.entityRddToEntityDataset(rawAssetJavaRDD, RawAsset.class, sparkSession);

        uploadData(factory, driverDataset, true);
        uploadData(factory, assetDataset, false);
    }

    /**
     * Method responsible for uploading entity data to Azure DataLake.
     *
     * @param factory       instance of {@link IRedisFailoverManagerFactory}
     * @param entityDataset Dataset of {@link RawDriver}
     * @param saveToeDriver  true, to save to driver Redis database, false - to Vehicle database
     */
    private static <T extends RawEntity> void uploadData(IRedisFailoverManagerFactory factory,
                                                         Dataset<T> entityDataset,
                                                         boolean saveToeDriver) {
        LOGGER.info("Amount of records: {}", entityDataset.count());
        entityDataset
                .foreachPartition((ForeachPartitionFunction<T>) iterator -> {
                    ISecretStorage secretStorage = factory.createSecretStorage();
                    IRedis redis = saveToeDriver ? factory.createDriverRedis(secretStorage) : factory.createVehicleRedis(secretStorage);
                    RedisUtil.pushObjectToRedis(
                            iterator,
                            redis);
                });
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
                "java -jar " + RedisFailoverManager.class.getSimpleName() + ".jar",
                "Options", options,
                "Exit code:\n"
                        + "   0 : job successfully have passed\n"
                        + "   1 : job has failed\n",
                true);
    }
}
