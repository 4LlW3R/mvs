package com.epam.tcodata.external.pump.dto;

import com.epam.tcodata.external.pump.dto.maker.IDtoMaker;
import com.epam.tcodata.models.mix.Entity;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.rdd.RDD;
import org.apache.spark.streaming.Time;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.dstream.InputDStream;
import org.apache.spark.streaming.scheduler.StreamInputInfo;
import org.apache.spark.streaming.scheduler.StreamInputInfo$;
import scala.Option;
import scala.Predef;
import scala.Some;
import scala.collection.JavaConverters;
import scala.reflect.ClassTag;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class DtoInputStream<T extends Entity> extends InputDStream<AbstractDto<T>> {

    private static final long serialVersionUID = -1843091042479636604L;

    private transient JavaStreamingContext jsc;
    private transient IDtoMaker dtoMaker;
    private final Integer numSlices;

    /**
     * RequestInfoDto input stream.
     *
     * @param jsc         java streaming context.
     * @param dtoClassTag RequestInfoDto class tag.
     * @param dtoMaker    request service.
     */
    public DtoInputStream(JavaStreamingContext jsc,
                          ClassTag<AbstractDto<T>> dtoClassTag,
                          IDtoMaker<T> dtoMaker,
                          Integer numSlices) {
        super(jsc.ssc(), dtoClassTag);
        this.jsc = jsc;
        this.dtoMaker = dtoMaker;
        this.numSlices = numSlices;
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
    public Option<RDD<AbstractDto<T>>> compute(Time validTime) {
        JavaSparkContext sparkContext = jsc.sparkContext();
        RDD<AbstractDto<T>> rdd;

        rdd = (numSlices == null)
                ? sparkContext.parallelize(dtoMaker.makeDtoList()).rdd()
                : sparkContext.parallelize(dtoMaker.makeDtoList(), numSlices).rdd();

        reportSchedulingMetadata("Dto info", validTime, rdd.count());
        return new Some<>(rdd);
    }

    private void reportSchedulingMetadata(String description, Time validTime, long numberOfRecords) {
        // Report the record number and metadata of this batch interval to InputInfoTracker.

        Map<String, Object> metadata = new HashMap<>();
        metadata.put(StreamInputInfo$.MODULE$.METADATA_KEY_DESCRIPTION(), description);

        scala.collection.immutable.Map scalaMeta = JavaConverters.mapAsScalaMapConverter(metadata).asScala().toMap(Predef.conforms());

        StreamInputInfo inputInfo = new StreamInputInfo(id(), numberOfRecords, scalaMeta);
        this.context().scheduler().inputInfoTracker().reportInfo(validTime, inputInfo);
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
    }
}
