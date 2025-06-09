package com.epam.tcodata.models.mix.fact;


import com.epam.tcodata.models.mix.Entity;
import com.epam.tcodata.models.nested.MediaUrls;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * Maps to SOAP RecordedEvent.
 */
public class Event extends Entity {

    private static final long serialVersionUID = 4623220163695884614L;

    private Long assetId;
    private Long driverId;
    private Long eventId;
    private Long eventTypeId;
    private String eventCategory;
    private Timestamp startDateTime;
    private Double startOdometerKilometres;
    private Position startPosition;
    private Timestamp endDateTime;
    private Double endOdometerKilometres;
    private Position endPosition;
    private Double value;
    private String valueType;
    private String valueUnits;
    private Integer totalTimeSeconds;
    @JsonProperty("TotalOccurances")
    private Long totalOccurrences;
    private MediaUrls mediaUrls;
    private Long locationId;
    private Double speedLimit;

    public Event() {
        /***  Default implementation ***/
    }

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(Long eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public Timestamp getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Timestamp startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Double getStartOdometerKilometres() {
        return startOdometerKilometres;
    }

    public void setStartOdometerKilometres(Double startOdometerKilometres) {
        this.startOdometerKilometres = startOdometerKilometres;
    }

    public Position getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Position startPosition) {
        this.startPosition = startPosition;
    }

    public Timestamp getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Timestamp endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Double getEndOdometerKilometres() {
        return endOdometerKilometres;
    }

    public void setEndOdometerKilometres(Double endOdometerKilometres) {
        this.endOdometerKilometres = endOdometerKilometres;
    }

