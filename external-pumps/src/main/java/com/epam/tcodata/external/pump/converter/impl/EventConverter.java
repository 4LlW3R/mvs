package com.epam.tcodata.external.pump.converter.impl;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.dto.fact.FactDto;
import com.epam.tcodata.external.pump.factory.IExternalFactory;
import com.epam.tcodata.external.pump.util.KeyManagerUtil;
import com.epam.tcodata.mdm.IKeyManager;
import com.epam.tcodata.mdm.SearchingResult;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.avro.fact.AvroEvent;
import com.epam.tcodata.models.avro.fact.AvroMediaUrls;
import com.epam.tcodata.models.enriched.fact.EnrichedEvent;
import com.epam.tcodata.models.mix.fact.Event;
import com.epam.tcodata.models.mix.fact.Position;
import com.epam.tcodata.models.nested.MediaUrls;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.epam.tcodata.external.pump.util.ConverterUtil.timestampToDateTime;

public class EventConverter implements IConverter<Event, EnrichedEvent, AvroEvent> {

    private static final long serialVersionUID = -7896307555578017902L;

    private static final Logger LOGGER = LoggerFactory.getLogger(PositionConverter.class);

    // general properties
    private static final int AVRO_SCHEMA_VERSION = 1;
    private PositionConverter positionConverter;

    public EventConverter() throws Exception {
        this.positionConverter = (PositionConverter) IExternalFactory.createConverter(EntityType.POSITION);
    }

    @Override
    public EnrichedEvent convertToEnriched(Event event, AbstractDto dto, IKeyManager keyManager) {
        // copying main fields
        EnrichedEvent enrichedEvent = new EnrichedEvent(event);

        // enriched with surrogate keys
        Map<EntityType, List<SearchingResult>> substitutionMap = keyManager.keysSubstitution(
                event,
                ApiVersion.API_2_0,
                String.valueOf(((FactDto) dto).getOrgGroupId()),
                EntityType.EVENT);
        try {
            KeyManagerUtil.enrichEntity(enrichedEvent, substitutionMap);
        } catch (IllegalAccessException e) {
            LOGGER.error(e.getMessage(), e);
        }

        String durableKey = keyManager.factDurableKey(EntityType.EVENT,
                "" + event.getEventId()).toString();

        // enriched with additional info
        enrichedEvent
                .setDurableId(durableKey)
                .setIngestedDateUtc(Timestamp.from(Instant.now()))
                .setSubscriptionId(((FactDto) dto).getOrgGroupId())
                .setLineageCode(4);

        // enriching inner entities
        Position startPosition = Optional.ofNullable(event.getStartPosition()).orElse(null);
        Position endPosition = Optional.ofNullable(event.getEndPosition()).orElse(null);

        if (startPosition != null) {
            enrichedEvent.setEnrichedStartPosition(
                    this.positionConverter.convertToEnriched(startPosition, dto, keyManager));
        }
        if (endPosition != null) {
            enrichedEvent.setEnrichedEndPosition(
                    this.positionConverter.convertToEnriched(endPosition, dto, keyManager));
        }

        return enrichedEvent;
    }

