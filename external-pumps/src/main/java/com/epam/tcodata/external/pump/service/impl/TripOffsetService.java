package com.epam.tcodata.external.pump.service.impl;

import com.epam.tcodata.external.pump.factory.IExternalFactory;
import com.epam.tcodata.external.pump.service.AbstractOffsetService;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.enriched.fact.EnrichedTrip;
import com.epam.tcodata.sql.dal.IDaoFactory;

public class TripOffsetService extends AbstractOffsetService<EnrichedTrip> {

    public TripOffsetService(IExternalFactory externalFactory, IDaoFactory daoFactory) {
        super(externalFactory, daoFactory, EntityType.TRIP);
    }
}
