package com.epam.tcodata.mock.external.pump.factory.impl;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.converter.impl.LibraryEventConverter;
import com.epam.tcodata.external.pump.source.IMixSource;
import com.epam.tcodata.mock.external.pump.factory.MockAbstractExternalFactory;
import com.epam.tcodata.mock.external.pump.source.dimension.impl.MockLibraryEventMixSource;
import com.epam.tcodata.models.avro.dimension.AvroLibraryEvent;
import com.epam.tcodata.models.enriched.dimension.EnrichedLibraryEvent;
import com.epam.tcodata.models.mix.dimension.LibraryEvent;

public class MockExternalLibraryEventFactory extends MockAbstractExternalFactory<LibraryEvent, EnrichedLibraryEvent, AvroLibraryEvent> {

    private static final long serialVersionUID = -6003755941801122479L;

    public MockExternalLibraryEventFactory() {
        super(LibraryEvent.class);
    }

    @Override
    public IMixSource<LibraryEvent> createMixSource() {
        return new MockLibraryEventMixSource();
    }

    @Override
    public IConverter<LibraryEvent, EnrichedLibraryEvent, AvroLibraryEvent> createConverter() throws Exception {
        return new LibraryEventConverter();
    }
}
