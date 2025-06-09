package com.epam.tcodata.eventhub.dal.configuration;


import com.epam.tcodata.common.ConfigBuilder;
import com.epam.tcodata.eventhub.dal.EventHubInfo;
import com.epam.tcodata.secure.storage.dal.ISecretIdentity;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.Secret;
import com.microsoft.azure.eventhubs.ConnectionStringBuilder;
import org.apache.spark.eventhubs.EventHubsConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Locale;
import java.util.Map;


/**
 * Utility file to work with Azure EventHub configuration.
 */
public final class EventHubConfigManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventHubConfigManager.class);

    private static final String PROPERTIES_FILE = "eventhub.properties";
    private static final String NAMESPACE_POSTFIX = ".eventHub.namespace";
    private static final String EVENT_HUB_POSTFIX = ".eventHub";
    private static final String SHARED_ACCESS_KEY_NAME_POSTFIX = ".eventHub.sharedAccessKeyName";
    private static final String EH_ENDPOINT_TEMPLATE = "sb://%s.servicebus.windows.net/";

    public static final Duration DEFAULT_OPERATION_TIMEOUT = Duration.ofMinutes(1L);

    private EventHubConfigManager() {
    }

    /**
     * Creates {@link ConnectionStringBuilder} (which can be used to establish communication
     * with Event Hub instances) using {@link EventHubInfo}.
     *
     * @param eventHubInfo Enum that stores EventHub namespace type and EventHub name.
     * @return {@link ConnectionStringBuilder}
     */
    public static ConnectionStringBuilder createConnectionStringBuilder(EventHubInfo eventHubInfo, ISecretStorage secretStorage) {
        String namespaceProp = eventHubInfo.getNamespaceType() + NAMESPACE_POSTFIX;
        String eventHubProp = eventHubInfo.getNamespaceType() + "." + eventHubInfo.name().toLowerCase() + EVENT_HUB_POSTFIX;
        String sharedAccessKeyNameProp = eventHubInfo.getNamespaceType() + SHARED_ACCESS_KEY_NAME_POSTFIX;
        ConfigBuilder builder = new ConfigBuilder()
                .setResourceParametersFileName(PROPERTIES_FILE)
                .addParameter(sharedAccessKeyNameProp, true)
                .addParameter(namespaceProp, true)
                .addParameter(eventHubProp, true);
        Map<String, String> params = builder.build();

        URI probeUri;
        String namespaceName = params.get(namespaceProp);
        try {
            probeUri = new URI(String.format(Locale.US, EH_ENDPOINT_TEMPLATE, namespaceName));
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(String.format(Locale.US, "Invalid namespace name: %s", namespaceName), ex);
        }

        String eventHub = params.get(eventHubProp);
        ISecretIdentity accessKeyIdentity = Secret.EventHub.accessKey.usingSection(eventHubInfo.getNameSpace().name());
        String sharedAccessKey = secretStorage.retrieveSecret(accessKeyIdentity);
        String sharedAccessKeyName =  params.get(sharedAccessKeyNameProp);

        return formConnectionStringBuilder(probeUri, eventHub, sharedAccessKeyName, sharedAccessKey);
    }


    /**
     * Creating {@link EventHubsConf}.
     *
     * @param connectionStringBuilder connection string builder
     * @param consumerGroupName       consumer group name
     * @return EH configuration.
     */
    public static EventHubsConf createEventHubsConf(ConnectionStringBuilder connectionStringBuilder, String consumerGroupName) {
        try {
            Constructor<EventHubsConf> constructor = EventHubsConf.class.getDeclaredConstructor(String.class);
            constructor.setAccessible(true);
            return constructor.newInstance(connectionStringBuilder.toString())
                    .setConsumerGroup(consumerGroupName);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            String msg = "Exception in creating new instance of EventHubsConf.";
            LOGGER.error(msg);
            throw new RuntimeException(msg, ex);
        }
    }

    private static ConnectionStringBuilder formConnectionStringBuilder(URI probeUri, String eventHubName, String sasKeyName, String sasKey) {
        return new ConnectionStringBuilder()
                .setEndpoint(probeUri)
                .setEventHubName(eventHubName)
                .setSasKeyName(sasKeyName)
                .setSasKey(sasKey)
                .setOperationTimeout(DEFAULT_OPERATION_TIMEOUT);
    }

    /**
     * Creates dummy {@link ConnectionStringBuilder} using given namespace and event hub name.
     * Use for test purposes only.
     *
     * @return {@link ConnectionStringBuilder}
     */
    public static ConnectionStringBuilder createTestConnectionStringBuilder(String namespaceName, String entityEventHubName) {
        URI probeUri;
        try {
            probeUri = new URI(String.format(Locale.US, EH_ENDPOINT_TEMPLATE, namespaceName));
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(String.format(Locale.US, "Invalid namespace name: %s", namespaceName), ex);
        }
        return formConnectionStringBuilder(probeUri, entityEventHubName, "", "");
    }
}
