package com.epam.tcodata.analytics.overtaking.detection;

import com.epam.tcodata.analytics.overtaking.detection.drivers.IDriver;
import com.epam.tcodata.analytics.overtaking.detection.drivers.OvertakingDetectionHistoricalDriver;
import com.epam.tcodata.analytics.overtaking.detection.drivers.OvertakingDetectionStreamDriver;
import com.epam.tcodata.analytics.overtaking.detection.drivers.OvertakingDetectionTestDriver;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum RunMode {
    STREAM("stream", new OvertakingDetectionStreamDriver()),
    HISTORICAL("hive", new OvertakingDetectionHistoricalDriver()),
    TEST("test", new OvertakingDetectionTestDriver());

    private final String mode;
    private final transient IDriver driver;

    RunMode(String mode, IDriver driver) {
        this.mode = mode;
        this.driver = driver;
    }

    /**
     * Parses command line argument ro one of possible {@link RunMode}.
     *
     * @param mode - string from command line
     * @return {@link RunMode}
     */
    public static RunMode getModeFromCmd(String mode) {
        for (RunMode runMode : values()) {
            if (runMode.mode.equals(mode)) {
                return runMode;
            }
        }
        throw new IllegalArgumentException("Unknown run mode: should be one of: "
                + Arrays.stream(values()).map(runMode -> runMode.mode).collect(Collectors.joining(", ")));
    }

    public String getMode() {
        return mode;
    }

    public IDriver getDriver() {
        return driver;
    }
}
