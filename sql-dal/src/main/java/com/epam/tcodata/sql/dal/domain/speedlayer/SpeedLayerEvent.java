package com.epam.tcodata.sql.dal.domain.speedlayer;

import com.epam.tcodata.sql.dal.util.SqlCommon;
import com.google.common.base.Objects;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.sql.Timestamp;

public class SpeedLayerEvent implements ISpeedLayerEntity {

    public static class Fields {
        public static final String OBSERVED_DAY = "ObservedDay";
        public static final String DURABLE_ID = "DurableId";
        public static final String INGESTED_DATE_UTC = "IngestedDateUtc";
        public static final String SUBSCRIPTION_ID = "SubscriptionId";
        public static final String LINEAGE_CODE = "LineageCode";
        public static final String PERSISTED_DATE_UTC = "PersistedDateUtc";
        public static final String DRIVER_DURABLE_KEY = "DriverDurableKey";
        public static final String VEHICLE_DURABLE_KEY = "VehicleDurableKey";
        public static final String ASSER_ID = "AssetId";
        public static final String DRIVER_ID = "DriverId";
        public static final String EVENT_ID = "EventId";
        public static final String EVENT_TYPE_ID = "EventTypeId";
        public static final String EVENT_CATEGORY = "EventCategory";
        public static final String START_DATE_TIME = "StartDateTime";
        public static final String START_ODOMETER_KILOMETRES = "StartOdometerKilometres";
        public static final String START_POSITION_TIMESTAMP = "StartPositionTimestamp";
        public static final String START_POSITION_LONGITUDE = "StartPositionLongitude";
        public static final String START_POSITION_LATITUDE = "StartPositionLatitude";
        public static final String START_POSITION_POSITION_ID = "StartPositionPositionId";
        public static final String START_POSITION_SPEED_KILOMETRES_PER_HOUR = "StartPositionSpeedKilometresPerHour";
        public static final String END_DATE_TIME = "EndDateTime";
        public static final String END_ODOMETER_KILOMETRES = "EndOdometerKilometres";
        public static final String END_POSITION_TIMESTAMP = "EndPositionTimestamp";
        public static final String END_POSITION_LONGITUDE = "EndPositionLongitude";
        public static final String END_POSITION_LATITUDE = "EndPositionLatitude";
        public static final String END_POSITION_POSITION_ID = "EndPositionPositionId";
        public static final String END_POSITION_SPEED_KILOMETRES_PER_HOUR = "EndPositionSpeedKilometresPerHour";
        public static final String VALUE = "Value";
        public static final String VALUE_TYPE = "ValueType";
        public static final String VALUE_UNITS = "ValueUnits";
        public static final String TOTAL_TIME_SECONDS = "TotalTimeSeconds";
        public static final String TOTAL_OCCURRENCES = "TotalOccurrences";
        public static final String MEDIA_URLS_ROAD = "MediaUrlsRoad";
        public static final String MEDIA_URLS_CAB = "MediaUrlsCab";
        public static final String MEDIA_URLS_CAMERA3 = "MediaUrlsCamera3";
        public static final String MEDIA_URLS_CAMERA4 = "MediaUrlsCamera4";
        public static final String LOCATION_ID = "LocationId";
        public static final String SPEED_LIMIT = "SpeedLimit";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.OBSERVED_DAY)
    private Integer observedDay;

    @ColumnName(Fields.DURABLE_ID)
    private String durableId;

    @ColumnName(Fields.INGESTED_DATE_UTC)
    private Timestamp ingestedDateUtc;

    @ColumnName(Fields.SUBSCRIPTION_ID)
    private Long subscriptionId;

    @ColumnName(Fields.LINEAGE_CODE)
    private Integer lineageCode;

    @ColumnName(Fields.PERSISTED_DATE_UTC)
    private Timestamp persistedDateUtc;

    @ColumnName(Fields.DRIVER_DURABLE_KEY)
    private String driverDurableKey;

    @ColumnName(Fields.VEHICLE_DURABLE_KEY)
    private String vehicleDurableKey;

