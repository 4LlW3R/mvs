package com.epam.tcodata.models.mix.fact;

import com.epam.tcodata.models.mix.Entity;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * Maps to SOAP SubTrip.
 */
public class SubTrip extends Entity {

    private static final long serialVersionUID = -1263044940283180912L;

    private Timestamp subTripStart;
    private Long startPositionId;
    private Position startPosition;
    private Timestamp depart;
    private Timestamp halt;
    private Timestamp subTripEnd;
    private Long endPositionId;
    private Position endPosition;
    private Integer drivingTime;
    private Integer standingTime;
    private Integer duration;
    private Double distanceKilometres;
    private Double startOdometerKilometres;
    private Double endOdometerKilometres;
    private Integer startEngineSeconds;
    private Integer endEngineSeconds;
    private Integer engineSeconds;
    private Double pulseValue;
    private Double fuelUsedLitres;
    private Double maxSpeedKilometersPerHour;
    private Double maxAccelerationKilometersPerHourPerSecond;
    private Double maxDecelerationKilometersPerHourPerSecond;
    private Double maxRpm;

    public SubTrip() {
        /***  Default implementation ***/
    }

    public Timestamp getSubTripStart() {
        return subTripStart;
    }

    public void setSubTripStart(Timestamp subTripStart) {
        this.subTripStart = subTripStart;
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

    public Timestamp getDepart() {
        return depart;
    }

    public void setDepart(Timestamp depart) {
        this.depart = depart;
    }

    public Timestamp getHalt() {
        return halt;
    }

    public void setHalt(Timestamp halt) {
        this.halt = halt;
    }

    public Timestamp getSubTripEnd() {
        return subTripEnd;
    }

    public void setSubTripEnd(Timestamp subTripEnd) {
        this.subTripEnd = subTripEnd;
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

    public Integer getDrivingTime() {
        return drivingTime;
    }

    public void setDrivingTime(Integer drivingTime) {
        this.drivingTime = drivingTime;
    }

    public Integer getStandingTime() {
        return standingTime;
    }

    public void setStandingTime(Integer standingTime) {
        this.standingTime = standingTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Double getDistanceKilometres() {
        return distanceKilometres;
    }

    public void setDistanceKilometres(Double distanceKilometres) {
        this.distanceKilometres = distanceKilometres;
    }

    public Double getStartOdometerKilometres() {
        return startOdometerKilometres;
    }

    public void setStartOdometerKilometres(Double startOdometerKilometres) {
        this.startOdometerKilometres = startOdometerKilometres;
    }

    public Double getEndOdometerKilometres() {
        return endOdometerKilometres;
    }

    public void setEndOdometerKilometres(Double endOdometerKilometres) {
        this.endOdometerKilometres = endOdometerKilometres;
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

    public Integer getEngineSeconds() {
        return engineSeconds;
    }

    public void setEngineSeconds(Integer engineSeconds) {
        this.engineSeconds = engineSeconds;
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
        return "SubTrip{"
                + "subTripStart=" + subTripStart
                + ", startPositionId=" + startPositionId
                + ", startPosition=" + startPosition
                + ", depart=" + depart
                + ", halt=" + halt
                + ", subTripEnd=" + subTripEnd
                + ", endPositionId=" + endPositionId
                + ", endPosition=" + endPosition
                + ", drivingTime=" + drivingTime
                + ", standingTime=" + standingTime
                + ", duration=" + duration
                + ", distanceKilometres=" + distanceKilometres
                + ", startOdometerKilometres=" + startOdometerKilometres
                + ", endOdometerKilometres=" + endOdometerKilometres
                + ", startEngineSeconds=" + startEngineSeconds
                + ", endEngineSeconds=" + endEngineSeconds
                + ", engineSeconds=" + engineSeconds
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
        SubTrip subTrip = (SubTrip) o;
        return  Objects.equals(subTripStart, subTrip.subTripStart)
                && Objects.equals(startPositionId, subTrip.startPositionId)
                && Objects.equals(startPosition, subTrip.startPosition)
                && Objects.equals(depart, subTrip.depart)
                && Objects.equals(halt, subTrip.halt)
                && Objects.equals(subTripEnd, subTrip.subTripEnd)
                && Objects.equals(endPositionId, subTrip.endPositionId)
                && Objects.equals(endPosition, subTrip.endPosition)
                && Objects.equals(drivingTime, subTrip.drivingTime)
                && Objects.equals(standingTime, subTrip.standingTime)
                && Objects.equals(duration, subTrip.duration)
                && Objects.equals(distanceKilometres, subTrip.distanceKilometres)
                && Objects.equals(startOdometerKilometres, subTrip.startOdometerKilometres)
                && Objects.equals(endOdometerKilometres, subTrip.endOdometerKilometres)
                && Objects.equals(startEngineSeconds, subTrip.startEngineSeconds)
                && Objects.equals(endEngineSeconds, subTrip.endEngineSeconds)
                && Objects.equals(engineSeconds, subTrip.engineSeconds)
                && Objects.equals(pulseValue, subTrip.pulseValue)
                && Objects.equals(fuelUsedLitres, subTrip.fuelUsedLitres)
                && Objects.equals(maxSpeedKilometersPerHour, subTrip.maxSpeedKilometersPerHour)
                && Objects.equals(maxAccelerationKilometersPerHourPerSecond, subTrip.maxAccelerationKilometersPerHourPerSecond)
                && Objects.equals(maxDecelerationKilometersPerHourPerSecond, subTrip.maxDecelerationKilometersPerHourPerSecond)
                && Objects.equals(maxRpm, subTrip.maxRpm);
    }

    @Override
    public int hashCode() {

        return Objects.hash(subTripStart, startPositionId, startPosition, depart, halt, subTripEnd, endPositionId,
                endPosition, drivingTime, standingTime, duration, distanceKilometres, startOdometerKilometres,
                endOdometerKilometres, startEngineSeconds, endEngineSeconds, engineSeconds, pulseValue, fuelUsedLitres,
                maxSpeedKilometersPerHour, maxAccelerationKilometersPerHourPerSecond,
                maxDecelerationKilometersPerHourPerSecond, maxRpm);
    }


    public static final class SubTripBuilder {
        private Timestamp subTripStart;
        private Long startPositionId;
        private Position startPosition;
        private Timestamp depart;
        private Timestamp halt;
        private Timestamp subTripEnd;
        private Long endPositionId;
        private Position endPosition;
        private Integer drivingTime;
        private Integer standingTime;
        private Integer duration;
        private Double distanceKilometres;
        private Double startOdometerKilometres;
        private Double endOdometerKilometres;
        private Integer startEngineSeconds;
        private Integer endEngineSeconds;
        private Integer engineSeconds;
        private Double pulseValue;
        private Double fuelUsedLitres;
        private Double maxSpeedKilometersPerHour;
        private Double maxAccelerationKilometersPerHourPerSecond;
        private Double maxDecelerationKilometersPerHourPerSecond;
        private Double maxRpm;

        public SubTripBuilder() {
            /***  Default implementation ***/
        }

        public SubTripBuilder setSubTripStart(Timestamp subTripStart) {
            this.subTripStart = subTripStart;
            return this;
        }

        public SubTripBuilder setStartPositionId(Long startPositionId) {
            this.startPositionId = startPositionId;
            return this;
        }

        public SubTripBuilder setStartPosition(Position startPosition) {
            this.startPosition = startPosition;
            return this;
        }

        public SubTripBuilder setDepart(Timestamp depart) {
            this.depart = depart;
            return this;
        }

        public SubTripBuilder setHalt(Timestamp halt) {
            this.halt = halt;
            return this;
        }

        public SubTripBuilder setSubTripEnd(Timestamp subTripEnd) {
            this.subTripEnd = subTripEnd;
            return this;
        }

        public SubTripBuilder setEndPositionId(Long endPositionId) {
            this.endPositionId = endPositionId;
            return this;
        }

        public SubTripBuilder setEndPosition(Position endPosition) {
            this.endPosition = endPosition;
            return this;
        }

        public SubTripBuilder setDrivingTime(Integer drivingTime) {
            this.drivingTime = drivingTime;
            return this;
        }

        public SubTripBuilder setStandingTime(Integer standingTime) {
            this.standingTime = standingTime;
            return this;
        }

        public SubTripBuilder setDuration(Integer duration) {
            this.duration = duration;
            return this;
        }

        public SubTripBuilder setDistanceKilometres(Double distanceKilometres) {
            this.distanceKilometres = distanceKilometres;
            return this;
        }

        public SubTripBuilder setStartOdometerKilometres(Double startOdometerKilometres) {
            this.startOdometerKilometres = startOdometerKilometres;
            return this;
        }

        public SubTripBuilder setEndOdometerKilometres(Double endOdometerKilometres) {
            this.endOdometerKilometres = endOdometerKilometres;
            return this;
        }

        public SubTripBuilder setStartEngineSeconds(Integer startEngineSeconds) {
            this.startEngineSeconds = startEngineSeconds;
            return this;
        }

        public SubTripBuilder setEndEngineSeconds(Integer endEngineSeconds) {
            this.endEngineSeconds = endEngineSeconds;
            return this;
        }

        public SubTripBuilder setEngineSeconds(Integer engineSeconds) {
            this.engineSeconds = engineSeconds;
            return this;
        }

        public SubTripBuilder setPulseValue(Double pulseValue) {
            this.pulseValue = pulseValue;
            return this;
        }

        public SubTripBuilder setFuelUsedLitres(Double fuelUsedLitres) {
            this.fuelUsedLitres = fuelUsedLitres;
            return this;
        }

        public SubTripBuilder setMaxSpeedKilometersPerHour(Double maxSpeedKilometersPerHour) {
            this.maxSpeedKilometersPerHour = maxSpeedKilometersPerHour;
            return this;
        }

        public SubTripBuilder setMaxAccelerationKilometersPerHourPerSecond(Double maxAccelerationKilometersPerHourPerSecond) {
            this.maxAccelerationKilometersPerHourPerSecond = maxAccelerationKilometersPerHourPerSecond;
            return this;
        }

        public SubTripBuilder setMaxDecelerationKilometersPerHourPerSecond(Double maxDecelerationKilometersPerHourPerSecond) {
            this.maxDecelerationKilometersPerHourPerSecond = maxDecelerationKilometersPerHourPerSecond;
            return this;
        }

        public SubTripBuilder setMaxRpm(Double maxRpm) {
            this.maxRpm = maxRpm;
            return this;
        }

        /**
         * Build entity with specified parameters.
         * @return new entity.
         */
        public SubTrip build() {
            SubTrip subTrip = new SubTrip();
            subTrip.setSubTripStart(subTripStart);
            subTrip.setStartPositionId(startPositionId);
            subTrip.setStartPosition(startPosition);
            subTrip.setDepart(depart);
            subTrip.setHalt(halt);
            subTrip.setSubTripEnd(subTripEnd);
            subTrip.setEndPositionId(endPositionId);
            subTrip.setEndPosition(endPosition);
            subTrip.setDrivingTime(drivingTime);
            subTrip.setStandingTime(standingTime);
            subTrip.setDuration(duration);
            subTrip.setDistanceKilometres(distanceKilometres);
            subTrip.setStartOdometerKilometres(startOdometerKilometres);
            subTrip.setEndOdometerKilometres(endOdometerKilometres);
            subTrip.setStartEngineSeconds(startEngineSeconds);
            subTrip.setEndEngineSeconds(endEngineSeconds);
            subTrip.setEngineSeconds(engineSeconds);
            subTrip.setPulseValue(pulseValue);
            subTrip.setFuelUsedLitres(fuelUsedLitres);
            subTrip.setMaxSpeedKilometersPerHour(maxSpeedKilometersPerHour);
            subTrip.setMaxAccelerationKilometersPerHourPerSecond(maxAccelerationKilometersPerHourPerSecond);
            subTrip.setMaxDecelerationKilometersPerHourPerSecond(maxDecelerationKilometersPerHourPerSecond);
            subTrip.setMaxRpm(maxRpm);
            return subTrip;
        }
    }
}
