package com.epam.tcodata.models.datalake.prepared.fact;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.prepared.PreparedEntity;

import java.sql.Timestamp;

@SuppressWarnings("CPD-START")
public class PreparedTacho extends PreparedEntity {

    private static final long serialVersionUID = 3166513504727589157L;

    public static class Fields {
        public static final String ORGANIZATION_DURABLE_KEY = "organization_durable_key";
        public static final String EXTERNAL_ID = "external_id";
        public static final String PERSISTED_DATE_UTC = "persisted_date_utc";
        public static final String ASSET_ID = "asset_id";
        public static final String DURABLE_ASSET_ID = "durable_asset_id";
        public static final String LINE_NAME = "line_name";
        public static final String VALUE_DATE_TIME = "value_date_time";
        public static final String VALUE = "value";
        public static final String START_DATE_TIME = "start_date_time";
        public static final String END_DATE_TIME = "end_date_time";
        public static final String YEAR = "year";
        public static final String WEEK_NUMBER = "week_number";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.ORGANIZATION_DURABLE_KEY)
    private String organizationDurableKey;
    @ColumnName(Fields.EXTERNAL_ID)
    private Long externalId;
    @ColumnName(Fields.PERSISTED_DATE_UTC)
    private Timestamp persistedDateUtc;
    @ColumnName(Fields.ASSET_ID)
    private Long assetId;
    @ColumnName(Fields.DURABLE_ASSET_ID)
    private String durableAssetId;
    @ColumnName(Fields.LINE_NAME)
    private String lineName;
    @ColumnName(Fields.VALUE_DATE_TIME)
    private Timestamp valueDateTime;
    @ColumnName(Fields.VALUE)
    private Integer value;
    @ColumnName(Fields.START_DATE_TIME)
    private Timestamp startDateTime;
    @ColumnName(Fields.END_DATE_TIME)
    private Timestamp endDateTime;
    @ColumnName(Fields.YEAR)
    private Integer year;
    @ColumnName(Fields.WEEK_NUMBER)
    private Integer weekNumber;

    public PreparedTacho() {
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

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public String getDurableAssetId() {
        return durableAssetId;
    }

    public void setDurableAssetId(String durableAssetId) {
        this.durableAssetId = durableAssetId;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public Timestamp getValueDateTime() {
        return valueDateTime;
    }

    public void setValueDateTime(Timestamp valueDateTime) {
        this.valueDateTime = valueDateTime;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Timestamp getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Timestamp startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Timestamp getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Timestamp endDateTime) {
        this.endDateTime = endDateTime;
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
