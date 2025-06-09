package com.epam.tcodata.redis.manager;

import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.models.ApplicationType;
import com.epam.tcodata.models.avro.dimension.AvroAsset;
import com.epam.tcodata.models.avro.util.AvroSerDeUtil;
import com.epam.tcodata.models.datalake.raw.dimension.RawAsset;
import com.epam.tcodata.redis.dal.IRedis;
import com.epam.tcodata.redis.dal.RedisConfig;
import com.epam.tcodata.redis.dal.impl.Redis;
import com.epam.tcodata.redis.manager.converter.AssetConverter;
import com.epam.tcodata.redis.manager.factory.IRedisFailoverManagerFactory;
import com.epam.tcodata.redis.manager.factory.impl.RedisFailoverManagerFactory;
import com.epam.tcodata.redis.manager.util.RedisUtil;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.factory.ISecretStorageFactory;
import com.microsoft.azure.eventhubs.EventData;
import org.apache.commons.cli.*;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;


/**
 * Spark Driver Job responsible for uploading Vehicle Data from EventHub into Redis.
 */
@SuppressWarnings("CPD-START")
@Deprecated
public class RedisAssetStreamDriver {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisAssetStreamDriver.class);

    private static final String APP_NAME = "appName";
    private static final String BATCH_INTERVAL_SECONDS = "batchIntervalSeconds";

    /**
     * Main method invoking business logic.
     *
     * @param args list of CMD arguments
     */
    public static void main(String[] args) throws InterruptedException {
        CommandLine commandLine = parseAsCommandLine(args);
        String appName = commandLine.getOptionValue(APP_NAME);
        int batchInterval = Integer.parseInt(commandLine.getOptionValue(BATCH_INTERVAL_SECONDS));

        JavaSparkContext sparkContext = new JavaSparkContext(
                RedisUtil.getSparkSession(RedisDriverStreamDriver.class.getName(), appName).sparkContext());
        try (JavaStreamingContext jsc = new JavaStreamingContext(sparkContext, Durations.seconds(batchInterval))) {
            IRedisFailoverManagerFactory factory = new RedisFailoverManagerFactory();
            ISecretStorage secretStorage = factory.createSecretStorage();
            IEventHub eventHub = factory.createdAssetEventHub(secretStorage);
            JavaDStream<EventData> eventDataDStream = eventHub.receiveStream(jsc,
                    ApplicationType.REDIS_MANAGER.getConsumerGroup());
            JavaDStream<RawAsset> dataLakeAssetDStream = eventDataDStream
                    .map(EventData::getBytes)
                    .map(bytes -> AvroSerDeUtil.deserialize(AvroAsset.class, bytes))
                    .map(AssetConverter::convertToEnriched)
                    .map(AssetConverter::convertToDataLake);
            handleAssetStream(dataLakeAssetDStream);
            jsc.start();
            jsc.awaitTermination();
        }
    }

    /**
     * Stream handler for vehicles.
     *
     * @param dataLakeAssetDStream DStream object, which defines the process for each batch
     * @return Object
     */
    private static Object handleAssetStream(JavaDStream<RawAsset> dataLakeAssetDStream) {
        dataLakeAssetDStream
                .repartition(RedisConfig.getConnections())
                .foreachRDD(rdd -> rdd
                        .foreachPartition(partition -> {
                            ISecretStorageFactory defaultFactory = ISecretStorageFactory.createDefaultFactory();
                            ISecretStorage secretStorage = defaultFactory.createSecretStorage(new Properties());
                            IRedis redis = new Redis(RedisConfig.VEHICLE, secretStorage);
                            RedisUtil.pushObjectToRedis(
                                    partition,
                                    redis);
                        }));
        return null;
    }

    private static CommandLine parseAsCommandLine(String[] args) {
        Options options = new Options();
        try {
            options.addOption(createMandatoryOption(APP_NAME, APP_NAME));
            options.addOption(createMandatoryOption(BATCH_INTERVAL_SECONDS, BATCH_INTERVAL_SECONDS));
            CommandLineParser clParser = new PosixParser();
            return clParser.parse(options, args);
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(
                    "java -jar " + RedisDriverStreamDriver.class.getSimpleName() + ".jar",
                    "Options", options, "");
            String msg = "Error parsing input args.";
            LOGGER.error(msg, e);
            throw new IllegalArgumentException(msg);
        }
    }

    private static Option createMandatoryOption(String name, String description) {
        Option option = new Option(name, true, description);
        option.setRequired(true);
        return option;
    }
}

