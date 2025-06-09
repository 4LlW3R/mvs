package com.epam.tcodata.models;


import com.epam.tcodata.models.avro.dimension.*;
import com.epam.tcodata.models.avro.fact.*;
import com.epam.tcodata.models.datalake.raw.dimension.*;
import com.epam.tcodata.models.datalake.raw.fact.*;
import com.epam.tcodata.models.enriched.dimension.*;
import com.epam.tcodata.models.enriched.fact.*;
import com.epam.tcodata.models.exception.NonExistentEntityTypeException;
import com.epam.tcodata.models.mix.dimension.*;
import com.epam.tcodata.models.mix.fact.*;

/**
 * Entity type codification codes.
 */
public enum EntityType {

    //facts
    /**
     * Entity is GPS position fact.
     */
    POSITION(1,
            EntitySuperType.FACT,
            Position.class,
            EnrichedPosition.class,
            AvroPosition.class,
            RawPosition.class
    ),
    /**
     * Entity is recorded event fact.
     */
    EVENT(2,
            EntitySuperType.FACT,
            Event.class,
            EnrichedEvent.class,
            AvroEvent.class,
            RawEvent.class
    ),
    /**
     * Entity is trip fact.
     */
    TRIP(3,
            EntitySuperType.FACT,
            Trip.class,
            EnrichedTrip.class,
            AvroTrip.class,
            RawTrip.class
    ),
    /**
     * Entity is sub trip fact.
     */
    SUBTRIP(4,
            EntitySuperType.FACT,
            SubTrip.class,
            EnrichedSubTrip.class,
            AvroSubTrip.class,
            RawSubTrip.class
    ),

    //dimensions
    /**
     * Entity is driver dimension.
     */
    DRIVER(5,
            EntitySuperType.DIMENSION,
            Driver.class,
            EnrichedDriver.class,
            AvroDriver.class,
            RawDriver.class
    ),
    /**
     * Entity is vehicle dimension.
     */
    ASSET(6,
            EntitySuperType.DIMENSION,
            Asset.class,
            EnrichedAsset.class,
            AvroAsset.class,
            RawAsset.class
    ),
    /**
     * Entity is event description dimension.
     */
    LIBRARY_EVENT(7,
            EntitySuperType.DIMENSION,
            LibraryEvent.class,
            EnrichedLibraryEvent.class,
            AvroLibraryEvent.class,
            RawLibraryEvent.class
    ),
    /**
     * Entity is location dimension.
     */
    LOCATION(8,
            EntitySuperType.DIMENSION,
            Location.class,
            EnrichedLocation.class,
            AvroLocation.class,
            RawLocation.class
    ),
    /**
     * Entity is organisation group dimension.
     */
    ORGANISATION_GROUP(9,
            EntitySuperType.DIMENSION,
            OrganisationGroup.class,
            EnrichedOrganisationGroup.class,
            AvroOrganisationGroup.class,
            RawOrganisationGroup.class
    ),
    /**
     * Entity is tacho fact.
     */
    TACHO(10,
            EntitySuperType.FACT,
            Tacho.class,
            EnrichedTacho.class,
            AvroTacho.class,
            RawTacho.class
    ),
    /**
     * Entity is organisation subGroup dimension.
     */
    ORGANISATION_SUBGROUP(11,
            EntitySuperType.DIMENSION,
            OrganisationSubGroup.class,
            EnrichedOrganisationSubGroup.class,
            AvroOrganisationSubGroup.class,
            RawOrganisationSubGroup.class),
    /**
     * Detected event that is created from analytical entities (confirmed_overtaking_violation/road_condition_violation).
     * It comes from EH with confirmed violations (filled by side system), therefore it does not have enriched/avro class.
     * Added to {@link EntityType} to be trackable via HiveOffsets.
     */
    DETECTED_EVENT(12,
            EntitySuperType.FACT,
            null,
            null,
            null,
            RawDetectedEvent.class);

    private int code;
    private EntitySuperType superType;
    private Class<?> entityClass;
    private Class<?> enrichedClass;
    private Class<?> avroClass;
    private Class<?> rawDataLakeClass;

    EntityType(int code,
               EntitySuperType superType,
               Class<?> entityClass,
               Class<?> enrichedClass,
               Class<?> avroClass,
               Class<?> rawDataLakeClass) {

        this.code = code;
        this.superType = superType;
        this.entityClass = entityClass;
        this.enrichedClass = enrichedClass;
        this.avroClass = avroClass;
        this.rawDataLakeClass = rawDataLakeClass;
    }

    /**
     * Returns numeric representation of current code.
     *
     * @return int
     */
    public int getCode() {
        return this.code;
    }

    public EntitySuperType getSuperType() {
        return this.superType;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public Class<?> getEnrichedClass() {
        return enrichedClass;
    }

    public Class<?> getAvroClass() {
        return avroClass;
    }

    public Class<?> getRawDataLakeClass() {
        return rawDataLakeClass;
    }

    /**
     * Returns entity type by entity class.
     *
     * @param entityClass entity class
     * @return {@link EntityType}
     */
    public static EntityType byEntityClass(Class<?> entityClass) {
        for (EntityType value : values()) {
            if (value.entityClass == entityClass) {
                return value;
            }
        }
        throw new NonExistentEntityTypeException("For the class: " + entityClass);
    }

    /**
     * Returns entity type by raw data lake entity class.
     *
     * @param rawDataLakeClass data lake class
     * @return {@link EntityType}
     */
    public static EntityType byRawDataLakeClass(Class<?> rawDataLakeClass) {
        for (EntityType value : values()) {
            if (value.rawDataLakeClass == rawDataLakeClass) {
                return value;
            }
        }
        throw new NonExistentEntityTypeException("For the class: " + rawDataLakeClass);
    }
}
