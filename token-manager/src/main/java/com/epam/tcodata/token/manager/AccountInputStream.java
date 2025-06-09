package com.epam.tcodata.token.manager;

import com.epam.tcodata.sql.dal.domain.pumps.Account;
import com.epam.tcodata.sql.dal.service.pumps.IAccountService;
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

public class AccountInputStream extends InputDStream {

    private static final long serialVersionUID = -2674341132360432617L;
    private transient JavaStreamingContext jsc;
    private transient IAccountService accountService;

    /**
     * Account input stream main constructor.
     * @param jsc java streaming context.
     * @param classTag class tag.
     * @param accountService account service.
     */
    public AccountInputStream(JavaStreamingContext jsc, ClassTag<Account> classTag, IAccountService accountService) {
        super(jsc.ssc(), classTag);
        this.jsc = jsc;
        this.accountService = accountService;
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
    public Option<RDD<Account>> compute(Time time) {
        RDD<Account> rdd = jsc.sparkContext().parallelize(this.accountService.readAll()).rdd();
        reportSchedulingMetadata("Accounts info", time, rdd.count());
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

    /**
     * Does some thing in old style.
     *
     * @deprecated use {@link #new()} instead.
     */
    @Deprecated
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
    }

    /**
     * Does some thing in old style.
     *
     * @deprecated use {@link #new()} instead.
     */
    @Deprecated
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
    }

}
