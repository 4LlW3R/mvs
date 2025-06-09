package com.epam.tcodata.event.validator.logic;

import java.io.Serializable;


public class SpeedValues implements Serializable {

    private static final long serialVersionUID = 5191738069270938368L;


    /** Velocity from gps position. */
    private int v;
    /** Speed from cost center. */
    private int s;
    /** Maximum allowed speed. */
    private int x;
    /** Event type. */
    private EventType eventType;


    /**
     * Create Speed values.
     * @param v velocity from gps position.
     * @param s speed from cost center.
     * @param description specified description.
     */
    public SpeedValues(int v, int s, String description) {
        this.v = v;
        this.s = s;
        this.initEventType(description);
    }

    private void initEventType(String description) {
        this.eventType = EventType.getByDescription(description);

        if (this.eventType == EventType.OVERSPEED) {
            this.x = EventAnalyzer.parseSpeedValue(description);
        }
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }


}
