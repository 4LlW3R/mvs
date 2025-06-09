package com.epam.tcodata.internal.pump.converter.fact;

import com.epam.tcodata.internal.pump.converter.AbstractEntityConverter;
import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.internal.pump.converter.ISpeedLayerConverter;
import com.epam.tcodata.models.avro.fact.AvroPosition;
import com.epam.tcodata.models.datalake.raw.fact.RawPosition;
import com.epam.tcodata.models.enriched.fact.EnrichedPosition;
import com.epam.tcodata.models.mix.fact.Position;
import com.epam.tcodata.sql.dal.domain.speedlayer.SpeedLayerPosition;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.Instant;

import static com.epam.tcodata.common.ConverterUtils.checkedToString;

@SuppressWarnings("CPD-START")
public class PositionConverter extends AbstractEntityConverter<AvroPosition, EnrichedPosition, RawPosition>
        implements ISpeedLayerConverter<EnrichedPosition, SpeedLayerPosition> {

    private static final long serialVersionUID = -2242213241960126386L;

    private static final Logger LOGGER = LoggerFactory.getLogger(PositionConverter.class);

    /**
     * Main public constructor.
     */
    public PositionConverter() {
        /***  Default implementation ***/
    }


    /**
     * Method converts avro entity to enriched entity.
     *
     * @return EnrichedPosition
     */
    @Override
    public EnrichedPosition convertToEnriched(AvroPosition avro) {
        Position.PositionBuilder positionBuilder = new Position.PositionBuilder()
                .setPositionId(avro.getPositionId())
                .setAssetId(avro.getAssetId())
                .setDriverId(avro.getDriverId())
                .setTimestamp(IEntityConverter.dateTimeToTimestamp(avro.getTimestamp()))
                .setLatitude(avro.getLatitude())
                .setLongitude(avro.getLongitude())
                .setSpeedKilometresPerHour(avro.getSpeedKilometresPerHour())
                .setSpeedLimit(avro.getSpeedLimit())
                .setAltitudeMetres(avro.getAltitudeMetres())
                .setHeading(avro.getHeading())
                .setNumberOfSatellites(avro.getNumberOfSatellites())
                .setHdop(avro.getHdop())
                .setVdop(avro.getVdop())
                .setPdop(avro.getPdop())
                .setAgeOfReadingSeconds(avro.getAgeOfReadingSeconds())
                .setDistanceSinceReadingKilometres(avro.getDistanceSinceReadingKilometres())
                .setIgnitionOn(avro.getIgnitionOn())
                .setOdometerKilometres(avro.getOdometerKilometres())
                .setFormattedAddress(checkedToString(avro.getFormattedAddress()))
                .setSource(checkedToString(avro.getSource()))
                .setAvl(avro.getAvl());

        Position position = positionBuilder.build();

        EnrichedPosition enrichedPosition = new EnrichedPosition(position)
                // IEnrichable fields
                .setDurableId(checkedToString(avro.getDurableId()))
                .setIngestedDateUtc(IEntityConverter.dateTimeToTimestamp(avro.getIngestedDateUtc()))
                .setSubscriptionId(avro.getSubscriptionId())
                .setLineageCode(avro.getLineageCode())
                // EnrichedPosition fields
                .setDriverDurableKey(checkedToString(avro.getDriverDurableKey()))
                .setVehicleDurableKey(checkedToString(avro.getVehicleDurableKey()));

        LOGGER.info(
                "#position-converter-internal-pump# Position with id: {} and durable id: {} was converted to the enriched position",
                enrichedPosition.getPositionId(),
                enrichedPosition.getDurableId());

        return enrichedPosition;
    }
    /**
     * Method converts enriched entity to dataLake entity.
     *
     * @return DataLakePosition
     */
    @Override
    public RawPosition convertToRaw(EnrichedPosition enriched, Timestamp persistedDate) {
        RawPosition dataLakePosition = new RawPosition();
        // DataLakeEntity fields
        dataLakePosition.setYear(IEntityConverter.timestampToYear(persistedDate));
        dataLakePosition.setWeekNumber(IEntityConverter.timestampToWeekNumber(persistedDate));
        dataLakePosition.setDurableId(enriched.getDurableId());
        dataLakePosition.setIngestedDateUtc(enriched.getIngestedDateUtc());
        dataLakePosition.setSubscriptionId(enriched.getSubscriptionId());
        dataLakePosition.setLineageCode(enriched.getLineageCode());
        dataLakePosition.setPersistedDateUtc(persistedDate);
        // DataLakePosition fields
        dataLakePosition.setDriverDurableKey(enriched.getDriverDurableKey());
        dataLakePosition.setVehicleDurableKey(enriched.getVehicleDurableKey());
        dataLakePosition.setPositionId(enriched.getPositionId());
        dataLakePosition.setAssetId(enriched.getAssetId());
        dataLakePosition.setDriverId(enriched.getDriverId());
        dataLakePosition.setTimestamp(enriched.getTimestamp());
        dataLakePosition.setLatitude(enriched.getLatitude());
        dataLakePosition.setLongitude(enriched.getLongitude());
        dataLakePosition.setSpeedKilometresPerHour(enriched.getSpeedKilometresPerHour());
        dataLakePosition.setSpeedLimit(enriched.getSpeedLimit());
        dataLakePosition.setAltitudeMetres(enriched.getAltitudeMetres());
        dataLakePosition.setHeading(enriched.getHeading());
        dataLakePosition.setNumberOfSatellites(enriched.getNumberOfSatellites());
        dataLakePosition.setHdop(enriched.getHdop());
        dataLakePosition.setVdop(enriched.getVdop());
        dataLakePosition.setPdop(enriched.getPdop());
        dataLakePosition.setAgeOfReadingSeconds(enriched.getAgeOfReadingSeconds());
        dataLakePosition.setDistanceSinceReadingKilometres(enriched.getDistanceSinceReadingKilometres());
        dataLakePosition.setIgnitionOn(enriched.getIgnitionOn());
        dataLakePosition.setOdometerKilometres(enriched.getOdometerKilometres());
        dataLakePosition.setFormattedAddress(enriched.getFormattedAddress());
        dataLakePosition.setSource(enriched.getSource());
        dataLakePosition.setAvl(enriched.getAvl());
        return dataLakePosition;
    }

    /**
     * Method converts enriched entity to speedLayer entity.
     *
     * @return SpeedLayerPosition
     */
    @Override
    public SpeedLayerPosition convertToSpeedLayer(EnrichedPosition enriched) {
        if (enriched.getTimestamp() == null) {
            return null;
        } else {
            return new SpeedLayerPosition()
                    // SpeedLayerCommon fields
                    .setObservedDay(IEntityConverter.timestampToDayOfYear(enriched.getTimestamp()))
                    .setDurableId(enriched.getDurableId())
                    .setIngestedDateUtc(enriched.getIngestedDateUtc())
                    .setSubscriptionId(enriched.getSubscriptionId())
                    .setLineageCode(enriched.getLineageCode())
                    .setPersistedDateUtc(Timestamp.from(Instant.now()))
                    .setDriverDurableKey(enriched.getDriverDurableKey())
                    .setVehicleDurableKey(enriched.getVehicleDurableKey())
                    // Position fields
                    .setPositionId(enriched.getPositionId())
                    .setAssetId(enriched.getAssetId())
                    .setDriverId(enriched.getDriverId())
                    .setTimestamp(enriched.getTimestamp())
                    .setLatitude(enriched.getLatitude())
                    .setLongitude(enriched.getLongitude())
                    .setSpeedKilometresPerHour(enriched.getSpeedKilometresPerHour())
                    .setSpeedLimit(enriched.getSpeedLimit())
                    .setAltitudeMetres(enriched.getAltitudeMetres())
                    .setHeading(enriched.getHeading())
                    .setNumberOfSatellites(enriched.getNumberOfSatellites())
                    .setHdop(enriched.getHdop())
                    .setVdop(enriched.getVdop())
                    .setPdop(enriched.getPdop())
                    .setAgeOfReadingSeconds(enriched.getAgeOfReadingSeconds())
                    .setDistanceSinceReadingKilometres(enriched.getDistanceSinceReadingKilometres())
                    .setIgnitionOn(enriched.getIgnitionOn())
                    .setOdometerKilometres(enriched.getOdometerKilometres())
                    .setFormattedAddress(enriched.getFormattedAddress())
                    .setSource(enriched.getSource())
                    .setAvl(enriched.getAvl());
        }
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
                        DataTypes.createStructField("speed_limit", DataTypes.DoubleType, true),
                        DataTypes.createStructField("year", DataTypes.IntegerType, true),
                        DataTypes.createStructField("week_number", DataTypes.IntegerType, true)
                }
        );
    }
}