    @ColumnName(Fields.ASSER_ID)
    private Long assetId;

    @ColumnName(Fields.DRIVER_ID)
    private Long driverId;

    @ColumnName(Fields.EVENT_ID)
    private Long eventId;

    @ColumnName(Fields.EVENT_TYPE_ID)
    private Long eventTypeId;

    @ColumnName(Fields.EVENT_CATEGORY)
    private String eventCategory;

    @ColumnName(Fields.START_DATE_TIME)
    private Timestamp startDateTime;

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

    @ColumnName(Fields.END_DATE_TIME)
    private Timestamp endDateTime;

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

    @ColumnName(Fields.VALUE)
    private Double value;

    @ColumnName(Fields.VALUE_TYPE)
    private String valueType;

    @ColumnName(Fields.VALUE_UNITS)
    private String valueUnits;

    @ColumnName(Fields.TOTAL_TIME_SECONDS)
    private Integer totalTimeSeconds;

    @ColumnName(Fields.TOTAL_OCCURRENCES)
    private Long totalOccurrences;

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

    @Override
    public long getId() {
        return -1;
    }

    @Override
    public void setId(long id) {
        /***  Default implementation ***/
    }

    public Integer getObservedDay() {
        return observedDay;
    }

    public SpeedLayerEvent setObservedDay(Integer observedDay) {
        this.observedDay = observedDay;
        return this;
    }

    @Override
    public String getDurableId() {
        return durableId;
    }

    public SpeedLayerEvent setDurableId(String durableId) {
        this.durableId = durableId;
        return this;
    }

    public Timestamp getIngestedDateUtc() {
        return SqlCommon.clone(ingestedDateUtc);
    }

    public SpeedLayerEvent setIngestedDateUtc(Timestamp ingestedDateUtc) {
        this.ingestedDateUtc = SqlCommon.clone(ingestedDateUtc);
        return this;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public SpeedLayerEvent setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
        return this;
    }

    public Integer getLineageCode() {
        return lineageCode;
    }

    public SpeedLayerEvent setLineageCode(Integer lineageCode) {
        this.lineageCode = lineageCode;
        return this;
    }

    public Timestamp getPersistedDateUtc() {
        return SqlCommon.clone(persistedDateUtc);
    }

    public SpeedLayerEvent setPersistedDateUtc(Timestamp persistedDateUtc) {
        this.persistedDateUtc = SqlCommon.clone(persistedDateUtc);
        return this;
    }

    public String getDriverDurableKey() {
        return driverDurableKey;
    }

    public SpeedLayerEvent setDriverDurableKey(String driverDurableKey) {
        this.driverDurableKey = driverDurableKey;
        return this;
    }

    public String getVehicleDurableKey() {
        return vehicleDurableKey;
    }

    public SpeedLayerEvent setVehicleDurableKey(String vehicleDurableKey) {
        this.vehicleDurableKey = vehicleDurableKey;
        return this;
    }

    public Long getAssetId() {
        return assetId;
    }

    public SpeedLayerEvent setAssetId(Long assetId) {
        this.assetId = assetId;
        return this;
    }

    public Long getDriverId() {
        return driverId;
    }

    public SpeedLayerEvent setDriverId(Long driverId) {
        this.driverId = driverId;
        return this;
    }

    public Long getEventId() {
        return eventId;
    }

    public SpeedLayerEvent setEventId(Long eventId) {
        this.eventId = eventId;
        return this;
    }

    public Long getEventTypeId() {
        return eventTypeId;
    }

    public SpeedLayerEvent setEventTypeId(Long eventTypeId) {
        this.eventTypeId = eventTypeId;
        return this;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public SpeedLayerEvent setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
        return this;
    }

    public Timestamp getStartDateTime() {
        return SqlCommon.clone(startDateTime);
    }

    public SpeedLayerEvent setStartDateTime(Timestamp startDateTime) {
        this.startDateTime = SqlCommon.clone(startDateTime);
        return this;
    }

