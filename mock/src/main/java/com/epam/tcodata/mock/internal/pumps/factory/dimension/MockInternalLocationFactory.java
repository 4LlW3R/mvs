package com.epam.tcodata.mock.internal.pumps.factory.dimension;

import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.internal.pump.converter.dimension.LocationConverter;
import com.epam.tcodata.internal.pump.handler.IDataHandler;
import com.epam.tcodata.internal.pump.handler.dimension.LocationDataHandler;
import com.epam.tcodata.mock.internal.pumps.factory.MockAbstractInternalFactory;
import com.epam.tcodata.models.avro.dimension.AvroLocation;
import com.epam.tcodata.models.datalake.raw.dimension.RawLocation;
import com.epam.tcodata.models.enriched.dimension.EnrichedLocation;
import com.epam.tcodata.models.mix.dimension.Location;
import org.apache.spark.sql.SparkSession;

public class MockInternalLocationFactory extends MockAbstractInternalFactory<Location, EnrichedLocation, AvroLocation, RawLocation> {
    private static final long serialVersionUID = 8507331691640314019L;

    public MockInternalLocationFactory() {
        super(Location.class);
    }


    @Override
    public IDataHandler createEventDataHandler(SparkSession sparkSession) {
        return new LocationDataHandler(this, sparkSession);
    }

    @Override
    public IEntityConverter createConverter() {
        return new LocationConverter();
    }
}
