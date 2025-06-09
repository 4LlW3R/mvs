package com.epam.tcodata.mock.external.pump.factory.impl.mix;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.converter.impl.AssetConverter;
import com.epam.tcodata.external.pump.source.IMixSource;
import com.epam.tcodata.mock.external.pump.factory.MixMockAbstractExternalFactory;
import com.epam.tcodata.mock.external.pump.source.dimension.impl.mix.MixMockAssetMixSource;
import com.epam.tcodata.models.avro.dimension.AvroAsset;
import com.epam.tcodata.models.enriched.dimension.EnrichedAsset;
import com.epam.tcodata.models.mix.dimension.Asset;

public class MixMockExternalAssetFactory extends MixMockAbstractExternalFactory<Asset, EnrichedAsset, AvroAsset> {

    private static final long serialVersionUID = -6977675976274391286L;

    public MixMockExternalAssetFactory() {
        super(Asset.class);
    }

    @Override
    public IMixSource<Asset> createMixSource() {
        return new MixMockAssetMixSource();
    }

    @Override
    public IConverter<Asset, EnrichedAsset, AvroAsset> createConverter() {
        return new AssetConverter();
    }
}
