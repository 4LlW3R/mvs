package com.epam.tcodata.external.pump.converter.impl;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.dto.fact.FactDto;
import com.epam.tcodata.external.pump.util.KeyManagerUtil;
import com.epam.tcodata.mdm.IKeyManager;
import com.epam.tcodata.mdm.SearchingResult;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.avro.fact.AvroPosition;
import com.epam.tcodata.models.enriched.fact.EnrichedPosition;
import com.epam.tcodata.models.mix.fact.Position;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.epam.tcodata.external.pump.util.ConverterUtil.timestampToDateTime;

public class PositionConverter implements IConverter<Position, EnrichedPosition, AvroPosition> {

    private static final long serialVersionUID = 3012071650248695003L;

    private static final Logger LOGGER = LoggerFactory.getLogger(PositionConverter.class);

    // general properties
    private static final int AVRO_SCHEMA_VERSION = 1;

    @Override
    public EnrichedPosition convertToEnriched(Position position, AbstractDto dto, IKeyManager keyManager) {
        // copying main fields
        EnrichedPosition enrichedPosition = new EnrichedPosition(position);

        Map<EntityType, List<SearchingResult>> substitutionMap = keyManager.keysSubstitution(
                position,
                ApiVersion.API_2_0,
                String.valueOf(((FactDto) dto).getOrgGroupId()),
                EntityType.POSITION);

        // enriched with surrogate keys
        try {
            KeyManagerUtil.enrichEntity(enrichedPosition, substitutionMap);
        } catch (IllegalAccessException e) {
            LOGGER.error(e.getMessage(), e);
        }

        String durableKey = keyManager.factDurableKey(EntityType.POSITION,
                "" + position.getPositionId()).toString();

        enrichedPosition

                //enriched with additional info
                .setDurableId(durableKey)
                .setIngestedDateUtc(Timestamp.from(Instant.now()))
                .setSubscriptionId(((FactDto) dto).getOrgGroupId())
                .setLineageCode(4);

        LOGGER.info(
                "#position-converter-external-pump# Position with id: {} and durable id: {} was converted to the enriched position",
                enrichedPosition.getPositionId(),
                enrichedPosition.getDurableId());

        return enrichedPosition;
    }

    @Override
    public List<EnrichedPosition> convertListToEnriched(List<Position> positions, AbstractDto<Position> dto, IKeyManager keyManager) {
        long subscriptionId = ((FactDto) dto).getOrgGroupId();

        // copying main fields
        List<EnrichedPosition> enrichedPositions = new ArrayList<>();
        positions.forEach(position -> enrichedPositions.add(new EnrichedPosition(position)));
        Map<EntityType, List<SearchingResult>> substitutionMap = keyManager.keysSubstitutions(
                new ArrayList<Object>(positions),
                ApiVersion.API_2_0,
                String.valueOf(subscriptionId),
                EntityType.POSITION);

        // Simple checking. Should be the same size.
        substitutionMap.entrySet().forEach(entry -> {
            if (enrichedPositions.size() != entry.getValue().size()) {
                throw new IllegalStateException("Count for each entity should be the same as positions number: positions "
                        + enrichedPositions.size() + " vs entity " + entry.getKey() + " " + entry.getValue().size());
            }
        });

        // enriched with surrogate keys
        List<Object> preparedEntitiesForEnrichment = new ArrayList<>(enrichedPositions);
        try {
            KeyManagerUtil.enrichEntity(preparedEntitiesForEnrichment, substitutionMap);
        } catch (IllegalAccessException e) {
            LOGGER.error(e.getMessage(), e);
        }

        Timestamp ingestedDateUtc = Timestamp.from(Instant.now());
        long groupId = ((FactDto) dto).getOrgGroupId();
        enrichedPositions.forEach(enrichedPosition -> {
            String durableKey = keyManager.factDurableKey(EntityType.POSITION,
                    "" + enrichedPosition.getPositionId()).toString();
            enrichedPosition
                    //enriched with additional info
                    .setDurableId(durableKey)
                    .setIngestedDateUtc(ingestedDateUtc)
                    .setSubscriptionId(groupId)
                    .setLineageCode(4);
        });

        return enrichedPositions;
    }


