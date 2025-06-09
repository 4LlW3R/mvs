package com.epam.tcodata.analytics.overtaking.violation.detection.policies.overtaking;

import com.epam.tcodata.analytics.overtaking.violation.detection.entities.OvertakingMetaData;
import com.epam.tcodata.analytics.overtaking.violation.detection.entities.Violation;
import com.epam.tcodata.analytics.overtaking.violation.detection.policies.IPolicy;
import com.epam.tcodata.storage.avro.entities.events.overtaking.OvertakingEventAvro;

public class VehicleInFrontIsFastViolation implements IPolicy<OvertakingMetaData> {

    private static final double OVERTAKING_SPEED_OFFSET = 10.0;

    private static Violation checkIfSpeedDifferenceIsSmall(OvertakingMetaData meta) {
        double overtakingSpeedLimit = meta.getSpeedZoneArea().getSpeedLimit() - OVERTAKING_SPEED_OFFSET;
        OvertakingEventAvro event = meta.getEvent();
        if (event.getVelocityB() > overtakingSpeedLimit && event.getVelocityA() > overtakingSpeedLimit)
            return new Violation(meta.getSpeedZoneArea().getId(), Violation.Type.VEHICLE_IN_FRONT_IS_FAST_OVERTAKING);
        return Violation.NO_VIOLATION;
    }

    @Override
    public Violation applyPolicy(OvertakingMetaData entity) {
        return checkIfSpeedDifferenceIsSmall(entity);
    }
}
