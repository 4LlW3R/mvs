package com.epam.tcodata.internal.pump.factory;

import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.hive.dal.Hive;
import com.epam.tcodata.hive.dal.HiveConfig;
import com.epam.tcodata.hive.dal.IHive;
import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.internal.pump.handler.IDataHandler;
import com.epam.tcodata.internal.pump.service.entity.IEntityService;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.datalake.raw.RawEntity;
import com.epam.tcodata.models.enriched.IEnrichable;
import com.epam.tcodata.models.mix.Entity;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.speedlayer.ISpeedLayerEntity;
import com.epam.tcodata.sql.dal.impl.pumps.PumpsDaoFactory;
import com.epam.tcodata.sql.dal.impl.speedlayer.SpeedLayerDaoFactory;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.spark.sql.SparkSession;

import java.io.Serializable;
import java.util.Map;

public interface IInternalFactory<
        T extends Entity,
        S extends IEnrichable,
        U extends SpecificRecordBase,
        L extends RawEntity>

        extends Serializable {

    /**
     * Set extra parameters, that this factory may use for its own purposes.
     *
     * @param parameters
     */
    default void setInitParameters(Map<String, String> parameters) {

    }

    /**
     * Determines EntityTipe that this factory works with.
     *
     * @return EntityTipe.
     */
    default EntityType getEntityType() {
        Class<T> clazz = getEntityClass();

        return EntityType.byEntityClass(clazz);
    }

    /**
     * To every instance of internal pumps matches its own entity class.
     * These classes should belong to com.epam.tcodata.models.mix.* packets.
     *
     * @return class.
     */
    Class<T> getEntityClass();

    /**
     * Some entities should be written int SpeedLayer databases. This method determines if there is such matching.
     * In case it is not should return null. Classes should belong to com.epam.tcodata.sql.dal.domain.speedlayer.* packet.
     *
     * @return class.
     */
    default Class<? extends ISpeedLayerEntity> getSpeedLayerClass() {
        return null;
    }

    ISecretStorage createSecretStorage() throws Exception;

    default IDaoFactory createPumpDaoFactory(ISecretStorage secretStorage) throws Exception {
        return new PumpsDaoFactory(secretStorage);
    }

    default IDaoFactory createSpeedLayerDaoFactory(ISecretStorage secretStorage) throws Exception {
        return new SpeedLayerDaoFactory(secretStorage);
    }

    default IHive createHive(SparkSession sparkSession) {
        return new Hive(HiveConfig.RAW, sparkSession);
    }

    IEventHub createEventHub(ISecretStorage secretStorage);

    IDataHandler createEventDataHandler(SparkSession sparkSession);

    IEntityService createEntityService(SparkSession sparkSession);

    IEntityConverter<U, S, L> createConverter();
}
