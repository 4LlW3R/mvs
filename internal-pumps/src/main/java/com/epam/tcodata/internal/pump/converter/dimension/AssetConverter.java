package com.epam.tcodata.internal.pump.converter.dimension;

import com.epam.tcodata.internal.pump.converter.AbstractEntityConverter;
import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.models.avro.dimension.AvroAsset;
import com.epam.tcodata.models.datalake.raw.dimension.RawAsset;
import com.epam.tcodata.models.enriched.dimension.EnrichedAsset;
import com.epam.tcodata.models.mix.dimension.Asset;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.sql.Timestamp;

import static com.epam.tcodata.common.ConverterUtils.checkedToString;

public class AssetConverter extends AbstractEntityConverter<AvroAsset, EnrichedAsset, RawAsset> {

    private static final long serialVersionUID = -3158681515461001947L;

    /**
     * Main public constructor.
     */
    public AssetConverter() {
        /***  Default implementation ***/
    }

    /**
     * Method converts avro entity to enriched entity.
     *
     * @return EnrichedAsset
     */
    @Override
    public EnrichedAsset convertToEnriched(AvroAsset avro) {
        Asset.AssetBuilder assetBuilder = new Asset.AssetBuilder()
                .setAssetId(avro.getAssetId())
                .setAssetTypeId(avro.getAssetTypeId())
                .setDescription(checkedToString(avro.getDescription()))
                .setConnectedTrailer(avro.getConnectedTrailer())
                .setRegistrationNumber(checkedToString(avro.getRegistrationNumber()))
                .setSiteId(avro.getSiteId())
                .setFuelType(checkedToString(avro.getFuelType()))
                .setTargetFuelConsumption(avro.getTargetFuelConsumption())
                .setTargetFuelConsumptionUnits(checkedToString(avro.getTargetFuelConsumptionUnits()))
                .setTargetHourlyFuelConsumption(avro.getTargetHourlyFuelConsumption())
                .setTargetHourlyFuelConsumptionUnits(checkedToString(avro.getTargetHourlyFuelConsumptionUnits()))
                .setFleetNumber(checkedToString(avro.getFleetNumber()))
                .setMake(checkedToString(avro.getMake()))
                .setModel(checkedToString(avro.getModel()))
                .setYear(checkedToString(avro.getYear()))
                .setVinNumber(checkedToString(avro.getVinNumber()))
                .setEngineNumber(checkedToString(avro.getEngineNumber()))
                .setFmVehicleId(avro.getFmVehicleId())
                .setAdditionalMobileDevice(checkedToString(avro.getAdditionalMobileDevice()))
                .setNotes(checkedToString(avro.getNotes()))
                .setIcon(checkedToString(avro.getIcon()))
                .setIconColour(checkedToString(avro.getIconColour()))
                .setColour(checkedToString(avro.getColour()))
                .setAssetImage(checkedToString(avro.getAssetImage()))
                .setDefaultImage(avro.getDefaultImage())
                .setAssetImageUrl(checkedToString(avro.getAssetImageUrl()))
                .setUserState(checkedToString(avro.getUserState()))
                .setCreatedBy(checkedToString(avro.getCreatedBy()))
                .setCreatedDate(IEntityConverter.dateTimeToTimestamp(avro.getCreatedDate()))
                .setOdometer(avro.getOdometer())
                .setEngineHours(checkedToString(avro.getEngineHours()))
                .setCountry(checkedToString(avro.getCountry()));

        Asset asset = assetBuilder.build();

        return new EnrichedAsset(asset)
                // IEnrichable fields
                .setDurableId(checkedToString(avro.getDurableId()))
                .setIngestedDateUtc(IEntityConverter.dateTimeToTimestamp(avro.getIngestedDateUtc()))
                .setSubscriptionId(avro.getSubscriptionId())
                .setLineageCode(avro.getLineageCode());
    }

    /**
     * Method converts enriched entity to dataLake entity.
     *
     * @return DataLakeAsset
     */
    @Override
    public RawAsset convertToRaw(EnrichedAsset enriched, Timestamp persistedDate) {
        RawAsset dataLakeAsset = new RawAsset();
        // DataLakeEntity fields
        dataLakeAsset.setDurableId(enriched.getDurableId());
        dataLakeAsset.setIngestedDateUtc(enriched.getIngestedDateUtc());
        dataLakeAsset.setSubscriptionId(enriched.getSubscriptionId());
        dataLakeAsset.setLineageCode(enriched.getLineageCode());
        dataLakeAsset.setPersistedDateUtc(persistedDate);
        // DataLakeAsset fields
        dataLakeAsset.setAssetId(enriched.getAssetId());
        dataLakeAsset.setAssetTypeId(enriched.getAssetTypeId());
        dataLakeAsset.setDescription(enriched.getDescription());
        dataLakeAsset.setConnectedTrailer(enriched.getConnectedTrailer());
        dataLakeAsset.setRegistrationNumber(enriched.getRegistrationNumber());
        dataLakeAsset.setSiteId(enriched.getSiteId());
        dataLakeAsset.setFuelType(enriched.getFuelType());
        dataLakeAsset.setTargetFuelConsumption(enriched.getTargetFuelConsumption());
        dataLakeAsset.setTargetFuelConsumptionUnits(enriched.getTargetFuelConsumptionUnits());
        dataLakeAsset.setTargetHourlyFuelConsumption(enriched.getTargetHourlyFuelConsumption());
        dataLakeAsset.setTargetHourlyFuelConsumptionUnits(enriched.getTargetHourlyFuelConsumptionUnits());
        dataLakeAsset.setFleetNumber(enriched.getFleetNumber());
        dataLakeAsset.setMake(enriched.getMake());
        dataLakeAsset.setModel(enriched.getModel());
        dataLakeAsset.setYear(enriched.getYear());
        dataLakeAsset.setVinNumber(enriched.getVinNumber());
        dataLakeAsset.setEngineNumber(enriched.getEngineNumber());
        dataLakeAsset.setFmVehicleId(enriched.getFmVehicleId());
        dataLakeAsset.setAdditionalMobileDevice(enriched.getAdditionalMobileDevice());
        dataLakeAsset.setNotes(enriched.getNotes());
        dataLakeAsset.setIcon(enriched.getIcon());
        dataLakeAsset.setIconColour(enriched.getIconColour());
        dataLakeAsset.setColour(enriched.getColour());
        dataLakeAsset.setAssetImage(enriched.getAssetImage());
        dataLakeAsset.setDefaultImage(enriched.getDefaultImage());
        dataLakeAsset.setAssetImageUrl(enriched.getAssetImageUrl());
        dataLakeAsset.setUserState(enriched.getUserState());
        dataLakeAsset.setCreatedBy(enriched.getCreatedBy());
        dataLakeAsset.setCreatedDate(enriched.getCreatedDate());
        dataLakeAsset.setOdometer(enriched.getOdometer());
        dataLakeAsset.setEngineHours(enriched.getEngineHours());
        dataLakeAsset.setCountry(enriched.getCountry());
        return dataLakeAsset;
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
