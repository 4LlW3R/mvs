package com.epam.tcodata.mock.eventhub.dal;

import com.microsoft.azure.eventhubs.EventData;
import org.apache.spark.Partition;
import org.apache.spark.TaskContext;
import org.apache.spark.api.java.JavaSparkContext$;
import org.apache.spark.eventhubs.rdd.HasOffsetRanges;
import org.apache.spark.eventhubs.rdd.OffsetRange;
import org.apache.spark.rdd.RDD;
import scala.collection.Iterator;

public class CustomRDD extends RDD<EventData> implements HasOffsetRanges {

    private final RDD<EventData> rdd;
    private OffsetRange[] ranges;

    CustomRDD(RDD<EventData> rdd, OffsetRange[] ranges) {
        super(rdd, JavaSparkContext$.MODULE$.fakeClassTag());
        this.rdd = rdd;
        this.ranges = ranges;
    }

    @Override
    public OffsetRange[] offsetRanges() {
        return this.ranges.clone();
    }

    @Override
    public Iterator<EventData> compute(Partition partition, TaskContext taskContext) {
        return this.rdd.compute(partition, taskContext);
    }

    @Override
    public Partition[] getPartitions() {
        return this.rdd.getPartitions();
    }
}
