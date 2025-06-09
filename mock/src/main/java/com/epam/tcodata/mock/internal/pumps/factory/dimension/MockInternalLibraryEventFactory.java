package com.epam.tcodata.mock.internal.pumps.factory.dimension;

import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.internal.pump.converter.dimension.LibraryEventConverter;
import com.epam.tcodata.internal.pump.handler.IDataHandler;
import com.epam.tcodata.internal.pump.handler.dimension.LibraryEventDataHandler;
import com.epam.tcodata.mock.internal.pumps.factory.MockAbstractInternalFactory;
import com.epam.tcodata.models.avro.dimension.AvroLibraryEvent;
import com.epam.tcodata.models.datalake.raw.dimension.RawLibraryEvent;
import com.epam.tcodata.models.enriched.dimension.EnrichedLibraryEvent;
import com.epam.tcodata.models.mix.dimension.LibraryEvent;
import org.apache.spark.sql.SparkSession;

public class MockInternalLibraryEventFactory extends MockAbstractInternalFactory<LibraryEvent, EnrichedLibraryEvent, AvroLibraryEvent, RawLibraryEvent> {
    private static final long serialVersionUID = -1208072904879532495L;

    public MockInternalLibraryEventFactory() {
        super(LibraryEvent.class);
    }

    @Override
    public IDataHandler createEventDataHandler(SparkSession sparkSession) {
        return new LibraryEventDataHandler(this, sparkSession);
    }

    @Override
    public IEntityConverter createConverter() {
        return new LibraryEventConverter();
    }
}
