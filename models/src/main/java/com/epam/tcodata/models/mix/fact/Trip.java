package com.epam.tcodata.models.mix.fact;

import com.epam.tcodata.models.mix.Entity;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Maps to SOAP Trip.
 */
public class Trip extends Entity {

    private static final long serialVersionUID = -577455718914132423L;

    private Long tripId;
    private Long assetId;
    private Long driverId;
    private Timestamp tripStart;
    private Timestamp tripEnd;
    private String notes;
    private String pulseParameterName;
    @JsonProperty("SubTrips")
    private List<SubTrip> subTripList;
    private Integer engineSeconds;
    private Long startPositionId;
    private Position startPosition;
    private Long endPositionId;
    private Position endPosition;
    private Timestamp firstDepart;
    private Timestamp lastHalt;
    private Double drivingTime;
    private Double standingTime;
    private Double duration;
    private Double distanceKilometers;
    private Double startOdometerKilometers;
    private Double endOdometerKilometers;
    private Integer startEngineSeconds;
    private Integer endEngineSeconds;
    private Double pulseValue;
    private Double fuelUsedLitres;
    private Double maxSpeedKilometersPerHour;
    private Double maxAccelerationKilometersPerHourPerSecond;
    private Double maxDecelerationKilometersPerHourPerSecond;
    private Double maxRpm;

