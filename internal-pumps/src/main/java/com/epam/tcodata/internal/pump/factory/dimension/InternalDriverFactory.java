package com.epam.tcodata.internal.pump.factory.dimension;

import com.epam.tcodata.eventhub.dal.EventHubInfo;
import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.eventhub.dal.impl.EventHub;
import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.internal.pump.converter.dimension.DriverConverter;
import com.epam.tcodata.internal.pump.factory.AbstractInternalFactory;
import com.epam.tcodata.internal.pump.handler.IDataHandler;
import com.epam.tcodata.internal.pump.handler.dimension.DriverDataHandler;
import com.epam.tcodata.models.avro.dimension.AvroDriver;
import com.epam.tcodata.models.datalake.raw.dimension.RawDriver;
import com.epam.tcodata.models.enriched.dimension.EnrichedDriver;
import com.epam.tcodata.models.mix.dimension.Driver;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import org.apache.spark.sql.SparkSession;

public class InternalDriverFactory extends AbstractInternalFactory<Driver, EnrichedDriver, AvroDriver, RawDriver> {

    private static final long serialVersionUID = -5802173281300103306L;

    /**
     * Increased max batch size for Drivers. Default value (1000) is too small.
     */
    private static final int MAX_BATCH_SIZE = 5000;

    public InternalDriverFactory() {
        super(Driver.class);
    }

    @Override
    public IEventHub createEventHub(ISecretStorage secretStorage) {
        return new EventHub(EventHubInfo.DRIVER, secretStorage, MAX_BATCH_SIZE);
    }

    @Override
    public IDataHandler createEventDataHandler(SparkSession sparkSession) {
        return new DriverDataHandler(this, sparkSession);
    }

    @Override
    public IEntityConverter createConverter() {
        return new DriverConverter();
    }
}
