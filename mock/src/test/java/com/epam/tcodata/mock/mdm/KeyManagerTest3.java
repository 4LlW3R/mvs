package com.epam.tcodata.mock.mdm;

import com.epam.tcodata.mdm.IKeyFactory;
import com.epam.tcodata.mdm.IKeyManager;
import com.epam.tcodata.mdm.rules.DriverDimensionDeduplicatedRule;
import com.epam.tcodata.mock.mdm.base.impl.MockKeyFactory;
import com.epam.tcodata.mock.sql.dal.impl.mdm.MockMdmDaoFactory;
import com.epam.tcodata.mock.util.MockUtils;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.mix.dimension.Driver;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.mdm.ApiVersion;
import com.epam.tcodata.sql.dal.domain.mdm.KeyManagerVersion;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class KeyManagerTest3 {

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
        MdmDbUtils.populateTables(this.daoFactory, DriverDimensionDeduplicatedRule.class);
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

    @Test()
    public void driverNew() {
        Driver driver = new Driver();
        driver.setDriverId(10001L);
        driver.setEmployeeNumber("0010001");
        UUID uuid = this.keyManager.findOrCreate(driver, ApiVersion.API_1_0, MdmDbUtils.SUBSCRIPTIONS[0], EntityType.DRIVER).getSurrogateKey();

        assertNotNull(uuid);
        assertEquals(MdmDbUtils.UUID_1.length(), uuid.toString().length()); // we don't know the real UUID
        assertEquals(1, MdmDbUtils.existsInMapping(this.daoFactory, MdmDbUtils.DRIVER_ID, "10001", uuid)); // and it should appear in the mapping
        assertEquals(1, MdmDbUtils.existsInMapping(this.daoFactory, MdmDbUtils.EMPLOYEE_NUMBER, "0010001", uuid)); // and it should appear in the mapping
    }


    @Test()
    public void driverExistedWithFilledEmployeeNumber() {
        // check if the driver already exists
        Driver driver = new Driver();
        driver.setDriverId(Long.parseLong(MdmDbUtils.NATURAL_100));
        driver.setEmployeeNumber("888");
        assertEquals(0, MdmDbUtils.existsInMapping(this.daoFactory, MdmDbUtils.EMPLOYEE_NUMBER, "888", UUID.fromString(MdmDbUtils.UUID_1))); // non exist before
        UUID uuid = this.keyManager.findOrCreate(driver, ApiVersion.API_1_0, MdmDbUtils.SUBSCRIPTIONS[0], EntityType.DRIVER).getSurrogateKey();
        assertEquals(MdmDbUtils.UUID_1, uuid.toString());
        assertEquals(1, MdmDbUtils.existsInMapping(this.daoFactory, MdmDbUtils.DRIVER_ID, MdmDbUtils.NATURAL_100, uuid)); // and it should appear in the mapping
        assertEquals(1, MdmDbUtils.existsInMapping(this.daoFactory, MdmDbUtils.EMPLOYEE_NUMBER, "888", uuid)); // and it shouldn't appear in the mapping
    }

    @Test()
    public void driverExistedWithTheSameBadge() {
        // check if the driver already exists
        Driver driver = new Driver();
        driver.setDriverId(Long.parseLong(MdmDbUtils.NATURAL_600));
        driver.setEmployeeNumber(MdmDbUtils.BADGE_555);
        UUID uuid = this.keyManager.findOrCreate(driver, ApiVersion.API_1_0, MdmDbUtils.SUBSCRIPTIONS[0], EntityType.DRIVER).getSurrogateKey();
        assertNotNull(uuid);
        assertEquals(MdmDbUtils.UUID_4, uuid.toString());
        assertEquals(1, MdmDbUtils.existsInMapping(this.daoFactory, MdmDbUtils.DRIVER_ID, MdmDbUtils.NATURAL_600, uuid)); // and it should appear in the mapping
        assertEquals(1, MdmDbUtils.existsInMapping(this.daoFactory, MdmDbUtils.EMPLOYEE_NUMBER, MdmDbUtils.BADGE_555, uuid)); // and it shouldn't appear in the mapping
    }

}
