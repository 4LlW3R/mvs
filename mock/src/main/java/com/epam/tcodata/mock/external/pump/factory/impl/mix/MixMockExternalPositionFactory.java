package com.epam.tcodata.mock.external.pump.factory.impl.mix;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.converter.impl.PositionConverter;
import com.epam.tcodata.external.pump.service.IOffsetService;
import com.epam.tcodata.external.pump.service.impl.PositionOffsetService;
import com.epam.tcodata.external.pump.source.IMixSource;
import com.epam.tcodata.mock.external.pump.factory.MixMockAbstractExternalFactory;
import com.epam.tcodata.mock.external.pump.source.fact.impl.mix.MixMockPositionMixSource;
import com.epam.tcodata.models.avro.fact.AvroPosition;
import com.epam.tcodata.models.enriched.fact.EnrichedPosition;
import com.epam.tcodata.models.mix.fact.Position;
import com.epam.tcodata.sql.dal.IDaoFactory;

public class MixMockExternalPositionFactory extends MixMockAbstractExternalFactory<Position, EnrichedPosition, AvroPosition> {

    private static final long serialVersionUID = 2984349961927097192L;

    public MixMockExternalPositionFactory() {
        super(Position.class);
    }

    @Override
    public IMixSource createMixSource() {
        return new MixMockPositionMixSource();
    }

    @Override
    public IOffsetService createOffsetService(IDaoFactory daoFactory) {
        return new PositionOffsetService(this, daoFactory);
    }

    @Override
    public IConverter createConverter() {
        return new PositionConverter();
    }
}
