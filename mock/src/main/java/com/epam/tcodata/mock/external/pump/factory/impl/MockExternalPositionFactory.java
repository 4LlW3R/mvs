package com.epam.tcodata.mock.external.pump.factory.impl;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.converter.impl.PositionConverter;
import com.epam.tcodata.external.pump.service.IOffsetService;
import com.epam.tcodata.external.pump.service.impl.PositionOffsetService;
import com.epam.tcodata.external.pump.source.IMixSource;
import com.epam.tcodata.mock.external.pump.factory.MockAbstractExternalFactory;
import com.epam.tcodata.mock.external.pump.source.fact.impl.MockPositionMixSource;
import com.epam.tcodata.models.avro.fact.AvroPosition;
import com.epam.tcodata.models.enriched.fact.EnrichedPosition;
import com.epam.tcodata.models.mix.fact.Position;
import com.epam.tcodata.sql.dal.IDaoFactory;

public class MockExternalPositionFactory extends MockAbstractExternalFactory<Position, EnrichedPosition, AvroPosition> {

    private static final long serialVersionUID = -8022183485137599229L;

    public MockExternalPositionFactory() {
        super(Position.class);
    }

    @Override
    public IMixSource<Position> createMixSource() {
        return new MockPositionMixSource();
    }

    @Override
    public IOffsetService createOffsetService(IDaoFactory daoFactory) {
        return new PositionOffsetService(this, daoFactory);
    }

    @Override
    public IConverter<Position, EnrichedPosition, AvroPosition> createConverter() {
        return new PositionConverter();
    }
}
