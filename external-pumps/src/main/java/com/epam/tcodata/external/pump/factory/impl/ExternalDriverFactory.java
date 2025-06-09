package com.epam.tcodata.external.pump.factory.impl;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.converter.impl.DriverConverter;
import com.epam.tcodata.external.pump.factory.AbstractExternalFactory;
import com.epam.tcodata.external.pump.source.IMixSource;
import com.epam.tcodata.external.pump.source.dimension.impl.DriverMixSource;
import com.epam.tcodata.models.avro.dimension.AvroDriver;
import com.epam.tcodata.models.enriched.dimension.EnrichedDriver;
import com.epam.tcodata.models.mix.dimension.Driver;

public class ExternalDriverFactory extends AbstractExternalFactory<Driver, EnrichedDriver, AvroDriver> {

    private static final long serialVersionUID = 7201363522756932569L;

    public ExternalDriverFactory() {
        super(Driver.class);
    }

    @Override
    public IMixSource createMixSource() {
        return new DriverMixSource();
    }

    @Override
    public IConverter createConverter() {
        return new DriverConverter();
    }
}
