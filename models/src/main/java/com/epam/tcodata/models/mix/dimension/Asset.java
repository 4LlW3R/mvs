package com.epam.tcodata.models.mix.dimension;

import com.epam.tcodata.models.mix.Entity;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

/**
 * Maps to SOAP Vehicle.
 */
public class Asset extends Entity {

    private static final long serialVersionUID = 7307389028145248781L;

    private Long assetId;
    private Integer assetTypeId;
    private String description;
    @JsonProperty("IsConnectedTrailer")
    private Boolean connectedTrailer;
    private String registrationNumber;
    private Long siteId;
    private String fuelType;
    private Double targetFuelConsumption;
    private String targetFuelConsumptionUnits;
    private Double targetHourlyFuelConsumption;
    private String targetHourlyFuelConsumptionUnits;
    private String fleetNumber;
    private String make;
    private String model;
    private String year;
    private String vinNumber;
    private String engineNumber;
    private Long fmVehicleId;
    private String additionalMobileDevice;
    private String notes;
    private String icon;
    private String iconColour;
    private String colour;
    private String assetImage;
    @JsonProperty("IsDefaultImage")
    private Boolean defaultImage; //read only in swagger
    private String assetImageUrl;
    private String userState;
    private String createdBy;
    private Timestamp createdDate;
    private Double odometer;
    private String engineHours;
    private String country;

    public Asset() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Asset asset = (Asset) o;

