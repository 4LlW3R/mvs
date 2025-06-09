package com.epam.tcodata.models.datalake.prepared.analytics;


import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.AbstractDataLakeEntity;

import java.sql.Timestamp;


public class PreparedRoadConditionViolation extends AbstractDataLakeEntity {

    private static final long serialVersionUID = -3140649338165028076L;


    public static class Fields {
        public static final String DOMAIN = "domain";
        public static final String ENTITY_TYPE = "entity_type";
        public static final String SCHEMA_VERSION = "schema_version";
        public static final String ID = "id";
        public static final String SUBSCRIPTION_ID = "subscription_id";
        public static final String VEHICLE_ID = "vehicle_id";
        public static final String DRIVER_ID = "driver_id";
        public static final String START_TIME = "start_time";
        public static final String END_TIME = "end_time";
        public static final String AVERAGE_SPEED = "average_speed";
        public static final String MAX_SPEED = "max_speed";
        public static final String ROAD_CONDITION_ID = "road_condition_id";
        public static final String CREATION_TIME = "creation_time";
        public static final String START_FACT_GPS_ID = "start_fact_gps_id";
        public static final String END_FACT_GPS_ID = "end_fact_gps_id";
        public static final String START_LATITUDE = "start_latitude";
        public static final String START_LONGITUDE = "start_longitude";
        public static final String END_LATITUDE = "end_latitude";
        public static final String END_LONGITUDE = "end_longitude";
        private Fields(){   /***  Default implementation ***/  }
    }


    @ColumnName(Fields.DOMAIN)
    private String domain;
    @ColumnName(Fields.ENTITY_TYPE)
    private Integer entityType;
    @ColumnName(Fields.SCHEMA_VERSION)
    private Integer schemaVersion;
    @ColumnName(Fields.ID)
    private String id;
    @ColumnName(Fields.SUBSCRIPTION_ID)
    private Long subscriptionId;
    @ColumnName(Fields.VEHICLE_ID)
    private String vehicleId;
    @ColumnName(Fields.DRIVER_ID)
    private String driverId;
    @ColumnName(Fields.START_TIME)
    private Timestamp startTime;
    @ColumnName(Fields.END_TIME)
    private Timestamp endTime;
    @ColumnName(Fields.AVERAGE_SPEED)
    private Double averageSpeed;
    @ColumnName(Fields.MAX_SPEED)
    private Double maxSpeed;
    @ColumnName(Fields.ROAD_CONDITION_ID)
    private String roadConditionId;
    @ColumnName(Fields.CREATION_TIME)
    private Timestamp creationTime;
    @ColumnName(Fields.START_FACT_GPS_ID)
    private String startFactGpsId;
    @ColumnName(Fields.END_FACT_GPS_ID)
    private String endFactGpsId;
    @ColumnName(Fields.START_LATITUDE)
    private Double startLatitude;
    @ColumnName(Fields.START_LONGITUDE)
    private Double startLongitude;
    @ColumnName(Fields.END_LATITUDE)
    private Double endLatitude;
    @ColumnName(Fields.END_LONGITUDE)
    private Double endLongitude;

    public PreparedRoadConditionViolation() {
        /***  Default implementation ***/
    }

