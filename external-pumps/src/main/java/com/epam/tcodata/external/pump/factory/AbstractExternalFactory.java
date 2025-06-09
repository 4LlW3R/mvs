package com.epam.tcodata.external.pump.factory;

import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.eventhub.dal.impl.EventHub;
import com.epam.tcodata.external.pump.dto.maker.IDtoMaker;
import com.epam.tcodata.external.pump.dto.maker.dimension.DimensionDtoMaker;
import com.epam.tcodata.external.pump.dto.maker.fact.FactDtoMaker;
import com.epam.tcodata.mdm.IKeyFactory;
import com.epam.tcodata.mdm.base.impl.KeyFactory;
import com.epam.tcodata.models.EntitySuperType;
import com.epam.tcodata.models.enriched.IEnrichable;
import com.epam.tcodata.models.mix.Entity;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.factory.ISecretStorageFactory;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.impl.pumps.PumpsDaoFactory;
import org.apache.avro.specific.SpecificRecord;
import org.apache.spark.sql.SparkSession;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

public abstract class AbstractExternalFactory<T extends Entity, S extends IEnrichable, U extends SpecificRecord>
        implements IExternalFactory<T, S, U> {

    private ISecretStorageFactory secretStorageFactory = null;

    private static final long serialVersionUID = -9005023057140964375L;
    private Class<T> entityClass;

    protected AbstractExternalFactory(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public ISecretStorage createSecretStorage() throws Exception {

        if (this.secretStorageFactory == null) {
            this.secretStorageFactory = createSecretStorageFactory();
        }
        return secretStorageFactory.createSecretStorage(new Properties());
    }

    @Override
    public IDaoFactory createPumpDaoFactory(ISecretStorage secretStorage) throws Exception {
        return new PumpsDaoFactory(secretStorage);
    }

    @Override
    public IKeyFactory createKeyFactory() throws Exception {
        return new KeyFactory();
    }

    @Override
    public Class<T> getEntityClass() {
        return this.entityClass;
    }

    @Override
    public IEventHub createEventHub(ISecretStorage secretStorage) {
        return new EventHub(getEventHubInfo(), secretStorage);
    }

    @Override
    public IDtoMaker<T> createDtoMaker(IDaoFactory daoFactory, SparkSession sparkSession) {
        if (getEntityType().getSuperType() == EntitySuperType.DIMENSION) {
            return new DimensionDtoMaker<>(daoFactory);
        }
        return new FactDtoMaker<>(daoFactory);
    }

    @Override
    public void setCurrentMoment(Instant instant) {
        // do nothing
    }

    @Override
    public Instant getCurrentMoment() {
        return Instant.now().truncatedTo(ChronoUnit.SECONDS);
    }

    protected ISecretStorageFactory createSecretStorageFactory() {
        return ISecretStorageFactory.createDefaultFactory();
    }
}
