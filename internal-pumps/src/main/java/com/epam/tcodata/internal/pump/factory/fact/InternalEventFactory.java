package com.epam.tcodata.internal.pump.factory.fact;

import com.epam.tcodata.eventhub.dal.EventHubInfo;
import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.eventhub.dal.impl.EventHub;
import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.internal.pump.converter.fact.EventConverter;
import com.epam.tcodata.internal.pump.converter.fact.PositionConverter;
import com.epam.tcodata.internal.pump.factory.AbstractInternalFactory;
import com.epam.tcodata.internal.pump.handler.IDataHandler;
import com.epam.tcodata.internal.pump.handler.fact.EventDataHandler;
import com.epam.tcodata.models.avro.fact.AvroEvent;
import com.epam.tcodata.models.datalake.raw.fact.RawEvent;
import com.epam.tcodata.models.enriched.fact.EnrichedEvent;
import com.epam.tcodata.models.mix.fact.Event;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.domain.speedlayer.ISpeedLayerEntity;
import com.epam.tcodata.sql.dal.domain.speedlayer.SpeedLayerEvent;
import org.apache.spark.sql.SparkSession;

public class InternalEventFactory extends AbstractInternalFactory<Event, EnrichedEvent, AvroEvent, RawEvent> {

    private static final long serialVersionUID = -7171969382370496931L;

    public InternalEventFactory() {
        super(Event.class);
    }

    @Override
    public Class<? extends ISpeedLayerEntity> getSpeedLayerClass() {
        return SpeedLayerEvent.class;
    }

    @Override
    public IEventHub createEventHub(ISecretStorage secretStorage) {
        return new EventHub(EventHubInfo.EVENT, secretStorage);
    }

    @Override
    public IDataHandler createEventDataHandler(SparkSession sparkSession) {
        return new EventDataHandler(this, sparkSession);
    }

    @Override
    public IEntityConverter createConverter() {
        return new EventConverter(new PositionConverter());
    }
}