    /**
     * Method provides structed fields for writing to datalake.
     *
     * @return Object[] fields
     */
    public Object[] getOrderedValues() {
        return new Object[]{
                domain,
                entityType,
                schemaVersion,
                id,
                subscriptionId,
                vehicleId,
                driverId,
                startTime,
                endTime,
                averageSpeed,
                maxSpeed,
                roadConditionId,
                creationTime,
                startFactGpsId,
                endFactGpsId,
                startLatitude,
                startLongitude,
                endLatitude,
                endLongitude
        };
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Integer getEntityType() {
        return entityType;
    }

    public void setEntityType(Integer entityType) {
        this.entityType = entityType;
    }

    public Integer getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(Integer schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public Timestamp getStartTime() {
        return (startTime == null) ? null : new Timestamp(startTime.getTime());
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = (startTime == null) ? null : new Timestamp(startTime.getTime());
    }

    public Timestamp getEndTime() {
        return (endTime == null) ? null : new Timestamp(endTime.getTime());
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = (endTime == null) ? null : new Timestamp(endTime.getTime());
    }

    public Double getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(Double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public Double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getRoadConditionId() {
        return roadConditionId;
    }

    public void setRoadConditionId(String roadConditionId) {
        this.roadConditionId = roadConditionId;
    }

    public Timestamp getCreationTime() {
        return (creationTime == null) ? null : new Timestamp(creationTime.getTime());
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = (creationTime == null) ? null : new Timestamp(creationTime.getTime());
    }

    public String getStartFactGpsId() {
        return startFactGpsId;
    }

    public void setStartFactGpsId(String startFactGpsId) {
        this.startFactGpsId = startFactGpsId;
    }

    public String getEndFactGpsId() {
        return endFactGpsId;
    }

    public void setEndFactGpsId(String endFactGpsId) {
        this.endFactGpsId = endFactGpsId;
    }

    @SuppressWarnings("CPD-START")
    public Double getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(Double startLatitude) {
        this.startLatitude = startLatitude;
    }

    public Double getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(Double startLongitude) {
        this.startLongitude = startLongitude;
    }

    public Double getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(Double endLatitude) {
        this.endLatitude = endLatitude;
    }

    public Double getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(Double endLongitude) {
        this.endLongitude = endLongitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PreparedRoadConditionViolation that = (PreparedRoadConditionViolation) o;

        if (domain != null ? !domain.equals(that.domain) : that.domain != null) return false;
        if (entityType != null ? !entityType.equals(that.entityType) : that.entityType != null) return false;
        if (schemaVersion != null ? !schemaVersion.equals(that.schemaVersion) : that.schemaVersion != null)
            return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (subscriptionId != null ? !subscriptionId.equals(that.subscriptionId) : that.subscriptionId != null)
            return false;
        if (vehicleId != null ? !vehicleId.equals(that.vehicleId) : that.vehicleId != null) return false;
        if (driverId != null ? !driverId.equals(that.driverId) : that.driverId != null) return false;
        if (startTime != null ? !startTime.equals(that.startTime) : that.startTime != null) return false;
        if (endTime != null ? !endTime.equals(that.endTime) : that.endTime != null) return false;
        if (averageSpeed != null ? !averageSpeed.equals(that.averageSpeed) : that.averageSpeed != null) return false;
        if (maxSpeed != null ? !maxSpeed.equals(that.maxSpeed) : that.maxSpeed != null) return false;
        if (roadConditionId != null ? !roadConditionId.equals(that.roadConditionId) : that.roadConditionId != null)
            return false;
        if (creationTime != null ? !creationTime.equals(that.creationTime) : that.creationTime != null) return false;
        if (startFactGpsId != null ? !startFactGpsId.equals(that.startFactGpsId) : that.startFactGpsId != null)
            return false;
        if (endFactGpsId != null ? !endFactGpsId.equals(that.endFactGpsId) : that.endFactGpsId != null) return false;
        if (startLatitude != null ? !startLatitude.equals(that.startLatitude) : that.startLatitude != null)
            return false;
        if (startLongitude != null ? !startLongitude.equals(that.startLongitude) : that.startLongitude != null)
            return false;
        if (endLatitude != null ? !endLatitude.equals(that.endLatitude) : that.endLatitude != null) return false;
        return endLongitude != null ? endLongitude.equals(that.endLongitude) : that.endLongitude == null;
    }

    @Override
    public int hashCode() {
        int result = domain != null ? domain.hashCode() : 0;
        result = 31 * result + (entityType != null ? entityType.hashCode() : 0);
        result = 31 * result + (schemaVersion != null ? schemaVersion.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (subscriptionId != null ? subscriptionId.hashCode() : 0);
        result = 31 * result + (vehicleId != null ? vehicleId.hashCode() : 0);
        result = 31 * result + (driverId != null ? driverId.hashCode() : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + (averageSpeed != null ? averageSpeed.hashCode() : 0);
        result = 31 * result + (maxSpeed != null ? maxSpeed.hashCode() : 0);
        result = 31 * result + (roadConditionId != null ? roadConditionId.hashCode() : 0);
        result = 31 * result + (creationTime != null ? creationTime.hashCode() : 0);
        result = 31 * result + (startFactGpsId != null ? startFactGpsId.hashCode() : 0);
        result = 31 * result + (endFactGpsId != null ? endFactGpsId.hashCode() : 0);
        result = 31 * result + (startLatitude != null ? startLatitude.hashCode() : 0);
        result = 31 * result + (startLongitude != null ? startLongitude.hashCode() : 0);
        result = 31 * result + (endLatitude != null ? endLatitude.hashCode() : 0);
        result = 31 * result + (endLongitude != null ? endLongitude.hashCode() : 0);
        return result;
    }

    @SuppressWarnings("CPD-END")


    @Override
    public String toString() {
        return "PreparedRoadConditionViolation{"
                + "domain='" + domain + '\''
                + ", entityType=" + entityType
                + ", schemaVersion=" + schemaVersion
                + ", id='" + id + '\''
                + ", subscriptionId=" + subscriptionId
                + ", vehicleId=" + vehicleId
                + ", driverId=" + driverId
                + ", startTime=" + startTime
                + ", endTime=" + endTime
                + ", averageSpeed=" + averageSpeed
                + ", maxSpeed=" + maxSpeed
                + ", roadConditionId='" + roadConditionId + '\''
                + ", creationTime=" + creationTime
                + ", startFactGpsId='" + startFactGpsId + '\''
                + ", endFactGpsId='" + endFactGpsId + '\''
                + ", startLatitude=" + startLatitude
                + ", startLongitude=" + startLongitude
                + ", endLatitude=" + endLatitude
                + ", endLongitude=" + endLongitude
                + '}';
    }
}
