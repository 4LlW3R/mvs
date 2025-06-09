package com.epam.tcodata.redis.manager.converter;

import com.epam.tcodata.models.avro.dimension.AvroAsset;
import com.epam.tcodata.models.datalake.raw.dimension.RawAsset;
import com.epam.tcodata.models.enriched.dimension.EnrichedAsset;
import com.epam.tcodata.models.mix.dimension.Asset;

import java.io.Serializable;

import static com.epam.tcodata.common.ConverterUtils.checkedToString;

@Deprecated
public class AssetConverter implements Serializable {

    private static final long serialVersionUID = 2518557458476184753L;

    /**
     * Method converts avro entity to enriched entity.
     *
     * @return EnrichedDriver
     */
    public static EnrichedAsset convertToEnriched(AvroAsset avro) {
        Asset.AssetBuilder assetBuilder = new Asset.AssetBuilder()
                .setRegistrationNumber(checkedToString(avro.getRegistrationNumber()))
                .setDescription(checkedToString(avro.getDescription()))
                .setAssetTypeId(avro.getAssetTypeId());

        Asset asset = assetBuilder.build();

        return new EnrichedAsset(asset)
                .setDurableId(checkedToString(avro.getDurableId()));
    }

    /**
     * Method converts enriched entity to dataLake entity.
     *
     * @return DataLakeDriver
     */
    public static RawAsset convertToDataLake(EnrichedAsset enriched) {
        RawAsset dataLakeAsset = new RawAsset();
        dataLakeAsset.setDurableId(enriched.getDurableId());
        dataLakeAsset.setRegistrationNumber(enriched.getRegistrationNumber());
        dataLakeAsset.setDescription(enriched.getDescription());
        dataLakeAsset.setAssetTypeId(enriched.getAssetTypeId());
        return dataLakeAsset;
    }
}
