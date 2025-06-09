package com.epam.tcodata.mock.external.pump.factory.impl.mix;

import com.epam.tcodata.external.pump.converter.IConverter;
import com.epam.tcodata.external.pump.converter.impl.OrganisationGroupConverter;
import com.epam.tcodata.external.pump.dto.maker.IDtoMaker;
import com.epam.tcodata.external.pump.dto.maker.dimension.impl.OrganisationGroupDtoMaker;
import com.epam.tcodata.external.pump.source.IMixSource;
import com.epam.tcodata.mock.external.pump.factory.MixMockAbstractExternalFactory;
import com.epam.tcodata.mock.external.pump.source.dimension.impl.mix.MixMockOrganisationGroupMixSource;
import com.epam.tcodata.models.avro.dimension.AvroOrganisationGroup;
import com.epam.tcodata.models.enriched.dimension.EnrichedOrganisationGroup;
import com.epam.tcodata.models.mix.dimension.OrganisationGroup;
import com.epam.tcodata.sql.dal.IDaoFactory;
import org.apache.spark.sql.SparkSession;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MixMockExternalOrganisationGroupFactory extends MixMockAbstractExternalFactory<OrganisationGroup, EnrichedOrganisationGroup, AvroOrganisationGroup> {

    private static final long serialVersionUID = 6482929294567581215L;

    private static final String RESOURCE_FILE_NAME = "pump.properties";
    private static final String ACCOUNT_NAME = "organisation-group-pump.account-name";
    private String accountName;

    /**
     * External Organisation group factory.
     */
    public MixMockExternalOrganisationGroupFactory() {
        super(OrganisationGroup.class);

        Properties props = new Properties();
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(RESOURCE_FILE_NAME)) {
            props.load(inputStream);
            this.accountName = props.getProperty(ACCOUNT_NAME);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    @Override
    public IMixSource<OrganisationGroup> createMixSource() {
        return new MixMockOrganisationGroupMixSource();
    }

    @Override
    public IConverter<OrganisationGroup, EnrichedOrganisationGroup, AvroOrganisationGroup> createConverter() {
        return new OrganisationGroupConverter();
    }

    @Override
    public IDtoMaker<OrganisationGroup> createDtoMaker(IDaoFactory daoFactory, SparkSession sparkSession) {
        return new OrganisationGroupDtoMaker<>(daoFactory, this.accountName);
    }
}
