package com.epam.tcodata.event.validator.logic;


import com.epam.tcodata.event.validator.logic.problem.vehicle.ProblemVehicleCode;
import com.epam.tcodata.event.validator.logic.problem.vehicle.ProblemVehicleRuleFactory;
import com.epam.tcodata.event.validator.logic.validation.status.ValidationStatusCode;
import com.epam.tcodata.event.validator.logic.validation.status.ValidationStatusRuleFactory;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;


@RunWith(value = Parameterized.class)
public class SpeedValuesLogicTest {

    private String description;
    private Integer speed;
    private Integer velocity;
    private int expectedValidationCode;
    private int expectedProblemVehicle;
    private static final String SICIM_BASE_STR_CONST = "Sicim Over Speed - Sicim Base";
    private static final String SICIM_OVR_20_STR_CONST = "Sicim Over Speed 20 - Sicim Base";
    private static final String TCO_OVER_SPEED_30_STR_CONST = "TCO Over Speed > 30 KTL-TA DO NOT USE";
    private static final String SEAT_BELT_NOT_USEDIM_STR_CONST = "Seat belt not usedim Base";
    private static final String VEHICLE_DRVN_WO_HDLGHTS_STR_CONST = "Vehicle driven without headlights";
    private static final String TCO_HARSH_BREAKING123_STR_CONST = "TCO Harsh braking123";
    private static final String TCO_HARSH_ACCLRTN_STR_CONST = "TCO Harsh acceleration";


    public SpeedValuesLogicTest(
            Integer velocity,
            Integer speed,
            String description,
            Integer expectedValidationCode,
            Integer expectedProblemVehicle) {
        this.velocity = velocity;
        this.speed = speed;
        this.description = description;
        this.expectedValidationCode = expectedValidationCode;
        this.expectedProblemVehicle = expectedProblemVehicle;
    }


    @Test
    public void testDescriptionParsing() {
        SpeedValues speedValues = new SpeedValues(this.velocity, this.speed, this.description);
        IRuleChain problemVehicleCodeAnalyzer = new ProblemVehicleRuleFactory(speedValues).createProblemVehicleCodeAnalyzer();
        IRuleChain validationStatusAnalyzer = new ValidationStatusRuleFactory(speedValues).createValidationStatusAnalyzer();

        TestCase.assertEquals(this.expectedProblemVehicle, problemVehicleCodeAnalyzer.apply());
        TestCase.assertEquals(this.expectedValidationCode, validationStatusAnalyzer.apply());
    }

    @Parameterized.Parameters(name = "{index}: [velocity - {0}], [speed - {1}], [description - {2}]")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                // Overspeed cases
                // False - GPS problem
                // velocity, speed, description, expected validation status, expected speed sender problem.
                {17, 22, SICIM_OVR_20_STR_CONST, ValidationStatusCode.FALSE_POSITIVE, ProblemVehicleCode.GPS_PROBLEM},
                {26, 22, "Sicim Over Speed 30 - Sicim Base", ValidationStatusCode.FALSE_POSITIVE, ProblemVehicleCode.GPS_PROBLEM},
                {35, 22, "Sicim Over Speed 40 - Sicim Base", ValidationStatusCode.FALSE_POSITIVE, ProblemVehicleCode.GPS_PROBLEM},
                {44, 22, "Sicim Over Speed 50 - Sicim Base", ValidationStatusCode.FALSE_POSITIVE, ProblemVehicleCode.GPS_PROBLEM},
                {53, 22, "Sicim Over Speed 60 - Sicim Base", ValidationStatusCode.FALSE_POSITIVE, ProblemVehicleCode.GPS_PROBLEM},
                {62, 22, "Sicim Over Speed 70 - Sicim Base", ValidationStatusCode.FALSE_POSITIVE, ProblemVehicleCode.GPS_PROBLEM},
                {71, 22, "Sicim Over Speed 80 - Sicim Base", ValidationStatusCode.FALSE_POSITIVE, ProblemVehicleCode.GPS_PROBLEM},
                {80, 22, SICIM_BASE_STR_CONST, ValidationStatusCode.FALSE_POSITIVE, ProblemVehicleCode.GPS_PROBLEM},

                // 10 % error margin
                // velocity, speed, description, expected validation status, expected speed sender problem.
                {50, 40, TCO_OVER_SPEED_30_STR_CONST, ValidationStatusCode.VALID, ProblemVehicleCode.SPEED_SENDER_PROBLEM},
                {40, 50, TCO_OVER_SPEED_30_STR_CONST, ValidationStatusCode.VALID, ProblemVehicleCode.GPS_PROBLEM},

