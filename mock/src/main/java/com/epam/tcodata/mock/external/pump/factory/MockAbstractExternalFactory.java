package com.epam.tcodata.mock.external.pump.factory;

import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.external.pump.factory.AbstractExternalFactory;
import com.epam.tcodata.mdm.IKeyFactory;
import com.epam.tcodata.mock.eventhub.dal.MockEventHub;
import com.epam.tcodata.mock.mdm.base.impl.MockKeyFactory;
import com.epam.tcodata.mock.secure.storage.dal.factory.impl.MockSecretStorageFactory;
import com.epam.tcodata.mock.sql.dal.impl.pumps.MockPumpsDaoFactory;
import com.epam.tcodata.models.enriched.IEnrichable;
import com.epam.tcodata.models.mix.Entity;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.factory.ISecretStorageFactory;
import com.epam.tcodata.sql.dal.IDaoFactory;
import org.apache.avro.specific.SpecificRecord;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public abstract class MockAbstractExternalFactory<T extends Entity, S extends IEnrichable, U extends SpecificRecord>
        extends AbstractExternalFactory<T, S, U> {

    private Map<String, String> parameters = new HashMap<>();
    private Instant currentMoment;

    /**
     * Public main constructor with parameters.
     *
     * @param entityClass entity class.
     */
    protected MockAbstractExternalFactory(Class<T> entityClass) {
        super(entityClass);
    }

    @Override
    public void setInitParameters(Map<String, String> parameters) {
        this.parameters.putAll(parameters);
    }

    @Override
    public IDaoFactory createPumpDaoFactory(ISecretStorage secretStorage) throws Exception {
        return new MockPumpsDaoFactory(secretStorage, this.parameters);
    }

    @Override
    public ISecretStorage createSecretStorage() throws Exception {
        return new MockSecretStorageFactory(this.parameters).createSecretStorage(new Properties());
    }

    @Override
    public IKeyFactory createKeyFactory() throws Exception {
        IKeyFactory keyFactory = MockKeyFactory.instance();
        keyFactory.setInitParameters(this.parameters);
        return keyFactory;
    }

    @Override
    public IEventHub createEventHub(ISecretStorage secretStorage) {
        return MockEventHub.instance();
    }

    @Override
    public void setCurrentMoment(Instant currentMoment) {
        this.currentMoment = currentMoment;
    }

    @Override
    public Instant getCurrentMoment() {
        return this.currentMoment;
    }

    @Override
    protected ISecretStorageFactory createSecretStorageFactory() {
        return new MockSecretStorageFactory(this.parameters);
    }
}