    public Double getStartOdometerKilometres() {
        return startOdometerKilometres;
    }

    public SpeedLayerEvent setStartOdometerKilometres(Double startOdometerKilometres) {
        this.startOdometerKilometres = startOdometerKilometres;
        return this;
    }

    public Timestamp getStartPositionTimestamp() {
        return SqlCommon.clone(startPositionTimestamp);
    }

    public SpeedLayerEvent setStartPositionTimestamp(Timestamp startPositionTimestamp) {
        this.startPositionTimestamp = SqlCommon.clone(startPositionTimestamp);
        return this;
    }

    public Double getStartPositionLongitude() {
        return startPositionLongitude;
    }

    public SpeedLayerEvent setStartPositionLongitude(Double startPositionLongitude) {
        this.startPositionLongitude = startPositionLongitude;
        return this;
    }

    public Double getStartPositionLatitude() {
        return startPositionLatitude;
    }

    public SpeedLayerEvent setStartPositionLatitude(Double startPositionLatitude) {
        this.startPositionLatitude = startPositionLatitude;
        return this;
    }

    public Long getStartPositionPositionId() {
        return startPositionPositionId;
    }

    public SpeedLayerEvent setStartPositionPositionId(Long startPositionPositionId) {
        this.startPositionPositionId = startPositionPositionId;
        return this;
    }

    public Double getStartPositionSpeedKilometresPerHour() {
        return startPositionSpeedKilometresPerHour;
    }

    public SpeedLayerEvent setStartPositionSpeedKilometresPerHour(Double startPositionSpeedKilometresPerHour) {
        this.startPositionSpeedKilometresPerHour = startPositionSpeedKilometresPerHour;
        return this;
    }

    public Timestamp getEndDateTime() {
        return SqlCommon.clone(endDateTime);
    }

    public SpeedLayerEvent setEndDateTime(Timestamp endDateTime) {
        this.endDateTime = SqlCommon.clone(endDateTime);
        return this;
    }

    public Double getEndOdometerKilometres() {
        return endOdometerKilometres;
    }

    public SpeedLayerEvent setEndOdometerKilometres(Double endOdometerKilometres) {
        this.endOdometerKilometres = endOdometerKilometres;
        return this;
    }

    public Timestamp getEndPositionTimestamp() {
        return SqlCommon.clone(endPositionTimestamp);
    }

    public SpeedLayerEvent setEndPositionTimestamp(Timestamp endPositionTimestamp) {
        this.endPositionTimestamp = SqlCommon.clone(endPositionTimestamp);
        return this;
    }

    public Double getEndPositionLongitude() {
        return endPositionLongitude;
    }

    public SpeedLayerEvent setEndPositionLongitude(Double endPositionLongitude) {
        this.endPositionLongitude = endPositionLongitude;
        return this;
    }

    public Double getEndPositionLatitude() {
        return endPositionLatitude;
    }

    public SpeedLayerEvent setEndPositionLatitude(Double endPositionLatitude) {
        this.endPositionLatitude = endPositionLatitude;
        return this;
    }

    public Long getEndPositionPositionId() {
        return endPositionPositionId;
    }

    public SpeedLayerEvent setEndPositionPositionId(Long endPositionPositionId) {
        this.endPositionPositionId = endPositionPositionId;
        return this;
    }

    public Double getEndPositionSpeedKilometresPerHour() {
        return endPositionSpeedKilometresPerHour;
    }

    public SpeedLayerEvent setEndPositionSpeedKilometresPerHour(Double endPositionSpeedKilometresPerHour) {
        this.endPositionSpeedKilometresPerHour = endPositionSpeedKilometresPerHour;
        return this;
    }

    public Double getValue() {
        return value;
    }

    public SpeedLayerEvent setValue(Double value) {
        this.value = value;
        return this;
    }

    public String getValueType() {
        return valueType;
    }

    public SpeedLayerEvent setValueType(String valueType) {
        this.valueType = valueType;
        return this;
    }

    public String getValueUnits() {
        return valueUnits;
    }

