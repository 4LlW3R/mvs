package com.epam.tcodata.external.pump.factory.impl;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.converter.impl.LibraryEventConverter;
import com.epam.tcodata.external.pump.factory.AbstractExternalFactory;
import com.epam.tcodata.external.pump.source.IMixSource;
import com.epam.tcodata.external.pump.source.dimension.impl.LibraryEventMixSource;
import com.epam.tcodata.models.avro.dimension.AvroLibraryEvent;
import com.epam.tcodata.models.enriched.dimension.EnrichedLibraryEvent;
import com.epam.tcodata.models.mix.dimension.LibraryEvent;

public class ExternalLibraryEventFactory extends AbstractExternalFactory<LibraryEvent, EnrichedLibraryEvent, AvroLibraryEvent> {

    private static final long serialVersionUID = 7079793486022279271L;

    public ExternalLibraryEventFactory() {
        super(LibraryEvent.class);
    }

    @Override
    public IMixSource<LibraryEvent> createMixSource() {
        return new LibraryEventMixSource();
    }

    @Override
    public IConverter<LibraryEvent, EnrichedLibraryEvent, AvroLibraryEvent> createConverter() {
        return new LibraryEventConverter();
    }
}
