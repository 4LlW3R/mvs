package com.epam.tcodata.mock.external.pump.factory.impl;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.converter.impl.AssetConverter;
import com.epam.tcodata.external.pump.source.IMixSource;
import com.epam.tcodata.mock.external.pump.factory.MockAbstractExternalFactory;
import com.epam.tcodata.mock.external.pump.source.dimension.impl.MockAssetMixSource;
import com.epam.tcodata.models.avro.dimension.AvroAsset;
import com.epam.tcodata.models.enriched.dimension.EnrichedAsset;
import com.epam.tcodata.models.mix.dimension.Asset;

public class MockExternalAssetFactory extends MockAbstractExternalFactory<Asset, EnrichedAsset, AvroAsset> {

    private static final long serialVersionUID = -5288479986931789456L;

    public MockExternalAssetFactory() {
        super(Asset.class);
    }

    @Override
    public IMixSource<Asset> createMixSource() {
        return new MockAssetMixSource();
    }

    @Override
    public IConverter<Asset, EnrichedAsset, AvroAsset> createConverter() throws Exception {
        return new AssetConverter();
    }
}
