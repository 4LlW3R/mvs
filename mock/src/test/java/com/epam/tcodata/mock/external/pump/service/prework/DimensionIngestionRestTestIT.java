package com.epam.tcodata.mock.external.pump.service.prework;

import com.epam.tcodata.external.pump.factory.IExternalFactory;
import com.epam.tcodata.external.pump.factory.impl.ExternalDriverFactory;
import com.epam.tcodata.models.mix.dimension.Asset;
import com.epam.tcodata.models.mix.dimension.Driver;
import com.epam.tcodata.models.mix.dimension.LibraryEvent;
import com.epam.tcodata.models.mix.dimension.Location;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.AccountTokens;
import com.epam.tcodata.sql.dal.service.pumps.IAccountTokensService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;

// TO DO
@Ignore("Testing receiving of dimensions using REST MiX Api and converting it from JSON to POJO")
public class DimensionIngestionRestTestIT {
    private static final Logger LOGGER = LoggerFactory.getLogger(DimensionIngestionRestTestIT.class);

    private static final String GROUP_ID = "7880976660639567437"; // the same as organisation id
    private static final String DRIVER_ENDPOINT = "https://integrate.uk.mixtelematics.com/api/drivers" +
            "/organisation/" + GROUP_ID;
    private static final String ASSET_ENDPOINT = "https://integrate.uk.mixtelematics.com/api/assets" +
            "/group/" + GROUP_ID;
    private static final String LIBRARY_EVENT_ENDPOINT = "https://integrate.uk.mixtelematics.com/api/libraryevents" +
            "/organisation/" + GROUP_ID;
    private static final String LOCATION_ENDPOINT = "https://integrate.uk.mixtelematics.com/api/locations" +
            "/group/" + GROUP_ID;

    private static String restAccessToken;
    private static CloseableHttpClient httpClient;
    private static ObjectMapper objectMapper;
    private static volatile Boolean stopFlag = false;

    @BeforeClass
    public static void setUp() throws Exception {
        IExternalFactory factory = new ExternalDriverFactory();
//        IFactory factory = IFactory.entityFactory(EntityType.DRIVER);
        ISecretStorage secretStorage = factory.createSecretStorage();
        IDaoFactory daoFactory = factory.createPumpDaoFactory(secretStorage);
        IAccountTokensService accountTokenService = IDaoFactory.service(daoFactory, AccountTokens.class);
        Map<String, Object> accountIdFilter = new HashMap<>();
        accountIdFilter.put(AccountTokens.Fields.ACCOUNT_ID, 1L);
        restAccessToken = accountTokenService.readFiltered(accountIdFilter).get(0).getAccessToken();

        ConnectionKeepAliveStrategy keepAliveStrategy = new DefaultConnectionKeepAliveStrategy() {

            @Override
            public long getKeepAliveDuration(
                    HttpResponse response,
                    HttpContext context) {
                long keepAlive = super.getKeepAliveDuration(response, context);
                if (keepAlive == -1) {
                    // Keep connections alive 5 seconds if a keep-alive value
                    // has not be explicitly set by the server
                    keepAlive = 5000;
                }
                return keepAlive;
            }
        };

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(100);
        connectionManager.setMaxTotal(100);

        httpClient = HttpClients.custom()
                .setKeepAliveStrategy(keepAliveStrategy)
                .setConnectionManager(connectionManager)
                .build();

        objectMapper = new ObjectMapper();
    }

    @AfterClass
    public static void cleanUp() throws IOException {
        httpClient.close();
    }

    @Test
    public void driversPojoListNotEmptyTest() throws IOException, InterruptedException {
        CloseableHttpResponse response = getResponseFromRestApi(DRIVER_ENDPOINT);
        List<Driver> drivers = mapJsonToPojo(response);
        printList(drivers);
        assertFalse(drivers.isEmpty());
    }

    @Test
    public void assetsPojoListNotEmptyTest() throws IOException, InterruptedException {
        CloseableHttpResponse response = getResponseFromRestApi(ASSET_ENDPOINT);
        List<Asset> assets = mapJsonToPojo(response);
        printList(assets);
        assertFalse(assets.isEmpty());
    }

    @Test
    public void libraryEventsPojoListNotEmptyTest() throws IOException, InterruptedException {
        CloseableHttpResponse response = getResponseFromRestApi(LIBRARY_EVENT_ENDPOINT);
        List<LibraryEvent> libraryEvents = mapJsonToPojo(response);
//        printList(libraryEvents);
        assertFalse(libraryEvents.isEmpty());
    }

    @Test
    public void locationsPojoListNotEmptyTest() throws IOException, InterruptedException {
        CloseableHttpResponse response = getResponseFromRestApi(LOCATION_ENDPOINT);
        List<Location> locations = mapJsonToPojo(response);
//        printList(locations);
        assertFalse(locations.isEmpty());
    }


    private <T> List<T> mapJsonToPojo(CloseableHttpResponse response) throws IOException {
        try (BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
            String entityJsonStr = rd.readLine();

            LOGGER.info("json: {}", entityJsonStr);

            return objectMapper.readValue(entityJsonStr, new TypeReference<List<T>>() {
            });
        } finally {
            response.close();
        }
    }

    private CloseableHttpResponse getResponseFromRestApi(String endPoint) throws IOException, InterruptedException {

        // for multithreaded requesting
        GetThread[] threads = new GetThread[100];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new GetThread(httpClient, endPoint);
            threads[i].setName("Thread n " + i);
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

        // for one request test
        HttpGet httpGet = new HttpGet(endPoint);
        httpGet.setHeader("Authorization", "Bearer " + restAccessToken);
        return httpClient.execute(httpGet);
    }

    private <T> void printList(List<T> list) {
        for (T t : list) {
            LOGGER.info(t.toString());
        }
    }

    static class GetThread extends Thread {

        private final CloseableHttpClient httpClient;
        private final HttpClientContext clientContext;
        private final String endpoint;

        GetThread(CloseableHttpClient httpClient, String endpoint) {
            this.httpClient = httpClient;

            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(20000)
                    .setConnectTimeout(20000)
                    .build();
            HttpHost targetHost = HttpHost.create("integrate.uk.mixtelematics.com");
            clientContext = HttpClientContext.create();
            clientContext.setRequestConfig(requestConfig);
            clientContext.setTargetHost(targetHost);

            this.endpoint = endpoint;
        }

        @Override
        public void run() {
            HttpGet httpGet = new HttpGet(endpoint);
            httpGet.setHeader("Authorization", "Bearer " + restAccessToken);
            for (int i = 1; i < 1000; i++) {
                if (Boolean.TRUE.equals(stopFlag)) {
                    break;
                }
                try (CloseableHttpResponse httpResponse = httpClient.execute(httpGet, clientContext)) {
                    if (httpResponse.getStatusLine().getStatusCode() == 429) {
                        stopFlag = true;
                    }
                    httpResponse.getEntity();
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                }
                LOGGER.info("###attempt: {} - {}", Thread.currentThread().getName(), i);
            }
        }
    }
}
