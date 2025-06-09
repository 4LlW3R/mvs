package com.epam.tcodata.external.pump.converter.impl;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.dto.dimension.DimensionDto;
import com.epam.tcodata.mdm.Decision;
import com.epam.tcodata.mdm.IKeyManager;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.avro.dimension.AvroLibraryEvent;
import com.epam.tcodata.models.enriched.dimension.EnrichedLibraryEvent;
import com.epam.tcodata.models.mix.dimension.LibraryEvent;
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

public class LibraryEventConverter implements IConverter<LibraryEvent, EnrichedLibraryEvent, AvroLibraryEvent> {

    private static final long serialVersionUID = 4759439099738357996L;

    // general properties
    private static final int AVRO_SCHEMA_VERSION = 1;

    @Override
    public EnrichedLibraryEvent convertToEnriched(LibraryEvent libraryEvent, AbstractDto dto, IKeyManager keyManager) {
        EnrichedLibraryEvent enrichedLibraryEvent = new EnrichedLibraryEvent(libraryEvent);

        Decision decision = keyManager.findOrCreate(libraryEvent, ApiVersion.API_2_0, String.valueOf(((DimensionDto) dto).getOrgGroupId()), EntityType.LIBRARY_EVENT);

        String surrogateKeyStr = decision == null ? null : decision.getSurrogateKey().toString();

        enrichedLibraryEvent

                //enriched with additional info
                .setDurableId(surrogateKeyStr) //TO DO
                .setIngestedDateUtc(Timestamp.from(Instant.now()))
                .setSubscriptionId(((DimensionDto) dto).getOrgGroupId())
                .setLineageCode(4);

        return enrichedLibraryEvent;
    }

    @Override
    public AvroLibraryEvent convertToAvro(EnrichedLibraryEvent enrichedLibraryEvent) {
        return AvroLibraryEvent.newBuilder()

                // enriched fields
                .setDurableId(enrichedLibraryEvent.getDurableId())
                .setIngestedDateUtc(timestampToDateTime(enrichedLibraryEvent.getIngestedDateUtc()))
                .setSubscriptionId(enrichedLibraryEvent.getSubscriptionId())
                .setLineageCode(enrichedLibraryEvent.getLineageCode())

                // transport message support
                .setSchemaVersion(AVRO_SCHEMA_VERSION)

                // main fields
                .setDescription(enrichedLibraryEvent.getDescription())
                .setEventTypeId(enrichedLibraryEvent.getEventTypeId())
                .setEventType(enrichedLibraryEvent.getEventType())
                .setDisplayUnits(enrichedLibraryEvent.getDisplayUnits())
                .setFormatType(enrichedLibraryEvent.getFormatType())
                .setValueName(enrichedLibraryEvent.getValueName())
                .build();
    }

    @Override
    public Dataset<Row> convertToDataset(SparkSession sparkSession, JavaRDD enrichedEntityJavaRDD) {
        JavaRDD<Row> rows = prepareForWriting(enrichedEntityJavaRDD);
        return sparkSession.createDataFrame(rows, getDataLakeSchema());
    }

    private JavaRDD<Row> prepareForWriting(JavaRDD<EnrichedLibraryEvent> enrichedEntityJavaRDD) {
        return enrichedEntityJavaRDD.map((Function<EnrichedLibraryEvent, Row>) enrichedLibraryEvent ->
                RowFactory.create(enrichedLibraryEvent.getOrderedValues()));
    }

    @SuppressWarnings("CPD-START")
    StructType getDataLakeSchema() {
        return new StructType(
                new StructField[] {
                        DataTypes.createStructField("durable_id", DataTypes.StringType, true),
                        DataTypes.createStructField("ingested_date_utc", DataTypes.TimestampType, true),
                        DataTypes.createStructField("subscription_id", DataTypes.LongType, true),
                        DataTypes.createStructField("lineage_code", DataTypes.IntegerType, true),
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
