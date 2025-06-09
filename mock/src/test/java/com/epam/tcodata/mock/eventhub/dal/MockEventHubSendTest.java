package com.epam.tcodata.mock.eventhub.dal;

import com.epam.tcodata.eventhub.dal.IEventHub;
import com.microsoft.azure.eventhubs.EventData;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MockEventHubSendTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockEventHubSendTest.class);

    private IEventHub mockEventHub;

    private static final int PARTITION_COUNT = 32;
    private static final int MAX_READ_COUNT = 1000;
    private static final String CONSUMER_GROUP_1 = "consumer_group_1";
    private static final String PARTITION_KEY_0 = "0";
    private static final String EVENT_1 = "event-1";
    private static final String EVENT_2 = "event-2";
    private static final String EVENT_3 = "event-3";

    @Before
    public void init() {
        this.mockEventHub = new MockEventHub(PARTITION_COUNT, MAX_READ_COUNT);
    }

    @Test
    public void sendOneEventDataToSpecificPartitionTest() {
        EventData eventDataSent = EventData.create("data".getBytes());
        this.mockEventHub.send(eventDataSent, PARTITION_KEY_0);

        List<EventData> eventDataListLandedPartition =
                ((MockEventHub) this.mockEventHub).readDataFromPartition(PARTITION_KEY_0, CONSUMER_GROUP_1);
        EventData eventDataLandedPartition = eventDataListLandedPartition.get(0);

        assertEquals(eventDataSent, eventDataLandedPartition);
    }

    @Test
    public void sendOneEventDataWithoutSpecifyingPartitionTest() {
        EventData eventDataSent = EventData.create("data".getBytes());
        this.mockEventHub.send(eventDataSent);

        String nonEmptyPartitionKey = null;
        int nonEmptyPartitionCount = 0;
        for (int partitionKeyN = 0; partitionKeyN < PARTITION_COUNT; partitionKeyN++) {
            String partitionKey = String.valueOf(partitionKeyN);
            if (!((MockEventHub) this.mockEventHub).readDataFromPartition(partitionKey, CONSUMER_GROUP_1).isEmpty()) {
                nonEmptyPartitionCount++;
                nonEmptyPartitionKey = partitionKey;
            }
        }

        List<EventData> eventDataListLandedPartition =
                ((MockEventHub) this.mockEventHub).readDataFromPartition(nonEmptyPartitionKey, CONSUMER_GROUP_1);
        EventData eventDataLandedPartition = eventDataListLandedPartition.get(0);

        assertEquals(1, nonEmptyPartitionCount);
        assertEquals(eventDataSent, eventDataLandedPartition);
    }

    @Test
    public void sendIterableEventDataToSpecificPartitionTest() {
        List<EventData> eventDataListSent = new ArrayList<>();
        eventDataListSent.add(EventData.create(EVENT_1.getBytes()));
        eventDataListSent.add(EventData.create(EVENT_2.getBytes()));
        eventDataListSent.add(EventData.create(EVENT_3.getBytes()));
        this.mockEventHub.send(eventDataListSent, PARTITION_KEY_0);

        List<EventData> eventDataListLandedPartition =
                ((MockEventHub) this.mockEventHub).readDataFromPartition(PARTITION_KEY_0, CONSUMER_GROUP_1);

        assertEquals(eventDataListSent, eventDataListLandedPartition);
    }

    @Test
    public void sendIterableEventDataWithoutSpecifyingPartitionTest() {
        List<EventData> eventDataListSent = new ArrayList<>();
        eventDataListSent.add(EventData.create(EVENT_1.getBytes()));
        eventDataListSent.add(EventData.create(EVENT_2.getBytes()));
        eventDataListSent.add(EventData.create(EVENT_3.getBytes()));
        this.mockEventHub.send(eventDataListSent);

        String nonEmptyPartitionKey = null;
        int nonEmptyPartitionCount = 0;
        for (int partitionKeyN = 0; partitionKeyN < PARTITION_COUNT; partitionKeyN++) {
            String partitionKey = String.valueOf(partitionKeyN);
            if (!((MockEventHub) this.mockEventHub).readDataFromPartition(partitionKey, CONSUMER_GROUP_1).isEmpty()) {
                nonEmptyPartitionCount++;
                nonEmptyPartitionKey = partitionKey;
            }
        }

        List<EventData> eventDataListLandedPartition =
                ((MockEventHub) this.mockEventHub).readDataFromPartition(nonEmptyPartitionKey, CONSUMER_GROUP_1);

        assertEquals(1, nonEmptyPartitionCount);
        assertEquals(eventDataListSent, eventDataListLandedPartition);
    }

    @Test
    public void sendManyEventDataToSpecificPartitionTest() {
        List<EventData> eventDataListSent = new ArrayList<>();
        eventDataListSent.add(EventData.create(EVENT_1.getBytes()));
        eventDataListSent.add(EventData.create(EVENT_2.getBytes()));
        eventDataListSent.add(EventData.create(EVENT_3.getBytes()));

        for (EventData eventData : eventDataListSent) {
            this.mockEventHub.send(eventData, PARTITION_KEY_0);
        }

        List<EventData> eventDataListLandedPartition =
                ((MockEventHub) this.mockEventHub).readDataFromPartition(PARTITION_KEY_0, CONSUMER_GROUP_1);

        assertEquals(eventDataListSent, eventDataListLandedPartition);
    }

    /**
     * This test have no chance of failing because {@link java.util.Random}
     * can be initialized with a seed passed to constructor which will be used
     * for generating pseudo random numbers (sequence of which in fact will always be
     * the same). So if the test passed once, it'll always pass with the same seed.
     */
    @Test
    public void sendManyEventDataToTryToFillMoreThenOnePartitionTest() {
        List<EventData> eventDataListSent = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            eventDataListSent.add(EventData.create(("event" + i).getBytes()));
        }

        for (EventData eventData : eventDataListSent) {
            this.mockEventHub.send(eventData);
        }

        int nonEmptyPartitionCount = 0;
        for (int partitionKeyN = 0; partitionKeyN < PARTITION_COUNT; partitionKeyN++) {
            String partitionKey = String.valueOf(partitionKeyN);
            if (!((MockEventHub) this.mockEventHub).readDataFromPartition(partitionKey, CONSUMER_GROUP_1).isEmpty()) {
                nonEmptyPartitionCount++;
            }
        }

        LOGGER.info("Non empty partition count: {}", nonEmptyPartitionCount);
        assertTrue(nonEmptyPartitionCount > 1);
    }

    /**
     * This test have no chance of failing because {@link java.util.Random}
     * can be initialized with a seed passed to constructor which will be used
     * for generating pseudo random numbers (sequence of which in fact will always be
     * the same). So if the test passed once, it'll always pass with the same seed.
     */
    @Test
    public void sendManyEventDataToTryToFillAllPartitionsTest() {
        List<EventData> eventDataListSent = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            eventDataListSent.add(EventData.create(("event" + i).getBytes()));
        }

        for (EventData eventData : eventDataListSent) {
            this.mockEventHub.send(eventData);
        }

        int nonEmptyPartitionCount = 0;
        for (int partitionKeyN = 0; partitionKeyN < PARTITION_COUNT; partitionKeyN++) {
            String partitionKey = String.valueOf(partitionKeyN);
            if (!((MockEventHub) this.mockEventHub).readDataFromPartition(partitionKey, CONSUMER_GROUP_1).isEmpty()) {
                nonEmptyPartitionCount++;
            }
        }

        LOGGER.info("Non empty partition count: {}", nonEmptyPartitionCount);
        assertTrue(nonEmptyPartitionCount > 0);
    }
}
