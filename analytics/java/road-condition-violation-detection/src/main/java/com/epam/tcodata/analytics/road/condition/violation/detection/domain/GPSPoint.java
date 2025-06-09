package com.epam.tcodata.analytics.road.condition.violation.detection.domain;

import java.io.Serializable;
import java.sql.Timestamp;

public class GPSPoint implements Serializable {
    private static final long serialVersionUID = 2627636364582340261L;

    private Double latitude;
    private Double longitude;
    private Timestamp time;
    private Double velocity;
    private String vehicleId;
    private Long subscriptionId;
    private String driverId;
    private String externalId;

    public GPSPoint() {
        /***  Default implementation ***/
    }

    public Double getLatitude() {
        return latitude;
    }

    public GPSPoint setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Double getLongitude() {
        return longitude;
    }

    public GPSPoint setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public Timestamp getTime() {
        return time == null ? null : new Timestamp(time.getTime());
    }

    public GPSPoint setTime(Timestamp time) {
        this.time = time == null ? null : new Timestamp(time.getTime());
        return this;
    }

    public Double getVelocity() {
        return velocity;
    }

    public GPSPoint setVelocity(Double velocity) {
        this.velocity = velocity;
        return this;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public GPSPoint setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
        return this;
    }

    public String getDriverId() {
        return driverId;
    }

    public GPSPoint setDriverId(String driverId) {
        this.driverId = driverId;
        return this;
    }

    public String getExternalId() {
        return externalId;
    }

    public GPSPoint setExternalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public GPSPoint setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GPSPoint gpsPoint = (GPSPoint) o;

        if (latitude != null ? !latitude.equals(gpsPoint.latitude) : gpsPoint.latitude != null) return false;
        if (longitude != null ? !longitude.equals(gpsPoint.longitude) : gpsPoint.longitude != null) return false;
        if (time != null ? !time.equals(gpsPoint.time) : gpsPoint.time != null) return false;
        if (velocity != null ? !velocity.equals(gpsPoint.velocity) : gpsPoint.velocity != null) return false;
        if (vehicleId != null ? !vehicleId.equals(gpsPoint.vehicleId) : gpsPoint.vehicleId != null) return false;
        if (subscriptionId != null ? !subscriptionId.equals(gpsPoint.subscriptionId) : gpsPoint.subscriptionId != null)
            return false;
        if (driverId != null ? !driverId.equals(gpsPoint.driverId) : gpsPoint.driverId != null) return false;
        return externalId != null ? externalId.equals(gpsPoint.externalId) : gpsPoint.externalId == null;
    }

    @Override
    public int hashCode() {
        int result = latitude != null ? latitude.hashCode() : 0;
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (velocity != null ? velocity.hashCode() : 0);
        result = 31 * result + (vehicleId != null ? vehicleId.hashCode() : 0);
        result = 31 * result + (subscriptionId != null ? subscriptionId.hashCode() : 0);
        result = 31 * result + (driverId != null ? driverId.hashCode() : 0);
        result = 31 * result + (externalId != null ? externalId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GPSPoint{"
                + "latitude=" + latitude
                + ", longitude=" + longitude
                + ", time=" + time
                + ", velocity=" + velocity
                + ", vehicleId='" + vehicleId + '\''
                + ", subscriptionId='" + subscriptionId + '\''
                + ", driverId='" + driverId + '\''
                + ", externalId='" + externalId + '\''
                + '}';
    }
}
