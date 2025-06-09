package com.epam.tcodata.external.pump.source.dimension.impl;

import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.dto.dimension.DimensionDto;
import com.epam.tcodata.external.pump.source.dimension.AbstractDimensionMixSource;
import com.epam.tcodata.models.mix.dimension.LibraryEvent;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import java.time.Instant;

public class LibraryEventMixSource extends AbstractDimensionMixSource<LibraryEvent> {

    private static final long serialVersionUID = 5978875540317920621L;

    // request properties
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String AUTHORIZATION_BEARER = "Bearer ";

    //mix integration properties
    private static final String MIX_ENDPOINT_LIBRARY_EVENT = "/api/libraryevents/organisation/";

    /**
     * Service for requesting data from MIX REST API
     * and enriching DTO with requested data and meta information.
     */
    public LibraryEventMixSource() {
        super(LibraryEvent.class);
    }

    @Override
    public void buildURI(URIBuilder uriBuilder, AbstractDto<LibraryEvent> dto, Instant currentTime) {
        String path = buildPath(((DimensionDto) dto).getOrgGroupId());
        uriBuilder.setPath(path);
    }


    private String buildPath(long orgGroupId) {
        return MIX_ENDPOINT_LIBRARY_EVENT + orgGroupId;
    }

    @Override
    protected void setRequestParameters(HttpGet request, AbstractDto<LibraryEvent> dto) {
        request.setHeader(HEADER_AUTHORIZATION, AUTHORIZATION_BEARER + dto.getAccessToken());
    }
}
