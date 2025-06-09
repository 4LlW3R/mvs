package com.epam.tcodata.analytics.overtaking.violation.detection.policies.area;

import com.epam.tcodata.analytics.overtaking.violation.detection.factory.IOvertakingViolationFactory;
import com.epam.tcodata.redis.dal.IRedis;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AreasLookup {

    private static final Logger LOGGER = LoggerFactory.getLogger(AreasLookup.class);

    private static final String ROAD_CONDITIONS_KEY = "roadConditionsHashMap";
    private static final String NO_OVERTAKING_KEY = "NoOvertaking";
    private static final String SPEED_LIMITS_KEY = "SpeedLimits";

    private IOvertakingViolationFactory factory;

    /**
     * TO DO.
     */
    public AreasLookup(IOvertakingViolationFactory factory) {
        this.factory = factory;
    }

    public List<RoadConditionArea> getRoadConditionAreas() {
        return getAreas(ROAD_CONDITIONS_KEY, s -> new RoadConditionArea().fromJson(s), true);
    }

    public List<NoOvertakingArea> getNoOvertakingAreas() {
        return getAreas(NO_OVERTAKING_KEY, s -> new NoOvertakingArea().fromJson(s), false);
    }

    public List<SpeedZoneArea> getSpeedLimitsAreas() {
        return getAreas(SPEED_LIMITS_KEY, s -> new SpeedZoneArea().fromJson(s), false);
    }

    private <T extends IGeoJsonReadable> List<T> getAreas(String redisKey, Function<String, T> mapper, boolean isHash) {
        ISecretStorage secretStorage = this.factory.createSecretStorage();
        IRedis redis = this.factory.createAreaRedis(secretStorage);
        Collection<String> jsonStrings = isHash
                ? getAreasGeoJsonHash(redis, redisKey).values()
                : getAreasGeoJsonList(redis, redisKey);
        List<T> areas = jsonStrings
                .stream()
                .map(mapper)
                .collect(Collectors.toList());

        LOGGER.info("Got {} areas from Redis on key {}.", areas.size(), redisKey);
        return areas;
    }

    private Map<String, String> getAreasGeoJsonHash(IRedis redis, String key) {
        LOGGER.info("Key request to redis: {}", key);
        return redis.getMap(key);
    }

    private List<String> getAreasGeoJsonList(IRedis redis, String key) {
        LOGGER.info("Key request to redis: {}", key);
        return redis.getList(key);
    }
}
