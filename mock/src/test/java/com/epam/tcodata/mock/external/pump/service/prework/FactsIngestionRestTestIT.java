//package com.epam.tcodata.rest.pump.service.prework;
//
//import com.epam.tcodata.models.mix.fact.Event;
//import com.epam.tcodata.models.mix.fact.Position;
//import com.epam.tcodata.models.mix.fact.Trip;
//import com.epam.tcodata.rest.pump.service.entity.EventService;
//import com.epam.tcodata.rest.pump.service.entity.IEntityService;
//import com.epam.tcodata.rest.pump.service.entity.TripService;
//import com.epam.tcodata.sql.dal.service.IAccountTokensService;
//import com.epam.tcodata.sql.dal.service.impl.AccountTokenService;
//import com.fasterxml.jackson.databind.JavaType;
//import com.fasterxml.jackson.databind.MapperFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.ContentType;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.junit.Before;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.nio.charset.Charset;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Properties;
//
//import static org.junit.Assert.assertFalse;
//
//@Ignore("Testing receiving of facts using REST MiX Api and converting it from JSON to POJO")
//public class FactsIngestionRestTestIT {
//    private static final Logger LOGGER = LoggerFactory.getLogger(FactsIngestionRestTestIT.class);
//
//    private static final String ENCODING_UTF_8 = "UTF-8";
//
//    private static final String GROUP_ID = "[7880976660639567437]";
//    private static final String FROM = "20181010125900"; //Fix time 12:59 - 13 00 works fine at 14:10 (UTC)
//    private static final String TO = "20181010130000";
//
//    private static final String TIME_SINCE = "20181220125900000";
//
//    private static final String POSITION_ENDPOINT = "https://integrate.uk.mixtelematics.com/api/positions" +
//            "/groups/from/" + FROM + "/to/" + TO;
//    private static final String TRIP_ENDPOINT = "https://integrate.uk.mixtelematics.com/api/trips" +
//            "/groups/from/" + FROM + "/to/" + TO + "/entitytype/Asset";
//    private static final String EVENT_ENDPOINT = "https://integrate.uk.mixtelematics.com/api/events" +
//            "/groups/createdsince/entitytype/Asset/sincetoken/" + TIME_SINCE + "/quantity/1";
//
//    private static String REST_ACCESS_TOKEN;
//    private CloseableHttpClient httpClient;
//    private ObjectMapper objectMapper;
//
//    private Properties mixIntegrateProperties;
//
//    @Before
//    public void setUp() {
//        IAccountTokensService accountTokenService = new AccountTokenService();
//        REST_ACCESS_TOKEN = accountTokenService.getAccountTokensByAccountId(1).get().getAccessToken();
//
//        httpClient = HttpClients.createDefault();
//
//        mixIntegrateProperties = loadProperties("mix-integration.properties");
//
//        objectMapper = new ObjectMapper();
//        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
//    }
//
//    @Test
//    public void positionsPojoListNotEmptyTest() throws IOException {
//        CloseableHttpResponse response = getResponseFromRestApi(POSITION_ENDPOINT);
//        List<Position> positions = mapJsonToPojo(response, new PositionService(mixIntegrateProperties));
//        printList(positions);
//        assertFalse(positions.isEmpty());
//    }
//
//    @Test
//    public void tripsPojoListNotEmptyTest() throws IOException {
//        CloseableHttpResponse response = getResponseFromRestApi(TRIP_ENDPOINT);
//        List<Trip> trips = mapJsonToPojo(response, new TripService(mixIntegrateProperties));
//        printList(trips);
//        assertFalse(trips.isEmpty());
//    }
//
//    @Test
//    public void eventsPojoListNotEmptyTest() throws IOException {
//        CloseableHttpResponse response = getResponseFromRestApi(EVENT_ENDPOINT);
//        List<Event> events = mapJsonToPojo(response, new EventService(mixIntegrateProperties));
//        printList(events);
//        assertFalse(events.isEmpty());
//    }
//
//    private <T> List<T> mapJsonToPojo(CloseableHttpResponse response,
//                                      IEntityService entityService) throws IOException {
//        String line;
//
//        try (BufferedReader rd = new BufferedReader(
//                new InputStreamReader(response.getEntity().getContent(), Charset.forName(ENCODING_UTF_8)))) {
//            line = rd.readLine();
//        }
//        if (line == null) {
//            return Collections.emptyList();
//        }
//
//        JavaType type = objectMapper.getTypeFactory()
//                .constructParametricType(
//                        ArrayList.class,
//                        entityService.getEntitySuperType());
//
//        return objectMapper.readValue(line, type);
//    }
//
//    private CloseableHttpResponse getResponseFromRestApi(String endPoint) throws IOException {
//        HttpPost post = new HttpPost(endPoint);
//        post.setHeader("Content-Type", "application/json");
//        post.setHeader("Authorization", "Bearer " + REST_ACCESS_TOKEN);
//        StringEntity groupIds = new StringEntity(
//                GROUP_ID,
//                ContentType.APPLICATION_JSON);
//        post.setEntity(groupIds);
//        return httpClient.execute(post);
//    }
//
//    private <T> void printList(List<T> list) {
//        for (T t : list) {
//            LOGGER.info(t.toString());
//        }
//    }
//
//    @Test
//    public void mediaUrlsParsingTest() throws IOException {
//        String json = "[{\"TotalOccurances\":1,\"TotalTimeSeconds\":0,\"EventTypeId\":341083421588912423," +
//                "\"EventId\":802135163472154726,\"DriverId\":-2626449904374589061," +
//                "\"AssetId\":6762728369874548266,\"MediaUrls\":{" +
//                "\"Road\":\"http://mvr.uk.mixtelematics.com/viewclip?recording_id=4c5a05a65bf63010&input=0&profile=1\"," +
//                "\"Cab\":\"http://mvr.uk.mixtelematics.com/viewclip?recording_id=4c5a05a65bf63010&input=1&profile=1\"," +
//                "\"Camera_3\":\"http://mvr.uk.mixtelematics.com/viewclip?recording_id=4c5a05a65bf63010&input=2&profile=1\"," +
//                "\"Camera_4\":\"http://mvr.uk.mixtelematics.com/viewclip?recording_id=4c5a05a65bf63010&input=3&profile=1\"}}]";
//        JavaType type = objectMapper.getTypeFactory()
//                .constructParametricType(
//                        ArrayList.class,
//                        Event.class);
//        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
//        List<Event> list = objectMapper.readValue(json, type);
//        LOGGER.info("List: " + list);
//    }
//
//    private static Properties loadProperties(String propertiesFileName) {
//        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiesFileName)) {
//            Properties properties = new Properties();
//            properties.load(inputStream);
//            return properties;
//        } catch (IOException e) {
//            String msg = "Error load " + propertiesFileName + ". Please check it.";
//            LOGGER.error(msg);
//            throw new IllegalArgumentException(msg);
//        }
//    }
//}
