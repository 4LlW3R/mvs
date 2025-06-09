package com.epam.tcodata.models.datalake.raw.fact;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.raw.RawEntity;

import java.sql.Timestamp;

@SuppressWarnings("CPD-START")
public class RawTrip extends RawEntity {

    private static final long serialVersionUID = 6630479634944473988L;

    public static class Fields {
        public static final String DRIVER_DURABLE_KEY = "driver_durable_key";
        public static final String VEHICLE_DURABLE_KEY = "vehicle_durable_key";
        public static final String TRIP_ID = "trip_id";
        public static final String ASSET_ID = "asset_id";
        public static final String DRIVER_ID = "driver_id";
        public static final String TRIP_START = "trip_start";
        public static final String TRIP_END = "trip_end";
        public static final String NOTES = "notes";
        public static final String PULSE_PARAMETER_NAME = "pulse_parameter_name";
        public static final String ENGINE_SECONDS = "engine_seconds";
        public static final String START_POSITION_ID = "start_position_id";
        public static final String START_POSITION_TIMESTAMP = "start_position_timestamp";
        public static final String START_POSITION_LONGITUDE = "start_position_longitude";
        public static final String START_POSITION_LATITUDE = "start_position_latitude";
        public static final String START_POSITION_SPEED_KILOMETRES_PER_HOUR = "start_position_speed_kilometres_per_hour";
        public static final String END_POSITION_ID = "end_position_id";
        public static final String END_POSITION_TIMESTAMP = "end_position_timestamp";
        public static final String END_POSITION_LONGITUDE = "end_position_longitude";
        public static final String END_POSITION_LATITUDE = "end_position_latitude";
        public static final String END_POSITION_SPEED_KILOMETRES_PER_HOUR = "end_position_speed_kilometres_per_hour";
        public static final String FIRST_DEPART = "first_depart";
        public static final String LAST_HALT = "last_halt";
        public static final String DRIVING_TIME = "driving_time";
        public static final String STANDING_TIME = "standing_time";
        public static final String DURATION = "duration";
        public static final String DISTANCE_KILOMETRES = "distance_kilometers";
        public static final String START_ODOMETER_KILOMETERS = "start_odometer_kilometers";
        public static final String END_ODOMETER_KILOMETERS = "end_odometer_kilometers";
        public static final String START_ENGINE_SECONDS = "start_engine_seconds";
        public static final String END_ENGINE_SECONDS = "end_engine_seconds";
        public static final String PULSE_VALUE = "pulse_value";
        public static final String FUEL_USED_LITRES = "fuel_used_litres";
        public static final String MAX_SPEED_KILOMETERS_PER_HOUR = "max_speed_kilometers_per_hour";
        public static final String MAX_ACCELERATION_KILOMETERS_PER_HOUR_PER_SECOND = "max_acceleration_kilometers_per_hour_per_second";
        public static final String MAX_DECELERATION_KILOMETERS_PER_HOUR_PER_SECOND = "max_deceleration_kilometers_per_hour_per_second";
        public static final String MAX_RPM = "max_rpm";
        public static final String YEAR = "year";
        public static final String WEEK_NUMBER = "week_number";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.DRIVER_DURABLE_KEY)
    private String driverDurableKey;
    @ColumnName(Fields.VEHICLE_DURABLE_KEY)
    private String vehicleDurableKey;
    @ColumnName(Fields.TRIP_ID)
    private Long tripId;
    @ColumnName(Fields.ASSET_ID)
    private Long assetId;
    @ColumnName(Fields.DRIVER_ID)
    private Long driverId;
    @ColumnName(Fields.TRIP_START)
    private Timestamp tripStart;
    @ColumnName(Fields.TRIP_END)
    private Timestamp tripEnd;
    @ColumnName(Fields.NOTES)
    private String notes;
    @ColumnName(Fields.PULSE_PARAMETER_NAME)
    private String pulseParameterName;
    @ColumnName(Fields.ENGINE_SECONDS)
    private Integer engineSeconds;
    @ColumnName(Fields.START_POSITION_ID)
    private Long startPositionId;
    @ColumnName(Fields.START_POSITION_TIMESTAMP)
    private Timestamp startPositionTimestamp;
    @ColumnName(Fields.START_POSITION_LONGITUDE)
    private Double startPositionLongitude;
    @ColumnName(Fields.START_POSITION_LATITUDE)
    private Double startPositionLatitude;
    @ColumnName(Fields.START_POSITION_SPEED_KILOMETRES_PER_HOUR)
    private Double startPositionSpeedKilometresPerHour;
    @ColumnName(Fields.END_POSITION_ID)
    private Long endPositionId;
    @ColumnName(Fields.END_POSITION_TIMESTAMP)
    private Timestamp endPositionTimestamp;
    @ColumnName(Fields.END_POSITION_LONGITUDE)
    private Double endPositionLongitude;
    @ColumnName(Fields.END_POSITION_LATITUDE)
    private Double endPositionLatitude;
    @ColumnName(Fields.END_POSITION_SPEED_KILOMETRES_PER_HOUR)
    private Double endPositionSpeedKilometresPerHour;
    @ColumnName(Fields.FIRST_DEPART)
    private Timestamp firstDepart;
    @ColumnName(Fields.LAST_HALT)
    private Timestamp lastHalt;
    @ColumnName(Fields.DRIVING_TIME)
    private Double drivingTime;
    @ColumnName(Fields.STANDING_TIME)
    private Double standingTime;
    @ColumnName(Fields.DURATION)
    private Double duration;
    @ColumnName(Fields.DISTANCE_KILOMETRES)
    private Double distanceKilometers;
    @ColumnName(Fields.START_ODOMETER_KILOMETERS)
    private Double startOdometerKilometers;
    @ColumnName(Fields.END_ODOMETER_KILOMETERS)
    private Double endOdometerKilometers;
    @ColumnName(Fields.START_ENGINE_SECONDS)
    private Integer startEngineSeconds;
    @ColumnName(Fields.END_ENGINE_SECONDS)
    private Integer endEngineSeconds;
    @ColumnName(Fields.PULSE_VALUE)
    private Double pulseValue;
    @ColumnName(Fields.FUEL_USED_LITRES)
    private Double fuelUsedLitres;
    @ColumnName(Fields.MAX_SPEED_KILOMETERS_PER_HOUR)
    private Double maxSpeedKilometersPerHour;
    @ColumnName(Fields.MAX_ACCELERATION_KILOMETERS_PER_HOUR_PER_SECOND)
    private Double maxAccelerationKilometersPerHourPerSecond;
    @ColumnName(Fields.MAX_DECELERATION_KILOMETERS_PER_HOUR_PER_SECOND)
    private Double maxDecelerationKilometersPerHourPerSecond;
    @ColumnName(Fields.MAX_RPM)
    private Double maxRpm;
    @ColumnName(Fields.YEAR)
    private Integer year;
    @ColumnName(Fields.WEEK_NUMBER)
    private Integer weekNumber;

