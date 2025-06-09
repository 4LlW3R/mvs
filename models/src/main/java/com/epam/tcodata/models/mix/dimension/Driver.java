package com.epam.tcodata.models.mix.dimension;

import com.epam.tcodata.models.mix.Entity;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Maps to SOAP Driver.
 */
public class Driver extends Entity {

    private static final long serialVersionUID = -4493506038033428807L;

    private Long siteId;
    private Long driverId;
    private String name;
    private String imageUri;
    private Long fmDriverId;
    private String employeeNumber;
    @JsonProperty("IsSystemDriver")
    private Boolean systemDriver; //read only in swagger
    private String mobileNumber;
    private String email;
    private String extendedDriverId;
    private String extendedDriverIdType;
    private String country;
    private List<DriverAdditionalDetailField> additionalDetailFields;

    public Driver() {
        this.additionalDetailFields = new ArrayList<>();
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public Long getFmDriverId() {
        return fmDriverId;
    }

    public void setFmDriverId(Long fmDriverId) {
        this.fmDriverId = fmDriverId;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public Boolean getSystemDriver() {
        return systemDriver;
    }

    public void setSystemDriver(Boolean systemDriver) {
        this.systemDriver = systemDriver;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExtendedDriverId() {
        return extendedDriverId;
    }

    public void setExtendedDriverId(String extendedDriverId) {
        this.extendedDriverId = extendedDriverId;
    }

    public String getExtendedDriverIdType() {
        return extendedDriverIdType;
    }

    public void setExtendedDriverIdType(String extendedDriverIdType) {
        this.extendedDriverIdType = extendedDriverIdType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<DriverAdditionalDetailField> getAdditionalDetailFields() {
        return additionalDetailFields;
    }

    public void setAdditionalDetailFields(List<DriverAdditionalDetailField> additionalDetailFields) {
        this.additionalDetailFields.clear();
        this.additionalDetailFields.addAll(additionalDetailFields);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Driver driver = (Driver) o;

        if (siteId != null ? !siteId.equals(driver.siteId) : driver.siteId != null) return false;
        if (driverId != null ? !driverId.equals(driver.driverId) : driver.driverId != null) return false;
        if (name != null ? !name.equals(driver.name) : driver.name != null) return false;
        if (imageUri != null ? !imageUri.equals(driver.imageUri) : driver.imageUri != null) return false;
        if (fmDriverId != null ? !fmDriverId.equals(driver.fmDriverId) : driver.fmDriverId != null) return false;
        if (employeeNumber != null ? !employeeNumber.equals(driver.employeeNumber) : driver.employeeNumber != null)
            return false;
        if (systemDriver != null ? !systemDriver.equals(driver.systemDriver) : driver.systemDriver != null)
            return false;
        if (mobileNumber != null ? !mobileNumber.equals(driver.mobileNumber) : driver.mobileNumber != null)
            return false;
        if (email != null ? !email.equals(driver.email) : driver.email != null) return false;
        if (extendedDriverId != null ? !extendedDriverId.equals(driver.extendedDriverId) : driver.extendedDriverId != null)
            return false;
        if (extendedDriverIdType != null ? !extendedDriverIdType.equals(driver.extendedDriverIdType) : driver.extendedDriverIdType != null)
            return false;
        if (country != null ? country.equals(driver.country) : driver.country != null)
            return false;
        return additionalDetailFields != null ? additionalDetailFields.equals(driver.additionalDetailFields) : driver.additionalDetailFields == null;
    }

    @Override
    public int hashCode() {
        int result = siteId != null ? siteId.hashCode() : 0;
        result = 31 * result + (driverId != null ? driverId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (imageUri != null ? imageUri.hashCode() : 0);
        result = 31 * result + (fmDriverId != null ? fmDriverId.hashCode() : 0);
        result = 31 * result + (employeeNumber != null ? employeeNumber.hashCode() : 0);
        result = 31 * result + (systemDriver != null ? systemDriver.hashCode() : 0);
        result = 31 * result + (mobileNumber != null ? mobileNumber.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (extendedDriverId != null ? extendedDriverId.hashCode() : 0);
        result = 31 * result + (extendedDriverIdType != null ? extendedDriverIdType.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (additionalDetailFields != null ? additionalDetailFields.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Driver{"
                + "siteId=" + siteId
                + ", driverId=" + driverId
                + ", name='" + name + '\''
                + ", imageUri='" + imageUri + '\''
                + ", fmDriverId=" + fmDriverId
                + ", employeeNumber='" + employeeNumber + '\''
                + ", systemDriver=" + systemDriver
                + ", mobileNumber='" + mobileNumber + '\''
                + ", email='" + email + '\''
                + ", extendedDriverId='" + extendedDriverId + '\''
                + ", extendedDriverIdType=" + extendedDriverIdType
                + ", country='" + country + '\''
                + ", additionalDetailFields='" + additionalDetailFields + '\''
                + "} " + super.toString();
    }


    public static final class DriverBuilder {
        private Long siteId;
        private Long driverId;
        private String name;
        private String imageUri;
        private Long fmDriverId;
        private String employeeNumber;
        private Boolean systemDriver; //read only in swagger
        private String mobileNumber;
        private String email;
        private String extendedDriverId;
        private String extendedDriverIdType;
        private String country;
        private List<DriverAdditionalDetailField> additionalDetailFields;

        public DriverBuilder() {
            /***  Default implementation ***/
        }

        public DriverBuilder setSiteId(Long siteId) {
            this.siteId = siteId;
            return this;
        }

        public DriverBuilder setDriverId(Long driverId) {
            this.driverId = driverId;
            return this;
        }

        public DriverBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public DriverBuilder setImageUri(String imageUri) {
            this.imageUri = imageUri;
            return this;
        }

        public DriverBuilder setFmDriverId(Long fmDriverId) {
            this.fmDriverId = fmDriverId;
            return this;
        }

        public DriverBuilder setEmployeeNumber(String employeeNumber) {
            this.employeeNumber = employeeNumber;
            return this;
        }

        public DriverBuilder setSystemDriver(Boolean systemDriver) {
            this.systemDriver = systemDriver;
            return this;
        }

        public DriverBuilder setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
            return this;
        }

        public DriverBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public DriverBuilder setExtendedDriverId(String extendedDriverId) {
            this.extendedDriverId = extendedDriverId;
            return this;
        }

        public DriverBuilder setExtendedDriverIdType(String extendedDriverIdType) {
            this.extendedDriverIdType = extendedDriverIdType;
            return this;
        }

        public DriverBuilder setCountry(String country) {
            this.country = country;
            return this;
        }

        public DriverBuilder setAdditionalDetailFields(List<DriverAdditionalDetailField> additionalDetailFields) {
            this.additionalDetailFields = additionalDetailFields;
            return this;
        }

        /**
         * Build entity with specified parameters.
         * @return new entity.
         */
        public Driver build() {
            Driver driver = new Driver();
            driver.setSiteId(siteId);
            driver.setDriverId(driverId);
            driver.setName(name);
            driver.setImageUri(imageUri);
            driver.setFmDriverId(fmDriverId);
            driver.setEmployeeNumber(employeeNumber);
            driver.setSystemDriver(systemDriver);
            driver.setMobileNumber(mobileNumber);
            driver.setEmail(email);
            driver.setExtendedDriverId(extendedDriverId);
            driver.setExtendedDriverIdType(extendedDriverIdType);
            driver.setCountry(country);
            driver.setAdditionalDetailFields(additionalDetailFields == null ? new ArrayList<>() : additionalDetailFields);
            return driver;
        }
    }
}
