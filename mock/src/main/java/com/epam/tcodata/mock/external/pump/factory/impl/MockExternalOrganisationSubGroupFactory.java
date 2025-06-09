package com.epam.tcodata.mock.external.pump.factory.impl;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.converter.impl.OrganisationSubGroupConverter;
import com.epam.tcodata.external.pump.source.IMixSource;
import com.epam.tcodata.mock.external.pump.factory.MockAbstractExternalFactory;
import com.epam.tcodata.mock.external.pump.source.dimension.impl.MockOrganisationSubGroupMixSource;
import com.epam.tcodata.models.avro.dimension.AvroOrganisationSubGroup;
import com.epam.tcodata.models.enriched.dimension.EnrichedOrganisationSubGroup;
import com.epam.tcodata.models.mix.dimension.OrganisationSubGroup;

public class MockExternalOrganisationSubGroupFactory extends MockAbstractExternalFactory<OrganisationSubGroup, EnrichedOrganisationSubGroup, AvroOrganisationSubGroup> {

    private static final long serialVersionUID = 5088083515192689981L;

    public MockExternalOrganisationSubGroupFactory() {
        super(OrganisationSubGroup.class);
    }

    @Override
    public IMixSource<OrganisationSubGroup> createMixSource() {
        return new MockOrganisationSubGroupMixSource();
    }

    @Override
    public IConverter<OrganisationSubGroup, EnrichedOrganisationSubGroup, AvroOrganisationSubGroup> createConverter() throws Exception {
        return new OrganisationSubGroupConverter();
    }
}
