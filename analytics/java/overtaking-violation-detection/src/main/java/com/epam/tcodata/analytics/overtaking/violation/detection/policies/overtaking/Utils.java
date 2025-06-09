package com.epam.tcodata.analytics.overtaking.violation.detection.policies.overtaking;

import com.epam.tcodata.analytics.overtaking.violation.detection.policies.area.GeoUtils;
import com.epam.tcodata.analytics.overtaking.violation.detection.policies.area.SpeedZoneArea;
import com.epam.tcodata.storage.avro.entities.events.overtaking.OvertakingEventAvro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

public class Utils {
    private Utils(){}

    public static final double DEFAULT_SPEED_LIMIT = 70.0;
    public static final TimeZone TENGIZ_TIMEZONE = TimeZone.getTimeZone("GMT+5");   //NOSONAR {subscription_id}_{vehicle_id}
    public static final String REDIS_KEY = "%s";
    private static final int SLOW_MOVING_VEHICLE_SPEED_DIFFERENCE = 20;
    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    /**
     * Returns true if {@link OvertakingEventAvro} has one or more participating slow moving vehicles (SMV) according to
     * overtaking policy.
     *
     * @return - true if there is SMV, false otherwise.
     */
    public static boolean slowMovingVehicleIsPresent(OvertakingEventAvro event, double speedLimit) {
        double smvSpeedLimit = speedLimit - SLOW_MOVING_VEHICLE_SPEED_DIFFERENCE;
        return event.getVelocityA() < smvSpeedLimit || event.getVelocityB() < smvSpeedLimit;
    }

    /**
     * Returns speed limit depending on area, to which the input point belongs.
     *
     * @param lon - longitude
     * @param lat - latitude
     * @return - most strict (i.e. with lowest speed limit) area
     */
    public static SpeedZoneArea getCorrectSpeedZone(double lon, double lat, List<SpeedZoneArea> areas) {
        // NOSONAR in case of intersecting areas choose the most strict policy
        Optional<SpeedZoneArea> strictZone = areas
                .stream()
                .filter(a -> GeoUtils.pointIsInArea(lon, lat, a.getGeometry()))
                .min(Comparator.comparingDouble(SpeedZoneArea::getSpeedLimit));

        LOGGER.debug("Speed zone area: [use default instead of empty = {}] data={}",
                !strictZone.isPresent(),
                strictZone.orElse(SpeedZoneArea.getDefaultSpeedZoneArea()));
        return strictZone.orElse(SpeedZoneArea.getDefaultSpeedZoneArea());
    }


}
