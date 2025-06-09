package com.epam.tcodata.mock.internal.pumps.factory.dimension;

import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.internal.pump.converter.dimension.OrganisationGroupConverter;
import com.epam.tcodata.internal.pump.handler.IDataHandler;
import com.epam.tcodata.internal.pump.handler.dimension.OrganisationGroupDataHandler;
import com.epam.tcodata.mock.internal.pumps.factory.MockAbstractInternalFactory;
import com.epam.tcodata.models.avro.dimension.AvroOrganisationGroup;
import com.epam.tcodata.models.datalake.raw.dimension.RawOrganisationGroup;
import com.epam.tcodata.models.enriched.dimension.EnrichedOrganisationGroup;
import com.epam.tcodata.models.mix.dimension.OrganisationGroup;
import org.apache.spark.sql.SparkSession;

public class MockInternalOrganisationGroupFactory extends MockAbstractInternalFactory<OrganisationGroup, EnrichedOrganisationGroup, AvroOrganisationGroup, RawOrganisationGroup> {

    private static final long serialVersionUID = 5367850068848992272L;

    public MockInternalOrganisationGroupFactory() {
        super(OrganisationGroup.class);
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