                // Unknown velocity.
                {-1, 50, TCO_OVER_SPEED_30_STR_CONST, ValidationStatusCode.VALID, ProblemVehicleCode.NO_GPS_DATA_AVAILABLE},

                // Overspeed cases
                // Suspect - valid
                // velocity, speed, description, expected validation status, expected speed sender problem.
                {18, 22, SICIM_OVR_20_STR_CONST, ValidationStatusCode.SUSPECT, ProblemVehicleCode.VALID},
                {27, 31, "Sicim Over Speed 30 - Sicim Base", ValidationStatusCode.SUSPECT, ProblemVehicleCode.VALID},
                {36, 41, "Sicim Over Speed 40 - Sicim Base", ValidationStatusCode.SUSPECT, ProblemVehicleCode.VALID},
                {45, 50, "Sicim Over Speed 50 - Sicim Base", ValidationStatusCode.SUSPECT, ProblemVehicleCode.VALID},
                {54, 60, "Sicim Over Speed 60 - Sicim Base", ValidationStatusCode.SUSPECT, ProblemVehicleCode.VALID},
                {63, 71, "Sicim Over Speed 70 - Sicim Base", ValidationStatusCode.SUSPECT, ProblemVehicleCode.VALID},
                {72, 80, "Sicim Over Speed 80 - Sicim Base", ValidationStatusCode.SUSPECT, ProblemVehicleCode.VALID},
                {81, 90, SICIM_BASE_STR_CONST, ValidationStatusCode.SUSPECT, ProblemVehicleCode.VALID},

                {18, 19, SICIM_OVR_20_STR_CONST, ValidationStatusCode.VALID, ProblemVehicleCode.VALID},
                {81, 75, SICIM_BASE_STR_CONST, ValidationStatusCode.VALID, ProblemVehicleCode.VALID},

                // Seat belt not used
                // velocity, speed, description, expected validation status, expected speed sender problem.
                {0, 200, "Seatbelt not Used\n", ValidationStatusCode.FALSE_POSITIVE, ProblemVehicleCode.GPS_PROBLEM},
                {-1, 200, "Sicim Over SSeatbelt not used\tSicim Base", ValidationStatusCode.VALID, ProblemVehicleCode.NO_GPS_DATA_AVAILABLE},
                {6, 29, "Seatbelt not used", ValidationStatusCode.VALID, ProblemVehicleCode.GPS_PROBLEM},
                {30, 10, SEAT_BELT_NOT_USEDIM_STR_CONST, ValidationStatusCode.VALID, ProblemVehicleCode.SPEED_SENDER_PROBLEM},
                {4, 20, SEAT_BELT_NOT_USEDIM_STR_CONST, ValidationStatusCode.SUSPECT, ProblemVehicleCode.GPS_PROBLEM},
                {5, 10, SEAT_BELT_NOT_USEDIM_STR_CONST, ValidationStatusCode.SUSPECT, ProblemVehicleCode.GPS_PROBLEM},
                {1, 1, SEAT_BELT_NOT_USEDIM_STR_CONST, ValidationStatusCode.SUSPECT, ProblemVehicleCode.VALID},
                {4, 4, SEAT_BELT_NOT_USEDIM_STR_CONST, ValidationStatusCode.SUSPECT, ProblemVehicleCode.VALID},
                {65, 100, SEAT_BELT_NOT_USEDIM_STR_CONST, ValidationStatusCode.VALID, ProblemVehicleCode.GPS_PROBLEM},

                // Vehicle driven without headlights
                // velocity, speed, description, expected validation status, expected speed sender problem.
                {15, 16, VEHICLE_DRVN_WO_HDLGHTS_STR_CONST, ValidationStatusCode.VALID, ProblemVehicleCode.VALID},
                {31, 33, VEHICLE_DRVN_WO_HDLGHTS_STR_CONST, ValidationStatusCode.VALID, ProblemVehicleCode.VALID},
                {-1, Integer.MAX_VALUE, "asdaVehicle driven without headlights", ValidationStatusCode.VALID, ProblemVehicleCode.NO_GPS_DATA_AVAILABLE},
                {6, 30, VEHICLE_DRVN_WO_HDLGHTS_STR_CONST, ValidationStatusCode.VALID, ProblemVehicleCode.GPS_PROBLEM},
                {30, 7, "asdVehicle driven without headlights123", ValidationStatusCode.VALID, ProblemVehicleCode.SPEED_SENDER_PROBLEM},
                {5, 5, "Vehicle driven without headlights1232", ValidationStatusCode.SUSPECT, ProblemVehicleCode.VALID},
                {5, 1, "    Vehicle driven without headlights    ", ValidationStatusCode.SUSPECT, ProblemVehicleCode.GPS_PROBLEM},
                {5, 2, "xxda asdaVehicle driven without headlights   asd ", ValidationStatusCode.SUSPECT, ProblemVehicleCode.GPS_PROBLEM},

