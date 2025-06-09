package com.epam.tcodata.external.pump.converter.impl;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.dto.dimension.DimensionDto;
import com.epam.tcodata.mdm.Decision;
import com.epam.tcodata.mdm.IKeyManager;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.avro.dimension.AvroAsset;
import com.epam.tcodata.models.enriched.dimension.EnrichedAsset;
import com.epam.tcodata.models.mix.dimension.Asset;
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

public class AssetConverter implements IConverter<Asset, EnrichedAsset, AvroAsset> {

    private static final long serialVersionUID = -1478919728095927748L;

    // general properties
    private static final int AVRO_SCHEMA_VERSION = 1;

    @Override
    public EnrichedAsset convertToEnriched(Asset asset, AbstractDto dto, IKeyManager keyManager) {
        EnrichedAsset enrichedAsset = new EnrichedAsset(asset);

        Decision decision = keyManager.findOrCreate(asset, ApiVersion.API_2_0, String.valueOf(((DimensionDto) dto).getOrgGroupId()), EntityType.ASSET);
        String surrogateKeyStr = decision == null ? null : String.valueOf(decision.getSurrogateKey());

        enrichedAsset
                //enriched with additional info
                .setDurableId(surrogateKeyStr)
                .setIngestedDateUtc(Timestamp.from(Instant.now()))
                .setSubscriptionId(((DimensionDto) dto).getOrgGroupId())
                .setLineageCode(4);

        return enrichedAsset;
    }

    @Override
    public AvroAsset convertToAvro(EnrichedAsset enrichedAsset) {
        return AvroAsset.newBuilder()

                // enriched fields
                .setDurableId(enrichedAsset.getDurableId())
                .setIngestedDateUtc(timestampToDateTime(enrichedAsset.getIngestedDateUtc()))
                .setSubscriptionId(enrichedAsset.getSubscriptionId())
                .setLineageCode(enrichedAsset.getLineageCode())

                // transport message support
                .setSchemaVersion(AVRO_SCHEMA_VERSION)

                // main fields
                .setAssetId(enrichedAsset.getAssetId())
                .setAssetTypeId(enrichedAsset.getAssetTypeId())
                .setDescription(enrichedAsset.getDescription())
                .setConnectedTrailer(enrichedAsset.getConnectedTrailer())
                .setRegistrationNumber(enrichedAsset.getRegistrationNumber())
                .setSiteId(enrichedAsset.getSiteId())
                .setFuelType(enrichedAsset.getFuelType())
                .setTargetFuelConsumption(enrichedAsset.getTargetFuelConsumption())
                .setTargetFuelConsumptionUnits(enrichedAsset.getTargetFuelConsumptionUnits())
                .setTargetHourlyFuelConsumption(enrichedAsset.getTargetHourlyFuelConsumption())
                .setTargetHourlyFuelConsumptionUnits(enrichedAsset.getTargetHourlyFuelConsumptionUnits())
                .setFleetNumber(enrichedAsset.getFleetNumber())
                .setMake(enrichedAsset.getMake())
                .setModel(enrichedAsset.getModel())
                .setYear(enrichedAsset.getYear())
                .setVinNumber(enrichedAsset.getVinNumber())
                .setEngineNumber(enrichedAsset.getEngineNumber())
                .setFmVehicleId(enrichedAsset.getFmVehicleId())
                .setAdditionalMobileDevice(enrichedAsset.getAdditionalMobileDevice())
                .setNotes(enrichedAsset.getNotes())
                .setIcon(enrichedAsset.getIcon())
                .setIconColour(enrichedAsset.getIconColour())
                .setColour(enrichedAsset.getColour())
                .setAssetImage(enrichedAsset.getAssetImage())
                .setDefaultImage(enrichedAsset.getDefaultImage())
                .setAssetImageUrl(enrichedAsset.getAssetImageUrl())
                .setUserState(enrichedAsset.getUserState())
                .setCreatedBy(enrichedAsset.getCreatedBy())
                .setCreatedDate(timestampToDateTime(enrichedAsset.getCreatedDate()))
                .setOdometer(enrichedAsset.getOdometer())
                .setEngineHours(enrichedAsset.getEngineHours())
                .setCountry(enrichedAsset.getCountry())
                .build();
    }

