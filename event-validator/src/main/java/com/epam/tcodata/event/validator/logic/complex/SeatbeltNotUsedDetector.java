package com.epam.tcodata.event.validator.logic.complex;

import com.epam.tcodata.event.validator.domain.EnrichedPreparedEvent;
import com.epam.tcodata.event.validator.logic.EventType;

public class SeatbeltNotUsedDetector extends AbstractDetector {

    private static final int RANGE_FROM_MILLIS = 0;
    private static final int RANGE_TO_MILLIS = 3000;

    private static final long serialVersionUID = 4889041280446361780L;

    @Override
    protected boolean isSuitable(EnrichedPreparedEvent event) {
        return EventType.getByDescription(event.getDescription()) == EventType.SEATBELT_NOT_USED;
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
