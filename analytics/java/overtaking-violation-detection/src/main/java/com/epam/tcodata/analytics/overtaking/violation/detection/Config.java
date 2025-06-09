package com.epam.tcodata.analytics.overtaking.violation.detection;

import com.epam.tcodata.common.ConfigBuilder;
import com.epam.tcodata.common.TCOConfig;

public class Config {
    public static final TCOConfig SPARK_CONFIG;

    public static final String SPARK_CHECKPOINT_DIR = "spark.checkpoint.dir";
    public static final String SPARK_PROGRESS_DIR = "spark.progress.dir";
    public static final String SPARK_CHECKPOINT_INTERVAL = "spark.checkpoint.interval";
    private static final String SPARK_PROPERTIES_FILE = "spark.properties";

    static {
        SPARK_CONFIG = getSparkConfig();
    }

    private Config() {
    }

    private static TCOConfig getSparkConfig() {
        return new TCOConfig(new ConfigBuilder()
                .setResourceParametersFileName(SPARK_PROPERTIES_FILE)
                .addParameter(SPARK_CHECKPOINT_DIR, true)
                .addParameter(SPARK_PROGRESS_DIR, true)
                .addParameter(SPARK_CHECKPOINT_INTERVAL, true)
                .build());
    }
}
