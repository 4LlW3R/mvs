package com.epam.tcodata.road.condition.violation.stream.datalake.converter;


import com.epam.tcodata.models.datalake.prepared.analytics.PreparedRoadConditionViolation;
import com.epam.tcodata.models.datalake.raw.fact.RawDetectedEvent;
import com.epam.tcodata.storage.avro.entities.events.violations.v2.RoadConditionViolationAvro;

import java.io.Serializable;
import java.sql.Timestamp;


public interface IRoadConditionConverter extends Serializable {

    PreparedRoadConditionViolation convertToRoadConditionViolation(RoadConditionViolationAvro avro);

    RawDetectedEvent convertToDataLakeEvent(RoadConditionViolationAvro avro, Long eventTypeId, Timestamp persistedDateUtc);
}
