package com.epam.tcodata.event.validator.logic;


import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;
import java.util.List;

public final class EventAnalyzer {
    private EventAnalyzer(){}

    /** Default speed. */
    private static final int DEFAULT_SPEED = 90;
    /** Seat belt not used trigger. */
    private static final int SEATBELT_NOT_USED_SPEED_TRIGGER = 5;
    /** Standard speed limits. */
    private static final List<Integer> STANDARD_SPEED_LIMITS = Arrays.asList(20, 30, 40, 50, 60, 70, 80, DEFAULT_SPEED);


    /**
     * Check description for overspeeding.
     * @param description description as string.
     * @return <code>true</code> - overspeed, <code>false</code> - non-overspeed.
     */
    public static boolean isOverspeed(String description) {
        String lowerCase = description.toLowerCase();
        return lowerCase.contains("over speed") || lowerCase.contains("overspeed");
    }

    /**
     * Check description for harsh braking.
     * @param description description as string.
     * @return <code>true</code> - harsh braking, <code>false</code> - not harsh braking.
     */
    public static boolean isHarshBraking(String description) {
        String lowerCase = description.toLowerCase();
        return lowerCase.contains("harsh braking") || lowerCase.contains("harshbraking");
    }

    /**
     * Check description for harsh acceleration.
     * @param description description as string.
     * @return <code>true</code> - harsh acceleration, <code>false</code> - not harsh acceleration.
     */
    public static boolean isHarshAcceleration(String description) {
        String lowerCase = description.toLowerCase();
        return lowerCase.contains("harsh acceleration") || lowerCase.contains("harshacceleration");
    }

    /**
     * Check description for seatbelt not using.
     * @param description description as string.
     * @return <code>true</code> - seatbelt not used, <code>false</code> - other.
     */
    public static boolean isSeatBeltNotUsed(String description) {
        String lowerCase = description.toLowerCase();
        return lowerCase.contains("seatbelt not used") || lowerCase.contains("seatbeltnotused") || lowerCase.contains("seat belt not used");
    }

    /**
     * Check description for vehicle driven without headlights.
     * @param description description as string.
     * @return <code>true</code> - vehicle driven without headlights, <code>false</code> - other.
     */
    public static boolean isVehicleDrivenWithoutHeadlights(String description) {
        String lowerCase = description.toLowerCase();
        return lowerCase.contains("vehicle driven without headlights") || lowerCase.contains("vehicledrivenwithoutheadlights");
    }

    /**
     * Checking description for analyzing.
     * @param description specified description.
     * @return <code>true</code> - is supported description, <code>false</code> - not supported.
     */
    public static boolean isHandledDescription(String description) {
        return EventAnalyzer.isOverspeed(description)
                || EventAnalyzer.isSeatBeltNotUsed(description)
                || EventAnalyzer.isVehicleDrivenWithoutHeadlights(description)
                || EventAnalyzer.isHarshBraking(description)
                || EventAnalyzer.isHarshAcceleration(description);
    }


    /**
     * Parse speed value from description.
     * @param description description as string.
     * @return parsed value or default speed value if it can not be parsed from description.
     */
    public static Integer parseSpeedValue(String description) {
        List<String> tokens = Arrays.asList(description.split(" "));
        for (String token: tokens) {
            if (NumberUtils.isCreatable(token)) {
                Integer value = Integer.valueOf(token);
                if (value > 0) {
                    return value;
                }
            }
        }
        return DEFAULT_SPEED;
    }

    /**
     * Common rule for false positive events.
     * @param v speed in gps positions, s speed in cost center (From event description).
     * @param x speed limit from description.
     * @return <code>true</code> - in case of false positive, <code>false</code> - in another case.
     */
    public static boolean isFalsePositiveOverspeed(int v, int x) {
        boolean res;
        if (STANDARD_SPEED_LIMITS.contains(x) && v < x) {
            int delta = x / 10;
            res = v < x - delta;
        } else {
            res = false;
        }
        return res;
    }

    /**
     * Common rule for false positive events.
     * @param v speed in gps positions.
     * @param s speed in cost center (From event description).
     * @param x speed limit from description.
     * @return <code>true</code> - in case of false positive, <code>false</code> - in another case.
     */
    public static boolean isSuspectOverspeed(int v, int s, int x) {
        boolean res;
        int delta;
        if (x <= 30) {
            delta = 4;
        } else {
            delta = x / 10;
        }

        if (STANDARD_SPEED_LIMITS.contains(x)
                && x - x / 10 <= v
                && v < x) {
            res = s - v >= delta;
        } else {
            res = false;
        }
        return res;
    }

    /**
     * Common rule for seat belt not used events.
     * @param v speed in gps positions.
     * @param s speed in cost center (From event description), x speed limit from description.
     * @return <code>true</code> - in case of false positive, <code>false</code> - in another case.
     */
    public static boolean isDiffernceMoreThan10percent(int v, int s) {
        boolean res;
        if (Math.abs(s - v) / (double) s > 0.1) {
            res = true;
        } else {
            res = false;
        }
        return res;
    }

    /**
     * Common rule for seat belt not used events.
     * @param v speed in gps positions.
     * @param s speed in cost center (From event description), x speed limit from description.
     * @return <code>true</code> - in case of false positive, <code>false</code> - in another case.
     */
    public static boolean isDiffernceLessThan10percent(int v, int s) {
        boolean res;
        if (Math.abs(s - v) / (double) s <= 0.1) {
            res = true;
        } else {
            res = false;
        }
        return res;
    }

    /**
     * Common rule for seat belt not used events.
     * @param v speed in gps positions.
     * @param s speed in cost center (From event description), x speed limit from description.
     * @return <code>true</code> - in case of false positive, <code>false</code> - in another case.
     */
    @SuppressWarnings("unused")
    public static boolean isSuspectSeatbeltNotUsed(int v, int s) {
        boolean res;
        if (v > 0 && v <= SEATBELT_NOT_USED_SPEED_TRIGGER && Math.abs(s - v) / (double) s <= 0.1) {
            res = true;
        } else {
            res = false;
        }
        return res;
    }

    /**
     * False positive with 10% erro margin.
     * @param v speed in gps positions.
     * @param s speed in cost center (From event description).
     * @param x speed limit from description.
     * @return <code>true</code> - in case of false positive, <code>false</code> - in another case.
     */
    public static boolean is10PercentErrorMarginOverspeed(int v, int s, int x) {
        boolean res;

        if (v > x && Math.abs(s - v) / (double) s > 0.1) {
            res = true;
        } else {
            res = false;
        }

        return res;
    }

    /**
     * Checking unknown velocity.
     * @param v velocity.
     * @return <code>true</code> - unknown velocity value, <code>false</code> - another way.
     */
    public static boolean isUnknownVelocity(int v) {
        return v == -1;
    }

}
