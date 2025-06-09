package com.epam.tcodata.external.pump.source;

import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.dto.fact.FactDto;
import com.epam.tcodata.models.mix.Entity;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public abstract class AbstractMixSource<T extends Entity> implements IMixSource<T> {
    private static final long serialVersionUID = -224537009171104411L;
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMixSource.class);
    //mix integration properties
    private static final String MIX_ENDPOINT_SCHEME = "https";
    private static final String MIX_ENDPOINT_HOST = "integrate.uk.mixtelematics.com";
    private static final String RESOURCE_FILE_NAME = "timeouts.properties";
    // timeouts
    private static final String CONNECT_TIMEOUT = "connect-timeout";
    private static final String CONNECTION_REQUEST_TIMEOUT = "connection-request-timeout";
    private static final String SOCKET_TIMEOUT = "socket-timeout";
    private int connectTimeout;
    private int connectionRequestTimeout;
    private int socketTimeout;

    // jackson
    protected ObjectMapper objectMapper;
    protected JavaType javaType;

    /**
     * Service for requesting data from MIX REST API and enriching DTO with requested data and meta information.
     */
    protected AbstractMixSource(Class<T> entityClass) {
        Properties props = new Properties();
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(RESOURCE_FILE_NAME)) {
            props.load(inputStream);
            this.connectTimeout = Integer.parseInt(props.getProperty(CONNECT_TIMEOUT));
            this.connectionRequestTimeout = Integer.parseInt(props.getProperty(CONNECTION_REQUEST_TIMEOUT));
            this.socketTimeout = Integer.parseInt(props.getProperty(SOCKET_TIMEOUT));
            LOGGER.info("#timeouts# entity {}, connectTimeout {}, connectionRequestTimeout {}, socketTimeout {}",
                    entityClass.getSimpleName(), connectTimeout, connectionRequestTimeout, socketTimeout);

            objectMapper = new ObjectMapper();
            objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                    .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));
            javaType = objectMapper.getTypeFactory()
                    .constructParametricType(
                            ArrayList.class,
                            entityClass);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    /**
     * Creates rest request and gets data from received response.
     *
     * @param dtoList object that stores info to make request.
     * @return
     */
    @Override
    public void requestDataAndFillDto(List<AbstractDto<T>> dtoList, String host, Instant currentTime) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            List<HttpRequestBase> requests = dtoList.stream()
                    .map(dto -> createRequest(buildEndpoint(dto, host, currentTime), dto))
                    .collect(Collectors.toList());

            List<CompletableFuture<Void>> futures = new ArrayList<>();
            ExecutorService executorService = Executors.newCachedThreadPool();
            for (int i = 0; i < requests.size(); i++) {
                futures.add(getToExecuteAsync(httpClient, requests.get(i), dtoList.get(i), executorService));
            }

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).get();

        } catch (IOException e) {
            LOGGER.error("#create-httpClient-error# Error while creating httpClient. \n Message: {}.",
                    e.getMessage());
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage());
// NOSONAR            throw new RuntimeException(e);
        }
    }

    private CompletableFuture<Void> getToExecuteAsync(CloseableHttpClient httpClient,
                                                      HttpRequestBase request,
                                                      AbstractDto<T> dto,
                                                      Executor executor) {

        return CompletableFuture.runAsync(() -> {
            try {
                executeRequest(httpClient, request, dto);
            } catch (IOException e) {
                LOGGER.error("IOException : {}", e.getMessage());
            }
        }, executor);
    }

    protected void executeRequest(CloseableHttpClient httpClient, HttpRequestBase request, AbstractDto<T> dto) throws IOException {
        Instant requestStartTime = Instant.now();
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            Instant requestEndTime = Instant.now();
            Time duration = Time.valueOf(LocalTime.MIDNIGHT.plus(Duration.between(requestStartTime, requestEndTime)));
            parseResponse(response, dto, duration);
        } catch (SocketTimeoutException e) {
            dto.setLastSyncResultCode(HttpStatus.SC_REQUEST_TIMEOUT);
            dto.setLastSyncDuration(Time.valueOf("00:00:00"));
            dto.setEntityList(Collections.emptyList());
            dto.setLastSyncElementCount(0);
            dto.setLastErrorMessage("Read timed out");
            if (dto instanceof FactDto) ((FactDto) dto).setNextSinceToken(((FactDto) dto).getSinceToken());

            LOGGER.warn("#execute-request-warn# Read timed out while executing request.");
//            LOGGER.debug("#execute-request-warn# Read timed out while executing request. \n Message: {}. \n Status code: {}. \n Sync duration: {}."
//                            + "\n Last sync element count: {}. \n Total elements count: {}. \n Additional info: {}.",
//                    dto.getLastErrorMessage(),
//                    dto.getLastSyncResultCode(),
//                    dto.getLastSyncDuration(),
//                    dto.getLastSyncElementCount(),
//                    dto.getTotalElementsCount(),
//                    dto.additionalInfo()
//            );
        } catch (IOException e) {
            LOGGER.error("#execute-request-error# Error while executing request. \n Message: {}. \n Request: {} \n Dto: {}.",
                    e.getMessage(), request, dto);
        }
    }

    private URI buildEndpoint(AbstractDto<T> dto, String host, Instant currentTime) {
        URIBuilder builder = new URIBuilder();
        buildURI(builder, dto, currentTime);
        try {
            return builder
                    .setScheme(endPointScheme())
                    .setHost(endPointHost(host))
                    .build();
        } catch (URISyntaxException e) {
            LOGGER.error("#build-uri-error# Error while building uri. \n Message: {}. \n Dto: {}.",
                    e.getMessage(), dto);
        }
        return null;
    }

    protected RequestConfig createRequestConfig() {
        // setting timeouts to avoid pumps lags when MIX doesn't respond (default timeout = infinite)
        return RequestConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(socketTimeout)
                .build();
    }

    private void parseResponse(CloseableHttpResponse response, AbstractDto<T> dto, Time duration) {
        dto.setLastSyncResultCode(response.getStatusLine().getStatusCode());
        dto.setLastSyncDuration(duration);

        switch (response.getStatusLine().getStatusCode()) {
            case HttpStatus.SC_OK:
            case HttpStatus.SC_PARTIAL_CONTENT:
                fillDtoWithInfoOnSuccess(dto, receiveEntitiesFromResponse(response), response);
                break;
            default:
                fillDtoWithInfoOnError(dto, Collections.emptyList(), response);
                break;
        }
    }


    private List<T> receiveEntitiesFromResponse(CloseableHttpResponse response) {
        List<T> result = Collections.emptyList();
        try {
            if (response.getEntity() != null) {
                InputStream content = response.getEntity().getContent();
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(content, StandardCharsets.UTF_8))) {
                    String jsonContent = bufferedReader.readLine();
                    if (jsonContent != null) {
                        result = objectMapper.readValue(jsonContent, javaType);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("#parse-response-error# Error while receiving content from response. \n Message: {}. \n Response: {}.",
                    e.getMessage(), response);
            return Collections.emptyList();
        }
        return result;
    }

    protected abstract void fillDtoWithInfoOnSuccess(AbstractDto<T> dto, List<T> entityList, CloseableHttpResponse response);

    protected abstract void fillDtoWithInfoOnError(AbstractDto<T> dto, List<T> entityList, CloseableHttpResponse response);

    protected abstract HttpRequestBase createRequest(URI endPoint, AbstractDto<T> dto);

    protected abstract void buildURI(URIBuilder uriBuilder, AbstractDto<T> dto, Instant currentTime);

    protected String endPointScheme() {
        return MIX_ENDPOINT_SCHEME;
    }

    protected String endPointHost(String host) {
        LOGGER.info("Host : {}", host);
        return MIX_ENDPOINT_HOST;
    }
}
