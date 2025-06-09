package com.epam.tcodata.models.datalake.prepared.analytics;


import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.prepared.PreparedEntity;

import java.sql.Timestamp;
import java.util.Objects;

@SuppressWarnings("CPD-START")
public class PreparedValidatedEvent extends PreparedEntity {

    private static final long serialVersionUID = -1163756690266025252L;

    public static class Fields {
        public static final String ORGANIZATION_DURABLE_KEY = "organization_durable_key";
        public static final String EXTERNAL_ID = "external_id";
        public static final String PERSISTED_DATE_UTC = "persisted_date_utc";
        public static final String DRIVER_DURABLE_KEY = "driver_durable_key";
        public static final String VEHICLE_DURABLE_KEY = "vehicle_durable_key";
        public static final String TOTAL_OCCURANCES = "total_occurances";
        public static final String TOTAL_TIME_SECONDS = "total_time_seconds";
        public static final String EVENT_TYPE_DURABLE_KEY = "event_type_durable_key";
        public static final String EVENT_TYPE_ID = "event_type_id";
        public static final String DRIVER_ID = "driver_id";
        public static final String ASSET_ID = "asset_id";
        public static final String VALUE = "value";
        public static final String END_DATE_TIME = "end_date_time";
        public static final String START_DATE_TIME = "start_date_time";
        public static final String EVENT_CATEGORY = "event_category";
        public static final String START_ODOMETER_KILOMETRES = "start_odometer_kilometres";
        public static final String START_POSITION_TIMESTAMP = "start_position_timestamp";
        public static final String START_POSITION_LONGITUDE = "start_position_longitude";
        public static final String START_POSITION_LATITUDE = "start_position_latitude";
        public static final String START_POSITION_POSITION_ID = "start_position_position_id";
        public static final String START_POSITION_SPEED_KILOMETRES_PER_HOUR = "start_position_speed_kilometres_per_hour";
        public static final String END_ODOMETER_KILOMETRES = "end_odometer_kilometres";
        public static final String END_POSITION_TIMESTAMP = "end_position_timestamp";
        public static final String END_POSITION_LONGITUDE = "end_position_longitude";
        public static final String END_POSITION_LATITUDE = "end_position_latitude";
        public static final String END_POSITION_POSITION_ID = "end_position_position_id";
        public static final String END_POSITION_SPEED_KILOMETRES_PER_HOUR = "end_position_speed_kilometres_per_hour";
        public static final String VALUE_TYPE = "value_type";
        public static final String VALUE_UNITS = "value_units";
        public static final String LOCATION_ID = "location_id";
        public static final String SPEED_LIMIT = "speed_limit";
        public static final String VALIDATION_CODE = "validation_code";
        public static final String PROBLEM_VEHICLE = "problem_vehicle";
        public static final String YEAR = "year";
        public static final String WEEK_NUMBER = "week_number";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.ORGANIZATION_DURABLE_KEY)
    private String organizationDurableKey;
    @ColumnName(Fields.EXTERNAL_ID)
    private Long externalId;
    @ColumnName(Fields.PERSISTED_DATE_UTC)
    private Timestamp persistedDateUtc;
    @ColumnName(Fields.DRIVER_DURABLE_KEY)
    private String driverDurableKey;
    @ColumnName(Fields.VEHICLE_DURABLE_KEY)
    private String vehicleDurableKey;
    @ColumnName(Fields.TOTAL_OCCURANCES)
    private Long totalOccurances;
    @ColumnName(Fields.TOTAL_TIME_SECONDS)
    private Integer totalTimeSeconds;
    @ColumnName(Fields.EVENT_TYPE_DURABLE_KEY)
    private String eventTypeDurableKey;
    @ColumnName(Fields.EVENT_TYPE_ID)
    private Long eventTypeId;
    @ColumnName(Fields.DRIVER_ID)
    private Long driverId;
    @ColumnName(Fields.ASSET_ID)
    private Long assetId;
    @ColumnName(Fields.VALUE)
    private Double value;
    @ColumnName(Fields.END_DATE_TIME)
    private Timestamp endDateTime;
    @ColumnName(Fields.START_DATE_TIME)
    private Timestamp startDateTime;
    @ColumnName(Fields.EVENT_CATEGORY)
    private String eventCategory;
    @ColumnName(Fields.START_ODOMETER_KILOMETRES)
    private Double startOdometerKilometres;
    @ColumnName(Fields.START_POSITION_TIMESTAMP)
    private Timestamp startPositionTimestamp;
    @ColumnName(Fields.START_POSITION_LONGITUDE)
    private Double startPositionLongitude;
    @ColumnName(Fields.START_POSITION_LATITUDE)
    private Double startPositionLatitude;
    @ColumnName(Fields.START_POSITION_POSITION_ID)
    private Long startPositionPositionId;
    @ColumnName(Fields.START_POSITION_SPEED_KILOMETRES_PER_HOUR)
    private Double startPositionSpeedKilometresPerHour;
    @ColumnName(Fields.END_ODOMETER_KILOMETRES)
    private Double endOdometerKilometres;
    @ColumnName(Fields.END_POSITION_TIMESTAMP)
    private Timestamp endPositionTimestamp;
    @ColumnName(Fields.END_POSITION_LONGITUDE)
    private Double endPositionLongitude;
    @ColumnName(Fields.END_POSITION_LATITUDE)
    private Double endPositionLatitude;
    @ColumnName(Fields.END_POSITION_POSITION_ID)
    private Long endPositionPositionId;
    @ColumnName(Fields.END_POSITION_SPEED_KILOMETRES_PER_HOUR)
    private Double endPositionSpeedKilometresPerHour;
    @ColumnName(Fields.VALUE_TYPE)
    private String valueType;
    @ColumnName(Fields.VALUE_UNITS)
    private String valueUnits;
    @ColumnName(Fields.LOCATION_ID)
    private Long locationId;
    @ColumnName(Fields.SPEED_LIMIT)
    private Double speedLimit;
    @ColumnName(Fields.VALIDATION_CODE)
    private Integer validationCode;
    @ColumnName(Fields.PROBLEM_VEHICLE)
    private Integer problemVehicle;
    @ColumnName(Fields.YEAR)
    private Integer year;
    @ColumnName(Fields.WEEK_NUMBER)
    private Integer weekNumber;

