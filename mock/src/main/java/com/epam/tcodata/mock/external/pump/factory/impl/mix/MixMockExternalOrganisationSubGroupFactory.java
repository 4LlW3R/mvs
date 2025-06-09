package com.epam.tcodata.mock.external.pump.factory.impl.mix;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.converter.impl.OrganisationSubGroupConverter;
import com.epam.tcodata.external.pump.dto.maker.IDtoMaker;
import com.epam.tcodata.external.pump.dto.maker.dimension.impl.OrganisationSubGroupDtoMaker;
import com.epam.tcodata.external.pump.source.IMixSource;
import com.epam.tcodata.mock.external.pump.factory.MixMockAbstractExternalFactory;
import com.epam.tcodata.mock.external.pump.source.dimension.impl.mix.MixMockOrganisationSubGroupMixSource;
import com.epam.tcodata.models.avro.dimension.AvroOrganisationSubGroup;
import com.epam.tcodata.models.enriched.dimension.EnrichedOrganisationSubGroup;
import com.epam.tcodata.models.mix.dimension.OrganisationSubGroup;
import com.epam.tcodata.sql.dal.IDaoFactory;
import org.apache.spark.sql.SparkSession;

public class MixMockExternalOrganisationSubGroupFactory extends MixMockAbstractExternalFactory<OrganisationSubGroup, EnrichedOrganisationSubGroup, AvroOrganisationSubGroup> {

    private static final long serialVersionUID = 6482929294567581215L;

    /**
     * External Organisation subGroup factory.
     */
    public MixMockExternalOrganisationSubGroupFactory() {
        super(OrganisationSubGroup.class);
    }

    @Override
    public IMixSource<OrganisationSubGroup> createMixSource() {
        return new MixMockOrganisationSubGroupMixSource();
    }

    @Override
    public IConverter<OrganisationSubGroup, EnrichedOrganisationSubGroup, AvroOrganisationSubGroup> createConverter() {
        return new OrganisationSubGroupConverter();
    }

    @Override
    public IDtoMaker<OrganisationSubGroup> createDtoMaker(IDaoFactory daoFactory, SparkSession sparkSession) {
        return new OrganisationSubGroupDtoMaker<>(daoFactory, sparkSession);
    }
}
