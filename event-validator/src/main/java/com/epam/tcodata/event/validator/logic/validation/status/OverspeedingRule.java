package com.epam.tcodata.event.validator.logic.validation.status;


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
            res = ValidationStatusCode.VALID;

        } else if (EventAnalyzer.isFalsePositiveOverspeed(v, x)) {
            res = ValidationStatusCode.FALSE_POSITIVE;

        } else if (EventAnalyzer.isSuspectOverspeed(v, s, x)) {
            res = ValidationStatusCode.SUSPECT;

        } else if (EventAnalyzer.is10PercentErrorMarginOverspeed(v, s, x)) {
            res = ValidationStatusCode.VALID;

            // TO DO It's legacy. Should be removed.
        } else if (v > x) {
            res = ValidationStatusCode.VALID;

        } else {
            if (s - v > 3) {
                res = ValidationStatusCode.FALSE_POSITIVE;
            } else if (s - v == 3) {
                res = ValidationStatusCode.SUSPECT;
            } else {
                res = ValidationStatusCode.VALID;
            }
        }

        return res;
    }

    @Override
    public boolean isApplied() {
        return this.speedValues.getEventType() == EventType.OVERSPEED;
    }
}
