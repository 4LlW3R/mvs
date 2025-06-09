package com.epam.tcodata.redis.manager.util;

import com.epam.tcodata.common.ConfigBuilder;
import com.epam.tcodata.models.datalake.raw.RawEntity;
import com.epam.tcodata.models.datalake.raw.dimension.RawAsset;
import com.epam.tcodata.models.datalake.raw.dimension.RawDriver;
import com.epam.tcodata.redis.dal.IRedis;
import com.epam.tcodata.redis.manager.exception.UnsupportedDimensionTypeException;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * Utility class.
 */
public class RedisUtil {

    /**
     * SparkSession builder.
     *
     * @return SparkSession object instance
     */
    private static final String SPARK_CHECKPOINT_INTERVAL = "spark.checkpoint.interval";

    /**
     * Method returns spark session via class name and app name.
     * @summary Summary for this method
     * @param className Class name
     * @param appName Name of application
     * @return Returns spark session object
     */
    public static SparkSession getSparkSession(String className, String appName) {
        return SparkSession.builder()
                .config(getConf(className, appName))
                .config("spark.sql.hive.metastore.version", "2.3.9")
                .config("spark.sql.hive.metastore.jars", "builtin")
                .config("hive.metastore.schema.verification", "false")
                .config("hive.metastore.schema.verification.record.version", "false")
                .enableHiveSupport()
                .getOrCreate();
    }

    /**
     * Method creating key-value pair, which will be stored in Redis.
     *
     * @param entityIterator Iterator of {@link RawDriver} or {@link RawAsset} to be pushed into Redis
     * @param redis          Database in Redis, where objects should be placed
     */
    public static <T extends RawEntity> void pushObjectToRedis(Iterator<T> entityIterator,
                                                               IRedis redis) {

        entityIterator.forEachRemaining(entity -> {
            String key = RedisUtil.getKey(entity);
            Map<String, String> values = RedisUtil.getValue(entity);
            if (!containsNullValue(Objects.requireNonNull(
                    values, "values to push to redis should not be null"))) {
                redis.set(key, values);
            }
        });
    }

    /**
     * Method creating 'key' for given value.
     *
     * @param entity instance of {@link RawEntity}
     * @return String key for corresponding object
     */
    private static <T extends RawEntity> String getKey(T entity) {
        return entity.getDurableId();
    }

    /**
     * Method invoking 'value' from given object.
     *
     * @param entity instance of {@link RawDriver} or {@link RawAsset}
     * @return String value for corresponding object
     */
    private static <T extends RawEntity> Map<String, String> getValue(T entity) {
        if (entity instanceof RawAsset) {
            return new HashMap<String, String>() {{
                    RawAsset asset = (RawAsset) entity;
                    put("RegistrationNumber", asset.getRegistrationNumber());
                    put("Description", asset.getDescription());
                    put("AssetTypeId", String.valueOf(asset.getAssetTypeId()));
                }};
        } else if (entity instanceof RawDriver) {
            return new HashMap<String, String>() {{
                    RawDriver driver = (RawDriver) entity;
                    put("Name", driver.getName());
                    put("EmployeeNumber", driver.getEmployeeNumber());
                }};
        }
        throw new UnsupportedDimensionTypeException("Class " + entity.getClass().getName() + " is unsupported");
    }

    private static boolean containsNullValue(Map<String, String> map) {
        return  map.values().stream().anyMatch(Objects::isNull);
    }

    /**
     * SparkConf getter.
     *
     * @param className name of class to build checkpoint dir
     * @return SparkConf which should be used for SparkSession init
     */
    private static SparkConf getConf(String className, String appName) {
        Map<String, String> tcoConfig = new ConfigBuilder()
                .addParameter(SPARK_CHECKPOINT_INTERVAL, true)
                .setResourceParametersFileName("application.properties").build();

        return new SparkConf()
                .setAppName(appName)

                .set("spark.streaming.driver.writeAheadLog.allowBatching", "true")
                .set("spark.streaming.driver.writeAheadLog.batchingTimeout", "60000")
                .set("spark.streaming.receiver.writeAheadLog.enable", "true")
                .set("spark.streaming.driver.writeAheadLog.closeFileAfterWrite", "true")
                .set("spark.streaming.receiver.writeAheadLog.closeFileAfterWrite", "true")
                .set("spark.streaming.stopGracefullyOnShutdown", "true")

                .set("spark.streaming.backpressure.enabled", "true")
                .set("spark.progressDir", String.format("%s_progress_dir", className))
                .set("spark.checkpointDir", String.format("%s_checkpoint_dir", className))
                .set(SPARK_CHECKPOINT_INTERVAL, tcoConfig.get(SPARK_CHECKPOINT_INTERVAL));
    }

}
