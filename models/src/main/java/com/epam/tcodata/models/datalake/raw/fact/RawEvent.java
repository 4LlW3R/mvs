package com.epam.tcodata.models.datalake.raw.fact;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.raw.RawEntity;

import java.sql.Timestamp;

@SuppressWarnings("CPD-START")
public class RawEvent extends RawEntity {

    private static final long serialVersionUID = -4867337595585409377L;


    public static class Fields {
        public static final String DRIVER_DURABLE_KEY = "driver_durable_key";
        public static final String VEHICLE_DURABLE_KEY = "vehicle_durable_key";
        public static final String TOTAL_OCCURANCES = "total_occurances";
        public static final String TOTAL_TIME_SECONS = "total_time_seconds";
        public static final String EVENT_TYPE_ID = "event_type_id";
        public static final String EVENT_ID = "event_id";
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
        public static final String END_POSITION_LONGITURE = "end_position_longitude";
        public static final String END_POSITION_LATITUDE = "end_position_latitude";
        public static final String END_POSITION_POSITION_ID = "end_position_position_id";
        public static final String END_POSITION_SPEED_KILOMETRES_PER_HOUR = "end_position_speed_kilometres_per_hour";
        public static final String VALUE_TYPE = "value_type";
        public static final String VALUE_UNITS = "value_units";
        public static final String MEDIA_URLS_ROAD = "media_urls_road";
        public static final String MEDIA_URLS_CAB = "media_urls_cab";
        public static final String MEDIA_URLS_CAMERA3 = "media_urls_camera3";
        public static final String MEDIA_URLS_CAMERA4 = "media_urls_camera4";
        public static final String LOCATION_ID = "location_id";
        public static final String SPEED_LIMIT = "speed_limit";
        public static final String YEAR = "year";
        public static final String WEEK_NUMBER = "week_number";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.DRIVER_DURABLE_KEY)
    private String driverDurableKey;
    @ColumnName(Fields.VEHICLE_DURABLE_KEY)
    private String vehicleDurableKey;
    @ColumnName(Fields.TOTAL_OCCURANCES)
    private Long totalOccurances;
    @ColumnName(Fields.TOTAL_TIME_SECONS)
    private Integer totalTimeSeconds;
    @ColumnName(Fields.EVENT_TYPE_ID)
    private Long eventTypeId;
    @ColumnName(Fields.EVENT_ID)
    private Long eventId;
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
    @ColumnName(Fields.END_POSITION_LONGITURE)
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
    @ColumnName(Fields.MEDIA_URLS_ROAD)
    private String mediaUrlsRoad;
    @ColumnName(Fields.MEDIA_URLS_CAB)
    private String mediaUrlsCab;
    @ColumnName(Fields.MEDIA_URLS_CAMERA3)
    private String mediaUrlsCamera3;
    @ColumnName(Fields.MEDIA_URLS_CAMERA4)
    private String mediaUrlsCamera4;
    @ColumnName(Fields.LOCATION_ID)
    private Long locationId;
    @ColumnName(Fields.SPEED_LIMIT)
    private Double speedLimit;
    @ColumnName(Fields.YEAR)
    private Integer year;
    @ColumnName(Fields.WEEK_NUMBER)
    private Integer weekNumber;

    public RawEvent() {
        /***  Default implementation ***/
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

    public Long getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(Long eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
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
        return endDateTime == null ? null : new Timestamp(endDateTime.getTime());
    }

    public void setEndDateTime(Timestamp endDateTime) {
        this.endDateTime = endDateTime == null ? null : new Timestamp(endDateTime.getTime());
    }

    public Timestamp getStartDateTime() {
        return startDateTime == null ? null : new Timestamp(startDateTime.getTime());
    }

    public void setStartDateTime(Timestamp startDateTime) {
        this.startDateTime = startDateTime == null ? null : new Timestamp(startDateTime.getTime());
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
        return startPositionTimestamp == null ? null : new Timestamp(startPositionTimestamp.getTime());
    }

    public void setStartPositionTimestamp(Timestamp startPositionTimestamp) {
        this.startPositionTimestamp = startPositionTimestamp == null ? null : new Timestamp(startPositionTimestamp.getTime());
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
        return endPositionTimestamp == null ? null : new Timestamp(endPositionTimestamp.getTime());
    }

    public void setEndPositionTimestamp(Timestamp endPositionTimestamp) {
        this.endPositionTimestamp = endPositionTimestamp == null ? null : new Timestamp(endPositionTimestamp.getTime());
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

    public String getMediaUrlsRoad() {
        return mediaUrlsRoad;
    }

    public void setMediaUrlsRoad(String mediaUrlsRoad) {
        this.mediaUrlsRoad = mediaUrlsRoad;
    }

    public String getMediaUrlsCab() {
        return mediaUrlsCab;
    }

    public void setMediaUrlsCab(String mediaUrlsCab) {
        this.mediaUrlsCab = mediaUrlsCab;
    }

    public String getMediaUrlsCamera3() {
        return mediaUrlsCamera3;
    }

    public void setMediaUrlsCamera3(String mediaUrlsCamera3) {
        this.mediaUrlsCamera3 = mediaUrlsCamera3;
    }

    public String getMediaUrlsCamera4() {
        return mediaUrlsCamera4;
    }

    public void setMediaUrlsCamera4(String mediaUrlsCamera4) {
        this.mediaUrlsCamera4 = mediaUrlsCamera4;
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
}
