package com.epam.tcodata.mock.sql.dal.impl.speedlayer;

import com.epam.tcodata.mock.util.MockUtils;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.speedlayer.SpeedLayerEvent;
import com.epam.tcodata.sql.dal.domain.speedlayer.SpeedLayerPosition;
import com.epam.tcodata.sql.dal.exception.OperationIsNotSupportedException;
import com.epam.tcodata.sql.dal.service.impl.speedlayer.SpeedLayerEventService;
import com.epam.tcodata.sql.dal.service.impl.speedlayer.SpeedLayerPositionService;
import org.junit.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static com.epam.tcodata.sql.dal.IDaoFactory.service;
import static junit.framework.TestCase.*;

public class MockSpeedLayerDaoFactoryTest {

    private static IDaoFactory daoFactory = null;

    private static SpeedLayerPositionService speedLayerPositionService = null;
    private static SpeedLayerEventService speedLayerEventService = null;


    @BeforeClass
    public static void setUp() throws Exception {
        ISecretStorage defaultMockSecretStorage = MockUtils.createDefaultMockSecretStorage();
        daoFactory = new MockSpeedLayerDaoFactory(defaultMockSecretStorage);

        speedLayerPositionService = service(daoFactory, SpeedLayerPosition.class);
        speedLayerEventService = service(daoFactory, SpeedLayerEvent.class);
    }

    @AfterClass
    public static void cleanUp() throws Exception {
        speedLayerPositionService.close();
        speedLayerEventService.close();
        daoFactory.close();
    }

    @Before
    public void init() throws Exception {
        speedLayerPositionService.deleteAll();
        speedLayerEventService.deleteAll();
    }

    @After
    public void reset() throws Exception {
        /***  Default implementation ***/
    }

    @Test
    public void daoFactoryWithProperSecretStorageTest() {
        ISecretStorage defaultMockSecretStorage = MockUtils.createDefaultMockSecretStorage();
        IDaoFactory testFactory2 = new MockSpeedLayerDaoFactory(defaultMockSecretStorage);
        assertNotNull(testFactory2);
    }

    @Test
    public void SpeedLayerPositionServiceCRUD_Test() {
        SpeedLayerPositionService service = speedLayerPositionService;

        SpeedLayerPosition entity = new SpeedLayerPosition();
        entity.setObservedDay(365);
        entity.setDurableId("durable id");
        entity.setIngestedDateUtc(Timestamp.from(Instant.now()));
        entity.setSubscriptionId(100L);
        entity.setLineageCode(5);
        entity.setPersistedDateUtc(Timestamp.from(Instant.now()));
        entity.setDriverDurableKey("driver durable key");
        entity.setVehicleDurableKey("vehicle durable key");
        entity.setPositionId(1L);
        entity.setAssetId(2L);
        entity.setDriverId(3L);
        entity.setTimestamp(Timestamp.from(Instant.now()));
        entity.setLatitude(40.123D);
        entity.setLongitude(55.234D);
        entity.setSpeedKilometresPerHour(100.0D);
        entity.setSpeedLimit(50.0D);
        entity.setAltitudeMetres(100);
        entity.setHeading(1);
        entity.setNumberOfSatellites(15);
        entity.setHdop(11);
        entity.setVdop(12);
        entity.setPdop(13);
        entity.setAgeOfReadingSeconds(3600L);
        entity.setDistanceSinceReadingKilometres(500);
        entity.setIgnitionOn(true);
        entity.setOdometerKilometres(100000.0D);
        entity.setFormattedAddress("formatted address");
        entity.setSource("source");
        entity.setAvl(true);
        long id = service.insert(entity);

        SpeedLayerPosition inserted = service.readAll().get(0);
        assertNotSame(entity, inserted);
        assertEquals(entity, inserted);
        entity.setDurableId("another");
        assertFalse(entity.equals(inserted));

        List<SpeedLayerPosition> list = service.readAll();
        assertEquals(1, list.size());

        service.deleteAll();
        List<SpeedLayerPosition> all = service.readAll();
        assertTrue(all.isEmpty());
    }

