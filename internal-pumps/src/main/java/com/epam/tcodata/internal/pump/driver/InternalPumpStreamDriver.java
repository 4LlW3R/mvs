package com.epam.tcodata.internal.pump.driver;

import com.epam.tcodata.common.FactoryUtil;
import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.eventhub.dal.configuration.EventHubConfigManager;
import com.epam.tcodata.eventhub.dal.impl.EventHub;
import com.epam.tcodata.internal.pump.factory.IInternalFactory;
import com.epam.tcodata.internal.pump.handler.IDataHandler;
import com.epam.tcodata.internal.pump.service.EventHubOffsetService;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.SignalType;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.HiveOffset;
import com.epam.tcodata.sql.dal.domain.pumps.Signal;
import com.epam.tcodata.sql.dal.service.pumps.IHiveOffsetService;
import com.epam.tcodata.sql.dal.service.pumps.ISignalService;
import com.microsoft.azure.eventhubs.EventData;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.spark.SparkEnv;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.eventhubs.rdd.HasOffsetRanges;
import org.apache.spark.eventhubs.rdd.OffsetRange;
import org.apache.spark.rpc.RpcEnv;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.epam.tcodata.internal.pump.util.DriverUtils.*;
import static com.epam.tcodata.models.ApplicationType.INTERNAL_PUMP;

public class InternalPumpStreamDriver {

    private static final Logger LOGGER = LoggerFactory.getLogger(InternalPumpStreamDriver.class);

    private static volatile boolean stopSignalReceived = false;

    /**
     * Run internal pump.
     *
     * @param args expected arguments (appName master batchIntervalSeconds entityTypeCode databaseName tableName).
     * @throws InterruptedException for spark context.
     */
    public static void main(String[] args) throws Exception {
        LOGGER.info("Parsing and Loading parameters...");
        Options options = prepareOptions();
        options.addOption(createMandatoryOption(BATCH_INTERVAL_SECONDS, BATCH_INTERVAL_SECONDS));
        CommandLine commandLine = parseAsCommandLine(args, options);

        long batchIntervalSeconds = Long.parseLong(commandLine.getOptionValue(BATCH_INTERVAL_SECONDS));
        String factoryClassName = commandLine.getOptionValue(FACTORY_CLASS_NAME);

        IInternalFactory internalFactory = FactoryUtil.loadFactory(IInternalFactory.class, factoryClassName);
        ISecretStorage secretStorage = internalFactory.createSecretStorage();
        try (IDaoFactory pumpsDaoFactory = internalFactory.createPumpDaoFactory(secretStorage)) {

            LOGGER.info("Creating javaStreamingContext...");
            String appName = "InternalPump_" + internalFactory.getEntityType().name();
            SparkSession sparkSession = getSparkSession(appName);
            JavaStreamingContext jsc = createJavaStreamContext(sparkSession, batchIntervalSeconds);
            IDataHandler handler = internalFactory.createEventDataHandler(sparkSession);

            LOGGER.info("Handling javaStreamingContext...");
            handleJavaStreamContext(jsc, handler, internalFactory, pumpsDaoFactory, secretStorage);

            LOGGER.info("Starting driver...");
            runPump(jsc);
        }
    }

