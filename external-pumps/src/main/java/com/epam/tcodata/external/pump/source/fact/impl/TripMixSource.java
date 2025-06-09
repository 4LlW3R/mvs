package com.epam.tcodata.external.pump.source.fact.impl;

import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.dto.fact.FactDto;
import com.epam.tcodata.external.pump.source.fact.AbstractFactMixSource;
import com.epam.tcodata.external.pump.util.ConverterUtil;
import com.epam.tcodata.models.mix.fact.Trip;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import java.time.Instant;

public class TripMixSource extends AbstractFactMixSource<Trip> {

    private static final long serialVersionUID = 6276302247840277422L;

    //mix integration properties
    private static final String MIX_ENDPOINT_TRIPS = "/api/trips/groups/createdsince";
    private static final String ENDPOINT_ORGANISATION_PART = "/organisation/";
    private static final String ENDPOINT_SINCETOKEN_PART = "/sincetoken/";
    private static final String ENDPOINT_QUANTITY_PART = "/quantity/";

    // constants for building request URL
    private static final String QUANTITY = "1000";

    // include subTrips
    private static final String INCLUDE_SUBTRIPS_PARAMETER = "includeSubTrips";
    private static final String INCLUDE_SUBTRIPS_VALUE = "true";

    // request properties
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String AUTHORIZATION_BEARER = "Bearer ";

    public TripMixSource() {
        super(Trip.class);
    }

    @Override
    public void buildURI(URIBuilder uriBuilder, AbstractDto<Trip> dto, Instant currentTime) {
        fixOverdueDto(((FactDto) dto).getOrgGroupId(), (FactDto) dto, currentTime);
        String sinceToken = ConverterUtil.instantToString(((FactDto) dto).getSinceToken(), timePattern);
        String path = buildPath(sinceToken, (FactDto) dto);

        uriBuilder.setPath(path)
                .setParameter(INCLUDE_SUBTRIPS_PARAMETER, INCLUDE_SUBTRIPS_VALUE);
    }

    private static String buildPath(String sinceToken, FactDto<Trip> dto) {
        return MIX_ENDPOINT_TRIPS
                + ENDPOINT_ORGANISATION_PART
                + dto.getOrgGroupId()
                + ENDPOINT_SINCETOKEN_PART
                + sinceToken
                + ENDPOINT_QUANTITY_PART
                + QUANTITY;
    }

    @Override
    protected void setRequestParameters(HttpGet get, AbstractDto<Trip> dto) {
        get.setHeader(HEADER_CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON);
        get.setHeader(HEADER_AUTHORIZATION, AUTHORIZATION_BEARER + dto.getAccessToken());
    }
}
