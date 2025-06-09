package com.epam.tcodata.external.pump.source.dimension.impl;

import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.source.dimension.AbstractDimensionMixSource;
import com.epam.tcodata.models.mix.dimension.OrganisationGroup;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import java.time.Instant;

public class OrganisationGroupMixSource extends AbstractDimensionMixSource<OrganisationGroup> {

    private static final long serialVersionUID = 5978875540317920621L;

    // request properties
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String AUTHORIZATION_BEARER = "Bearer ";

    //mix integration properties
    private static final String MIX_ENDPOINT_ORGANISATION_GROUP = "/api/organisationgroups";

    /**
     * Service for requesting data from MIX REST API
     * and enriching DTO with requested data and meta information.
     */
    public OrganisationGroupMixSource() {
        super(OrganisationGroup.class);
    }

    @Override
    public void buildURI(URIBuilder uriBuilder, AbstractDto<OrganisationGroup> dto, Instant currentTime) {
        uriBuilder.setPath(MIX_ENDPOINT_ORGANISATION_GROUP);
    }

    @Override
    protected void setRequestParameters(HttpGet request, AbstractDto<OrganisationGroup> dto) {
        request.setHeader(HEADER_AUTHORIZATION, AUTHORIZATION_BEARER + dto.getAccessToken());
    }
}
