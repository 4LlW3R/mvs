package com.epam.tcodata.external.pump.converter.impl;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.dto.fact.FactDto;
import com.epam.tcodata.external.pump.factory.IExternalFactory;
import com.epam.tcodata.external.pump.util.ConverterUtil;
import com.epam.tcodata.external.pump.util.KeyManagerUtil;
import com.epam.tcodata.mdm.IKeyManager;
import com.epam.tcodata.mdm.SearchingResult;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.avro.fact.AvroSubTrip;
import com.epam.tcodata.models.avro.fact.AvroTrip;
import com.epam.tcodata.models.enriched.fact.EnrichedSubTrip;
import com.epam.tcodata.models.enriched.fact.EnrichedTrip;
import com.epam.tcodata.models.mix.fact.Position;
import com.epam.tcodata.models.mix.fact.SubTrip;
import com.epam.tcodata.models.mix.fact.Trip;
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
import java.util.Optional;
import java.util.stream.Collectors;

public class TripConverter implements IConverter<Trip, EnrichedTrip, AvroTrip> {

    private static final long serialVersionUID = -4429433122180265947L;

    private static final Logger LOGGER = LoggerFactory.getLogger(TripConverter.class);

    // general properties
    private static final int AVRO_SCHEMA_VERSION = 1;
    private PositionConverter positionConverter;
    private SubTripConverter subTripConverter;

    /**
     * Converter for Trips.
     * Uses converter for positions and for sub trips.
     *
     * @throws Exception exception may occur while creation Factories.
     */
    public TripConverter() throws Exception {
        this.positionConverter = (PositionConverter) IExternalFactory.createConverter(EntityType.POSITION);
        this.subTripConverter = (SubTripConverter) IExternalFactory.createConverter(EntityType.SUBTRIP);
    }

    @Override
    public EnrichedTrip convertToEnriched(Trip trip, AbstractDto dto, IKeyManager keyManager) {
        // copying main fields
        EnrichedTrip enrichedTrip = new EnrichedTrip(trip);

        Timestamp ingestedDate = Timestamp.from(Instant.now());

        Map<EntityType, List<SearchingResult>> substitutionMap = keyManager.keysSubstitution(
                trip,
                ApiVersion.API_2_0,
                String.valueOf(((FactDto) dto).getOrgGroupId()),
                EntityType.TRIP);

        // enriched with surrogate keys
        try {
            KeyManagerUtil.enrichEntity(enrichedTrip, substitutionMap);
        } catch (IllegalAccessException e) {
            LOGGER.error(e.getMessage(), e);
        }

        String durableKey = keyManager.factDurableKey(EntityType.TACHO,
                "" + trip.getTripId()).toString();

        //enriched with additional info
        enrichedTrip
                .setDurableId(durableKey)
                .setIngestedDateUtc(ingestedDate)
                .setSubscriptionId(((FactDto) dto).getOrgGroupId())
                .setLineageCode(4);

        // enriching inner entities
        Position startPosition = Optional.ofNullable(trip.getStartPosition()).orElse(null);
        Position endPosition = Optional.ofNullable(trip.getEndPosition()).orElse(null);
        List<SubTrip> subTripList = Optional.ofNullable(trip.getSubTripList()).orElse(null);

        if (startPosition != null) {
            enrichedTrip.setEnrichedStartPosition(
                    this.positionConverter.convertToEnriched(startPosition, dto, keyManager));
        }
        if (endPosition != null) {
            enrichedTrip.setEnrichedEndPosition(
                    this.positionConverter.convertToEnriched(endPosition, dto, keyManager));
        }
        if (subTripList != null) {
            enrichedTrip.setEnrichedSubTripList(
                    convertSubTripListToEnriched(subTripList, dto, ingestedDate, durableKey, keyManager));
        }

        return enrichedTrip;
    }

    private List<EnrichedSubTrip> convertSubTripListToEnriched(List<SubTrip> subTripList,
                                                               AbstractDto dto,
                                                               Timestamp ingestedDate,
                                                               String parentTripKey,
                                                               IKeyManager keyManager) {

        List<EnrichedSubTrip> result = new ArrayList<>(subTripList.size());
        for (int i = 0; i < subTripList.size(); ++i) {
            result.add(
                    this.subTripConverter.convertToEnriched(
                            subTripList.get(i),
                            dto,
                            ingestedDate,
                            parentTripKey,
                            (long) i,
                            keyManager));
        }

        return result;
    }

