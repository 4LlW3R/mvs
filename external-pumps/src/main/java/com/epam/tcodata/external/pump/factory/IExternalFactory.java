package com.epam.tcodata.external.pump.factory;

import com.epam.tcodata.eventhub.dal.EventHubInfo;
import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.converter.impl.*;
import com.epam.tcodata.external.pump.dto.DtoInputStream;
import com.epam.tcodata.external.pump.dto.maker.IDtoMaker;
import com.epam.tcodata.external.pump.exception.UnknownEntityTypeException;
import com.epam.tcodata.external.pump.service.IOffsetService;
import com.epam.tcodata.external.pump.source.IMixSource;
import com.epam.tcodata.mdm.IKeyFactory;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.enriched.IEnrichable;
import com.epam.tcodata.models.mix.Entity;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import org.apache.avro.specific.SpecificRecord;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.reflect.ClassTag;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;

public interface IExternalFactory<T extends Entity, S extends IEnrichable, U extends SpecificRecord> extends Serializable {

    /**
     * Set extra parameters, that this factory may use for its own purposes.
     *
     * @param parameters
     */
    default void setInitParameters(Map<String, String> parameters) {

    }

    ISecretStorage createSecretStorage() throws Exception;

    IDaoFactory createPumpDaoFactory(ISecretStorage secretStorage) throws Exception;

    IKeyFactory createKeyFactory() throws Exception;

    default EntityType getEntityType() {
        Class<T> clazz = getEntityClass();
        return EntityType.byEntityClass(clazz);
    }

    default EventHubInfo getEventHubInfo() {
        return EventHubInfo.getEventHubInfoByEntityType(getEntityType());
    }

    Class<T> getEntityClass();

    IMixSource<T> createMixSource();

    /**
     * Creates converter for different entity, not for factory's own entity.
     *
     * @param type - EntityType.
     * @return instance of IConverter.
     * @throws Exception in case unknown type.
     */
    static IConverter createConverter(EntityType type) throws Exception {
        switch (type) {
            case POSITION:
                return new PositionConverter();
            case EVENT:
                return new EventConverter();
            case TRIP:
                return new TripConverter();
            case SUBTRIP:
                return new SubTripConverter();
            case DRIVER:
                return new DriverConverter();
            case ASSET:
                return new AssetConverter();
            case LIBRARY_EVENT:
                return new LibraryEventConverter();
            case LOCATION:
                return new LocationConverter();
            case ORGANISATION_GROUP:
                return new OrganisationGroupConverter();
            case TACHO:
                return new TachoConverter();
            default:
                throw new UnknownEntityTypeException("For entity type: " + type);
        }
    }

    IEventHub createEventHub(ISecretStorage secretStorage);

    IConverter<T, S, U> createConverter() throws Exception;

    IDtoMaker<T> createDtoMaker(IDaoFactory daoFactory, SparkSession sparkSession);

    void setCurrentMoment(Instant instant);

    Instant getCurrentMoment();

    default IOffsetService createOffsetService(IDaoFactory daoFactory) {
        throw new UnsupportedOperationException("OffsetService");
    }

    default DtoInputStream<T> createInputStream(JavaStreamingContext jsc, ClassTag dtoClassTag, IDtoMaker<T> dtoMaker) {
        return new DtoInputStream(jsc, dtoClassTag, dtoMaker, null);
    }
}
