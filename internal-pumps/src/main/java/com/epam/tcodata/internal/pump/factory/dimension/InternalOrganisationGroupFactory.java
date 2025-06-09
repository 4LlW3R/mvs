package com.epam.tcodata.internal.pump.factory.dimension;

import com.epam.tcodata.eventhub.dal.EventHubInfo;
import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.eventhub.dal.impl.EventHub;
import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.internal.pump.converter.dimension.OrganisationGroupConverter;
import com.epam.tcodata.internal.pump.factory.AbstractInternalFactory;
import com.epam.tcodata.internal.pump.handler.IDataHandler;
import com.epam.tcodata.internal.pump.handler.dimension.OrganisationGroupDataHandler;
import com.epam.tcodata.models.avro.dimension.AvroOrganisationGroup;
import com.epam.tcodata.models.datalake.raw.dimension.RawOrganisationGroup;
import com.epam.tcodata.models.enriched.dimension.EnrichedOrganisationGroup;
import com.epam.tcodata.models.mix.dimension.OrganisationGroup;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import org.apache.spark.sql.SparkSession;

public class InternalOrganisationGroupFactory extends AbstractInternalFactory<OrganisationGroup, EnrichedOrganisationGroup, AvroOrganisationGroup, RawOrganisationGroup> {

    private static final long serialVersionUID = -784508490478896399L;

    public InternalOrganisationGroupFactory() {
        super(OrganisationGroup.class);
    }

    @Override
    public IEventHub createEventHub(ISecretStorage secretStorage) {
        return new EventHub(EventHubInfo.ORGANIZATION_GROUP, secretStorage);
    }

    @Override
    public IDataHandler createEventDataHandler(SparkSession sparkSession) {
        return new OrganisationGroupDataHandler(this, sparkSession);
    }

    @Override
    public IEntityConverter createConverter() {
        return new OrganisationGroupConverter();
    }

}
