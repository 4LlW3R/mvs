package com.epam.tcodata.internal.pump.driver;

import com.epam.tcodata.common.FactoryUtil;
import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.internal.pump.factory.IInternalFactory;
import com.epam.tcodata.internal.pump.handler.IDataHandler;
import com.epam.tcodata.internal.pump.service.EventHubOffsetService;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.HiveOffset;
import com.epam.tcodata.sql.dal.service.pumps.IHiveOffsetService;
import com.microsoft.azure.eventhubs.EventData;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.eventhubs.rdd.HasOffsetRanges;
import org.apache.spark.eventhubs.rdd.OffsetRange;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;

import static com.epam.tcodata.internal.pump.util.DriverUtils.*;
import static com.epam.tcodata.models.ApplicationType.INTERNAL_PUMP;

public class InternalPumpBatchDriver {

    private static final Logger LOGGER = LoggerFactory.getLogger(InternalPumpBatchDriver.class);

    /**
     * Run internal pump.
     *
     * @param args expected arguments (appName master batchIntervalSeconds entityTypeCode databaseName tableName).
     * @throws InterruptedException for spark context.
     */
    public static void main(String[] args) throws Exception {
        Options options = prepareOptions();
        CommandLine commandLine = parseAsCommandLine(args, options);

        String factoryClassName = commandLine.getOptionValue(FACTORY_CLASS_NAME);

        IInternalFactory internalFactory = FactoryUtil.loadFactory(IInternalFactory.class, factoryClassName);
        ISecretStorage secretStorage = internalFactory.createSecretStorage();

        String appName = "InternalPump_" + internalFactory.getEntityType().name();
        SparkSession sparkSession = getSparkSession(appName);
        try (IDaoFactory pumpsDaoFactory = internalFactory.createPumpDaoFactory(secretStorage); JavaSparkContext sparkContext = new JavaSparkContext(sparkSession.sparkContext())) {
            IDataHandler handler = internalFactory.createEventDataHandler(sparkSession);
            handle(sparkContext, handler, internalFactory, pumpsDaoFactory, secretStorage);
        }
    }

    /**
     * Entry point for testing.
     *
     * @param sparkContext    java spark context.
     * @param handler         data handler.
     * @param internalFactory internal pump factory.
     * @param pumpsDaoFactory dao pump factory.
     */
    public static void handle(JavaSparkContext sparkContext,
                              IDataHandler handler,
                              IInternalFactory internalFactory,
                              IDaoFactory pumpsDaoFactory,
                              ISecretStorage secretStorage) {

        LOGGER.info("Preparing offsets...");
        IEventHub eventHub = internalFactory.createEventHub(secretStorage);
        EventHubOffsetService eventHubOffsetService = new EventHubOffsetService(eventHub, internalFactory.getEntityType(), pumpsDaoFactory);
        Map<String, OffsetRange> offsets = eventHubOffsetService.getOffsets();
        JavaRDD<EventData> eventDataJavaRDD = eventHub.receiveRdd(sparkContext, INTERNAL_PUMP.getConsumerGroup(), offsets);

        IHiveOffsetService hiveOffsetService = IDaoFactory.service(pumpsDaoFactory, HiveOffset.class);

        OffsetRange[] offsetRanges = ((HasOffsetRanges) eventDataJavaRDD.rdd()).offsetRanges();

        LOGGER.info("Offsets from RDD: {}", Arrays.toString(offsetRanges));
        handler.handle(eventDataJavaRDD, hiveOffsetService, internalFactory.getEntityType());
        eventHubOffsetService.updateOffsets(offsetRanges);
    }

}
