package com.epam.tcodata.models.datalake.prepared.dimension;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.prepared.PreparedEntity;

import java.sql.Timestamp;

@SuppressWarnings("CPD-START")
public class PreparedVehicle extends PreparedEntity {

    private static final long serialVersionUID = -265210659839125328L;

    public static class Fields {
        public static final String ORGANIZATION_DURABLE_KEY = "organization_durable_key";
        public static final String EXTERNAL_ID = "external_id";
        public static final String PERSISTED_DATE_UTC = "persisted_date_utc";
        public static final String VEHICLE_TYPE_DURABLE_KEY = "vehicle_type_durable_key";
        public static final String VEHICLE_TYPE_CODE = "vehicle_type_code";
        public static final String DESCRIPTION = "description";
        public static final String CONNECTED_TRAILER = "connected_trailer";
        public static final String REGISTRATION_NUMBER = "registration_number";
        public static final String ORG_GROUP_DURABLE_ID = "org_group_durable_id";
        public static final String FUEL_TYPE_DURABLE_KEY = "fuel_type_durable_key";
        public static final String FUEL_TYPE_CODE = "fuel_type_code";
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
        public static final String VEHICLE_STATE_DURABLE_KEY = "vehicle_state_durable_key";
        public static final String VEHICLE_STATE_CODE = "vehicle_state_code";
        public static final String CREATED_BY = "created_by";
        public static final String CREATED_DATE = "created_date";
        public static final String ODOMETER = "odometer";
        public static final String ENGINE_SECONDS = "engine_seconds";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.ORGANIZATION_DURABLE_KEY)
    private String organizationDurableKey;
    @ColumnName(Fields.EXTERNAL_ID)
    private Long externalId;
    @ColumnName(Fields.PERSISTED_DATE_UTC)
    private Timestamp persistedDateUtc;
    @ColumnName(Fields.VEHICLE_TYPE_DURABLE_KEY)
    private String vehicleTypeDurableKey;
    @ColumnName(Fields.VEHICLE_TYPE_CODE)
    private String vehicleTypeCode;
    @ColumnName(Fields.DESCRIPTION)
    private String description;
    @ColumnName(Fields.CONNECTED_TRAILER)
    private Boolean connectedTrailer;
    @ColumnName(Fields.REGISTRATION_NUMBER)
    private String registrationNumber;
    @ColumnName(Fields.ORG_GROUP_DURABLE_ID)
    private String orgGroupDurableId;
    @ColumnName(Fields.FUEL_TYPE_DURABLE_KEY)
    private String fuelTypeDurableKey;
    @ColumnName(Fields.FUEL_TYPE_CODE)
    private String fuelTypeCode;
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
    @ColumnName(Fields.VEHICLE_STATE_DURABLE_KEY)
    private String vehicleStateDurableKey;
    @ColumnName(Fields.VEHICLE_STATE_CODE)
    private String vehicleStateCode;
    @ColumnName(Fields.CREATED_BY)
    private String createdBy;
    @ColumnName(Fields.CREATED_DATE)
    private Timestamp createdDate;
    @ColumnName(Fields.ODOMETER)
    private Double odometer;
    @ColumnName(Fields.ENGINE_SECONDS)
    private Long engineSeconds;

    public PreparedVehicle() {
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

    public String getVehicleTypeDurableKey() {
        return vehicleTypeDurableKey;
    }

    public void setVehicleTypeDurableKey(String vehicleTypeDurableKey) {
        this.vehicleTypeDurableKey = vehicleTypeDurableKey;
    }

    public String getVehicleTypeCode() {
        return vehicleTypeCode;
    }

    public void setVehicleTypeCode(String vehicleTypeCode) {
        this.vehicleTypeCode = vehicleTypeCode;
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

    public String getOrgGroupDurableId() {
        return orgGroupDurableId;
    }

    public void setOrgGroupDurableId(String orgGroupDurableId) {
        this.orgGroupDurableId = orgGroupDurableId;
    }

    public String getFuelTypeDurableKey() {
        return fuelTypeDurableKey;
    }

    public void setFuelTypeDurableKey(String fuelTypeDurableKey) {
        this.fuelTypeDurableKey = fuelTypeDurableKey;
    }

    public String getFuelTypeCode() {
        return fuelTypeCode;
    }

    public void setFuelTypeCode(String fuelTypeCode) {
        this.fuelTypeCode = fuelTypeCode;
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

    public String getVehicleStateDurableKey() {
        return vehicleStateDurableKey;
    }

    public void setVehicleStateDurableKey(String vehicleStateDurableKey) {
        this.vehicleStateDurableKey = vehicleStateDurableKey;
    }

    public String getVehicleStateCode() {
        return vehicleStateCode;
    }

    public void setVehicleStateCode(String vehicleStateCode) {
        this.vehicleStateCode = vehicleStateCode;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Double getOdometer() {
        return odometer;
    }

    public void setOdometer(Double odometer) {
        this.odometer = odometer;
    }

    public Long getEngineSeconds() {
        return engineSeconds;
    }

    public void setEngineSeconds(Long engineSeconds) {
        this.engineSeconds = engineSeconds;
    }
}
