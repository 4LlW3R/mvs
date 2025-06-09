package com.epam.tcodata.mock.external.pump.factory.impl.mix;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.converter.impl.EventConverter;
import com.epam.tcodata.external.pump.service.IOffsetService;
import com.epam.tcodata.external.pump.service.impl.EventOffsetService;
import com.epam.tcodata.external.pump.source.IMixSource;
import com.epam.tcodata.mock.external.pump.factory.MixMockAbstractExternalFactory;
import com.epam.tcodata.mock.external.pump.source.fact.impl.mix.MixMockEventMixSource;
import com.epam.tcodata.models.avro.fact.AvroEvent;
import com.epam.tcodata.models.enriched.fact.EnrichedEvent;
import com.epam.tcodata.models.mix.fact.Event;
import com.epam.tcodata.sql.dal.IDaoFactory;

public class MixMockExternalEventFactory extends MixMockAbstractExternalFactory<Event, EnrichedEvent, AvroEvent> {

    private static final long serialVersionUID = -6550083220277627779L;

    public MixMockExternalEventFactory() {
        super(Event.class);
    }

    @Override
    public IMixSource createMixSource() {
        return new MixMockEventMixSource();
    }

    @Override
    public IOffsetService createOffsetService(IDaoFactory daoFactory) {
        return new EventOffsetService(this, daoFactory);
    }

    @Override
    public IConverter createConverter() throws Exception {
        return new EventConverter();
    }
}
