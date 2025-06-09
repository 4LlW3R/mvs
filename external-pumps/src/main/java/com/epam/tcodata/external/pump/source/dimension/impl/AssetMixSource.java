package com.epam.tcodata.external.pump.source.dimension.impl;

import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.dto.dimension.DimensionDto;
import com.epam.tcodata.external.pump.source.dimension.AbstractDimensionMixSource;
import com.epam.tcodata.models.mix.dimension.Asset;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import java.time.Instant;

public class AssetMixSource extends AbstractDimensionMixSource<Asset> {

    // request properties
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String AUTHORIZATION_BEARER = "Bearer ";

    //mix integration properties
    private static final String MIX_ENDPOINT_ASSET = "/api/assets/group/";

    /**
     * Service for requesting data from MIX REST API
     * and enriching DTO with requested data and meta information.
     */
    public AssetMixSource() {
        super(Asset.class);
    }

    @Override
    protected void setRequestParameters(HttpGet request, AbstractDto<Asset> dto) {
        request.setHeader(HEADER_AUTHORIZATION, AUTHORIZATION_BEARER + dto.getAccessToken());
    }

    @Override
    protected void buildURI(URIBuilder uriBuilder, AbstractDto<Asset> dto, Instant currentTime) {
        String path = buildPath(((DimensionDto) dto).getOrgGroupId());
        uriBuilder.setPath(path);
    }

    private String buildPath(long orgGroupId) {
        return MIX_ENDPOINT_ASSET + orgGroupId;
    }
}
