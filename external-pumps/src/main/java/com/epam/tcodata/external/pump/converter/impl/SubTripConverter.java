package com.epam.tcodata.external.pump.converter.impl;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.dto.fact.FactDto;
import com.epam.tcodata.external.pump.factory.IExternalFactory;
import com.epam.tcodata.external.pump.util.ConverterUtil;
import com.epam.tcodata.mdm.IKeyManager;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.avro.fact.AvroSubTrip;
import com.epam.tcodata.models.enriched.fact.EnrichedSubTrip;
import com.epam.tcodata.models.mix.fact.Position;
import com.epam.tcodata.models.mix.fact.SubTrip;
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
import java.util.Optional;

public class SubTripConverter implements IConverter<SubTrip, EnrichedSubTrip, AvroSubTrip> {

    private static final long serialVersionUID = -8847954933451131183L;

    // general properties
    private static final int AVRO_SCHEMA_VERSION = 1;
    private PositionConverter positionConverter;

    public SubTripConverter() throws Exception {
        this.positionConverter = (PositionConverter) IExternalFactory.createConverter(EntityType.POSITION);
    }

    @Override
    public EnrichedSubTrip convertToEnriched(SubTrip entity, AbstractDto<SubTrip> dto, IKeyManager keyManager) {
        throw new UnsupportedOperationException("Use method with parentTripKey and subTripId as parameters");
    }

    /**
     * Special method for SubTrips, because it is used only from Trip service
     * and enrich SubTrips additionaly with parentTripKey and SubTripId.
     *
     * @param subTrip         supTrip.
     * @param dto             dto.
     * @param ingestedDateUtc date that SubTrip was ingested (?).
     * @param parentTripKey   to do (?).
     * @param subTripId       sub trip id (?).
     * @return EnrichedSubTrip.
     */
    public EnrichedSubTrip convertToEnriched(SubTrip subTrip,
                                             AbstractDto dto,
                                             Timestamp ingestedDateUtc,
                                             String parentTripKey,
                                             Long subTripId,
                                             IKeyManager keyManager) {

        // copying main fields
        EnrichedSubTrip enrichedSubTrip = new EnrichedSubTrip(subTrip);

        String durableKey = keyManager.factDurableKey(EntityType.SUBTRIP,
                "" + subTrip.getStartPositionId()).toString();

        //enriched with additional info
        enrichedSubTrip
                .setDurableId(durableKey)
                .setIngestedDateUtc(ingestedDateUtc)
                .setSubscriptionId(((FactDto) dto).getOrgGroupId())
                .setLineageCode(4)
                .setParentTripKey(parentTripKey)
                .setSubTripId(subTripId);

        // enriching inner entities
        Position startPosition = Optional.ofNullable(subTrip.getStartPosition()).orElse(null);
        Position endPosition = Optional.ofNullable(subTrip.getEndPosition()).orElse(null);

        if (startPosition != null) {
            enrichedSubTrip.setEnrichedStartPosition(
                    this.positionConverter.convertToEnriched(startPosition, dto, keyManager));
        }
        if (endPosition != null) {
            enrichedSubTrip.setEnrichedEndPosition(
                    this.positionConverter.convertToEnriched(endPosition, dto, keyManager));
        }

        return enrichedSubTrip;
    }

