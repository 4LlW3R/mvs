package com.epam.tcodata.event.validator.logic.problem.vehicle;


import com.epam.tcodata.event.validator.logic.EventType;
import com.epam.tcodata.event.validator.logic.IRule;
import com.epam.tcodata.event.validator.logic.SpeedValues;


class HarshAccelerationRule implements IRule {

    private final SpeedValues speedValues;

    HarshAccelerationRule(SpeedValues speedValues) {
        this.speedValues = speedValues;
    }


    @Override
    public int apply() {
        int res;
        int s = this.speedValues.getS();
        int v = this.speedValues.getV();

        if (s > 20) {
            res = ProblemVehicleCode.ACCELERATION_RATE_HIGH;

        } else if (s < 10) {
            if (v == -1) {
                res = ProblemVehicleCode.NO_GPS_DATA_AVAILABLE;
            } else {
                res = ProblemVehicleCode.ACCELERATION_RATE_COMPARE_WITH_GPS;
            }

        } else if (v == -1) {
            res = ProblemVehicleCode.NO_GPS_DATA_AVAILABLE;

        } else if (v <= 10) {
            res = ProblemVehicleCode.ACCELERATION_RATE_COMPARE_WITH_GPS;

        } else {
            res = ProblemVehicleCode.VALID;
        }

        return res;
    }

    @Override
    public boolean isApplied() {
        return EventType.HARSH_ACCELERATION == this.speedValues.getEventType();
    }
}
