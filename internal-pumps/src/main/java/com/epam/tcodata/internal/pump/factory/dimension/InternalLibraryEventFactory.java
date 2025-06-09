package com.epam.tcodata.internal.pump.factory.dimension;

import com.epam.tcodata.eventhub.dal.EventHubInfo;
import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.eventhub.dal.impl.EventHub;
import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.internal.pump.converter.dimension.LibraryEventConverter;
import com.epam.tcodata.internal.pump.factory.AbstractInternalFactory;
import com.epam.tcodata.internal.pump.handler.IDataHandler;
import com.epam.tcodata.internal.pump.handler.dimension.LibraryEventDataHandler;
import com.epam.tcodata.models.avro.dimension.AvroLibraryEvent;
import com.epam.tcodata.models.datalake.raw.dimension.RawLibraryEvent;
import com.epam.tcodata.models.enriched.dimension.EnrichedLibraryEvent;
import com.epam.tcodata.models.mix.dimension.LibraryEvent;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import org.apache.spark.sql.SparkSession;

public class InternalLibraryEventFactory extends AbstractInternalFactory<LibraryEvent, EnrichedLibraryEvent, AvroLibraryEvent, RawLibraryEvent> {

    private static final long serialVersionUID = -6021170029922995665L;

    public InternalLibraryEventFactory() {
        super(LibraryEvent.class);
    }

    @Override
    public IEventHub createEventHub(ISecretStorage secretStorage) {
        return new EventHub(EventHubInfo.LIBRARY_EVENT, secretStorage);
    }

    @Override
    public IDataHandler createEventDataHandler(SparkSession sparkSession) {
        return new LibraryEventDataHandler(this, sparkSession);
    }

    @Override
    public IEntityConverter createConverter() {
        return new LibraryEventConverter();
    }

}
