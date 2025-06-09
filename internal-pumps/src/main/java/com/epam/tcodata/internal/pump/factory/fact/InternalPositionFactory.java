package com.epam.tcodata.internal.pump.factory.fact;

import com.epam.tcodata.eventhub.dal.EventHubInfo;
import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.eventhub.dal.impl.EventHub;
import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.internal.pump.converter.fact.PositionConverter;
import com.epam.tcodata.internal.pump.factory.AbstractInternalFactory;
import com.epam.tcodata.internal.pump.handler.IDataHandler;
import com.epam.tcodata.internal.pump.handler.fact.PositionDataHandler;
import com.epam.tcodata.models.avro.fact.AvroPosition;
import com.epam.tcodata.models.datalake.raw.fact.RawPosition;
import com.epam.tcodata.models.enriched.fact.EnrichedPosition;
import com.epam.tcodata.models.mix.fact.Position;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.domain.speedlayer.ISpeedLayerEntity;
import com.epam.tcodata.sql.dal.domain.speedlayer.SpeedLayerPosition;
import org.apache.spark.sql.SparkSession;

public class InternalPositionFactory extends AbstractInternalFactory<Position, EnrichedPosition, AvroPosition, RawPosition> {

    private static final long serialVersionUID = -3215337365092967484L;

    public InternalPositionFactory() {
        super(Position.class);
    }

    @Override
    public Class<? extends ISpeedLayerEntity> getSpeedLayerClass() {
        return SpeedLayerPosition.class;
    }

    @Override
    public IEventHub createEventHub(ISecretStorage secretStorage) {
        return new EventHub(EventHubInfo.POSITION, secretStorage);
    }

    @Override
    public IDataHandler createEventDataHandler(SparkSession sparkSession) {
        return new PositionDataHandler(this, sparkSession);
    }

    @Override
    public IEntityConverter createConverter() {
        return new PositionConverter();
    }

}
