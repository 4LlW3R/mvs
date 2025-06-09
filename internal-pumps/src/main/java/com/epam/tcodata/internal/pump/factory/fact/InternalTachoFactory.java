package com.epam.tcodata.internal.pump.factory.fact;

import com.epam.tcodata.eventhub.dal.EventHubInfo;
import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.eventhub.dal.impl.EventHub;
import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.internal.pump.converter.fact.TachoConverter;
import com.epam.tcodata.internal.pump.factory.AbstractInternalFactory;
import com.epam.tcodata.internal.pump.handler.IDataHandler;
import com.epam.tcodata.internal.pump.handler.fact.TachoDataHandler;
import com.epam.tcodata.models.avro.fact.AvroTacho;
import com.epam.tcodata.models.datalake.raw.fact.RawTacho;
import com.epam.tcodata.models.enriched.fact.EnrichedTacho;
import com.epam.tcodata.models.mix.fact.Tacho;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import org.apache.spark.sql.SparkSession;

public class InternalTachoFactory extends AbstractInternalFactory<Tacho, EnrichedTacho, AvroTacho, RawTacho> {

    private static final long serialVersionUID = 2776305482330375984L;

    public InternalTachoFactory() {
        super(Tacho.class);
    }

    @Override
    public IEventHub createEventHub(ISecretStorage secretStorage) {
        return new EventHub(EventHubInfo.TACHO, secretStorage);
    }

    @Override
    public IDataHandler createEventDataHandler(SparkSession sparkSession) {
        return new TachoDataHandler(this, sparkSession);
    }

    @Override
    public IEntityConverter createConverter() {
        return new TachoConverter();
    }

}