    public Position getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(Position endPosition) {
        this.endPosition = endPosition;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getValueUnits() {
        return valueUnits;
    }

    public void setValueUnits(String valueUnits) {
        this.valueUnits = valueUnits;
    }

    public Integer getTotalTimeSeconds() {
        return totalTimeSeconds;
    }

    public void setTotalTimeSeconds(Integer totalTimeSeconds) {
        this.totalTimeSeconds = totalTimeSeconds;
    }

    public Long getTotalOccurrences() {
        return totalOccurrences;
    }

    public void setTotalOccurrences(Long totalOccurrences) {
        this.totalOccurrences = totalOccurrences;
    }

    public MediaUrls getMediaUrls() {
        return mediaUrls;
    }

    public void setMediaUrls(MediaUrls mediaUrls) {
        this.mediaUrls = mediaUrls;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Double getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(Double speedLimit) {
        this.speedLimit = speedLimit;
    }

    @Override
    public String toString() {
        return "Event{"
                + "assetId=" + assetId
                + ", driverId=" + driverId
                + ", eventId=" + eventId
                + ", eventTypeId=" + eventTypeId
                + ", eventCategory='" + eventCategory + '\''
                + ", startDateTime=" + startDateTime
                + ", startOdometerKilometres=" + startOdometerKilometres
                + ", startPosition=" + startPosition
                + ", endDateTime=" + endDateTime
                + ", endOdometerKilometres=" + endOdometerKilometres
                + ", endPosition=" + endPosition
                + ", value=" + value
                + ", valueType='" + valueType + '\''
                + ", valueUnits='" + valueUnits + '\''
                + ", totalTimeSeconds=" + totalTimeSeconds
                + ", totalOccurrences=" + totalOccurrences
                + ", mediaUrls=" + mediaUrls
                + ", locationId=" + locationId
                + ", speedLimit=" + speedLimit
                + "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(assetId, event.assetId)
                && Objects.equals(driverId, event.driverId)
                && Objects.equals(eventId, event.eventId)
                && Objects.equals(eventTypeId, event.eventTypeId)
                && Objects.equals(eventCategory, event.eventCategory)
                && Objects.equals(startDateTime, event.startDateTime)
                && Objects.equals(startOdometerKilometres, event.startOdometerKilometres)
                && Objects.equals(startPosition, event.startPosition)
                && Objects.equals(endDateTime, event.endDateTime)
                && Objects.equals(endOdometerKilometres, event.endOdometerKilometres)
                && Objects.equals(endPosition, event.endPosition)
                && Objects.equals(value, event.value)
                && Objects.equals(valueType, event.valueType)
                && Objects.equals(valueUnits, event.valueUnits)
                && Objects.equals(totalTimeSeconds, event.totalTimeSeconds)
                && Objects.equals(totalOccurrences, event.totalOccurrences)
                && Objects.equals(mediaUrls, event.mediaUrls)
                && Objects.equals(locationId, event.locationId)
                && Objects.equals(speedLimit, event.speedLimit);
    }

    @Override
    public int hashCode() {

        return Objects.hash(assetId, driverId, eventId, eventTypeId, eventCategory, startDateTime,
                startOdometerKilometres, startPosition, endDateTime, endOdometerKilometres, endPosition, value,
                valueType, valueUnits, totalTimeSeconds, totalOccurrences, mediaUrls, locationId, speedLimit);
    }


    public static final class EventBuilder {
        private Long assetId;
        private Long driverId;
        private Long eventId;
        private Long eventTypeId;
        private String eventCategory;
        private Timestamp startDateTime;
        private Double startOdometerKilometres;
        private Position startPosition;
        private Timestamp endDateTime;
        private Double endOdometerKilometres;
        private Position endPosition;
        private Double value;
        private String valueType;
        private String valueUnits;
        private Integer totalTimeSeconds;
        private Long totalOccurrences;
        private MediaUrls mediaUrls;
        private Long locationId;
        private Double speedLimit;

        public EventBuilder() {
            /***  Default implementation ***/
        }

        public EventBuilder setAssetId(Long assetId) {
            this.assetId = assetId;
            return this;
        }

        public EventBuilder setDriverId(Long driverId) {
            this.driverId = driverId;
            return this;
        }

        public EventBuilder setEventId(Long eventId) {
            this.eventId = eventId;
            return this;
        }

        public EventBuilder setEventTypeId(Long eventTypeId) {
            this.eventTypeId = eventTypeId;
            return this;
        }

        public EventBuilder setEventCategory(String eventCategory) {
            this.eventCategory = eventCategory;
            return this;
        }

        public EventBuilder setStartDateTime(Timestamp startDateTime) {
            this.startDateTime = startDateTime;
            return this;
        }

        public EventBuilder setStartOdometerKilometres(Double startOdometerKilometres) {
            this.startOdometerKilometres = startOdometerKilometres;
            return this;
        }

        public EventBuilder setStartPosition(Position startPosition) {
            this.startPosition = startPosition;
            return this;
        }

        public EventBuilder setEndDateTime(Timestamp endDateTime) {
            this.endDateTime = endDateTime;
            return this;
        }

        public EventBuilder setEndOdometerKilometres(Double endOdometerKilometres) {
            this.endOdometerKilometres = endOdometerKilometres;
            return this;
        }

        public EventBuilder setEndPosition(Position endPosition) {
            this.endPosition = endPosition;
            return this;
        }

        public EventBuilder setValue(Double value) {
            this.value = value;
            return this;
        }

        public EventBuilder setValueType(String valueType) {
            this.valueType = valueType;
            return this;
        }

        public EventBuilder setValueUnits(String valueUnits) {
            this.valueUnits = valueUnits;
            return this;
        }

        public EventBuilder setTotalTimeSeconds(Integer totalTimeSeconds) {
            this.totalTimeSeconds = totalTimeSeconds;
            return this;
        }

        public EventBuilder setTotalOccurrences(Long totalOccurrences) {
            this.totalOccurrences = totalOccurrences;
            return this;
        }

        public EventBuilder setMediaUrls(MediaUrls mediaUrls) {
            this.mediaUrls = mediaUrls;
            return this;
        }

        public EventBuilder setLocationId(Long locationId) {
            this.locationId = locationId;
            return this;
        }

        public EventBuilder setSpeedLimit(Double speedLimit) {
            this.speedLimit = speedLimit;
            return this;
        }

        /**
         * Build entity with specified parameters.
         * @return new entity.
         */
        public Event build() {
            Event event = new Event();
            event.setAssetId(assetId);
            event.setDriverId(driverId);
            event.setEventId(eventId);
            event.setEventTypeId(eventTypeId);
            event.setEventCategory(eventCategory);
            event.setStartDateTime(startDateTime);
            event.setStartOdometerKilometres(startOdometerKilometres);
            event.setStartPosition(startPosition);
            event.setEndDateTime(endDateTime);
            event.setEndOdometerKilometres(endOdometerKilometres);
            event.setEndPosition(endPosition);
            event.setValue(value);
            event.setValueType(valueType);
            event.setValueUnits(valueUnits);
            event.setTotalTimeSeconds(totalTimeSeconds);
            event.setTotalOccurrences(totalOccurrences);
            event.setMediaUrls(mediaUrls);
            event.setLocationId(locationId);
            event.setSpeedLimit(speedLimit);
            return event;
        }
    }
}

