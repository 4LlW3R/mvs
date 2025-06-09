package com.epam.tcodata.analytics.road.condition.violation.detection.domain;

import java.io.Serializable;
import java.sql.Timestamp;

public class GPSPointWithArea implements Serializable {
    public static final String DEFAULT_AREA_TYPE = "NO_RESTRICTIONS";
    private static final String DEFAULT_AREA_ID = "NO_RESTRICTIONS";
    private static final long serialVersionUID = 4502224805907814699L;

    private Double latitude;
    private Double longitude;
    private Timestamp time;
    private Double velocity;
    private String vehicleId;
    private Long subscriptionId;
    private String driverId;
    private String areaId;
    private String areaType;
    private String externalId;

    public GPSPointWithArea() {
    }

    /**
     * Simple copy constructor with default area.
     */
    public GPSPointWithArea(GPSPoint point) {
        this(point, DEFAULT_AREA_ID, DEFAULT_AREA_TYPE);
    }

    /**
     * Mapping constructor from {@link GPSPoint} with specified area type.
     */
    public GPSPointWithArea(GPSPoint point, String areaId, String areaType) {
        this.latitude = point.getLatitude();
        this.longitude = point.getLongitude();
        this.time = new Timestamp(point.getTime().getTime());
        this.velocity = point.getVelocity();
        this.vehicleId = point.getVehicleId();
        this.subscriptionId = point.getSubscriptionId();
        this.driverId = point.getDriverId();
        this.externalId = point.getExternalId();
        this.areaId = areaId;
        this.areaType = areaType;
    }

    //region Getters and setters
    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public Timestamp getTime() {
        return time == null ? null : new Timestamp(time.getTime());
    }

    public void setTime(Timestamp time) {
        this.time = time == null ? null : new Timestamp(time.getTime());
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

    public Double getVelocity() {
        return velocity;
    }

    public void setVelocity(Double velocity) {
        this.velocity = velocity;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
    //endregion

    @Override
    public String toString() {
        return "GPSPointWithArea{"
                + "latitude=" + latitude
                + ", longitude=" + longitude
                + ", time=" + time
                + ", velocity=" + velocity
                + ", vehicleId='" + vehicleId + '\''
                + ", subscriptionId=" + subscriptionId
                + ", driverId='" + driverId + '\''
                + ", areaId='" + areaId + '\''
                + ", areaType='" + areaType + '\''
                + ", externalId='" + externalId + '\''
                + '}';
    }
}
