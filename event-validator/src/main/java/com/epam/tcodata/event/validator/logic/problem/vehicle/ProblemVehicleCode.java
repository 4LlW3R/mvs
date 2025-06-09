package com.epam.tcodata.event.validator.logic.problem.vehicle;


public final class ProblemVehicleCode {
    private ProblemVehicleCode(){}

    /** Valid marker. */
    public static final int VALID = 1;
    /** GPS problem marker. */
    public static final int GPS_PROBLEM = 2;
    /** Speed sender problem marker. */
    public static final int SPEED_SENDER_PROBLEM = 3;
    /** No GPS data available. */
    public static final int NO_GPS_DATA_AVAILABLE = 4;
    /** IVMS detects several duplicate events (within 3 sec). */
    public static final int DUPLICATE_EVENTS = 5;
    /** Braking rate value improbably high. */
    public static final int BRACKING_RATE_VALUE_HIGH = 6;
    /** Improbable harsh-braking rate value compared to GPS velocity. */
    public static final int BRACKING_RATE_VALUE_HIGH_COMPARE_WITH_GPS = 7;
    /** Acceleration rate value improbably high. */
    public static final int ACCELERATION_RATE_HIGH = 8;
    /** Improbable harsh-acceleration rate value compared to GPS velocity. */
    public static final int ACCELERATION_RATE_COMPARE_WITH_GPS = 9;
    /** Speed sensor spike. */
    public static final int SPEED_SENSOR_SPIKE = 10;
    /** Invalid speed value. */
    public static final int INVALID_SPEED_VALUE = 11;
    /** Velocity issue. */
    public static final int VELOCITY_ISSUE = 12; // to do name assigned by developers

}
