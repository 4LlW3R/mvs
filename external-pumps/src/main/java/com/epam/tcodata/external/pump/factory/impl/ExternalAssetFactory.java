package com.epam.tcodata.external.pump.factory.impl;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.converter.impl.AssetConverter;
import com.epam.tcodata.external.pump.factory.AbstractExternalFactory;
import com.epam.tcodata.external.pump.source.IMixSource;
import com.epam.tcodata.external.pump.source.dimension.impl.AssetMixSource;
import com.epam.tcodata.models.avro.dimension.AvroAsset;
import com.epam.tcodata.models.enriched.dimension.EnrichedAsset;
import com.epam.tcodata.models.mix.dimension.Asset;

public class ExternalAssetFactory extends AbstractExternalFactory<Asset, EnrichedAsset, AvroAsset> {

    private static final long serialVersionUID = -6977675976274391286L;

    public ExternalAssetFactory() {
        super(Asset.class);
    }

    @Override
    public IMixSource<Asset> createMixSource() {
        return new AssetMixSource();
    }

    @Override
    public IConverter<Asset, EnrichedAsset, AvroAsset> createConverter() {
        return new AssetConverter();
    }
}