    public SpeedLayerEvent setValueUnits(String valueUnits) {
        this.valueUnits = valueUnits;
        return this;
    }

    public Integer getTotalTimeSeconds() {
        return totalTimeSeconds;
    }

    public SpeedLayerEvent setTotalTimeSeconds(Integer totalTimeSeconds) {
        this.totalTimeSeconds = totalTimeSeconds;
        return this;
    }

    public Long getTotalOccurrences() {
        return totalOccurrences;
    }

    public SpeedLayerEvent setTotalOccurrences(Long totalOccurrences) {
        this.totalOccurrences = totalOccurrences;
        return this;
    }

    public String getMediaUrlsRoad() {
        return mediaUrlsRoad;
    }

    public SpeedLayerEvent setMediaUrlsRoad(String mediaUrlsRoad) {
        this.mediaUrlsRoad = mediaUrlsRoad;
        return this;
    }

    public String getMediaUrlsCab() {
        return mediaUrlsCab;
    }

    public SpeedLayerEvent setMediaUrlsCab(String mediaUrlsCab) {
        this.mediaUrlsCab = mediaUrlsCab;
        return this;
    }

    public String getMediaUrlsCamera3() {
        return mediaUrlsCamera3;
    }

    public SpeedLayerEvent setMediaUrlsCamera3(String mediaUrlsCamera3) {
        this.mediaUrlsCamera3 = mediaUrlsCamera3;
        return this;
    }

    public String getMediaUrlsCamera4() {
        return mediaUrlsCamera4;
    }

    public SpeedLayerEvent setMediaUrlsCamera4(String mediaUrlsCamera4) {
        this.mediaUrlsCamera4 = mediaUrlsCamera4;
        return this;
    }

    public Long getLocationId() {
        return locationId;
    }

    public SpeedLayerEvent setLocationId(Long locationId) {
        this.locationId = locationId;
        return this;
    }

    public Double getSpeedLimit() {
        return speedLimit;
    }

    public SpeedLayerEvent setSpeedLimit(Double speedLimit) {
        this.speedLimit = speedLimit;
        return this;
    }

