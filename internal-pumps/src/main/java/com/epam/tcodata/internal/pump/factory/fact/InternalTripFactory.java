package com.epam.tcodata.internal.pump.factory.fact;

import com.epam.tcodata.eventhub.dal.EventHubInfo;
import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.eventhub.dal.impl.EventHub;
import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.internal.pump.converter.fact.PositionConverter;
import com.epam.tcodata.internal.pump.converter.fact.SubTripConverter;
import com.epam.tcodata.internal.pump.converter.fact.TripConverter;
import com.epam.tcodata.internal.pump.factory.AbstractInternalFactory;
import com.epam.tcodata.internal.pump.handler.IDataHandler;
import com.epam.tcodata.internal.pump.handler.fact.SubTripDataHandler;
import com.epam.tcodata.internal.pump.handler.fact.TripDataHandler;
import com.epam.tcodata.models.avro.fact.AvroTrip;
import com.epam.tcodata.models.datalake.raw.fact.RawTrip;
import com.epam.tcodata.models.enriched.fact.EnrichedTrip;
import com.epam.tcodata.models.mix.fact.Trip;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import org.apache.spark.sql.SparkSession;

public class InternalTripFactory extends AbstractInternalFactory<Trip, EnrichedTrip, AvroTrip, RawTrip> {

    private static final long serialVersionUID = 3330676291694670577L;

    private InternalSubTripFactory subTripFactory;

    public InternalTripFactory() {
        super(Trip.class);
        this.subTripFactory = new InternalSubTripFactory();
    }

    @Override
    public IEventHub createEventHub(ISecretStorage secretStorage) {
        return new EventHub(EventHubInfo.TRIP, secretStorage);
    }

    @Override
    public IDataHandler createEventDataHandler(SparkSession sparkSession) {
        SubTripDataHandler subTripDataHandler = new SubTripDataHandler(this.subTripFactory, sparkSession);
        return new TripDataHandler(this, sparkSession, subTripDataHandler);
    }

    @Override
    public IEntityConverter createConverter() {
        return new TripConverter(new PositionConverter(), new SubTripConverter(new PositionConverter()));
    }

}
