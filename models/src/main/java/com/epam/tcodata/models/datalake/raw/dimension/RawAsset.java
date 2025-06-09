package com.epam.tcodata.models.datalake.raw.dimension;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.raw.RawEntity;

import java.sql.Timestamp;

@SuppressWarnings("CPD-START")
public class RawAsset extends RawEntity {

    private static final long serialVersionUID = 23291378800218240L;

    public static class Fields {
        public static final String ASSET_ID = "asset_id";
        public static final String ASSET_TYPE_ID = "asset_type_id";
        public static final String DESCRIPTION = "description";
        public static final String CONNECTED_TRAILER = "connected_trailer";
        public static final String REGISTRATION_NUMBER = "registration_number";
        public static final String SITE_ID = "site_id";
        public static final String FUEL_TYPE = "fuel_type";
        public static final String TARGET_FUEL_CONSUMPTION = "target_fuel_consumption";
        public static final String TARGET_FUEL_CONSUMPTION_UNITS = "target_fuel_consumption_units";
        public static final String TARGET_HOURLY_FUEL_CONSUMPTION = "target_hourly_fuel_consumption";
        public static final String TARGET_HOURLY_FUEL_CONSUMPTION_UNITS = "target_hourly_fuel_consumption_units";
        public static final String FLEET_NUMBER = "fleet_number";
        public static final String MAKE = "make";
        public static final String MODEL = "model";
        public static final String YEAR = "year";
        public static final String VIN_NUMBER = "vin_number";
        public static final String ENGINE_NUMBER = "engine_number";
        public static final String FM_VEHICLE_ID = "fm_vehicle_id";
        public static final String ADDITION_MOBILE_DEVICE = "additional_mobile_device";
        public static final String NOTES = "notes";
        public static final String ICON = "icon";
        public static final String ICON_COLOUR = "icon_colour";
        public static final String COLOUR = "colour";
        public static final String ASSET_IMAGE = "asset_image";
        public static final String DEFAULT_IMAGE = "default_image";
        public static final String ASSET_IMAGE_URL = "asset_image_url";
        public static final String USER_STATE = "user_state";
        public static final String CREATED_BY = "created_by";
        public static final String CREATED_DATE = "created_date";
        public static final String ODOMETER = "odometer";
        public static final String ENGINE_HOURS = "engine_hours";
        public static final String COUNTRY = "country";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.ASSET_ID)
    private Long assetId;
    @ColumnName(Fields.ASSET_TYPE_ID)
    private Integer assetTypeId;
    @ColumnName(Fields.DESCRIPTION)
    private String description;
    @ColumnName(Fields.CONNECTED_TRAILER)
    private Boolean connectedTrailer;
    @ColumnName(Fields.REGISTRATION_NUMBER)
    private String registrationNumber;
    @ColumnName(Fields.SITE_ID)
    private Long siteId;
    @ColumnName(Fields.FUEL_TYPE)
    private String fuelType;
    @ColumnName(Fields.TARGET_FUEL_CONSUMPTION)
    private Double targetFuelConsumption;
    @ColumnName(Fields.TARGET_FUEL_CONSUMPTION_UNITS)
    private String targetFuelConsumptionUnits;
    @ColumnName(Fields.TARGET_HOURLY_FUEL_CONSUMPTION)
    private Double targetHourlyFuelConsumption;
    @ColumnName(Fields.TARGET_HOURLY_FUEL_CONSUMPTION_UNITS)
    private String targetHourlyFuelConsumptionUnits;
    @ColumnName(Fields.FLEET_NUMBER)
    private String fleetNumber;
    @ColumnName(Fields.MAKE)
    private String make;
    @ColumnName(Fields.MODEL)
    private String model;
    @ColumnName(Fields.YEAR)
    private String year;
    @ColumnName(Fields.ENGINE_NUMBER)
    private String vinNumber;
    @ColumnName(Fields.VIN_NUMBER)
    private String engineNumber;
    @ColumnName(Fields.FM_VEHICLE_ID)
    private Long fmVehicleId;
    @ColumnName(Fields.ADDITION_MOBILE_DEVICE)
    private String additionalMobileDevice;
    @ColumnName(Fields.NOTES)
    private String notes;
    @ColumnName(Fields.ICON)
    private String icon;
    @ColumnName(Fields.ICON_COLOUR)
    private String iconColour;
    @ColumnName(Fields.COLOUR)
    private String colour;
    @ColumnName(Fields.ASSET_IMAGE)
    private String assetImage;
    @ColumnName(Fields.DEFAULT_IMAGE)
    private Boolean defaultImage;
    @ColumnName(Fields.ASSET_IMAGE_URL)
    private String assetImageUrl;
    @ColumnName(Fields.USER_STATE)
    private String userState;
    @ColumnName(Fields.CREATED_BY)
    private String createdBy;
    @ColumnName(Fields.CREATED_DATE)
    private Timestamp createdDate;
    @ColumnName(Fields.ODOMETER)
    private Double odometer;
    @ColumnName(Fields.ENGINE_HOURS)
    private String engineHours;
    @ColumnName(Fields.COUNTRY)
    private String country;