    @Test(expected = OperationIsNotSupportedException.class)
    public void SpeedLayerPositionServiceUpdate_Test() throws OperationIsNotSupportedException{
        SpeedLayerPositionService service = speedLayerPositionService;
        SpeedLayerPosition entity = new SpeedLayerPosition();
        service.update(entity);
    }

    @Test(expected = OperationIsNotSupportedException.class)
    public void SpeedLayerPositionServiceDelete_Test() throws OperationIsNotSupportedException{
        SpeedLayerPositionService service = speedLayerPositionService;
        service.delete(0L);
    }

    @Test
    public void SpeedLayerEventServiceCRUD_Test() {
        SpeedLayerEventService service = speedLayerEventService;

        SpeedLayerEvent entity = new SpeedLayerEvent();
        entity.setObservedDay(365);
        entity.setDurableId("durable id");
        entity.setIngestedDateUtc(Timestamp.from(Instant.now()));
        entity.setSubscriptionId(100L);
        entity.setLineageCode(5);
        entity.setPersistedDateUtc(Timestamp.from(Instant.now()));
        entity.setDriverDurableKey("driver durable key");
        entity.setVehicleDurableKey("vehicle durable key");
        entity.setAssetId(2L);
        entity.setDriverId(3L);
        entity.setEventId(222L);
        entity.setEventTypeId(4L);
        entity.setEventCategory("event category");
        entity.setStartDateTime(Timestamp.from(Instant.now()));
        entity.setStartOdometerKilometres(200000.0D);
        entity.setStartPositionTimestamp(Timestamp.from(Instant.now()));
        entity.setStartPositionLatitude(40.111D);
        entity.setStartPositionLongitude(55.000D);
        entity.setStartPositionPositionId(444L);
        entity.setStartPositionSpeedKilometresPerHour(60.0D);
        entity.setEndDateTime(Timestamp.from(Instant.now()));
        entity.setEndOdometerKilometres(220000.0D);
        entity.setEndPositionTimestamp(Timestamp.from(Instant.now()));
        entity.setEndPositionLongitude(55.111D);
        entity.setEndPositionLatitude(66.999D);
        entity.setEndPositionPositionId(12L);
        entity.setEndPositionSpeedKilometresPerHour(45.0D);
        entity.setValue(123.0D);
        entity.setValueType("value type");
        entity.setValueUnits("value units");
        entity.setTotalTimeSeconds(600);
        entity.setTotalOccurrences(800L);
        entity.setMediaUrlsRoad("media urls road");
        entity.setMediaUrlsCab("media urls cab");
        entity.setMediaUrlsCamera3("media urls camera3");
        entity.setMediaUrlsCamera4("media urls camera4");
        entity.setLocationId(888L);
        entity.setSpeedLimit(50.0D);
        long id = service.insert(entity);

        SpeedLayerEvent inserted = service.readAll().get(0);
        assertNotSame(entity, inserted);
        assertEquals(entity, inserted);

        entity.setDurableId("another");
        assertFalse(entity.equals(inserted));

        List<SpeedLayerEvent> list = service.readAll();
        assertEquals(1, list.size());

        service.deleteAll();
        List<SpeedLayerEvent> all = service.readAll();
        assertTrue(all.isEmpty());
    }



    @Test(expected = OperationIsNotSupportedException.class)
    public void SpeedLayerEventServiceUpdate_Test() {
        SpeedLayerEventService service = speedLayerEventService;
        SpeedLayerEvent entity = new SpeedLayerEvent();
        service.update(entity);
    }

    @Test(expected = OperationIsNotSupportedException.class)
    public void SpeedLayerEventServiceDelete_Test() {
        SpeedLayerEventService service = speedLayerEventService;
        service.delete(0L);
    }

    @Test
    public void AllServicesReadAllTest() {
        List<SpeedLayerPosition> speedLayerPositions = speedLayerPositionService.readAll();
        List<SpeedLayerEvent> speedLayerEvents = speedLayerEventService.readAll();

        assertNotNull(speedLayerPositions);
        assertNotNull(speedLayerEvents);
    }
}
