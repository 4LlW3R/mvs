package com.epam.tcodata.analytics.overtaking.detection.drivers;

import com.epam.tcodata.analytics.overtaking.detection.factory.IOvertakingDetectionFactory;
import org.apache.commons.cli.CommandLine;

import java.io.Serializable;

public class OvertakingDetectionHistoricalDriver implements IDriver, Serializable {
    private static final long serialVersionUID = -7687020128528602148L;

    public static final String OPTION_TIMESTAMP_FROM = "from";
    public static final String OPTION_TIMESTAMP_TO = "to";

    @Override
    public void runJob(CommandLine commandLine, IOvertakingDetectionFactory factory) {
        /***
         * No logic is provided yet
         * ***/
    }
}
