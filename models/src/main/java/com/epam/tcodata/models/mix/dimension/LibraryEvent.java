package com.epam.tcodata.models.mix.dimension;

import com.epam.tcodata.models.mix.Entity;

/**
 * Maps to SOAP EventDescription.
 */
public class LibraryEvent extends Entity {

    private static final long serialVersionUID = -3015959507508794114L;

    private String description;
    private Long eventTypeId;
    private String eventType;
    private String displayUnits;
    private String formatType;
    private String valueName;

    public LibraryEvent() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LibraryEvent that = (LibraryEvent) o;

        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (eventTypeId != null ? !eventTypeId.equals(that.eventTypeId) : that.eventTypeId != null) return false;
        if (eventType != null ? !eventType.equals(that.eventType) : that.eventType != null) return false;
        if (displayUnits != null ? !displayUnits.equals(that.displayUnits) : that.displayUnits != null) return false;
        if (formatType != null ? !formatType.equals(that.formatType) : that.formatType != null) return false;
        return valueName != null ? valueName.equals(that.valueName) : that.valueName == null;
    }

    @Override
    public int hashCode() {
        int result = description != null ? description.hashCode() : 0;
        result = 31 * result + (eventTypeId != null ? eventTypeId.hashCode() : 0);
        result = 31 * result + (eventType != null ? eventType.hashCode() : 0);
        result = 31 * result + (displayUnits != null ? displayUnits.hashCode() : 0);
        result = 31 * result + (formatType != null ? formatType.hashCode() : 0);
        result = 31 * result + (valueName != null ? valueName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LibraryEvent{"
                + "description='" + description + '\''
                + ", eventTypeId=" + eventTypeId
                + ", eventType='" + eventType + '\''
                + ", displayUnits='" + displayUnits + '\''
                + ", formatType='" + formatType + '\''
                + ", valueName='" + valueName + '\''
                + "} " + super.toString();
    }


    public static final class LibraryEventBuilder {
        private String description;
        private Long eventTypeId;
        private String eventType;
        private String displayUnits;
        private String formatType;
        private String valueName;

        public LibraryEventBuilder() {
            /***  Default implementation ***/
        }

        public LibraryEventBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public LibraryEventBuilder setEventTypeId(Long eventTypeId) {
            this.eventTypeId = eventTypeId;
            return this;
        }

        public LibraryEventBuilder setEventType(String eventType) {
            this.eventType = eventType;
            return this;
        }

        public LibraryEventBuilder setDisplayUnits(String displayUnits) {
            this.displayUnits = displayUnits;
            return this;
        }

        public LibraryEventBuilder setFormatType(String formatType) {
            this.formatType = formatType;
            return this;
        }

        public LibraryEventBuilder setValueName(String valueName) {
            this.valueName = valueName;
            return this;
        }

        /**
         * Build entity with specified parameters.
         * @return new entity.
         */
        public LibraryEvent build() {
            LibraryEvent libraryEvent = new LibraryEvent();
            libraryEvent.setDescription(description);
            libraryEvent.setEventTypeId(eventTypeId);
            libraryEvent.setEventType(eventType);
            libraryEvent.setDisplayUnits(displayUnits);
            libraryEvent.setFormatType(formatType);
            libraryEvent.setValueName(valueName);
            return libraryEvent;
        }
    }
}