    /**
     * Create PreparedValidatedEvent.
     */
    public PreparedValidatedEvent() {
        /***  Default implementation ***/
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getOrganizationDurableKey() {
        return organizationDurableKey;
    }

    public void setOrganizationDurableKey(String organizationDurableKey) {
        this.organizationDurableKey = organizationDurableKey;
    }

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    public Timestamp getPersistedDateUtc() {
        return persistedDateUtc;
    }

    public void setPersistedDateUtc(Timestamp persistedDateUtc) {
        this.persistedDateUtc = persistedDateUtc;
    }

    public String getDriverDurableKey() {
        return driverDurableKey;
    }

    public void setDriverDurableKey(String driverDurableKey) {
        this.driverDurableKey = driverDurableKey;
    }

    public String getVehicleDurableKey() {
        return vehicleDurableKey;
    }

    public void setVehicleDurableKey(String vehicleDurableKey) {
        this.vehicleDurableKey = vehicleDurableKey;
    }

    public Long getTotalOccurances() {
        return totalOccurances;
    }

    public void setTotalOccurances(Long totalOccurances) {
        this.totalOccurances = totalOccurances;
    }

    public Integer getTotalTimeSeconds() {
        return totalTimeSeconds;
    }

    public void setTotalTimeSeconds(Integer totalTimeSeconds) {
        this.totalTimeSeconds = totalTimeSeconds;
    }

    public String getEventTypeDurableKey() {
        return eventTypeDurableKey;
    }

    public void setEventTypeDurableKey(String eventTypeDurableKey) {
        this.eventTypeDurableKey = eventTypeDurableKey;
    }

    public Long getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(Long eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Timestamp getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Timestamp endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Timestamp getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Timestamp startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public Double getStartOdometerKilometres() {
        return startOdometerKilometres;
    }

    public void setStartOdometerKilometres(Double startOdometerKilometres) {
        this.startOdometerKilometres = startOdometerKilometres;
    }

    public Timestamp getStartPositionTimestamp() {
        return startPositionTimestamp;
    }

    public void setStartPositionTimestamp(Timestamp startPositionTimestamp) {
        this.startPositionTimestamp = startPositionTimestamp;
    }

    public Double getStartPositionLongitude() {
        return startPositionLongitude;
    }

    public void setStartPositionLongitude(Double startPositionLongitude) {
        this.startPositionLongitude = startPositionLongitude;
    }

    public Double getStartPositionLatitude() {
        return startPositionLatitude;
    }

    public void setStartPositionLatitude(Double startPositionLatitude) {
        this.startPositionLatitude = startPositionLatitude;
    }

    public Long getStartPositionPositionId() {
        return startPositionPositionId;
    }

    public void setStartPositionPositionId(Long startPositionPositionId) {
        this.startPositionPositionId = startPositionPositionId;
    }

    public Double getStartPositionSpeedKilometresPerHour() {
        return startPositionSpeedKilometresPerHour;
    }

    public void setStartPositionSpeedKilometresPerHour(Double startPositionSpeedKilometresPerHour) {
        this.startPositionSpeedKilometresPerHour = startPositionSpeedKilometresPerHour;
    }

    public Double getEndOdometerKilometres() {
        return endOdometerKilometres;
    }

    public void setEndOdometerKilometres(Double endOdometerKilometres) {
        this.endOdometerKilometres = endOdometerKilometres;
    }

    public Timestamp getEndPositionTimestamp() {
        return endPositionTimestamp;
    }

    public void setEndPositionTimestamp(Timestamp endPositionTimestamp) {
        this.endPositionTimestamp = endPositionTimestamp;
    }

    public Double getEndPositionLongitude() {
        return endPositionLongitude;
    }

    public void setEndPositionLongitude(Double endPositionLongitude) {
        this.endPositionLongitude = endPositionLongitude;
    }

    public Double getEndPositionLatitude() {
        return endPositionLatitude;
    }

    public void setEndPositionLatitude(Double endPositionLatitude) {
        this.endPositionLatitude = endPositionLatitude;
    }

    public Long getEndPositionPositionId() {
        return endPositionPositionId;
    }

    public void setEndPositionPositionId(Long endPositionPositionId) {
        this.endPositionPositionId = endPositionPositionId;
    }

    public Double getEndPositionSpeedKilometresPerHour() {
        return endPositionSpeedKilometresPerHour;
    }

    public void setEndPositionSpeedKilometresPerHour(Double endPositionSpeedKilometresPerHour) {
        this.endPositionSpeedKilometresPerHour = endPositionSpeedKilometresPerHour;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getValueUnits() {
        return valueUnits;
    }

    public void setValueUnits(String valueUnits) {
        this.valueUnits = valueUnits;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Double getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(Double speedLimit) {
        this.speedLimit = speedLimit;
    }

    public Integer getValidationCode() {
        return validationCode;
    }

    public void setValidationCode(Integer validationCode) {
        this.validationCode = validationCode;
    }

    public Integer getProblemVehicle() {
        return problemVehicle;
    }

    public void setProblemVehicle(Integer problemVehicle) {
        this.problemVehicle = problemVehicle;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PreparedValidatedEvent that = (PreparedValidatedEvent) o;
        return Objects.equals(organizationDurableKey, that.organizationDurableKey)
                && Objects.equals(externalId, that.externalId)
                && Objects.equals(persistedDateUtc, that.persistedDateUtc)
                && Objects.equals(driverDurableKey, that.driverDurableKey)
                && Objects.equals(vehicleDurableKey, that.vehicleDurableKey)
                && Objects.equals(totalOccurances, that.totalOccurances)
                && Objects.equals(totalTimeSeconds, that.totalTimeSeconds)
                && Objects.equals(eventTypeDurableKey, that.eventTypeDurableKey)
                && Objects.equals(eventTypeId, that.eventTypeId)
                && Objects.equals(driverId, that.driverId)
                && Objects.equals(assetId, that.assetId)
                && Objects.equals(value, that.value)
                && Objects.equals(endDateTime, that.endDateTime)
                && Objects.equals(startDateTime, that.startDateTime)
                && Objects.equals(eventCategory, that.eventCategory)
                && Objects.equals(startOdometerKilometres, that.startOdometerKilometres)
                && Objects.equals(startPositionTimestamp, that.startPositionTimestamp)
                && Objects.equals(startPositionLongitude, that.startPositionLongitude)
                && Objects.equals(startPositionLatitude, that.startPositionLatitude)
                && Objects.equals(startPositionPositionId, that.startPositionPositionId)
                && Objects.equals(startPositionSpeedKilometresPerHour, that.startPositionSpeedKilometresPerHour)
                && Objects.equals(endOdometerKilometres, that.endOdometerKilometres)
                && Objects.equals(endPositionTimestamp, that.endPositionTimestamp)
                && Objects.equals(endPositionLongitude, that.endPositionLongitude)
                && Objects.equals(endPositionLatitude, that.endPositionLatitude)
                && Objects.equals(endPositionPositionId, that.endPositionPositionId)
                && Objects.equals(endPositionSpeedKilometresPerHour, that.endPositionSpeedKilometresPerHour)
                && Objects.equals(valueType, that.valueType)
                && Objects.equals(valueUnits, that.valueUnits)
                && Objects.equals(locationId, that.locationId)
                && Objects.equals(speedLimit, that.speedLimit)
                && Objects.equals(validationCode, that.validationCode)
                && Objects.equals(problemVehicle, that.problemVehicle)
                && Objects.equals(year, that.year)
                && Objects.equals(weekNumber, that.weekNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organizationDurableKey, externalId, persistedDateUtc, driverDurableKey, vehicleDurableKey, totalOccurances, totalTimeSeconds, eventTypeDurableKey, eventTypeId, driverId, assetId, value, endDateTime, startDateTime, eventCategory, startOdometerKilometres, startPositionTimestamp, startPositionLongitude, startPositionLatitude, startPositionPositionId, startPositionSpeedKilometresPerHour, endOdometerKilometres, endPositionTimestamp, endPositionLongitude, endPositionLatitude, endPositionPositionId, endPositionSpeedKilometresPerHour, valueType, valueUnits, locationId, speedLimit, validationCode, problemVehicle, year, weekNumber);
    }
}
