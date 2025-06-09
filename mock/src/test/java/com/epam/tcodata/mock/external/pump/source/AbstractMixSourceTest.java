//package com.epam.tcodata.mock.external.pump.source;
//
//import com.epam.tcodata.external.pump.dto.Dto;
//import com.epam.tcodata.external.pump.dto.maker.IDtoMaker;
//import com.epam.tcodata.external.pump.source.IMixSource;
//import com.epam.tcodata.mock.external.pump.factory.MockAbstractExternalFactory;
//import com.epam.tcodata.mock.external.pump.factory.impl.MockExternalPositionFactory;
//import com.epam.tcodata.mock.external.pump.util.misc.MockExpectationInitializer;
//import com.epam.tcodata.mock.external.pump.util.misc.RestMockUtil;
//import com.epam.tcodata.mock.external.pump.util.misc.TestDaoUtil;
//import com.epam.tcodata.models.avro.fact.AvroPosition;
//import com.epam.tcodata.models.enriched.fact.EnrichedPosition;
//import com.epam.tcodata.models.mix.fact.Position;
//import com.epam.tcodata.secure.storage.dal.ISecretStorage;
//import com.epam.tcodata.sql.dal.IDaoFactory;
//import org.junit.*;
//import org.mockserver.configuration.ConfigurationProperties;
//import org.mockserver.integration.ClientAndServer;
//
//import java.time.Instant;
//import java.time.OffsetDateTime;
//import java.time.ZoneOffset;
//import java.util.List;
//
//import static junit.framework.TestCase.assertEquals;
//
//public class AbstractMixSourceTest {
//
//    static {
//        ConfigurationProperties.initializationClass(MockExpectationInitializer.class.getName());
//    }
//
//    private static int BACH_INTERVAL = 100;
//
//    private static ClientAndServer mockServer = null;
//
//    private MockAbstractExternalFactory<Position, EnrichedPosition, AvroPosition> factory = null;
//    private IDaoFactory daoFactory = null;
//
//    @BeforeClass
//    public static void setup() {
//        mockServer = ClientAndServer.startClientAndServer(RestMockUtil.PORT);
//    }
//
//    @AfterClass
//    public static void cleanup() {
//        mockServer.stop();
//    }
//
//
//
//    @Before
//    public void setUp() throws Exception {
//        this.factory = new MockExternalPositionFactory();
//        ISecretStorage secretStorage = this.factory.createSecretStorage();
//        this.daoFactory = this.factory.createPumpDaoFactory(secretStorage);
//
//        TestDaoUtil.clearDatabase(this.daoFactory);
//        TestDaoUtil.populateDatabase(this.daoFactory);
//    }
//
//    @After
//    public void tearDown() throws Exception {
//    }
//
//    @Test
//    public void testMixSource() throws Exception {
//        IMixSource<Position> mixSource = this.factory.createMixSource();
//        IDtoMaker<Position> dtoMaker = this.factory.createDtoMaker(daoFactory, BACH_INTERVAL);
//        List<Dto<Position>> dtos = dtoMaker.makeDtoList();
//
//        for (Dto<Position> dto : dtos) {
//            String sinceToken = instantToString(dto.getSinceToken());
//
//            String fileName = String.format("base/api/positions/groups/from/%s/to/%s/20191020090241000.json", sinceToken);
//            List<Position> positions = RestMockUtil.loadData(Position.class, fileName);
//            mixSource.requestDataAndFillDto(dto);
//
//            assertEquals(positions, dto.getEntityList());
//        }
//
//    }
//    private static String instantToString(Instant instant) {
//        OffsetDateTime off = instant.atOffset(ZoneOffset.UTC);
//        return String.format("%04d%02d%02d%02d%02d%02d", off.getYear(), off.getMonthValue(), off.getDayOfMonth(),
//                off.getHour(), off.getMinute(), off.getSecond());
//    }
//}
