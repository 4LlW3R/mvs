package com.epam.tcodata.raw.prepared.etl.factory.impl;

import com.epam.tcodata.models.datalake.prepared.dimension.PreparedLocation;
import com.epam.tcodata.models.datalake.prepared.statics.LocationShapeType;
import com.epam.tcodata.models.datalake.prepared.statics.LocationType;
import com.epam.tcodata.models.datalake.raw.dimension.RawLocation;
import com.epam.tcodata.raw.prepared.etl.ReferenceSupplier;
import com.epam.tcodata.raw.prepared.etl.converter.ISingleDomainModelConverter;
import com.epam.tcodata.raw.prepared.etl.factory.AbstractSDMFactory;

public class LocationSDMFactory extends AbstractSDMFactory<RawLocation, PreparedLocation> {

    private static final long serialVersionUID = 7136331545553662525L;

    public LocationSDMFactory() {
        super(RawLocation.class, PreparedLocation.class);
    }

    @Override
    public ISingleDomainModelConverter<RawLocation, PreparedLocation> createConverter(ReferenceSupplier referenceSupplier) {
        return raw -> {
            String orgGroupDurable = referenceSupplier.getGroupDurableId(raw.getGroupId());
            String organizationDurable = referenceSupplier.getGroupDurableId(raw.getSubscriptionId());
            LocationType.Values locationType = LocationType.Values.valueByStringCode(raw.getLocationType());
            String locationDurableId = locationType == null ? null : locationType.getUuid().toString();
            LocationShapeType.Values locationShapeType = LocationShapeType.Values.valueByStringCode(raw.getShapeType());
            String locationShapeDurableId = locationShapeType == null ? null : locationShapeType.getUuid().toString();

            PreparedLocation res = new PreparedLocation();
            res.setDurableId(raw.getDurableId());
            res.setOrganizationDurableKey(organizationDurable);
            res.setExternalId(raw.getLocationId());
            res.setPersistedDateUtc(raw.getPersistedDateUtc());
            res.setOrgGroupDurableKey(orgGroupDurable);
            res.setName(raw.getName());
            res.setAddress(raw.getAddress());
            res.setLocationTypeDurableKey(locationDurableId);
            res.setLocationTypeCode(raw.getLocationType());
            res.setShapeTypeDurableId(locationShapeDurableId);
            res.setShapeTypeCode(raw.getShapeType());
            res.setRadius(raw.getRadius());
            res.setShapeWkt(raw.getShapeWkt());
            res.setDeleted(raw.getDeleted());
            res.setColourOnMap(raw.getColourOnMap());
            res.setOpacityOnMap(raw.getOpacityOnMap());
            res.setTemporary(raw.getTemporary());
            res.setExternalReference(raw.getExternalReference());

            return res;
        };
    }
}
