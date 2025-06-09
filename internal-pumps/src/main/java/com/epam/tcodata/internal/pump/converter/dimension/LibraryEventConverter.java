package com.epam.tcodata.internal.pump.converter.dimension;

import com.epam.tcodata.internal.pump.converter.AbstractEntityConverter;
import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.models.avro.dimension.AvroLibraryEvent;
import com.epam.tcodata.models.datalake.raw.dimension.RawLibraryEvent;
import com.epam.tcodata.models.enriched.dimension.EnrichedLibraryEvent;
import com.epam.tcodata.models.mix.dimension.LibraryEvent;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.sql.Timestamp;

import static com.epam.tcodata.common.ConverterUtils.checkedToString;

public class LibraryEventConverter extends AbstractEntityConverter<AvroLibraryEvent, EnrichedLibraryEvent, RawLibraryEvent> {

    private static final long serialVersionUID = -1061027830842047965L;

    /**
     * Main public constructor.
     */
    public LibraryEventConverter() {
        /***  Default implementation ***/
    }

    /**
     * Method converts avro entity to enriched entity.
     *
     * @return EnrichedLibraryEvent
     */
    @Override
    public EnrichedLibraryEvent convertToEnriched(AvroLibraryEvent avro) {
        LibraryEvent.LibraryEventBuilder libraryEventBuilder = new LibraryEvent.LibraryEventBuilder()
                .setDescription(checkedToString(avro.getDescription()))
                .setEventTypeId(avro.getEventTypeId())
                .setEventType(checkedToString(avro.getEventType()))
                .setDisplayUnits(checkedToString(avro.getDisplayUnits()))
                .setFormatType(checkedToString(avro.getFormatType()))
                .setValueName(checkedToString(avro.getValueName()));

        LibraryEvent libraryEvent = libraryEventBuilder.build();

        return new EnrichedLibraryEvent(libraryEvent)
                // IEnrichable fields
                .setDurableId(checkedToString(avro.getDurableId()))
                .setIngestedDateUtc(IEntityConverter.dateTimeToTimestamp(avro.getIngestedDateUtc()))
                .setSubscriptionId(avro.getSubscriptionId())
                .setLineageCode(avro.getLineageCode());
    }

    /**
     * Method converts enriched entity to dataLake entity.
     *
     * @return DataLakeLibraryEvent
     */
    @Override
    public RawLibraryEvent convertToRaw(EnrichedLibraryEvent enriched, Timestamp persistedDate) {
        RawLibraryEvent dataLakeLibraryEvent = new RawLibraryEvent();
        // DataLakeEntity fields
        dataLakeLibraryEvent.setDurableId(enriched.getDurableId());
        dataLakeLibraryEvent.setIngestedDateUtc(enriched.getIngestedDateUtc());
        dataLakeLibraryEvent.setSubscriptionId(enriched.getSubscriptionId());
        dataLakeLibraryEvent.setLineageCode(enriched.getLineageCode());
        dataLakeLibraryEvent.setPersistedDateUtc(persistedDate);
        // DataLakeLibraryEvent fields
        dataLakeLibraryEvent.setDescription(enriched.getDescription());
        dataLakeLibraryEvent.setEventTypeId(enriched.getEventTypeId());
        dataLakeLibraryEvent.setEventType(enriched.getEventType());
        dataLakeLibraryEvent.setDisplayUnits(enriched.getDisplayUnits());
        dataLakeLibraryEvent.setFormatType(enriched.getFormatType());
        dataLakeLibraryEvent.setValueName(enriched.getValueName());
        return dataLakeLibraryEvent;
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
                        DataTypes.createStructField("description", DataTypes.StringType, true),
                        DataTypes.createStructField("event_type_id", DataTypes.LongType, true),
                        DataTypes.createStructField("event_type", DataTypes.StringType, true),
                        DataTypes.createStructField("display_units", DataTypes.StringType, true),
                        DataTypes.createStructField("format_type", DataTypes.StringType, true),
                        DataTypes.createStructField("value_name", DataTypes.StringType, true)
                }
        );
    }
}
