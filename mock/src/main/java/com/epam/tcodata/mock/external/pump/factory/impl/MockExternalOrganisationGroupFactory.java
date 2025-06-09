package com.epam.tcodata.mock.external.pump.factory.impl;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.converter.impl.OrganisationGroupConverter;
import com.epam.tcodata.external.pump.source.IMixSource;
import com.epam.tcodata.mock.external.pump.factory.MockAbstractExternalFactory;
import com.epam.tcodata.mock.external.pump.source.dimension.impl.MockOrganisationGroupMixSource;
import com.epam.tcodata.models.avro.dimension.AvroOrganisationGroup;
import com.epam.tcodata.models.enriched.dimension.EnrichedOrganisationGroup;
import com.epam.tcodata.models.mix.dimension.OrganisationGroup;

public class MockExternalOrganisationGroupFactory extends MockAbstractExternalFactory<OrganisationGroup, EnrichedOrganisationGroup, AvroOrganisationGroup> {

    private static final long serialVersionUID = 2375671800419184169L;

    public MockExternalOrganisationGroupFactory() {
        super(OrganisationGroup.class);
    }

    @Override
    public IMixSource<OrganisationGroup> createMixSource() {
        return new MockOrganisationGroupMixSource();
    }

    @Override
    public IConverter<OrganisationGroup, EnrichedOrganisationGroup, AvroOrganisationGroup> createConverter() throws Exception {
        return new OrganisationGroupConverter();
    }
}
