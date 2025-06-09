package com.epam.tcodata.external.pump.source.fact;

import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.dto.fact.FactDto;
import com.epam.tcodata.external.pump.source.AbstractMixSource;
import com.epam.tcodata.external.pump.util.ConverterUtil;
import com.epam.tcodata.models.mix.Entity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@SuppressWarnings("CPD-START")
public abstract class AbstractFactMixSource<T extends Entity> extends AbstractMixSource<T> {

    private static final long serialVersionUID = -4540836448953974231L;

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFactMixSource.class);

    private static final String GET_SINCE_TOKEN_HEADER = "GetSinceToken";
    private static final Long SECONDS_IN_WEEK = 604800L;
    private static final Long SECONDS_IN_HALF_AN_HOUR = 1800L; //added to SECONDS_IN_WEEK to avoid corner cases
    private static final Long SECONDS_IN_FIVE_MIN = 300L; // added to SECONDS_IN_WEEK to decrease tolerated time-gap for sinceToken

    protected String timePattern = "yyyyMMddHHmmssnnn";

    /**
     * Service for requesting data from MIX REST API
     * and enriching DTO with requested data and meta information.
     */
    protected AbstractFactMixSource(Class<T> entityClass) {
        super(entityClass);
    }

    @Override
    protected HttpRequestBase createRequest(URI endPoint, AbstractDto<T> dto) {
        HttpGet request = new HttpGet(endPoint);
        setRequestParameters(request, dto);
        request.setConfig(createRequestConfig());
        return request;
    }

    @Override
    protected void fillDtoWithInfoOnSuccess(AbstractDto<T> dto,
                                            List<T> entityList,
                                            CloseableHttpResponse response) {
        long currentElementCount = entityList.size();
        long totalElementCount = dto.getTotalElementsCount();

        Instant nextSinceToken = ConverterUtil.stringToInstant(response.getFirstHeader(GET_SINCE_TOKEN_HEADER).getValue(), timePattern);
        ((FactDto) dto).setNextSinceToken(nextSinceToken);
        dto.setEntityList(entityList);
        dto.setLastSyncElementCount(currentElementCount);
        dto.setTotalElementsCount(totalElementCount + currentElementCount);

        logResponseInfo(dto, response);
    }

    @Override
    protected void fillDtoWithInfoOnError(AbstractDto<T> dto, List<T> entityList, CloseableHttpResponse response) {
        long currentElementCount = entityList.size();
        long totalElementCount = dto.getTotalElementsCount();

        ((FactDto) dto).setNextSinceToken(((FactDto) dto).getSinceToken());
        dto.setEntityList(entityList);
        dto.setLastSyncElementCount(currentElementCount);
        dto.setTotalElementsCount(totalElementCount + currentElementCount);
        dto.setLastErrorMessage(response.getStatusLine().getReasonPhrase());

        logResponseInfo(dto, response);
    }

    protected void logResponseInfo(AbstractDto<T> dto, CloseableHttpResponse response) {
        Set<T> duplicates = findDuplicates(dto.getEntityList());
        LOGGER.info("#mix-response# Mix response. \n Duplicates count: {}.", duplicates.size());

        LOGGER.debug("#mix-response# Mix response. \n Status line: {}. \n Status code: {}. \n Sync duration: {}."
                        + "\n Last sync element count: {}. \n Total elements count: {}. \n Duplicates count: {}."
                        + "\n Duplicates: {}. \n Additional info: {}.",
                response.getStatusLine(),
                response.getStatusLine().getStatusCode(),
                dto.getLastSyncDuration(),
                dto.getLastSyncElementCount(),
                dto.getTotalElementsCount(),
                duplicates.size(),
                duplicates,
                dto.additionalInfo()
        );
    }

    protected abstract void setRequestParameters(HttpGet request, AbstractDto<T> dto);

    /**
     * Fix overdue sinceToken (change sinceToken if it is older than 7 days according to MIX restrictions).
     *
     * @param orgId organisation id
     * @param dto   dto.
     */
    protected static void fixOverdueDto(long orgId, FactDto dto, Instant currentTime) {
        Instant oldSinceToken = dto.getSinceToken();
        if (oldSinceToken == null) {
            throw new IllegalArgumentException("SinceToken is null for " + orgId);
        }
        if (oldSinceToken.compareTo(currentTime.minusSeconds(SECONDS_IN_WEEK).plusSeconds(SECONDS_IN_FIVE_MIN)) < 0) {
            Instant newSinceToken = currentTime.minusSeconds(SECONDS_IN_WEEK).plusSeconds(SECONDS_IN_HALF_AN_HOUR);
            LOGGER.warn("SinceToken for organisation id is overdue. SinceToken changed");
//            LOGGER.debug("SinceToken for organisation id {} is overdue. SinceToken changed from {} to {}",
//                    orgId,
//                    oldSinceToken,
//                    newSinceToken);
            dto.setSinceToken(newSinceToken);
        }
    }
}
