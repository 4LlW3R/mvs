package com.epam.tcodata.overtaking.violation.stream.datalake.converter;

import com.epam.tcodata.models.datalake.prepared.analytics.PreparedConfirmedOvertakingViolation;
import com.epam.tcodata.models.datalake.raw.fact.RawDetectedEvent;
import com.epam.tcodata.storage.avro.entities.events.overtaking.otdb.AvroConfirmedOvertakingViolation;

import java.io.Serializable;
import java.sql.Timestamp;

public interface IConfirmedOvertakingConverter extends Serializable {

    PreparedConfirmedOvertakingViolation convertToConfirmedOvertakingViolation(AvroConfirmedOvertakingViolation confirmedOvertakingEventWithViolations);

    RawDetectedEvent convertToDataLakeEvent(AvroConfirmedOvertakingViolation confirmedOvertakingEventWithViolations,
                                            Long eventTypeId,
                                            Timestamp persistedDateUtc);
}
