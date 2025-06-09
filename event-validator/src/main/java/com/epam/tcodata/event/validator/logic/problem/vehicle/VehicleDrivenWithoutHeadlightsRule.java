package com.epam.tcodata.event.validator.logic.problem.vehicle;


import com.epam.tcodata.event.validator.logic.EventType;
import com.epam.tcodata.event.validator.logic.IRule;
import com.epam.tcodata.event.validator.logic.SpeedValues;


class VehicleDrivenWithoutHeadlightsRule implements IRule {

    private final SpeedValues speedValues;

    VehicleDrivenWithoutHeadlightsRule(SpeedValues speedValues) {
        this.speedValues = speedValues;
    }

    @Override
    public int apply() {
        return ProblemVehicleCodeAnalyzer.processWithNotUsedBeltOrNotUsedLights(speedValues);
    }

    @Override
    public boolean isApplied() {
        return this.speedValues.getEventType() == EventType.VEHICLE_DRIVEN_WITHOUT_HEADLIGHTS;
    }
}
