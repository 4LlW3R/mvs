package com.epam.tcodata.internal.pump.factory.dimension;

import com.epam.tcodata.eventhub.dal.EventHubInfo;
import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.eventhub.dal.impl.EventHub;
import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.internal.pump.converter.dimension.OrganisationSubGroupConverter;
import com.epam.tcodata.internal.pump.factory.AbstractInternalFactory;
import com.epam.tcodata.internal.pump.handler.IDataHandler;
import com.epam.tcodata.internal.pump.handler.dimension.OrganisationSubGroupDataHandler;
import com.epam.tcodata.models.avro.dimension.AvroOrganisationSubGroup;
import com.epam.tcodata.models.datalake.raw.dimension.RawOrganisationSubGroup;
import com.epam.tcodata.models.enriched.dimension.EnrichedOrganisationSubGroup;
import com.epam.tcodata.models.mix.dimension.OrganisationSubGroup;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import org.apache.spark.sql.SparkSession;

public class InternalOrganisationSubGroupFactory extends AbstractInternalFactory<OrganisationSubGroup, EnrichedOrganisationSubGroup, AvroOrganisationSubGroup, RawOrganisationSubGroup> {

    private static final long serialVersionUID = 222075983549562689L;

    public InternalOrganisationSubGroupFactory() {
        super(OrganisationSubGroup.class);
    }

    @Override
    public IEventHub createEventHub(ISecretStorage secretStorage) {
        return new EventHub(EventHubInfo.ORGANIZATION_SUBGROUP, secretStorage);
    }

    @Override
    public IDataHandler createEventDataHandler(SparkSession sparkSession) {
        return new OrganisationSubGroupDataHandler(this, sparkSession);
    }

    @Override
    public IEntityConverter createConverter() {
        return new OrganisationSubGroupConverter();
    }

}
