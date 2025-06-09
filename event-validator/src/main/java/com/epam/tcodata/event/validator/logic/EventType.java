package com.epam.tcodata.event.validator.logic;


public enum EventType {

    /** Vehicle driven without headlights. */
    VEHICLE_DRIVEN_WITHOUT_HEADLIGHTS,
    /** Seatbelt not used. */
    SEATBELT_NOT_USED,
    /** Overspeed. */
    OVERSPEED,
    /** Harsh acceleration. */
    HARSH_ACCELERATION,
    /** Harsh braking. */
    HARSH_BRAKING,
    /** Non-typed. */
    NONE;

    EventType() {
    }

    /**
     * Get EventType by description.
     */
    public static EventType getByDescription(String description) {
        if (description == null) {
            return EventType.NONE;
        }

        if (EventAnalyzer.isOverspeed(description)) {
            return EventType.OVERSPEED;

        } else if (EventAnalyzer.isSeatBeltNotUsed(description)) {
            return EventType.SEATBELT_NOT_USED;

        } else if (EventAnalyzer.isVehicleDrivenWithoutHeadlights(description)) {
            return EventType.VEHICLE_DRIVEN_WITHOUT_HEADLIGHTS;

        } else if (EventAnalyzer.isHarshBraking(description)) {
            return EventType.HARSH_BRAKING;

        } else if (EventAnalyzer.isHarshAcceleration(description)) {
            return EventType.HARSH_ACCELERATION;

        } else {
            return EventType.NONE;
        }
    }

}
