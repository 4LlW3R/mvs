package com.epam.tcodata.internal.pump.converter.fact;

import com.epam.tcodata.internal.pump.converter.AbstractEntityConverter;
import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.models.avro.fact.AvroSubTrip;
import com.epam.tcodata.models.datalake.raw.fact.RawSubTrip;
import com.epam.tcodata.models.enriched.fact.EnrichedSubTrip;
import com.epam.tcodata.models.mix.fact.SubTrip;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.sql.Timestamp;

import static com.epam.tcodata.common.ConverterUtils.checkedToString;

@SuppressWarnings("CPD-START")
public class SubTripConverter extends AbstractEntityConverter<AvroSubTrip, EnrichedSubTrip, RawSubTrip> {

    private static final long serialVersionUID = 3833267108704881539L;
    private PositionConverter positionConverter;

    /**
     * Main public constructor.
     */
    public SubTripConverter(PositionConverter positionConverter) {
        this.positionConverter = positionConverter;
    }

    /**
     * Method converts avro entity to enriched entity.
     *
     * @return EnrichedSubTrip
     */
    @Override
    public EnrichedSubTrip convertToEnriched(AvroSubTrip avro) {
        SubTrip.SubTripBuilder subTripBuilder = new SubTrip.SubTripBuilder()
                .setSubTripStart(IEntityConverter.dateTimeToTimestamp(avro.getSubTripStart()))
                .setStartPositionId(avro.getStartPositionId())
                .setDepart(IEntityConverter.dateTimeToTimestamp(avro.getDepart()))
                .setHalt(IEntityConverter.dateTimeToTimestamp(avro.getHalt()))
                .setSubTripEnd(IEntityConverter.dateTimeToTimestamp(avro.getSubTripEnd()))
                .setEndPositionId(avro.getEndPositionId())
                .setDrivingTime(avro.getDrivingTime())
                .setStandingTime(avro.getStandingTime())
                .setDuration(avro.getDuration())
                .setDistanceKilometres(avro.getDistanceKilometres())
                .setStartOdometerKilometres(avro.getStartOdometerKilometres())
                .setEndOdometerKilometres(avro.getEndOdometerKilometres())
                .setStartEngineSeconds(avro.getStartEngineSeconds())
                .setEndEngineSeconds(avro.getEndEngineSeconds())
                .setEngineSeconds(avro.getEngineSeconds())
                .setPulseValue(avro.getPulseValue())
                .setFuelUsedLitres(avro.getFuelUsedLitres())
                .setMaxSpeedKilometersPerHour(avro.getMaxSpeedKilometersPerHour())
                .setMaxAccelerationKilometersPerHourPerSecond(avro.getMaxAccelerationKilometersPerHourPerSecond())
                .setMaxDecelerationKilometersPerHourPerSecond(avro.getMaxDecelerationKilometersPerHourPerSecond())
                .setMaxRpm(avro.getMaxRpm());

        SubTrip subTrip = subTripBuilder.build();

        return new EnrichedSubTrip(subTrip)
                // IEnrichable fields
                .setDurableId(checkedToString(avro.getDurableId())) //should handle by key manager
                .setIngestedDateUtc(IEntityConverter.dateTimeToTimestamp(avro.getIngestedDateUtc()))
                .setSubscriptionId(avro.getSubscriptionId())
                .setLineageCode(avro.getLineageCode())
                // EnrichedSubTrip fields
                .setSubTripId(avro.getSubTripId())
                .setParentTripKey(checkedToString(avro.getParentTripKey()))
                .setEnrichedStartPosition(avro.getStartPosition() == null
                        ? null : positionConverter.convertToEnriched(avro.getStartPosition()))
                .setEnrichedEndPosition(avro.getEndPosition() == null
                        ? null : positionConverter.convertToEnriched(avro.getEndPosition()));
    }

