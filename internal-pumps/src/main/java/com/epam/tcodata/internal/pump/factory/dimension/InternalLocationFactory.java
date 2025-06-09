package com.epam.tcodata.internal.pump.factory.dimension;

import com.epam.tcodata.eventhub.dal.EventHubInfo;
import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.eventhub.dal.impl.EventHub;
import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.internal.pump.converter.dimension.LocationConverter;
import com.epam.tcodata.internal.pump.factory.AbstractInternalFactory;
import com.epam.tcodata.internal.pump.handler.IDataHandler;
import com.epam.tcodata.internal.pump.handler.dimension.LocationDataHandler;
import com.epam.tcodata.models.avro.dimension.AvroLocation;
import com.epam.tcodata.models.datalake.raw.dimension.RawLocation;
import com.epam.tcodata.models.enriched.dimension.EnrichedLocation;
import com.epam.tcodata.models.mix.dimension.Location;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import org.apache.spark.sql.SparkSession;

public class InternalLocationFactory extends AbstractInternalFactory<Location, EnrichedLocation, AvroLocation, RawLocation> {

    private static final long serialVersionUID = 194207224023300569L;

    public InternalLocationFactory() {
        super(Location.class);
    }

    @Override
    public IEventHub createEventHub(ISecretStorage secretStorage) {
        return new EventHub(EventHubInfo.LOCATION, secretStorage);
    }

    @Override
    public IDataHandler createEventDataHandler(SparkSession sparkSession) {
        return new LocationDataHandler(this, sparkSession);
    }

    @Override
    public IEntityConverter createConverter() {
        return new LocationConverter();
    }

}
