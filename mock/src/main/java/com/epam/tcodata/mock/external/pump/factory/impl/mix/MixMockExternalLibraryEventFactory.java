package com.epam.tcodata.mock.external.pump.factory.impl.mix;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.converter.impl.LibraryEventConverter;
import com.epam.tcodata.external.pump.source.IMixSource;
import com.epam.tcodata.mock.external.pump.factory.MixMockAbstractExternalFactory;
import com.epam.tcodata.mock.external.pump.source.dimension.impl.mix.MixMockLibraryEventMixSource;
import com.epam.tcodata.models.avro.dimension.AvroLibraryEvent;
import com.epam.tcodata.models.enriched.dimension.EnrichedLibraryEvent;
import com.epam.tcodata.models.mix.dimension.LibraryEvent;

public class MixMockExternalLibraryEventFactory extends MixMockAbstractExternalFactory<LibraryEvent, EnrichedLibraryEvent, AvroLibraryEvent> {

    private static final long serialVersionUID = 7079793486022279271L;

    public MixMockExternalLibraryEventFactory() {
        super(LibraryEvent.class);
    }

    @Override
    public IMixSource<LibraryEvent> createMixSource() {
        return new MixMockLibraryEventMixSource();
    }

    @Override
    public IConverter<LibraryEvent, EnrichedLibraryEvent, AvroLibraryEvent> createConverter() {
        return new LibraryEventConverter();
    }
}
