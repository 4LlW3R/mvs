package com.epam.tcodata.hive.dal.domain.raw;

import com.epam.tcodata.hive.dal.HiveConfig;
import com.epam.tcodata.hive.dal.domain.IHiveEntityType;
import com.epam.tcodata.models.datalake.IDataLakeEntity;
import com.epam.tcodata.models.datalake.raw.dimension.*;
import com.epam.tcodata.models.datalake.raw.fact.*;

import java.util.Arrays;
import java.util.List;


/**
 * This enum defines all entities that can be stored into Raw data lake area.
 * Each element of this enum matches to the separate table into hive metastore for 'raw' database.
 */
public enum RawAreaEntityType implements IHiveEntityType {
    ASSET(RawAsset.class, false),
    ASSET_NORM(RawAsset.class, true),
    DRIVER(RawDriver.class, false),
    DRIVER_NORM(RawDriver.class, true),
    LIBRARY_EVENT(RawLibraryEvent.class, false),
    LIBRARY_EVENT_NORM(RawLibraryEvent.class, true),
    LOCATION(RawLocation.class, false),
    LOCATION_NORM(RawLocation.class, true),
    ORGANISATION_GROUP(RawOrganisationGroup.class, false),
    ORGANISATION_GROUP_NORM(RawOrganisationGroup.class, true),
    ORGANISATION_SUBGROUP(RawOrganisationSubGroup.class, false),
    ORGANISATION_SUBGROUP_NORM(RawOrganisationSubGroup.class, true),

    EVENT(RawEvent.class, false, "year", "week_number"),
    DETECTED_EVENT(RawDetectedEvent.class, false, "year", "week_number"),
    POSITION(RawPosition.class, false, "year", "week_number"),
    SUBTRIP(RawSubTrip.class, false, "year", "week_number"),
    TRIP(RawTrip.class, false, "year", "week_number"),
    TACHO(RawTacho.class, false, "year", "week_number");

    private Class<? extends IDataLakeEntity> entityClazz;
    private String[] partitions;
    private boolean norm;

    RawAreaEntityType(Class<? extends IDataLakeEntity> entityClazz, boolean norm, String... partitions) {
        this.entityClazz = entityClazz;
        this.norm = norm;
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
        return this.norm;
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
        return HiveConfig.RAW;
    }
}