    private static JavaStreamingContext createJavaStreamContext(SparkSession sparkSession, long batchIntervalSeconds) {
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkSession.sparkContext());
        return new JavaStreamingContext(javaSparkContext, Durations.seconds(batchIntervalSeconds));
    }

    /**
     * Entry point for testing.
     *
     * @param jsc             java streaming context.
     * @param handler         data handler.
     * @param internalFactory internal pump factory.
     * @param pumpsDaoFactory dao pump factory.
     */
    public static void handleJavaStreamContext(JavaStreamingContext jsc,
                                               IDataHandler handler,
                                               IInternalFactory internalFactory,
                                               IDaoFactory pumpsDaoFactory,
                                               ISecretStorage secretStorage) {

        LOGGER.info("Preparing offsets...");
        IEventHub eventHub = internalFactory.createEventHub(secretStorage);
        EventHubOffsetService eventHubOffsetService = new EventHubOffsetService(eventHub, internalFactory.getEntityType(), pumpsDaoFactory);
        Map<String, OffsetRange> offsets = eventHubOffsetService.getOffsets();
        LOGGER.info("Initial offsets: {}", offsets);
        JavaDStream<EventData> eventDataDStream = eventHub.receiveStream(jsc, INTERNAL_PUMP.getConsumerGroup(), offsets);

        eventDataDStream.foreachRDD(eventDataJavaRDD -> {
                    IHiveOffsetService hiveOffsetService = IDaoFactory.service(pumpsDaoFactory, HiveOffset.class);
                    if (shouldHandleBatch(jsc, pumpsDaoFactory, internalFactory.getEntityType())) {
                        OffsetRange[] offsetRanges = ((HasOffsetRanges) eventDataJavaRDD.rdd()).offsetRanges();
                        LOGGER.info("Offsets from RDD: {}", Arrays.asList(offsetRanges));
                        handler.handle(eventDataJavaRDD, hiveOffsetService, internalFactory.getEntityType());
                        eventHubOffsetService.updateOffsets(offsetRanges);
                    }
                }
        );
    }


    private static boolean shouldHandleBatch(JavaStreamingContext jsc, IDaoFactory pumpsDaoFactory, EntityType entityType) {
        boolean result = true;

        if (stopSignalReceived) {
            LOGGER.info("Stopping immediately...");
            stopContext(jsc);
            result = false;
        } else {
            ISignalService signalService = IDaoFactory.service(pumpsDaoFactory, Signal.class);
            List<Signal> signals = receiveSignals(signalService, entityType);
            LOGGER.info("Received signals: " + signals);
            if (!signals.isEmpty()) {
                signalService.deleteAll(signals);
                switch (SignalType.byCode(signals.get(0).getSignalType())) {
                    case STOP:
                        LOGGER.info("Stop signal received. Stopping immediately...");
                        stopContext(jsc);
                        result = false;
                        break;
                    case ONE_BATCH_STOP:
                        LOGGER.info("One batch stop signal received. One more batch...");
                        stopSignalReceived = true;
                        break;
                    default:
                        LOGGER.error("Wrong signal received...");
                        break;
                }
            }
        }

        return result;
    }

    private static void stopContext(JavaStreamingContext jsc) {
        final String methodLogPrefix = "InternalPumpDriver.stopContext(): ";

        SparkEnv sparkEnv = jsc.sparkContext().env();
        RpcEnv sparkRpcEnv = sparkEnv.rpcEnv();
        try {
            scheduleShutdownThreadPoolsAndExit();
        } finally {
            try {
                LOGGER.info(methodLogPrefix + "calling sparkContext().cancelAllJobs() ...");
                jsc.sparkContext().cancelAllJobs();
                LOGGER.info(methodLogPrefix + "... sparkContext().cancelAllJobs() done");
            } finally {
                try {
                    LOGGER.info(methodLogPrefix + "Stopping java streaming context with stopSparkContext=true, stopGracefully=false ...");
                    jsc.stop(true, false);
                    LOGGER.info(methodLogPrefix + "... JavaStreamingContext.stop() done");
                } finally {
                    try {
                        LOGGER.info(methodLogPrefix + "calling sparkContext().env().stop() ...");
                        sparkEnv.stop();
                        LOGGER.info(methodLogPrefix + "... sparkContext().env().stop() done");
                    } finally {
                        LOGGER.info(methodLogPrefix + "calling sparkContext().env().rpcEnv().shutdown()...");
                        sparkRpcEnv.shutdown();
                        LOGGER.info(methodLogPrefix + "... sparkContext().env().rpcEnv().shutdown() done");
                    }
                }
            }
        }
    }

    private static void scheduleShutdownThreadPoolsAndExit() {
        ScheduledExecutorService ses = null;
        try {
            LOGGER.info("pushed shutdownThreadPoolsAndExit() to run after 2 minutes");
            ses = Executors.newScheduledThreadPool(1);
            long stopTimeout = EventHubConfigManager.DEFAULT_OPERATION_TIMEOUT.plus(Duration.ofMinutes(1)).toMinutes();
            ses.schedule(() -> shutdownThreadPoolsAndExit(), stopTimeout, TimeUnit.MINUTES);
        } finally {
            if (ses != null) {
                ses.shutdown();
            }
        }
    }

    private static void shutdownThreadPoolsAndExit() {
        shutdownEventHubThreadPools();

        LOGGER.info("Calling system.exit(0)");
        System.exit(0);
    }

    /**
     * Shutdowns event hub thread pools.
     */
    public static void shutdownEventHubThreadPools() {
        final String methodLogPrefix = "InternalPumpStreamDriver.shutdownEventHubThreadPools(): ";
        LOGGER.info(methodLogPrefix + "try to shutdown all ScheduledThreadPools kept by org.apache.spark.eventhubs.client.ClientThreadPool");
            /*TODO: extract from memory chain:
               EventHubsDirectDStream::_client (org.apache.spark.eventhubs.client.EventHubsClient)
               -> EventHubsClient::_client (com.microsoft.azure.eventhubs.impl.EventHubClientImpl)
               being referred by
               object ClientConnectionPool (org.apache.spark.eventhubs.client.ClientConnectionPool$MODULE$)
               private[this] val pools = new MutableMap[String, ClientConnectionPool]()
               referencing element in map of
               object ClientThreadPool (org.apache.spark.eventhubs.client.ClientThreadPool)
               private[this] val pools = new MutableMap[String, ScheduledExecutorService]()

               Scheduled thread pools are working in default thread pool.
               We should call shutdown() on each instance referenced by
                    org.apache.spark.eventhubs.client.ClientThreadPool::pools map
             */
        Map<String, ScheduledExecutorService> poolsMap = EventHub.extractCachedThreadPools();
        LOGGER.info(methodLogPrefix + "shutdownEventHubThreadPools: got ClientThreadPool map of size {}", poolsMap.size());
        int idx = 0;
        for (Map.Entry<String, ScheduledExecutorService> kv : poolsMap.entrySet()) {
            LOGGER.info(methodLogPrefix + "Try to shutdown pool [idx={}, name={}]", idx, kv.getKey());
            try {
                kv.getValue().shutdown();
            } finally {
                LOGGER.info(methodLogPrefix + "done for pool [idx={}, name={}]", idx++, kv.getKey());
            }
        }
    }

    private static void runPump(JavaStreamingContext jsc) throws InterruptedException {
        jsc.start();
        jsc.awaitTermination();
        jsc.close();
    }
}
