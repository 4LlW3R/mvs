package com.epam.tcodata.tools.eventhub.consumer;

import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.Secret;
import com.epam.tcodata.secure.storage.dal.factory.ISecretStorageFactory;
import com.microsoft.azure.eventhubs.ConnectionStringBuilder;
import com.microsoft.azure.eventprocessorhost.EventProcessorHost;
import com.microsoft.azure.eventprocessorhost.EventProcessorOptions;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class EventProcessorSample {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventProcessorSample.class);

    /**
     * Run this to start consuming data from EH (parameters specified below).
     * @param args cli args.
     * @throws InterruptedException InterruptedException
     * @throws ExecutionException ExecutionException
     */
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // SETUP SETUP SETUP SETUP
        // Fill these strings in with the information of the Event Hub you wish to use. The consumer group
        // can probably be left as-is. You will also need the connection string for an Azure Storage account,
        // which is used to persist the lease and checkpoint data for this Event Hub. The Storage container name
        // indicates where the blobs used to implement leases and checkpoints will be placed within the Storage
        // account. All instances of EventProcessorHost which will be consuming from the same Event Hub and consumer
        // group must use the same Azure Storage account and container name.

        ISecretStorageFactory factory = ISecretStorageFactory.createDefaultFactory();
        ISecretStorage secretStorage = factory.createSecretStorage(new Properties());

        String consumerGroupName = "$Default";
        String namespaceName = "event-hub-mvsdatalake-t332mqa-namespace1";
        String eventHubName = "overtaking-violation";
        String sasKeyName = "RootManageSharedAccessKey";
        String sasKey = secretStorage.retrieveSecret(Secret.EventHub.Overtaking.accessKey);

        String storageContainerName = "development";
        String storageConnectionString = secretStorage.retrieveSecret(Secret.StorageAccount.MAIN.connectionString);
        String hostNamePrefix = "TestHost";

        // To conveniently construct the Event Hub connection string from the raw information, use the ConnectionStringBuilder class.
        ConnectionStringBuilder eventHubConnectionString = new ConnectionStringBuilder()
                .setNamespaceName(namespaceName)
                .setEventHubName(eventHubName)
                .setSasKeyName(sasKeyName)
                .setSasKey(sasKey);

        // Create the instance of EventProcessorHost using the most basic constructor. This constructor uses Azure Storage for
        // persisting partition leases and checkpoints. The host name, which identifies the instance of EventProcessorHost, must be unique.
        // You can use a plain UUID, or use the createHostName utility method which appends a UUID to a supplied string.
        EventProcessorHost host = EventProcessorHost.EventProcessorHostBuilder
                .newBuilder(EventProcessorHost.createHostName(hostNamePrefix), consumerGroupName)
                .useAzureStorageCheckpointLeaseManager(storageConnectionString, storageContainerName, null)
                .useEventHubConnectionString(eventHubConnectionString.toString(), eventHubName)
                .build();

        // Registering an event processor class with an instance of EventProcessorHost starts event processing. The host instance
        // obtains leases on some partitions of the Event Hub, possibly stealing some from other host instances, in a way that
        // converges on an even distribution of partitions across all host instances. For each leased partition, the host instance
        // creates an instance of the provided event processor class, then receives events from that partition and passes them to
        // the event processor instance.
        //
        // There are two error notification systems in EventProcessorHost. Notification of errors tied to a particular partition,
        // such as a receiver failing, are delivered to the event processor instance for that partition via the onError method.
        // Notification of errors not tied to a particular partition, such as initialization failures, are delivered to a general
        // notification handler that is specified via an EventProcessorOptions object. You are not required to provide such a
        // notification handler, but if you don't, then you may not know that certain errors have occurred.
        LOGGER.info("Registering host named {}", host.getHostName());
        EventProcessorOptions options = new EventProcessorOptions();
        options.setExceptionNotification(new ErrorNotificationHandler());

        host.registerEventProcessor(EventProcessor.class, options)
                .whenComplete((unused, e) ->
                {
                    // whenComplete passes the result of the previous stage through unchanged,
                    // which makes it useful for logging a result without side effects.
                    if (e != null) {
                        LOGGER.error("Failure while registering: {}", e);
                        if (e.getCause() != null) {
                            LOGGER.error("Inner exception: {}", e.getCause());
                        }
                    }
                })
                .thenAccept(unused ->
                {
                    // This stage will only execute if registerEventProcessor succeeded.
                    // If it completed exceptionally, this stage will be skipped.
                    LOGGER.info("Press enter to stop.");
                    try {
                        System.in.read();
                    } catch (Exception e) {
                        LOGGER.error("Keyboard read failed: {}", e);
                    }
                })
                .thenCompose(unused ->
                {
                    // This stage will only execute if registerEventProcessor succeeded.
                    //
                    // Processing of events continues until unregisterEventProcessor is called. Unregistering shuts down the
                    // receivers on all currently owned leases, shuts down the instances of the event processor class, and
                    // releases the leases for other instances of EventProcessorHost to claim.
                    return host.unregisterEventProcessor();
                })
                .exceptionally(e ->
                {
                    LOGGER.error("Failure while unregistering: {}", e);
                    if (e.getCause() != null) {
                        LOGGER.error("Inner exception: {}", e.getCause());
                    }
                    return null;
                })
                .get(); // Wait for everything to finish before exiting main!

        LOGGER.info("End of sample");
    }
}
