package com.epam.tcodata.sql.dal.domain.speedlayer;

import com.epam.tcodata.sql.dal.util.SqlCommon;
import com.google.common.base.Objects;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.sql.Timestamp;

public class SpeedLayerPosition implements ISpeedLayerEntity {

    public static class Fields {
        public static final String OBSERVED_DAY = "ObservedDay";
        public static final String DURABLE_ID = "DurableId";
        public static final String INGESTED_DATE_UTC = "IngestedDateUtc";
        public static final String SUBSTRACTION_ID = "SubscriptionId";
        public static final String LINEAGE_CODE = "LineageCode";
        public static final String PERSISTED_DATE_UTC = "PersistedDateUtc";
        public static final String DRIVER_DURABLE_KEY = "DriverDurableKey";
        public static final String VEHICLE_DURABLE_KEY = "VehicleDurableKey";
        public static final String POSITION_ID = "PositionId";
        public static final String ASSET_ID = "AssetId";
        public static final String DRIVER_ID = "DriverId";
        public static final String TIMESTAMP = "Timestamp";
        public static final String LATITUDE = "Latitude";
        public static final String LONGITUDE = "Longitude";
        public static final String SPEED_KILOMETRES_PER_HOUR = "SpeedKilometresPerHour";
        public static final String SPEED_LIMIT = "SpeedLimit";
        public static final String ALTITUDE_METRES = "AltitudeMetres";
        public static final String HEADING = "Heading";
        public static final String NUMBER_OF_SATELLITES = "NumberOfSatellites";
        public static final String HDOP = "Hdop";
        public static final String VDOP = "Vdop";
        public static final String PDOP = "Pdop";
        public static final String AGE_OF_READING_SECONDS = "AgeOfReadingSeconds";
        public static final String DISTANCE_SINCE_READING_KILOMETRES = "DistanceSinceReadingKilometres";
        public static final String IGNITION_ON = "IgnitionOn";
        public static final String ODOMETER_KILOMETRES = "OdometerKilometres";
        public static final String FORMATTED_ADDRESS = "FormattedAddress";
        public static final String SOURCE = "Source";
        public static final String AV1 = "Avl";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.OBSERVED_DAY)
    private Integer observedDay;

    @ColumnName(Fields.DURABLE_ID)
    private String durableId;

    @ColumnName(Fields.INGESTED_DATE_UTC)
    private Timestamp ingestedDateUtc;

    @ColumnName(Fields.SUBSTRACTION_ID)
    private Long subscriptionId;

    @ColumnName(Fields.LINEAGE_CODE)
    private Integer lineageCode;

    @ColumnName(Fields.PERSISTED_DATE_UTC)
    private Timestamp persistedDateUtc;

    @ColumnName(Fields.DRIVER_DURABLE_KEY)
    private String driverDurableKey;

    @ColumnName(Fields.VEHICLE_DURABLE_KEY)
    private String vehicleDurableKey;

    @ColumnName(Fields.POSITION_ID)
    private Long positionId;

    @ColumnName(Fields.ASSET_ID)
    private Long assetId;

    @ColumnName(Fields.DRIVER_ID)
    private Long driverId;

    @ColumnName(Fields.TIMESTAMP)
    private Timestamp timestamp;

    @ColumnName(Fields.LATITUDE)
    private Double latitude;

    @ColumnName(Fields.LONGITUDE)
    private Double longitude;

    @ColumnName(Fields.SPEED_KILOMETRES_PER_HOUR)
    private Double speedKilometresPerHour;

    @ColumnName(Fields.SPEED_LIMIT)
    private Double speedLimit;

    @ColumnName(Fields.ALTITUDE_METRES)
    private Integer altitudeMetres;

    @ColumnName(Fields.HEADING)
    private Integer heading;

    @ColumnName(Fields.NUMBER_OF_SATELLITES)
    private Integer numberOfSatellites;

    @ColumnName(Fields.HDOP)
    private Integer hdop;

    @ColumnName(Fields.VDOP)
    private Integer vdop;

    @ColumnName(Fields.PDOP)
    private Integer pdop;

    @ColumnName(Fields.AGE_OF_READING_SECONDS)
    private Long ageOfReadingSeconds;

    @ColumnName(Fields.DISTANCE_SINCE_READING_KILOMETRES)
    private Integer distanceSinceReadingKilometres;

    @ColumnName(Fields.IGNITION_ON)
    private Boolean ignitionOn;

    @ColumnName(Fields.ODOMETER_KILOMETRES)
    private Double odometerKilometres;

