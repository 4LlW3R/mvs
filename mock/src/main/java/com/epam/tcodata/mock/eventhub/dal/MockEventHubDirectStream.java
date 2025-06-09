package com.epam.tcodata.mock.eventhub.dal;

import com.microsoft.azure.eventhubs.EventData;
import org.apache.spark.eventhubs.NameAndPartition;
import org.apache.spark.eventhubs.rdd.OffsetRange;
import org.apache.spark.rdd.RDD;
import org.apache.spark.streaming.Time;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.dstream.InputDStream;
import scala.Option;
import scala.Some;
import scala.reflect.ClassTag;

import java.util.List;
import java.util.stream.Collectors;

public class MockEventHubDirectStream extends InputDStream<EventData> {

    private static final long serialVersionUID = 2408073930609437330L;

    private transient JavaStreamingContext jsc;
    private transient MockEventHub mockEventHub;
    private String consumerGroupName;

    /**
     * Public constructor with parameters.
     *
     * @param jsc               java streaming context.
     * @param classTag          class tag.
     * @param mockEventHub      event hub.
     * @param consumerGroupName consumer group name.
     */
    public MockEventHubDirectStream(JavaStreamingContext jsc,
                                    ClassTag<EventData> classTag,
                                    MockEventHub mockEventHub,
                                    String consumerGroupName) {
        super(jsc.ssc(), classTag);
        this.jsc = jsc;
        this.mockEventHub = mockEventHub;
        this.consumerGroupName = consumerGroupName;
    }

    @Override
    public void start() {
        /***  Default implementation ***/
    }

    @Override
    public void stop() {
        /***  Default implementation ***/
    }

    @Override
    public Option<RDD<EventData>> compute(Time validTime) {

        List<MockEventHubResult> mockEventHubResults = this.mockEventHub.readBatchUsingOffsets(this.consumerGroupName);

        List<OffsetRange> list = mockEventHubResults.stream().map(result -> new OffsetRange(new NameAndPartition(result.getPartitionKey(),
                Integer.parseInt(result.getPartitionKey())), result.getFrom(), result.getUntil(), Option.empty())).collect(Collectors.toList());

        List<EventData> data = mockEventHubResults.stream().flatMap(result -> result.getList().stream()).collect(Collectors.toList());

        RDD<EventData> rdd = this.jsc
                .sparkContext()
                .parallelize(data, this.mockEventHub.getPartitionCount())
                .rdd();

        CustomRDD customRDD = new CustomRDD(rdd, list.toArray(new OffsetRange[]{}));
        return new Some<>(customRDD);
    }
}
