package com.epam.tcodata.external.pump.converter.impl;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.dto.dimension.DimensionDto;
import com.epam.tcodata.mdm.Decision;
import com.epam.tcodata.mdm.IKeyManager;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.avro.dimension.AvroLocation;
import com.epam.tcodata.models.enriched.dimension.EnrichedLocation;
import com.epam.tcodata.models.mix.dimension.Location;
import com.epam.tcodata.sql.dal.domain.mdm.ApiVersion;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.sql.Timestamp;
import java.time.Instant;

import static com.epam.tcodata.external.pump.util.ConverterUtil.timestampToDateTime;

public class LocationConverter implements IConverter<Location, EnrichedLocation, AvroLocation> {

    private static final long serialVersionUID = -5522470024474971953L;

    // general properties
    private static final int AVRO_SCHEMA_VERSION = 1;

    @Override
    public EnrichedLocation convertToEnriched(Location location, AbstractDto dto, IKeyManager keyManager) {
        EnrichedLocation enrichedLocation = new EnrichedLocation(location);

        Decision decision = keyManager.findOrCreate(location, ApiVersion.API_2_0, String.valueOf(((DimensionDto) dto).getOrgGroupId()), EntityType.LOCATION);

        String surrogateKeyStr = decision == null ? null : decision.getSurrogateKey().toString();

        enrichedLocation

                //enriched with additional info
                .setDurableId(surrogateKeyStr) //TO DO
                .setIngestedDateUtc(Timestamp.from(Instant.now()))
                .setSubscriptionId(((DimensionDto) dto).getOrgGroupId())
                .setLineageCode(4);

        return enrichedLocation;
    }

    @Override
    public AvroLocation convertToAvro(EnrichedLocation enrichedLocation) {
        return AvroLocation.newBuilder()

                // enriched fields
                .setDurableId(enrichedLocation.getDurableId())
                .setIngestedDateUtc(timestampToDateTime(enrichedLocation.getIngestedDateUtc()))
                .setSubscriptionId(enrichedLocation.getSubscriptionId())
                .setLineageCode(enrichedLocation.getLineageCode())

                // transport message support
                .setSchemaVersion(AVRO_SCHEMA_VERSION)

                // main fields
                .setLocationId(enrichedLocation.getLocationId())
                .setOrganisationId(enrichedLocation.getOrganisationId())
                .setGroupId(enrichedLocation.getGroupId())
                .setName(enrichedLocation.getName())
                .setAddress(enrichedLocation.getAddress())
                .setLocationType(enrichedLocation.getLocationType())
                .setShapeType(enrichedLocation.getShapeType())
                .setRadius(enrichedLocation.getRadius())
                .setShapeWkt(enrichedLocation.getShapeWkt())
                .setDeleted(enrichedLocation.getDeleted())
                .setColourOnMap(enrichedLocation.getColourOnMap())
                .setOpacityOnMap(enrichedLocation.getOpacityOnMap())
                .setTemporary(enrichedLocation.getTemporary())
                .setExternalReference(enrichedLocation.getExternalReference())
                .build();
    }

    @Override
    public Dataset<Row> convertToDataset(SparkSession sparkSession, JavaRDD enrichedEntityJavaRDD) {
        JavaRDD<Row> rows = prepareForWriting(enrichedEntityJavaRDD);
        return sparkSession.createDataFrame(rows, getDataLakeSchema());
    }

    private JavaRDD<Row> prepareForWriting(JavaRDD<EnrichedLocation> enrichedEntityJavaRDD) {
        return enrichedEntityJavaRDD.map((Function<EnrichedLocation, Row>) enrichedLocation ->
                RowFactory.create(enrichedLocation.getOrderedValues()));
    }

    @SuppressWarnings("CPD-START")
    StructType getDataLakeSchema() {
        return new StructType(
                new StructField[] {
                        DataTypes.createStructField("durable_id", DataTypes.StringType, true),
                        DataTypes.createStructField("ingested_date_utc", DataTypes.TimestampType, true),
                        DataTypes.createStructField("subscription_id", DataTypes.LongType, true),
                        DataTypes.createStructField("lineage_code", DataTypes.IntegerType, true),
                        DataTypes.createStructField("location_id", DataTypes.LongType, true),
                        DataTypes.createStructField("organisation_id", DataTypes.LongType, true),
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
