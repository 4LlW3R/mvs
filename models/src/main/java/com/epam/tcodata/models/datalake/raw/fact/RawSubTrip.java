package com.epam.tcodata.models.datalake.raw.fact;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.raw.RawEntity;

import java.sql.Timestamp;

@SuppressWarnings("CPD-START")
public class RawSubTrip extends RawEntity {

    private static final long serialVersionUID = 8678295454711290799L;

    public static class Fields {
        public static final String SUB_TRIP_ID = "sub_trip_id";
        public static final String PARENT_TRIP_KEY = "parent_trip_key";
        public static final String SUB_TRIP_START = "sub_trip_start";
        public static final String START_POSITION_ID = "start_position_id";
        public static final String START_POSITION_TIMESTAMP = "start_position_timestamp";
        public static final String START_POSITION_LONGITUDE = "start_position_longitude";
        public static final String START_POSITION_LATITUDE = "start_position_latitude";
        public static final String START_POSITION_SPEED_KILOMETRES_PER_HOUR = "start_position_speed_kilometres_per_hour";
        public static final String DEPART = "depart";
        public static final String HALT = "halt";
        public static final String SUB_TRIP_END = "sub_trip_end";
        public static final String END_POSITION_ID = "end_position_id";
        public static final String END_POSITION_TIMESTAMP = "end_position_timestamp";
        public static final String END_POSITION_LONGITUDE = "end_position_longitude";
        public static final String END_POSITION_LATITUDE = "end_position_latitude";
        public static final String DRIVING_TIME = "driving_time";
        public static final String STANDING_TIME = "standing_time";
        public static final String DURATION = "duration";
        public static final String DISTANCE_KILOMETRES = "distance_kilometres";
        public static final String START_ODOMETER_KILOMETRES = "start_odometer_kilometres";
        public static final String END_ODOMETER_KILOMETRES = "end_odometer_kilometres";
        public static final String START_ENGINE_SECONDS = "start_engine_seconds";
        public static final String END_ENGINE_SECONDS = "end_engine_seconds";
        public static final String ENGINE_SECONDS = "engine_seconds";
        public static final String PULSE_VALUE = "pulse_value";
        public static final String FUEL_USED_LITRES = "fuel_used_litres";
        public static final String MAX_SPEED_KILOMETRES_PER_HOUR = "max_speed_kilometers_per_hour";
        public static final String MAX_ACCELERATION_KILOMETRES_PER_HOUR_PER_SECONS = "max_acceleration_kilometers_per_hour_per_second";
        public static final String MAX_DECELERATION_KILOMETRES_PER_HOUR_PER_SECONS = "max_deceleration_kilometers_per_hour_per_second";
        public static final String MAX_RPM = "max_rpm";
        public static final String YEAR = "year";
        public static final String WEEK_NUMBER = "week_number";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.SUB_TRIP_ID)
    private Long subTripId;
    @ColumnName(Fields.PARENT_TRIP_KEY)
    private String parentTripKey;
    @ColumnName(Fields.SUB_TRIP_START)
    private Timestamp subTripStart;
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
    @ColumnName(Fields.DEPART)
    private Timestamp depart;
    @ColumnName(Fields.HALT)
    private Timestamp halt;
    @ColumnName(Fields.SUB_TRIP_END)
    private Timestamp subTripEnd;
    @ColumnName(Fields.END_POSITION_ID)
    private Long endPositionId;
    @ColumnName(Fields.END_POSITION_TIMESTAMP)
    private Timestamp endPositionTimestamp;
    @ColumnName(Fields.END_POSITION_LONGITUDE)
    private Double endPositionLongitude;
    @ColumnName(Fields.END_POSITION_LATITUDE)
    private Double endPositionLatitude;
    @ColumnName(Fields.DRIVING_TIME)
    private Integer drivingTime;
    @ColumnName(Fields.STANDING_TIME)
    private Integer standingTime;
    @ColumnName(Fields.DURATION)
    private Integer duration;
    @ColumnName(Fields.DISTANCE_KILOMETRES)
    private Double distanceKilometres;
    @ColumnName(Fields.START_ODOMETER_KILOMETRES)
    private Double startOdometerKilometres;
    @ColumnName(Fields.END_ODOMETER_KILOMETRES)
    private Double endOdometerKilometres;
    @ColumnName(Fields.START_ENGINE_SECONDS)
    private Integer startEngineSeconds;
    @ColumnName(Fields.END_ENGINE_SECONDS)
    private Integer endEngineSeconds;
    @ColumnName(Fields.ENGINE_SECONDS)
    private Integer engineSeconds;
    @ColumnName(Fields.PULSE_VALUE)
    private Double pulseValue;
    @ColumnName(Fields.FUEL_USED_LITRES)
    private Double fuelUsedLitres;
    @ColumnName(Fields.MAX_SPEED_KILOMETRES_PER_HOUR)
    private Double maxSpeedKilometersPerHour;
    @ColumnName(Fields.MAX_ACCELERATION_KILOMETRES_PER_HOUR_PER_SECONS)
    private Double maxAccelerationKilometersPerHourPerSecond;
    @ColumnName(Fields.MAX_DECELERATION_KILOMETRES_PER_HOUR_PER_SECONS)
    private Double maxDecelerationKilometersPerHourPerSecond;
    @ColumnName(Fields.MAX_RPM)
    private Double maxRpm;
    @ColumnName(Fields.YEAR)
    private Integer year;
    @ColumnName(Fields.WEEK_NUMBER)
    private Integer weekNumber;

    public RawSubTrip() {
        /***  Default implementation ***/
    }

    public Long getSubTripId() {
        return subTripId;
    }

    public void setSubTripId(Long subTripId) {
        this.subTripId = subTripId;
    }

    public String getParentTripKey() {
        return parentTripKey;
    }

    public void setParentTripKey(String parentTripKey) {
        this.parentTripKey = parentTripKey;
    }

    public Timestamp getSubTripStart() {
        return subTripStart == null ? null : new Timestamp(subTripStart.getTime());
    }

    public void setSubTripStart(Timestamp subTripStart) {
        this.subTripStart = subTripStart == null ? null : new Timestamp(subTripStart.getTime());
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
        return subTripEnd == null ? null : new Timestamp(subTripEnd.getTime());
    }

    public void setSubTripEnd(Timestamp subTripEnd) {
        this.subTripEnd = subTripEnd == null ? null : new Timestamp(subTripEnd.getTime());
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
