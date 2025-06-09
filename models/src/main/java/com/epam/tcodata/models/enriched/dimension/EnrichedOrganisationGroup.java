package com.epam.tcodata.models.enriched.dimension;

import com.epam.tcodata.models.enriched.EnrichedCommon;
import com.epam.tcodata.models.enriched.IEnrichable;
import com.epam.tcodata.models.mix.dimension.OrganisationGroup;

import java.sql.Timestamp;

public class EnrichedOrganisationGroup extends OrganisationGroup implements IEnrichable {

    private static final long serialVersionUID = -1269505248715227327L;

    private EnrichedCommon enrichedCommon;

    public EnrichedOrganisationGroup() {
        enrichedCommon = new EnrichedCommon();
    }

    /**
     * Main constructor.
     * @param organisationGroup asset to construct {@link OrganisationGroup}
     */
    public EnrichedOrganisationGroup(OrganisationGroup organisationGroup) {
        this.enrichedCommon = new EnrichedCommon();
        this.setGroupId(organisationGroup.getGroupId());
        this.setType(organisationGroup.getType());
        this.setDisplayTimeZone(organisationGroup.getDisplayTimeZone());
        this.setName(organisationGroup.getName());
    }

    @Override
    public String getDurableId() {
        return enrichedCommon.getDurableId();
    }

    @Override
    public EnrichedOrganisationGroup setDurableId(String durableId) {
        this.enrichedCommon.setDurableId(durableId);
        return this;
    }

    @Override
    public Timestamp getIngestedDateUtc() {
        return enrichedCommon.getIngestedDateUtc();
    }

    @Override
    public EnrichedOrganisationGroup setIngestedDateUtc(Timestamp ingestedDateUtc) {
        this.enrichedCommon.setIngestedDateUtc(ingestedDateUtc);
        return this;
    }

    @Override
    public Long getSubscriptionId() {
        return enrichedCommon.getSubscriptionId();
    }

    @Override
    public EnrichedOrganisationGroup setSubscriptionId(Long subscriptionId) {
        this.enrichedCommon.setSubscriptionId(subscriptionId);
        return this;
    }

    @Override
    public Integer getLineageCode() {
        return enrichedCommon.getLineageCode();
    }

    @Override
    public EnrichedOrganisationGroup setLineageCode(Integer lineageCode) {
        this.enrichedCommon.setLineageCode(lineageCode);
        return this;
    }

    @Override
    public String toString() {
        return "EnrichedOrganisationGroup{"
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
                getType(),
                getDisplayTimeZone(),
                getName()
        };
    }
}
