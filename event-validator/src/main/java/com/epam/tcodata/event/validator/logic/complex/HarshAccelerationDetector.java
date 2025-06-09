package com.epam.tcodata.event.validator.logic.complex;

import com.epam.tcodata.event.validator.domain.EnrichedPreparedEvent;
import com.epam.tcodata.event.validator.logic.EventType;
import com.epam.tcodata.event.validator.logic.problem.vehicle.ProblemVehicleCode;
import com.epam.tcodata.event.validator.logic.validation.status.ValidationStatusCode;

import java.util.List;

@SuppressWarnings("CPD-START")
public class HarshAccelerationDetector extends AbstractDetector {

    private static final long serialVersionUID = 9204862117591111510L;

    private static final int FIRST_BOUND_MILLIS = 0;
    private static final int SECOND_BOUND_MILLIS = 3000;
    private static final int THIRD_BOUND_MILLIS = 5000;

    @Override
    public void checkRules(List<EnrichedPreparedEvent> sorted) {
        EnrichedPreparedEvent firstRangeStartEvent = null;
        EnrichedPreparedEvent secondRangeStartEvent = null;
        EnrichedPreparedEvent previous = null;

        for (EnrichedPreparedEvent current : sorted) {
            if (isSuitable(current)) {
                if (firstRangeStartEvent == null) {
                    firstRangeStartEvent = current;
                    secondRangeStartEvent = current;
                } else {
                    if (isTimesEqualPreviousEventVelocityNull(previous, current)) {
                        setCodes(current, ValidationStatusCode.FALSE_POSITIVE, ProblemVehicleCode.VELOCITY_ISSUE);
                    } else {
                        if (isTimesEqualCurrentEventVelocityNotNull(previous, current)) {
                            setCodes(current, ValidationStatusCode.SUSPECT, ProblemVehicleCode.VELOCITY_ISSUE);
                        } else {
                            if (isInRange(firstRangeStartEvent, current, FIRST_BOUND_MILLIS, SECOND_BOUND_MILLIS)) {
                                setCodes(current, ValidationStatusCode.SUSPECT, ProblemVehicleCode.DUPLICATE_EVENTS);
                            } else {
                                firstRangeStartEvent = current;
                                if (isInRange(secondRangeStartEvent, current, SECOND_BOUND_MILLIS, THIRD_BOUND_MILLIS)) {
                                    setCodes(current, ValidationStatusCode.FALSE_POSITIVE, ProblemVehicleCode.SPEED_SENSOR_SPIKE);
                                } else {
                                    secondRangeStartEvent = current;
                                }
                            }
                        }
                    }
                }
                previous = current;
            }
        }
    }

    protected boolean isSuitable(EnrichedPreparedEvent event) {
        return EventType.getByDescription(event.getDescription()) == EventType.HARSH_ACCELERATION;
    }

    @Override
    protected int getRangeFrom() {
        return FIRST_BOUND_MILLIS;
    }

    @Override
    protected int getRangeTo() {
        return SECOND_BOUND_MILLIS;
    }
}
