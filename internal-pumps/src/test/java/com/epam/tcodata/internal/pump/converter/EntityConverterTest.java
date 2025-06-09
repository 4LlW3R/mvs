package com.epam.tcodata.internal.pump.converter;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;

import static junit.framework.TestCase.assertEquals;

public class EntityConverterTest {

    private static final String DATE_20190101_09_04_54_0 = "2019-01-01 09:04:54.0";

    @Before
    public void init() {
        /***  Default implementation ***/
    }

    @Test
    public void shouldConvertDateTimeToTimestamp() {
        DateTime dateTime1 = new DateTime(2019, 01, 01, 12, 30);
        Timestamp timestamp1 = IEntityConverter.dateTimeToTimestamp(dateTime1);
        assertEquals(timestamp1, new Timestamp(dateTime1.getMillis()));

        DateTime dateTime2 = DateTime.parse("1700-01-01T00:00:00Z");
        Timestamp timestamp2 = IEntityConverter.dateTimeToTimestamp(dateTime2);
        assertEquals(timestamp2, null);

        DateTime dateTime3 = null;
        Timestamp timestamp3 = IEntityConverter.dateTimeToTimestamp(dateTime3);
        assertEquals(timestamp3, null);
    }

    @Test
    public void shouldConvertTimestampToYear(){
        Timestamp timestamp1 = Timestamp.valueOf(DATE_20190101_09_04_54_0);
        int year1 = IEntityConverter.timestampToYear(timestamp1);
        assertEquals(year1, 2019);
    }

    @Test
    public void shouldConvertTimestampToWeekNumber() {
        Timestamp timestamp1 = Timestamp.valueOf(DATE_20190101_09_04_54_0);
        int weekNumber1 = IEntityConverter.timestampToWeekNumber(timestamp1);
        assertEquals(weekNumber1, 1);

        Timestamp timestamp2 = Timestamp.valueOf("2019-01-07 09:04:54.0");
        int weekNumber2 = IEntityConverter.timestampToWeekNumber(timestamp2);
        assertEquals(weekNumber2, 2);

        Timestamp timestamp3 = Timestamp.valueOf("2019-01-06 09:04:54.0");
        int weekNumber3 = IEntityConverter.timestampToWeekNumber(timestamp3);
        assertEquals(weekNumber3, 1);

        Timestamp timestamp4 = Timestamp.valueOf("2019-01-16 09:04:54.0");
        int weekNumber4 = IEntityConverter.timestampToWeekNumber(timestamp4);
        assertEquals(weekNumber4, 3);
    }

    @Test
    public void shouldConvertTimestampToDayOfYear(){
        Timestamp timestamp1 = Timestamp.valueOf(DATE_20190101_09_04_54_0);
        int dayOfYear1 = IEntityConverter.timestampToDayOfYear(timestamp1);
        assertEquals(dayOfYear1, 1);
    }

}
