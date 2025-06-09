package com.epam.tcodata.mock.internal.pumps.factory.dimension;

import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.internal.pump.converter.dimension.OrganisationSubGroupConverter;
import com.epam.tcodata.internal.pump.handler.IDataHandler;
import com.epam.tcodata.internal.pump.handler.dimension.OrganisationSubGroupDataHandler;
import com.epam.tcodata.mock.internal.pumps.factory.MockAbstractInternalFactory;
import com.epam.tcodata.models.avro.dimension.AvroOrganisationSubGroup;
import com.epam.tcodata.models.datalake.raw.dimension.RawOrganisationSubGroup;
import com.epam.tcodata.models.enriched.dimension.EnrichedOrganisationSubGroup;
import com.epam.tcodata.models.mix.dimension.OrganisationSubGroup;
import org.apache.spark.sql.SparkSession;

public class MockInternalOrganisationSubGroupFactory extends MockAbstractInternalFactory<OrganisationSubGroup, EnrichedOrganisationSubGroup, AvroOrganisationSubGroup, RawOrganisationSubGroup> {

    private static final long serialVersionUID = 5367850068848992272L;

    public MockInternalOrganisationSubGroupFactory() {
        super(OrganisationSubGroup.class);
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
