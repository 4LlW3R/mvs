package com.epam.tcodata.internal.pump.converter.fact;

import com.epam.tcodata.internal.pump.converter.AbstractEntityConverter;
import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.models.avro.fact.AvroTacho;
import com.epam.tcodata.models.avro.fact.AvroTachoInterval;
import com.epam.tcodata.models.avro.fact.AvroTachoParameterDefinition;
import com.epam.tcodata.models.avro.fact.AvroTachoParameterValue;
import com.epam.tcodata.models.datalake.raw.fact.RawTacho;
import com.epam.tcodata.models.enriched.fact.EnrichedTacho;
import com.epam.tcodata.models.mix.fact.Tacho;
import com.epam.tcodata.models.mix.fact.TachoInterval;
import com.epam.tcodata.models.mix.fact.TachoParameterDefinition;
import com.epam.tcodata.models.mix.fact.TachoParameterValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.sql.Timestamp;
import java.util.stream.Collectors;

import static com.epam.tcodata.common.ConverterUtils.checkedToString;

@SuppressWarnings("CPD-START")
public class TachoConverter extends AbstractEntityConverter<AvroTacho, EnrichedTacho, RawTacho> {

    private static final long serialVersionUID = -3923390165247226065L;

    private ObjectMapper objectMapper;

    /**
     * Main public constructor.
     */
    public TachoConverter() {
        this.objectMapper = new ObjectMapper();
    }


    /**
     * Method converts avro entity to enriched entity.
     *
     * @return EnrichedTacho
     */
    @Override
    public EnrichedTacho convertToEnriched(AvroTacho avro) {
        Tacho.TachoBuilder tachoBuilder = new Tacho.TachoBuilder()
                .setAssetId(avro.getAssetId())
                .setParameterDefinitions(avro.getParameterDefinitions().stream()
                        .map(TachoConverter::convertToTachoParameterDefinition).collect(Collectors.toList()))
                .setIntervals(avro.getIntervals().stream()
                        .map(TachoConverter::convertToTachoInterval).collect(Collectors.toList()))
                .setStartDateTime(IEntityConverter.dateTimeToTimestamp(avro.getStartDateTime()))
                .setEndDateTime(IEntityConverter.dateTimeToTimestamp(avro.getEndDateTime()));

        Tacho tacho = tachoBuilder.build();

        return new EnrichedTacho(tacho)
                // IEnrichable fields
                .setDurableId(checkedToString(avro.getDurableId()))
                .setIngestedDateUtc(IEntityConverter.dateTimeToTimestamp(avro.getIngestedDateUtc()))
                .setSubscriptionId(avro.getSubscriptionId())
                .setLineageCode(avro.getLineageCode());
    }

    private static TachoParameterDefinition convertToTachoParameterDefinition(AvroTachoParameterDefinition avro) {
        TachoParameterDefinition tachoParameterDefinition = new TachoParameterDefinition();
        tachoParameterDefinition.setKey(avro.getKey());
        tachoParameterDefinition.setParameterId(avro.getParameterId());
        tachoParameterDefinition.setDeviceId(avro.getDeviceId());
        tachoParameterDefinition.setLineName(checkedToString(avro.getLineName()));
        return tachoParameterDefinition;
    }

    private static TachoInterval convertToTachoInterval(AvroTachoInterval avro) {
        TachoInterval tachoInterval = new TachoInterval();
        tachoInterval.setIntervalDateTime(IEntityConverter.dateTimeToTimestamp(avro.getIntervalDateTime()));
        tachoInterval.setData(avro.getData().stream()
                .map(TachoConverter::convertToTachoParameterValue).collect(Collectors.toList()));
        return tachoInterval;
    }

    private static TachoParameterValue convertToTachoParameterValue(AvroTachoParameterValue avro) {
        TachoParameterValue tachoParameterValue = new TachoParameterValue();
        tachoParameterValue.setValue(avro.getValue());
        tachoParameterValue.setKey(avro.getKey());
        return tachoParameterValue;
    }

    /**
     * Method converts enriched entity to dataLake entity.
     *
     * @return DataLakeTacho
     */
    @Override
    public RawTacho convertToRaw(EnrichedTacho enriched, Timestamp persistedDate) {
        RawTacho dataLakeTacho = new RawTacho();
        try {
            // DataLakeEntity fields
            dataLakeTacho.setYear(IEntityConverter.timestampToYear(persistedDate));
            dataLakeTacho.setWeekNumber(IEntityConverter.timestampToWeekNumber(persistedDate));
            dataLakeTacho.setDurableId(enriched.getDurableId());
            dataLakeTacho.setIngestedDateUtc(enriched.getIngestedDateUtc());
            dataLakeTacho.setSubscriptionId(enriched.getSubscriptionId());
            dataLakeTacho.setLineageCode(enriched.getLineageCode());
            dataLakeTacho.setPersistedDateUtc(persistedDate);
            // DataLakeTacho fields
            dataLakeTacho.setAssetId(enriched.getAssetId());
            dataLakeTacho.setTachoParameterDefinitions(objectMapper.writeValueAsString(enriched.getParameterDefinitions()));
            dataLakeTacho.setTachoIntervals(objectMapper.writeValueAsString(enriched.getIntervals()));
            dataLakeTacho.setStartDateTime(enriched.getStartDateTime());
            dataLakeTacho.setEndDateTime(enriched.getEndDateTime());
        } catch (JsonProcessingException e) {
            LOGGER.error("Exception while trying convert EnrichedTacho to RawTacho: {}", enriched);
        }
        return dataLakeTacho;
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
                        DataTypes.createStructField("asset_id", DataTypes.LongType, true),
                        DataTypes.createStructField("tacho_parameter_definitions", DataTypes.StringType, true),
                        DataTypes.createStructField("tacho_intervals", DataTypes.StringType, true),
                        DataTypes.createStructField("start_date_time", DataTypes.TimestampType, true),
                        DataTypes.createStructField("end_date_time", DataTypes.TimestampType, true),
                        DataTypes.createStructField("year", DataTypes.IntegerType, true),
                        DataTypes.createStructField("week_number", DataTypes.IntegerType, true)
                }
        );
    }
}
