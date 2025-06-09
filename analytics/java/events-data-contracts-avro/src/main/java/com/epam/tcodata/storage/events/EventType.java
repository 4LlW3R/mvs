package com.epam.tcodata.storage.events;

/**
 * Decodes the event type id for avro data contracts.
 */
public enum EventType {

    /**
     * Indicates basic overtaking event detected by the corresponding algorithm.
     */
    OVERTAKING(1),

    /**
     * Indicates enriched overtaking event with violation policies applied.
     */
    OVERTAKING_WITH_VIOLATIONS(2),

    VEHICLES_IN_PROXIMITY_REQUEST(3),

    VEHICLES_IN_PROXIMITY_RESPONSE(4),

    ROAD_CONDITION_VIOLATION_EVENT(5);

    private final int eventTypeId;


    EventType(int eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public int getEventTypeId() {
        return eventTypeId;
    }
}
