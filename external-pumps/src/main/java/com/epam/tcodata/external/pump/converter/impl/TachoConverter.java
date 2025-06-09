package com.epam.tcodata.external.pump.converter.impl;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.dto.fact.impl.TachoDto;
import com.epam.tcodata.external.pump.util.ConverterUtil;
import com.epam.tcodata.mdm.IKeyManager;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.avro.fact.AvroTacho;
import com.epam.tcodata.models.avro.fact.AvroTachoInterval;
import com.epam.tcodata.models.avro.fact.AvroTachoParameterDefinition;
import com.epam.tcodata.models.avro.fact.AvroTachoParameterValue;
import com.epam.tcodata.models.enriched.fact.EnrichedTacho;
import com.epam.tcodata.models.mix.fact.Tacho;
import com.epam.tcodata.models.mix.fact.TachoInterval;
import com.epam.tcodata.models.mix.fact.TachoParameterDefinition;
import com.epam.tcodata.models.mix.fact.TachoParameterValue;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TachoConverter implements IConverter<Tacho, EnrichedTacho, AvroTacho> {

    private static final long serialVersionUID = 5956642855893031415L;

    // general properties
    private static final int AVRO_SCHEMA_VERSION = 1;

    /**
     * Converter for Tacho.
     */
    public TachoConverter() {
        /***  Default implementation ***/
    }

    @Override
    public EnrichedTacho convertToEnriched(Tacho tacho, AbstractDto dto, IKeyManager keyManager) {
        // copying main fields
        EnrichedTacho enrichedTacho = new EnrichedTacho(tacho);

        Timestamp ingestedDate = Timestamp.from(Instant.now());

        String durableKey = keyManager.factDurableKey(EntityType.TACHO,
                "" + tacho.getAssetId()).toString();

        //enriched with additional info
        enrichedTacho
                .setDurableId(durableKey)
                .setIngestedDateUtc(ingestedDate)
                .setSubscriptionId(((TachoDto) dto).getOrgGroupId())
                .setLineageCode(4);

        return enrichedTacho;
    }

    @Override
    public AvroTacho convertToAvro(EnrichedTacho enrichedTacho) {
        return AvroTacho.newBuilder()

                // enriched fields
                .setDurableId(enrichedTacho.getDurableId())
                .setIngestedDateUtc(ConverterUtil.timestampToDateTime(enrichedTacho.getIngestedDateUtc()))
                .setSubscriptionId(enrichedTacho.getSubscriptionId())
                .setLineageCode(enrichedTacho.getLineageCode())

                // transport message support
                .setSchemaVersion(AVRO_SCHEMA_VERSION)

                // main fields
                .setAssetId(enrichedTacho.getAssetId())
                .setParameterDefinitions(convertParameterDefinitionsToAvro(enrichedTacho.getParameterDefinitions()))
                .setIntervals(convertIntervalsToAvro(enrichedTacho.getIntervals()))
                .setStartDateTime(ConverterUtil.timestampToDateTime(enrichedTacho.getStartDateTime()))
                .setEndDateTime(ConverterUtil.timestampToDateTime(enrichedTacho.getEndDateTime()))
                .build();
    }

    private List<AvroTachoParameterDefinition> convertParameterDefinitionsToAvro(
            List<TachoParameterDefinition> parameterDefinitions) {
        if (parameterDefinitions == null) {
            return new ArrayList<>();
        }
        return parameterDefinitions.stream()
                .map(pd -> AvroTachoParameterDefinition.newBuilder()
                        .setKey(pd.getKey())
                        .setParameterId(pd.getParameterId())
                        .setDeviceId(pd.getDeviceId())
                        .setLineName(pd.getLineName())
                        .build())
                .collect(Collectors.toList());
    }

    private List<AvroTachoInterval> convertIntervalsToAvro(List<TachoInterval> intervals) {
        if (intervals == null) {
            return new ArrayList<>();
        }
        return intervals.stream()
                .map(interval -> AvroTachoInterval.newBuilder()
                        .setIntervalDateTime(ConverterUtil.timestampToDateTime(interval.getIntervalDateTime()))
                        .setData(convertDataToAvro(interval.getData()))
                        .build())
                .collect(Collectors.toList());
    }

    private List<AvroTachoParameterValue> convertDataToAvro(List<TachoParameterValue> data) {
        if (data == null) {
            return new ArrayList<>();
        }
        return data.stream()
                .map(tpv -> AvroTachoParameterValue.newBuilder()
                        .setValue(tpv.getValue())
                        .setKey(tpv.getKey())
                        .build())
                .collect(Collectors.toList());
    }

    //TO DO
    @Override
    public Dataset<Row> convertToDataset(SparkSession sparkSession, JavaRDD enrichedEntityJavaRDD) {
        JavaRDD<Row> rows = prepareForWriting(enrichedEntityJavaRDD);
        return sparkSession.createDataFrame(rows, getDataLakeSchema());
    }

    private JavaRDD<Row> prepareForWriting(JavaRDD<EnrichedTacho> enrichedTachoJavaRDD) {
        return enrichedTachoJavaRDD.map((Function<EnrichedTacho, Row>) enrichedTacho ->
                RowFactory.create(enrichedTacho.getOrderedValues()));
    }

    @SuppressWarnings("CPD-START")
    StructType getDataLakeSchema() {
        return new StructType(
                new StructField[]{
                        DataTypes.createStructField("durable_id", DataTypes.StringType, true),
                        DataTypes.createStructField("ingested_date_utc", DataTypes.TimestampType, true),
                        DataTypes.createStructField("subscription_id", DataTypes.LongType, true),
                        DataTypes.createStructField("lineage_code", DataTypes.IntegerType, true),
                        DataTypes.createStructField("asset_id", DataTypes.LongType, true),
                        DataTypes.createStructField("parameter_definitions", DataTypes.StringType, true),
                        DataTypes.createStructField("intervals", DataTypes.StringType, true),
                        DataTypes.createStructField("start_date_time", DataTypes.TimestampType, true),
                        DataTypes.createStructField("end_date_time", DataTypes.TimestampType, true)
                }
        );
    }
}