    @Override
    public AvroEvent convertToAvro(EnrichedEvent enrichedEvent) {
        return AvroEvent.newBuilder()

                //enriched fields
                .setDurableId(enrichedEvent.getDurableId())
                .setIngestedDateUtc(timestampToDateTime(enrichedEvent.getIngestedDateUtc()))
                .setSubscriptionId(enrichedEvent.getSubscriptionId())
                .setLineageCode(enrichedEvent.getLineageCode())
                .setDriverDurableKey(enrichedEvent.getDriverDurableKey())
                .setVehicleDurableKey(enrichedEvent.getVehicleDurableKey())

                // transport message support
                .setSchemaVersion(AVRO_SCHEMA_VERSION)

                // main fields
                .setAssetId(enrichedEvent.getAssetId())
                .setDriverId(enrichedEvent.getDriverId())
                .setEventId(enrichedEvent.getEventId())
                .setEventTypeId(enrichedEvent.getEventTypeId())
                .setEventCategory(enrichedEvent.getEventCategory())
                .setStartDateTime(timestampToDateTime(enrichedEvent.getStartDateTime()))
                .setStartOdometerKilometres(enrichedEvent.getStartOdometerKilometres())
                .setStartPosition(this.positionConverter.convertToAvro(enrichedEvent.getEnrichedStartPosition()))
                .setEndDateTime(timestampToDateTime(enrichedEvent.getEndDateTime()))
                .setEndOdometerKilometres(enrichedEvent.getEndOdometerKilometres())
                .setEndPosition(this.positionConverter.convertToAvro(enrichedEvent.getEnrichedEndPosition()))
                .setValue(enrichedEvent.getValue())
                .setValueType(enrichedEvent.getValueType())
                .setValueUnits(enrichedEvent.getValueUnits())
                .setTotalTimeSeconds(enrichedEvent.getTotalTimeSeconds())
                .setTotalOccurrences(enrichedEvent.getTotalOccurrences())
                .setMediaUrls(convertMediaUrlsToAvro(enrichedEvent.getMediaUrls()))
                .setLocationId(enrichedEvent.getLocationId())
                .setSpeedLimit(enrichedEvent.getSpeedLimit())
                .build();
    }

    private AvroMediaUrls convertMediaUrlsToAvro(MediaUrls mediaUrls) {
        if (mediaUrls == null) {
            return null;
        }
        return AvroMediaUrls.newBuilder()
                .setRoad(mediaUrls.getRoad())
                .setCab(mediaUrls.getCab())
                .setCamera3(mediaUrls.getCamera3())
                .setCamera4(mediaUrls.getCamera4())
                .build();
    }

    @Override
    public Dataset<Row> convertToDataset(SparkSession sparkSession, JavaRDD enrichedEntityJavaRDD) {
        JavaRDD<Row> rows = prepareForWriting(enrichedEntityJavaRDD);
        return sparkSession.createDataFrame(rows, getDataLakeSchema());
    }

