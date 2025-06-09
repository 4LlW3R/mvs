package com.epam.tcodata.mock.eventhub.dal;

import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.mock.eventhub.dal.exception.NullOffsetRangeException;
import com.epam.tcodata.mock.eventhub.dal.exception.WrongOffsetsException;
import com.epam.tcodata.mock.eventhub.dal.exception.WrongPartitionCountException;
import com.microsoft.azure.eventhubs.EventData;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.eventhubs.NameAndPartition;
import org.apache.spark.eventhubs.rdd.OffsetRange;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.reflect.ClassManifestFactory$;
import scala.reflect.ClassTag;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class MockEventHub implements IEventHub {

    private ConcurrentHashMap<String, EventHubPartition> eventHub;
    private int partitionCount;
    private int maxReadCount;
    private SecureRandom random;

    private static volatile MockEventHub singlton = null;

    /**
     * Entry point for singleton.
     *
     * @return instance of MockEventHub.
     */
    public static MockEventHub instance() {
        if (singlton == null)
            synchronized (MockEventHub.class) {
                if (singlton == null) {
                    singlton = new MockEventHub();
                }
            }
        return singlton;
    }

    /**
     * Public default constructor.
     */
    public MockEventHub() {
        this(32, 1000);
    }

    /**
     * Public constructor with implicit parameters.
     *
     * @param partitionCount how many partitions to create.
     * @param maxReadCount   how many elements can be sent in a batch.
     */
    public MockEventHub(int partitionCount, int maxReadCount) {
        this.eventHub = new ConcurrentHashMap<>();
        this.partitionCount = partitionCount;
        for (int i = 0; i < partitionCount; i++) {
            String partitionKey = String.valueOf(i);
            this.eventHub.put(partitionKey, new EventHubPartition(partitionKey));
        }
        this.maxReadCount = maxReadCount;
        this.random = new SecureRandom();
    }

    @Override
    public void send(EventData eventData, String partitionKey) {
        EventHubPartition partition = partitionKey != null
                ? this.eventHub.get(partitionKey)
                : this.eventHub.get(String.valueOf(random.nextInt(getPartitionCount())));
        partition.send(eventData);
    }

    @Override
    public void send(Iterable<EventData> eventDataIterable, String partitionKey) {
        EventHubPartition partition = partitionKey != null
                ? this.eventHub.get(partitionKey)
                : this.eventHub.get(String.valueOf(random.nextInt(getPartitionCount())));
        eventDataIterable.forEach(partition::send);
    }

    /**
     * Receiving {@link JavaDStream} without specifying offsets map.
     *
     * @param jsc           java streaming context.
     * @param consumerGroup consumer group to receive data with.
     * @return JavaDStream of {@link EventData}.
     */
    @Override
    public JavaDStream<EventData> receiveStream(JavaStreamingContext jsc, String consumerGroup) {
        return receiveStream(jsc, consumerGroup, null);
    }

    /**
     * Receiving {@link JavaDStream} specifying offsets.
     *
     * @param jsc           java streaming context.
     * @param consumerGroup consumer group to receive data with.
     * @param offsets       offsets which are represented as Map of (String, Long), where
     *                      String - partition key (ex: "0", "1", "2" ...)
     *                      Long - any valid offset value (possible start < valid offset < possible end) as a long number.
     * @return JavaDStream of {@link EventData}.
     */
    @Override
    public JavaDStream<EventData> receiveStream(JavaStreamingContext jsc, String consumerGroup, Map<String, OffsetRange> offsets) {
        validateOffsets(offsets);
        if (offsets != null) {
            for (Map.Entry<String, EventHubPartition> partitionEntry : eventHub.entrySet()) {
                OffsetRange offset = offsets.get(partitionEntry.getKey());
                partitionEntry.getValue().moveOffset(consumerGroup, offset.fromSeqNo());
            }
        } else {
            for (Map.Entry<String, EventHubPartition> partitionEntry : eventHub.entrySet()) {
                partitionEntry.getValue().moveOffsetToEnd(consumerGroup);
            }
        }

        ClassTag<EventData> eventDataClassTag = ClassManifestFactory$.MODULE$.fromClass(EventData.class);
        MockEventHubDirectStream mockEventHubDirectStream =
                new MockEventHubDirectStream(jsc, eventDataClassTag, this, consumerGroup);
        return new JavaInputDStream<>(mockEventHubDirectStream, eventDataClassTag);
    }

    @Override
    public JavaRDD<EventData> receiveRdd(JavaSparkContext sparkContext, String consumerGroup, Map<String, OffsetRange> offsets) {
        return null;
    }

    /**
     * Returns offsets from which you can start receiving data (means data with this offset is still in Event Hub).
     * Doesn't return offsets from which you should receive data in the next batch or offsets for different
     * consumer groups.
     *
     * @return map of (partition key, offset from which data can be received).
     */
    @Override
    public Map<String, OffsetRange> getPossibleOffsets() {
        return eventHub.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().eventDataWithOffset.isEmpty()
                                ? new OffsetRange(new NameAndPartition("", Integer.parseInt(entry.getKey())), 0L, 0L, null)
                                : new OffsetRange(new NameAndPartition("", Integer.parseInt(entry.getKey())), entry.getValue().eventDataWithOffset.firstKey(), 0L, null)
                ));
    }

    @Override
    public int getPartitionCount() {
        return this.eventHub.size();
    }

    /**
     * Returns offsets to which you can receive data (means data with this offset is present in Event Hub).
     * Doesn't return offsets from which you should receive data in the next batch or offsets for different
     * consumer groups.
     *
     * @return map of (partition key, offset to which data can be received).
     */
    Map<String, Long> getPossibleEndingOffsets() {
        return eventHub.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().eventDataWithOffset.isEmpty()
                                ? 0L
                                : entry.getValue().eventDataWithOffset.lastKey()
                ));
    }

    private boolean validateOffsets(Map<String, OffsetRange> offsets) {
        if (offsets == null) {
            return true;
        } else {

            if (offsets.size() != this.partitionCount) {
                throw new WrongPartitionCountException("Partition key is wrong, expected " + this.partitionCount + ", actual " + offsets.size());
            }

            Map<String, OffsetRange> possibleStartingOffsets = getPossibleOffsets();
            Map<String, Long> possibleEndingOffsets = getPossibleEndingOffsets();
            for (Map.Entry<String, OffsetRange> offset : offsets.entrySet()) {

                if (offset.getValue() == null) {
                    throw new NullOffsetRangeException("Partition value for partition key " + offset.getKey()
                            + " is absent");
                }

                long possibleStartingOffsetValue = possibleStartingOffsets.get(offset.getKey()).fromSeqNo();
                Long possibleEndingOffsetValue = possibleEndingOffsets.get(offset.getKey());
                if (possibleStartingOffsetValue > offset.getValue().fromSeqNo() || offset.getValue().fromSeqNo() > possibleEndingOffsetValue) {
                    throw new WrongOffsetsException("Wrong offset for partition key: " + offset.getKey() + ", "
                            + "possible starting offset: " + possibleStartingOffsetValue + ", "
                            + "possible ending offset: " + possibleEndingOffsetValue + ", "
                            + "requested offset: " + offset.getValue());
                }
            }
        }
        return true;
    }

    /**
     * Read batch by collecting all possible data using consumer group and max read count from all partitions.
     *
     * @param consumerGroupName consumer group name.
     * @return list of {@link EventData}
     */
    List<MockEventHubResult> readBatchUsingOffsets(String consumerGroupName) {
        return this.eventHub.values().stream()
                .map(partition -> partition.read(consumerGroupName, this.maxReadCount))
                .collect(Collectors.toList());
    }

    /**
     * Used mostly for tests.
     *
     * @param partitionKey      partition key.
     * @param consumerGroupName consumer group to read with.
     * @return list of EventData from specific partition.
     */
    List<EventData> readDataFromPartition(String partitionKey, String consumerGroupName) {
        EventHubPartition partition = this.eventHub.get(partitionKey);
        MockEventHubResult read = partition.read(consumerGroupName, this.maxReadCount);
        return read.getList();
    }

    private static class EventHubPartition {

        private String partitionKey;
        /**
         * Key is the offset of specific EventData, Value is this EventData.
         */
        private TreeMap<Long, EventData> eventDataWithOffset;
        /**
         * Key is the name of consumer group, Value is offset for this consumer group.
         */
        private Map<String, Long> offsets;

        private final AtomicLong offsetGenerator;

        private EventHubPartition(String partitionKey) {
            this.partitionKey = partitionKey;
            this.eventDataWithOffset = new TreeMap<>();
            this.offsets = new HashMap<>();
            this.offsetGenerator = new AtomicLong(0L);
        }

        synchronized void send(EventData eventData) {
            this.eventDataWithOffset.put(offsetGenerator.getAndIncrement(), eventData);
        }

        /**
         * Used only when receiving stream first time.
         *
         * @param consumerGroupName
         * @param offset
         */
        synchronized void moveOffset(String consumerGroupName, Long offset) {
            this.offsets.put(consumerGroupName, offset);
        }

        synchronized void moveOffsetToEnd(String consumerGroupName) {
            if (!eventDataWithOffset.isEmpty()) {
                this.offsets.put(consumerGroupName, eventDataWithOffset.lastKey() + 1);
            }
        }

        synchronized MockEventHubResult read(String consumerGroupName, int maxReadCount) {
            Long from = offsets.getOrDefault(consumerGroupName, 0L);
            long until = offsets.size() - from >= maxReadCount ? from + maxReadCount : offsets.size() - 1;
            offsets.put(consumerGroupName, until);
            List<EventData> list = eventDataWithOffset.entrySet().stream()
                    .filter(entry -> entry.getKey() >= from && entry.getKey() < maxReadCount)
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
            return new MockEventHubResult(partitionKey, from, until, list);
        }
    }
}
