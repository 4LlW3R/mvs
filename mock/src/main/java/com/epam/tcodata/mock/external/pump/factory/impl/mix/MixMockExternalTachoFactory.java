package com.epam.tcodata.mock.external.pump.factory.impl.mix;

import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.eventhub.dal.impl.EventHub;
import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.converter.impl.TachoConverter;
import com.epam.tcodata.external.pump.dto.DtoInputStream;
import com.epam.tcodata.external.pump.dto.maker.IDtoMaker;
import com.epam.tcodata.external.pump.dto.maker.fact.impl.TachoDtoMaker;
import com.epam.tcodata.external.pump.service.IOffsetService;
import com.epam.tcodata.external.pump.service.impl.TachoOffsetService;
import com.epam.tcodata.external.pump.source.IMixSource;
import com.epam.tcodata.mock.external.pump.factory.MixMockAbstractExternalFactory;
import com.epam.tcodata.mock.external.pump.source.fact.impl.mix.MixMockTachoMixSource;
import com.epam.tcodata.models.avro.fact.AvroTacho;
import com.epam.tcodata.models.enriched.fact.EnrichedTacho;
import com.epam.tcodata.models.mix.fact.Tacho;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.reflect.ClassTag;

public class MixMockExternalTachoFactory extends MixMockAbstractExternalFactory<Tacho, EnrichedTacho, AvroTacho> {

    private static final long serialVersionUID = 4036337000980566094L;

    /**
     * External Tacho factory.
     */
    public MixMockExternalTachoFactory() {
        super(Tacho.class);
    }

    @Override
    public IMixSource<Tacho> createMixSource() {
        return new MixMockTachoMixSource();
    }

    @Override
    public IConverter<Tacho, EnrichedTacho, AvroTacho> createConverter() {
        return new TachoConverter();
    }

    @Override
    public IOffsetService createOffsetService(IDaoFactory daoFactory) {
        return new TachoOffsetService(this, daoFactory);
    }

    @Override
    public IDtoMaker<Tacho> createDtoMaker(IDaoFactory daoFactory, SparkSession sparkSession) {
        return new TachoDtoMaker<>(daoFactory, this, sparkSession);
    }

    @Override
    public DtoInputStream<Tacho> createInputStream(JavaStreamingContext jsc, ClassTag dtoClassTag, IDtoMaker<Tacho> dtoMaker) {
        return new DtoInputStream<>(jsc, dtoClassTag, dtoMaker, 1);
    }

    @Override
    public IEventHub createEventHub(ISecretStorage secretStorage) {
        return new EventHub(getEventHubInfo(), secretStorage, 100);
    }
}
