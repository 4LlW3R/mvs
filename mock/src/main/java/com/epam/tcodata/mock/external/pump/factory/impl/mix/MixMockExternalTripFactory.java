package com.epam.tcodata.mock.external.pump.factory.impl.mix;

import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.eventhub.dal.impl.EventHub;
import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.converter.impl.TripConverter;
import com.epam.tcodata.external.pump.service.IOffsetService;
import com.epam.tcodata.external.pump.service.impl.TripOffsetService;
import com.epam.tcodata.external.pump.source.IMixSource;
import com.epam.tcodata.mock.external.pump.factory.MixMockAbstractExternalFactory;
import com.epam.tcodata.mock.external.pump.source.fact.impl.mix.MixMockTripMixSource;
import com.epam.tcodata.models.avro.fact.AvroTrip;
import com.epam.tcodata.models.enriched.fact.EnrichedTrip;
import com.epam.tcodata.models.mix.fact.Trip;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;

public class MixMockExternalTripFactory extends MixMockAbstractExternalFactory<Trip, EnrichedTrip, AvroTrip> {

    private static final long serialVersionUID = -5636777454819938654L;

    public MixMockExternalTripFactory() {
        super(Trip.class);
    }

    @Override
    public IMixSource createMixSource() {
        return new MixMockTripMixSource();
    }

    @Override
    public IOffsetService createOffsetService(IDaoFactory daoFactory) {
        return new TripOffsetService(this, daoFactory);
    }

    @Override
    public IConverter createConverter() throws Exception {
        return new TripConverter();
    }

    @Override
    public IEventHub createEventHub(ISecretStorage secretStorage) {
        return new EventHub(getEventHubInfo(), secretStorage, 100);
    }
}
