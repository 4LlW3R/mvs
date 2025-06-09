package com.epam.tcodata.event.validator.logic.problem.vehicle;


import com.epam.tcodata.event.validator.logic.AbstractRuleChain;
import com.epam.tcodata.event.validator.logic.EventAnalyzer;
import com.epam.tcodata.event.validator.logic.SpeedValues;


class ProblemVehicleCodeAnalyzer extends AbstractRuleChain {

    private final SpeedValues speedValues;


    /**
     * Create ProblemVehicleAnalyzer.
     *
     * @param speedValues speed values fomr different sources.
     */
    ProblemVehicleCodeAnalyzer(SpeedValues speedValues) {
        this.speedValues = speedValues;
        this.initRules();
    }

    private void initRules() {
        ProblemVehicleRuleFactory ruleFactory = new ProblemVehicleRuleFactory(this.speedValues);

        this.addRule(ruleFactory.createOverspeedingRule())
                .addRule(ruleFactory.createSeatBeltNotUsedRule())
                .addRule(ruleFactory.createVehicleDrivenWithoutHeadlightsRule())
                .addRule(ruleFactory.createHarshBrackingRule())
                .addRule(ruleFactory.createHarshAccelerationRule());
    }

    /**
     * Just a common logic for two different rules.
     *
     * @param speedValues speed values from different sources.
     * @return problem vehicle code.
     */
    public static int processWithNotUsedBeltOrNotUsedLights(SpeedValues speedValues) {
        int res;
        int v = speedValues.getV();
        int s = speedValues.getS();

        if (v == 0) {
            res = ProblemVehicleCode.GPS_PROBLEM;

        } else if (v == -1) {
            res = ProblemVehicleCode.NO_GPS_DATA_AVAILABLE;

        } else if (v > 5 && EventAnalyzer.isDiffernceMoreThan10percent(v, s)) {
            if (s > v) {
                res = ProblemVehicleCode.GPS_PROBLEM;
            } else {
                res = ProblemVehicleCode.SPEED_SENDER_PROBLEM;
            }

        } else if (v > 0 && v <= 5 && EventAnalyzer.isDiffernceLessThan10percent(v, s)) {
            res = ProblemVehicleCode.VALID;

        } else if (v > 0 && v <= 5 && EventAnalyzer.isDiffernceMoreThan10percent(v, s)) {
            res = ProblemVehicleCode.GPS_PROBLEM;

        } else {
            res = ProblemVehicleCode.VALID;
        }

        return res;
    }

}
