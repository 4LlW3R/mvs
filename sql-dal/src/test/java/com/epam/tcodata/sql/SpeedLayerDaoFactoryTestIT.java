package com.epam.tcodata.sql;

import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.Secret;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.speedlayer.SpeedLayerEvent;
import com.epam.tcodata.sql.dal.domain.speedlayer.SpeedLayerPosition;
import com.epam.tcodata.sql.dal.impl.speedlayer.SpeedLayerDaoFactory;
import com.epam.tcodata.sql.dal.service.speedlayer.ISpeedLayerService;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import static com.epam.tcodata.sql.dal.IDaoFactory.service;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SpeedLayerDaoFactoryTestIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpeedLayerDaoFactoryTestIT.class);

    private static IDaoFactory speedLayerDaoFactory = null;
    private static ISpeedLayerService<SpeedLayerPosition> speedLayerPositionService = null;
    private static ISpeedLayerService<SpeedLayerEvent> speedLayerEventService = null;
    private static final String ZEROES_CONST = "00000000-0000-0000-c000-000000000046";
    private static final String DATE_2019_01_09_09_04_54_0 = "2019-01-09 09:04:54.0";

    @BeforeClass
    public static void setUp() {
        ISecretStorage secretStorage = mock(ISecretStorage.class);
        when(secretStorage.retrieveSecret(Secret.Sql.SPEEDLAYER.user)).thenReturn("user");
        when(secretStorage.retrieveSecret(Secret.Sql.SPEEDLAYER.password)).thenReturn("password");
        speedLayerDaoFactory = new SpeedLayerDaoFactory(secretStorage);
        speedLayerPositionService = service(speedLayerDaoFactory, SpeedLayerPosition.class);
        speedLayerEventService = service(speedLayerDaoFactory, SpeedLayerEvent.class);
    }

    @AfterClass
    public static void cleanUp() throws Exception {
        speedLayerPositionService.close();
    }

    @Before
    public void init() {
        speedLayerPositionService.deleteAll();
        speedLayerEventService.deleteAll();
    }

    @After
    public void reset() {
        /***  Default implementation ***/
    }

    @Test
    public void testSpeedLayerPositionService() {
        SpeedLayerPosition speedLayerPosition = new SpeedLayerPosition()
                // SpeedLayerCommon fields
                .setObservedDay(9)//
                .setDurableId("60e34cfe-69db-488a-8c83-22dc833b32a4")//
                .setIngestedDateUtc(Timestamp.valueOf("2019-01-09 09:08:10.73"))
                .setSubscriptionId(-2179621628066165938L)//
                .setLineageCode(4)
                .setDriverDurableKey(ZEROES_CONST)
                .setVehicleDurableKey(ZEROES_CONST)
                // Position fields
                .setPositionId(819642514492067857L)
                .setAssetId(5984743831055803025L)
                .setDriverId(3750054867008277942L)
                .setTimestamp(Timestamp.valueOf(DATE_2019_01_09_09_04_54_0))//
                .setLatitude(46.1935005187988)
                .setLongitude(53.4127502441406)
                .setSpeedKilometresPerHour(0.0)
                .setSpeedLimit(null)
                .setAltitudeMetres(-18)
                .setHeading(0)
                .setNumberOfSatellites(11)
                .setHdop(1)
                .setVdop(0)
                .setPdop(0)
                .setAgeOfReadingSeconds(0L)
                .setDistanceSinceReadingKilometres(null)
                .setIgnitionOn(false)
                .setOdometerKilometres(0.0)
                .setFormattedAddress(null)
                .setSource("Gps")
                .setAvl(true);

        speedLayerPositionService.insert(Collections.singletonList(speedLayerPosition));
        List<SpeedLayerPosition> speedLayerPositions = speedLayerPositionService.readAll();

        LOGGER.info(String.valueOf(speedLayerPositions));
        assertEquals(speedLayerPositions.size(), 1);
    }

    @Test
    public void testSpeedLayerEventService() {
        SpeedLayerEvent speedLayerEvent = new SpeedLayerEvent()
                // SpeedLayerCommon fields
                .setObservedDay(9)//
                .setDurableId("60e34cfe-69db-488a-8c83-22dc833b32a4")//
                .setIngestedDateUtc(Timestamp.valueOf("2019-01-09 09:08:10.73"))
                .setSubscriptionId(-2179621628066165938L)//
                .setLineageCode(4)
                .setDriverDurableKey(ZEROES_CONST)
                .setVehicleDurableKey(ZEROES_CONST)
                // Event fields
                .setAssetId(5984743831055803025L)
                .setDriverId(3750054867008277942L)
                .setEventId(3750054867077942L)
                .setEventTypeId(375007008277942L)
                .setEventCategory("Category")
                .setStartDateTime(Timestamp.valueOf(DATE_2019_01_09_09_04_54_0))
                .setStartOdometerKilometres(453d)
                // startPosition fields
                .setStartPositionTimestamp(Timestamp.valueOf(DATE_2019_01_09_09_04_54_0))
                .setStartPositionLongitude(4534d)
                .setStartPositionLatitude(4534d)
                .setStartPositionPositionId(3750054867008277942L)
                .setStartPositionSpeedKilometresPerHour(4534d)
                .setEndDateTime(Timestamp.valueOf(DATE_2019_01_09_09_04_54_0))
                .setEndOdometerKilometres(4534d)
                // endPosition fields
                .setEndPositionTimestamp(Timestamp.valueOf(DATE_2019_01_09_09_04_54_0))
                .setEndPositionLongitude(4534d)
                .setEndPositionLatitude(4534d)
                .setEndPositionPositionId(3750054867008277942L)
                .setEndPositionSpeedKilometresPerHour(4534d)
                .setValue(123.0)
                .setValueType("Type")
                .setValueUnits("Units")
                .setTotalTimeSeconds(123123)
                .setTotalOccurrences(377008277942L)
                // mediaUrls fields
                .setMediaUrlsRoad("UrlsRoads")
                .setMediaUrlsCab("Cab")
                .setMediaUrlsCamera3("Camera3")
                .setMediaUrlsCamera4("Camera4")

                .setLocationId(375005442L)
                .setSpeedLimit(60d);

        speedLayerEventService.insert(Collections.singletonList(speedLayerEvent));
        List<SpeedLayerEvent> speedLayerEvents = speedLayerEventService.readAll();

        LOGGER.info(String.valueOf(speedLayerEvents));
        assertEquals(speedLayerEvents.size(), 1);
    }

}