    private JavaRDD<Row> prepareForWriting(JavaRDD<EnrichedEvent> enrichedEntityJavaRDD) {
        return enrichedEntityJavaRDD.map((Function<EnrichedEvent, Row>) enrichedEvent ->
                RowFactory.create(enrichedEvent.getOrderedValues()));
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
                        DataTypes.createStructField("total_occurances", DataTypes.LongType, true),
                        DataTypes.createStructField("total_time_seconds", DataTypes.IntegerType, true),
                        DataTypes.createStructField("event_type_id", DataTypes.LongType, true),
                        DataTypes.createStructField("event_id", DataTypes.LongType, true),
                        DataTypes.createStructField("driver_id", DataTypes.LongType, true),
                        DataTypes.createStructField("asset_id", DataTypes.LongType, true),
                        DataTypes.createStructField("value", DataTypes.DoubleType, true),
                        DataTypes.createStructField("end_date_time", DataTypes.TimestampType, true),
                        DataTypes.createStructField("start_date_time", DataTypes.TimestampType, true),
                        DataTypes.createStructField("event_category", DataTypes.StringType, true),
                        DataTypes.createStructField("start_odometer_kilometres", DataTypes.DoubleType, true),
                        //start position fields start
                        DataTypes.createStructField("start_position_timestamp", DataTypes.TimestampType, true),
                        DataTypes.createStructField("start_position_longitude", DataTypes.DoubleType, true),
                        DataTypes.createStructField("start_position_latitude", DataTypes.DoubleType, true),
                        DataTypes.createStructField("start_position_position_id", DataTypes.LongType, true),
                        DataTypes.createStructField("start_position_speed_kilometres_per_hour", DataTypes.DoubleType, true),
                        DataTypes.createStructField("start_position_asset_id", DataTypes.LongType, true),
                        DataTypes.createStructField("start_position_driver_id", DataTypes.LongType, true),
                        DataTypes.createStructField("start_position_speed_limit", DataTypes.DoubleType, true),
                        DataTypes.createStructField("start_position_altitude_metres", DataTypes.IntegerType, true),
                        DataTypes.createStructField("start_position_heading", DataTypes.IntegerType, true),
                        DataTypes.createStructField("start_position_number_of_satellites", DataTypes.IntegerType, true),
                        DataTypes.createStructField("start_position_hdop", DataTypes.IntegerType, true),
                        DataTypes.createStructField("start_position_vdop", DataTypes.IntegerType, true),
                        DataTypes.createStructField("start_position_pdop", DataTypes.IntegerType, true),
                        DataTypes.createStructField("start_position_age_of_reading_seconds", DataTypes.IntegerType, true),
                        DataTypes.createStructField("start_position_distance_since_reading_kilometres", DataTypes.IntegerType, true),
                        DataTypes.createStructField("start_position_ignition_on", DataTypes.BooleanType, true),
                        DataTypes.createStructField("start_position_odometer_kilometres", DataTypes.DoubleType, true),
                        DataTypes.createStructField("start_position_formatted_address", DataTypes.StringType, true),
                        DataTypes.createStructField("start_position_source", DataTypes.StringType, true),
                        DataTypes.createStructField("start_position_avl", DataTypes.BooleanType, true),
                        // start position fields end
                        DataTypes.createStructField("end_odometer_kilometres", DataTypes.DoubleType, true),
                        //end position fields start
                        DataTypes.createStructField("end_position_timestamp", DataTypes.TimestampType, true),
                        DataTypes.createStructField("end_position_longitude", DataTypes.DoubleType, true),
                        DataTypes.createStructField("end_position_latitude", DataTypes.DoubleType, true),
                        DataTypes.createStructField("end_position_position_id", DataTypes.LongType, true),
                        DataTypes.createStructField("end_position_speed_kilometres_per_hour", DataTypes.DoubleType, true),
                        DataTypes.createStructField("end_position_asset_id", DataTypes.LongType, true),
                        DataTypes.createStructField("end_position_driver_id", DataTypes.LongType, true),
                        DataTypes.createStructField("end_position_speed_limit", DataTypes.DoubleType, true),
                        DataTypes.createStructField("end_position_altitude_metres", DataTypes.IntegerType, true),
                        DataTypes.createStructField("end_position_heading", DataTypes.IntegerType, true),
                        DataTypes.createStructField("end_position_number_of_satellites", DataTypes.IntegerType, true),
                        DataTypes.createStructField("end_position_hdop", DataTypes.IntegerType, true),
                        DataTypes.createStructField("end_position_vdop", DataTypes.IntegerType, true),
                        DataTypes.createStructField("end_position_pdop", DataTypes.IntegerType, true),
                        DataTypes.createStructField("end_position_age_of_reading_seconds", DataTypes.IntegerType, true),
                        DataTypes.createStructField("end_position_distance_since_reading_kilometres", DataTypes.IntegerType, true),
                        DataTypes.createStructField("end_position_ignition_on", DataTypes.BooleanType, true),
                        DataTypes.createStructField("end_position_odometer_kilometres", DataTypes.DoubleType, true),
                        DataTypes.createStructField("end_position_formatted_address", DataTypes.StringType, true),
                        DataTypes.createStructField("end_position_source", DataTypes.StringType, true),
                        DataTypes.createStructField("end_position_avl", DataTypes.BooleanType, true),
                        //end position fields end
                        DataTypes.createStructField("value_type", DataTypes.StringType, true),
                        DataTypes.createStructField("value_units", DataTypes.StringType, true),
                        DataTypes.createStructField("media_urls_road", DataTypes.StringType, true),
                        DataTypes.createStructField("media_urls_cab", DataTypes.StringType, true),
                        DataTypes.createStructField("media_urls_camera3", DataTypes.StringType, true),
                        DataTypes.createStructField("media_urls_camera4", DataTypes.StringType, true),
                        DataTypes.createStructField("location_id", DataTypes.LongType, true),
                        DataTypes.createStructField("speed_limit", DataTypes.DoubleType, true)
                }
        );
    }
}
