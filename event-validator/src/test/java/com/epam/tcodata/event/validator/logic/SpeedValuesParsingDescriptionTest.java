package com.epam.tcodata.event.validator.logic;


import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;


@RunWith(value = Parameterized.class)
public class SpeedValuesParsingDescriptionTest {

    private String description;
    private EventType expectedEventType;
    private Integer expectedSpeed;


    public SpeedValuesParsingDescriptionTest(String description, EventType expectedEventType, Integer expectedSpeed) {
        this.description = description;
        this.expectedEventType = expectedEventType;
        this.expectedSpeed = expectedSpeed;
    }


    @Test
    public void testDescriptionParsing() {
        SpeedValues speedValues = new SpeedValues(0, 0, this.description);
        TestCase.assertEquals(this.expectedEventType, speedValues.getEventType());
        TestCase.assertEquals(this.expectedSpeed, Integer.valueOf(speedValues.getX()));
    }

    @Parameterized.Parameters(name = "{index}: [desc - {0}], [expected EventType - {1}]")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Harsh braking1", EventType.HARSH_BRAKING, 0},
                {"TCO Harsh braking", EventType.HARSH_BRAKING, 0},
                {"Over speeding in location - EXCESSIVE SPEED", EventType.OVERSPEED, 90},
                {"Over speeding", EventType.OVERSPEED, 90},
                {"TCO Over Speed > 10 KTL-TA DO NOT USE", EventType.OVERSPEED, 10},
                {"Over Speed > 70 SR#62059 Road To Field", EventType.OVERSPEED, 70},
                {"In-cab road speed over speeding - EXCESSIVE DURATION", EventType.OVERSPEED, 90},
                {"Harsh acceleration", EventType.HARSH_ACCELERATION, 0},
                {"Harsh 123 braking", EventType.NONE, 0},
                {"In-cab road speed over aasdaweq speeding - EXCESSIVE DURATION", EventType.NONE, 0},
                {"OvEr SpEEdInG_ - EXCESSIVE DURATION", EventType.OVERSPEED, 90},
                {"Seatbelt not Used", EventType.SEATBELT_NOT_USED, 0},
                {"asdqweSeatbelt not Usedasdqwe", EventType.SEATBELT_NOT_USED, 0},
                {"SeaTBelt nOt uSeD", EventType.SEATBELT_NOT_USED, 0},
                {"sEaTbelt not Used", EventType.SEATBELT_NOT_USED, 0},
                {"sEaT@belt not Used", EventType.NONE, 0},
                {"Vehicle driven without headlights", EventType.VEHICLE_DRIVEN_WITHOUT_HEADLIGHTS, 0},
                {"asdasdVehicle driven without headlightsasda", EventType.VEHICLE_DRIVEN_WITHOUT_HEADLIGHTS, 0},
                {"asdasdVehic@le driven without headlightsasda", EventType.NONE, 0}
        });
    }

}
