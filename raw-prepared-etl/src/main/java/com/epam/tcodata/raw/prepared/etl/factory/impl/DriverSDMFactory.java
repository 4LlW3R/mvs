package com.epam.tcodata.raw.prepared.etl.factory.impl;

import com.epam.tcodata.models.datalake.prepared.dimension.PreparedDriver;
import com.epam.tcodata.models.datalake.raw.dimension.RawDriver;
import com.epam.tcodata.raw.prepared.etl.ReferenceSupplier;
import com.epam.tcodata.raw.prepared.etl.converter.ISingleDomainModelConverter;
import com.epam.tcodata.raw.prepared.etl.factory.AbstractSDMFactory;

public class DriverSDMFactory extends AbstractSDMFactory<RawDriver, PreparedDriver> {

    private static final long serialVersionUID = 9149355219498741596L;

    public DriverSDMFactory() {
        super(RawDriver.class, PreparedDriver.class);
    }

    @Override
    public ISingleDomainModelConverter<RawDriver, PreparedDriver> createConverter(ReferenceSupplier referenceSupplier) {
        return raw -> {
            String orgGroupDurableKey = referenceSupplier.getSubGroupDurableId(raw.getSiteId());
            String organizationDurableKey = referenceSupplier.getGroupDurableId(raw.getSubscriptionId());

            PreparedDriver res = new PreparedDriver();
            res.setDurableId(raw.getDurableId());
            res.setOrganizationDurableKey(organizationDurableKey);
            res.setExternalId(raw.getDriverId());
            res.setPersistedDateUtc(raw.getPersistedDateUtc());
            res.setOrgGroupDurableKey(orgGroupDurableKey);
            res.setName(raw.getName());
            res.setFmDriverId(raw.getFmDriverId());
            res.setEmployeeNumber(raw.getEmployeeNumber());
            res.setSystemDriver(raw.getSystemDriver());
            res.setMobileNumber(raw.getMobileNumber());
            res.setEmail(raw.getEmail());
            res.setExtendedDriverId(raw.getExtendedDriverId());
            res.setExtendedDriverIdType(raw.getExtendedDriverIdType());
            res.setCountry(raw.getCountry());

            return res;
        };
    }
}
