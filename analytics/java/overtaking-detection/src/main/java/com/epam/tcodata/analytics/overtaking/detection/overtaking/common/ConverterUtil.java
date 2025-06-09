package com.epam.tcodata.analytics.overtaking.detection.overtaking.common;

import com.epam.tcodata.analytics.overtaking.detection.overtaking.common.types.OvertakingEvent;
import com.epam.tcodata.analytics.overtaking.detection.overtaking.common.types.OvertakingPathSegment;
import com.epam.tcodata.analytics.overtaking.detection.overtaking.gps.types.GPSPoint;
import com.epam.tcodata.models.avro.fact.AvroPosition;
import com.epam.tcodata.storage.avro.entities.events.overtaking.OvertakingEventAvro;
import com.epam.tcodata.storage.avro.entities.events.overtaking.OvertakingPathSegmentAvro;
import com.epam.tcodata.storage.events.DomainCode;
import com.epam.tcodata.storage.events.EventType;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.Serializable;
import java.sql.Timestamp;

import static com.epam.tcodata.common.ConverterUtils.checkedToString;

public class ConverterUtil implements Serializable {

    /**
     * Converts {@link AvroPosition} to {@link GPSPoint}.
     *
     * @param avroPosition {@link AvroPosition}.
     * @return {@link GPSPoint}.
     */
    public static GPSPoint avroPositionToGPSPoint(AvroPosition avroPosition) {
        GPSPoint point = new GPSPoint();
        point.setLatitude(avroPosition.getLatitude());
        point.setLongitude(avroPosition.getLongitude());
        point.setTime(new Timestamp(avroPosition.getTimestamp().getMillis()));
        point.setVelocity(avroPosition.getSpeedKilometresPerHour());
        point.setVehicleDurableId(checkedToString(avroPosition.getVehicleDurableKey())); // TO DO filter UNKNOWN durable keys
        point.setDriverDurableId(checkedToString(avroPosition.getDriverDurableKey())); // TO DO filter UNKNOWN durable keys
        return point;
    }

    /**
     * Converts {@link OvertakingEvent} to {@link OvertakingEventAvro}.
     *
     * @param overtakingEvent {@link OvertakingEvent}.
     * @return {@link OvertakingEventAvro}.
     */
    public static OvertakingEventAvro convertOvertakingEventToAvro(OvertakingEvent overtakingEvent) {
        return OvertakingEventAvro.newBuilder()
                .setDomain(DomainCode.DOMAIN_OVERTAKING.getDomain())
                .setEntityType(EventType.OVERTAKING.getEventTypeId())
                .setSchemaVersion(1)
                .setId(overtakingEvent.getOvertakingId())
                .setAOvertookB(overtakingEvent.getCorrectIdx(overtakingEvent.getPassingVehicleIndicator()))
                .setVehicleDurableIdA(overtakingEvent.getVehicleDurableIdA())
                .setVehicleDurableIdB(overtakingEvent.getVehicleDurableIdB())
                .setDriverDurableIdA(overtakingEvent.getDriverDurableIdA())
                .setDriverDurableIdB(overtakingEvent.getDriverDurableIdB())
                .setVelocityA(overtakingEvent.getSpeedA())
                .setVelocityB(overtakingEvent.getSpeedB())
                .setTime(new DateTime(overtakingEvent.getOvertakingTime()).withZoneRetainFields(DateTimeZone.UTC))
                .setLatitude(overtakingEvent.getOvertakingLatitude())
                .setLongitude(overtakingEvent.getOvertakingLongitude())
                .setTrajectoryA(convertOvertakingPathSegmentToAvro(overtakingEvent.getTrajectoryA()))
                .setTrajectoryB(convertOvertakingPathSegmentToAvro(overtakingEvent.getTrajectoryB()))
                .build();
    }

    private static OvertakingPathSegmentAvro convertOvertakingPathSegmentToAvro(OvertakingPathSegment overtakingPathSegment) {
        return OvertakingPathSegmentAvro.newBuilder()
                .setStartLatitude(overtakingPathSegment.getStartLatitude())
                .setStartLongitude(overtakingPathSegment.getStartLongitude())
                .setStartTime(new DateTime(overtakingPathSegment.getStartTime()).withZoneRetainFields(DateTimeZone.UTC))
                .setEndLatitude(overtakingPathSegment.getEndLatitude())
                .setEndLongitude(overtakingPathSegment.getEndLongitude())
                .setEndTime(new DateTime(overtakingPathSegment.getEndTime()).withZoneRetainFields(DateTimeZone.UTC))
                .build();
    }
}
