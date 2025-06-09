package com.epam.tcodata.internal.pump.factory.dimension;

import com.epam.tcodata.eventhub.dal.EventHubInfo;
import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.eventhub.dal.impl.EventHub;
import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.internal.pump.converter.dimension.AssetConverter;
import com.epam.tcodata.internal.pump.factory.AbstractInternalFactory;
import com.epam.tcodata.internal.pump.handler.IDataHandler;
import com.epam.tcodata.internal.pump.handler.dimension.AssetDataHandler;
import com.epam.tcodata.models.avro.dimension.AvroAsset;
import com.epam.tcodata.models.datalake.raw.dimension.RawAsset;
import com.epam.tcodata.models.enriched.dimension.EnrichedAsset;
import com.epam.tcodata.models.mix.dimension.Asset;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import org.apache.spark.sql.SparkSession;

public class InternalAssetFactory extends AbstractInternalFactory<Asset, EnrichedAsset, AvroAsset, RawAsset> {

    private static final long serialVersionUID = -4844901964695849208L;

    public InternalAssetFactory() {
        super(Asset.class);
    }

    @Override
    public IEventHub createEventHub(ISecretStorage secretStorage) {
        return new EventHub(EventHubInfo.ASSET, secretStorage);
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
