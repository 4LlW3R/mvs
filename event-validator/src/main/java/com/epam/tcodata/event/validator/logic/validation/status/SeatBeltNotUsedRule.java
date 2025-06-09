package com.epam.tcodata.event.validator.logic.validation.status;


import com.epam.tcodata.event.validator.logic.EventType;
import com.epam.tcodata.event.validator.logic.IRule;
import com.epam.tcodata.event.validator.logic.SpeedValues;


class SeatBeltNotUsedRule implements IRule {

    private final SpeedValues speedValues;


    SeatBeltNotUsedRule(SpeedValues speedValues) {
        this.speedValues = speedValues;
    }


    @Override
    public int apply() {
        return ValidationStatusAnalyzer.processWithNotUsedBeltOrNotUsedLights(speedValues);
    }

    @Override
    public boolean isApplied() {
        return this.speedValues.getEventType() == EventType.SEATBELT_NOT_USED;
    }
}
