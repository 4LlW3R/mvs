package com.epam.tcodata.mock.external.pump.factory.impl;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.converter.impl.EventConverter;
import com.epam.tcodata.external.pump.service.IOffsetService;
import com.epam.tcodata.external.pump.service.impl.EventOffsetService;
import com.epam.tcodata.external.pump.source.IMixSource;
import com.epam.tcodata.mock.external.pump.factory.MockAbstractExternalFactory;
import com.epam.tcodata.mock.external.pump.source.fact.impl.MockEventMixSource;
import com.epam.tcodata.models.avro.fact.AvroEvent;
import com.epam.tcodata.models.enriched.fact.EnrichedEvent;
import com.epam.tcodata.models.mix.fact.Event;
import com.epam.tcodata.sql.dal.IDaoFactory;

public class MockExternalEventFactory extends MockAbstractExternalFactory<Event, EnrichedEvent, AvroEvent> {

    private static final long serialVersionUID = 68194244038819658L;

    public MockExternalEventFactory() {
        super(Event.class);
    }

    @Override
    public IMixSource<Event> createMixSource() {
        return new MockEventMixSource();
    }

    @Override
    public IOffsetService createOffsetService(IDaoFactory daoFactory) {
        return new EventOffsetService(this, daoFactory);
    }

    @Override
    public IConverter<Event, EnrichedEvent, AvroEvent> createConverter() throws Exception {
        return new EventConverter();
    }
}
