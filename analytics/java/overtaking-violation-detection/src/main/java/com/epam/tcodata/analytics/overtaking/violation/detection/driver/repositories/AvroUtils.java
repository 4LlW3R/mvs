package com.epam.tcodata.analytics.overtaking.violation.detection.driver.repositories;

import com.epam.tcodata.analytics.overtaking.violation.detection.entities.Violation;
import com.epam.tcodata.analytics.overtaking.violation.detection.policies.PolicyVersion;
import com.epam.tcodata.storage.avro.entities.events.overtaking.OvertakingEventAvro;
import com.epam.tcodata.storage.avro.entities.events.overtaking.OvertakingEventWithViolationsAvro;
import com.epam.tcodata.storage.events.EventType;

import java.util.List;
import java.util.stream.Collectors;

public class AvroUtils {
    private AvroUtils(){}

    /**
     * Enriches existing overtaking event with information about detected violations, if any.     *
     *
     * @return Avro-serialized entity
     */
    public static OvertakingEventWithViolationsAvro enrichWithViolations(
            OvertakingEventAvro overtakingEvent,
            List<Violation> violations) {
        List<CharSequence> areaIds = violations.stream()
                .filter(Violation::filterAreas)
                .map(Violation::getPolicyAreaId)
                .distinct()
                .collect(Collectors.toList());
        List<Integer> violationIds = violations.stream()
                .map(Violation::getViolationId)
                .collect(Collectors.toList());
        return OvertakingEventWithViolationsAvro.newBuilder()
                .setDomain(overtakingEvent.getDomain())
                .setEntityType(EventType.OVERTAKING_WITH_VIOLATIONS.getEventTypeId())
                .setSchemaVersion(overtakingEvent.getSchemaVersion())
                .setId(overtakingEvent.getId())
                .setPolicyVersion(PolicyVersion.DEFAULT_POLICY.getId())
                .setVehicleDurableIdA(overtakingEvent.getVehicleDurableIdA())
                .setVehicleDurableIdB(overtakingEvent.getVehicleDurableIdB())
                .setDriverDurableIdA(overtakingEvent.getDriverDurableIdA())
                .setDriverDurableIdB(overtakingEvent.getDriverDurableIdB())
                .setVelocityA(overtakingEvent.getVelocityA())
                .setVelocityB(overtakingEvent.getVelocityB())
                .setTrajectoryA(overtakingEvent.getTrajectoryA())
                .setTrajectoryB(overtakingEvent.getTrajectoryB())
                .setTime(overtakingEvent.getTime())
                .setLatitude(overtakingEvent.getLatitude())
                .setLongitude(overtakingEvent.getLongitude())
                .setPolicyAreaIDs(areaIds)
                .setViolationIDs(violationIds)
                .setAOvertookB(overtakingEvent.getAOvertookB())
                .build();
    }

}
