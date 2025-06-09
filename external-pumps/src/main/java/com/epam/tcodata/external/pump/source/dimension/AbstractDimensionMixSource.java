package com.epam.tcodata.external.pump.source.dimension;

import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.source.AbstractMixSource;
import com.epam.tcodata.models.mix.Entity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;
import java.util.Set;

@SuppressWarnings("CPD-START")
public abstract class AbstractDimensionMixSource<T extends Entity> extends AbstractMixSource<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDimensionMixSource.class);

    private static final long serialVersionUID = 2764793261127704174L;

    /**
     * Service for requesting data from MIX REST API
     * and enriching DTO with requested data and meta information.
     *
     * @param entityClass
     */
    protected AbstractDimensionMixSource(Class<T> entityClass) {
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
    protected void fillDtoWithInfoOnSuccess(AbstractDto<T> dto, List<T> entityList, CloseableHttpResponse response) {
        long currentElementCount = entityList.size();
        long totalElementCount = dto.getTotalElementsCount();

        dto.setEntityList(entityList);
        dto.setLastSyncElementCount(currentElementCount);
        dto.setTotalElementsCount(totalElementCount + currentElementCount);

        logResponseInfo(dto, response);
    }

    @Override
    protected void fillDtoWithInfoOnError(AbstractDto<T> dto, List<T> entityList, CloseableHttpResponse response) {
        long currentElementCount = entityList.size();
        long totalElementCount = dto.getTotalElementsCount();

        dto.setEntityList(entityList);
        dto.setLastSyncElementCount(currentElementCount);
        dto.setTotalElementsCount(totalElementCount + currentElementCount);
        dto.setLastErrorMessage(response.getStatusLine().getReasonPhrase());

        logResponseInfo(dto, response);
    }

    protected void logResponseInfo(AbstractDto<T> dto, CloseableHttpResponse response) {
        Set<T> duplicates = findDuplicates(dto.getEntityList());
//        LOGGER.warn("#mix-response# Mix response.");
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
}

