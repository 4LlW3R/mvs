package com.epam.tcodata.mock.mdm;

import com.epam.tcodata.mdm.IKeyFactory;
import com.epam.tcodata.mdm.IKeyManager;
import com.epam.tcodata.mdm.SearchingResult;
import com.epam.tcodata.mdm.base.impl.KeyFactory;
import com.epam.tcodata.mdm.rules.DriverDimensionRule;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.mix.dimension.Driver;
import com.epam.tcodata.models.mix.fact.Position;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.factory.ISecretStorageFactory;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.mdm.ApiVersion;
import com.epam.tcodata.sql.dal.domain.mdm.KeyManagerVersion;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class KeyManagerIT {
    private IKeyFactory keyFactory = null;
    private IKeyManager keyManager = null;
    private IDaoFactory daoFactory = null;
    private ISecretStorageFactory secretStorageFactory = null;
    private ISecretStorage secretStorage = null;


    @Before
    public void setUp() throws Exception {
        this.secretStorageFactory = ISecretStorageFactory.createDefaultFactory();
        Properties properties = new Properties();
        this.secretStorage = this.secretStorageFactory.createSecretStorage(properties);
        this.keyFactory = new KeyFactory();
        this.keyManager = this.keyFactory.createKeyManager(KeyManagerVersion.VERSION_1_0, this.secretStorage);
        this.daoFactory = this.keyManager.getDaoFactory();

        MdmDbUtils.clearTables(this.daoFactory);
        MdmDbUtils.populateTables(this.daoFactory, DriverDimensionRule.class);
    }

    @Test
    public void subscriptions() {
        IKeyManager defaultManager = this.keyFactory.createKeyManager(KeyManagerVersion.VERSION_1_0, this.secretStorage);
        Set<String > actual = defaultManager.subscriptions();
        assertEquals(new HashSet<>(Arrays.asList("102", "*")), actual);
    }

    @Test()
    public void driverNew() {
        // check if the driver is real new
//        DbUtils.debugPrint("Before", daoFactory.historyService().readAll());

        Driver driver = new Driver();
        driver.setDriverId(10001L);
        UUID uuid = this.keyManager.findOrCreate(driver, ApiVersion.API_1_0, MdmDbUtils.SUBSCRIPTIONS[0], EntityType.DRIVER).getSurrogateKey();

//        DbUtils.debugPrint("After", daoFactory.historyService().readAll());

        assertNotNull(uuid);
        assertEquals(MdmDbUtils.UUID_1.length(), uuid.toString().length()); // we don't know the real UUID
        assertEquals(1, MdmDbUtils.existsInMapping(this.daoFactory, MdmDbUtils.DRIVER_ID, "10001", uuid)); // and it should appear in the mapping
    }


    @Test()
    public void driverExisted() {
        // check if the driver already exists
        Driver driver = new Driver();
        driver.setDriverId(Long.parseLong(MdmDbUtils.NATURAL_100));
        UUID uuid = this.keyManager.findOrCreate(driver, ApiVersion.API_1_0, MdmDbUtils.SUBSCRIPTIONS[0], EntityType.DRIVER).getSurrogateKey();
        assertNotNull(uuid);
        assertEquals(MdmDbUtils.UUID_1, uuid.toString());
        assertEquals(1, MdmDbUtils.existsInMapping(this.daoFactory, MdmDbUtils.DRIVER_ID, MdmDbUtils.NATURAL_100, uuid)); // and it should appear in the history
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

        Map<EntityType, List<SearchingResult>> actual = this.keyManager.keysSubstitution(position, ApiVersion.API_1_0,
                MdmDbUtils.SUBSCRIPTIONS[1], EntityType.POSITION);
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
}
