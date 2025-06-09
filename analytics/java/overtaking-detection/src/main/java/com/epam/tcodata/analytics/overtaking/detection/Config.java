package com.epam.tcodata.analytics.overtaking.detection;

import com.epam.tcodata.common.ConfigBuilder;
import com.epam.tcodata.common.TCOConfig;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;

public class Config {

    private Config() {
    }
    public static final TCOConfig SPARK_CONFIG;

    public static final String SPARK_CHECKPOINT_DIR = "spark.checkpoint.dir";
    public static final String SPARK_CHECKPOINT_INTERVAL = "spark.checkpoint.interval";

    private static final String SPARK_STREAMING_DRIVER_WRITE_AHEAD_LOG_ALLOW_BATCHING = "spark.streaming.driver.writeAheadLog.allowBatching";
    private static final String SPARK_STREAMING_DRIVER_WRITE_AHEAD_LOG_BATCHING_TIMEOUT = "spark.streaming.driver.writeAheadLog.batchingTimeout";
    private static final String SPARK_STREAMING_DRIVER_WRITE_AHEAD_LOG_CLOSE_FILE = "spark.streaming.driver.writeAheadLog.closeFileAfterWrite";
    private static final String SPARK_STREAMING_RECEIVER_WRITE_AHEAD_LOG_ENABLE = "spark.streaming.receiver.writeAheadLog.enable";
    private static final String SPARK_STREAMING_RECEIVER_WRITE_AHEAD_LOG_CLOSE_FILE = "spark.streaming.receiver.writeAheadLog.closeFileAfterWrite";
    private static final String SPARK_STREAMING_STOP_GRACEFULLY = "spark.streaming.stopGracefullyOnShutdown";
    private static final String SPARK_STREAMING_BACK_PRESSURE_ENABLED = "spark.streaming.backpressure.enabled";

    private static final String SPARK_PROPERTIES_FILE = "spark.properties";


    static {
        SPARK_CONFIG = getSparkConfigProperties();
    }

    /**
     * Creates SparkSession with Hive support from command line arguments.
     *
     * @param appName - application name
     * @return {@link SparkSession}
     */
    public static SparkSession getSparkSession(String appName) {
        return SparkSession.builder()
                .config(getSparkConf(appName))
                .enableHiveSupport()
                .getOrCreate();
    }

    /**
     * Creates SparkSession with Hive support from command line arguments and properties file.
     *
     * @param appName - application name
     * @return {@link SparkConf}
     */
    public static SparkConf getSparkConf(String appName) {
        return new SparkConf()
                .setAppName(appName)
                .set(SPARK_STREAMING_DRIVER_WRITE_AHEAD_LOG_ALLOW_BATCHING, SPARK_CONFIG.getProperty(SPARK_STREAMING_DRIVER_WRITE_AHEAD_LOG_ALLOW_BATCHING))
                .set(SPARK_STREAMING_DRIVER_WRITE_AHEAD_LOG_BATCHING_TIMEOUT, SPARK_CONFIG.getProperty(SPARK_STREAMING_DRIVER_WRITE_AHEAD_LOG_BATCHING_TIMEOUT))
                .set(SPARK_STREAMING_DRIVER_WRITE_AHEAD_LOG_CLOSE_FILE, SPARK_CONFIG.getProperty(SPARK_STREAMING_DRIVER_WRITE_AHEAD_LOG_CLOSE_FILE))
                .set(SPARK_STREAMING_RECEIVER_WRITE_AHEAD_LOG_ENABLE, SPARK_CONFIG.getProperty(SPARK_STREAMING_RECEIVER_WRITE_AHEAD_LOG_ENABLE))
                .set(SPARK_STREAMING_RECEIVER_WRITE_AHEAD_LOG_CLOSE_FILE, SPARK_CONFIG.getProperty(SPARK_STREAMING_RECEIVER_WRITE_AHEAD_LOG_CLOSE_FILE))
                .set(SPARK_STREAMING_STOP_GRACEFULLY, SPARK_CONFIG.getProperty(SPARK_STREAMING_STOP_GRACEFULLY))
                .set(SPARK_STREAMING_BACK_PRESSURE_ENABLED, SPARK_CONFIG.getProperty(SPARK_STREAMING_BACK_PRESSURE_ENABLED));
    }

    private static TCOConfig getSparkConfigProperties() {
        return new TCOConfig(new ConfigBuilder()
                .setResourceParametersFileName(SPARK_PROPERTIES_FILE)
                .addParameter(SPARK_CHECKPOINT_DIR, true)
                .addParameter(SPARK_CHECKPOINT_INTERVAL, true)
                .addParameter(SPARK_STREAMING_DRIVER_WRITE_AHEAD_LOG_ALLOW_BATCHING, true)
                .addParameter(SPARK_STREAMING_DRIVER_WRITE_AHEAD_LOG_BATCHING_TIMEOUT, true)
                .addParameter(SPARK_STREAMING_DRIVER_WRITE_AHEAD_LOG_CLOSE_FILE, true)
                .addParameter(SPARK_STREAMING_RECEIVER_WRITE_AHEAD_LOG_ENABLE, true)
                .addParameter(SPARK_STREAMING_RECEIVER_WRITE_AHEAD_LOG_CLOSE_FILE, true)
                .addParameter(SPARK_STREAMING_STOP_GRACEFULLY, true)
                .addParameter(SPARK_STREAMING_BACK_PRESSURE_ENABLED, true)
                .build());
    }
}
