package com.epam.tcodata.internal.pump.converter.fact;

import com.epam.tcodata.internal.pump.converter.AbstractEntityConverter;
import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.models.avro.fact.AvroTrip;
import com.epam.tcodata.models.datalake.raw.fact.RawTrip;
import com.epam.tcodata.models.enriched.fact.EnrichedTrip;
import com.epam.tcodata.models.mix.fact.Trip;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.sql.Timestamp;
import java.util.stream.Collectors;

import static com.epam.tcodata.common.ConverterUtils.checkedToString;

@SuppressWarnings("CPD-START")
public class TripConverter extends AbstractEntityConverter<AvroTrip, EnrichedTrip, RawTrip> {

    private static final long serialVersionUID = -1360300737265004400L;
    private PositionConverter positionConverter;
    private SubTripConverter subTripConverter;

    /**
     * Main public constructor.
     */
    public TripConverter(PositionConverter positionConverter, SubTripConverter subTripConverter) {
        this.positionConverter = positionConverter;
        this.subTripConverter = subTripConverter;
    }

    /**
     * Method converts avro entity to enriched entity.
     *
     * @return EnrichedTrip
     */
    @Override
    public EnrichedTrip convertToEnriched(AvroTrip avro) {
        Trip.TripBuilder tripBuilder = new Trip.TripBuilder()
                .setTripId(avro.getTripId())
                .setAssetId(avro.getAssetId())
                .setDriverId(avro.getDriverId())
                .setTripStart(IEntityConverter.dateTimeToTimestamp(avro.getTripStart()))
                .setTripEnd(IEntityConverter.dateTimeToTimestamp(avro.getTripEnd()))
                .setNotes(checkedToString(avro.getNotes()))
                .setPulseParameterName(checkedToString(avro.getPulseParameterName()))
                .setEngineSeconds(avro.getEngineSeconds())
                .setStartPositionId(avro.getStartPositionId())
                .setEndPositionId(avro.getEndPositionId())
                .setFirstDepart(IEntityConverter.dateTimeToTimestamp(avro.getFirstDepart()))
                .setLastHalt(IEntityConverter.dateTimeToTimestamp(avro.getLastHalt()))
                .setDrivingTime(avro.getDrivingTime())
                .setStandingTime(avro.getStandingTime())
                .setDuration(avro.getDuration())
                .setDistanceKilometers(avro.getDistanceKilometers())
                .setStartOdometerKilometers(avro.getStartOdometerKilometers())
                .setEndOdometerKilometers(avro.getEndOdometerKilometers())
                .setStartEngineSeconds(avro.getStartEngineSeconds())
                .setEndEngineSeconds(avro.getEndEngineSeconds())
                .setPulseValue(avro.getPulseValue())
                .setFuelUsedLitres(avro.getFuelUsedLitres())
                .setMaxSpeedKilometersPerHour(avro.getMaxSpeedKilometersPerHour())
                .setMaxAccelerationKilometersPerHourPerSecond(avro.getMaxAccelerationKilometersPerHourPerSecond())
                .setMaxDecelerationKilometersPerHourPerSecond(avro.getMaxDecelerationKilometersPerHourPerSecond())
                .setMaxRpm(avro.getMaxRpm());

        Trip trip = tripBuilder.build();

        return new EnrichedTrip(trip)
                // IEnrichable fields
                .setDurableId(checkedToString(avro.getDurableId()))
                .setIngestedDateUtc(IEntityConverter.dateTimeToTimestamp(avro.getIngestedDateUtc()))
                .setSubscriptionId(avro.getSubscriptionId())
                .setLineageCode(avro.getLineageCode())
                // EnrichedSubTrip fields
                .setDriverDurableKey(checkedToString(avro.getDriverDurableKey()))
                .setVehicleDurableKey(checkedToString(avro.getVehicleDurableKey()))
                .setEnrichedSubTripList(avro.getSubTripList() == null
                        ? null : avro.getSubTripList().stream()
                        .map(subTripConverter::convertToEnriched)
                        .collect(Collectors.toList()))
                .setEnrichedStartPosition(avro.getStartPosition() == null
                        ? null : positionConverter.convertToEnriched(avro.getStartPosition()))
                .setEnrichedEndPosition(avro.getEndPosition() == null
                        ? null : positionConverter.convertToEnriched(avro.getEndPosition()));
    }

