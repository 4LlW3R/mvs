package com.epam.tcodata.models.enriched.fact;

import com.epam.tcodata.models.enriched.EnrichedCommon;
import com.epam.tcodata.models.enriched.IEnrichable;
import com.epam.tcodata.models.exception.NonEnrichedEntityException;
import com.epam.tcodata.models.mix.fact.Event;

import java.sql.Timestamp;

/**
 * Maps to SOAP RecordedEvent.
 */
public class EnrichedEvent extends Event implements IEnrichable {

    private static final long serialVersionUID = 8523803476288126628L;

    private EnrichedCommon enrichedCommon;

    private String driverDurableKey;
    private String vehicleDurableKey;

    public EnrichedEvent() {
        enrichedCommon = new EnrichedCommon();
    }

    /**
     * Main constructor.
     * @param event asset to construct {@link EnrichedEvent}
     */
    public EnrichedEvent(Event event) {
        this.enrichedCommon = new EnrichedCommon();
        this.setAssetId(event.getAssetId());
        this.setDriverId(event.getDriverId());
        this.setEventId(event.getEventId());
        this.setEventTypeId(event.getEventTypeId());
        this.setEventCategory(event.getEventCategory());
        this.setStartDateTime(event.getStartDateTime());
        this.setStartOdometerKilometres(event.getStartOdometerKilometres());
        this.setStartPosition(event.getStartPosition());
        this.setEndDateTime(event.getEndDateTime());
        this.setEndOdometerKilometres(event.getEndOdometerKilometres());
        this.setEndPosition(event.getEndPosition());
        this.setValue(event.getValue());
        this.setValueType(event.getValueType());
        this.setValueUnits(event.getValueUnits());
        this.setTotalTimeSeconds(event.getTotalTimeSeconds());
        this.setTotalOccurrences(event.getTotalOccurrences());
        this.setMediaUrls(event.getMediaUrls());
        this.setLocationId(event.getLocationId());
        this.setSpeedLimit(event.getSpeedLimit());
    }

    @Override
    public String getDurableId() {
        return enrichedCommon.getDurableId();
    }

    @Override
    public EnrichedEvent setDurableId(String durableId) {
        this.enrichedCommon.setDurableId(durableId);
        return this;
    }

    @Override
    public Timestamp getIngestedDateUtc() {
        return enrichedCommon.getIngestedDateUtc();
    }

    @Override
    public EnrichedEvent setIngestedDateUtc(Timestamp ingestedDateUtc) {
        this.enrichedCommon.setIngestedDateUtc(ingestedDateUtc);
        return this;
    }

    @Override
    public Long getSubscriptionId() {
        return enrichedCommon.getSubscriptionId();
    }

    @Override
    public EnrichedEvent setSubscriptionId(Long subscriptionId) {
        this.enrichedCommon.setSubscriptionId(subscriptionId);
        return this;
    }

    @Override
    public Integer getLineageCode() {
        return enrichedCommon.getLineageCode();
    }

    @Override
    public EnrichedEvent setLineageCode(Integer lineageCode) {
        this.enrichedCommon.setLineageCode(lineageCode);
        return this;
    }

    public String getDriverDurableKey() {
        return driverDurableKey;
    }

    public EnrichedEvent setDriverDurableKey(String driverDurableKey) {
        this.driverDurableKey = driverDurableKey;
        return this;
    }

    public String getVehicleDurableKey() {
        return vehicleDurableKey;
    }

    public EnrichedEvent setVehicleDurableKey(String vehicleDurableKey) {
        this.vehicleDurableKey = vehicleDurableKey;
        return this;
    }

    /**
     * Returns {@link EnrichedPosition} if start position is enriched.
     * @return {@link EnrichedPosition}
     */
    public EnrichedPosition getEnrichedStartPosition() {
        if (getStartPosition() instanceof EnrichedPosition) {
            return (EnrichedPosition) getStartPosition();
        } else if (getStartPosition() == null) {
            return null;
        } else {
            throw new NonEnrichedEntityException("Position field was not enriched");
        }
    }

    public EnrichedEvent setEnrichedStartPosition(EnrichedPosition enrichedStartPosition) {
        setStartPosition(enrichedStartPosition);
        return this;
    }

    /**
     * Returns {@link EnrichedPosition} if end position is enriched.
     * @return {@link EnrichedPosition}
     */
    public EnrichedPosition getEnrichedEndPosition() {
        if (getEndPosition() instanceof EnrichedPosition) {
            return (EnrichedPosition) getEndPosition();
        } else if (getEndPosition() == null) {
            return null;
        } else {
            throw new NonEnrichedEntityException("Position field was not enriched");
        }
    }

    public EnrichedEvent setEnrichedEndPosition(EnrichedPosition enrichedEndPosition) {
        setEndPosition(enrichedEndPosition);
        return this;
    }