    @SuppressWarnings("CPD-START")
    @Override
    public Dataset<Row> convertToDataset(SparkSession sparkSession, JavaRDD enrichedEntityJavaRDD) {
        JavaRDD<Row> rows = prepareForWriting(enrichedEntityJavaRDD);
        return sparkSession.createDataFrame(rows, getDataLakeSchema());
    }

    private JavaRDD<Row> prepareForWriting(JavaRDD<EnrichedAsset> enrichedEntityJavaRDD) {
        return enrichedEntityJavaRDD.map((Function<EnrichedAsset, Row>) enrichedAsset ->
                RowFactory.create(enrichedAsset.getOrderedValues()));
    }

    StructType getDataLakeSchema() {
        return new StructType(
                new StructField[] {
                        DataTypes.createStructField("durable_id", DataTypes.StringType, true),
                        DataTypes.createStructField("ingested_date_utc", DataTypes.TimestampType, true),
                        DataTypes.createStructField("subscription_id", DataTypes.LongType, true),
                        DataTypes.createStructField("lineage_code", DataTypes.IntegerType, true),
                        DataTypes.createStructField("asset_id", DataTypes.LongType, true),
                        DataTypes.createStructField("asset_type_id", DataTypes.IntegerType, true),
                        DataTypes.createStructField("description", DataTypes.StringType, true),
                        DataTypes.createStructField("connected_trailer", DataTypes.BooleanType, true),
                        DataTypes.createStructField("registration_number", DataTypes.StringType, true),
                        DataTypes.createStructField("site_id", DataTypes.LongType, true),
                        DataTypes.createStructField("fuel_type", DataTypes.StringType, true),
                        DataTypes.createStructField("target_fuel_consumption", DataTypes.DoubleType, true),
                        DataTypes.createStructField("target_fuel_consumption_units", DataTypes.StringType, true),
                        DataTypes.createStructField("target_hourly_fuel_consumption", DataTypes.DoubleType, true),
                        DataTypes.createStructField("target_hourly_fuel_consumption_units", DataTypes.StringType, true),
                        DataTypes.createStructField("fleet_number", DataTypes.StringType, true),
                        DataTypes.createStructField("make", DataTypes.StringType, true),
                        DataTypes.createStructField("model", DataTypes.StringType, true),
                        DataTypes.createStructField("year", DataTypes.StringType, true),
                        DataTypes.createStructField("vin_number", DataTypes.StringType, true),
                        DataTypes.createStructField("engine_number", DataTypes.StringType, true),
                        DataTypes.createStructField("fm_vehicle_id", DataTypes.LongType, true),
                        DataTypes.createStructField("additional_mobile_device", DataTypes.StringType, true),
                        DataTypes.createStructField("notes", DataTypes.StringType, true),
                        DataTypes.createStructField("icon", DataTypes.StringType, true),
                        DataTypes.createStructField("icon_colour", DataTypes.StringType, true),
                        DataTypes.createStructField("colour", DataTypes.StringType, true),
                        DataTypes.createStructField("asset_image", DataTypes.StringType, true),
                        DataTypes.createStructField("default_image", DataTypes.BooleanType, true),
                        DataTypes.createStructField("asset_image_url", DataTypes.StringType, true),
                        DataTypes.createStructField("user_state", DataTypes.StringType, true),
                        DataTypes.createStructField("created_by", DataTypes.StringType, true),
                        DataTypes.createStructField("created_date", DataTypes.TimestampType, true),
                        DataTypes.createStructField("odometer", DataTypes.DoubleType, true),
                        DataTypes.createStructField("engine_hours", DataTypes.StringType, true),
                        DataTypes.createStructField("country", DataTypes.StringType, true)
                }
        );
    }
}