    public RawTrip() {
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
        return tripStart == null ? null : new Timestamp(tripStart.getTime());
    }

    public void setTripStart(Timestamp tripStart) {
        this.tripStart = tripStart == null ? null : new Timestamp(tripStart.getTime());
    }

    public Timestamp getTripEnd() {
        return tripEnd == null ? null : new Timestamp(tripEnd.getTime());
    }

    public void setTripEnd(Timestamp tripEnd) {
        this.tripEnd = tripEnd == null ? null : new Timestamp(tripEnd.getTime());
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

    public Double getStartPositionSpeedKilometresPerHour() {
        return startPositionSpeedKilometresPerHour;
    }

    public void setStartPositionSpeedKilometresPerHour(Double startPositionSpeedKilometresPerHour) {
        this.startPositionSpeedKilometresPerHour = startPositionSpeedKilometresPerHour;
    }

    public Long getEndPositionId() {
        return endPositionId;
    }

    public void setEndPositionId(Long endPositionId) {
        this.endPositionId = endPositionId;
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

    public Double getEndPositionSpeedKilometresPerHour() {
        return endPositionSpeedKilometresPerHour;
    }

    public void setEndPositionSpeedKilometresPerHour(Double endPositionSpeedKilometresPerHour) {
        this.endPositionSpeedKilometresPerHour = endPositionSpeedKilometresPerHour;
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
