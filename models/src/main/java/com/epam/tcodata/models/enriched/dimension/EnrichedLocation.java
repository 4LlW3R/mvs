package com.epam.tcodata.models.enriched.dimension;

import com.epam.tcodata.models.enriched.EnrichedCommon;
import com.epam.tcodata.models.enriched.IEnrichable;
import com.epam.tcodata.models.mix.dimension.Location;

import java.sql.Timestamp;

/**
 * Maps to SOAP EnrichedLocation.
 */
public class EnrichedLocation extends Location implements IEnrichable {

    private static final long serialVersionUID = -9134741775506025603L;

    private EnrichedCommon enrichedCommon;

    public EnrichedLocation() {
        enrichedCommon = new EnrichedCommon();
    }

    /**
     * Main constructor.
     * @param location asset to construct {@link EnrichedLocation}
     */
    public EnrichedLocation(Location location) {
        this.enrichedCommon = new EnrichedCommon();
        this.setLocationId(location.getLocationId());
        this.setOrganisationId(location.getOrganisationId());
        this.setGroupId(location.getGroupId());
        this.setName(location.getName());
        this.setAddress(location.getAddress());
        this.setLocationType(location.getLocationType());
        this.setShapeType(location.getShapeType());
        this.setRadius(location.getRadius());
        this.setShapeWkt(location.getShapeWkt());
        this.setDeleted(location.getDeleted());
        this.setColourOnMap(location.getColourOnMap());
        this.setOpacityOnMap(location.getOpacityOnMap());
        this.setTemporary(location.getTemporary());
        this.setExternalReference(location.getExternalReference());
    }

    @Override
    public String getDurableId() {
        return enrichedCommon.getDurableId();
    }

    @Override
    public EnrichedLocation setDurableId(String durableId) {
        this.enrichedCommon.setDurableId(durableId);
        return this;
    }

    @Override
    public Timestamp getIngestedDateUtc() {
        return enrichedCommon.getIngestedDateUtc();
    }

    @Override
    public EnrichedLocation setIngestedDateUtc(Timestamp ingestedDateUtc) {
        this.enrichedCommon.setIngestedDateUtc(ingestedDateUtc);
        return this;
    }

    @Override
    public Long getSubscriptionId() {
        return enrichedCommon.getSubscriptionId();
    }

    @Override
    public EnrichedLocation setSubscriptionId(Long subscriptionId) {
        this.enrichedCommon.setSubscriptionId(subscriptionId);
        return this;
    }

    @Override
    public Integer getLineageCode() {
        return enrichedCommon.getLineageCode();
    }

    @Override
    public EnrichedLocation setLineageCode(Integer lineageCode) {
        this.enrichedCommon.setLineageCode(lineageCode);
        return this;
    }

    @Override
    public String toString() {
        return "EnrichedLocation{"
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
                getLocationId(),
                getOrganisationId(),
                getGroupId(),
                getName(),
                getAddress(),
                getLocationType(),
                getShapeType(),
                getRadius(),
                getShapeWkt(),
                getDeleted(),
                getColourOnMap(),
                getOpacityOnMap(),
                getTemporary(),
                getExternalReference(),
        };
    }
}
