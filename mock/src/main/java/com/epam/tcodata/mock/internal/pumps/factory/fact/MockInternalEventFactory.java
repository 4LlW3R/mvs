package com.epam.tcodata.mock.internal.pumps.factory.fact;

import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.internal.pump.converter.fact.EventConverter;
import com.epam.tcodata.internal.pump.converter.fact.PositionConverter;
import com.epam.tcodata.internal.pump.handler.IDataHandler;
import com.epam.tcodata.internal.pump.handler.fact.EventDataHandler;
import com.epam.tcodata.mock.internal.pumps.factory.MockAbstractInternalFactory;
import com.epam.tcodata.models.avro.fact.AvroEvent;
import com.epam.tcodata.models.datalake.raw.fact.RawEvent;
import com.epam.tcodata.models.enriched.fact.EnrichedEvent;
import com.epam.tcodata.models.mix.fact.Event;
import org.apache.spark.sql.SparkSession;

public class MockInternalEventFactory extends MockAbstractInternalFactory<Event, EnrichedEvent, AvroEvent, RawEvent> {
    private static final long serialVersionUID = 8016478358706615760L;

    public MockInternalEventFactory() {
        super(Event.class);
    }

    @Override
    public IDataHandler createEventDataHandler(SparkSession sparkSession) {
        return new EventDataHandler(this, sparkSession);
    }

    @Override
    public IEntityConverter createConverter() {
        return new EventConverter(new PositionConverter());
    }
}
