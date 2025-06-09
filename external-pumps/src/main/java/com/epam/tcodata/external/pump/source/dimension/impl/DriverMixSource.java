package com.epam.tcodata.external.pump.source.dimension.impl;

import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.dto.dimension.DimensionDto;
import com.epam.tcodata.external.pump.source.dimension.AbstractDimensionMixSource;
import com.epam.tcodata.models.mix.dimension.Driver;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import java.time.Instant;

public class DriverMixSource extends AbstractDimensionMixSource<Driver> {

    // request properties
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String AUTHORIZATION_BEARER = "Bearer ";

    //mix integration properties
    private static final String MIX_ENDPOINT_DRIVER = "/api/drivers/organisation/";

    /**
     * Service for requesting data from MIX REST API
     * and enriching DTO with requested data and meta information.
     */
    public DriverMixSource() {
        super(Driver.class);
    }

    @Override
    public void buildURI(URIBuilder uriBuilder, AbstractDto<Driver> dto, Instant currentTime) {
        String path = buildPath(((DimensionDto) dto).getOrgGroupId());
        uriBuilder.setPath(path);
    }


    private String buildPath(long orgGroupId) {
        return MIX_ENDPOINT_DRIVER + orgGroupId;
    }

    @Override
    protected void setRequestParameters(HttpGet request, AbstractDto<Driver> dto) {
        request.setHeader(HEADER_AUTHORIZATION, AUTHORIZATION_BEARER + dto.getAccessToken());
    }
}
