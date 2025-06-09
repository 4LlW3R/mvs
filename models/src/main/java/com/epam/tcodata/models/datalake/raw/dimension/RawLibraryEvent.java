package com.epam.tcodata.models.datalake.raw.dimension;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.raw.RawEntity;

@SuppressWarnings("CPD-START")
public class RawLibraryEvent extends RawEntity {

    private static final long serialVersionUID = 4235245329602648447L;

    public static class Fields {
        public static final String DESCRIPTION = "description";
        public static final String EVENT_TYPE_ID = "event_type_id";
        public static final String EVENT_TYPE = "event_type";
        public static final String DISPLAY_UNITS = "display_units";
        public static final String FORMAT_TYPE = "format_type";
        public static final String VALUE_NAME = "value_name";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.DESCRIPTION)
    private String description;
    @ColumnName(Fields.EVENT_TYPE_ID)
    private Long eventTypeId;
    @ColumnName(Fields.EVENT_TYPE)
    private String eventType;
    @ColumnName(Fields.DISPLAY_UNITS)
    private String displayUnits;
    @ColumnName(Fields.FORMAT_TYPE)
    private String formatType;
    @ColumnName(Fields.VALUE_NAME)
    private String valueName;

    public RawLibraryEvent() {
        /***  Default implementation ***/
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(Long eventTypeId) {
        this.eventTypeId = eventTypeId;
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
