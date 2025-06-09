package com.epam.tcodata.eventhub.dal;

import com.microsoft.azure.eventhubs.EventData;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.eventhubs.rdd.OffsetRange;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import java.util.Map;

/**
 * Main entry point to all EventHub functionality provided in this module, which is following:
 * sending {@link EventData}(main transfer logic entity in EventHub - easily serialized and deserialized)
 * entities to EventHub, receiving stream {@link JavaDStream} of EventData from EventHub specific type{@link com.microsoft.azure.eventhubs.EventData}, getting meta information
 * from EventHub eventhub(analogue of topic in Kafka).
 */
public interface IEventHub {

    default void send(EventData eventData) {
        send(eventData, null);
    }

    void send(EventData eventData, String partitionKey);

    default void send(Iterable<EventData> eventDataIterable) {
        send(eventDataIterable, null);
    }

    void send(Iterable<EventData> eventDataIterable, String partitionKey);

    JavaDStream<EventData> receiveStream(JavaStreamingContext jsc, String consumerGroup);

    JavaDStream<EventData> receiveStream(JavaStreamingContext jsc, String consumerGroup, Map<String, OffsetRange> offsets);

    JavaRDD<EventData> receiveRdd(JavaSparkContext sparkContext, String consumerGroup, Map<String, OffsetRange> offsets);

    Map<String, OffsetRange> getPossibleOffsets();

    int getPartitionCount();
}