    @Override
    public AvroPosition convertToAvro(EnrichedPosition enrichedPosition) {
        if (enrichedPosition == null) {
            return null;
        }
        return AvroPosition.newBuilder()

                // enriched fields
                .setDurableId(enrichedPosition.getDurableId())
                .setIngestedDateUtc(timestampToDateTime(enrichedPosition.getIngestedDateUtc()))
                .setSubscriptionId(enrichedPosition.getSubscriptionId())
                .setLineageCode(enrichedPosition.getLineageCode())
                .setDriverDurableKey(enrichedPosition.getDriverDurableKey())
                .setVehicleDurableKey(enrichedPosition.getVehicleDurableKey())

                // transport message support
                .setSchemaVersion(AVRO_SCHEMA_VERSION)

                // main fields
                .setPositionId(enrichedPosition.getPositionId())
                .setAssetId(enrichedPosition.getAssetId())
                .setDriverId(enrichedPosition.getDriverId())
                .setTimestamp(timestampToDateTime(enrichedPosition.getTimestamp()))
                .setLatitude(enrichedPosition.getLatitude())
                .setLongitude(enrichedPosition.getLongitude())
                .setSpeedKilometresPerHour(enrichedPosition.getSpeedKilometresPerHour())
                .setSpeedLimit(enrichedPosition.getSpeedLimit())
                .setAltitudeMetres(enrichedPosition.getAltitudeMetres())
                .setHeading(enrichedPosition.getHeading())
                .setNumberOfSatellites(enrichedPosition.getNumberOfSatellites())
                .setHdop(enrichedPosition.getHdop())
                .setVdop(enrichedPosition.getVdop())
                .setPdop(enrichedPosition.getPdop())
                .setAgeOfReadingSeconds(enrichedPosition.getAgeOfReadingSeconds())
                .setDistanceSinceReadingKilometres(enrichedPosition.getDistanceSinceReadingKilometres())
                .setIgnitionOn(enrichedPosition.getIgnitionOn())
                .setOdometerKilometres(enrichedPosition.getOdometerKilometres())
                .setFormattedAddress(enrichedPosition.getFormattedAddress())
                .setSource(enrichedPosition.getSource())
                .setAvl(enrichedPosition.getAvl())
                .build();
    }

    @Override
    public Dataset<Row> convertToDataset(SparkSession sparkSession, JavaRDD enrichedEntityJavaRDD) {
        JavaRDD<Row> rows = prepareForWriting(enrichedEntityJavaRDD);
        return sparkSession.createDataFrame(rows, getDataLakeSchema());
    }

    private JavaRDD<Row> prepareForWriting(JavaRDD<EnrichedPosition> enrichedEntityJavaRDD) {
        return enrichedEntityJavaRDD.map((Function<EnrichedPosition, Row>) enrichedPosition ->
                RowFactory.create(enrichedPosition.getOrderedValues()));
    }

    @SuppressWarnings("CPD-START")
    StructType getDataLakeSchema() {
        return new StructType(
                new StructField[] {
                        DataTypes.createStructField("durable_id", DataTypes.StringType, true),
                        DataTypes.createStructField("ingested_date_utc", DataTypes.TimestampType, true),
                        DataTypes.createStructField("subscription_id", DataTypes.LongType, true),
                        DataTypes.createStructField("lineage_code", DataTypes.IntegerType, true),
                        DataTypes.createStructField("driver_durable_key", DataTypes.StringType, true),
                        DataTypes.createStructField("vehicle_durable_key", DataTypes.StringType, true),
                        DataTypes.createStructField("timestamp", DataTypes.TimestampType, true),
                        DataTypes.createStructField("longitude", DataTypes.DoubleType, true),
                        DataTypes.createStructField("latitude", DataTypes.DoubleType, true),
                        DataTypes.createStructField("driver_id", DataTypes.LongType, true),
                        DataTypes.createStructField("asset_id", DataTypes.LongType, true),
                        DataTypes.createStructField("position_id", DataTypes.LongType, true),
                        DataTypes.createStructField("avl", DataTypes.BooleanType, true),
                        DataTypes.createStructField("source", DataTypes.StringType, true),
                        DataTypes.createStructField("odometer_kilometres", DataTypes.DoubleType, true),
                        DataTypes.createStructField("ignition_on", DataTypes.BooleanType, true),
                        DataTypes.createStructField("age_of_reading_seconds", DataTypes.LongType, true),
                        DataTypes.createStructField("pdop", DataTypes.IntegerType, true),
                        DataTypes.createStructField("vdop", DataTypes.IntegerType, true),
                        DataTypes.createStructField("hdop", DataTypes.IntegerType, true),
                        DataTypes.createStructField("number_of_satellites", DataTypes.IntegerType, true),
                        DataTypes.createStructField("heading", DataTypes.IntegerType, true),
                        DataTypes.createStructField("altitude_metres", DataTypes.IntegerType, true),
                        DataTypes.createStructField("speed_kilometres_per_hour", DataTypes.DoubleType, true),
                        DataTypes.createStructField("distance_since_reading_kilometres", DataTypes.IntegerType, true),
                        DataTypes.createStructField("formatted_address", DataTypes.StringType, true),
                        DataTypes.createStructField("speed_limit", DataTypes.DoubleType, true)
                }
        );
    }
}
