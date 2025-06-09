package com.epam.tcodata.internal.pump;

import com.epam.tcodata.internal.pump.driver.InternalPumpStreamDriver;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InternalPumpDriverTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(InternalPumpDriverTest.class);

    @Test
    public void shutdownEventHubThreadPools() {
        InternalPumpStreamDriver.shutdownEventHubThreadPools();
        LOGGER.info("Success w/o exceptions");
    }
}