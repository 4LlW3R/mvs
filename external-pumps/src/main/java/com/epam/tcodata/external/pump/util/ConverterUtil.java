package com.epam.tcodata.external.pump.util;

import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class ConverterUtil {

    private ConverterUtil() {
    }

    /**
     * Converts Timestamp to DateTime.
     *
     * @param date date as Timestamp.
     * @return date as DateTime.
     */
    public static DateTime timestampToDateTime(Timestamp date) {
        if (date == null) {
            return DateTime.parse("1700-01-01T00:00:00Z");
        }
        return new DateTime(date.getTime());
    }

    /**
     * Converts Instant to String.
     *
     * @param instant date as Instant.
     * @return date as String.
     */
    public static String instantToString(Instant instant, String timePattern) {
        if (instant == null) {
            return null;
        }
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(timePattern)
                        .withZone(ZoneOffset.UTC);
        return formatter.format(instant);
    }

    /**
     * Converts String to Instant.
     *
     * @param instantStr date as String.
     * @return date as Instant.
     */
    public static Instant stringToInstant(String instantStr, String timePattern) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(timePattern)
                        .withZone(ZoneOffset.UTC);
        return formatter.parse(instantStr, Instant::from);
    }
}
