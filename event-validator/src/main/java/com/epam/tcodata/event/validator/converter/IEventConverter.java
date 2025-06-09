package com.epam.tcodata.event.validator.converter;

import com.epam.tcodata.event.validator.domain.EnrichedPreparedEvent;
import com.epam.tcodata.models.datalake.prepared.analytics.PreparedValidatedEvent;
import com.epam.tcodata.models.datalake.prepared.fact.PreparedEvent;

import java.io.Serializable;

/**
 * This interface provides us possibilities to convert all necessary entities between different states.
 */
public interface IEventConverter extends Serializable {

    /**
     * Method converts prepared event into enriched prepared event (needs only for calculations).
     *
     * @return EnrichedPreparedEvent
     */
    EnrichedPreparedEvent convertToEnriched(PreparedEvent preparedEvent);

    /**
     * Method converts enriched prepared event into prepared validated event.
     *
     * @return PreparedValidatedEvent
     */
    PreparedValidatedEvent convertToPreparedValidated(EnrichedPreparedEvent enrichedPreparedEvent);

}
