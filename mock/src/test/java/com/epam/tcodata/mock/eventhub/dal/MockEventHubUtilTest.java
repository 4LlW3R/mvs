package com.epam.tcodata.mock.eventhub.dal;

import com.epam.tcodata.eventhub.dal.IEventHub;
import com.microsoft.azure.eventhubs.EventData;
import org.apache.spark.eventhubs.NameAndPartition;
import org.apache.spark.eventhubs.rdd.OffsetRange;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MockEventHubUtilTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockEventHubSendTest.class);

    private IEventHub mockEventHub;

    private static final int PARTITION_COUNT = 32;
    private static final int MAX_READ_COUNT = 1000;
    private static final String PARTITION_KEY_0 = "0";

    @Before
    public void init() {
        this.mockEventHub = new MockEventHub(PARTITION_COUNT, MAX_READ_COUNT);
    }

    @Test
    public void getPartitionCountTest() {
        int partitionCount = this.mockEventHub.getPartitionCount();
        LOGGER.info("Testing partition count..");
        assertEquals(PARTITION_COUNT, partitionCount);
    }

    @Test
    public void getPossibleOffsetsTest() {
        Map<String, OffsetRange> possibleStartingOffsets = this.mockEventHub.getPossibleOffsets();
        Map<String, OffsetRange> expectedOffsets = new HashMap<>();
        for (int i = 0; i < 32; i++) {
            expectedOffsets.put(String.valueOf(i), new OffsetRange(new NameAndPartition("", i), 0L, 0, null));
        }
        LOGGER.info("Testing possible offsets..");
        assertEquals(expectedOffsets, possibleStartingOffsets);
    }

    @Test
    public void possibleOffsetsDoesNotMoveAfterSendTest() {
        Map<String, OffsetRange> possibleOffsetsBeforeSend = this.mockEventHub.getPossibleOffsets();

        EventData eventDataSent = EventData.create("data".getBytes());
        this.mockEventHub.send(eventDataSent, PARTITION_KEY_0);

        Map<String, OffsetRange> possibleOffsetsAfterSend = this.mockEventHub.getPossibleOffsets();

        assertEquals(possibleOffsetsBeforeSend, possibleOffsetsAfterSend);
    }
}
