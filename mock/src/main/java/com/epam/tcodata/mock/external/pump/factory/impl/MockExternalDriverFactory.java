package com.epam.tcodata.mock.external.pump.factory.impl;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.converter.impl.DriverConverter;
import com.epam.tcodata.external.pump.source.IMixSource;
import com.epam.tcodata.mock.external.pump.factory.MockAbstractExternalFactory;
import com.epam.tcodata.mock.external.pump.source.dimension.impl.MockDriverMixSource;
import com.epam.tcodata.models.avro.dimension.AvroDriver;
import com.epam.tcodata.models.enriched.dimension.EnrichedDriver;
import com.epam.tcodata.models.mix.dimension.Driver;

public class MockExternalDriverFactory extends MockAbstractExternalFactory<Driver, EnrichedDriver, AvroDriver> {

    private static final long serialVersionUID = 68194244038819658L;

    public MockExternalDriverFactory() {
        super(Driver.class);
    }

    @Override
    public IMixSource<Driver> createMixSource() {
        return new MockDriverMixSource();
    }

    @Override
    public IConverter<Driver, EnrichedDriver, AvroDriver> createConverter() throws Exception {
        return new DriverConverter();
    }
}
