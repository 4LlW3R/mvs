package com.epam.tcodata.internal.pump.converter.fact;

import com.epam.tcodata.internal.pump.converter.AbstractEntityConverter;
import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.internal.pump.converter.ISpeedLayerConverter;
import com.epam.tcodata.models.avro.fact.AvroEvent;
import com.epam.tcodata.models.avro.fact.AvroMediaUrls;
import com.epam.tcodata.models.datalake.raw.fact.RawEvent;
import com.epam.tcodata.models.enriched.fact.EnrichedEvent;
import com.epam.tcodata.models.mix.fact.Event;
import com.epam.tcodata.models.nested.MediaUrls;
import com.epam.tcodata.sql.dal.domain.speedlayer.SpeedLayerEvent;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.sql.Timestamp;
import java.time.Instant;

import static com.epam.tcodata.common.ConverterUtils.checkedToString;

@SuppressWarnings("CPD-START")
public class EventConverter extends AbstractEntityConverter<AvroEvent, EnrichedEvent, RawEvent> implements
        ISpeedLayerConverter<EnrichedEvent, SpeedLayerEvent> {

    private static final long serialVersionUID = -442868066093373923L;
    private PositionConverter positionConverter;

    /**
     * Main public constructor.
     */
    public EventConverter(PositionConverter positionConverter) {
        this.positionConverter = positionConverter;
    }

    /**
     * Method converts avro entity to enriched entity.
     *
     * @return EnrichedEvent
     */
    @Override
    public EnrichedEvent convertToEnriched(AvroEvent avro) {
        Event.EventBuilder eventBuilder = new Event.EventBuilder()
                .setAssetId(avro.getAssetId())
                .setDriverId(avro.getDriverId())
                .setEventId(avro.getEventId())
                .setEventTypeId(avro.getEventTypeId())
                .setEventCategory(checkedToString(avro.getEventCategory()))
                .setStartDateTime(IEntityConverter.dateTimeToTimestamp(avro.getStartDateTime()))
                .setStartOdometerKilometres(avro.getStartOdometerKilometres())
                .setEndDateTime(IEntityConverter.dateTimeToTimestamp(avro.getEndDateTime()))
                .setEndOdometerKilometres(avro.getEndOdometerKilometres())
                .setValue(avro.getValue())
                .setValueType(checkedToString(avro.getValueType()))
                .setValueUnits(checkedToString(avro.getValueUnits()))
                .setTotalTimeSeconds(avro.getTotalTimeSeconds())
                .setTotalOccurrences(avro.getTotalOccurrences())
                .setLocationId(avro.getLocationId())
                .setSpeedLimit(avro.getSpeedLimit())
                .setMediaUrls(avro.getMediaUrls() == null ? null : convertToMediaUrls(avro.getMediaUrls()));

        Event event = eventBuilder.build();

        return new EnrichedEvent(event)
                // IEnrichable fields
                .setDurableId(checkedToString(avro.getDurableId()))
                .setIngestedDateUtc(IEntityConverter.dateTimeToTimestamp(avro.getIngestedDateUtc()))
                .setSubscriptionId(avro.getSubscriptionId())
                .setLineageCode(avro.getLineageCode())
                // EnrichedEvent fields
                .setDriverDurableKey(checkedToString(avro.getDriverDurableKey()))
                .setVehicleDurableKey(checkedToString(avro.getVehicleDurableKey()))
                // Event fields
                .setEnrichedStartPosition(avro.getStartPosition() == null
                        ? null : positionConverter.convertToEnriched(avro.getStartPosition()))
                .setEnrichedEndPosition(avro.getEndPosition() == null
                        ? null : positionConverter.convertToEnriched(avro.getEndPosition()));
    }

    /**
     * Method converts enriched entity to dataLake entity.
     *
     * @return DataLakeEvent
     */
    @Override
    public RawEvent convertToRaw(EnrichedEvent enriched, Timestamp persistedDate) {
        RawEvent dataLakeEvent = new RawEvent();
        // DataLakeEntity fields
        dataLakeEvent.setYear(IEntityConverter.timestampToYear(persistedDate));
        dataLakeEvent.setWeekNumber(IEntityConverter.timestampToWeekNumber(persistedDate));
        dataLakeEvent.setDurableId(enriched.getDurableId());
        dataLakeEvent.setIngestedDateUtc(enriched.getIngestedDateUtc());
        dataLakeEvent.setSubscriptionId(enriched.getSubscriptionId());
        dataLakeEvent.setLineageCode(enriched.getLineageCode());
        dataLakeEvent.setPersistedDateUtc(persistedDate);
        // DataLakeEvent fields
        dataLakeEvent.setDriverDurableKey(enriched.getDriverDurableKey());
        dataLakeEvent.setVehicleDurableKey(enriched.getVehicleDurableKey());
        dataLakeEvent.setAssetId(enriched.getAssetId());
        dataLakeEvent.setDriverId(enriched.getDriverId());
        dataLakeEvent.setEventId(enriched.getEventId());
        dataLakeEvent.setEventTypeId(enriched.getEventTypeId());
        dataLakeEvent.setEventCategory(enriched.getEventCategory());
        dataLakeEvent.setStartDateTime(enriched.getStartDateTime());
        dataLakeEvent.setStartOdometerKilometres(enriched.getStartOdometerKilometres());
        // startPosition fields
        if (enriched.getEnrichedStartPosition() != null) {
            dataLakeEvent.setStartPositionTimestamp(enriched.getEnrichedStartPosition().getTimestamp());
            dataLakeEvent.setStartPositionLongitude(enriched.getEnrichedStartPosition().getLongitude());
            dataLakeEvent.setStartPositionLatitude(enriched.getEnrichedStartPosition().getLatitude());
            dataLakeEvent.setStartPositionPositionId(enriched.getEnrichedStartPosition().getPositionId());
            dataLakeEvent.setStartPositionSpeedKilometresPerHour(enriched.getEnrichedStartPosition().getSpeedKilometresPerHour());
        }
        dataLakeEvent.setEndDateTime(enriched.getEndDateTime());
        dataLakeEvent.setEndOdometerKilometres(enriched.getEndOdometerKilometres());
        // endPosition fields
        if (enriched.getEnrichedEndPosition() != null) {
            dataLakeEvent.setEndPositionTimestamp(enriched.getEnrichedEndPosition().getTimestamp());
            dataLakeEvent.setEndPositionLongitude(enriched.getEnrichedEndPosition().getLongitude());
            dataLakeEvent.setEndPositionLatitude(enriched.getEnrichedEndPosition().getLatitude());
            dataLakeEvent.setEndPositionPositionId(enriched.getEnrichedEndPosition().getPositionId());
            dataLakeEvent.setEndPositionSpeedKilometresPerHour(enriched.getEnrichedEndPosition().getSpeedKilometresPerHour());
        }
        dataLakeEvent.setValue(enriched.getValue());
        dataLakeEvent.setValueType(enriched.getValueType());
        dataLakeEvent.setValueUnits(enriched.getValueUnits());
        dataLakeEvent.setTotalTimeSeconds(enriched.getTotalTimeSeconds());
        dataLakeEvent.setTotalOccurances(enriched.getTotalOccurrences());
        // mediaUrls fields
        if (enriched.getMediaUrls() != null) {
            dataLakeEvent.setMediaUrlsRoad(enriched.getMediaUrls().getRoad());
            dataLakeEvent.setMediaUrlsCab(enriched.getMediaUrls().getCab());
            dataLakeEvent.setMediaUrlsCamera3(enriched.getMediaUrls().getCamera3());
            dataLakeEvent.setMediaUrlsCamera4(enriched.getMediaUrls().getCamera4());
        }
        dataLakeEvent.setLocationId(enriched.getLocationId());
        dataLakeEvent.setSpeedLimit(enriched.getSpeedLimit());
        return dataLakeEvent;
    }

    /**
     * Method converts enriched entity to speedLayer entity.
     *
     * @return SpeedLayerEvent
     */
    @Override
    public SpeedLayerEvent convertToSpeedLayer(EnrichedEvent enriched) {
        if (enriched.getStartDateTime() == null) {
            return null;
        } else {
            SpeedLayerEvent speedLayerEvent = new SpeedLayerEvent()
                    // SpeedLayerCommon fields
                    .setObservedDay(IEntityConverter.timestampToDayOfYear(enriched.getStartDateTime()))
                    .setDurableId(enriched.getDurableId())
                    .setIngestedDateUtc(enriched.getIngestedDateUtc())
                    .setSubscriptionId(enriched.getSubscriptionId())
                    .setLineageCode(enriched.getLineageCode())
                    .setPersistedDateUtc(Timestamp.from(Instant.now()))
                    .setDriverDurableKey(enriched.getDriverDurableKey())
                    .setVehicleDurableKey(enriched.getVehicleDurableKey());
            // Event fields
            speedLayerEvent.setAssetId(enriched.getAssetId());
            speedLayerEvent.setDriverId(enriched.getDriverId());
            speedLayerEvent.setEventId(enriched.getEventId());
            speedLayerEvent.setEventTypeId(enriched.getEventTypeId());
            speedLayerEvent.setEventCategory(enriched.getEventCategory());
            speedLayerEvent.setStartDateTime(enriched.getStartDateTime());
            speedLayerEvent.setStartOdometerKilometres(enriched.getStartOdometerKilometres());
            // startPosition fields
            if (enriched.getEnrichedStartPosition() != null) {
                speedLayerEvent.setStartPositionTimestamp(enriched.getEnrichedStartPosition().getTimestamp());
                speedLayerEvent.setStartPositionLongitude(enriched.getEnrichedStartPosition().getLongitude());
                speedLayerEvent.setStartPositionLatitude(enriched.getEnrichedStartPosition().getLatitude());
                speedLayerEvent.setStartPositionPositionId(enriched.getEnrichedStartPosition().getPositionId());
                speedLayerEvent.setStartPositionSpeedKilometresPerHour(enriched.getEnrichedStartPosition().getSpeedKilometresPerHour());
            }
            speedLayerEvent.setEndDateTime(enriched.getEndDateTime());
            speedLayerEvent.setEndOdometerKilometres(enriched.getEndOdometerKilometres());
            // endPosition fields
            if (enriched.getEnrichedEndPosition() != null) {
                speedLayerEvent.setEndPositionTimestamp(enriched.getEnrichedEndPosition().getTimestamp());
                speedLayerEvent.setEndPositionLongitude(enriched.getEnrichedEndPosition().getLongitude());
                speedLayerEvent.setEndPositionLatitude(enriched.getEnrichedEndPosition().getLatitude());
                speedLayerEvent.setEndPositionPositionId(enriched.getEnrichedEndPosition().getPositionId());
                speedLayerEvent.setEndPositionSpeedKilometresPerHour(enriched.getEnrichedEndPosition().getSpeedKilometresPerHour());
            }
            speedLayerEvent.setValue(enriched.getValue());
            speedLayerEvent.setValueType(enriched.getValueType());
            speedLayerEvent.setValueUnits(enriched.getValueUnits());
            speedLayerEvent.setTotalTimeSeconds(enriched.getTotalTimeSeconds());
            speedLayerEvent.setTotalOccurrences(enriched.getTotalOccurrences());
            // mediaUrls fields
            if (enriched.getMediaUrls() != null) {
                speedLayerEvent.setMediaUrlsRoad(enriched.getMediaUrls().getRoad());
                speedLayerEvent.setMediaUrlsCab(enriched.getMediaUrls().getCab());
                speedLayerEvent.setMediaUrlsCamera3(enriched.getMediaUrls().getCamera3());
                speedLayerEvent.setMediaUrlsCamera4(enriched.getMediaUrls().getCamera4());
            }
            speedLayerEvent.setLocationId(enriched.getLocationId());
            speedLayerEvent.setSpeedLimit(enriched.getSpeedLimit());
            return speedLayerEvent;
        }
    }


    private static MediaUrls convertToMediaUrls(AvroMediaUrls avro) {
        MediaUrls mediaUrls = new MediaUrls();
        mediaUrls.setRoad(checkedToString(avro.getRoad()));
        mediaUrls.setCab(checkedToString(avro.getCab()));
        mediaUrls.setCamera3(checkedToString(avro.getCamera3()));
        mediaUrls.setCamera4(checkedToString(avro.getCamera4()));
        return mediaUrls;
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
                        DataTypes.createStructField("start_position_timestamp", DataTypes.TimestampType, true),
                        DataTypes.createStructField("start_position_longitude", DataTypes.DoubleType, true),
                        DataTypes.createStructField("start_position_latitude", DataTypes.DoubleType, true),
                        DataTypes.createStructField("start_position_position_id", DataTypes.LongType, true),
                        DataTypes.createStructField("start_position_speed_kilometres_per_hour", DataTypes.DoubleType, true),
                        DataTypes.createStructField("end_odometer_kilometres", DataTypes.DoubleType, true),
                        DataTypes.createStructField("end_position_timestamp", DataTypes.TimestampType, true),
                        DataTypes.createStructField("end_position_longitude", DataTypes.DoubleType, true),
                        DataTypes.createStructField("end_position_latitude", DataTypes.DoubleType, true),
                        DataTypes.createStructField("end_position_position_id", DataTypes.LongType, true),
                        DataTypes.createStructField("end_position_speed_kilometres_per_hour", DataTypes.DoubleType, true),
                        DataTypes.createStructField("value_type", DataTypes.StringType, true),
                        DataTypes.createStructField("value_units", DataTypes.StringType, true),
                        DataTypes.createStructField("media_urls_road", DataTypes.StringType, true),
                        DataTypes.createStructField("media_urls_cab", DataTypes.StringType, true),
                        DataTypes.createStructField("media_urls_camera3", DataTypes.StringType, true),
                        DataTypes.createStructField("media_urls_camera4", DataTypes.StringType, true),
                        DataTypes.createStructField("location_id", DataTypes.LongType, true),
                        DataTypes.createStructField("speed_limit", DataTypes.DoubleType, true),
                        DataTypes.createStructField("year", DataTypes.IntegerType, true),
                        DataTypes.createStructField("week_number", DataTypes.IntegerType, true)
                }
        );
    }
}
