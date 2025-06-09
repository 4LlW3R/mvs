package com.epam.tcodata.eventhub.dal.impl;

import com.epam.tcodata.eventhub.dal.EventHubInfo;
import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.factory.ISecretStorageFactory;
import org.apache.spark.eventhubs.rdd.OffsetRange;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertTrue;

public class EventHubIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventHubIT.class);

    static IEventHub driverEventHub;

    @BeforeClass
    public static void setUp() {
        ISecretStorageFactory defaultFactory = ISecretStorageFactory.createDefaultFactory();
        ISecretStorage secretStorage = defaultFactory.createSecretStorage(new Properties());
        driverEventHub = new EventHub(EventHubInfo.DRIVER, secretStorage);
    }

    @Test
    public void getDriverEHOffsetsTest() {
        Map<String, OffsetRange> possibleStartingOffsets = driverEventHub.getPossibleOffsets();
        LOGGER.info(String.valueOf(possibleStartingOffsets));
        assertTrue(possibleStartingOffsets.size() == 32);
    }
}
