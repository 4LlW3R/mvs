package com.epam.tcodata.models.enriched.fact;

import com.epam.tcodata.models.enriched.EnrichedCommon;
import com.epam.tcodata.models.enriched.IEnrichable;
import com.epam.tcodata.models.exception.NonEnrichedEntityException;
import com.epam.tcodata.models.mix.fact.SubTrip;

import java.sql.Timestamp;

/**
 * Maps to SOAP EnrichedSubTrip.
 */
public class EnrichedSubTrip extends SubTrip implements IEnrichable {

    private static final long serialVersionUID = -4600346683334346917L;

    private Long subTripId;
    private String parentTripKey;

    private EnrichedCommon enrichedCommon;

    public EnrichedSubTrip() {
        enrichedCommon = new EnrichedCommon();
    }

    /**
     * Main constructor.
     * @param subTrip asset to construct {@link EnrichedSubTrip}
     */
    public EnrichedSubTrip(SubTrip subTrip) {
        enrichedCommon = new EnrichedCommon();
        this.setSubTripStart(subTrip.getSubTripStart());
        this.setStartPositionId(subTrip.getStartPositionId());
        this.setStartPosition(subTrip.getStartPosition());
        this.setDepart(subTrip.getDepart());
        this.setHalt(subTrip.getHalt());
        this.setSubTripEnd(subTrip.getSubTripEnd());
        this.setEndPositionId(subTrip.getEndPositionId());
        this.setEndPosition(subTrip.getEndPosition());
        this.setDrivingTime(subTrip.getDrivingTime());
        this.setStandingTime(subTrip.getStandingTime());
        this.setDuration(subTrip.getDuration());
        this.setDistanceKilometres(subTrip.getDistanceKilometres());
        this.setStartOdometerKilometres(subTrip.getStartOdometerKilometres());
        this.setEndOdometerKilometres(subTrip.getEndOdometerKilometres());
        this.setStartEngineSeconds(subTrip.getStartEngineSeconds());
        this.setEndEngineSeconds(subTrip.getEndEngineSeconds());
        this.setEngineSeconds(subTrip.getEngineSeconds());
        this.setPulseValue(subTrip.getPulseValue());
        this.setFuelUsedLitres(subTrip.getFuelUsedLitres());
        this.setMaxSpeedKilometersPerHour(subTrip.getMaxSpeedKilometersPerHour());
        this.setMaxAccelerationKilometersPerHourPerSecond(subTrip.getMaxAccelerationKilometersPerHourPerSecond());
        this.setMaxDecelerationKilometersPerHourPerSecond(subTrip.getMaxDecelerationKilometersPerHourPerSecond());
        this.setMaxRpm(subTrip.getMaxRpm());
    }

    public String getParentTripKey() {
        return parentTripKey;
    }

    public EnrichedSubTrip setParentTripKey(String parentTripKey) {
        this.parentTripKey = parentTripKey;
        return this;
    }

    public Long getSubTripId() {
        return subTripId;
    }

    public EnrichedSubTrip setSubTripId(Long subTripId) {
        this.subTripId = subTripId;
        return this;
    }

    @Override
    public String getDurableId() {
        return enrichedCommon.getDurableId();
    }

    @Override
    public EnrichedSubTrip setDurableId(String durableId) {
        this.enrichedCommon.setDurableId(durableId);
        return this;
    }

    @Override
    public Timestamp getIngestedDateUtc() {
        return enrichedCommon.getIngestedDateUtc();
    }

    @Override
    public EnrichedSubTrip setIngestedDateUtc(Timestamp ingestedDateUtc) {
        this.enrichedCommon.setIngestedDateUtc(ingestedDateUtc);
        return this;
    }

    @Override
    public Long getSubscriptionId() {
        return enrichedCommon.getSubscriptionId();
    }

    @Override
    public EnrichedSubTrip setSubscriptionId(Long subscriptionId) {
        this.enrichedCommon.setSubscriptionId(subscriptionId);
        return this;
    }

    @Override
    public Integer getLineageCode() {
        return enrichedCommon.getLineageCode();
    }

    @Override
    public EnrichedSubTrip setLineageCode(Integer lineageCode) {
        this.enrichedCommon.setLineageCode(lineageCode);
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

    public EnrichedSubTrip setEnrichedStartPosition(EnrichedPosition enrichedStartPosition) {
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

    public EnrichedSubTrip setEnrichedEndPosition(EnrichedPosition enrichedEndPosition) {
        setEndPosition(enrichedEndPosition);
        return this;
    }

    @Override
    public String toString() {
        return "EnrichedSubTrip{"
                + super.toString()
                + ", parentTripKey='" + parentTripKey + '\''
                + ", subTripId=" + subTripId
                + enrichedCommon.toString()
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
                getSubTripId(),
                getParentTripKey(),
                getSubTripStart(),
                getStartPositionId(),
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
                getDepart(),
                getHalt(),
                getSubTripEnd(),
                getEndPositionId(),
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
                getDrivingTime(),
                getStandingTime(),
                getDuration(),
                getDistanceKilometres(),
                getStartOdometerKilometres(),
                getEndOdometerKilometres(),
                getStartEngineSeconds(),
                getEndEngineSeconds(),
                getEngineSeconds(),
                getPulseValue(),
                getFuelUsedLitres(),
                getMaxSpeedKilometersPerHour(),
                getMaxAccelerationKilometersPerHourPerSecond(),
                getMaxDecelerationKilometersPerHourPerSecond(),
                getMaxRpm(),
        };
    }
}
