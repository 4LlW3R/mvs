package com.epam.tcodata.external.pump.factory.impl;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.converter.impl.LocationConverter;
import com.epam.tcodata.external.pump.factory.AbstractExternalFactory;
import com.epam.tcodata.external.pump.source.IMixSource;
import com.epam.tcodata.external.pump.source.dimension.impl.LocationMixSource;
import com.epam.tcodata.models.avro.dimension.AvroLocation;
import com.epam.tcodata.models.enriched.dimension.EnrichedLocation;
import com.epam.tcodata.models.mix.dimension.Location;

public class ExternalLocationFactory extends AbstractExternalFactory<Location, EnrichedLocation, AvroLocation> {

    private static final long serialVersionUID = -6772698816122034160L;

    public ExternalLocationFactory() {
        super(Location.class);
    }

    @Override
    public IMixSource<Location> createMixSource() {
        return new LocationMixSource();
    }

    @Override
    public IConverter<Location, EnrichedLocation, AvroLocation> createConverter() throws Exception {
        return new LocationConverter();
    }
}