    /**
     * Method converts enriched entity to dataLake entity.
     *
     * @return DataLakeTrip
     */
    @Override
    public RawTrip convertToRaw(EnrichedTrip enriched, Timestamp persistedDate) {
        RawTrip dataLakeTrip = new RawTrip();
        // DataLakeEntity fields
        dataLakeTrip.setYear(IEntityConverter.timestampToYear(persistedDate));
        dataLakeTrip.setWeekNumber(IEntityConverter.timestampToWeekNumber(persistedDate));
        dataLakeTrip.setTripId(enriched.getTripId());
        dataLakeTrip.setAssetId(enriched.getAssetId());
        dataLakeTrip.setDriverId(enriched.getDriverId());
        dataLakeTrip.setTripStart(enriched.getTripStart());
        dataLakeTrip.setTripEnd(enriched.getTripEnd());
        dataLakeTrip.setNotes(enriched.getNotes());
        dataLakeTrip.setPulseParameterName(enriched.getPulseParameterName());
        dataLakeTrip.setEngineSeconds(enriched.getEngineSeconds());
        dataLakeTrip.setStartPositionId(enriched.getStartPositionId());
        // startPosition fields
        if (enriched.getEnrichedStartPosition() != null) {
            dataLakeTrip.setStartPositionTimestamp(enriched.getEnrichedStartPosition().getTimestamp());
            dataLakeTrip.setStartPositionLongitude(enriched.getEnrichedStartPosition().getLongitude());
            dataLakeTrip.setStartPositionLatitude(enriched.getEnrichedStartPosition().getLatitude());
            dataLakeTrip.setStartPositionSpeedKilometresPerHour(enriched.getEnrichedStartPosition().getSpeedKilometresPerHour());
        }
        dataLakeTrip.setEndPositionId(enriched.getEndPositionId());
        // endPosition fields
        if (enriched.getEnrichedEndPosition() != null) {
            dataLakeTrip.setEndPositionTimestamp(enriched.getEnrichedEndPosition().getTimestamp());
            dataLakeTrip.setEndPositionLongitude(enriched.getEnrichedEndPosition().getLongitude());
            dataLakeTrip.setEndPositionLatitude(enriched.getEnrichedEndPosition().getLatitude());
            dataLakeTrip.setEndPositionSpeedKilometresPerHour(enriched.getEnrichedEndPosition().getSpeedKilometresPerHour());
        }
        dataLakeTrip.setFirstDepart(enriched.getFirstDepart());
        dataLakeTrip.setLastHalt(enriched.getLastHalt());
        dataLakeTrip.setDrivingTime(enriched.getDrivingTime());
        dataLakeTrip.setStandingTime(enriched.getStandingTime());
        dataLakeTrip.setDuration(enriched.getDuration());
        dataLakeTrip.setDistanceKilometers(enriched.getDistanceKilometers());
        dataLakeTrip.setStartOdometerKilometers(enriched.getStartOdometerKilometers());
        dataLakeTrip.setEndOdometerKilometers(enriched.getEndOdometerKilometers());
        dataLakeTrip.setStartEngineSeconds(enriched.getStartEngineSeconds());
        dataLakeTrip.setEndEngineSeconds(enriched.getEndEngineSeconds());
        dataLakeTrip.setPulseValue(enriched.getPulseValue());
        dataLakeTrip.setFuelUsedLitres(enriched.getFuelUsedLitres());
        dataLakeTrip.setMaxSpeedKilometersPerHour(enriched.getMaxSpeedKilometersPerHour());
        dataLakeTrip.setMaxAccelerationKilometersPerHourPerSecond(enriched.getMaxAccelerationKilometersPerHourPerSecond());
        dataLakeTrip.setMaxDecelerationKilometersPerHourPerSecond(enriched.getMaxDecelerationKilometersPerHourPerSecond());
        dataLakeTrip.setMaxRpm(enriched.getMaxRpm());
        // IEnrichable fields
        dataLakeTrip.setDurableId(enriched.getDurableId());
        dataLakeTrip.setIngestedDateUtc(enriched.getIngestedDateUtc());
        dataLakeTrip.setSubscriptionId(enriched.getSubscriptionId());
        dataLakeTrip.setLineageCode(enriched.getLineageCode());
        dataLakeTrip.setPersistedDateUtc(persistedDate);
        // EnrichedTrip fields
        dataLakeTrip.setDriverDurableKey(enriched.getDriverDurableKey());
        dataLakeTrip.setVehicleDurableKey(enriched.getVehicleDurableKey());
        return dataLakeTrip;
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
                        DataTypes.createStructField("start_position_timestamp", DataTypes.TimestampType, true),
                        DataTypes.createStructField("start_position_longitude", DataTypes.DoubleType, true),
                        DataTypes.createStructField("start_position_latitude", DataTypes.DoubleType, true),
                        DataTypes.createStructField("start_position_speed_kilometres_per_hour", DataTypes.DoubleType, true),
                        DataTypes.createStructField("end_position_id", DataTypes.LongType, true),
                        DataTypes.createStructField("end_position_timestamp", DataTypes.TimestampType, true),
                        DataTypes.createStructField("end_position_longitude", DataTypes.DoubleType, true),
                        DataTypes.createStructField("end_position_latitude", DataTypes.DoubleType, true),
                        DataTypes.createStructField("end_position_speed_kilometres_per_hour", DataTypes.DoubleType, true),
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
                        DataTypes.createStructField("max_rpm", DataTypes.DoubleType, true),
                        DataTypes.createStructField("year", DataTypes.IntegerType, true),
                        DataTypes.createStructField("week_number", DataTypes.IntegerType, true)
                }
        );
    }
}
