package com.epam.tcodata.hive.dal.domain.prepared;

import com.epam.tcodata.hive.dal.HiveConfig;
import com.epam.tcodata.hive.dal.domain.IHiveEntityType;
import com.epam.tcodata.models.datalake.IDataLakeEntity;
import com.epam.tcodata.models.datalake.prepared.analytics.PreparedConfirmedOvertakingViolation;
import com.epam.tcodata.models.datalake.prepared.analytics.PreparedRoadConditionViolation;
import com.epam.tcodata.models.datalake.prepared.analytics.PreparedValidatedEvent;
import com.epam.tcodata.models.datalake.prepared.dimension.*;
import com.epam.tcodata.models.datalake.prepared.fact.*;
import com.epam.tcodata.models.datalake.prepared.statics.*;

import java.util.Arrays;
import java.util.List;

/**
 * This enum defines all entities that can be stored into Prepared data lake area.
 * Each element of this enum matches to the separate table into hive metastore for 'prepared' database.
 */
public enum PreparedAreaEntityType implements IHiveEntityType {

    // dimensions
    DRIVER(PreparedDriver.class),
    EVENT_DESCRIPTION(PreparedEventDescription.class),
    GROUP(PreparedGroup.class),
    LOCATION(PreparedLocation.class),
    VEHICLE(PreparedVehicle.class),
    CONFIRMED_OVERTAKING_VIOLATION(PreparedConfirmedOvertakingViolation.class),
    ROAD_CONDITION_VIOLATION(PreparedRoadConditionViolation.class),

    // facts
    EVENT(PreparedEvent.class, "year", "week_number"),
    EVENT_VIDEO(PreparedEventVideo.class, "year", "week_number"),
    POSITION(PreparedPosition.class, "year", "week_number"),
    SUBTRIP(PreparedSubTrip.class, "year", "week_number"),
    TRIP(PreparedTrip.class, "year", "week_number"),
    TACHO(PreparedTacho.class, "year", "week_number"),
    VALIDATED_EVENT(PreparedValidatedEvent.class, "year", "week_number"),

    // static dimensions
    FUEL_TYPE(FuelType.class),
    GROUP_TYPE(GroupType.class),
    LOCATION_TYPE(LocationType.class),
    LOCATION_SHAPE_TYPE(LocationShapeType.class),
    VEHICLE_STATE(VehicleState.class),
    VEHICLE_TYPE(VehicleType.class),
    VIDEO_CHANNEL_TYPE(VideoChannelType.class),
    EVENT_VALIDATION_CODE(EventValidationCode.class),
    EVENT_PROBLEM_VEHICLE_CODE(EventProblemVehicleCode.class),
    OVERTAKING_VIOLATION_CODE(OvertakingViolationCode.class);

    private Class<? extends IDataLakeEntity> entityClazz;
    private String[] partitions;

    PreparedAreaEntityType(Class<? extends IDataLakeEntity> entityClazz, String... partitions) {
        this.entityClazz = entityClazz;
        this.partitions = partitions;
    }

    @Override
    public List<IHiveEntityType> entityTypes() {
        return Arrays.asList(values());
    }

    @Override
    public Class<? extends IDataLakeEntity> getEntityClazz() {
        return this.entityClazz;
    }

    @Override
    public boolean isNorm() {
        return false;
    }

    @Override
    public String[] partitions() {
        return this.partitions == null ? null : Arrays.copyOf(this.partitions, this.partitions.length);
    }

    @Override
    public int getCode() {
        return this.ordinal();
    }

    @Override
    public String entityName() {
        return this.name();
    }

    @Override
    public HiveConfig config() {
        return HiveConfig.PREPARED;
    }
}
