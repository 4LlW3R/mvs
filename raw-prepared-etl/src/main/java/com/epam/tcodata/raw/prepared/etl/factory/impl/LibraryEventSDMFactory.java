package com.epam.tcodata.raw.prepared.etl.factory.impl;

import com.epam.tcodata.models.datalake.prepared.dimension.PreparedEventDescription;
import com.epam.tcodata.models.datalake.raw.dimension.RawLibraryEvent;
import com.epam.tcodata.raw.prepared.etl.ReferenceSupplier;
import com.epam.tcodata.raw.prepared.etl.converter.ISingleDomainModelConverter;
import com.epam.tcodata.raw.prepared.etl.factory.AbstractSDMFactory;

public class LibraryEventSDMFactory extends AbstractSDMFactory<RawLibraryEvent, PreparedEventDescription> {

    private static final long serialVersionUID = 5234332233441935963L;

    public LibraryEventSDMFactory() {
        super(RawLibraryEvent.class, PreparedEventDescription.class);
    }

    @Override
    public ISingleDomainModelConverter<RawLibraryEvent, PreparedEventDescription> createConverter(ReferenceSupplier referenceSupplier) {
        return raw -> {
            String organizationDurable = referenceSupplier.getGroupDurableId(raw.getSubscriptionId());

            PreparedEventDescription res = new PreparedEventDescription();
            res.setDurableId(raw.getDurableId());
            res.setOrganizationDurableKey(organizationDurable);
            res.setExternalId(raw.getEventTypeId());
            res.setPersistedDateUtc(raw.getPersistedDateUtc());
            res.setDescription(raw.getDescription());
            res.setEventType(raw.getEventType());
            res.setDisplayUnits(raw.getDisplayUnits());
            res.setFormatType(raw.getFormatType());
            res.setValueName(raw.getValueName());

            return res;
        };
    }
}
