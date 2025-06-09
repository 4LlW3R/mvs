package com.epam.tcodata.road.condition.violation.stream.datalake.converter;


import com.epam.tcodata.models.datalake.prepared.analytics.PreparedRoadConditionViolation;
import com.epam.tcodata.models.datalake.raw.fact.RawDetectedEvent;
import com.epam.tcodata.storage.avro.entities.events.violations.v2.RoadConditionViolationAvro;

import java.sql.Timestamp;

import static com.epam.tcodata.common.ConverterUtils.*;


public class RoadConditionConverter implements IRoadConditionConverter {

    private static final long serialVersionUID = -1876898729973560198L;

    /**
     * Method to convert RoadConditionViolationAvro to PreparedRoadConditionViolation.
     *
     * @param avro road condition violation avro
     */
    @Override
    public PreparedRoadConditionViolation convertToRoadConditionViolation(RoadConditionViolationAvro avro) {
        PreparedRoadConditionViolation violation = new PreparedRoadConditionViolation();
        violation.setDomain(checkedToString(avro.getDomain()));
        violation.setEntityType(avro.getEntityType());
        violation.setSchemaVersion(avro.getSchemaVersion());
        violation.setId(checkedToString(avro.getId()));
        violation.setSubscriptionId(avro.getSubscriptionId());
        violation.setVehicleId(checkedToString(avro.getVehicleId()));
        violation.setDriverId(checkedToString(avro.getDriverId()));
        violation.setStartTime(new Timestamp(avro.getStartTime().getMillis()));
        violation.setEndTime(new Timestamp(avro.getEndTime().getMillis()));
        violation.setAverageSpeed(avro.getAverageSpeed());
        violation.setMaxSpeed(avro.getMaxSpeed());
        violation.setRoadConditionId(checkedToString(avro.getRoadConditionId()));
        violation.setCreationTime(new Timestamp(avro.getCreationTime().getMillis()));
        violation.setStartFactGpsId(checkedToString(avro.getStartFactGpsId()));
        violation.setEndFactGpsId(checkedToString(avro.getEndFactGpsId()));
        violation.setStartLatitude(avro.getStartLatitude());
        violation.setStartLongitude(avro.getStartLongitude());
        violation.setEndLatitude(avro.getEndLatitude());
        violation.setEndLongitude(avro.getEndLongitude());
        return violation;
    }

    /**
     * Method to convert RoadConditionViolationAvro to DataLakeEvent.
     *
     * @param avro        road condition violation avro
     * @param eventTypeId event type id
     */
    @Override
    public RawDetectedEvent convertToDataLakeEvent(RoadConditionViolationAvro avro, Long eventTypeId, Timestamp persistedDateUtc) {
        RawDetectedEvent dataLakeEvent = new RawDetectedEvent();
        dataLakeEvent.setYear(timestampToYear(persistedDateUtc));
        dataLakeEvent.setWeekNumber(timestampToWeekNumber(persistedDateUtc));
        dataLakeEvent.setSubscriptionId(avro.getSubscriptionId());
        dataLakeEvent.setDurableId(checkedToString(avro.getId()));
        dataLakeEvent.setPersistedDateUtc(persistedDateUtc);
        dataLakeEvent.setDriverDurableKey(checkedToString(avro.getDriverId()));
        dataLakeEvent.setVehicleDurableKey(checkedToString(avro.getVehicleId()));
        dataLakeEvent.setEventTypeId(eventTypeId);
        dataLakeEvent.setStartDateTime(new Timestamp(avro.getStartTime().getMillis()));
        dataLakeEvent.setEndDateTime(new Timestamp(avro.getEndTime().getMillis()));
        dataLakeEvent.setStartPositionPositionId(Long.parseLong(checkedToString(avro.getStartFactGpsId())));
        dataLakeEvent.setEndPositionPositionId(Long.parseLong(checkedToString(avro.getEndFactGpsId())));
        dataLakeEvent.setValue(avro.getMaxSpeed());
        dataLakeEvent.setStartPositionLatitude(avro.getStartLatitude());
        dataLakeEvent.setStartPositionLongitude(avro.getStartLongitude());
        dataLakeEvent.setEndPositionLatitude(avro.getEndLatitude());
        dataLakeEvent.setEndPositionLongitude(avro.getEndLongitude());
        dataLakeEvent.setRoadConditionId(checkedToString(avro.getRoadConditionId()));
        return dataLakeEvent;
    }
}
