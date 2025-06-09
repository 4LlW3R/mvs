package com.epam.tcodata.models.mix.fact;

import com.epam.tcodata.models.mix.Entity;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * Maps to SOAP GPSPositionV2.
 */
public class Position extends Entity {

    private static final long serialVersionUID = -7677084452942502604L;

    private Long positionId;
    private Long assetId;
    private Long driverId;
    private Timestamp timestamp;
    private Double latitude;
    private Double longitude;
    private Double speedKilometresPerHour;
    private Double speedLimit;
    private Integer altitudeMetres;
    private Integer heading;
    private Integer numberOfSatellites;
    private Integer hdop;
    private Integer vdop;
    private Integer pdop;
    private Long ageOfReadingSeconds;
    private Integer distanceSinceReadingKilometres;
    private Boolean ignitionOn;
    private Double odometerKilometres;
    private String formattedAddress;
    private String source;
    @JsonProperty("IsAvl")
    private Boolean avl;

    public Position() {
        /***  Default implementation ***/
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getSpeedKilometresPerHour() {
        return speedKilometresPerHour;
    }

    public void setSpeedKilometresPerHour(Double speedKilometresPerHour) {
        this.speedKilometresPerHour = speedKilometresPerHour;
    }

    public Double getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(Double speedLimit) {
        this.speedLimit = speedLimit;
    }

    public Integer getAltitudeMetres() {
        return altitudeMetres;
    }

    public void setAltitudeMetres(Integer altitudeMetres) {
        this.altitudeMetres = altitudeMetres;
    }

    public Integer getHeading() {
        return heading;
    }

    public void setHeading(Integer heading) {
        this.heading = heading;
    }

    public Integer getNumberOfSatellites() {
        return numberOfSatellites;
    }

    public void setNumberOfSatellites(Integer numberOfSatellites) {
        this.numberOfSatellites = numberOfSatellites;
    }

    public Integer getHdop() {
        return hdop;
    }

    public void setHdop(Integer hdop) {
        this.hdop = hdop;
    }

    public Integer getVdop() {
        return vdop;
    }

    public void setVdop(Integer vdop) {
        this.vdop = vdop;
    }

    public Integer getPdop() {
        return pdop;
    }

    public void setPdop(Integer pdop) {
        this.pdop = pdop;
    }

    public Long getAgeOfReadingSeconds() {
        return ageOfReadingSeconds;
    }

    public void setAgeOfReadingSeconds(Long ageOfReadingSeconds) {
        this.ageOfReadingSeconds = ageOfReadingSeconds;
    }

    public Integer getDistanceSinceReadingKilometres() {
        return distanceSinceReadingKilometres;
    }

    public void setDistanceSinceReadingKilometres(Integer distanceSinceReadingKilometres) {
        this.distanceSinceReadingKilometres = distanceSinceReadingKilometres;
    }

    public Boolean getIgnitionOn() {
        return ignitionOn;
    }

    public void setIgnitionOn(Boolean ignitionOn) {
        this.ignitionOn = ignitionOn;
    }

    public Double getOdometerKilometres() {
        return odometerKilometres;
    }

    public void setOdometerKilometres(Double odometerKilometres) {
        this.odometerKilometres = odometerKilometres;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Boolean getAvl() {
        return avl;
    }

    public void setAvl(Boolean avl) {
        this.avl = avl;
    }

    @Override
    public String toString() {
        return "Position{"
                + "positionId=" + positionId
                + ", assetId=" + assetId
                + ", driverId=" + driverId
                + ", timestamp=" + timestamp
                + ", latitude=" + latitude
                + ", longitude=" + longitude
                + ", speedKilometresPerHour=" + speedKilometresPerHour
                + ", speedLimit=" + speedLimit
                + ", altitudeMetres=" + altitudeMetres
                + ", heading=" + heading
                + ", numberOfSatellites=" + numberOfSatellites
                + ", hdop=" + hdop
                + ", vdop=" + vdop
                + ", pdop=" + pdop
                + ", ageOfReadingSeconds=" + ageOfReadingSeconds
                + ", distanceSinceReadingKilometres=" + distanceSinceReadingKilometres
                + ", ignitionOn=" + ignitionOn
                + ", odometerKilometres=" + odometerKilometres
                + ", formattedAddress='" + formattedAddress + '\''
                + ", source='" + source + '\''
                + ", avl=" + avl
                + "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Objects.equals(positionId, position.positionId)
                && Objects.equals(assetId, position.assetId)
                && Objects.equals(driverId, position.driverId)
                && Objects.equals(timestamp, position.timestamp)
                && Objects.equals(latitude, position.latitude)
                && Objects.equals(longitude, position.longitude)
                && Objects.equals(speedKilometresPerHour, position.speedKilometresPerHour)
                && Objects.equals(speedLimit, position.speedLimit)
                && Objects.equals(altitudeMetres, position.altitudeMetres)
                && Objects.equals(heading, position.heading)
                && Objects.equals(numberOfSatellites, position.numberOfSatellites)
                && Objects.equals(hdop, position.hdop)
                && Objects.equals(vdop, position.vdop)
                && Objects.equals(pdop, position.pdop)
                && Objects.equals(ageOfReadingSeconds, position.ageOfReadingSeconds)
                && Objects.equals(distanceSinceReadingKilometres, position.distanceSinceReadingKilometres)
                && Objects.equals(ignitionOn, position.ignitionOn)
                && Objects.equals(odometerKilometres, position.odometerKilometres)
                && Objects.equals(formattedAddress, position.formattedAddress)
                && Objects.equals(source, position.source)
                && Objects.equals(avl, position.avl);
    }

    @Override
    public int hashCode() {

        return Objects.hash(positionId, assetId, driverId, timestamp, latitude, longitude, speedKilometresPerHour,
                speedLimit, altitudeMetres, heading, numberOfSatellites, hdop, vdop, pdop, ageOfReadingSeconds,
                distanceSinceReadingKilometres, ignitionOn, odometerKilometres, formattedAddress, source, avl);
    }


    public static final class PositionBuilder {
        private Long positionId;
        private Long assetId;
        private Long driverId;
        private Timestamp timestamp;
        private Double latitude;
        private Double longitude;
        private Double speedKilometresPerHour;
        private Double speedLimit;
        private Integer altitudeMetres;
        private Integer heading;
        private Integer numberOfSatellites;
        private Integer hdop;
        private Integer vdop;
        private Integer pdop;
        private Long ageOfReadingSeconds;
        private Integer distanceSinceReadingKilometres;
        private Boolean ignitionOn;
        private Double odometerKilometres;
        private String formattedAddress;
        private String source;
        private Boolean avl;

        public PositionBuilder() {
            /***  Default implementation ***/
        }

        public PositionBuilder setPositionId(Long positionId) {
            this.positionId = positionId;
            return this;
        }

        public PositionBuilder setAssetId(Long assetId) {
            this.assetId = assetId;
            return this;
        }

        public PositionBuilder setDriverId(Long driverId) {
            this.driverId = driverId;
            return this;
        }

        public PositionBuilder setTimestamp(Timestamp timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public PositionBuilder setLatitude(Double latitude) {
            this.latitude = latitude;
            return this;
        }

        public PositionBuilder setLongitude(Double longitude) {
            this.longitude = longitude;
            return this;
        }

        public PositionBuilder setSpeedKilometresPerHour(Double speedKilometresPerHour) {
            this.speedKilometresPerHour = speedKilometresPerHour;
            return this;
        }

        public PositionBuilder setSpeedLimit(Double speedLimit) {
            this.speedLimit = speedLimit;
            return this;
        }

        public PositionBuilder setAltitudeMetres(Integer altitudeMetres) {
            this.altitudeMetres = altitudeMetres;
            return this;
        }

        public PositionBuilder setHeading(Integer heading) {
            this.heading = heading;
            return this;
        }

        public PositionBuilder setNumberOfSatellites(Integer numberOfSatellites) {
            this.numberOfSatellites = numberOfSatellites;
            return this;
        }

        public PositionBuilder setHdop(Integer hdop) {
            this.hdop = hdop;
            return this;
        }

        public PositionBuilder setVdop(Integer vdop) {
            this.vdop = vdop;
            return this;
        }

        public PositionBuilder setPdop(Integer pdop) {
            this.pdop = pdop;
            return this;
        }

        public PositionBuilder setAgeOfReadingSeconds(Long ageOfReadingSeconds) {
            this.ageOfReadingSeconds = ageOfReadingSeconds;
            return this;
        }

        public PositionBuilder setDistanceSinceReadingKilometres(Integer distanceSinceReadingKilometres) {
            this.distanceSinceReadingKilometres = distanceSinceReadingKilometres;
            return this;
        }

        public PositionBuilder setIgnitionOn(Boolean ignitionOn) {
            this.ignitionOn = ignitionOn;
            return this;
        }

        public PositionBuilder setOdometerKilometres(Double odometerKilometres) {
            this.odometerKilometres = odometerKilometres;
            return this;
        }

        public PositionBuilder setFormattedAddress(String formattedAddress) {
            this.formattedAddress = formattedAddress;
            return this;
        }

        public PositionBuilder setSource(String source) {
            this.source = source;
            return this;
        }

        public PositionBuilder setAvl(Boolean avl) {
            this.avl = avl;
            return this;
        }

        /**
         * Build entity with specified parameters.
         * @return new entity.
         */
        public Position build() {
            Position position = new Position();
            position.setPositionId(positionId);
            position.setAssetId(assetId);
            position.setDriverId(driverId);
            position.setTimestamp(timestamp);
            position.setLatitude(latitude);
            position.setLongitude(longitude);
            position.setSpeedKilometresPerHour(speedKilometresPerHour);
            position.setSpeedLimit(speedLimit);
            position.setAltitudeMetres(altitudeMetres);
            position.setHeading(heading);
            position.setNumberOfSatellites(numberOfSatellites);
            position.setHdop(hdop);
            position.setVdop(vdop);
            position.setPdop(pdop);
            position.setAgeOfReadingSeconds(ageOfReadingSeconds);
            position.setDistanceSinceReadingKilometres(distanceSinceReadingKilometres);
            position.setIgnitionOn(ignitionOn);
            position.setOdometerKilometres(odometerKilometres);
            position.setFormattedAddress(formattedAddress);
            position.setSource(source);
            position.setAvl(avl);
            return position;
        }
    }
}
