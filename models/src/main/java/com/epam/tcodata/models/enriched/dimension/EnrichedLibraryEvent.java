package com.epam.tcodata.models.enriched.dimension;

import com.epam.tcodata.models.enriched.EnrichedCommon;
import com.epam.tcodata.models.enriched.IEnrichable;
import com.epam.tcodata.models.mix.dimension.LibraryEvent;

import java.sql.Timestamp;

/**
 * Maps to SOAP EventDescription.
 */
public class EnrichedLibraryEvent extends LibraryEvent implements IEnrichable {

    private static final long serialVersionUID = 156056780175326446L;

    private EnrichedCommon enrichedCommon;

    public EnrichedLibraryEvent() {
        enrichedCommon = new EnrichedCommon();
    }

    /**
     * Main constructor.
     * @param libraryEvent asset to construct {@link EnrichedLibraryEvent}
     */
    public EnrichedLibraryEvent(LibraryEvent libraryEvent) {
        this.enrichedCommon = new EnrichedCommon();
        this.setDescription(libraryEvent.getDescription());
        this.setEventTypeId(libraryEvent.getEventTypeId());
        this.setEventType(libraryEvent.getEventType());
        this.setDisplayUnits(libraryEvent.getDisplayUnits());
        this.setFormatType(libraryEvent.getFormatType());
        this.setValueName(libraryEvent.getValueName());
    }

    @Override
    public String getDurableId() {
        return enrichedCommon.getDurableId();
    }

    @Override
    public EnrichedLibraryEvent setDurableId(String durableId) {
        this.enrichedCommon.setDurableId(durableId);
        return this;
    }

    @Override
    public Timestamp getIngestedDateUtc() {
        return enrichedCommon.getIngestedDateUtc();
    }

    @Override
    public EnrichedLibraryEvent setIngestedDateUtc(Timestamp ingestedDateUtc) {
        this.enrichedCommon.setIngestedDateUtc(ingestedDateUtc);
        return this;
    }

    @Override
    public Long getSubscriptionId() {
        return enrichedCommon.getSubscriptionId();
    }

    @Override
    public EnrichedLibraryEvent setSubscriptionId(Long subscriptionId) {
        this.enrichedCommon.setSubscriptionId(subscriptionId);
        return this;
    }

    @Override
    public Integer getLineageCode() {
        return enrichedCommon.getLineageCode();
    }

    @Override
    public EnrichedLibraryEvent setLineageCode(Integer lineageCode) {
        this.enrichedCommon.setLineageCode(lineageCode);
        return this;
    }

    @Override
    public String toString() {
        return "EnrichedLibraryEvent{"
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
                getDescription(),
                getEventTypeId(),
                getEventType(),
                getDisplayUnits(),
                getFormatType(),
                getValueName(),
        };
    }
}
