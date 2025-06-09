package com.epam.tcodata.analytics.overtaking.detection.drivers;

import com.epam.tcodata.analytics.overtaking.detection.factory.IOvertakingDetectionFactory;
import org.apache.commons.cli.CommandLine;

public interface IDriver {
    String OPTION_APP_NAME = "appName";

    void runJob(CommandLine commandLine, IOvertakingDetectionFactory factory);
}
