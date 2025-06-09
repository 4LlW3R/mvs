package com.epam.tcodata.event.validator.logic.validation.status;


import com.epam.tcodata.event.validator.logic.IRule;
import com.epam.tcodata.event.validator.logic.IRuleChain;
import com.epam.tcodata.event.validator.logic.SpeedValues;

import java.io.Serializable;


public class ValidationStatusRuleFactory implements Serializable {

    private final SpeedValues speedValues;

    public ValidationStatusRuleFactory(SpeedValues speedValues) {
        this.speedValues = speedValues;
    }


    public IRule createHarshAccelerationRule() {
        return new HarshAccelerationRule(this.speedValues);
    }

    public IRule createHarshBrackingRule() {
        return new HarshBrackingRule(this.speedValues);
    }

    public IRule createOverspeedingRule() {
        return new OverspeedingRule(this.speedValues);
    }

    public IRule createSeatBeltNotUsedRule() {
        return new SeatBeltNotUsedRule(this.speedValues);
    }

    public IRule createVehicleDrivenWithoutHeadlightsRule() {
        return new VehicleDrivenWithoutHeadlightsRule(this.speedValues);
    }

    public IRuleChain createValidationStatusAnalyzer() {
        return new ValidationStatusAnalyzer(this.speedValues);
    }

}
