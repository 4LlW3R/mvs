package com.epam.tcodata.mock.sql.dal;

import com.epam.tcodata.mdm.rules.DriverDimensionRule;
import com.epam.tcodata.mock.mdm.MdmDbUtils;
import com.epam.tcodata.mock.sql.dal.impl.mdm.MockMdmDaoFactory;
import com.epam.tcodata.mock.sql.dal.impl.pumps.MockPumpsDaoFactory;
import com.epam.tcodata.mock.util.MockUtils;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.mdm.Relation;
import com.epam.tcodata.sql.dal.domain.mdm.Rule;
import com.epam.tcodata.sql.dal.domain.mdm.Step;
import com.epam.tcodata.sql.dal.domain.pumps.Account;
import com.epam.tcodata.sql.dal.service.mdm.IRelationService;
import com.epam.tcodata.sql.dal.service.mdm.IRuleService;
import com.epam.tcodata.sql.dal.service.mdm.IStepService;
import com.epam.tcodata.sql.dal.service.pumps.IAccountService;
import org.junit.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.epam.tcodata.sql.dal.IDaoFactory.service;
import static junit.framework.TestCase.assertNotNull;

public class DoubleMockDaoFactoryTest {

    private static final Path TEMP_DIR = Paths.get("temp", "test");
    private static final String EXT = ".json";

    private static IDaoFactory mdmDaoFactory = null;
    private static IRuleService ruleService = null;
    private static IStepService stepService = null;
    private static IRelationService relationService = null;

    private static IDaoFactory pumpDaoFactory = null;
    private static IAccountService accountService = null;

    @BeforeClass
    public static void setUp() throws Exception {
        ISecretStorage defaultMockSecretStorage = MockUtils.createDefaultMockSecretStorage();
        mdmDaoFactory = new MockMdmDaoFactory(defaultMockSecretStorage);
        ruleService = service(mdmDaoFactory, Rule.class);
        stepService = service(mdmDaoFactory, Step.class);
        relationService = service(mdmDaoFactory, Relation.class);

        pumpDaoFactory = new MockPumpsDaoFactory(defaultMockSecretStorage);
        accountService = service(pumpDaoFactory, Account.class);
    }

    @AfterClass
    public static void cleanUp() throws Exception {
        ruleService.close();
        stepService.close();
        mdmDaoFactory.close();
    }

    @Before
    public void init() throws Exception {
        MdmDbUtils.clearTables(mdmDaoFactory);
        MdmDbUtils.populateTables(mdmDaoFactory, DriverDimensionRule.class);
    }

    @After
    public void reset() throws Exception {
        /***  Default implementation ***/
    }

    @Test
    public void simultaneouslyUsingTest() {
        List<Account> accounts = accountService.readAll();
        List<Rule> rules = ruleService.readAll();

        assertNotNull(accounts);
        assertNotNull(rules);
    }
}
