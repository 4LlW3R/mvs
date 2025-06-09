package com.epam.tcodata.mock.external.pump.factory.impl;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.converter.impl.LocationConverter;
import com.epam.tcodata.external.pump.source.IMixSource;
import com.epam.tcodata.mock.external.pump.factory.MockAbstractExternalFactory;
import com.epam.tcodata.mock.external.pump.source.dimension.impl.MockLocationMixSource;
import com.epam.tcodata.models.avro.dimension.AvroLocation;
import com.epam.tcodata.models.enriched.dimension.EnrichedLocation;
import com.epam.tcodata.models.mix.dimension.Location;

public class MockExternalLocationFactory extends MockAbstractExternalFactory<Location, EnrichedLocation, AvroLocation> {

    private static final long serialVersionUID = 9180229393013614807L;

    public MockExternalLocationFactory() {
        super(Location.class);
    }

    @Override
    public IMixSource<Location> createMixSource() {
        return new MockLocationMixSource();
    }

    @Override
    public IConverter<Location, EnrichedLocation, AvroLocation> createConverter() {
        return new LocationConverter();
    }
}
