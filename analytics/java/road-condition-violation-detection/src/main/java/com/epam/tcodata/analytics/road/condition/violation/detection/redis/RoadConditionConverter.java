package com.epam.tcodata.analytics.road.condition.violation.detection.redis;

import com.epam.tcodata.analytics.road.condition.violation.detection.domain.GPSPoint;
import com.epam.tcodata.analytics.road.condition.violation.detection.domain.RoadConditionViolation;
import com.epam.tcodata.models.avro.fact.AvroPosition;
import com.epam.tcodata.storage.avro.entities.events.violations.v2.RoadConditionViolationAvro;
import com.epam.tcodata.storage.events.DomainCode;
import com.epam.tcodata.storage.events.EventType;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

import static com.epam.tcodata.common.ConverterUtils.checkedToString;

public class RoadConditionConverter implements Serializable {

    private static final long serialVersionUID = -5076033829323925130L;

    private static final int ROAD_CONDITION_VIOLATION_SCHEMA_VERSION = 2;

    public RoadConditionConverter() {
        /***  Default implementation ***/
    }

    /**
     * Converts {@link AvroPosition} ot {@link GPSPoint}.
     *
     * @param avro avro position entity
     * @return {@link GPSPoint}
     */
    public static GPSPoint convertToGPSPoint(AvroPosition avro) {
        return null == avro ? null : new GPSPoint()
                .setLatitude(avro.getLatitude())
                .setLongitude(avro.getLongitude())
                .setTime(new Timestamp(avro.getTimestamp().getMillis()))
                .setVelocity(avro.getSpeedKilometresPerHour())
                .setVehicleId(checkedToString(avro.getVehicleDurableKey()))
                .setDriverId(checkedToString(avro.getDriverDurableKey()))
                .setSubscriptionId(avro.getSubscriptionId())
                .setExternalId(String.valueOf(avro.getPositionId()));
    }

    /**
     * Serialize domain entity to avro.
     */
    public static RoadConditionViolationAvro convertToAvro(Iterable<RoadConditionViolation> viol) {
        if (!viol.iterator().hasNext())
            return null;
        return
                RoadConditionViolationAvro.newBuilder()
                .setDomain(DomainCode.DOMAIN_ROAD_CONDITION.getDomain())
                .setEntityType(EventType.ROAD_CONDITION_VIOLATION_EVENT.getEventTypeId())
                .setSchemaVersion(ROAD_CONDITION_VIOLATION_SCHEMA_VERSION)
                .setId(UUID.randomUUID().toString())
                .setSubscriptionId(viol.iterator().next().getSubscriptionId())
                .setVehicleId(viol.iterator().next().getVehicleId())
                .setDriverId(viol.iterator().next().getDriverId())
                .setCreationTime(fromTimestamp(viol.iterator().next().getCreationTimeUTC()))
                .setStartTime(fromTimestamp(viol.iterator().next().getStartTime()))
                .setEndTime(fromTimestamp(viol.iterator().next().getEndTime()))
                .setRoadConditionId(viol.iterator().next().getPolicyAreaId())
                .setAverageSpeed(viol.iterator().next().getAverageSpeed())
                .setMaxSpeed(viol.iterator().next().getMaxSpeed())
                .setStartFactGpsId(viol.iterator().next().getStartFactGpsId())
                .setEndFactGpsId(viol.iterator().next().getEndFactGpsId())
                .setStartLatitude(viol.iterator().next().getStartLatitude())
                .setStartLongitude(viol.iterator().next().getStartLongitude())
                .setEndLatitude(viol.iterator().next().getEndLatitude())
                .setEndLongitude(viol.iterator().next().getEndLongitude())
                .build();
    }

    private static DateTime fromTimestamp(Timestamp ts) {
        return new DateTime(ts.getTime(), DateTimeZone.UTC);
    }
}
