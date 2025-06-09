package com.epam.tcodata.models.datalake.prepared.dimension;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.prepared.PreparedEntity;

import java.sql.Timestamp;

@SuppressWarnings("CPD-START")
public class PreparedDriver extends PreparedEntity {

    private static final long serialVersionUID = -5475969248559094015L;

    public static class Fields {
        public static final String ORGANIZATION_DURABLE_KEY = "organization_durable_key";
        public static final String EXTERNAL_ID = "external_id";
        public static final String PERSISTED_DATE_UTC = "persisted_date_utc";
        public static final String ORG_GROUP_DURABLE_KEY = "org_group_durable_key";
        public static final String NAME = "name";
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

    @ColumnName(Fields.ORGANIZATION_DURABLE_KEY)
    private String organizationDurableKey;
    @ColumnName(Fields.EXTERNAL_ID)
    private Long externalId;
    @ColumnName(Fields.PERSISTED_DATE_UTC)
    private Timestamp persistedDateUtc;
    @ColumnName(Fields.ORG_GROUP_DURABLE_KEY)
    private String orgGroupDurableKey;
    @ColumnName(Fields.NAME)
    private String name;
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

    public PreparedDriver() {
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

    public String getOrgGroupDurableKey() {
        return orgGroupDurableKey;
    }

    public void setOrgGroupDurableKey(String orgGroupDurableKey) {
        this.orgGroupDurableKey = orgGroupDurableKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
