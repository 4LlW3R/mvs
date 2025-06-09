package com.epam.tcodata.external.pump.source.dimension.impl;

import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.dto.dimension.DimensionDto;
import com.epam.tcodata.external.pump.source.dimension.AbstractDimensionMixSource;
import com.epam.tcodata.models.mix.dimension.OrganisationSubGroup;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import java.time.Instant;
import java.util.List;

public class OrganisationSubGroupMixSource extends AbstractDimensionMixSource<OrganisationSubGroup> {

    private static final long serialVersionUID = -7733635914725169184L;

    // request properties
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String AUTHORIZATION_BEARER = "Bearer ";

    //mix integration properties
    private static final String MIX_ENDPOINT_ORGANISATION_SUBGROUP = "/api/organisationgroups/subgroups/";

    /**
     * Service for requesting data from MIX REST API
     * and enriching DTO with requested data and meta information.
     */
    public OrganisationSubGroupMixSource() {
        super(OrganisationSubGroup.class);
        super.javaType = super.objectMapper.getTypeFactory().constructType(OrganisationSubGroup.class);
    }

    @Override
    public void buildURI(URIBuilder uriBuilder, AbstractDto<OrganisationSubGroup> dto, Instant currentTime) {
        String path = buildPath(((DimensionDto) dto).getOrgGroupId());
        uriBuilder.setPath(path);
    }

    @Override
    protected void setRequestParameters(HttpGet request, AbstractDto<OrganisationSubGroup> dto) {
        request.setHeader(HEADER_AUTHORIZATION, AUTHORIZATION_BEARER + dto.getAccessToken());
    }

    private String buildPath(long orgGroupId) {
        return MIX_ENDPOINT_ORGANISATION_SUBGROUP + orgGroupId;
    }

    @Override
    protected void fillDtoWithInfoOnSuccess(AbstractDto<OrganisationSubGroup> dto, List<OrganisationSubGroup> entityList, CloseableHttpResponse response) {
        long currentElementCount = entityList.size();
        long totalElementCount = dto.getTotalElementsCount();

        entityList.forEach(entity -> entity.setParentOrgId(((DimensionDto) dto).getOrgGroupId()));
        dto.setEntityList(entityList);

        dto.setLastSyncElementCount(currentElementCount);
        dto.setTotalElementsCount(totalElementCount + currentElementCount);

        super.logResponseInfo(dto, response);
    }

}
