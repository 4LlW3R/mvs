//package com.epam.tcodata.rest.pump.service;
//
//import com.epam.tcodata.keymanager.dal.IDaoFactory;
//import com.epam.tcodata.keymanager.dal.PumpDaoFactory;
//import com.epam.tcodata.keymanager.dal.domain.pumps.MixOffset;
//import com.epam.tcodata.keymanager.dal.service.pumps.IMixOffsetService;
//import com.epam.tcodata.models.EntityType;
//import com.epam.tcodata.models.exception.NonExistentEntityTypeException;
//import com.epam.tcodata.models.mix.Entity;
//import com.epam.tcodata.models.mix.fact.Position;
//import com.epam.tcodata.rest.pump.drivers.domain.Dto;
//import com.epam.tcodata.rest.pump.service.entity.TripService;
//import com.epam.tcodata.rest.pump.source.fact.AbstractFactMixSource;
//import com.epam.tcodata.sql.dal.domain.AccountTokens;
//import com.epam.tcodata.sql.dal.domain.OrganisationGroup;
//import com.epam.tcodata.sql.dal.service.IAccountTokensService;
//import com.epam.tcodata.sql.dal.service.IOrganisationGroupService;
//import com.epam.tcodata.sql.dal.service.impl.AccountTokenService;
//import com.epam.tcodata.sql.dal.service.impl.OrganisationGroupService;
//import com.fasterxml.jackson.databind.JavaType;
//import com.fasterxml.jackson.databind.MapperFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.utils.URIBuilder;
//import org.apache.http.entity.ContentType;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.nio.charset.Charset;
//import java.sql.Time;
//import java.time.Instant;
//import java.time.LocalTime;
//import java.time.ZoneId;
//import java.time.ZoneOffset;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//
//import static com.epam.tcodata.keymanager.dal.IDaoFactory.service;
//import static junit.framework.TestCase.assertEquals;
//import static junit.framework.TestCase.assertFalse;
//
//public class AbstractMixSourceTest {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMixSourceTest.class);
//
//    private static IDaoFactory daoFactory = null;
//
//    private static IMixOffsetService mixOffsetService = null;
//    private IAccountTokensService accountTokenService;
//    private IOrganisationGroupService organisationGroupService;
//
//    private static Properties mixIntegrateProperties;
//    private String accessToken;
//    private List<Long> orgGroupIdList;
//
//    private static final String MIX_ENDPOINT_SCHEME_PROPERTY = "mix.endpoint.scheme";
//    private static final String MIX_ENDPOINT_HOST_PROPERTY = "mix.endpoint.host";
//    private static final String MIX_ENDPOINT_POSITIONS = "mix.endpoint.positions";
//    private static final String ENPOINT_FROM_PART = "/from/";
//    private static final String ENPOINT_TO_PART = "/to/";
//
//    private static final String HEADER_CONTENT_TYPE = "Content-Type";
//    private static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
//    private static final String HEADER_AUTHORIZATION = "Authorization";
//    private static final String AUTHORIZATION_BEARER = "Bearer ";
//
//    @BeforeClass
//    public static void setUp() {
//        daoFactory = new PumpDaoFactory();
//        mixOffsetService = service(daoFactory, MixOffset.class);
//
//        mixIntegrateProperties = loadProperties("mix-integration.properties");
//    }
//
//    @AfterClass
//    public static void cleanUp() throws Exception {
//        mixOffsetService.close();
//        daoFactory.close();
//    }
//
//    @Before
//    public void init() {
//        accountTokenService = new AccountTokenService();
//        organisationGroupService = new OrganisationGroupService();
//
//        orgGroupIdList = new ArrayList<>();
//        fillOrgGroupIdList(orgGroupIdList);
//    }
//
//    // !!START OF REQ TIME TEST!!
//    // TEST TO CHECK REQUEST TIME FOR ACTIVE ORGANISTATIONS
//    // FOR from/to ENDPOINT
//
//    @Test
//    public void requestTimeFromToForActiveOrganisationsTest() {
//        Instant startInstant = Instant.now().minusSeconds(14400);
//        for (int attemptNum = 1; attemptNum < 11; attemptNum++) {
//            updateAccessToken();
//
//            List<Dto> dtoList = formDtoList(startInstant, attemptNum);
//
//            Map<Long, String> requestTimeMap = new HashMap<>();
//
//            for (Dto dto : dtoList) {
//                updateAccessTokenForDto(dto);
//
//                try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
//                    HttpPost post = new HttpPost(buildEndpoint(dto, EntityType.POSITION));
//
//                    setRequestParameters(post, dto);
//
//                    long startTime = System.currentTimeMillis();
//                    CloseableHttpResponse response = httpClient.execute(post);
//                    long elapsedTime = System.currentTimeMillis() - startTime;
//
//                    if (response.getEntity() == null || response.getStatusLine().getStatusCode() == 400) {
//                        requestTimeMap.put(dto.getOrgGroupId(),
//                                " | Code: " + response.getStatusLine().getStatusCode()
//                                        + " | Time/Response: " + response.getStatusLine().getReasonPhrase());
//                        dto.setEntityList(Collections.emptyList());
//                    } else {
//                        requestTimeMap.put(dto.getOrgGroupId(),
//                                " | Code: " + response.getStatusLine().getStatusCode()
//                                        + " | Time/Response: " + elapsedTime);
//                        List<Entity> entityList = getDataFromJson(response);
//                        fillDtoWithInfo(dto, entityList, response);
//                    }
//                } catch (IOException e) {
//                    requestTimeMap.put(dto.getOrgGroupId(), " | Exception: " + e.toString());
//                }
//                LOGGER.info("Org group ID: " + dto.getOrgGroupId()
//                        + " | Time: " + requestTimeMap.get(dto.getOrgGroupId())
//                        + " | Received: " + dto.getEntityList().size());
//            }
//        }
//    }
//
//    private void updateAccessToken() {
//        Optional<AccountTokens> accountTokensOpt = accountTokenService.getAccountTokens(20);
//        if (accountTokensOpt.isPresent()) {
//            accessToken = accountTokensOpt.get().getAccessToken();
//        }
//    }
//
//    private void updateAccessTokenForDto(Dto dto) {
//        Optional<AccountTokens> accountTokensOpt = accountTokenService.getAccountTokens(20);
//        if (accountTokensOpt.isPresent()) {
//            accessToken = accountTokensOpt.get().getAccessToken();
//        }
//        dto.setAccessToken(accessToken);
//    }
//
//    private List<Dto> formDtoList(Instant startTime, int attemptNum) {
//
//        Instant from = startTime.minusSeconds(attemptNum * 120);
//        Instant to = from.plusSeconds(120);
//        LOGGER.info("ATTEMPT: " + attemptNum + ", FROM: " + from + ", TO: " + to);
//
//        List<Dto> dtoList = new ArrayList<>();
//
//        for (Long id : orgGroupIdList) {
//            dtoList.add(new Dto(
//                            from,
//                            to,
//                            1,
//                            id,
//                            accessToken,
//                            Time.valueOf(LocalTime.now()),
//                            0
//                    )
//            );
//        }
//
//        return dtoList;
//    }
//
//    private void fillDtoWithInfo(Dto dto,
//                                 List<Entity> entityList,
//                                 CloseableHttpResponse response) {
//        long currentElementCount = entityList.size();
//        long totalElementCount = dto.getTotalElementsCount();
//
//        dto.setEntityList(entityList);
//        dto.setLastSyncElementCount(currentElementCount);
//        dto.setTotalElementsCount(totalElementCount + currentElementCount);
//        dto.setLastSyncResultCode(response.getStatusLine().getStatusCode());
//    }
//
//    private List<Entity> getDataFromJson(CloseableHttpResponse response) throws IOException {
//        String line;
//        try (BufferedReader rd = new BufferedReader(
//                new InputStreamReader(response.getEntity().getContent(), Charset.forName("UTF-8")))) {
//            line = rd.readLine();
//        }
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
//        JavaType type = mapper.getTypeFactory()
//                .constructParametricType(
//                        ArrayList.class,
//                        Position.class);
//
//        List<Entity> list = mapper.readValue(line, type);
//        return list;
//    }
//
//    private void fillOrgGroupIdList(List<Long> orgGroupIdList) {
//        List<OrganisationGroup> organisationGroupList = organisationGroupService.getActiveOrganisationGroupByAccountId(1);
//        for (OrganisationGroup organisationGroup : organisationGroupList) {
//            orgGroupIdList.add(organisationGroup.getGroupId());
//        }
//    }
//
//    private String buildEndpoint(Dto dto, EntityType entityCode) {
//        String from = instantToString(dto.getFrom());
//        String to = instantToString(dto.getTo());
//
//        String scheme = mixIntegrateProperties.getProperty(MIX_ENDPOINT_SCHEME_PROPERTY);
//        String host = mixIntegrateProperties.getProperty(MIX_ENDPOINT_HOST_PROPERTY);
//        String path = new StringBuilder()
//                .append(mixIntegrateProperties.getProperty(getEndpointType(entityCode)))
//                .append(ENPOINT_FROM_PART)
//                .append(from)
//                .append(ENPOINT_TO_PART)
//                .append(to)
//                .toString();
//
//        URIBuilder builder = new URIBuilder();
//        return builder
//                .setScheme(scheme)
//                .setHost(host)
//                .setPath(path)
//                .toString();
//    }
//
//    private String instantToString(Instant instant) {
//        DateTimeFormatter formatter =
//                DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
//                        .withZone(ZoneId.systemDefault());
//        return formatter.format(instant);
//    }
//
//    private String getEndpointType(EntityType entityCode) {
//        switch (entityCode) {
//            case POSITION:
//                return MIX_ENDPOINT_POSITIONS;
//            default:
//                throw new NonExistentEntityTypeException("Entity code doesn't exist");
//        }
//    }
//
//    private void setRequestParameters(HttpPost post, Dto dto) {
//        String bracketedOrgGroupId = bracketGroupId(dto.getOrgGroupId());
//        StringEntity bodyGroupId = new StringEntity(
//                bracketedOrgGroupId,
//                ContentType.APPLICATION_JSON);
//        RequestConfig requestConfig = RequestConfig.custom()
//                .setConnectTimeout(60000)
//                .build();
//
//        post.setConfig(requestConfig);
//        post.setHeader(HEADER_CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON);
//        post.setHeader(HEADER_AUTHORIZATION, AUTHORIZATION_BEARER + dto.getAccessToken());
//        post.setEntity(bodyGroupId);
//    }
//
//    private String bracketGroupId(long orgGroupId) {
//        return new StringBuilder()
//                .append('[')
//                .append(orgGroupId)
//                .append(']')
//                .toString();
//    }
//
//    // !!END OF REQ TIME TEST!!
//
//    @Test
//    public void requestTripWithSubTripTest() {
//        AbstractFactMixSource abstractMixSource = new AbstractFactMixSource(new TripService(mixIntegrateProperties));
//
//
//        Instant to = Instant.now();
//        Instant from = to.minusSeconds(300);
//
//        Dto dto = new Dto(
//                from,
//                to,
//                1,
//                7880976660639567437L,
//                accountTokenService.getAccountTokensByAccountId(1).get().getAccessToken(),
//                Time.valueOf(LocalTime.now()),
//                0
//        );
//
//        abstractMixSource.requestDataAndFillDto(dto);
//
//        LOGGER.info("Enriched DTO: {}", dto);
//
//        assertFalse(dto.getEntityList().isEmpty());
//    }
//
//    @Test
//    public void convertInstantToStringTest() {
//        Instant instant = Instant.parse("2018-10-26T12:30:00Z");
//        DateTimeFormatter formatter =
//                DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
//                        .withZone(ZoneOffset.UTC);
//        assertEquals("20181026123000", formatter.format(instant));
//    }
//
//    @Test
//    public void createEndpointStrTest() {
//        String scheme = "https";
//        String host = "integrate.uk.mixtelematics.com";
//        String path = new StringBuilder()
//                .append("/api/positions/groups")
//                .append("/from/")
//                .append("20181026123000")
//                .append("/to/")
//                .append("20181026123100")
//                .toString();
//
//        URIBuilder builder = new URIBuilder();
//        String endpointStr = builder
//                .setScheme(scheme)
//                .setHost(host)
//                .setPath(path)
//                .toString();
//
//        assertEquals("https://integrate.uk.mixtelematics.com/api/positions/groups" +
//                        "/from/20181026123000/to/20181026123100",
//                endpointStr);
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
