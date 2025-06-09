package com.epam.tcodata.analytics.overtaking.detection.drivers;

import com.epam.tcodata.analytics.overtaking.detection.factory.IOvertakingDetectionFactory;
import com.epam.tcodata.analytics.overtaking.detection.overtaking.common.ConverterUtil;
import com.epam.tcodata.analytics.overtaking.detection.overtaking.common.types.OvertakingEvent;
import com.epam.tcodata.analytics.overtaking.detection.overtaking.gps.PureGPSProcessing;
import com.epam.tcodata.analytics.overtaking.detection.overtaking.gps.types.GPSPoint;
import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.models.ApplicationType;
import com.epam.tcodata.models.avro.fact.AvroPosition;
import com.epam.tcodata.models.avro.util.AvroSerDeUtil;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.storage.avro.entities.events.overtaking.OvertakingEventAvro;
import com.microsoft.azure.eventhubs.EventData;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import static com.epam.tcodata.analytics.overtaking.detection.Config.getSparkSession;

// TO DO Anonymous classes are used because otherwise we get (java.lang.IllegalArgumentException: Invalid lambda deserialization).
//  Do not change it without further investigation.
public class OvertakingDetectionStreamDriver implements IDriver, Serializable {

    private static final long serialVersionUID = -3563527293086247001L;

    private static final Logger LOGGER = LoggerFactory.getLogger(OvertakingDetectionStreamDriver.class);

    public static final String OPTION_BATCH_INTERVAL_SECONDS = "batchIntervalSeconds";

    @Override
    public void runJob(CommandLine commandLine, IOvertakingDetectionFactory factory) {
        JavaStreamingContext jsc = createStreamingContext(commandLine);
        ISecretStorage secretStorage = factory.createSecretStorage();
        IEventHub positionEventHub = factory.createPositionEventHub(secretStorage);
        JavaDStream<EventData> positionEventDataDStream = positionEventHub.receiveStream(
                jsc, ApplicationType.OVERTAKING_DETECTION.getConsumerGroup());
        JavaDStream<GPSPoint> gpsDStream = positionEventDataDStream
                .map(EventData::getBytes)
                .map(new Function<byte[], AvroPosition>() {
                    private static final long serialVersionUID = 4017263257116585298L;
                    @Override
                    public AvroPosition call(byte[] v1) throws Exception {
                        return AvroSerDeUtil.deserialize(AvroPosition.class, v1);
                    }
                })
                .map(new Function<AvroPosition, GPSPoint>() {
                    private static final long serialVersionUID = 2794098142489676751L;
                    @Override
                    public GPSPoint call(AvroPosition v1) {
                        return ConverterUtil.avroPositionToGPSPoint(v1);
                    }
                });
        OvertakingDetectionStreamDriver.handle(jsc, gpsDStream, factory);
        runGpsStream(jsc);
    }

    private static void handle(JavaStreamingContext jsc, JavaDStream<GPSPoint> gpsDStream, IOvertakingDetectionFactory factory) {
        String appName = jsc.sparkContext().appName();
        gpsDStream.foreachRDD(gpsRDD -> {
            SparkSession sparkSession = getSparkSession(appName);
            Dataset<GPSPoint> gpsDataset = sparkSession.createDataset(gpsRDD.rdd(), Encoders.bean(GPSPoint.class));
            Dataset<OvertakingEvent> overtakingDataset = PureGPSProcessing.processData(gpsDataset);
            sendToEventHub(overtakingDataset, factory);
        });
    }

    private static void sendToEventHub(Dataset<OvertakingEvent> overtakingDataset, IOvertakingDetectionFactory factory) {
        overtakingDataset
                .javaRDD()
                .map(new Function<OvertakingEvent, OvertakingEventAvro>() {
                    private static final long serialVersionUID = 206701682581954846L;
                    @Override
                    public OvertakingEventAvro call(OvertakingEvent v1) {
                        return ConverterUtil.convertOvertakingEventToAvro(v1);
                    }
                })
                .map(new Function<OvertakingEventAvro, byte[]>() {
                    private static final long serialVersionUID = 8088729792730355679L;
                    @Override
                    public byte[] call(OvertakingEventAvro v1) {
                        return AvroSerDeUtil.serialize(v1);
                    }
                })
                .map(EventData::create)
                .foreachPartition(iterator -> processPartition(iterator, factory));
    }

    private static void processPartition(Iterator<EventData> overtakingEventDataIterator, IOvertakingDetectionFactory factory) {
        List<EventData> overtakingEventDataList = IteratorUtils.toList(overtakingEventDataIterator);
        ISecretStorage secretStorage = factory.createSecretStorage();
        IEventHub overtakingEventHub = factory.createOvertakingEventHub(secretStorage);
        if (!overtakingEventDataList.isEmpty())
            overtakingEventHub.send(overtakingEventDataList);
    }

    private static JavaStreamingContext createStreamingContext(CommandLine cl) {
        LOGGER.info("Input params {} {} {}", (Object[]) cl.getArgs());

        long batchIntervalSeconds = Long.parseLong(cl.getOptionValue(OPTION_BATCH_INTERVAL_SECONDS));
        SparkConf conf = new SparkConf()
                .setAppName(cl.getOptionValue(OPTION_APP_NAME));
//        SparkConf conf = new SparkConf().setAppName(cl.getOptionValue(OPTION_APP_NAME)).setMaster("local[*]");
        return new JavaStreamingContext(conf, Durations.seconds(batchIntervalSeconds));
    }

    private static void runGpsStream(JavaStreamingContext jsc) {
        jsc.start();
        try {
            jsc.awaitTermination();
        } catch (InterruptedException e) {
            LOGGER.error("Exception during streaming: {}", e);
            Thread.currentThread().interrupt();
        }
        jsc.close();
    }
}
