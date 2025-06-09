package com.epam.tcodata.mock.mdm;

import com.epam.tcodata.mdm.IKeyFactory;
import com.epam.tcodata.mdm.IKeyManager;
import com.epam.tcodata.mdm.SearchingResult;
import com.epam.tcodata.mdm.rules.DriverDimensionRule;
import com.epam.tcodata.mock.mdm.base.impl.MockKeyFactory;
import com.epam.tcodata.mock.sql.dal.impl.mdm.MockMdmDaoFactory;
import com.epam.tcodata.mock.util.MockUtils;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.mix.dimension.Driver;
import com.epam.tcodata.models.mix.fact.Position;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.mdm.ApiVersion;
import com.epam.tcodata.sql.dal.domain.mdm.KeyManagerVersion;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class KeyManagerTest {

    private IKeyFactory keyFactory = null;
    private IKeyManager keyManager = null;
    private IDaoFactory daoFactory = null;
    private ISecretStorage secretStorage = null;


    @Before
    public void setUp() throws Exception {
        this.keyFactory = MockKeyFactory.instance();
        this.secretStorage = MockUtils.createDefaultMockSecretStorage();
        this.daoFactory = new MockMdmDaoFactory(this.secretStorage);
        MdmDbUtils.clearTables(this.daoFactory);
        MdmDbUtils.populateTables(this.daoFactory, DriverDimensionRule.class);
        this.keyManager = this.keyFactory.createKeyManager(KeyManagerVersion.VERSION_1_0, this.secretStorage);
    }

    @After
    public void cleanUp() throws Exception {
        this.keyFactory = null;
        this.keyManager.close();
        this.keyManager = null;
        this.daoFactory.close();
        this.daoFactory = null;
    }

    @Test
    public void autoClose() throws Exception {
        try (IKeyManager keyManager = this.keyFactory.createKeyManager(KeyManagerVersion.VERSION_1_0, this.secretStorage)) {
        }
    }

    @Test
    public void subscriptions() {
        IKeyManager defaultManager = this.keyFactory.createKeyManager(KeyManagerVersion.VERSION_1_0, this.secretStorage);
        Set<String> actual = defaultManager.subscriptions();
        assertEquals(new HashSet<>(Arrays.asList("102", "*")), actual);
    }

    @Test()
    public void driverNew() {
        Driver driver = new Driver();
        driver.setDriverId(10001L);
        driver.setEmployeeNumber("0010001");
        UUID uuid = this.keyManager.findOrCreate(driver, ApiVersion.API_1_0, MdmDbUtils.SUBSCRIPTIONS[0], EntityType.DRIVER).getSurrogateKey();

        assertNotNull(uuid);
        assertEquals(MdmDbUtils.UUID_1.length(), uuid.toString().length()); // we don't know the real UUID
        assertEquals(1, MdmDbUtils.existsInMapping(this.daoFactory, MdmDbUtils.DRIVER_ID, "10001", uuid)); // and it should appear in the mapping
        assertEquals(0, MdmDbUtils.existsInMapping(this.daoFactory, MdmDbUtils.EMPLOYEE_NUMBER, "0010001", uuid)); // and it should appear in the mapping
    }


    @Test()
    public void driverExistedWithEmptyEmployeeNumber() {
        // check if the driver already exists
        Driver driver = new Driver();
        driver.setDriverId(Long.parseLong(MdmDbUtils.NATURAL_100));
        UUID uuid = this.keyManager.findOrCreate(driver, ApiVersion.API_1_0, MdmDbUtils.SUBSCRIPTIONS[0], EntityType.DRIVER).getSurrogateKey();
        assertEquals(MdmDbUtils.UUID_1, uuid.toString());
        assertEquals(1, MdmDbUtils.existsInMapping(this.daoFactory, MdmDbUtils.DRIVER_ID, MdmDbUtils.NATURAL_100, uuid)); // and it should appear in the mapping
        assertEquals(0, MdmDbUtils.existsInMapping(this.daoFactory, MdmDbUtils.EMPLOYEE_NUMBER, null, uuid)); // and it shouldn't appear in the mapping
    }

    @Test()
    public void driverExistedWithExistedBadge() {
        // check if the driver already exists
        Driver driver = new Driver();
        driver.setDriverId(Long.parseLong(MdmDbUtils.NATURAL_500));
        driver.setEmployeeNumber(MdmDbUtils.BADGE_555);
        UUID uuid = this.keyManager.findOrCreate(driver, ApiVersion.API_1_0, MdmDbUtils.SUBSCRIPTIONS[0], EntityType.DRIVER).getSurrogateKey();
        assertNotNull(uuid);
        assertEquals(MdmDbUtils.UUID_4, uuid.toString());
        assertEquals(1, MdmDbUtils.existsInMapping(this.daoFactory, MdmDbUtils.DRIVER_ID, MdmDbUtils.NATURAL_500, uuid)); // and it should appear in the mapping
        assertEquals(1, MdmDbUtils.existsInMapping(this.daoFactory, MdmDbUtils.EMPLOYEE_NUMBER, MdmDbUtils.BADGE_555, uuid)); // and it shouldn't appear in the mapping
    }

    @Test
    public void positionNegative() {
        Position position = new Position();
        position.setDriverId(100L);
        position.setAssetId(200L);

        Map<EntityType, List<SearchingResult>> actual = this.keyManager.keysSubstitution(position, ApiVersion.API_1_0,
                MdmDbUtils.SUBSCRIPTIONS[1], EntityType.DRIVER);
        assertEquals(Collections.emptySet(), actual.keySet());
    }

    @Test
    public void positionPositive() {
        Position position = new Position();
        position.setDriverId(100L);
        position.setAssetId(300L);

        Map<EntityType, List<SearchingResult>> actual = this.keyManager.keysSubstitution(
                position,
                ApiVersion.API_1_0,
                MdmDbUtils.SUBSCRIPTIONS[1],
                EntityType.POSITION);

        Set<EntityType> expected = new HashSet<>(Arrays.asList(EntityType.DRIVER, EntityType.ASSET));
        assertEquals(expected, actual.keySet());

        List<SearchingResult> actualList1 = actual.get(EntityType.DRIVER);
        List<SearchingResult> expectedList1 = Arrays.asList(
                new SearchingResult(MdmDbUtils.DRIVER_DURABLE_KEY, UUID.fromString(MdmDbUtils.UUID_1))
        );
        assertEquals(expectedList1, actualList1);

        List<SearchingResult> actualList2 = actual.get(EntityType.ASSET);
        List<SearchingResult> expectedList2 = Arrays.asList(
                new SearchingResult(MdmDbUtils.VEHICLE_DURABLE_KEY, UUID.fromString(MdmDbUtils.UUID_2))
        );
        assertEquals(expectedList2, actualList2);
    }

    @Test
    public void severalPositionsNegative() {
        // Prepare input data
        List<Object> inputPositions = new ArrayList<>();

        Position.PositionBuilder builder = new Position.PositionBuilder()
                .setDriverId(100L)
                .setAssetId(300L);
        inputPositions.add(builder.build());

        builder.setDriverId(500L)
                .setAssetId(400L);
        inputPositions.add(builder.build());

        builder.setDriverId(600L)
                .setAssetId(400L);
        inputPositions.add(builder.build());

        // Request data from keyManager.
        Map<EntityType, List<SearchingResult>> actual = this.keyManager.keysSubstitutions(
                inputPositions,
                ApiVersion.API_1_0,
                MdmDbUtils.SUBSCRIPTIONS[1],
                EntityType.DRIVER);

        // Check entity types.
        assertEquals(Collections.emptySet(), actual.keySet());
    }

    @Test
    public void severalPositionsPositive() {
        // Prepare input data
        List<Object> inputPositions = new ArrayList<>();

        Position.PositionBuilder builder = new Position.PositionBuilder()
                .setDriverId(100L)
                .setAssetId(300L);
        inputPositions.add(builder.build());

        builder.setDriverId(500L)
                .setAssetId(400L);
        inputPositions.add(builder.build());

        builder.setDriverId(600L)
                .setAssetId(400L);
        inputPositions.add(builder.build());

        // Request data from keyManager.
        Map<EntityType, List<SearchingResult>> actual = this.keyManager.keysSubstitutions(
                inputPositions,
                ApiVersion.API_1_0,
                MdmDbUtils.SUBSCRIPTIONS[1],
                EntityType.POSITION);

        // Check entity types.
        Set<EntityType> expected = new HashSet<>(Arrays.asList(EntityType.DRIVER, EntityType.ASSET));
        assertEquals(expected, actual.keySet());

        // Check drivers.
        Set<SearchingResult> actualDrivers = new HashSet(actual.get(EntityType.DRIVER));
        Set<SearchingResult> expectedDrivers = new HashSet(Arrays.asList(
                new SearchingResult(MdmDbUtils.DRIVER_DURABLE_KEY, UUID.fromString(MdmDbUtils.UUID_1)),
                new SearchingResult(MdmDbUtils.DRIVER_DURABLE_KEY, UUID.fromString(MdmDbUtils.UUID_4)),
                new SearchingResult(MdmDbUtils.DRIVER_DURABLE_KEY, UUID.fromString(MdmDbUtils.UUID_5))));
        assertEquals(expectedDrivers, actualDrivers);

        // Check Assets.
        Set<SearchingResult> actualAssets = new HashSet(actual.get(EntityType.ASSET));
        Set<SearchingResult> expectedList2 = new HashSet(Arrays.asList(
                new SearchingResult(MdmDbUtils.VEHICLE_DURABLE_KEY, UUID.fromString(MdmDbUtils.UUID_3)),
                new SearchingResult(MdmDbUtils.VEHICLE_DURABLE_KEY, UUID.fromString(MdmDbUtils.UUID_2))));
        assertEquals(expectedList2, actualAssets);
    }

    @Test
    public void singlePositionsCheckFailure() {
        // Prepare input data

        long driverId = 10000L;
        long assetId = 10000L;
        Position.PositionBuilder builder = new Position.PositionBuilder();

        builder.setDriverId(driverId)
                .setAssetId(assetId);
        List<Object> inputPositions = Arrays.asList(builder.build());

        // Request data from keyManager.
        Map<EntityType, List<SearchingResult>> actual = this.keyManager.keysSubstitutions(
                inputPositions,
                ApiVersion.API_1_0,
                MdmDbUtils.SUBSCRIPTIONS[1],
                EntityType.POSITION);

        // Check entity types.
        Set<EntityType> expected = new HashSet<>(Arrays.asList(EntityType.DRIVER, EntityType.ASSET));
        assertEquals(expected, actual.keySet());

        // Check Drivers.
        Set<SearchingResult> actualDrivers = new HashSet(actual.get(EntityType.DRIVER));
        assertEquals("Actual drivers numbers should be equal", 1, actualDrivers.size());

        // Check Assets.
        Set<SearchingResult> actualAssets = new HashSet(actual.get(EntityType.ASSET));
        assertEquals("Actual assets numbers should be equal", 1, actualAssets.size());
    }


    @Test
    public void aLotOfPositionsCheckFailure() {
        // Prepare input data
        List<Object> inputPositions = new ArrayList<>();

        long driverId = 10000L;
        long assetId = 10000L;
        int size = 5000;
        Position.PositionBuilder builder = new Position.PositionBuilder();

        for (int i = 0; i < size; ++i) {
            builder.setDriverId(driverId)
                    .setAssetId(assetId);
            inputPositions.add(builder.build());
            driverId++;
            assetId++;
        }

        // Request data from keyManager.
        Map<EntityType, List<SearchingResult>> actual = this.keyManager.keysSubstitutions(
                inputPositions,
                ApiVersion.API_1_0,
                MdmDbUtils.SUBSCRIPTIONS[1],
                EntityType.POSITION);

        // Check entity types.
        Set<EntityType> expected = new HashSet<>(Arrays.asList(EntityType.DRIVER, EntityType.ASSET));
        assertEquals(expected, actual.keySet());

        // Check Drivers.
        Set<SearchingResult> actualDrivers = new HashSet(actual.get(EntityType.DRIVER));
        assertEquals("Actual drivers numbers should be equal", size, actualDrivers.size());

        // Check Assets.
        Set<SearchingResult> actualAssets = new HashSet(actual.get(EntityType.ASSET));
        assertEquals("Actual assets numbers should be equal", size, actualAssets.size());
    }


    @Test
    public void emptyPositionsPositive() {
        // Prepare input data
        List<Object> inputPositions = new ArrayList<>();

        // Request data from keyManager.
        Map<EntityType, List<SearchingResult>> actual = this.keyManager.keysSubstitutions(
                inputPositions,
                ApiVersion.API_1_0,
                MdmDbUtils.SUBSCRIPTIONS[1],
                EntityType.POSITION);

        // Check entity types.
        assertEquals(Collections.EMPTY_SET, actual.keySet());
    }

}
