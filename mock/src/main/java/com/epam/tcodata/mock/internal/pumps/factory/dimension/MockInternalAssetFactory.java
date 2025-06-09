package com.epam.tcodata.mock.internal.pumps.factory.dimension;

import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.internal.pump.converter.dimension.AssetConverter;
import com.epam.tcodata.internal.pump.handler.IDataHandler;
import com.epam.tcodata.internal.pump.handler.dimension.AssetDataHandler;
import com.epam.tcodata.mock.internal.pumps.factory.MockAbstractInternalFactory;
import com.epam.tcodata.models.avro.dimension.AvroAsset;
import com.epam.tcodata.models.datalake.raw.dimension.RawAsset;
import com.epam.tcodata.models.enriched.dimension.EnrichedAsset;
import com.epam.tcodata.models.mix.dimension.Asset;
import org.apache.spark.sql.SparkSession;

public class MockInternalAssetFactory extends MockAbstractInternalFactory<Asset, EnrichedAsset, AvroAsset, RawAsset> {
    private static final long serialVersionUID = -2274665909882020815L;

    public MockInternalAssetFactory() {
        super(Asset.class);
    }

    @Override
    public IDataHandler createEventDataHandler(SparkSession sparkSession) {
        return new AssetDataHandler(this, sparkSession);
    }

    @Override
    public IEntityConverter createConverter() {
        return new AssetConverter();
    }
}
