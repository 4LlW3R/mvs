package com.epam.tcodata.mock.internal.pumps.factory.fact;

import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.internal.pump.converter.fact.PositionConverter;
import com.epam.tcodata.internal.pump.converter.fact.SubTripConverter;
import com.epam.tcodata.internal.pump.converter.fact.TripConverter;
import com.epam.tcodata.internal.pump.handler.IDataHandler;
import com.epam.tcodata.internal.pump.handler.fact.SubTripDataHandler;
import com.epam.tcodata.internal.pump.handler.fact.TripDataHandler;
import com.epam.tcodata.mock.internal.pumps.factory.MockAbstractInternalFactory;
import com.epam.tcodata.models.avro.fact.AvroTrip;
import com.epam.tcodata.models.datalake.raw.fact.RawTrip;
import com.epam.tcodata.models.enriched.fact.EnrichedTrip;
import com.epam.tcodata.models.mix.fact.Trip;
import org.apache.spark.sql.SparkSession;

public class MockInternalTripFactory extends MockAbstractInternalFactory<Trip, EnrichedTrip, AvroTrip, RawTrip> {
    private static final long serialVersionUID = -948660763595969407L;

    private MockInternalSubTripFactory subTripFactory;

    public MockInternalTripFactory() {
        super(Trip.class);
        this.subTripFactory = new MockInternalSubTripFactory();
    }

    @Override
    public IDataHandler createEventDataHandler(SparkSession sparkSession) {
        SubTripDataHandler subTripDataHandler = new SubTripDataHandler(this.subTripFactory, sparkSession);
        return new TripDataHandler(this, sparkSession, subTripDataHandler);
    }

    @Override
    public IEntityConverter createConverter() {
        return new TripConverter(new PositionConverter(), new SubTripConverter(new PositionConverter()));
    }
}