    @Override
    public String toString() {
        return "SpeedLayerEvent{"
                + "observedDay=" + observedDay
                + ", durableId='" + durableId + '\''
                + ", ingestedDateUtc=" + ingestedDateUtc
                + ", subscriptionId=" + subscriptionId
                + ", lineageCode=" + lineageCode
                + ", persistedDateUtc=" + persistedDateUtc
                + ", driverDurableKey='" + driverDurableKey + '\''
                + ", vehicleDurableKey='" + vehicleDurableKey + '\''
                + ", assetId=" + assetId
                + ", driverId=" + driverId
                + ", eventId=" + eventId
                + ", eventTypeId=" + eventTypeId
                + ", eventCategory='" + eventCategory + '\''
                + ", startDateTime=" + startDateTime
                + ", startOdometerKilometres=" + startOdometerKilometres
                + ", startPositionTimestamp=" + startPositionTimestamp
                + ", startPositionLongitude=" + startPositionLongitude
                + ", startPositionLatitude=" + startPositionLatitude
                + ", startPositionPositionId=" + startPositionPositionId
                + ", startPositionSpeedKilometresPerHour=" + startPositionSpeedKilometresPerHour
                + ", endDateTime=" + endDateTime
                + ", endOdometerKilometres=" + endOdometerKilometres
                + ", endPositionTimestamp=" + endPositionTimestamp
                + ", endPositionLongitude=" + endPositionLongitude
                + ", endPositionLatitude=" + endPositionLatitude
                + ", endPositionPositionId=" + endPositionPositionId
                + ", endPositionSpeedKilometresPerHour=" + endPositionSpeedKilometresPerHour
                + ", value=" + value
                + ", valueType='" + valueType + '\''
                + ", valueUnits='" + valueUnits + '\''
                + ", totalTimeSeconds=" + totalTimeSeconds
                + ", totalOccurrences=" + totalOccurrences
                + ", mediaUrlsRoad='" + mediaUrlsRoad + '\''
                + ", mediaUrlsCab='" + mediaUrlsCab + '\''
                + ", mediaUrlsCamera3='" + mediaUrlsCamera3 + '\''
                + ", mediaUrlsCamera4='" + mediaUrlsCamera4 + '\''
                + ", locationId=" + locationId
                + ", speedLimit=" + speedLimit
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpeedLayerEvent that = (SpeedLayerEvent) o;
        return Objects.equal(observedDay, that.observedDay)
                && Objects.equal(durableId, that.durableId)
                && Objects.equal(ingestedDateUtc, that.ingestedDateUtc)
                && Objects.equal(subscriptionId, that.subscriptionId)
                && Objects.equal(lineageCode, that.lineageCode)
                && Objects.equal(persistedDateUtc, that.persistedDateUtc)
                && Objects.equal(driverDurableKey, that.driverDurableKey)
                && Objects.equal(vehicleDurableKey, that.vehicleDurableKey)
                && Objects.equal(assetId, that.assetId)
                && Objects.equal(driverId, that.driverId)
                && Objects.equal(eventId, that.eventId)
                && Objects.equal(eventTypeId, that.eventTypeId)
                && Objects.equal(eventCategory, that.eventCategory)
                && Objects.equal(startDateTime, that.startDateTime)
                && Objects.equal(startOdometerKilometres, that.startOdometerKilometres)
                && Objects.equal(startPositionTimestamp, that.startPositionTimestamp)
                && Objects.equal(startPositionLongitude, that.startPositionLongitude)
                && Objects.equal(startPositionLatitude, that.startPositionLatitude)
                && Objects.equal(startPositionPositionId, that.startPositionPositionId)
                && Objects.equal(startPositionSpeedKilometresPerHour, that.startPositionSpeedKilometresPerHour)
                && Objects.equal(endDateTime, that.endDateTime)
                && Objects.equal(endOdometerKilometres, that.endOdometerKilometres)
                && Objects.equal(endPositionTimestamp, that.endPositionTimestamp)
                && Objects.equal(endPositionLongitude, that.endPositionLongitude)
                && Objects.equal(endPositionLatitude, that.endPositionLatitude)
                && Objects.equal(endPositionPositionId, that.endPositionPositionId)
                && Objects.equal(endPositionSpeedKilometresPerHour, that.endPositionSpeedKilometresPerHour)
                && Objects.equal(value, that.value)
                && Objects.equal(valueType, that.valueType)
                && Objects.equal(valueUnits, that.valueUnits)
                && Objects.equal(totalTimeSeconds, that.totalTimeSeconds)
                && Objects.equal(totalOccurrences, that.totalOccurrences)
                && Objects.equal(mediaUrlsRoad, that.mediaUrlsRoad)
                && Objects.equal(mediaUrlsCab, that.mediaUrlsCab)
                && Objects.equal(mediaUrlsCamera3, that.mediaUrlsCamera3)
                && Objects.equal(mediaUrlsCamera4, that.mediaUrlsCamera4)
                && Objects.equal(locationId, that.locationId)
                && Objects.equal(speedLimit, that.speedLimit);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(observedDay, durableId, ingestedDateUtc, subscriptionId, lineageCode, persistedDateUtc, driverDurableKey,
                vehicleDurableKey, assetId, driverId, eventId, eventTypeId, eventCategory, startDateTime, startOdometerKilometres,
                startPositionTimestamp, startPositionLongitude, startPositionLatitude, startPositionPositionId,
                startPositionSpeedKilometresPerHour, endDateTime, endOdometerKilometres, endPositionTimestamp, endPositionLongitude,
                endPositionLatitude, endPositionPositionId, endPositionSpeedKilometresPerHour, value, valueType, valueUnits,
                totalTimeSeconds, totalOccurrences, mediaUrlsRoad, mediaUrlsCab, mediaUrlsCamera3, mediaUrlsCamera4, locationId, speedLimit);
    }
}
