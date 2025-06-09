package com.epam.tcodata.models.datalake.raw.fact;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.raw.RawEntity;

import java.sql.Timestamp;

@SuppressWarnings("CPD-START")
public class RawPosition extends RawEntity {

    private static final long serialVersionUID = -1520898197465609563L;

    public static class Fields {
        public static final String DRIVER_DURABLE_KEY = "driver_durable_key";
        public static final String VEHICLE_DURABLE_KEY = "vehicle_durable_key";
        public static final String TIMESTAMP = "timestamp";
        public static final String LONGITUDE = "longitude";
        public static final String LATITUDE = "latitude";
        public static final String DRIVER_ID = "driver_id";
        public static final String ASSET_ID = "asset_id";
        public static final String POSITION_ID = "position_id";
        public static final String AVL = "avl";
        public static final String SOURCE = "source";
        public static final String ODOMETER_KILOMETRES = "odometer_kilometres";
        public static final String IGNITION_ON = "ignition_on";
        public static final String AGE_OF_READING_SECONDS = "age_of_reading_seconds";
        public static final String PDOP = "pdop";
        public static final String VDOP = "vdop";
        public static final String HDOP = "hdop";
        public static final String NUMBER_OF_SATELLITES = "number_of_satellites";
        public static final String HEADING = "heading";
        public static final String ALTITUDE_METRES = "altitude_metres";
        public static final String SPEED_KILOMETRES_PER_HOUR = "speed_kilometres_per_hour";
        public static final String DISTANCE_SINCE_READING_KILOMETRES = "distance_since_reading_kilometres";
        public static final String FORMATTED_ADDRESS = "formatted_address";
        public static final String SPEED_LIMIT = "speed_limit";
        public static final String YEAR = "year";
        public static final String WEEK_NUMBER = "week_number";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.DRIVER_DURABLE_KEY)
    private String driverDurableKey;
    @ColumnName(Fields.VEHICLE_DURABLE_KEY)
    private String vehicleDurableKey;
    @ColumnName(Fields.TIMESTAMP)
    private Timestamp timestamp;
    @ColumnName(Fields.LONGITUDE)
    private Double longitude;
    @ColumnName(Fields.LATITUDE)
    private Double latitude;
    @ColumnName(Fields.DRIVER_ID)
    private Long driverId;
    @ColumnName(Fields.ASSET_ID)
    private Long assetId;
    @ColumnName(Fields.POSITION_ID)
    private Long positionId;
    @ColumnName(Fields.AVL)
    private Boolean avl;
    @ColumnName(Fields.SOURCE)
    private String source;
    @ColumnName(Fields.ODOMETER_KILOMETRES)
    private Double odometerKilometres;
    @ColumnName(Fields.IGNITION_ON)
    private Boolean ignitionOn;
    @ColumnName(Fields.AGE_OF_READING_SECONDS)
    private Long ageOfReadingSeconds;
    @ColumnName(Fields.PDOP)
    private Integer pdop;
    @ColumnName(Fields.VDOP)
    private Integer vdop;
    @ColumnName(Fields.HDOP)
    private Integer hdop;
    @ColumnName(Fields.NUMBER_OF_SATELLITES)
    private Integer numberOfSatellites;
    @ColumnName(Fields.HEADING)
    private Integer heading;
    @ColumnName(Fields.ALTITUDE_METRES)
    private Integer altitudeMetres;
    @ColumnName(Fields.SPEED_KILOMETRES_PER_HOUR)
    private Double speedKilometresPerHour;
    @ColumnName(Fields.DISTANCE_SINCE_READING_KILOMETRES)
    private Integer distanceSinceReadingKilometres;
    @ColumnName(Fields.FORMATTED_ADDRESS)
    private String formattedAddress;
    @ColumnName(Fields.SPEED_LIMIT)
    private Double speedLimit;
    @ColumnName(Fields.YEAR)
    private Integer year;
    @ColumnName(Fields.WEEK_NUMBER)
    private Integer weekNumber;

    public RawPosition() {
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

    public Timestamp getTimestamp() {
        return timestamp == null ? null : new Timestamp(timestamp.getTime());
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp == null ? null : new Timestamp(timestamp.getTime());
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
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

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public Boolean getAvl() {
        return avl;
    }

    public void setAvl(Boolean avl) {
        this.avl = avl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Double getOdometerKilometres() {
        return odometerKilometres;
    }

    public void setOdometerKilometres(Double odometerKilometres) {
        this.odometerKilometres = odometerKilometres;
    }

    public Boolean getIgnitionOn() {
        return ignitionOn;
    }

    public void setIgnitionOn(Boolean ignitionOn) {
        this.ignitionOn = ignitionOn;
    }

    public Long getAgeOfReadingSeconds() {
        return ageOfReadingSeconds;
    }

    public void setAgeOfReadingSeconds(Long ageOfReadingSeconds) {
        this.ageOfReadingSeconds = ageOfReadingSeconds;
    }

    public Integer getPdop() {
        return pdop;
    }

    public void setPdop(Integer pdop) {
        this.pdop = pdop;
    }

    public Integer getVdop() {
        return vdop;
    }

    public void setVdop(Integer vdop) {
        this.vdop = vdop;
    }

    public Integer getHdop() {
        return hdop;
    }

    public void setHdop(Integer hdop) {
        this.hdop = hdop;
    }

    public Integer getNumberOfSatellites() {
        return numberOfSatellites;
    }

    public void setNumberOfSatellites(Integer numberOfSatellites) {
        this.numberOfSatellites = numberOfSatellites;
    }

    public Integer getHeading() {
        return heading;
    }

    public void setHeading(Integer heading) {
        this.heading = heading;
    }

    public Integer getAltitudeMetres() {
        return altitudeMetres;
    }

    public void setAltitudeMetres(Integer altitudeMetres) {
        this.altitudeMetres = altitudeMetres;
    }

    public Double getSpeedKilometresPerHour() {
        return speedKilometresPerHour;
    }

    public void setSpeedKilometresPerHour(Double speedKilometresPerHour) {
        this.speedKilometresPerHour = speedKilometresPerHour;
    }

    public Integer getDistanceSinceReadingKilometres() {
        return distanceSinceReadingKilometres;
    }

    public void setDistanceSinceReadingKilometres(Integer distanceSinceReadingKilometres) {
        this.distanceSinceReadingKilometres = distanceSinceReadingKilometres;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
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
