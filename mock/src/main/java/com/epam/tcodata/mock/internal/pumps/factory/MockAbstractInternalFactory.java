package com.epam.tcodata.mock.internal.pumps.factory;

import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.hive.dal.HiveConfig;
import com.epam.tcodata.hive.dal.IHive;
import com.epam.tcodata.internal.pump.factory.AbstractInternalFactory;
import com.epam.tcodata.mock.eventhub.dal.MockEventHub;
import com.epam.tcodata.mock.hive.dal.MockHive;
import com.epam.tcodata.mock.secure.storage.dal.factory.impl.MockSecretStorageFactory;
import com.epam.tcodata.mock.sql.dal.impl.pumps.MockPumpsDaoFactory;
import com.epam.tcodata.mock.sql.dal.impl.speedlayer.MockSpeedLayerDaoFactory;
import com.epam.tcodata.models.datalake.raw.RawEntity;
import com.epam.tcodata.models.enriched.IEnrichable;
import com.epam.tcodata.models.mix.Entity;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.spark.sql.SparkSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public abstract class MockAbstractInternalFactory<T extends Entity, S extends IEnrichable, U extends SpecificRecordBase, L extends RawEntity>
        extends AbstractInternalFactory<T, S, U, L> {

    private Map<String, String> parameters = new HashMap<>();

    protected MockAbstractInternalFactory(Class<T> entityClass) {
        super(entityClass);
    }

    @Override
    public void setInitParameters(Map<String, String> parameters) {
        this.parameters.putAll(parameters);
    }

    @Override
    public ISecretStorage createSecretStorage() {
        return new MockSecretStorageFactory(this.parameters).createSecretStorage(new Properties());
    }

    @Override
    public IDaoFactory createPumpDaoFactory(ISecretStorage secretStorage) {
        return new MockPumpsDaoFactory(secretStorage, this.parameters);
    }

    @Override
    public IDaoFactory createSpeedLayerDaoFactory(ISecretStorage secretStorage) {
        return new MockSpeedLayerDaoFactory(secretStorage, this.parameters);
    }

    @Override
    public IEventHub createEventHub(ISecretStorage secretStorage) {
        return MockEventHub.instance();
    }

    @Override
    public IHive createHive(SparkSession sparkSession) {
        return MockHive.instance(HiveConfig.RAW, sparkSession);
    }
}
