package com.epam.tcodata.mock.internal.pumps.factory.dimension;

import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.internal.pump.converter.dimension.DriverConverter;
import com.epam.tcodata.internal.pump.handler.IDataHandler;
import com.epam.tcodata.internal.pump.handler.dimension.DriverDataHandler;
import com.epam.tcodata.mock.internal.pumps.factory.MockAbstractInternalFactory;
import com.epam.tcodata.models.avro.dimension.AvroDriver;
import com.epam.tcodata.models.datalake.raw.dimension.RawDriver;
import com.epam.tcodata.models.enriched.dimension.EnrichedDriver;
import com.epam.tcodata.models.mix.dimension.Driver;
import org.apache.spark.sql.SparkSession;

public class MockInternalDriverFactory extends MockAbstractInternalFactory<Driver, EnrichedDriver, AvroDriver, RawDriver> {
    private static final long serialVersionUID = 7806759376112316330L;

    public MockInternalDriverFactory() {
        super(Driver.class);
    }

    @Override
    public IDataHandler createEventDataHandler(SparkSession sparkSession) {
        return new DriverDataHandler(this, sparkSession);
    }

    @Override
    public IEntityConverter createConverter() {
        return new DriverConverter();
    }
}