        if (assetId != null ? !assetId.equals(asset.assetId) : asset.assetId != null) return false;
        if (assetTypeId != null ? !assetTypeId.equals(asset.assetTypeId) : asset.assetTypeId != null) return false;
        if (description != null ? !description.equals(asset.description) : asset.description != null) return false;
        if (connectedTrailer != null ? !connectedTrailer.equals(asset.connectedTrailer) : asset.connectedTrailer != null)
            return false;
        if (registrationNumber != null ? !registrationNumber.equals(asset.registrationNumber) : asset.registrationNumber != null)
            return false;
        if (siteId != null ? !siteId.equals(asset.siteId) : asset.siteId != null) return false;
        if (fuelType != null ? !fuelType.equals(asset.fuelType) : asset.fuelType != null) return false;
        if (targetFuelConsumption != null ? !targetFuelConsumption.equals(asset.targetFuelConsumption) : asset.targetFuelConsumption != null)
            return false;
        if (targetFuelConsumptionUnits != null ? !targetFuelConsumptionUnits.equals(asset.targetFuelConsumptionUnits) : asset.targetFuelConsumptionUnits != null)
            return false;
        if (targetHourlyFuelConsumption != null ? !targetHourlyFuelConsumption.equals(asset.targetHourlyFuelConsumption) : asset.targetHourlyFuelConsumption != null)
            return false;
        if (targetHourlyFuelConsumptionUnits != null ? !targetHourlyFuelConsumptionUnits.equals(asset.targetHourlyFuelConsumptionUnits) : asset.targetHourlyFuelConsumptionUnits != null)
            return false;
        if (fleetNumber != null ? !fleetNumber.equals(asset.fleetNumber) : asset.fleetNumber != null) return false;
        if (make != null ? !make.equals(asset.make) : asset.make != null) return false;
        if (model != null ? !model.equals(asset.model) : asset.model != null) return false;
        if (year != null ? !year.equals(asset.year) : asset.year != null) return false;
        if (vinNumber != null ? !vinNumber.equals(asset.vinNumber) : asset.vinNumber != null) return false;
        if (engineNumber != null ? !engineNumber.equals(asset.engineNumber) : asset.engineNumber != null) return false;
        if (fmVehicleId != null ? !fmVehicleId.equals(asset.fmVehicleId) : asset.fmVehicleId != null) return false;
        if (additionalMobileDevice != null ? !additionalMobileDevice.equals(asset.additionalMobileDevice) : asset.additionalMobileDevice != null)
            return false;
        if (notes != null ? !notes.equals(asset.notes) : asset.notes != null) return false;
        if (icon != null ? !icon.equals(asset.icon) : asset.icon != null) return false;
        if (iconColour != null ? !iconColour.equals(asset.iconColour) : asset.iconColour != null) return false;
        if (colour != null ? !colour.equals(asset.colour) : asset.colour != null) return false;
        if (assetImage != null ? !assetImage.equals(asset.assetImage) : asset.assetImage != null) return false;
        if (defaultImage != null ? !defaultImage.equals(asset.defaultImage) : asset.defaultImage != null) return false;
        if (assetImageUrl != null ? !assetImageUrl.equals(asset.assetImageUrl) : asset.assetImageUrl != null)
            return false;
        if (userState != null ? !userState.equals(asset.userState) : asset.userState != null) return false;
        if (createdBy != null ? !createdBy.equals(asset.createdBy) : asset.createdBy != null) return false;
        if (createdDate != null ? !createdDate.equals(asset.createdDate) : asset.createdDate != null) return false;
        if (odometer != null ? !odometer.equals(asset.odometer) : asset.odometer != null) return false;
        if (engineHours != null ? !engineHours.equals(asset.engineHours) : asset.engineHours != null) return false;
        return country != null ? country.equals(asset.country) : asset.country == null;
    }

    @Override
    public int hashCode() {
        int result = assetId != null ? assetId.hashCode() : 0;
        result = 31 * result + (assetTypeId != null ? assetTypeId.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (connectedTrailer != null ? connectedTrailer.hashCode() : 0);
        result = 31 * result + (registrationNumber != null ? registrationNumber.hashCode() : 0);
        result = 31 * result + (siteId != null ? siteId.hashCode() : 0);
        result = 31 * result + (fuelType != null ? fuelType.hashCode() : 0);
        result = 31 * result + (targetFuelConsumption != null ? targetFuelConsumption.hashCode() : 0);
        result = 31 * result + (targetFuelConsumptionUnits != null ? targetFuelConsumptionUnits.hashCode() : 0);
        result = 31 * result + (targetHourlyFuelConsumption != null ? targetHourlyFuelConsumption.hashCode() : 0);
        result = 31 * result + (targetHourlyFuelConsumptionUnits != null ? targetHourlyFuelConsumptionUnits.hashCode() : 0);
        result = 31 * result + (fleetNumber != null ? fleetNumber.hashCode() : 0);
        result = 31 * result + (make != null ? make.hashCode() : 0);
        result = 31 * result + (model != null ? model.hashCode() : 0);
        result = 31 * result + (year != null ? year.hashCode() : 0);
        result = 31 * result + (vinNumber != null ? vinNumber.hashCode() : 0);
        result = 31 * result + (engineNumber != null ? engineNumber.hashCode() : 0);
        result = 31 * result + (fmVehicleId != null ? fmVehicleId.hashCode() : 0);
        result = 31 * result + (additionalMobileDevice != null ? additionalMobileDevice.hashCode() : 0);
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        result = 31 * result + (iconColour != null ? iconColour.hashCode() : 0);
        result = 31 * result + (colour != null ? colour.hashCode() : 0);
        result = 31 * result + (assetImage != null ? assetImage.hashCode() : 0);
        result = 31 * result + (defaultImage != null ? defaultImage.hashCode() : 0);
        result = 31 * result + (assetImageUrl != null ? assetImageUrl.hashCode() : 0);
        result = 31 * result + (userState != null ? userState.hashCode() : 0);
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (odometer != null ? odometer.hashCode() : 0);
        result = 31 * result + (engineHours != null ? engineHours.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Asset{"
                + "assetId=" + assetId
                + ", assetTypeId=" + assetTypeId
                + ", description='" + description + '\''
                + ", connectedTrailer=" + connectedTrailer
                + ", registrationNumber='" + registrationNumber + '\''
                + ", siteId=" + siteId
                + ", fuelType='" + fuelType + '\''
                + ", targetFuelConsumption=" + targetFuelConsumption
                + ", targetFuelConsumptionUnits='" + targetFuelConsumptionUnits + '\''
                + ", targetHourlyFuelConsumption=" + targetHourlyFuelConsumption
                + ", targetHourlyFuelConsumptionUnits='" + targetHourlyFuelConsumptionUnits + '\''
                + ", fleetNumber='" + fleetNumber + '\''
                + ", make='" + make + '\''
                + ", model='" + model + '\''
                + ", year='" + year + '\''
                + ", vinNumber='" + vinNumber + '\''
                + ", engineNumber='" + engineNumber + '\''
                + ", fmVehicleId=" + fmVehicleId
                + ", additionalMobileDevice='" + additionalMobileDevice + '\''
                + ", notes='" + notes + '\''
                + ", icon='" + icon + '\''
                + ", iconColour='" + iconColour + '\''
                + ", colour='" + colour + '\''
                + ", assetImage='" + assetImage + '\''
                + ", defaultImage=" + defaultImage
                + ", assetImageUrl='" + assetImageUrl + '\''
                + ", userState='" + userState + '\''
                + ", createdBy='" + createdBy + '\''
                + ", createdDate='" + createdDate + '\''
                + ", odometer=" + odometer
                + ", engineHours='" + engineHours + '\''
                + ", country='" + country + '\''
                + "} " + super.toString();
    }


    public static final class AssetBuilder {
        private Long assetId;
        private Integer assetTypeId;
        private String description;
        private Boolean connectedTrailer;
        private String registrationNumber;
        private Long siteId;
        private String fuelType;
        private Double targetFuelConsumption;
        private String targetFuelConsumptionUnits;
        private Double targetHourlyFuelConsumption;
        private String targetHourlyFuelConsumptionUnits;
        private String fleetNumber;
        private String make;
        private String model;
        private String year;
        private String vinNumber;
        private String engineNumber;
        private Long fmVehicleId;
        private String additionalMobileDevice;
        private String notes;
        private String icon;
        private String iconColour;
        private String colour;
        private String assetImage;
        private Boolean defaultImage; //read only in swagger
        private String assetImageUrl;
        private String userState;
        private String createdBy;
        private Timestamp createdDate;
        private Double odometer;
        private String engineHours;
        private String country;

        public AssetBuilder() {
            /***  Default implementation ***/
        }

        public AssetBuilder setAssetId(Long assetId) {
            this.assetId = assetId;
            return this;
        }

        public AssetBuilder setAssetTypeId(Integer assetTypeId) {
            this.assetTypeId = assetTypeId;
            return this;
        }

        public AssetBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public AssetBuilder setConnectedTrailer(Boolean connectedTrailer) {
            this.connectedTrailer = connectedTrailer;
            return this;
        }

        public AssetBuilder setRegistrationNumber(String registrationNumber) {
            this.registrationNumber = registrationNumber;
            return this;
        }

        public AssetBuilder setSiteId(Long siteId) {
            this.siteId = siteId;
            return this;
        }

        public AssetBuilder setFuelType(String fuelType) {
            this.fuelType = fuelType;
            return this;
        }

        public AssetBuilder setTargetFuelConsumption(Double targetFuelConsumption) {
            this.targetFuelConsumption = targetFuelConsumption;
            return this;
        }

        public AssetBuilder setTargetFuelConsumptionUnits(String targetFuelConsumptionUnits) {
            this.targetFuelConsumptionUnits = targetFuelConsumptionUnits;
            return this;
        }

        public AssetBuilder setTargetHourlyFuelConsumption(Double targetHourlyFuelConsumption) {
            this.targetHourlyFuelConsumption = targetHourlyFuelConsumption;
            return this;
        }

        public AssetBuilder setTargetHourlyFuelConsumptionUnits(String targetHourlyFuelConsumptionUnits) {
            this.targetHourlyFuelConsumptionUnits = targetHourlyFuelConsumptionUnits;
            return this;
        }

        public AssetBuilder setFleetNumber(String fleetNumber) {
            this.fleetNumber = fleetNumber;
            return this;
        }

        public AssetBuilder setMake(String make) {
            this.make = make;
            return this;
        }

        public AssetBuilder setModel(String model) {
            this.model = model;
            return this;
        }

        public AssetBuilder setYear(String year) {
            this.year = year;
            return this;
        }

        public AssetBuilder setVinNumber(String vinNumber) {
            this.vinNumber = vinNumber;
            return this;
        }

        public AssetBuilder setEngineNumber(String engineNumber) {
            this.engineNumber = engineNumber;
            return this;
        }

        public AssetBuilder setFmVehicleId(Long fmVehicleId) {
            this.fmVehicleId = fmVehicleId;
            return this;
        }

        public AssetBuilder setAdditionalMobileDevice(String additionalMobileDevice) {
            this.additionalMobileDevice = additionalMobileDevice;
            return this;
        }

        public AssetBuilder setNotes(String notes) {
            this.notes = notes;
            return this;
        }

        public AssetBuilder setIcon(String icon) {
            this.icon = icon;
            return this;
        }

        public AssetBuilder setIconColour(String iconColour) {
            this.iconColour = iconColour;
            return this;
        }

        public AssetBuilder setColour(String colour) {
            this.colour = colour;
            return this;
        }

        public AssetBuilder setAssetImage(String assetImage) {
            this.assetImage = assetImage;
            return this;
        }

        public AssetBuilder setDefaultImage(Boolean defaultImage) {
            this.defaultImage = defaultImage;
            return this;
        }

        public AssetBuilder setAssetImageUrl(String assetImageUrl) {
            this.assetImageUrl = assetImageUrl;
            return this;
        }

        public AssetBuilder setUserState(String userState) {
            this.userState = userState;
            return this;
        }

        public AssetBuilder setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public AssetBuilder setCreatedDate(Timestamp createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public AssetBuilder setOdometer(Double odometer) {
            this.odometer = odometer;
            return this;
        }

        public AssetBuilder setEngineHours(String engineHours) {
            this.engineHours = engineHours;
            return this;
        }

        public AssetBuilder setCountry(String country) {
            this.country = country;
            return this;
        }

        /**
         * Build entity with specified parameters.
         * @return new entity.
         */
        public Asset build() {
            Asset asset = new Asset();
            asset.setAssetId(assetId);
            asset.setAssetTypeId(assetTypeId);
            asset.setDescription(description);
            asset.setConnectedTrailer(connectedTrailer);
            asset.setRegistrationNumber(registrationNumber);
            asset.setSiteId(siteId);
            asset.setFuelType(fuelType);
            asset.setTargetFuelConsumption(targetFuelConsumption);
            asset.setTargetFuelConsumptionUnits(targetFuelConsumptionUnits);
            asset.setTargetHourlyFuelConsumption(targetHourlyFuelConsumption);
            asset.setTargetHourlyFuelConsumptionUnits(targetHourlyFuelConsumptionUnits);
            asset.setFleetNumber(fleetNumber);
            asset.setMake(make);
            asset.setModel(model);
            asset.setYear(year);
            asset.setVinNumber(vinNumber);
            asset.setEngineNumber(engineNumber);
            asset.setFmVehicleId(fmVehicleId);
            asset.setAdditionalMobileDevice(additionalMobileDevice);
            asset.setNotes(notes);
            asset.setIcon(icon);
            asset.setIconColour(iconColour);
            asset.setColour(colour);
            asset.setAssetImage(assetImage);
            asset.setDefaultImage(defaultImage);
            asset.setAssetImageUrl(assetImageUrl);
            asset.setUserState(userState);
            asset.setCreatedBy(createdBy);
            asset.setCreatedDate(createdDate);
            asset.setOdometer(odometer);
            asset.setEngineHours(engineHours);
            asset.setCountry(country);
            return asset;
        }
    }
}
