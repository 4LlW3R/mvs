package com.epam.tcodata.analytics.overtaking.violation.detection.policies.overtaking;

import com.epam.tcodata.analytics.overtaking.violation.detection.entities.OvertakingMetaData;
import com.epam.tcodata.analytics.overtaking.violation.detection.entities.Violation;
import com.epam.tcodata.analytics.overtaking.violation.detection.factory.IOvertakingViolationFactory;
import com.epam.tcodata.analytics.overtaking.violation.detection.policies.IPolicy;
import com.epam.tcodata.redis.dal.IRedis;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class BusOvertaking implements IPolicy<OvertakingMetaData> {
    private static final String BUS_TYPE = "BUS";
    private static final String VEHICLE_DESCRIPTION_FIELD = "Description";
    private static final String UNKNOWN_VEHICLE_DESCRIPTION = "Unknown";
    private static final long serialVersionUID = -3929820298764458179L;
    private static final Logger LOGGER = LoggerFactory.getLogger(BusOvertaking.class);

    private IOvertakingViolationFactory factory;

    public BusOvertaking(IOvertakingViolationFactory factory) {
        this.factory = factory;
    }

    @Override
    /*
      Because the algorithm doesn't tell who overtook who, this violation only checks if any of two vehicle is a bus.
     */
    public Violation applyPolicy(OvertakingMetaData entity) {
        Violation violation;
        ISecretStorage secretStorage = this.factory.createSecretStorage();
        IRedis redis = this.factory.createVehicleRedis(secretStorage);
        LOGGER.debug("Speed zone area for event {}: {}", entity.getEvent().getId(), entity.getSpeedZoneArea().getId());
        switch (entity.getEvent().getAOvertookB()) {
            case NOT_DETECTED:
                if (busParticipated(redis, entity)) {
                    violation = new Violation(Violation.Type.BUS_OVERTAKING);
                } else {
                    violation = Violation.NO_VIOLATION;
                }
                break;
            case A_OVERTOOK_B:
                String keyA = String.format(Utils.REDIS_KEY, entity.getEvent().getVehicleDurableIdA());
                if (isVehicleBus(redis, keyA)) {
                    violation = new Violation(Violation.Type.BUS_OVERTAKING);
                } else {
                    violation = Violation.NO_VIOLATION;
                }
                break;
            case B_OVERTOOK_A:
                String keyB = String.format(Utils.REDIS_KEY, entity.getEvent().getVehicleDurableIdB());
                if (isVehicleBus(redis, keyB)) {
                    violation = new Violation(Violation.Type.BUS_OVERTAKING);
                } else {
                    violation = Violation.NO_VIOLATION;
                }
                break;
            default:
                violation = Violation.NO_VIOLATION;
                break;
        }
        return violation;
    }

    private boolean busParticipated(IRedis redis, OvertakingMetaData entity) {
        LOGGER.debug("Speed limit for event {}: {}", entity.getEvent().getId(), entity.getSpeedZoneArea().getSpeedLimit());
        String keyA = String.format(Utils.REDIS_KEY, entity.getEvent().getVehicleDurableIdA());
        String keyB = String.format(Utils.REDIS_KEY, entity.getEvent().getVehicleDurableIdB());
        return isVehicleBus(redis, keyA) || isVehicleBus(redis, keyB);
    }

    private boolean isVehicleBus(IRedis redis, String key) {
        Map<String, String> vehicle = redis.getMap(key);
        LOGGER.debug("Vehicle description for key {}: {}", key, vehicle);
        String description = vehicle.getOrDefault(VEHICLE_DESCRIPTION_FIELD, UNKNOWN_VEHICLE_DESCRIPTION);
        return descriptionContainsBus(description);
    }

    private boolean descriptionContainsBus(String s) {
        return s.toUpperCase().contains(BUS_TYPE);
    }
}
