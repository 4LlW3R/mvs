package com.epam.tcodata.models.enriched.fact;

import com.epam.tcodata.models.enriched.EnrichedCommon;
import com.epam.tcodata.models.enriched.IEnrichable;
import com.epam.tcodata.models.mix.fact.Tacho;

import java.sql.Timestamp;

public class EnrichedTacho extends Tacho implements IEnrichable {

    private static final long serialVersionUID = -2786615737385920684L;

    private EnrichedCommon enrichedCommon;

    public EnrichedTacho(EnrichedCommon enrichedCommon) {
        this.enrichedCommon = enrichedCommon;
    }

    /**
     * Main constructor.
     *
     * @param tacho asset to construct {@link EnrichedTacho}
     */
    public EnrichedTacho(Tacho tacho) {
        this.enrichedCommon = new EnrichedCommon();
        this.setAssetId(tacho.getAssetId());
        this.setParameterDefinitions(tacho.getParameterDefinitions());
        this.setIntervals(tacho.getIntervals());
        this.setStartDateTime(tacho.getStartDateTime());
        this.setEndDateTime(tacho.getEndDateTime());
    }

    @Override
    public String getDurableId() {
        return enrichedCommon.getDurableId();
    }

    @Override
    public EnrichedTacho setDurableId(String durableId) {
        this.enrichedCommon.setDurableId(durableId);
        return this;
    }

    @Override
    public Timestamp getIngestedDateUtc() {
        return enrichedCommon.getIngestedDateUtc();
    }

    @Override
    public EnrichedTacho setIngestedDateUtc(Timestamp ingestedDateUtc) {
        this.enrichedCommon.setIngestedDateUtc(ingestedDateUtc);
        return this;
    }

    @Override
    public Long getSubscriptionId() {
        return enrichedCommon.getSubscriptionId();
    }

    @Override
    public EnrichedTacho setSubscriptionId(Long subscriptionId) {
        this.enrichedCommon.setSubscriptionId(subscriptionId);
        return this;
    }

    @Override
    public Integer getLineageCode() {
        return enrichedCommon.getLineageCode();
    }

    @Override
    public EnrichedTacho setLineageCode(Integer lineageCode) {
        this.enrichedCommon.setLineageCode(lineageCode);
        return this;
    }

    @Override
    public String toString() {
        return "EnrichedTacho{"
                + super.toString()
                + enrichedCommon.toString()
                + "}";
    }

    /**
     * Method provides structured fields for writing to DataLake.
     *
     * @return Object[] fields.
     */
    @SuppressWarnings("CPD-START")
    public Object[] getOrderedValues() {
        return new Object[] {
                getDurableId(),
                getIngestedDateUtc(),
                getSubscriptionId(),
                getLineageCode(),
                getAssetId(),
                getParameterDefinitions().toString(),
                getIntervals().toString(),
                getStartDateTime(),
                getEndDateTime()
        };
    }
}