    public Trip() {
        this.subTripList = new ArrayList<>();
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Timestamp getTripStart() {
        return tripStart;
    }

    public void setTripStart(Timestamp tripStart) {
        this.tripStart = tripStart;
    }

    public Timestamp getTripEnd() {
        return tripEnd;
    }

    public void setTripEnd(Timestamp tripEnd) {
        this.tripEnd = tripEnd;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPulseParameterName() {
        return pulseParameterName;
    }

    public void setPulseParameterName(String pulseParameterName) {
        this.pulseParameterName = pulseParameterName;
    }

    public List<SubTrip> getSubTripList() {
        return subTripList;
    }

    public void setSubTripList(List<SubTrip> subTripList) {
        this.subTripList.clear();
        this.subTripList.addAll(subTripList);
    }

    public Integer getEngineSeconds() {
        return engineSeconds;
    }

    public void setEngineSeconds(Integer engineSeconds) {
        this.engineSeconds = engineSeconds;
    }

    public Long getStartPositionId() {
        return startPositionId;
    }

    public void setStartPositionId(Long startPositionId) {
        this.startPositionId = startPositionId;
    }

    public Position getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Position startPosition) {
        this.startPosition = startPosition;
    }

    public Long getEndPositionId() {
        return endPositionId;
    }

    public void setEndPositionId(Long endPositionId) {
        this.endPositionId = endPositionId;
    }

    public Position getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(Position endPosition) {
        this.endPosition = endPosition;
    }

    public Timestamp getFirstDepart() {
        return firstDepart;
    }

    public void setFirstDepart(Timestamp firstDepart) {
        this.firstDepart = firstDepart;
    }

    public Timestamp getLastHalt() {
        return lastHalt;
    }

    public void setLastHalt(Timestamp lastHalt) {
        this.lastHalt = lastHalt;
    }

    public Double getDrivingTime() {
        return drivingTime;
    }

    public void setDrivingTime(Double drivingTime) {
        this.drivingTime = drivingTime;
    }

    public Double getStandingTime() {
        return standingTime;
    }

    public void setStandingTime(Double standingTime) {
        this.standingTime = standingTime;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Double getDistanceKilometers() {
        return distanceKilometers;
    }

    public void setDistanceKilometers(Double distanceKilometers) {
        this.distanceKilometers = distanceKilometers;
    }

    public Double getStartOdometerKilometers() {
        return startOdometerKilometers;
    }

    public void setStartOdometerKilometers(Double startOdometerKilometers) {
        this.startOdometerKilometers = startOdometerKilometers;
    }

    public Double getEndOdometerKilometers() {
        return endOdometerKilometers;
    }

    public void setEndOdometerKilometers(Double endOdometerKilometers) {
        this.endOdometerKilometers = endOdometerKilometers;
    }

    public Integer getStartEngineSeconds() {
        return startEngineSeconds;
    }

    public void setStartEngineSeconds(Integer startEngineSeconds) {
        this.startEngineSeconds = startEngineSeconds;
    }

    public Integer getEndEngineSeconds() {
        return endEngineSeconds;
    }

    public void setEndEngineSeconds(Integer endEngineSeconds) {
        this.endEngineSeconds = endEngineSeconds;
    }

    public Double getPulseValue() {
        return pulseValue;
    }

    public void setPulseValue(Double pulseValue) {
        this.pulseValue = pulseValue;
    }

    public Double getFuelUsedLitres() {
        return fuelUsedLitres;
    }

    public void setFuelUsedLitres(Double fuelUsedLitres) {
        this.fuelUsedLitres = fuelUsedLitres;
    }

    public Double getMaxSpeedKilometersPerHour() {
        return maxSpeedKilometersPerHour;
    }

    public void setMaxSpeedKilometersPerHour(Double maxSpeedKilometersPerHour) {
        this.maxSpeedKilometersPerHour = maxSpeedKilometersPerHour;
    }

    public Double getMaxAccelerationKilometersPerHourPerSecond() {
        return maxAccelerationKilometersPerHourPerSecond;
    }

    public void setMaxAccelerationKilometersPerHourPerSecond(Double maxAccelerationKilometersPerHourPerSecond) {
        this.maxAccelerationKilometersPerHourPerSecond = maxAccelerationKilometersPerHourPerSecond;
    }

    public Double getMaxDecelerationKilometersPerHourPerSecond() {
        return maxDecelerationKilometersPerHourPerSecond;
    }

    public void setMaxDecelerationKilometersPerHourPerSecond(Double maxDecelerationKilometersPerHourPerSecond) {
        this.maxDecelerationKilometersPerHourPerSecond = maxDecelerationKilometersPerHourPerSecond;
    }

    public Double getMaxRpm() {
        return maxRpm;
    }

    public void setMaxRpm(Double maxRpm) {
        this.maxRpm = maxRpm;
    }

    @Override
    public String toString() {
        return "Trip{"
                + "tripId=" + tripId
                + ", assetId=" + assetId
                + ", driverId=" + driverId
                + ", tripStart=" + tripStart
                + ", tripEnd=" + tripEnd
                + ", notes='" + notes + '\''
                + ", pulseParameterName='" + pulseParameterName + '\''
                + ", subTripList=" + subTripList
                + ", engineSeconds=" + engineSeconds
                + ", startPositionId=" + startPositionId
                + ", startPosition=" + startPosition
                + ", endPositionId=" + endPositionId
                + ", endPosition=" + endPosition
                + ", firstDepart=" + firstDepart
                + ", lastHalt=" + lastHalt
                + ", drivingTime=" + drivingTime
                + ", standingTime=" + standingTime
                + ", duration=" + duration
                + ", distanceKilometers=" + distanceKilometers
                + ", startOdometerKilometers=" + startOdometerKilometers
                + ", endOdometerKilometers=" + endOdometerKilometers
                + ", startEngineSeconds=" + startEngineSeconds
                + ", endEngineSeconds=" + endEngineSeconds
                + ", pulseValue=" + pulseValue
                + ", fuelUsedLitres=" + fuelUsedLitres
                + ", maxSpeedKilometersPerHour=" + maxSpeedKilometersPerHour
                + ", maxAccelerationKilometersPerHourPerSecond=" + maxAccelerationKilometersPerHourPerSecond
                + ", maxDecelerationKilometersPerHourPerSecond=" + maxDecelerationKilometersPerHourPerSecond
                + ", maxRpm=" + maxRpm
                + "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return  Objects.equals(tripId, trip.tripId)
                && Objects.equals(assetId, trip.assetId)
                && Objects.equals(driverId, trip.driverId)
                && Objects.equals(tripStart, trip.tripStart)
                && Objects.equals(tripEnd, trip.tripEnd)
                && Objects.equals(notes, trip.notes)
                && Objects.equals(pulseParameterName, trip.pulseParameterName)
                && Objects.equals(subTripList, trip.subTripList)
                && Objects.equals(engineSeconds, trip.engineSeconds)
                && Objects.equals(startPositionId, trip.startPositionId)
                && Objects.equals(startPosition, trip.startPosition)
                && Objects.equals(endPositionId, trip.endPositionId)
                && Objects.equals(endPosition, trip.endPosition)
                && Objects.equals(firstDepart, trip.firstDepart)
                && Objects.equals(lastHalt, trip.lastHalt)
                && Objects.equals(drivingTime, trip.drivingTime)
                && Objects.equals(standingTime, trip.standingTime)
                && Objects.equals(duration, trip.duration)
                && Objects.equals(distanceKilometers, trip.distanceKilometers)
                && Objects.equals(startOdometerKilometers, trip.startOdometerKilometers)
                && Objects.equals(endOdometerKilometers, trip.endOdometerKilometers)
                && Objects.equals(startEngineSeconds, trip.startEngineSeconds)
                && Objects.equals(endEngineSeconds, trip.endEngineSeconds)
                && Objects.equals(pulseValue, trip.pulseValue)
                && Objects.equals(fuelUsedLitres, trip.fuelUsedLitres)
                && Objects.equals(maxSpeedKilometersPerHour, trip.maxSpeedKilometersPerHour)
                && Objects.equals(maxAccelerationKilometersPerHourPerSecond, trip.maxAccelerationKilometersPerHourPerSecond)
                && Objects.equals(maxDecelerationKilometersPerHourPerSecond, trip.maxDecelerationKilometersPerHourPerSecond)
                && Objects.equals(maxRpm, trip.maxRpm);
    }

    @Override
    public int hashCode() {

        return Objects.hash(tripId, assetId, driverId, tripStart, tripEnd, notes, pulseParameterName, subTripList,
                engineSeconds, startPositionId, startPosition, endPositionId, endPosition, firstDepart, lastHalt,
                drivingTime, standingTime, duration, distanceKilometers, startOdometerKilometers, endOdometerKilometers,
                startEngineSeconds, endEngineSeconds, pulseValue, fuelUsedLitres, maxSpeedKilometersPerHour,
                maxAccelerationKilometersPerHourPerSecond, maxDecelerationKilometersPerHourPerSecond, maxRpm);
    }


    public static final class TripBuilder {
        private Long tripId;
        private Long assetId;
        private Long driverId;
        private Timestamp tripStart;
        private Timestamp tripEnd;
        private String notes;
        private String pulseParameterName;
        private List<SubTrip> subTripList;
        private Integer engineSeconds;
        private Long startPositionId;
        private Position startPosition;
        private Long endPositionId;
        private Position endPosition;
        private Timestamp firstDepart;
        private Timestamp lastHalt;
        private Double drivingTime;
        private Double standingTime;
        private Double duration;
        private Double distanceKilometers;
        private Double startOdometerKilometers;
        private Double endOdometerKilometers;
        private Integer startEngineSeconds;
        private Integer endEngineSeconds;
        private Double pulseValue;
        private Double fuelUsedLitres;
        private Double maxSpeedKilometersPerHour;
        private Double maxAccelerationKilometersPerHourPerSecond;
        private Double maxDecelerationKilometersPerHourPerSecond;
        private Double maxRpm;

        public TripBuilder() {
            /***  Default implementation ***/
        }

        public TripBuilder setTripId(Long tripId) {
            this.tripId = tripId;
            return this;
        }

        public TripBuilder setAssetId(Long assetId) {
            this.assetId = assetId;
            return this;
        }

        public TripBuilder setDriverId(Long driverId) {
            this.driverId = driverId;
            return this;
        }

        public TripBuilder setTripStart(Timestamp tripStart) {
            this.tripStart = tripStart;
            return this;
        }

        public TripBuilder setTripEnd(Timestamp tripEnd) {
            this.tripEnd = tripEnd;
            return this;
        }

        public TripBuilder setNotes(String notes) {
            this.notes = notes;
            return this;
        }

        public TripBuilder setPulseParameterName(String pulseParameterName) {
            this.pulseParameterName = pulseParameterName;
            return this;
        }

        public TripBuilder setSubTripList(List<SubTrip> subTripList) {
            this.subTripList = subTripList;
            return this;
        }

        public TripBuilder setEngineSeconds(Integer engineSeconds) {
            this.engineSeconds = engineSeconds;
            return this;
        }

        public TripBuilder setStartPositionId(Long startPositionId) {
            this.startPositionId = startPositionId;
            return this;
        }

        public TripBuilder setStartPosition(Position startPosition) {
            this.startPosition = startPosition;
            return this;
        }

        public TripBuilder setEndPositionId(Long endPositionId) {
            this.endPositionId = endPositionId;
            return this;
        }

        public TripBuilder setEndPosition(Position endPosition) {
            this.endPosition = endPosition;
            return this;
        }

        public TripBuilder setFirstDepart(Timestamp firstDepart) {
            this.firstDepart = firstDepart;
            return this;
        }

        public TripBuilder setLastHalt(Timestamp lastHalt) {
            this.lastHalt = lastHalt;
            return this;
        }

        public TripBuilder setDrivingTime(Double drivingTime) {
            this.drivingTime = drivingTime;
            return this;
        }

        public TripBuilder setStandingTime(Double standingTime) {
            this.standingTime = standingTime;
            return this;
        }

        public TripBuilder setDuration(Double duration) {
            this.duration = duration;
            return this;
        }

        public TripBuilder setDistanceKilometers(Double distanceKilometers) {
            this.distanceKilometers = distanceKilometers;
            return this;
        }

        public TripBuilder setStartOdometerKilometers(Double startOdometerKilometers) {
            this.startOdometerKilometers = startOdometerKilometers;
            return this;
        }

        public TripBuilder setEndOdometerKilometers(Double endOdometerKilometers) {
            this.endOdometerKilometers = endOdometerKilometers;
            return this;
        }

        public TripBuilder setStartEngineSeconds(Integer startEngineSeconds) {
            this.startEngineSeconds = startEngineSeconds;
            return this;
        }

        public TripBuilder setEndEngineSeconds(Integer endEngineSeconds) {
            this.endEngineSeconds = endEngineSeconds;
            return this;
        }

        public TripBuilder setPulseValue(Double pulseValue) {
            this.pulseValue = pulseValue;
            return this;
        }

        public TripBuilder setFuelUsedLitres(Double fuelUsedLitres) {
            this.fuelUsedLitres = fuelUsedLitres;
            return this;
        }

        public TripBuilder setMaxSpeedKilometersPerHour(Double maxSpeedKilometersPerHour) {
            this.maxSpeedKilometersPerHour = maxSpeedKilometersPerHour;
            return this;
        }

        public TripBuilder setMaxAccelerationKilometersPerHourPerSecond(Double maxAccelerationKilometersPerHourPerSecond) {
            this.maxAccelerationKilometersPerHourPerSecond = maxAccelerationKilometersPerHourPerSecond;
            return this;
        }

        public TripBuilder setMaxDecelerationKilometersPerHourPerSecond(Double maxDecelerationKilometersPerHourPerSecond) {
            this.maxDecelerationKilometersPerHourPerSecond = maxDecelerationKilometersPerHourPerSecond;
            return this;
        }

        public TripBuilder setMaxRpm(Double maxRpm) {
            this.maxRpm = maxRpm;
            return this;
        }

        /**
         * Build entity with specified parameters.
         * @return new entity.
         */
        public Trip build() {
            Trip trip = new Trip();
            trip.setTripId(tripId);
            trip.setAssetId(assetId);
            trip.setDriverId(driverId);
            trip.setTripStart(tripStart);
            trip.setTripEnd(tripEnd);
            trip.setNotes(notes);
            trip.setPulseParameterName(pulseParameterName);
            trip.setSubTripList(subTripList == null ? new ArrayList<>() : subTripList);
            trip.setEngineSeconds(engineSeconds);
            trip.setStartPositionId(startPositionId);
            trip.setStartPosition(startPosition);
            trip.setEndPositionId(endPositionId);
            trip.setEndPosition(endPosition);
            trip.setFirstDepart(firstDepart);
            trip.setLastHalt(lastHalt);
            trip.setDrivingTime(drivingTime);
            trip.setStandingTime(standingTime);
            trip.setDuration(duration);
            trip.setDistanceKilometers(distanceKilometers);
            trip.setStartOdometerKilometers(startOdometerKilometers);
            trip.setEndOdometerKilometers(endOdometerKilometers);
            trip.setStartEngineSeconds(startEngineSeconds);
            trip.setEndEngineSeconds(endEngineSeconds);
            trip.setPulseValue(pulseValue);
            trip.setFuelUsedLitres(fuelUsedLitres);
            trip.setMaxSpeedKilometersPerHour(maxSpeedKilometersPerHour);
            trip.setMaxAccelerationKilometersPerHourPerSecond(maxAccelerationKilometersPerHourPerSecond);
            trip.setMaxDecelerationKilometersPerHourPerSecond(maxDecelerationKilometersPerHourPerSecond);
            trip.setMaxRpm(maxRpm);
            return trip;
        }
    }
}