                // Harsh bracking events
                // velocity, speed, description, expected validation status, expected speed sender problem.
                {0, 36, TCO_HARSH_BREAKING123_STR_CONST, ValidationStatusCode.FALSE_POSITIVE, ProblemVehicleCode.BRACKING_RATE_VALUE_HIGH},
                {-1, 100, TCO_HARSH_BREAKING123_STR_CONST, ValidationStatusCode.FALSE_POSITIVE, ProblemVehicleCode.BRACKING_RATE_VALUE_HIGH},
                {-1, 35, TCO_HARSH_BREAKING123_STR_CONST, ValidationStatusCode.VALID, ProblemVehicleCode.NO_GPS_DATA_AVAILABLE},
                {-1, 10, TCO_HARSH_BREAKING123_STR_CONST, ValidationStatusCode.VALID, ProblemVehicleCode.NO_GPS_DATA_AVAILABLE},
                {-1, 9, TCO_HARSH_BREAKING123_STR_CONST, ValidationStatusCode.FALSE_POSITIVE, ProblemVehicleCode.NO_GPS_DATA_AVAILABLE},
                {123, 35, TCO_HARSH_BREAKING123_STR_CONST, ValidationStatusCode.VALID, ProblemVehicleCode.VALID},
                {123, 10, TCO_HARSH_BREAKING123_STR_CONST, ValidationStatusCode.VALID, ProblemVehicleCode.VALID},

                // Harsh acceleration
                // velocity, speed, description, expected validation status, expected speed sender problem.
                {30, 21, TCO_HARSH_ACCLRTN_STR_CONST, ValidationStatusCode.FALSE_POSITIVE, ProblemVehicleCode.ACCELERATION_RATE_HIGH},
                {30, 200, TCO_HARSH_ACCLRTN_STR_CONST, ValidationStatusCode.FALSE_POSITIVE, ProblemVehicleCode.ACCELERATION_RATE_HIGH},
                {30, Integer.MAX_VALUE, TCO_HARSH_ACCLRTN_STR_CONST, ValidationStatusCode.FALSE_POSITIVE, ProblemVehicleCode.ACCELERATION_RATE_HIGH},

                {-1, 21, TCO_HARSH_ACCLRTN_STR_CONST, ValidationStatusCode.FALSE_POSITIVE, ProblemVehicleCode.ACCELERATION_RATE_HIGH},
                {10, 10, TCO_HARSH_ACCLRTN_STR_CONST, ValidationStatusCode.FALSE_POSITIVE, ProblemVehicleCode.ACCELERATION_RATE_COMPARE_WITH_GPS},

                {11, 10, TCO_HARSH_ACCLRTN_STR_CONST, ValidationStatusCode.VALID, ProblemVehicleCode.VALID},

                {-1, 9, TCO_HARSH_ACCLRTN_STR_CONST, ValidationStatusCode.FALSE_POSITIVE, ProblemVehicleCode.NO_GPS_DATA_AVAILABLE},
                {123, 8, TCO_HARSH_ACCLRTN_STR_CONST, ValidationStatusCode.FALSE_POSITIVE, ProblemVehicleCode.ACCELERATION_RATE_COMPARE_WITH_GPS},

                {30, 13, TCO_HARSH_ACCLRTN_STR_CONST, ValidationStatusCode.VALID, ProblemVehicleCode.VALID},
                {11, 13, TCO_HARSH_ACCLRTN_STR_CONST, ValidationStatusCode.VALID, ProblemVehicleCode.VALID},
                {1000, 21, TCO_HARSH_ACCLRTN_STR_CONST, ValidationStatusCode.FALSE_POSITIVE, ProblemVehicleCode.ACCELERATION_RATE_HIGH},
                {-1, 3000, " 123 TCO Harsh acceleration", ValidationStatusCode.FALSE_POSITIVE, ProblemVehicleCode.ACCELERATION_RATE_HIGH},
                {-1, 20, "TCO Harsh acceleration 321 ", ValidationStatusCode.VALID, ProblemVehicleCode.NO_GPS_DATA_AVAILABLE},
                {10, 20, "   TCO Harsh acceleration  ", ValidationStatusCode.FALSE_POSITIVE, ProblemVehicleCode.ACCELERATION_RATE_COMPARE_WITH_GPS},
                {11, 20, " 212  TCO HaRsh aCcEleration  ", ValidationStatusCode.VALID, ProblemVehicleCode.VALID}
        });
    }

}