    @Override
    public String toString() {
        return "EnrichedEvent{"
                + super.toString()
                + enrichedCommon.toString()
                + ", driverDurableKey=" + driverDurableKey
                + ", vehicleDurableKey=" + vehicleDurableKey
                + '}';
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
                driverDurableKey,
                vehicleDurableKey,
                getTotalOccurrences(),
                getTotalTimeSeconds(),
                getEventTypeId(),
                getEventId(),
                getDriverId(),
                getAssetId(),
                getValue(),
                getEndDateTime(),
                getStartDateTime(),
                getEventCategory(),
                getStartOdometerKilometres(),
                getEnrichedStartPosition() == null ? null : getEnrichedStartPosition().getTimestamp(),
                getEnrichedStartPosition() == null ? null : getEnrichedStartPosition().getLongitude(),
                getEnrichedStartPosition() == null ? null : getEnrichedStartPosition().getLatitude(),
                getEnrichedStartPosition() == null ? null : getEnrichedStartPosition().getPositionId(),
                getEnrichedStartPosition() == null ? null : getEnrichedStartPosition().getSpeedKilometresPerHour(),
                getEnrichedStartPosition() == null ? null : getEnrichedStartPosition().getAssetId(),
                getEnrichedStartPosition() == null ? null : getEnrichedStartPosition().getDriverId(),
                getEnrichedStartPosition() == null ? null : getEnrichedStartPosition().getSpeedLimit(),
                getEnrichedStartPosition() == null ? null : getEnrichedStartPosition().getAltitudeMetres(),
                getEnrichedStartPosition() == null ? null : getEnrichedStartPosition().getHeading(),
                getEnrichedStartPosition() == null ? null : getEnrichedStartPosition().getNumberOfSatellites(),
                getEnrichedStartPosition() == null ? null : getEnrichedStartPosition().getHdop(),
                getEnrichedStartPosition() == null ? null : getEnrichedStartPosition().getVdop(),
                getEnrichedStartPosition() == null ? null : getEnrichedStartPosition().getPdop(),
                getEnrichedStartPosition() == null ? null : getEnrichedStartPosition().getAgeOfReadingSeconds(),
                getEnrichedStartPosition() == null ? null : getEnrichedStartPosition().getDistanceSinceReadingKilometres(),
                getEnrichedStartPosition() == null ? null : getEnrichedStartPosition().getIgnitionOn(),
                getEnrichedStartPosition() == null ? null : getEnrichedStartPosition().getOdometerKilometres(),
                getEnrichedStartPosition() == null ? null : getEnrichedStartPosition().getFormattedAddress(),
                getEnrichedStartPosition() == null ? null : getEnrichedStartPosition().getSource(),
                getEnrichedStartPosition() == null ? null : getEnrichedStartPosition().getAvl(),
                getEndOdometerKilometres(),
                getEnrichedEndPosition() == null ? null : getEnrichedEndPosition().getTimestamp(),
                getEnrichedEndPosition() == null ? null : getEnrichedEndPosition().getLongitude(),
                getEnrichedEndPosition() == null ? null : getEnrichedEndPosition().getLatitude(),
                getEnrichedEndPosition() == null ? null : getEnrichedEndPosition().getPositionId(),
                getEnrichedEndPosition() == null ? null : getEnrichedEndPosition().getSpeedKilometresPerHour(),
                getEnrichedEndPosition() == null ? null : getEnrichedEndPosition().getAssetId(),
                getEnrichedEndPosition() == null ? null : getEnrichedEndPosition().getDriverId(),
                getEnrichedEndPosition() == null ? null : getEnrichedEndPosition().getSpeedLimit(),
                getEnrichedEndPosition() == null ? null : getEnrichedEndPosition().getAltitudeMetres(),
                getEnrichedEndPosition() == null ? null : getEnrichedEndPosition().getHeading(),
                getEnrichedEndPosition() == null ? null : getEnrichedEndPosition().getNumberOfSatellites(),
                getEnrichedEndPosition() == null ? null : getEnrichedEndPosition().getHdop(),
                getEnrichedEndPosition() == null ? null : getEnrichedEndPosition().getVdop(),
                getEnrichedEndPosition() == null ? null : getEnrichedEndPosition().getPdop(),
                getEnrichedEndPosition() == null ? null : getEnrichedEndPosition().getAgeOfReadingSeconds(),
                getEnrichedEndPosition() == null ? null : getEnrichedEndPosition().getDistanceSinceReadingKilometres(),
                getEnrichedEndPosition() == null ? null : getEnrichedEndPosition().getIgnitionOn(),
                getEnrichedEndPosition() == null ? null : getEnrichedEndPosition().getOdometerKilometres(),
                getEnrichedEndPosition() == null ? null : getEnrichedEndPosition().getFormattedAddress(),
                getEnrichedEndPosition() == null ? null : getEnrichedEndPosition().getSource(),
                getEnrichedEndPosition() == null ? null : getEnrichedEndPosition().getAvl(),
                getValueType(),
                getValueUnits(),
                getMediaUrls() == null ? null : getMediaUrls().getRoad(),
                getMediaUrls() == null ? null : getMediaUrls().getCab(),
                getMediaUrls() == null ? null : getMediaUrls().getCamera3(),
                getMediaUrls() == null ? null : getMediaUrls().getCamera4(),
                getLocationId(),
                getSpeedLimit()
        };
    }
}
