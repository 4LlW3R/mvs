package com.epam.tcodata.mock.external.pump.factory.impl.mix;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.converter.impl.DriverConverter;
import com.epam.tcodata.external.pump.source.IMixSource;
import com.epam.tcodata.mock.external.pump.factory.MixMockAbstractExternalFactory;
import com.epam.tcodata.mock.external.pump.source.dimension.impl.mix.MixMockDriverMixSource;
import com.epam.tcodata.models.avro.dimension.AvroDriver;
import com.epam.tcodata.models.enriched.dimension.EnrichedDriver;
import com.epam.tcodata.models.mix.dimension.Driver;

public class MixMockExternalDriverFactory extends MixMockAbstractExternalFactory<Driver, EnrichedDriver, AvroDriver> {

    private static final long serialVersionUID = 7201363522756932569L;

    public MixMockExternalDriverFactory() {
        super(Driver.class);
    }

    @Override
    public IMixSource createMixSource() {
        return new MixMockDriverMixSource();
    }

    @Override
    public IConverter createConverter() {
        return new DriverConverter();
    }
}
