package com.epam.tcodata.event.validator.logic.validation.status;


import com.epam.tcodata.event.validator.logic.AbstractRuleChain;
import com.epam.tcodata.event.validator.logic.EventAnalyzer;
import com.epam.tcodata.event.validator.logic.SpeedValues;


class ValidationStatusAnalyzer extends AbstractRuleChain {

    private final SpeedValues speedValues;


    /**
     * Createt validation status analyzer.
     * @param speedValues speeds from different sources.
     */
    ValidationStatusAnalyzer(SpeedValues speedValues) {
        this.speedValues = speedValues;
        this.initRules();
    }

    private void initRules() {
        ValidationStatusRuleFactory ruleFactory = new ValidationStatusRuleFactory(this.speedValues);
        this.addRule(ruleFactory.createOverspeedingRule())
                .addRule(ruleFactory.createSeatBeltNotUsedRule())
                .addRule(ruleFactory.createVehicleDrivenWithoutHeadlightsRule())
                .addRule(ruleFactory.createHarshBrackingRule())
                .addRule(ruleFactory.createHarshAccelerationRule());
    }

    public static int processWithNotUsedBeltOrNotUsedLights(SpeedValues speedValues) {
        int res;

        int v = speedValues.getV();
        int s = speedValues.getS();

        if (v == 0) {
            res = ValidationStatusCode.FALSE_POSITIVE;

        } else if (v == -1) {
            res = ValidationStatusCode.VALID;

        } else if (v > 5 && EventAnalyzer.isDiffernceMoreThan10percent(v, s)) {
            res = ValidationStatusCode.VALID;

        } else if (v > 0 && v <= 5 && EventAnalyzer.isDiffernceLessThan10percent(v, s)) {
            res = ValidationStatusCode.SUSPECT;

        } else if (v > 0 && v <= 5 && EventAnalyzer.isDiffernceMoreThan10percent(v, s)) {
            res = ValidationStatusCode.SUSPECT;

        } else {
            res = ValidationStatusCode.VALID;
        }

        return res;
    }
}
