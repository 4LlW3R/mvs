package com.epam.tcodata.event.validator.logic.complex;

import com.epam.tcodata.event.validator.domain.EnrichedPreparedEvent;

import java.util.ArrayList;
import java.util.List;

public class ComplexDetector extends AbstractDetector {

    private List<AbstractDetector> detectorList = new ArrayList<>();

    private static final int FIRST_BOUND_MILLIS = 0;
    private static final int SECOND_BOUND_MILLIS = 3000;


    /**
     * Entry point for complex rules analysis.
     */
    public ComplexDetector() {
        detectorList.add(new HarshBrakingDetector());
        detectorList.add(new HarshAccelerationDetector());
        detectorList.add(new OverSpeedDetector());
        detectorList.add(new SeatbeltNotUsedDetector());
        detectorList.add(new HeadlightsNotUsedDetector());
    }

    @Override
    public void checkRules(List<EnrichedPreparedEvent> sorted) {
        for (AbstractDetector detector : detectorList) {
            detector.checkRules(sorted);
        }
    }

    @Override
    boolean isSuitable(EnrichedPreparedEvent event) {
        for (AbstractDetector detector : detectorList) {
            if (detector.isSuitable(event)) {
                return true;
            }
        }
        return false;
    }

    @Override
    int getRangeFrom() {
        return FIRST_BOUND_MILLIS;
    }

    @Override
    int getRangeTo() {
        return SECOND_BOUND_MILLIS;
    }
}
