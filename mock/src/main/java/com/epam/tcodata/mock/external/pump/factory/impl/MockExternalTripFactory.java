package com.epam.tcodata.mock.external.pump.factory.impl;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.converter.impl.TripConverter;
import com.epam.tcodata.external.pump.service.IOffsetService;
import com.epam.tcodata.external.pump.service.impl.TripOffsetService;
import com.epam.tcodata.external.pump.source.IMixSource;
import com.epam.tcodata.mock.external.pump.factory.MockAbstractExternalFactory;
import com.epam.tcodata.mock.external.pump.source.fact.impl.MockTripMixSource;
import com.epam.tcodata.models.avro.fact.AvroTrip;
import com.epam.tcodata.models.enriched.fact.EnrichedTrip;
import com.epam.tcodata.models.mix.fact.Trip;
import com.epam.tcodata.sql.dal.IDaoFactory;

public class MockExternalTripFactory extends MockAbstractExternalFactory<Trip, EnrichedTrip, AvroTrip> {

    private static final long serialVersionUID = 6440874289914389837L;

    public MockExternalTripFactory() {
        super(Trip.class);
    }

    @Override
    public IMixSource<Trip> createMixSource() {
        return new MockTripMixSource();
    }

    @Override
    public IOffsetService createOffsetService(IDaoFactory daoFactory) {
        return new TripOffsetService(this, daoFactory);
    }

    @Override
    public IConverter<Trip, EnrichedTrip, AvroTrip> createConverter() throws Exception {
        return new TripConverter();
    }
}
