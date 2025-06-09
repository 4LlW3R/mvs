package com.epam.tcodata.mock.external.pump.factory.impl.mix;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.converter.impl.LocationConverter;
import com.epam.tcodata.external.pump.source.IMixSource;
import com.epam.tcodata.mock.external.pump.factory.MixMockAbstractExternalFactory;
import com.epam.tcodata.mock.external.pump.source.dimension.impl.mix.MixMockLocationMixSource;
import com.epam.tcodata.models.avro.dimension.AvroLocation;
import com.epam.tcodata.models.enriched.dimension.EnrichedLocation;
import com.epam.tcodata.models.mix.dimension.Location;

public class MixMockExternalLocationFactory extends MixMockAbstractExternalFactory<Location, EnrichedLocation, AvroLocation> {

    private static final long serialVersionUID = -6772698816122034160L;

    public MixMockExternalLocationFactory() {
        super(Location.class);
    }

    @Override
    public IMixSource<Location> createMixSource() {
        return new MixMockLocationMixSource();
    }

    @Override
    public IConverter<Location, EnrichedLocation, AvroLocation> createConverter() throws Exception {
        return new LocationConverter();
    }
}
