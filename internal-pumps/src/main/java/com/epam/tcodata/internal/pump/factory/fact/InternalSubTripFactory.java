package com.epam.tcodata.internal.pump.factory.fact;

import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.internal.pump.converter.fact.PositionConverter;
import com.epam.tcodata.internal.pump.converter.fact.SubTripConverter;
import com.epam.tcodata.internal.pump.factory.AbstractInternalFactory;
import com.epam.tcodata.internal.pump.handler.IDataHandler;
import com.epam.tcodata.internal.pump.handler.fact.SubTripDataHandler;
import com.epam.tcodata.models.avro.fact.AvroSubTrip;
import com.epam.tcodata.models.datalake.raw.fact.RawSubTrip;
import com.epam.tcodata.models.enriched.fact.EnrichedSubTrip;
import com.epam.tcodata.models.mix.fact.SubTrip;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import org.apache.spark.sql.SparkSession;

public class InternalSubTripFactory extends AbstractInternalFactory<SubTrip, EnrichedSubTrip, AvroSubTrip, RawSubTrip> {

    private static final long serialVersionUID = 2252830750425123959L;

    public InternalSubTripFactory() {
        super(SubTrip.class);
    }

    @Override
    public IEventHub createEventHub(ISecretStorage secretStorage) {
        throw new UnsupportedOperationException("SubTrips are written to Trip EventHub");
    }

    @Override
    public IDataHandler createEventDataHandler(SparkSession sparkSession) {
        return new SubTripDataHandler(this, sparkSession);
    }

    @Override
    public IEntityConverter createConverter() {
        return new SubTripConverter(new PositionConverter());
    }
}
