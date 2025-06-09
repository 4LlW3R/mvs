package com.epam.tcodata.raw.prepared.etl.factory;

import com.epam.tcodata.hive.dal.IHive;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.datalake.prepared.PreparedEntity;
import com.epam.tcodata.models.datalake.raw.RawEntity;
import com.epam.tcodata.raw.prepared.etl.ReferenceSupplier;
import com.epam.tcodata.raw.prepared.etl.converter.ISingleDomainModelConverter;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import org.apache.spark.sql.SparkSession;

import java.io.Serializable;
import java.util.Map;

public interface ISDMFactory<T extends RawEntity, U extends PreparedEntity>
        extends Serializable {

    /**
     * Set extra parameters, that this factory may use for its own purposes.
     *
     * @param parameters
     */
    default void setInitParameters(Map<String, String> parameters) {

    }

    default EntityType getEntityType() {
        Class<T> rawEntityClass = getRawEntityClass();
        return EntityType.byRawDataLakeClass(rawEntityClass);
    }

    /**
     * To every instance of internal pumps matches its own entity class.
     * These classes should belong to com.epam.tcodata.models.datalake.raw.* packets.
     *
     * @return class.
     */
    Class<T> getRawEntityClass();

    Class<U> getPreparedEntityClass();

    ISecretStorage createSecretStorage() throws Exception;

    IDaoFactory createPumpDaoFactory(ISecretStorage secretStorage) throws Exception;

    IHive createRawHive(SparkSession sparkSession);

    IHive createPreparedHive(SparkSession sparkSession);

    /**
     * Returns converter from raw to prepared area.
     * @param referenceSupplier
     * @return converter from raw to prepared area.
     */
    ISingleDomainModelConverter<T, U> createConverter(ReferenceSupplier referenceSupplier);
}
