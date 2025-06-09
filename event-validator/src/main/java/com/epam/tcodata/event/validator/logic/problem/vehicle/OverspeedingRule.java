package com.epam.tcodata.event.validator.logic.problem.vehicle;


import com.epam.tcodata.event.validator.logic.EventAnalyzer;
import com.epam.tcodata.event.validator.logic.EventType;
import com.epam.tcodata.event.validator.logic.IRule;
import com.epam.tcodata.event.validator.logic.SpeedValues;


class OverspeedingRule implements IRule {

    private final SpeedValues speedValues;


    OverspeedingRule(SpeedValues speedValues) {
        this.speedValues = speedValues;
    }


    @Override
    public int apply() {
        int res;
        int v = this.speedValues.getV();
        int s = this.speedValues.getS();
        int x = this.speedValues.getX();

        if (EventAnalyzer.isUnknownVelocity(v)) {
            res = ProblemVehicleCode.NO_GPS_DATA_AVAILABLE;

        } else if (EventAnalyzer.isFalsePositiveOverspeed(v, x)) {
            res = ProblemVehicleCode.GPS_PROBLEM;

        } else if (EventAnalyzer.isSuspectOverspeed(v, s, x)) {
            res = ProblemVehicleCode.VALID;

        } else if (EventAnalyzer.is10PercentErrorMarginOverspeed(v, s, x)) {
            if (s > v) {
                res = ProblemVehicleCode.GPS_PROBLEM;
            } else {
                res = ProblemVehicleCode.SPEED_SENDER_PROBLEM;
            }


            // TO DO It's legacy. Should be removed.
        } else if (v > x) {
            if (v - s > 3) {
                res = ProblemVehicleCode.SPEED_SENDER_PROBLEM;
            } else {
                res = ProblemVehicleCode.VALID;
            }

        } else {
            if (s - v >= 3) {
                res = ProblemVehicleCode.SPEED_SENDER_PROBLEM;
            } else {
                res = ProblemVehicleCode.VALID;
            }
        }

        return res;
    }

    @Override
    public boolean isApplied() {
        return this.speedValues.getEventType() == EventType.OVERSPEED;
    }
}
