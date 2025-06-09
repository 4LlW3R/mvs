package com.epam.tcodata.mock.eventhub.dal;

import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.mock.eventhub.dal.exception.NullOffsetRangeException;
import com.epam.tcodata.mock.eventhub.dal.exception.WrongOffsetsException;
import com.epam.tcodata.mock.eventhub.dal.exception.WrongPartitionCountException;
import com.microsoft.azure.eventhubs.EventData;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.eventhubs.NameAndPartition;
import org.apache.spark.eventhubs.rdd.OffsetRange;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;

@Ignore
public class MockEventHubReceiveTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MockEventHubReceiveTest.class);
    private static final int PARTITION_COUNT = 32;
    private static final int MAX_READ_COUNT = 1000;
    private static final String CONSUMER_GROUP_1 = "consumer_group_1";
    private static final String EVENT = "event";
    private static final String APP_NAME = "app-name";
    private static final String LOCAL_1 = "local[1]";

    @Ignore("Unexpected floating failures")
    @Test
    public void receiveStreamWithoutSpecifyingOffsetsAfterSendingTest() {
        IEventHub mockEventHub = new MockEventHub(PARTITION_COUNT, MAX_READ_COUNT);

        List<EventData> eventDataListSent = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            eventDataListSent.add(EventData.create((EVENT + i).getBytes()));
        }

        for (EventData eventData : eventDataListSent) {
            mockEventHub.send(eventData);
        }

        SparkSession sparkSession = SparkSession.builder()
                .config(new SparkConf()
                        .setAppName(APP_NAME)
                        .setMaster(LOCAL_1))
                .getOrCreate();
        JavaSparkContext sparkContext = new JavaSparkContext(sparkSession.sparkContext());

        AtomicLong count = new AtomicLong(0);
        try (JavaStreamingContext jsc = new JavaStreamingContext(sparkContext, Durations.seconds(5))) {
            JavaDStream<EventData> eventDataJavaDStream = mockEventHub.receiveStream(jsc, CONSUMER_GROUP_1);

            eventDataJavaDStream.foreachRDD(rdd -> {
                long rddCount = rdd.count();
                count.addAndGet(rddCount);
            });

            jsc.start();
            jsc.awaitTerminationOrTimeout(5000);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
            Thread.currentThread().interrupt();
        }

        assertEquals(0L, count.get());
    }

    @Test
    public void receiveStreamWithoutSpecifyingOffsetsBeforeSendingTest() {
        IEventHub mockEventHub = new MockEventHub(PARTITION_COUNT, MAX_READ_COUNT);

        SparkSession sparkSession = SparkSession.builder()
                .config(new SparkConf()
                        .setAppName(APP_NAME)
                        .setMaster(LOCAL_1))
                .getOrCreate();
        JavaSparkContext sparkContext = new JavaSparkContext(sparkSession.sparkContext());

        AtomicLong count = new AtomicLong(0);
        try (JavaStreamingContext jsc = new JavaStreamingContext(sparkContext, Durations.seconds(5))) {
            JavaDStream<EventData> eventDataJavaDStream = mockEventHub.receiveStream(jsc, CONSUMER_GROUP_1);

            List<EventData> eventDataListSent = new ArrayList<>();
            for (int i = 0; i < 200; i++) {
                eventDataListSent.add(EventData.create((EVENT + i).getBytes()));
            }

            for (EventData eventData : eventDataListSent) {
                mockEventHub.send(eventData);
            }

            eventDataJavaDStream.foreachRDD(rdd -> {
                long rddCount = rdd.count();
                count.addAndGet(rddCount);
            });

            jsc.start();
            jsc.awaitTerminationOrTimeout(5000);

        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
            Thread.currentThread().interrupt();
        }

        assertEquals(200L, count.get());
    }

    /**
     * This test have no chance of failing because {@link java.util.Random}
     * can be initialized with a seed passed to constructor which will be used
     * for generating pseudo random numbers (sequence of which in fact will always be
     * the same). So if the test passed once, it'll always pass with the same seed.
     */
    @Test
    public void receiveStreamSpecifyingOffsetsTest() {
        IEventHub mockEventHub = new MockEventHub(PARTITION_COUNT, MAX_READ_COUNT);
        List<EventData> eventDataListSent = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            eventDataListSent.add(EventData.create((EVENT + i).getBytes()));
        }

        for (EventData eventData : eventDataListSent) {
            mockEventHub.send(eventData);
        }

        SparkSession sparkSession = SparkSession.builder()
                .config(new SparkConf()
                        .setAppName(APP_NAME)
                        .setMaster(LOCAL_1))
                .getOrCreate();
        JavaSparkContext sparkContext = new JavaSparkContext(sparkSession.sparkContext());

        Map<String, OffsetRange> offsets = composeOffsets(new OffsetRange(new NameAndPartition("", 0), 5L, 0, null));

        AtomicLong count = new AtomicLong(0);
        try (JavaStreamingContext jsc = new JavaStreamingContext(sparkContext, Durations.seconds(5))) {
            JavaDStream<EventData> eventDataJavaDStream = mockEventHub.receiveStream(jsc, CONSUMER_GROUP_1, offsets);
            eventDataJavaDStream.foreachRDD(rdd -> {
                long rddCount = rdd.count();
                count.addAndGet(rddCount);
            });

            jsc.start();
            jsc.awaitTerminationOrTimeout(5000);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
            Thread.currentThread().interrupt();
        }

        assertEquals(195L, count.get());
    }

    @Test(expected = WrongPartitionCountException.class)
    public void tryToReceiveStreamSpecifyingOffsetsWithWrongPartitionCountTest() throws InterruptedException {
        IEventHub mockEventHub = new MockEventHub(PARTITION_COUNT, MAX_READ_COUNT);
        Map<String, OffsetRange> brokenOffsets = new HashMap<String, OffsetRange>() {
            {
                for (int i = 1; i < 32; i++) {
                    put(String.valueOf(i), new OffsetRange(new NameAndPartition("", i), 0L, 0, null));
                }
            }
        };

        SparkSession sparkSession = SparkSession.builder()
                .config(new SparkConf()
                        .setAppName("appName")
                        .setMaster(LOCAL_1))
                .getOrCreate();
        JavaSparkContext sparkContext = new JavaSparkContext(sparkSession.sparkContext());
        try (JavaStreamingContext jsc = new JavaStreamingContext(sparkContext, Durations.seconds(30))) {
            JavaDStream<EventData> eventDataJavaDStream = mockEventHub.receiveStream(jsc, CONSUMER_GROUP_1, brokenOffsets);
            eventDataJavaDStream.print();
            jsc.start();
            jsc.awaitTermination();
        }
    }

    @Test(expected = WrongOffsetsException.class)
    public void tryToReceiveStreamSpecifyingToBigOffsetsTest() {
        IEventHub mockEventHub = new MockEventHub(PARTITION_COUNT, MAX_READ_COUNT);
        List<EventData> eventDataListSent = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            eventDataListSent.add(EventData.create((EVENT + i).getBytes()));
        }

        for (EventData eventData : eventDataListSent) {
            mockEventHub.send(eventData);
        }

        SparkSession sparkSession = SparkSession.builder()
                .config(new SparkConf()
                        .setAppName(APP_NAME)
                        .setMaster(LOCAL_1))
                .getOrCreate();
        JavaSparkContext sparkContext = new JavaSparkContext(sparkSession.sparkContext());

        Map<String, OffsetRange> offsets = composeOffsets(new OffsetRange(new NameAndPartition("", 0), 101L, 0, null));

        try (JavaStreamingContext jsc = new JavaStreamingContext(sparkContext, Durations.seconds(5))) {
            JavaDStream<EventData> eventDataJavaDStream = mockEventHub.receiveStream(jsc, CONSUMER_GROUP_1, offsets);
            eventDataJavaDStream.print();
            jsc.start();
            jsc.awaitTerminationOrTimeout(5000);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    @Test(expected = WrongOffsetsException.class)
    public void tryToReceiveStreamSpecifyingToSmallOffsetsTest() {
        IEventHub mockEventHub = new MockEventHub(PARTITION_COUNT, MAX_READ_COUNT);
        SparkSession sparkSession = SparkSession.builder()
                .config(new SparkConf()
                        .setAppName(APP_NAME)
                        .setMaster(LOCAL_1))
                .getOrCreate();
        JavaSparkContext sparkContext = new JavaSparkContext(sparkSession.sparkContext());

        Map<String, OffsetRange> offsets = composeOffsets(new OffsetRange(new NameAndPartition("", 0), -1L, 0, null));

        try (JavaStreamingContext jsc = new JavaStreamingContext(sparkContext, Durations.seconds(5))) {
            JavaDStream<EventData> eventDataJavaDStream = mockEventHub.receiveStream(jsc, CONSUMER_GROUP_1, offsets);
            eventDataJavaDStream.print();
            jsc.start();
            jsc.awaitTerminationOrTimeout(5000);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    @Test(expected = NullOffsetRangeException.class)
    public void tryToReceiveStreamSpecifyingOffsetsWithNullOffsetRangeTest() {
        IEventHub mockEventHub = new MockEventHub(PARTITION_COUNT, MAX_READ_COUNT);
        SparkSession sparkSession = SparkSession.builder()
                .config(new SparkConf()
                        .setAppName(APP_NAME)
                        .setMaster(LOCAL_1))
                .getOrCreate();
        JavaSparkContext sparkContext = new JavaSparkContext(sparkSession.sparkContext());

        Map<String, OffsetRange> offsets = composeOffsets(null);

        try (JavaStreamingContext jsc = new JavaStreamingContext(sparkContext, Durations.seconds(5))) {
            JavaDStream<EventData> eventDataJavaDStream = mockEventHub.receiveStream(jsc, CONSUMER_GROUP_1, offsets);
            eventDataJavaDStream.print();
            jsc.start();
            jsc.awaitTerminationOrTimeout(5000);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
//
    private Map<String, OffsetRange> composeOffsets(OffsetRange firstElem) {
        return new HashMap<String, OffsetRange>() {
            {
                put("0", firstElem);
                for (int i = 1; i < 32; i++) {
                    put(String.valueOf(i), new OffsetRange(new NameAndPartition("", i), 0L, 0, null));
                }
            }
        };
    }
}