    @ColumnName(Fields.FORMATTED_ADDRESS)
    private String formattedAddress;

    @ColumnName(Fields.SOURCE)
    private String source;

    @ColumnName(Fields.AV1)
    private Boolean avl;

    public SpeedLayerPosition() {
        /***  Default implementation ***/
    }

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

    public SpeedLayerPosition setObservedDay(Integer observedDay) {
        this.observedDay = observedDay;
        return this;
    }

    @Override
    public String getDurableId() {
        return durableId;
    }

    public SpeedLayerPosition setDurableId(String durableId) {
        this.durableId = durableId;
        return this;
    }

    public Timestamp getIngestedDateUtc() {
        return SqlCommon.clone(ingestedDateUtc);
    }

    public SpeedLayerPosition setIngestedDateUtc(Timestamp ingestedDateUtc) {
        this.ingestedDateUtc = SqlCommon.clone(ingestedDateUtc);
        return this;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public SpeedLayerPosition setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
        return this;
    }

    public Integer getLineageCode() {
        return lineageCode;
    }

    public SpeedLayerPosition setLineageCode(Integer lineageCode) {
        this.lineageCode = lineageCode;
        return this;
    }

    public Timestamp getPersistedDateUtc() {
        return SqlCommon.clone(persistedDateUtc);
    }

    public SpeedLayerPosition setPersistedDateUtc(Timestamp persistedDateUtc) {
        this.persistedDateUtc = SqlCommon.clone(persistedDateUtc);
        return this;
    }

    public String getDriverDurableKey() {
        return driverDurableKey;
    }

    public SpeedLayerPosition setDriverDurableKey(String driverDurableKey) {
        this.driverDurableKey = driverDurableKey;
        return this;
    }

    public String getVehicleDurableKey() {
        return vehicleDurableKey;
    }

    public SpeedLayerPosition setVehicleDurableKey(String vehicleDurableKey) {
        this.vehicleDurableKey = vehicleDurableKey;
        return this;
    }

    public Long getPositionId() {
        return positionId;
    }

    public SpeedLayerPosition setPositionId(Long positionId) {
        this.positionId = positionId;
        return this;
    }

    public Long getAssetId() {
        return assetId;
    }

    public SpeedLayerPosition setAssetId(Long assetId) {
        this.assetId = assetId;
        return this;
    }

    public Long getDriverId() {
        return driverId;
    }

    public SpeedLayerPosition setDriverId(Long driverId) {
        this.driverId = driverId;
        return this;
    }

    public Timestamp getTimestamp() {
        return SqlCommon.clone(timestamp);
    }

    public SpeedLayerPosition setTimestamp(Timestamp timestamp) {
        this.timestamp = SqlCommon.clone(timestamp);
        return this;
    }

    public Double getLatitude() {
        return latitude;
    }

    public SpeedLayerPosition setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Double getLongitude() {
        return longitude;
    }

    public SpeedLayerPosition setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public Double getSpeedKilometresPerHour() {
        return speedKilometresPerHour;
    }

    public SpeedLayerPosition setSpeedKilometresPerHour(Double speedKilometresPerHour) {
        this.speedKilometresPerHour = speedKilometresPerHour;
        return this;
    }

    public Double getSpeedLimit() {
        return speedLimit;
    }

    public SpeedLayerPosition setSpeedLimit(Double speedLimit) {
        this.speedLimit = speedLimit;
        return this;
    }

    public Integer getAltitudeMetres() {
        return altitudeMetres;
    }

    public SpeedLayerPosition setAltitudeMetres(Integer altitudeMetres) {
        this.altitudeMetres = altitudeMetres;
        return this;
    }

    public Integer getHeading() {
        return heading;
    }

    public SpeedLayerPosition setHeading(Integer heading) {
        this.heading = heading;
        return this;
    }

    public Integer getNumberOfSatellites() {
        return numberOfSatellites;
    }

    public SpeedLayerPosition setNumberOfSatellites(Integer numberOfSatellites) {
        this.numberOfSatellites = numberOfSatellites;
        return this;
    }

    public Integer getHdop() {
        return hdop;
    }

    public SpeedLayerPosition setHdop(Integer hdop) {
        this.hdop = hdop;
        return this;
    }

    public Integer getVdop() {
        return vdop;
    }

    public SpeedLayerPosition setVdop(Integer vdop) {
        this.vdop = vdop;
        return this;
    }

    public Integer getPdop() {
        return pdop;
    }

