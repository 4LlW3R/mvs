package com.epam.tcodata.models.datalake.raw.dimension;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.raw.RawEntity;

import java.util.Objects;

@SuppressWarnings("CPD-START")
public class RawDriver extends RawEntity {

    private static final long serialVersionUID = -1756991809646598765L;

    public static class Fields {
        public static final String SITE_ID = "site_id";
        public static final String DRIVER_ID = "driver_id";
        public static final String NAME = "name";
        public static final String IMAGE_URI = "image_uri";
        public static final String FM_DRIVER_ID = "fm_driver_id";
        public static final String EMPLOYEE_NUMBER = "employee_number";
        public static final String SYSTEM_DRIVER = "system_driver";
        public static final String MOBILE_NUMBER = "mobile_number";
        public static final String EMAIL = "email";
        public static final String EXTENDED_DRIVER_ID = "extended_driver_id";
        public static final String EXTENDED_DRIVER_ID_TYPE = "extended_driver_id_type";
        public static final String COUNTRY = "country";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.SITE_ID)
    private Long siteId;
    @ColumnName(Fields.DRIVER_ID)
    private Long driverId;
    @ColumnName(Fields.NAME)
    private String name;
    @ColumnName(Fields.IMAGE_URI)
    private String imageUri;
    @ColumnName(Fields.FM_DRIVER_ID)
    private Long fmDriverId;
    @ColumnName(Fields.EMPLOYEE_NUMBER)
    private String employeeNumber;
    @ColumnName(Fields.SYSTEM_DRIVER)
    private Boolean systemDriver;
    @ColumnName(Fields.MOBILE_NUMBER)
    private String mobileNumber;
    @ColumnName(Fields.EMAIL)
    private String email;
    @ColumnName(Fields.EXTENDED_DRIVER_ID)
    private String extendedDriverId;
    @ColumnName(Fields.EXTENDED_DRIVER_ID_TYPE)
    private String extendedDriverIdType;
    @ColumnName(Fields.COUNTRY)
    private String country;

    public RawDriver() {
        /***  Default implementation ***/
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RawDriver driver = (RawDriver) o;
        return Objects.equals(siteId, driver.siteId)
                && Objects.equals(driverId, driver.driverId)
                && Objects.equals(name, driver.name)
                && Objects.equals(imageUri, driver.imageUri)
                && Objects.equals(fmDriverId, driver.fmDriverId)
                && Objects.equals(employeeNumber, driver.employeeNumber)
                && Objects.equals(systemDriver, driver.systemDriver)
                && Objects.equals(mobileNumber, driver.mobileNumber)
                && Objects.equals(email, driver.email)
                && Objects.equals(extendedDriverId, driver.extendedDriverId)
                && Objects.equals(extendedDriverIdType, driver.extendedDriverIdType)
                && Objects.equals(country, driver.country);
    }

    @Override
    public int hashCode() {

        return Objects.hash(siteId, driverId, name, imageUri, fmDriverId, employeeNumber, systemDriver, mobileNumber,
                email, extendedDriverId, extendedDriverIdType, country);
    }
}
