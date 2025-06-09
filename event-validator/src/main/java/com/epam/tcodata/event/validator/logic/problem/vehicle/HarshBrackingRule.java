package com.epam.tcodata.event.validator.logic.problem.vehicle;


import com.epam.tcodata.event.validator.logic.EventType;
import com.epam.tcodata.event.validator.logic.IRule;
import com.epam.tcodata.event.validator.logic.SpeedValues;


class HarshBrackingRule implements IRule {

    private final SpeedValues speedValues;


    HarshBrackingRule(SpeedValues speedValues) {
        this.speedValues = speedValues;
    }


    @Override
    public int apply() {
        int res;
        int s = this.speedValues.getS();
        int v = this.speedValues.getV();

        if (s > 35) {
            res = ProblemVehicleCode.BRACKING_RATE_VALUE_HIGH;

        } else if (v == -1) {
            res = ProblemVehicleCode.NO_GPS_DATA_AVAILABLE;

        } else if (s < 10) {
            res = ProblemVehicleCode.BRACKING_RATE_VALUE_HIGH_COMPARE_WITH_GPS;

        } else {
            res = ProblemVehicleCode.VALID;
        }

        return res;
    }

    @Override
    public boolean isApplied() {
        return EventType.HARSH_BRAKING == this.speedValues.getEventType();
    }
}
