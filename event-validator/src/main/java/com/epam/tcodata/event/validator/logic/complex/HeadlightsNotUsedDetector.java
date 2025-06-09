package com.epam.tcodata.event.validator.logic.complex;

import com.epam.tcodata.event.validator.domain.EnrichedPreparedEvent;
import com.epam.tcodata.event.validator.logic.EventType;
import com.epam.tcodata.event.validator.logic.problem.vehicle.ProblemVehicleCode;
import com.epam.tcodata.event.validator.logic.validation.status.ValidationStatusCode;

import java.util.List;

@SuppressWarnings("CPD-START")
public class HeadlightsNotUsedDetector extends AbstractDetector {

    private static final int RANGE_FROM_MILLIS = 0;
    private static final int RANGE_TO_MILLIS = 3000;

    private static final long serialVersionUID = -1522246997799130153L;

    @Override
    public void checkRules(List<EnrichedPreparedEvent> sorted) {
        EnrichedPreparedEvent rangeStartEvent = null;
        EnrichedPreparedEvent previous = null;

        for (EnrichedPreparedEvent current : sorted) {
            if (isSuitable(current)) {
                if (rangeStartEvent == null) {
                    rangeStartEvent = current;
                } else {
                    if (isTimesEqualPreviousEventVelocityNull(previous, current)) {
                        setCodes(current, ValidationStatusCode.FALSE_POSITIVE, ProblemVehicleCode.VELOCITY_ISSUE);
                    } else {
                        if (isTimesEqualCurrentEventVelocityNotNull(previous, current)) {
                            setCodes(current, ValidationStatusCode.SUSPECT, ProblemVehicleCode.VELOCITY_ISSUE);
                        } else {
                            if (isInRange(rangeStartEvent, current, getRangeFrom(), getRangeTo())) {
                                setCodes(current, ValidationStatusCode.FALSE_POSITIVE, ProblemVehicleCode.DUPLICATE_EVENTS);
                            } else {
                                rangeStartEvent = current;
                            }
                        }
                    }
                }
                previous = current;
            }
        }
    }

    @Override
    protected boolean isSuitable(EnrichedPreparedEvent event) {
        return EventType.getByDescription(event.getDescription()) == EventType.VEHICLE_DRIVEN_WITHOUT_HEADLIGHTS;
    }

    @Override
    protected int getRangeFrom() {
        return RANGE_FROM_MILLIS;
    }

    @Override
    protected int getRangeTo() {
        return RANGE_TO_MILLIS;
    }

}
