package com.epam.tcodata.mock.external.pump.factory;

import com.epam.tcodata.external.pump.factory.AbstractExternalFactory;
import com.epam.tcodata.models.enriched.IEnrichable;
import com.epam.tcodata.models.mix.Entity;
import org.apache.avro.specific.SpecificRecord;

import java.time.Instant;

public abstract class MixMockAbstractExternalFactory<T extends Entity, S extends IEnrichable, U extends SpecificRecord>
        extends AbstractExternalFactory<T, S, U> {

    private Instant currentMoment;

    protected MixMockAbstractExternalFactory(Class<T> entityClass) {
        super(entityClass);
    }

    @Override
    public void setCurrentMoment(Instant currentMoment) {
        this.currentMoment = currentMoment;
    }

    @Override
    public Instant getCurrentMoment() {
        return this.currentMoment;
    }
}
