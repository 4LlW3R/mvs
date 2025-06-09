package com.epam.tcodata.external.pump.util;

import org.joda.time.DateTime;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.Instant;

import static junit.framework.TestCase.assertEquals;

public class ConverterUtilTest {

    private static final String TIME_PATTERN = "yyyyMMddHHmmssnnn";

    @Test
    public void nullTimestampToDateTimeTest() {
        Timestamp nullTimestamp = null;
        DateTime defaultDateTime = ConverterUtil.timestampToDateTime(nullTimestamp);
        assertEquals(DateTime.parse("1700-01-01T00:00:00Z"), defaultDateTime);
    }

    @Test
    public void timestampToDateTimeTest() {
        Timestamp timestamp = Timestamp.valueOf("2019-03-13 00:00:00");
        DateTime dateTime = ConverterUtil.timestampToDateTime(timestamp);
        assertEquals(DateTime.parse("2019-03-13T00:00:00"), dateTime);
    }

    @Test
    public void instantToStringTest() {
        Instant instant = Instant.parse("2007-12-03T10:15:30.00Z");
        String minInstantString = ConverterUtil.instantToString(instant, TIME_PATTERN);
        assertEquals("20071203101530000", minInstantString);
    }

    @Test
    public void stringToInstantTest() {
        String string = "20071203101530000";
        Instant actual = ConverterUtil.stringToInstant(string, TIME_PATTERN);
        Instant expected = Instant.parse("2007-12-03T10:15:30.00Z");
        assertEquals(expected, actual);
    }
}
