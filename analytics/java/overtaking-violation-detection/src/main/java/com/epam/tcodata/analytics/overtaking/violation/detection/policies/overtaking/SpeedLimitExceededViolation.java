package com.epam.tcodata.analytics.overtaking.violation.detection.policies.overtaking;

import com.epam.tcodata.analytics.overtaking.violation.detection.entities.OvertakingMetaData;
import com.epam.tcodata.analytics.overtaking.violation.detection.entities.Violation;
import com.epam.tcodata.analytics.overtaking.violation.detection.policies.IPolicy;
import com.epam.tcodata.storage.avro.entities.events.overtaking.OvertakingEventAvro;

public class SpeedLimitExceededViolation implements IPolicy<OvertakingMetaData> {

    private static Violation checkSpeedLimit(OvertakingMetaData meta) {
        double speedLimit = meta.getSpeedZoneArea().getSpeedLimit();
        OvertakingEventAvro event = meta.getEvent();
        if (event.getVelocityA() > speedLimit || event.getVelocityB() > speedLimit)
            return new Violation(meta.getSpeedZoneArea().getId(), Violation.Type.SPEED_LIMIT_EXCEEDED_OVERTAKING);
        return Violation.NO_VIOLATION;
    }

    @Override
    public Violation applyPolicy(OvertakingMetaData entity) {
        return checkSpeedLimit(entity);
    }
}
