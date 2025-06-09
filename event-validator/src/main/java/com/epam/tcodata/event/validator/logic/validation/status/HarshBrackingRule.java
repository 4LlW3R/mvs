package com.epam.tcodata.event.validator.logic.validation.status;


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

        if (10 <= s && s <= 35) {
            res = ValidationStatusCode.VALID;
        } else {
            res = ValidationStatusCode.FALSE_POSITIVE;
        }

        return res;
    }

    @Override
    public boolean isApplied() {
        return EventType.HARSH_BRAKING == this.speedValues.getEventType();
    }
}
