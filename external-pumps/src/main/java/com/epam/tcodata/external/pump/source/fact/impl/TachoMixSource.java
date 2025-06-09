package com.epam.tcodata.external.pump.source.fact.impl;

import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.dto.fact.impl.TachoDto;
import com.epam.tcodata.external.pump.source.fact.AbstractFactMixSource;
import com.epam.tcodata.external.pump.util.ConverterUtil;
import com.epam.tcodata.models.mix.fact.Tacho;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import java.time.Instant;
import java.util.List;

public class TachoMixSource extends AbstractFactMixSource<Tacho> {

    private static final long serialVersionUID = -1725772499524903663L;

    //mix integration properties
    private static final String MIX_ENDPOINT_TACHO = "/api/tachos/asset/";
    private static final String ENDPOINT_FROM_PART = "/range/from/";
    private static final String ENDPOINT_TO_PART = "/to/";

    // request properties
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String AUTHORIZATION_BEARER = "Bearer ";

    public TachoMixSource() {
        super(Tacho.class);
        super.timePattern = "yyyyMMddHHmmss";
    }

    @Override
    protected void buildURI(URIBuilder uriBuilder, AbstractDto<Tacho> dto, Instant currentTime) {
        Instant from = ((TachoDto) dto).getFrom().compareTo(Instant.now().minusSeconds(3600)) > 0
                ? ((TachoDto) dto).getFrom().plusSeconds(1)
                : Instant.now().minusSeconds(3600);
        Instant to = from.plusSeconds(3600);

        String path = buildPath(((TachoDto) dto).getAssetId(), ConverterUtil.instantToString(from, timePattern), ConverterUtil.instantToString(to, timePattern));

        uriBuilder.setPath(path);
    }

    private static String buildPath(Long assetId, String from, String to) {
        return MIX_ENDPOINT_TACHO
                + assetId
                + ENDPOINT_FROM_PART
                + from
                + ENDPOINT_TO_PART
                + to;
    }

    @Override
    protected void setRequestParameters(HttpGet get, AbstractDto<Tacho> dto) {
        get.setHeader(HEADER_CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON);
        get.setHeader(HEADER_AUTHORIZATION, AUTHORIZATION_BEARER + dto.getAccessToken());
    }

    @Override
    protected void fillDtoWithInfoOnSuccess(AbstractDto<Tacho> dto, List<Tacho> entityList, CloseableHttpResponse response) {
        long currentElementCount = entityList.size();
        long totalElementCount = dto.getTotalElementsCount();

        dto.setEntityList(entityList);
        dto.setLastSyncElementCount(currentElementCount);
        dto.setTotalElementsCount(totalElementCount + currentElementCount);

        super.logResponseInfo(dto, response);
    }
}
