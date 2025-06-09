package com.epam.tcodata.eventhub.dal.impl;

import com.epam.tcodata.eventhub.dal.EventHubInfo;
import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.eventhub.dal.configuration.EventHubConfigManager;
import com.epam.tcodata.eventhub.dal.exceptions.ExceptionUtil;
import com.epam.tcodata.eventhub.dal.exceptions.RuntimeEventHubException;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.google.common.collect.Iterables;
import com.microsoft.azure.eventhubs.*;
import com.microsoft.azure.eventhubs.impl.ClientConstants;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.eventhubs.EventHubsConf;
import org.apache.spark.eventhubs.EventHubsUtils;
import org.apache.spark.eventhubs.EventPosition;
import org.apache.spark.eventhubs.NameAndPartition;
import org.apache.spark.eventhubs.rdd.OffsetRange;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Option;
import scala.Predef;
import scala.collection.JavaConverters;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

/**
 * Concrete realization of EventHub.
 */
public class EventHub implements IEventHub {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventHub.class);

    private final ConnectionStringBuilder connectionStringBuilder;
    private final int maxBatchSize;

    private static final int DEFAULT_EH_MAX_BATCH_SIZE = 1000;
    private static final String EVENT_HUB_EXCEPTION_MESSAGE = "EventHub exception";

    /**
     * Main constructor with SecretStorage.
     *
     * @param eventHubInfo  enum containing event hub credentials (without keys).
     * @param secretStorage secret storage instance to get sensitive data from there.
     */
    public EventHub(EventHubInfo eventHubInfo, ISecretStorage secretStorage) {
        this.connectionStringBuilder = EventHubConfigManager.createConnectionStringBuilder(eventHubInfo, secretStorage);
        this.maxBatchSize = DEFAULT_EH_MAX_BATCH_SIZE;
    }

    /**
     * Main constructor with SecretStorage.
     *
     * @param eventHubInfo  enum containing event hub credentials (without keys).
     * @param secretStorage secret storage instance to get sensitive data from there.
     * @param maxBatchSize  maximum size for one batch.
     */
    public EventHub(EventHubInfo eventHubInfo, ISecretStorage secretStorage, int maxBatchSize) {
        this.connectionStringBuilder = EventHubConfigManager.createConnectionStringBuilder(eventHubInfo, secretStorage);
        this.maxBatchSize = maxBatchSize;
    }

    /**
     * Synchronously sends given data (provided as byte[])
     * to Azure EventHub specified by its connection string builder.
     *
     * @param eventData    {@link EventData} to be sent.
     * @param partitionKey given partition key.
     */
    @Override
    public void send(EventData eventData, String partitionKey) {
        Objects.requireNonNull(eventData, "event data to send should not be null");

        ExceptionUtil.wrapEventhubExceptions(() -> {
            ScheduledExecutorService scheduledExecutorService = null;
            EventHubClient ehClient = null;
            try {
                scheduledExecutorService = Executors.newScheduledThreadPool(4);
                ehClient = EventHubClient.createFromConnectionStringSync(connectionStringBuilder.toString(), scheduledExecutorService);
                if (partitionKey == null) {
                    ehClient.sendSync(eventData);
                } else {
                    ehClient.sendSync(eventData, partitionKey);
                }

            } finally {
                if (ehClient != null) {
                    ehClient.closeSync();
                }
                if (scheduledExecutorService != null) {
                    scheduledExecutorService.shutdown();
                }
            }
        });
    }

    @Override
    public void send(Iterable<EventData> eventDataIterable, String partitionKey) {
        Objects.requireNonNull(eventDataIterable, "bytes iterable to send should not be null");

        if (Iterables.size(eventDataIterable) != 0) {
            ExceptionUtil.wrapEventhubExceptions(() -> {
                ScheduledExecutorService scheduledExecutorService = null;
                EventHubClient ehClient = null;
                try {
                    scheduledExecutorService = Executors.newScheduledThreadPool(4);
                    ehClient = EventHubClient.createFromConnectionStringSync(connectionStringBuilder.toString(), scheduledExecutorService);
                    if (isBatchPayloadExceedsMaxAllowed(eventDataIterable, this.maxBatchSize)) {
                        List<EventData> batchSlice = new ArrayList<>();
                        iterateEventData(eventDataIterable, partitionKey, batchSlice, ehClient);
                        sendDependingOnPartitionKeyPresence(partitionKey, ehClient, batchSlice);
                    } else {
                        sendDependingOnPartitionKeyPresence(partitionKey, ehClient, eventDataIterable);
                    }
                } finally {
                    if (ehClient != null) {
                        ehClient.closeSync();
                    }
                    if (scheduledExecutorService != null) {
                        scheduledExecutorService.shutdown();
                    }
                }
            });
        }
    }
    private void iterateEventData(Iterable<EventData> eventDataIterable, String partitionKey, List<EventData> batchSlice, EventHubClient ehClient) throws EventHubException {
        for (EventData eventData : eventDataIterable) {
            if (batchSlice.size() < this.maxBatchSize) {
                batchSlice.add(eventData);
            } else {
                sendDependingOnPartitionKeyPresence(partitionKey, ehClient, batchSlice);
                batchSlice.clear();
                batchSlice.add(eventData);
            }
        }
    }

    private static void sendDependingOnPartitionKeyPresence(
            String partitionKey, EventHubClient ehClient,
            Iterable<EventData> eventDataBatch) throws EventHubException {
        if (partitionKey == null) {
            ehClient.sendSync(eventDataBatch);
        } else {
            ehClient.sendSync(eventDataBatch, partitionKey);
        }
    }

    @Override
    public JavaDStream<EventData> receiveStream(JavaStreamingContext jsc, String consumerGroup) {
        return receiveStream(jsc, consumerGroup, null);
    }

    @Override
    public JavaDStream<EventData> receiveStream(JavaStreamingContext jsc, String consumerGroup, Map<String, OffsetRange> offsets) {
        EventHubsConf eventHubsConf = EventHubConfigManager
                .createEventHubsConf(connectionStringBuilder, consumerGroup);
        eventHubsConf.setMaxRatePerPartition(this.maxBatchSize);

        if (null != offsets) {
            eventHubsConf.setStartingPositions(JavaConverters.mapAsScalaMapConverter(convertOffsetsToEventHubFormat(offsets))
                    .asScala().toMap(Predef.conforms()));
        } else {
            eventHubsConf.setStartingPosition(EventPosition.fromEndOfStream());
        }
        return EventHubsUtils.createDirectStream(jsc, eventHubsConf);
    }

    @Override
    public JavaRDD<EventData> receiveRdd(JavaSparkContext sparkContext, String consumerGroup, Map<String, OffsetRange> offsets) {
        EventHubsConf eventHubsConf = EventHubConfigManager
                .createEventHubsConf(connectionStringBuilder, consumerGroup);

        if (!offsets.isEmpty()) {
            eventHubsConf.setStartingPositions(JavaConverters.mapAsScalaMapConverter(convertOffsetsToEventHubFormat(offsets))
                    .asScala().toMap(Predef.conforms()));
        } else {
            eventHubsConf.setStartingPosition(EventPosition.fromEndOfStream());
        }

        Map<String, OffsetRange> validatedOffsets = correctOffsetRange(offsets);

        OffsetRange[] offsetRanges = validatedOffsets.values().toArray(new OffsetRange[0]);
        return EventHubsUtils.createRDD(sparkContext, eventHubsConf, offsetRanges);
    }

    private Map<String, OffsetRange> correctOffsetRange(Map<String, OffsetRange> offsets) {
        return offsets.values()
                .stream()
                .map(offsetRange -> {
                    if (!(offsetRange.fromSeqNo() == 0 && offsetRange.untilSeqNo() == 0)) {
                        return new OffsetRange(
                                offsetRange.nameAndPartition(),
                                offsetRange.fromSeqNo(),
                                offsetRange.untilSeqNo() + 1, //because [fromSeqNo, untilSeqNo) is exclusive
                                offsetRange.preferredLoc());
                    } else {
                        return offsetRange;
                    }
                })
                .collect(Collectors.toMap(e -> String.valueOf(e.nameAndPartition().partitionId()),
                        e -> e)
                );
    }

    @Override
    public Map<String, OffsetRange> getPossibleOffsets() {
        Map<String, OffsetRange> offsets = new HashMap<>();
        ScheduledExecutorService scheduledExecutorService = null;
        EventHubClient ehClient = null;
        try {
            scheduledExecutorService = Executors.newScheduledThreadPool(4);
            ehClient = EventHubClient.createFromConnectionStringSync(connectionStringBuilder.toString(), scheduledExecutorService);
            EventHubRuntimeInformation eventHubRuntimeInformation = ehClient.getRuntimeInformation().get();
            String[] partitionIds = eventHubRuntimeInformation.getPartitionIds();
            for (String partitionId : partitionIds) {
                PartitionRuntimeInformation partInformation =
                        ehClient.getPartitionRuntimeInformation(partitionId).get();

                NameAndPartition nameAndPartition = new NameAndPartition(ehClient.getEventHubName(), Integer.parseInt(partitionId));
                long fromSeqNo = partInformation.getBeginSequenceNumber() > -1 ? partInformation.getBeginSequenceNumber() : 0;
                long untilSeqNo = partInformation.getLastEnqueuedSequenceNumber() > -1 ? partInformation.getLastEnqueuedSequenceNumber() : 0;
                offsets.put(partitionId, new OffsetRange(nameAndPartition, fromSeqNo, untilSeqNo, Option.empty()));
            }
        } catch (InterruptedException e) {
            LOGGER.error(EVENT_HUB_EXCEPTION_MESSAGE, e);
            Thread.currentThread().interrupt();
        } catch (EventHubException | IOException | ExecutionException e) {
            throw new RuntimeEventHubException(EVENT_HUB_EXCEPTION_MESSAGE, e);
        } finally {
            finallyTry(scheduledExecutorService, ehClient);
        }
        return offsets;
    }

    private void finallyTry(ScheduledExecutorService scheduledExecutorService, EventHubClient ehClient) {
        if (ehClient != null) {
            try {
                ehClient.closeSync();
            } catch (EventHubException e) {
                throw new RuntimeEventHubException(EVENT_HUB_EXCEPTION_MESSAGE, e);
            }
        }
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
        }
    }

    /**
     * Method returns partition count for given connectionStringBuilder.
     *
     * @return int partition count
     */
    @Override
    public int getPartitionCount() {
        ScheduledExecutorService scheduledExecutorService = null;
        EventHubClient ehClient = null;
        try {
            scheduledExecutorService = Executors.newScheduledThreadPool(4);
            ehClient = EventHubClient.createFromConnectionStringSync(connectionStringBuilder.toString(), scheduledExecutorService);
            EventHubRuntimeInformation eventHubRuntimeInformation;
            eventHubRuntimeInformation = ehClient.getRuntimeInformation().get();
            int partitionCount = eventHubRuntimeInformation.getPartitionCount();
            LOGGER.info("Partition count is {}", partitionCount);
            return partitionCount;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeEventHubException(EVENT_HUB_EXCEPTION_MESSAGE, e);
        } catch (EventHubException | IOException | ExecutionException e) {
            throw new RuntimeEventHubException(EVENT_HUB_EXCEPTION_MESSAGE, e);
        } finally {
            finallyTry(scheduledExecutorService, ehClient);
        }
    }

    /**
     * Method returns current offsets from database.
     *
     * @return map with offsets
     */
    private Map<NameAndPartition, EventPosition> convertOffsetsToEventHubFormat(Map<String, OffsetRange> offsets) {
        if (!offsets.isEmpty()) {
            return offsets.entrySet().stream()
                    .collect(Collectors.toMap(
                            offsetEntry -> new NameAndPartition(connectionStringBuilder.getEventHubName(),
                                    Integer.valueOf(offsetEntry.getKey())),
                            offsetEntry -> EventPosition.fromSequenceNumber(offsetEntry.getValue().fromSeqNo())));
        } else {
            throw new RuntimeEventHubException("Offsets can not be null!");
        }
    }

    /**
     * Max payload (both for batch and single message) is 256kB.
     * Note that in a batch send scenario the limit can include possible batch overhead.
     * See details
     * at <a href="http://go.microsoft.com/fwlink/?LinkId=761101">http://go.microsoft.com/fwlink/?LinkId=761101</a>
     *
     * @param dataBatch    data batch.
     * @param maxBatchSize maximum of batch size.
     * @return boolean (true - if need to slice data batch).
     */
    private static boolean isBatchPayloadExceedsMaxAllowed(Iterable<EventData> dataBatch, int maxBatchSize) {
        int batchSize = 0;

        if (dataBatch instanceof Collection) {
            batchSize = ((Collection<?>) dataBatch).size();
        }

        if (batchSize > maxBatchSize) {
            return true;
        }

        long batchPayload = computeBatchPayload(dataBatch);
        return batchPayload > ClientConstants.MAX_MESSAGE_LENGTH_BYTES;
    }

    /**
     * Note that actually payload can include possible batch overhead,
     * look at serialization logic in codebase of com.microsoft.azure.servicebus.MessageSender
     * public CompletableFuture(Void) send(final Iterable(Message) messages)
     *
     * @param dataBatch data batch.
     * @return batch payload.
     */
    private static long computeBatchPayload(Iterable<EventData> dataBatch) {
        long batchPayload = ClientConstants.MAX_EVENTHUB_AMQP_HEADER_SIZE_BYTES;
        for (EventData eventData : dataBatch) {
            batchPayload += eventData.getBytes().length;
        }
        return batchPayload;
    }

    /**
     * Extracts cached deeply inside in org.apache.spark.eventhubs.client.ClientThreadPool::pools private map
     * mapping EventHub configurations to ScheduledExecutorService serving them
     * so that ScheduledExecutorService could be stopped.
     *
     * @return {@code Map<String, ScheduledExecutorService>}
     */
    @SuppressWarnings({"JavaReflectionMemberAccess", "unchecked"})
    public static Map<String, ScheduledExecutorService> extractCachedThreadPools() {
        /*extract from memory heap dump object graph chain:
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

        Map<String, ScheduledExecutorService> result = null;
        Class<?> typeOfScalaSingleton;
        try {
            typeOfScalaSingleton = Class.forName("org.apache.spark.eventhubs.client.ClientThreadPool$");
            Field singletonField = typeOfScalaSingleton.getDeclaredField("MODULE$");
            singletonField.setAccessible(true);
            Object singletonInstance = singletonField.get(null);

            Field poolsField = typeOfScalaSingleton.getDeclaredField("pools");
            poolsField.setAccessible(true);

            //actual type
            //private[this] val pools = new MutableMap[String, ScheduledExecutorService]()
            Object poolStr2ExecutorMapInstance = poolsField.get(singletonInstance);
            scala.collection.mutable.HashMap<String, ScheduledExecutorService> poolMap = (scala.collection.mutable.HashMap<String, ScheduledExecutorService>) poolStr2ExecutorMapInstance;

//NOSONAR            LOGGER.info("singletonInstance type = {}", singletonInstance.getClass().getName());
//NOSONAR            LOGGER.info("poolStr2ExecutorMapInstance type = {}", poolStr2ExecutorMapInstance.getClass().getCanonicalName());
//NOSONAR            LOGGER.info("typed poolMap.size = {}", poolMap.size());

            result = JavaConverters.mapAsJavaMapConverter(poolMap).asJava();
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            LOGGER.error("Failed to extract cached by Spark Streaming Event Hub client thread pools", e);
        }

        return result;
    }
}
