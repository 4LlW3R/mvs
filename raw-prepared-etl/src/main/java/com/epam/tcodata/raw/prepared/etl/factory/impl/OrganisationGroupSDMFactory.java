package com.epam.tcodata.raw.prepared.etl.factory.impl;

import com.epam.tcodata.models.datalake.prepared.dimension.PreparedGroup;
import com.epam.tcodata.models.datalake.prepared.statics.GroupType;
import com.epam.tcodata.models.datalake.raw.dimension.RawOrganisationGroup;
import com.epam.tcodata.models.datalake.raw.dimension.RawOrganisationSubGroup;
import com.epam.tcodata.raw.prepared.etl.ReferenceSupplier;
import com.epam.tcodata.raw.prepared.etl.converter.ISingleDomainModelConverter;
import com.epam.tcodata.raw.prepared.etl.factory.AbstractSDMFactory;

public class OrganisationGroupSDMFactory extends AbstractSDMFactory<RawOrganisationSubGroup, PreparedGroup> {

    private static final long serialVersionUID = 9149355219498741596L;

    public OrganisationGroupSDMFactory() {
        super(RawOrganisationSubGroup.class, PreparedGroup.class);
    }

    @Override
    public ISingleDomainModelConverter<RawOrganisationSubGroup, PreparedGroup> createConverter(ReferenceSupplier referenceSupplier) {
        return raw -> {
            GroupType.Values groupType = GroupType.Values.valueByStringCode(raw.getType());
            RawOrganisationGroup parentGroup = referenceSupplier.getGroup(raw.getParentOrgId());
            String displayTimeZone = parentGroup == null ? null : parentGroup.getDisplayTimeZone();
            String parentDurableId = parentGroup == null ? null : parentGroup.getDurableId();

            PreparedGroup res = new PreparedGroup();
            res.setDurableId(raw.getParentSubGroupId() == null ? parentDurableId : raw.getDurableId());
            res.setExternalId(raw.getGroupId());
            res.setPersistedDateUtc(raw.getPersistedDateUtc());
            res.setName(raw.getName());
            res.setGroupTypeDurableKey(groupType == null ? null : groupType.getUuid().toString());
            res.setGroupTypeCode(raw.getType());
            res.setDisplayTimeZone(raw.getParentSubGroupId() == null ? displayTimeZone : null);
            res.setParentGroupDurableKey(parentDurableId);
            res.setParentGroupId(raw.getParentSubGroupId());
            res.setFmOrgGroupId(raw.getSubscriptionId());

            return res;
        };
    }
}
