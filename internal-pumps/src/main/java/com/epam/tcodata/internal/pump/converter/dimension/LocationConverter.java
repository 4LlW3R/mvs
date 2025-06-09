package com.epam.tcodata.internal.pump.converter.dimension;

import com.epam.tcodata.internal.pump.converter.AbstractEntityConverter;
import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.models.avro.dimension.AvroLocation;
import com.epam.tcodata.models.datalake.raw.dimension.RawLocation;
import com.epam.tcodata.models.enriched.dimension.EnrichedLocation;
import com.epam.tcodata.models.mix.dimension.Location;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.sql.Timestamp;

import static com.epam.tcodata.common.ConverterUtils.checkedToString;

public class LocationConverter extends AbstractEntityConverter<AvroLocation, EnrichedLocation, RawLocation> {

    private static final long serialVersionUID = 9131603941301697698L;

    /**
     * Main public constructor.
     */
    public LocationConverter() {
        /***  Default implementation ***/
    }

    /**
     * Method converts avro entity to enriched entity.
     *
     * @return EnrichedLocation
     */
    @Override
    public EnrichedLocation convertToEnriched(AvroLocation avro) {
        Location.LocationBuilder locationBuilder = new Location.LocationBuilder()
                .setLocationId(avro.getLocationId())
                .setOrganisationId(avro.getOrganisationId())
                .setGroupId(avro.getGroupId())
                .setName(checkedToString(avro.getName()))
                .setAddress(checkedToString(avro.getAddress()))
                .setLocationType(checkedToString(avro.getLocationType()))
                .setShapeType(checkedToString(avro.getShapeType()))
                .setRadius(avro.getRadius())
                .setShapeWkt(checkedToString(avro.getShapeWkt()))
                .setDeleted(avro.getDeleted())
                .setColourOnMap(checkedToString(avro.getColourOnMap()))
                .setOpacityOnMap(avro.getOpacityOnMap())
                .setTemporary(avro.getTemporary())
                .setExternalReference(checkedToString(avro.getExternalReference()));

        Location location = locationBuilder.build();

        return new EnrichedLocation(location)
                // IEnrichable fields
                .setDurableId(checkedToString(avro.getDurableId()))
                .setIngestedDateUtc(IEntityConverter.dateTimeToTimestamp(avro.getIngestedDateUtc()))
                .setSubscriptionId(avro.getSubscriptionId())
                .setLineageCode(avro.getLineageCode());
    }

    /**
     * Method converts enriched entity to dataLake entity.
     *
     * @return DataLakeLocation
     */
    @Override
    public RawLocation convertToRaw(EnrichedLocation enriched, Timestamp persistedDate) {
        RawLocation dataLakeLocation = new RawLocation();
        // DataLakeEntity fields
        dataLakeLocation.setDurableId(enriched.getDurableId());
        dataLakeLocation.setIngestedDateUtc(enriched.getIngestedDateUtc());
        dataLakeLocation.setSubscriptionId(enriched.getSubscriptionId());
        dataLakeLocation.setLineageCode(enriched.getLineageCode());
        dataLakeLocation.setPersistedDateUtc(persistedDate);
        // DataLakeLocation fields
        dataLakeLocation.setLocationId(enriched.getLocationId());
        dataLakeLocation.setGroupId(enriched.getGroupId());
        dataLakeLocation.setName(enriched.getName());
        dataLakeLocation.setAddress(enriched.getAddress());
        dataLakeLocation.setLocationType(enriched.getLocationType());
        dataLakeLocation.setShapeType(enriched.getShapeType());
        dataLakeLocation.setRadius(enriched.getRadius());
        dataLakeLocation.setShapeWkt(enriched.getShapeWkt());
        dataLakeLocation.setDeleted(enriched.getDeleted());
        dataLakeLocation.setColourOnMap(enriched.getColourOnMap());
        dataLakeLocation.setOpacityOnMap(enriched.getOpacityOnMap());
        dataLakeLocation.setTemporary(enriched.getTemporary());
        dataLakeLocation.setExternalReference(enriched.getExternalReference());
        return dataLakeLocation;
    }

    @Override
    public StructType getSchemaForWrite() {
        return new StructType(
                new StructField[]{
                        DataTypes.createStructField("durable_id", DataTypes.StringType, true),
                        DataTypes.createStructField("ingested_date_utc", DataTypes.TimestampType, true),
                        DataTypes.createStructField("subscription_id", DataTypes.LongType, true),
                        DataTypes.createStructField("lineage_code", DataTypes.IntegerType, true),
                        DataTypes.createStructField("persisted_date_utc", DataTypes.TimestampType, true),
                        DataTypes.createStructField("location_id", DataTypes.LongType, true),
                        DataTypes.createStructField("group_id", DataTypes.LongType, true),
                        DataTypes.createStructField("name", DataTypes.StringType, true),
                        DataTypes.createStructField("address", DataTypes.StringType, true),
                        DataTypes.createStructField("location_type", DataTypes.StringType, true),
                        DataTypes.createStructField("shape_type", DataTypes.StringType, true),
                        DataTypes.createStructField("radius", DataTypes.DoubleType, true),
                        DataTypes.createStructField("shape_wkt", DataTypes.StringType, true),
                        DataTypes.createStructField("deleted", DataTypes.BooleanType, true),
                        DataTypes.createStructField("colour_on_map", DataTypes.StringType, true),
                        DataTypes.createStructField("opacity_on_map", DataTypes.DoubleType, true),
                        DataTypes.createStructField("temporary", DataTypes.BooleanType, true),
                        DataTypes.createStructField("external_reference", DataTypes.StringType, true)
                }
        );
    }
}
