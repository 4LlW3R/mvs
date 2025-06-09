package com.epam.tcodata.event.validator.logic.validation.status;


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
            res = ValidationStatusCode.FALSE_POSITIVE;

        } else {
            if (s < 10) {
                res = ValidationStatusCode.FALSE_POSITIVE;

            } else {
                if (v == -1) {
                    res = ValidationStatusCode.VALID;
                } else if (v <= 10) {
                    res = ValidationStatusCode.FALSE_POSITIVE;
                } else {
                    res = ValidationStatusCode.VALID;
                }
            }
        }

        return res;
    }

    @Override
    public boolean isApplied() {
        return EventType.HARSH_ACCELERATION == this.speedValues.getEventType();
    }
}