    @Override
    public AvroSubTrip convertToAvro(EnrichedSubTrip enrichedSubTrip) {
        return AvroSubTrip.newBuilder()

                // enriched fields
                .setIngestedDateUtc(ConverterUtil.timestampToDateTime(enrichedSubTrip.getIngestedDateUtc()))
                .setSubscriptionId(enrichedSubTrip.getSubscriptionId())
                .setDurableId(enrichedSubTrip.getDurableId())
                .setLineageCode(enrichedSubTrip.getLineageCode())
                .setParentTripKey(enrichedSubTrip.getParentTripKey())
                .setSubTripId(enrichedSubTrip.getSubTripId())

                // transport message support
                .setSchemaVersion(AVRO_SCHEMA_VERSION)

                // main fields
                .setSubTripStart(ConverterUtil.timestampToDateTime(enrichedSubTrip.getSubTripStart()))
                .setStartPositionId(enrichedSubTrip.getStartPositionId())
                .setStartPosition(this.positionConverter.convertToAvro(enrichedSubTrip.getEnrichedStartPosition()))
                .setDepart(ConverterUtil.timestampToDateTime(enrichedSubTrip.getDepart()))
                .setHalt(ConverterUtil.timestampToDateTime(enrichedSubTrip.getHalt()))
                .setSubTripEnd(ConverterUtil.timestampToDateTime(enrichedSubTrip.getSubTripEnd()))
                .setEndPositionId(enrichedSubTrip.getEndPositionId())
                .setEndPosition(this.positionConverter.convertToAvro(enrichedSubTrip.getEnrichedEndPosition()))
                .setDrivingTime(enrichedSubTrip.getDrivingTime())
                .setStandingTime(enrichedSubTrip.getStandingTime())
                .setDuration(enrichedSubTrip.getDuration())
                .setDistanceKilometres(enrichedSubTrip.getDistanceKilometres())
                .setStartOdometerKilometres(enrichedSubTrip.getStartOdometerKilometres())
                .setEndOdometerKilometres(enrichedSubTrip.getEndOdometerKilometres())
                .setStartEngineSeconds(enrichedSubTrip.getStartEngineSeconds())
                .setEndEngineSeconds(enrichedSubTrip.getEndEngineSeconds())
                .setEngineSeconds(enrichedSubTrip.getEngineSeconds())
                .setPulseValue(enrichedSubTrip.getPulseValue())
                .setFuelUsedLitres(enrichedSubTrip.getFuelUsedLitres())
                .setMaxSpeedKilometersPerHour(enrichedSubTrip.getMaxSpeedKilometersPerHour())
                .setMaxAccelerationKilometersPerHourPerSecond(enrichedSubTrip.getMaxAccelerationKilometersPerHourPerSecond())
                .setMaxDecelerationKilometersPerHourPerSecond(enrichedSubTrip.getMaxDecelerationKilometersPerHourPerSecond())
                .setMaxRpm(enrichedSubTrip.getMaxRpm())
                .build();
    }

    //TO DO
    @Override
    public Dataset<Row> convertToDataset(SparkSession sparkSession, JavaRDD enrichedEntityJavaRDD) {
        JavaRDD<Row> rows = prepareForWriting(enrichedEntityJavaRDD);
        return sparkSession.createDataFrame(rows, getDataLakeSchema());
    }

    private JavaRDD<Row> prepareForWriting(JavaRDD<EnrichedSubTrip> enrichedEntityJavaRDD) {
        return enrichedEntityJavaRDD.map((Function<EnrichedSubTrip, Row>) enrichedSubTrip ->
                RowFactory.create(enrichedSubTrip.getOrderedValues()));
    }

    @SuppressWarnings("CPD-START")
    StructType getDataLakeSchema() {
        return new StructType(
                new StructField[] {
                        DataTypes.createStructField("durable_id", DataTypes.StringType, true),
                        DataTypes.createStructField("ingested_date_utc", DataTypes.TimestampType, true),
                        DataTypes.createStructField("subscription_id", DataTypes.LongType, true),
                        DataTypes.createStructField("lineage_code", DataTypes.IntegerType, true),
                        DataTypes.createStructField("sub_trip_id", DataTypes.LongType, true),
                        DataTypes.createStructField("parent_trip_key", DataTypes.StringType, true),
                        DataTypes.createStructField("sub_trip_start", DataTypes.TimestampType, true),
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
                        DataTypes.createStructField("depart", DataTypes.TimestampType, true),
                        DataTypes.createStructField("halt", DataTypes.TimestampType, true),
                        DataTypes.createStructField("sub_trip_end", DataTypes.TimestampType, true),
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
                        DataTypes.createStructField("driving_time", DataTypes.IntegerType, true),
                        DataTypes.createStructField("standing_time", DataTypes.IntegerType, true),
                        DataTypes.createStructField("duration", DataTypes.IntegerType, true),
                        DataTypes.createStructField("distance_kilometres", DataTypes.DoubleType, true),
                        DataTypes.createStructField("start_odometer_kilometres", DataTypes.DoubleType, true),
                        DataTypes.createStructField("end_odometer_kilometres", DataTypes.DoubleType, true),
                        DataTypes.createStructField("start_engine_seconds", DataTypes.IntegerType, true),
                        DataTypes.createStructField("end_engine_seconds", DataTypes.IntegerType, true),
                        DataTypes.createStructField("engine_seconds", DataTypes.IntegerType, true),
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
