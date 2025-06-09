package com.epam.tcodata.external.pump.factory.impl;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.converter.impl.PositionConverter;
import com.epam.tcodata.external.pump.factory.AbstractExternalFactory;
import com.epam.tcodata.external.pump.service.IOffsetService;
import com.epam.tcodata.external.pump.service.impl.PositionOffsetService;
import com.epam.tcodata.external.pump.source.IMixSource;
import com.epam.tcodata.external.pump.source.fact.impl.PositionMixSource;
import com.epam.tcodata.models.avro.fact.AvroPosition;
import com.epam.tcodata.models.enriched.fact.EnrichedPosition;
import com.epam.tcodata.models.mix.fact.Position;
import com.epam.tcodata.sql.dal.IDaoFactory;

public class ExternalPositionFactory extends AbstractExternalFactory<Position, EnrichedPosition, AvroPosition> {

    private static final long serialVersionUID = 2984349961927097192L;

    public ExternalPositionFactory() {
        super(Position.class);
    }

    @Override
    public IMixSource createMixSource() {
        return new PositionMixSource();
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
