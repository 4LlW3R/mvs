package com.epam.tcodata.event.validator.logic.complex;

import com.epam.tcodata.event.validator.domain.EnrichedPreparedEvent;
import com.epam.tcodata.event.validator.logic.EventType;

public class OverSpeedDetector extends AbstractDetector {

    private static final int RANGE_FROM_MILLIS = 0;
    private static final int RANGE_TO_MILLIS = 3000;

    private static final long serialVersionUID = -2451562331577824389L;

    @Override
    protected boolean isSuitable(EnrichedPreparedEvent event) {
        return EventType.getByDescription(event.getDescription()) == EventType.OVERSPEED;
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