    public SpeedLayerPosition setPdop(Integer pdop) {
        this.pdop = pdop;
        return this;
    }

    public Long getAgeOfReadingSeconds() {
        return ageOfReadingSeconds;
    }

    public SpeedLayerPosition setAgeOfReadingSeconds(Long ageOfReadingSeconds) {
        this.ageOfReadingSeconds = ageOfReadingSeconds;
        return this;
    }

    public Integer getDistanceSinceReadingKilometres() {
        return distanceSinceReadingKilometres;
    }

    public SpeedLayerPosition setDistanceSinceReadingKilometres(Integer distanceSinceReadingKilometres) {
        this.distanceSinceReadingKilometres = distanceSinceReadingKilometres;
        return this;
    }

    public Boolean getIgnitionOn() {
        return ignitionOn;
    }

    public SpeedLayerPosition setIgnitionOn(Boolean ignitionOn) {
        this.ignitionOn = ignitionOn;
        return this;
    }

    public Double getOdometerKilometres() {
        return odometerKilometres;
    }

    public SpeedLayerPosition setOdometerKilometres(Double odometerKilometres) {
        this.odometerKilometres = odometerKilometres;
        return this;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public SpeedLayerPosition setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
        return this;
    }

    public String getSource() {
        return source;
    }

    public SpeedLayerPosition setSource(String source) {
        this.source = source;
        return this;
    }

    public Boolean getAvl() {
        return avl;
    }

    public SpeedLayerPosition setAvl(Boolean avl) {
        this.avl = avl;
        return this;
    }

    @Override
    public String toString() {
        return "SpeedLayerPosition{"
                + "observedDay=" + observedDay
                + ", durableId='" + durableId + '\''
                + ", ingestedDateUtc=" + ingestedDateUtc
                + ", subscriptionId=" + subscriptionId
                + ", lineageCode=" + lineageCode
                + ", persistedDateUtc=" + persistedDateUtc
                + ", driverDurableKey='" + driverDurableKey + '\''
                + ", vehicleDurableKey='" + vehicleDurableKey + '\''
                + ", positionId=" + positionId
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
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpeedLayerPosition that = (SpeedLayerPosition) o;
        return Objects.equal(observedDay, that.observedDay)
                && Objects.equal(durableId, that.durableId)
                && Objects.equal(ingestedDateUtc, that.ingestedDateUtc)
                && Objects.equal(subscriptionId, that.subscriptionId)
                && Objects.equal(lineageCode, that.lineageCode)
                && Objects.equal(persistedDateUtc, that.persistedDateUtc)
                && Objects.equal(driverDurableKey, that.driverDurableKey)
                && Objects.equal(vehicleDurableKey, that.vehicleDurableKey)
                && Objects.equal(positionId, that.positionId)
                && Objects.equal(assetId, that.assetId)
                && Objects.equal(driverId, that.driverId)
                && Objects.equal(timestamp, that.timestamp)
                && Objects.equal(latitude, that.latitude)
                && Objects.equal(longitude, that.longitude)
                && Objects.equal(speedKilometresPerHour, that.speedKilometresPerHour)
                && Objects.equal(speedLimit, that.speedLimit)
                && Objects.equal(altitudeMetres, that.altitudeMetres)
                && Objects.equal(heading, that.heading)
                && Objects.equal(numberOfSatellites, that.numberOfSatellites)
                && Objects.equal(hdop, that.hdop)
                && Objects.equal(vdop, that.vdop)
                && Objects.equal(pdop, that.pdop)
                && Objects.equal(ageOfReadingSeconds, that.ageOfReadingSeconds)
                && Objects.equal(distanceSinceReadingKilometres, that.distanceSinceReadingKilometres)
                && Objects.equal(ignitionOn, that.ignitionOn)
                && Objects.equal(odometerKilometres, that.odometerKilometres)
                && Objects.equal(formattedAddress, that.formattedAddress)
                && Objects.equal(source, that.source)
                && Objects.equal(avl, that.avl);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(observedDay, durableId, ingestedDateUtc, subscriptionId, lineageCode, persistedDateUtc, driverDurableKey,
                vehicleDurableKey, positionId, assetId, driverId, timestamp, latitude, longitude, speedKilometresPerHour, speedLimit,
                altitudeMetres, heading, numberOfSatellites, hdop, vdop, pdop, ageOfReadingSeconds, distanceSinceReadingKilometres,
                ignitionOn, odometerKilometres, formattedAddress, source, avl);
    }
}
