package com.epam.tcodata.internal.pump.handler;

import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.internal.pump.converter.ISpeedLayerConverter;
import com.epam.tcodata.internal.pump.factory.IInternalFactory;
import com.epam.tcodata.internal.pump.service.entity.IEntityService;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.datalake.raw.RawEntity;
import com.epam.tcodata.models.enriched.IEnrichable;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.IStorable;
import com.epam.tcodata.sql.dal.domain.pumps.HiveOffset;
import com.epam.tcodata.sql.dal.domain.speedlayer.ISpeedLayerEntity;
import com.epam.tcodata.sql.dal.service.pumps.IHiveOffsetService;
import com.epam.tcodata.sql.dal.service.speedlayer.ISpeedLayerService;
import com.microsoft.azure.eventhubs.EventData;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.storage.StorageLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * This class is responsible for common behaviour of handlers. If handler need to have some specific,
 * so, it's needed to derive class from this one and override protected method handle(...).
 */
public abstract class AbstractDataHandler implements IDataHandler {

    private static final long serialVersionUID = 8382739282497224209L;

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDataHandler.class);

    protected final IInternalFactory internalFactory;

    private SparkSession sparkSession;

    /**
     * Main public constructor.
     *
     * @param internalFactory the factory that created this handler.
     * @param sparkSession    the Spark session in which this handler is actual.
     */
    protected AbstractDataHandler(IInternalFactory internalFactory, SparkSession sparkSession) {
        this.internalFactory = internalFactory;
        this.sparkSession = sparkSession;
    }

    @Override
    public void handle(JavaRDD<EventData> eventDataJavaRDD, IHiveOffsetService hiveOffsetService, EntityType entityType) {
        JavaRDD<IEnrichable> enrichedJavaRDD = convertToEnriched(eventDataJavaRDD);
        if (!enrichedJavaRDD.isEmpty()) {
            handle(enrichedJavaRDD, this.sparkSession, Timestamp.from(Instant.now()), hiveOffsetService, entityType);
        }
    }

    /**
     * Override this method if it's needed.
     *
     * @param enrichedJavaRDD   RDD that contains enriched entities.
     * @param sparkSession      spark session object.
     * @param hiveOffsetService
     */
    protected void handle(JavaRDD<IEnrichable> enrichedJavaRDD,
                          SparkSession sparkSession,
                          Timestamp persistedDateUtc,
                          IHiveOffsetService hiveOffsetService,
                          EntityType entityType) {

        IEntityConverter entityConverter = this.internalFactory.createConverter();
        JavaRDD<RawEntity> rawEntityJavaRDD = enrichedJavaRDD.map(enriched -> entityConverter.convertToRaw(enriched, persistedDateUtc));
        JavaRDD<RawEntity> persistedEntityJavaRDD = rawEntityJavaRDD.persist(StorageLevel.MEMORY_ONLY()).repartition(3);

        IEntityService entityService = this.internalFactory.createEntityService(sparkSession);

        // write entities themselves into Hive
        entityService.write(entityConverter.convertToDataset(sparkSession, persistedEntityJavaRDD));

        // write hive offset record
        LOGGER.info("Storing in HiveOffset...");
        long count = persistedEntityJavaRDD.count();
        LOGGER.info("Entities count: {}", count);
        insertHiveOffsetRecord(hiveOffsetService, internalFactory.getEntityType(), persistedDateUtc, (int) count);

        // if it's needed write speed layer
        Class<?> speedLayerClass = this.internalFactory.getSpeedLayerClass();
        if (speedLayerClass != null) {
            LOGGER.info("Storing in speed layer...");
            JavaRDD<IStorable> speedLayerJavaRDD = convertToSpeedLayer(enrichedJavaRDD).coalesce(3);
            speedLayerJavaRDD.foreachPartition(iterator -> {
                ISecretStorage secretStorage = this.internalFactory.createSecretStorage();
                try (IDaoFactory speedLayerDaoFactory = this.internalFactory.createSpeedLayerDaoFactory(secretStorage)) {
                    if (iterator.hasNext() && speedLayerDaoFactory.knownEntityClasses().contains(speedLayerClass)) {
                        ISpeedLayerService speedLayerService = IDaoFactory.service(speedLayerDaoFactory, speedLayerClass);
                        speedLayerService.insertBatch(IteratorUtils.toList(iterator));
                    }
                }
            });
        }
        persistedEntityJavaRDD.unpersist();
    }

    protected void insertHiveOffsetRecord(IHiveOffsetService hiveOffsetService,
                                          EntityType entityType,
                                          Timestamp persistedDateUtc,
                                          int count) {
        HiveOffset hiveOffset = new HiveOffset();
        hiveOffset.setEntityType(entityType.getCode());
        hiveOffset.setPersistedDateUtc(persistedDateUtc);
        hiveOffset.setElementCount(count);
        hiveOffsetService.insert(Arrays.asList(hiveOffset));
    }

    private JavaRDD<IStorable> convertToSpeedLayer(JavaRDD<IEnrichable> enrichedJavaRDD) {
        IEntityConverter entityConverter = this.internalFactory.createConverter();

        return enrichedJavaRDD
                .map((Function<IEnrichable, ISpeedLayerEntity>) ((ISpeedLayerConverter) entityConverter)::convertToSpeedLayer)
                .filter(Objects::nonNull)
                .mapPartitions(speedLayerEntityIterator -> {
                    List<IStorable> speedLayerEntityList = IteratorUtils.toList(speedLayerEntityIterator);
                    return speedLayerEntityList.iterator();
                })
                .map(e -> e);
    }


    private JavaRDD<IEnrichable> convertToEnriched(JavaRDD<EventData> eventDataJavaRDD) {

        IEntityConverter entityConverter = this.internalFactory.createConverter();

        return eventDataJavaRDD
                .map(EventData::getBytes)
                .map(new Function<byte[], SpecificRecordBase>() {
                    private static final long serialVersionUID = 4637016979714510143L;

                    @Override
                    public SpecificRecordBase call(byte[] v1) {
                        return entityConverter.convertToAvro(internalFactory.getEntityType().getAvroClass(), v1);
                    }
                })
                // TO DO Anonymous classes are used because otherwise we get (java.lang.IllegalArgumentException: Invalid lambda deserialization).
                //  Do not change it without further investigation.
                .map(new Function<SpecificRecordBase, IEnrichable>() {
                    private static final long serialVersionUID = -6021494033046988476L;

                    @Override
                    public IEnrichable call(SpecificRecordBase v1) {
                        return entityConverter.convertToEnriched(v1);
                    }
                })
                // TO DO Anonymous classes are used because otherwise we get (java.lang.IllegalArgumentException: Invalid lambda deserialization).
                //  Do not change it without further investigation.
                .mapPartitions(enrichedEntityIterator -> {
                    List<IEnrichable> enrichedEntityList = IteratorUtils.toList(enrichedEntityIterator);
                    return enrichedEntityList.iterator();
                })
                .persist(StorageLevel.MEMORY_AND_DISK_SER());
    }
}
