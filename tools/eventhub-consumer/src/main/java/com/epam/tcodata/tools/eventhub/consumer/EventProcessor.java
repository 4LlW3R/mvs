package com.epam.tcodata.tools.eventhub.consumer;

import com.epam.tcodata.models.avro.util.AvroSerDeUtil;
import com.epam.tcodata.storage.avro.entities.events.overtaking.v2.OvertakingEventWithViolationsAvro;
import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventprocessorhost.CloseReason;
import com.microsoft.azure.eventprocessorhost.IEventProcessor;
import com.microsoft.azure.eventprocessorhost.PartitionContext;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

public class EventProcessor implements IEventProcessor {

    private static final Logger LOGGER = Logger.getLogger(EventProcessor.class);
    public static final String USER_DIR_PROPERTY = "user.dir";
    public static final String SAMPLE_PARTITION = "SAMPLE: Partition ";

    // OnOpen is called when a new event processor instance is created by the host. In a real implementation, this
    // is the place to do initialization so that events can be processed when they arrive, such as opening a database
    // connection.
    @Override
    public void onOpen(PartitionContext context) throws Exception {
        LOGGER.info("{}{} is opening ", SAMPLE_PARTITION, context.getPartitionId());
    }

    // OnClose is called when an event processor instance is being shut down. The reason argument indicates whether the shut down
    // is because another host has stolen the lease for this partition or due to error or host shutdown. In a real implementation,
    // this is the place to do cleanup for resources that were opened in onOpen.
    @Override
    public void onClose(PartitionContext context, CloseReason reason) throws Exception {
        LOGGER.info("{}{} is closing for reason {} ", SAMPLE_PARTITION, context.getPartitionId(), reason);
    }

    // onError is called when an error occurs in EventProcessorHost code that is tied to this partition, such as a receiver failure.
    // It is NOT called for exceptions thrown out of onOpen/onClose/onEvents. EventProcessorHost is responsible for recovering from
    // the error, if possible, or shutting the event processor down if not, in which case there will be a call to onClose. The
    // notification provided to onError is primarily informational.
    @Override
    public void onError(PartitionContext context, Throwable error) {
        LOGGER.info("{}{} onError: {}", SAMPLE_PARTITION, context.getPartitionId(), error);
    }

    // onEvents is called when events are received on this partition of the Event Hub. The maximum number of events in a batch
    // can be controlled via EventProcessorOptions. Also, if the "invoke processor after receive timeout" option is set to true,
    // this method will be called with null when a receive timeout occurs.
    @Override
    public void onEvents(PartitionContext context, Iterable<EventData> events) throws Exception {
        LOGGER.info("{}{} got event batch", SAMPLE_PARTITION, context.getPartitionId());
        int eventCount = 0;
        File file = new File(System.getProperty(USER_DIR_PROPERTY) + File.separator + "file.csv");
        for (EventData data : events) {
            // It is important to have a try-catch around the processing of each event. Throwing out of onEvents deprives
            // you of the chance to process any remaining events in the batch.
            try {
                OvertakingEventWithViolationsAvro avro = AvroSerDeUtil.deserialize(OvertakingEventWithViolationsAvro.class, data.getBytes());
                LOGGER.info(avro);

                try (Writer w = new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8);
                        PrintWriter out = new PrintWriter(w)) {
                    out.println(avro.toString());
                }

                eventCount++;

                // Checkpointing persists the current position in the event stream for this partition and means that the next
                // time any host opens an event processor on this event hub+consumer group+partition combination, it will start
                // receiving at the event after this one. Checkpointing is usually not a fast operation, so there is a tradeoff
                // between checkpointing frequently (to minimize the number of events that will be reprocessed after a crash, or
                // if the partition lease is stolen) and checkpointing infrequently (to reduce the impact on event processing
                // performance). Checkpointing every five events is an arbitrary choice for this sample.
                LOGGER.info("{}{} checkpointing at {}, {}", SAMPLE_PARTITION, context.getPartitionId(),
                        data.getSystemProperties().getOffset(), data.getSystemProperties().getSequenceNumber());
                // Checkpoints are created asynchronously. It is important to wait for the result of checkpointing
                // before exiting onEvents or before creating the next checkpoint, to detect errors and to ensure proper ordering.
                context.checkpoint(data).get();
            } catch (InterruptedException e) {
                LOGGER.error("Processing failed for an event: {}", e));
                Thread.currentThread().interrupt();
            } catch (IOException | InterruptedException | ExecutionException e) {
                LOGGER.error("Processing failed for an event: {}", e));
                throw new RuntimeException(e);
            }
        }
        LOGGER.info("{}{} batch size was {} for host {}", SAMPLE_PARTITION, context.getPartitionId(), eventCount, context.getOwner());
    }
}
