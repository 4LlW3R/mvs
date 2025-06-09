package com.epam.tcodata.mock.internal.pumps.factory.fact;

import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.internal.pump.converter.fact.PositionConverter;
import com.epam.tcodata.internal.pump.converter.fact.SubTripConverter;
import com.epam.tcodata.internal.pump.handler.IDataHandler;
import com.epam.tcodata.internal.pump.handler.fact.SubTripDataHandler;
import com.epam.tcodata.mock.internal.pumps.factory.MockAbstractInternalFactory;
import com.epam.tcodata.models.avro.fact.AvroSubTrip;
import com.epam.tcodata.models.datalake.raw.fact.RawSubTrip;
import com.epam.tcodata.models.enriched.fact.EnrichedSubTrip;
import com.epam.tcodata.models.mix.fact.SubTrip;
import org.apache.spark.sql.SparkSession;

public class MockInternalSubTripFactory extends MockAbstractInternalFactory<SubTrip, EnrichedSubTrip, AvroSubTrip, RawSubTrip> {
    private static final long serialVersionUID = 593039973702103390L;

    public MockInternalSubTripFactory() {
        super(SubTrip.class);
    }

    @Override
    public IDataHandler createEventDataHandler(SparkSession sparkSession) {
        return new SubTripDataHandler(this, sparkSession);
    }

    @Override
    public IEntityConverter createConverter() {
        return new SubTripConverter(new PositionConverter());
    }
}
