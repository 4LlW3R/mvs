package com.epam.tcodata.external.pump.factory.impl;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.converter.impl.SubTripConverter;
import com.epam.tcodata.external.pump.factory.AbstractExternalFactory;
import com.epam.tcodata.external.pump.service.IOffsetService;
import com.epam.tcodata.external.pump.source.IMixSource;
import com.epam.tcodata.models.avro.fact.AvroSubTrip;
import com.epam.tcodata.models.enriched.fact.EnrichedSubTrip;
import com.epam.tcodata.models.mix.fact.SubTrip;
import com.epam.tcodata.sql.dal.IDaoFactory;

public class ExternalSubTripFactory extends AbstractExternalFactory<SubTrip, EnrichedSubTrip, AvroSubTrip> {

    private static final long serialVersionUID = -4028650950140616303L;

    public ExternalSubTripFactory() {
        super(SubTrip.class);
    }

    @Override
    public IMixSource createMixSource() {
        throw new UnsupportedOperationException("Sub trips are received with trips");
    }

    @Override
    public IOffsetService createOffsetService(IDaoFactory daoFactory) {
        throw new UnsupportedOperationException("Sub trips are received with trips");
    }

    @Override
    public IConverter createConverter() throws Exception {
        return new SubTripConverter();
    }
}