    public RawAsset() {
        /***  Default implementation ***/
    }

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public Integer getAssetTypeId() {
        return assetTypeId;
    }

    public void setAssetTypeId(Integer assetTypeId) {
        this.assetTypeId = assetTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getConnectedTrailer() {
        return connectedTrailer;
    }

    public void setConnectedTrailer(Boolean connectedTrailer) {
        this.connectedTrailer = connectedTrailer;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public Double getTargetFuelConsumption() {
        return targetFuelConsumption;
    }

    public void setTargetFuelConsumption(Double targetFuelConsumption) {
        this.targetFuelConsumption = targetFuelConsumption;
    }

    public String getTargetFuelConsumptionUnits() {
        return targetFuelConsumptionUnits;
    }

    public void setTargetFuelConsumptionUnits(String targetFuelConsumptionUnits) {
        this.targetFuelConsumptionUnits = targetFuelConsumptionUnits;
    }

    public Double getTargetHourlyFuelConsumption() {
        return targetHourlyFuelConsumption;
    }

    public void setTargetHourlyFuelConsumption(Double targetHourlyFuelConsumption) {
        this.targetHourlyFuelConsumption = targetHourlyFuelConsumption;
    }

    public String getTargetHourlyFuelConsumptionUnits() {
        return targetHourlyFuelConsumptionUnits;
    }

    public void setTargetHourlyFuelConsumptionUnits(String targetHourlyFuelConsumptionUnits) {
        this.targetHourlyFuelConsumptionUnits = targetHourlyFuelConsumptionUnits;
    }

    public String getFleetNumber() {
        return fleetNumber;
    }

    public void setFleetNumber(String fleetNumber) {
        this.fleetNumber = fleetNumber;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getVinNumber() {
        return vinNumber;
    }

    public void setVinNumber(String vinNumber) {
        this.vinNumber = vinNumber;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    public Long getFmVehicleId() {
        return fmVehicleId;
    }

    public void setFmVehicleId(Long fmVehicleId) {
        this.fmVehicleId = fmVehicleId;
    }

    public String getAdditionalMobileDevice() {
        return additionalMobileDevice;
    }

    public void setAdditionalMobileDevice(String additionalMobileDevice) {
        this.additionalMobileDevice = additionalMobileDevice;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIconColour() {
        return iconColour;
    }

    public void setIconColour(String iconColour) {
        this.iconColour = iconColour;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getAssetImage() {
        return assetImage;
    }

    public void setAssetImage(String assetImage) {
        this.assetImage = assetImage;
    }

    public Boolean getDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(Boolean defaultImage) {
        this.defaultImage = defaultImage;
    }

    public String getAssetImageUrl() {
        return assetImageUrl;
    }

    public void setAssetImageUrl(String assetImageUrl) {
        this.assetImageUrl = assetImageUrl;
    }

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedDate() {
        return createdDate == null ? null : new Timestamp(createdDate.getTime());
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate == null ? null : new Timestamp(createdDate.getTime());
    }

    public Double getOdometer() {
        return odometer;
    }

    public void setOdometer(Double odometer) {
        this.odometer = odometer;
    }

    public String getEngineHours() {
        return engineHours;
    }

    public void setEngineHours(String engineHours) {
        this.engineHours = engineHours;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
