package com.epam.tcodata.common;

import java.sql.Timestamp;
import java.time.temporal.WeekFields;

/**
 * Universal tool for different conversions.
 */
public class ConverterUtils {
    private ConverterUtils(){}

    /**
     * Converts CharSequence to String with checking for null.
     *
     * @param charSequence charSequence.
     * @return String.
     */
    public static String checkedToString(CharSequence charSequence) {
        return null == charSequence ? null : charSequence.toString();
    }


    /**
     * Method converts timestamp to int year.
     *
     * @return year
     */
    public static Integer timestampToYear(Timestamp timestamp) {
        return timestamp.toLocalDateTime().getYear();
    }

    /**
     * Method converts timestamp to int week number.
     *
     * @return week number
     */
    public static Integer timestampToWeekNumber(Timestamp timestamp) {
        return timestamp.toLocalDateTime().get(WeekFields.ISO.weekOfWeekBasedYear());
    }
}
