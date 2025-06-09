package com.epam.tcodata.models.enriched.dimension;

import com.epam.tcodata.models.enriched.EnrichedCommon;
import com.epam.tcodata.models.enriched.IEnrichable;
import com.epam.tcodata.models.mix.dimension.OrganisationSubGroup;

import java.sql.Timestamp;

public class EnrichedOrganisationSubGroup extends OrganisationSubGroup implements IEnrichable {

    private static final long serialVersionUID = -1269505248715227327L;

    private EnrichedCommon enrichedCommon;

    public EnrichedOrganisationSubGroup() {
        enrichedCommon = new EnrichedCommon();
    }

    /**
     * Main constructor.
     *
     * @param organisationSubGroup asset to construct {@link OrganisationSubGroup}
     */
    public EnrichedOrganisationSubGroup(OrganisationSubGroup organisationSubGroup) {
        this.enrichedCommon = new EnrichedCommon();
        this.setGroupId(organisationSubGroup.getGroupId());
        this.setParentOrgId(organisationSubGroup.getParentOrgId());
        this.setParentSubGroupId(organisationSubGroup.getParentSubGroupId());
        this.setName(organisationSubGroup.getName());
        this.setType(organisationSubGroup.getType());
    }

    @Override
    public String getDurableId() {
        return enrichedCommon.getDurableId();
    }

    @Override
    public EnrichedOrganisationSubGroup setDurableId(String durableId) {
        this.enrichedCommon.setDurableId(durableId);
        return this;
    }

    @Override
    public Timestamp getIngestedDateUtc() {
        return enrichedCommon.getIngestedDateUtc();
    }

    @Override
    public EnrichedOrganisationSubGroup setIngestedDateUtc(Timestamp ingestedDateUtc) {
        this.enrichedCommon.setIngestedDateUtc(ingestedDateUtc);
        return this;
    }

    @Override
    public Long getSubscriptionId() {
        return enrichedCommon.getSubscriptionId();
    }

    @Override
    public EnrichedOrganisationSubGroup setSubscriptionId(Long subscriptionId) {
        this.enrichedCommon.setSubscriptionId(subscriptionId);
        return this;
    }

    @Override
    public Integer getLineageCode() {
        return enrichedCommon.getLineageCode();
    }

    @Override
    public EnrichedOrganisationSubGroup setLineageCode(Integer lineageCode) {
        this.enrichedCommon.setLineageCode(lineageCode);
        return this;
    }

    @Override
    public String toString() {
        return "EnrichedOrganisationSubGroup{"
                + "enrichedCommon=" + enrichedCommon
                + "} " + super.toString();
    }

    /**
     * Method provides structed fields for writing to datalake.
     *
     * @return Object[] fields.
     */
    public Object[] getOrderedValues() {
        return new Object[] {
                getDurableId(),
                getIngestedDateUtc(),
                getSubscriptionId(),
                getLineageCode(),
                getGroupId(),
                getParentOrgId(),
                getParentSubGroupId(),
                getName(),
                getType()
        };
    }
}
