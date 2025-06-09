package com.epam.tcodata.models.datalake.prepared.dimension;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.prepared.PreparedEntity;

import java.sql.Timestamp;

@SuppressWarnings("CPD-START")
public class PreparedEventDescription extends PreparedEntity {

    private static final long serialVersionUID = 306326460061417136L;

    public static class Fields {
        public static final String ORGANIZATION_DURABLE_KEY = "organization_durable_key";
        public static final String EXTERNAL_ID = "external_id";
        public static final String PERSISTED_DATE_UTC = "persisted_date_utc";
        public static final String DESCRIPTION = "description";
        public static final String EVENT_TYPE = "event_type";
        public static final String DISPLAY_UNITS = "display_units";
        public static final String FORMAT_TYPE = "format_type";
        public static final String VALUE_NAME = "value_name";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.ORGANIZATION_DURABLE_KEY)
    private String organizationDurableKey;
    @ColumnName(Fields.EXTERNAL_ID)
    private Long externalId;
    @ColumnName(Fields.PERSISTED_DATE_UTC)
    private Timestamp persistedDateUtc;
    @ColumnName(Fields.DESCRIPTION)
    private String description;
    @ColumnName(Fields.EVENT_TYPE)
    private String eventType;
    @ColumnName(Fields.DISPLAY_UNITS)
    private String displayUnits;
    @ColumnName(Fields.FORMAT_TYPE)
    private String formatType;
    @ColumnName(Fields.VALUE_NAME)
    private String valueName;

    public PreparedEventDescription() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getDisplayUnits() {
        return displayUnits;
    }

    public void setDisplayUnits(String displayUnits) {
        this.displayUnits = displayUnits;
    }

    public String getFormatType() {
        return formatType;
    }

    public void setFormatType(String formatType) {
        this.formatType = formatType;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }
}
