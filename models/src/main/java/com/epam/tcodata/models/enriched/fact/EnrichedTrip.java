package com.epam.tcodata.models.enriched.fact;

import com.epam.tcodata.models.enriched.EnrichedCommon;
import com.epam.tcodata.models.enriched.IEnrichable;
import com.epam.tcodata.models.exception.NonEnrichedEntityException;
import com.epam.tcodata.models.mix.fact.SubTrip;
import com.epam.tcodata.models.mix.fact.Trip;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Maps to SOAP EnrichedTrip.
 */
public class EnrichedTrip extends Trip implements IEnrichable {

    private static final long serialVersionUID = 1369394590179876343L;

    private EnrichedCommon enrichedCommon;

    private String driverDurableKey;
    private String vehicleDurableKey;

    public EnrichedTrip() {
        enrichedCommon = new EnrichedCommon();
    }

    /**
     * Main constructor.
     * @param trip asset to construct {@link EnrichedTrip}
     */
    public EnrichedTrip(Trip trip) {
        this.enrichedCommon = new EnrichedCommon();
        this.setTripId(trip.getTripId());
        this.setAssetId(trip.getAssetId());
        this.setDriverId(trip.getDriverId());
        this.setTripStart(trip.getTripStart());
        this.setTripEnd(trip.getTripEnd());
        this.setNotes(trip.getNotes());
        this.setPulseParameterName(trip.getPulseParameterName());
        this.setSubTripList(trip.getSubTripList());
        this.setEngineSeconds(trip.getEngineSeconds());
        this.setStartPositionId(trip.getStartPositionId());
        this.setStartPosition(trip.getStartPosition());
        this.setEndPositionId(trip.getEndPositionId());
        this.setEndPosition(trip.getEndPosition());
        this.setFirstDepart(trip.getFirstDepart());
        this.setLastHalt(trip.getLastHalt());
        this.setDrivingTime(trip.getDrivingTime());
        this.setStandingTime(trip.getStandingTime());
        this.setDuration(trip.getDuration());
        this.setDistanceKilometers(trip.getDistanceKilometers());
        this.setStartOdometerKilometers(trip.getStartOdometerKilometers());
        this.setEndOdometerKilometers(trip.getEndOdometerKilometers());
        this.setStartEngineSeconds(trip.getStartEngineSeconds());
        this.setEndEngineSeconds(trip.getEndEngineSeconds());
        this.setPulseValue(trip.getPulseValue());
        this.setFuelUsedLitres(trip.getFuelUsedLitres());
        this.setMaxSpeedKilometersPerHour(trip.getMaxSpeedKilometersPerHour());
        this.setMaxAccelerationKilometersPerHourPerSecond(trip.getMaxAccelerationKilometersPerHourPerSecond());
        this.setMaxDecelerationKilometersPerHourPerSecond(trip.getMaxDecelerationKilometersPerHourPerSecond());
        this.setMaxRpm(trip.getMaxRpm());
    }

    @Override
    public String getDurableId() {
        return enrichedCommon.getDurableId();
    }

    @Override
    public EnrichedTrip setDurableId(String durableId) {
        this.enrichedCommon.setDurableId(durableId);
        return this;
    }

    @Override
    public Timestamp getIngestedDateUtc() {
        return enrichedCommon.getIngestedDateUtc();
    }

    @Override
    public EnrichedTrip setIngestedDateUtc(Timestamp ingestedDateUtc) {
        this.enrichedCommon.setIngestedDateUtc(ingestedDateUtc);
        return this;
    }

    @Override
    public Long getSubscriptionId() {
        return enrichedCommon.getSubscriptionId();
    }

    @Override
    public EnrichedTrip setSubscriptionId(Long subscriptionId) {
        this.enrichedCommon.setSubscriptionId(subscriptionId);
        return this;
    }

    @Override
    public Integer getLineageCode() {
        return enrichedCommon.getLineageCode();
    }

    @Override
    public EnrichedTrip setLineageCode(Integer lineageCode) {
        this.enrichedCommon.setLineageCode(lineageCode);
        return this;
    }

    public String getDriverDurableKey() {
        return driverDurableKey;
    }

    public EnrichedTrip setDriverDurableKey(String driverDurableKey) {
        this.driverDurableKey = driverDurableKey;
        return this;
    }

    public String getVehicleDurableKey() {
        return vehicleDurableKey;
    }

    public EnrichedTrip setVehicleDurableKey(String vehicleDurableKey) {
        this.vehicleDurableKey = vehicleDurableKey;
        return this;
    }

    /**
     * Returns list of {@link EnrichedSubTrip} if sub trips are enriched.
     * @return list of {@link EnrichedSubTrip}
     */
    public List<EnrichedSubTrip> getEnrichedSubTripList() {
        List<EnrichedSubTrip> enrichedSubTripList = new ArrayList<>();
        for (SubTrip subTrip : getSubTripList()) {
            if (subTrip instanceof EnrichedSubTrip) {
                enrichedSubTripList.add((EnrichedSubTrip) subTrip);
            } else {
                throw new NonEnrichedEntityException("Sub Trip field was not enriched");
            }
        }
        return enrichedSubTripList;
    }

    /**
     * Sets list of {@link EnrichedSubTrip} to sub trip list.
     * @return {@link EnrichedTrip}
     */
    public EnrichedTrip setEnrichedSubTripList(List<EnrichedSubTrip> enrichedSubTripList) {
        if (enrichedSubTripList != null) {
            setSubTripList(new ArrayList<>());
            getSubTripList().addAll(enrichedSubTripList);
        }
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

    public EnrichedTrip setEnrichedStartPosition(EnrichedPosition enrichedStartPosition) {
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

    public EnrichedTrip setEnrichedEndPosition(EnrichedPosition enrichedEndPosition) {
        setEndPosition(enrichedEndPosition);
        return this;
    }

    @Override
    public String toString() {
        return "EnrichedTrip{"
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
                getTripId(),
                getAssetId(),
                getDriverId(),
                getTripStart(),
                getTripEnd(),
                getNotes(),
                getPulseParameterName(),
                getEngineSeconds(),
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
                getFirstDepart(),
                getLastHalt(),
                getDrivingTime(),
                getStandingTime(),
                getDuration(),
                getDistanceKilometers(),
                getStartOdometerKilometers(),
                getEndOdometerKilometers(),
                getStartEngineSeconds(),
                getEndEngineSeconds(),
                getPulseValue(),
                getFuelUsedLitres(),
                getMaxSpeedKilometersPerHour(),
                getMaxAccelerationKilometersPerHourPerSecond(),
                getMaxDecelerationKilometersPerHourPerSecond(),
                getMaxRpm()
        };
    }
}