    @Override
    public AvroTrip convertToAvro(EnrichedTrip enrichedTrip) {
        return AvroTrip.newBuilder()

                // enriched fields
                .setDurableId(enrichedTrip.getDurableId())
                .setIngestedDateUtc(ConverterUtil.timestampToDateTime(enrichedTrip.getIngestedDateUtc()))
                .setSubscriptionId(enrichedTrip.getSubscriptionId())
                .setLineageCode(enrichedTrip.getLineageCode())
                .setDriverDurableKey(enrichedTrip.getDriverDurableKey())
                .setVehicleDurableKey(enrichedTrip.getVehicleDurableKey())

                // transport message support
                .setSchemaVersion(AVRO_SCHEMA_VERSION)

                // main fields
                .setTripId(enrichedTrip.getTripId())
                .setAssetId(enrichedTrip.getAssetId())
                .setDriverId(enrichedTrip.getDriverId())
                .setTripStart(ConverterUtil.timestampToDateTime(enrichedTrip.getTripStart()))
                .setTripEnd(ConverterUtil.timestampToDateTime(enrichedTrip.getTripEnd()))
                .setNotes(enrichedTrip.getNotes())
                .setPulseParameterName(enrichedTrip.getPulseParameterName())
                .setSubTripList(convertSubTripListToAvro(enrichedTrip.getEnrichedSubTripList()))
                .setEngineSeconds(enrichedTrip.getEngineSeconds())
                .setStartPositionId(enrichedTrip.getStartPositionId())
                .setStartPosition(this.positionConverter.convertToAvro(enrichedTrip.getEnrichedStartPosition()))
                .setEndPositionId(enrichedTrip.getEndPositionId())
                .setEndPosition(this.positionConverter.convertToAvro(enrichedTrip.getEnrichedEndPosition()))
                .setFirstDepart(ConverterUtil.timestampToDateTime(enrichedTrip.getFirstDepart()))
                .setLastHalt(ConverterUtil.timestampToDateTime(enrichedTrip.getLastHalt()))
                .setDrivingTime(enrichedTrip.getDrivingTime())
                .setStandingTime(enrichedTrip.getStandingTime())
                .setDuration(enrichedTrip.getDuration())
                .setDistanceKilometers(enrichedTrip.getDistanceKilometers())
                .setStartOdometerKilometers(enrichedTrip.getStartOdometerKilometers())
                .setEndOdometerKilometers(enrichedTrip.getEndOdometerKilometers())
                .setStartEngineSeconds(enrichedTrip.getStartEngineSeconds())
                .setEndEngineSeconds(enrichedTrip.getEndEngineSeconds())
                .setPulseValue(enrichedTrip.getPulseValue())
                .setFuelUsedLitres(enrichedTrip.getFuelUsedLitres())
                .setMaxSpeedKilometersPerHour(enrichedTrip.getMaxSpeedKilometersPerHour())
                .setMaxAccelerationKilometersPerHourPerSecond(enrichedTrip.getMaxAccelerationKilometersPerHourPerSecond())
                .setMaxDecelerationKilometersPerHourPerSecond(enrichedTrip.getMaxDecelerationKilometersPerHourPerSecond())
                .setMaxRpm(enrichedTrip.getMaxRpm())
                .build();
    }

    private List<AvroSubTrip> convertSubTripListToAvro(List<EnrichedSubTrip> enrichedSubTripList) {
        if (enrichedSubTripList == null) {
            return new ArrayList<>();
        }
        return enrichedSubTripList.stream()
                .map(enrichedSubTrip -> this.subTripConverter.convertToAvro(enrichedSubTrip))
                .collect(Collectors.toList());
    }

    //TO DO
    @Override
    public Dataset<Row> convertToDataset(SparkSession sparkSession, JavaRDD enrichedEntityJavaRDD) {
        JavaRDD<Row> rows = prepareForWriting(enrichedEntityJavaRDD);
        return sparkSession.createDataFrame(rows, getDataLakeSchema());
    }

    private JavaRDD<Row> prepareForWriting(JavaRDD<EnrichedTrip> enrichedEntityJavaRDD) {
        return enrichedEntityJavaRDD.map((Function<EnrichedTrip, Row>) enrichedTrip ->
                RowFactory.create(enrichedTrip.getOrderedValues()));
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
                        DataTypes.createStructField("trip_id", DataTypes.LongType, true),
                        DataTypes.createStructField("asset_id", DataTypes.LongType, true),
                        DataTypes.createStructField("driver_id", DataTypes.LongType, true),
                        DataTypes.createStructField("trip_start", DataTypes.TimestampType, true),
                        DataTypes.createStructField("trip_end", DataTypes.TimestampType, true),
                        DataTypes.createStructField("notes", DataTypes.StringType, true),
                        DataTypes.createStructField("pulse_parameter_name", DataTypes.StringType, true),
                        DataTypes.createStructField("engine_seconds", DataTypes.IntegerType, true),
                        DataTypes.createStructField("start_position_id", DataTypes.LongType, true),
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
                        //start position fields end
                        DataTypes.createStructField("end_position_id", DataTypes.LongType, true),
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
                        DataTypes.createStructField("first_depart", DataTypes.TimestampType, true),
                        DataTypes.createStructField("last_halt", DataTypes.TimestampType, true),
                        DataTypes.createStructField("driving_time", DataTypes.DoubleType, true),
                        DataTypes.createStructField("standing_time", DataTypes.DoubleType, true),
                        DataTypes.createStructField("duration", DataTypes.DoubleType, true),
                        DataTypes.createStructField("distance_kilometers", DataTypes.DoubleType, true),
                        DataTypes.createStructField("start_odometer_kilometers", DataTypes.DoubleType, true),
                        DataTypes.createStructField("end_odometer_kilometers", DataTypes.DoubleType, true),
                        DataTypes.createStructField("start_engine_seconds", DataTypes.IntegerType, true),
                        DataTypes.createStructField("end_engine_seconds", DataTypes.IntegerType, true),
                        DataTypes.createStructField("pulse_value", DataTypes.DoubleType, true),
                        DataTypes.createStructField("fuel_used_litres", DataTypes.DoubleType, true),
                        DataTypes.createStructField("max_speed_kilometers_per_hour", DataTypes.DoubleType, true),
                        DataTypes.createStructField("max_acceleration_kilometers_per_hour_per_second", DataTypes.DoubleType, true),
                        DataTypes.createStructField("max_deceleration_kilometers_per_hour_per_second", DataTypes.DoubleType, true),
                        DataTypes.createStructField("max_rpm", DataTypes.DoubleType, true)
                }
        );
    }
}