    /**
     * Method converts enriched entity to dataLake entity.
     *
     * @return DataLakeSubTrip
     */
    @Override
    public RawSubTrip convertToRaw(EnrichedSubTrip enriched, Timestamp persistedDate) {
        RawSubTrip dataLakeSubTrip = new RawSubTrip();
        dataLakeSubTrip.setYear(IEntityConverter.timestampToYear(persistedDate));
        dataLakeSubTrip.setWeekNumber(IEntityConverter.timestampToWeekNumber(persistedDate));
        dataLakeSubTrip.setSubTripStart(enriched.getSubTripStart());
        dataLakeSubTrip.setStartPositionId(enriched.getStartPositionId());
        // startPosition fields
        if (enriched.getEnrichedStartPosition() != null) {
            dataLakeSubTrip.setStartPositionTimestamp(enriched.getEnrichedStartPosition().getTimestamp());
            dataLakeSubTrip.setStartPositionLongitude(enriched.getEnrichedStartPosition().getLongitude());
            dataLakeSubTrip.setStartPositionLatitude(enriched.getEnrichedStartPosition().getLatitude());
            dataLakeSubTrip.setStartPositionSpeedKilometresPerHour(enriched.getEnrichedStartPosition().getSpeedKilometresPerHour());
        }
        dataLakeSubTrip.setDepart(enriched.getDepart());
        dataLakeSubTrip.setHalt(enriched.getHalt());
        dataLakeSubTrip.setSubTripEnd(enriched.getSubTripEnd());
        dataLakeSubTrip.setEndPositionId(enriched.getEndPositionId());
        // endPosition fields
        if (enriched.getEnrichedEndPosition() != null) {
            dataLakeSubTrip.setEndPositionTimestamp(enriched.getEnrichedEndPosition().getTimestamp());
            dataLakeSubTrip.setEndPositionLongitude(enriched.getEnrichedEndPosition().getLongitude());
            dataLakeSubTrip.setEndPositionLatitude(enriched.getEnrichedEndPosition().getLatitude());
        }
        dataLakeSubTrip.setDrivingTime(enriched.getDrivingTime());
        dataLakeSubTrip.setStandingTime(enriched.getStandingTime());
        dataLakeSubTrip.setDuration(enriched.getDuration());
        dataLakeSubTrip.setDistanceKilometres(enriched.getDistanceKilometres());
        dataLakeSubTrip.setStartOdometerKilometres(enriched.getStartOdometerKilometres());
        dataLakeSubTrip.setEndOdometerKilometres(enriched.getEndOdometerKilometres());
        dataLakeSubTrip.setStartEngineSeconds(enriched.getStartEngineSeconds());
        dataLakeSubTrip.setEndEngineSeconds(enriched.getEndEngineSeconds());
        dataLakeSubTrip.setEngineSeconds(enriched.getEngineSeconds());
        dataLakeSubTrip.setPulseValue(enriched.getPulseValue());
        dataLakeSubTrip.setFuelUsedLitres(enriched.getFuelUsedLitres());
        dataLakeSubTrip.setMaxSpeedKilometersPerHour(enriched.getMaxSpeedKilometersPerHour());
        dataLakeSubTrip.setMaxAccelerationKilometersPerHourPerSecond(enriched.getMaxAccelerationKilometersPerHourPerSecond());
        dataLakeSubTrip.setMaxDecelerationKilometersPerHourPerSecond(enriched.getMaxDecelerationKilometersPerHourPerSecond());
        dataLakeSubTrip.setMaxRpm(enriched.getMaxRpm());
        // IEnrichable fields
        dataLakeSubTrip.setDurableId(enriched.getDurableId());
        dataLakeSubTrip.setIngestedDateUtc(enriched.getIngestedDateUtc());
        dataLakeSubTrip.setSubscriptionId(enriched.getSubscriptionId());
        dataLakeSubTrip.setLineageCode(enriched.getLineageCode());
        dataLakeSubTrip.setPersistedDateUtc(persistedDate);
        // EnrichedSubTrip fields
        dataLakeSubTrip.setSubTripId(enriched.getSubTripId());
        dataLakeSubTrip.setParentTripKey(enriched.getParentTripKey());
        return dataLakeSubTrip;
    }

    @Override
    public StructType getSchemaForWrite() {
        return new StructType(
                new StructField[] {
                        DataTypes.createStructField("durable_id", DataTypes.StringType, true),
                        DataTypes.createStructField("ingested_date_utc", DataTypes.TimestampType, true),
                        DataTypes.createStructField("subscription_id", DataTypes.LongType, true),
                        DataTypes.createStructField("lineage_code", DataTypes.IntegerType, true),
                        DataTypes.createStructField("persisted_date_utc", DataTypes.TimestampType, true),
                        DataTypes.createStructField("sub_trip_id", DataTypes.LongType, true),
                        DataTypes.createStructField("parent_trip_key", DataTypes.StringType, true),
                        DataTypes.createStructField("sub_trip_start", DataTypes.TimestampType, true),
                        DataTypes.createStructField("start_position_id", DataTypes.LongType, true),
                        DataTypes.createStructField("start_position_timestamp", DataTypes.TimestampType, true),
                        DataTypes.createStructField("start_position_longitude", DataTypes.DoubleType, true),
                        DataTypes.createStructField("start_position_latitude", DataTypes.DoubleType, true),
                        DataTypes.createStructField("start_position_speed_kilometres_per_hour", DataTypes.DoubleType, true),
                        DataTypes.createStructField("depart", DataTypes.TimestampType, true),
                        DataTypes.createStructField("halt", DataTypes.TimestampType, true),
                        DataTypes.createStructField("sub_trip_end", DataTypes.TimestampType, true),
                        DataTypes.createStructField("end_position_id", DataTypes.LongType, true),
                        DataTypes.createStructField("end_position_timestamp", DataTypes.TimestampType, true),
                        DataTypes.createStructField("end_position_longitude", DataTypes.DoubleType, true),
                        DataTypes.createStructField("end_position_latitude", DataTypes.DoubleType, true),
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
                        DataTypes.createStructField("max_rpm", DataTypes.DoubleType, true),
                        DataTypes.createStructField("year", DataTypes.IntegerType, true),
                        DataTypes.createStructField("week_number", DataTypes.IntegerType, true)
                }
        );
    }
}
