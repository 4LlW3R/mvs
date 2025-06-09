package com.epam.tcodata.internal.pump.factory;

import com.epam.tcodata.internal.pump.service.entity.DimensionService;
import com.epam.tcodata.internal.pump.service.entity.FactService;
import com.epam.tcodata.internal.pump.service.entity.IEntityService;
import com.epam.tcodata.models.EntitySuperType;
import com.epam.tcodata.models.datalake.raw.RawEntity;
import com.epam.tcodata.models.enriched.IEnrichable;
import com.epam.tcodata.models.mix.Entity;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.factory.ISecretStorageFactory;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.spark.sql.SparkSession;

import java.util.Properties;

/**
 * This class defines different behaviour for both fact entities and dimension entities to avoid repeating
 * the same code into derived classes.
 *
 * @param <T> entity type (MIX entities)
 * @param <S> enrichable type (MIX entities with durable keys)
 * @param <U> base Avro entity type
 * @param <L> data lake landed entity type
 */
public abstract class AbstractInternalFactory<T extends Entity, S extends IEnrichable, U extends SpecificRecordBase, L extends RawEntity>
        implements IInternalFactory<T, S, U, L> {

    private static final long serialVersionUID = -3855670016368732561L;

    private ISecretStorageFactory defaultFactory = ISecretStorageFactory.createDefaultFactory();

    private Class<T> entityClass;

    /**
     * Public main constructor.
     *
     * @param entityClass entity class.
     */
    protected AbstractInternalFactory(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public Class<T> getEntityClass() {
        return this.entityClass;
    }

    @Override
    public ISecretStorage createSecretStorage() throws Exception {
        Properties emptyProperty = new Properties();
        return this.defaultFactory.createSecretStorage(emptyProperty);
    }

    @Override
    public IEntityService createEntityService(SparkSession sparkSession) {

        if (getEntityType().getSuperType() == EntitySuperType.DIMENSION) {
            return new DimensionService(this, sparkSession);
        }
        return new FactService(this, sparkSession);
    }
}
