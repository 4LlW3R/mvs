package com.epam.tcodata.mock.internal.pumps.factory.fact;

import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.internal.pump.converter.fact.PositionConverter;
import com.epam.tcodata.internal.pump.handler.IDataHandler;
import com.epam.tcodata.internal.pump.handler.fact.PositionDataHandler;
import com.epam.tcodata.mock.internal.pumps.factory.MockAbstractInternalFactory;
import com.epam.tcodata.models.avro.fact.AvroPosition;
import com.epam.tcodata.models.datalake.raw.fact.RawPosition;
import com.epam.tcodata.models.enriched.fact.EnrichedPosition;
import com.epam.tcodata.models.mix.fact.Position;
import org.apache.spark.sql.SparkSession;

public class MockInternalPositionFactory extends MockAbstractInternalFactory<Position, EnrichedPosition, AvroPosition, RawPosition> {
    private static final long serialVersionUID = 3994566209419292364L;

    public MockInternalPositionFactory() {
        super(Position.class);
    }

    @Override
    public IDataHandler createEventDataHandler(SparkSession sparkSession) {
        return new PositionDataHandler(this, sparkSession);
    }

    @Override
    public IEntityConverter createConverter() {
        return new PositionConverter();
    }
}
