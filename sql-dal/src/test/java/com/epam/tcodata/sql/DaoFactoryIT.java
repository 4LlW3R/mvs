package com.epam.tcodata.sql;

import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.Secret;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.mdm.Rule;
import com.epam.tcodata.sql.dal.domain.mdm.*;
import com.epam.tcodata.sql.dal.impl.mdm.MdmDaoFactory;
import com.epam.tcodata.sql.dal.service.mdm.IKeyMappingService;
import com.epam.tcodata.sql.dal.service.mdm.IRelationService;
import com.epam.tcodata.sql.dal.service.mdm.IRuleService;
import com.epam.tcodata.sql.dal.service.mdm.IStepService;
import com.epam.tcodata.sql.dal.util.MdmDbUtils;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.epam.tcodata.sql.dal.IDaoFactory.service;
import static junit.framework.TestCase.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DaoFactoryIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(DaoFactoryIT.class);

    private static IDaoFactory daoFactory = null;
    private static IRuleService ruleService = null;
    private static IStepService stepService = null;
    private static IRelationService relationService = null;


    @BeforeClass
    public static void setUp() throws Exception {
        ISecretStorage secretStorage = mock(ISecretStorage.class);
        when(secretStorage.retrieveSecret(Secret.Sql.MDM.user)).thenReturn("user");
        when(secretStorage.retrieveSecret(Secret.Sql.MDM.password)).thenReturn("password");
        daoFactory = new MdmDaoFactory(secretStorage);
        ruleService = service(daoFactory, Rule.class);
        stepService = service(daoFactory, Step.class);
        relationService = service(daoFactory, Relation.class);
    }

    @AfterClass
    public static void cleanUp() throws Exception {
        ruleService.close();
        stepService.close();
        relationService.close();
        daoFactory.close();
    }

    @Before
    public void init() throws Exception {
        LOGGER.info("Initializing");
        MdmDbUtils.clearTables(daoFactory);
        MdmDbUtils.populateTables(daoFactory);
    }

    @After
    public void reset() throws Exception {
        /***  Default implementation ***/
    }

    @Test
    public void testKeyMappingService() {
        IKeyMappingService mappingService = service(daoFactory, KeyMapping.class);
        Optional<KeyMapping> result = mappingService.findByNaturalKey(EntityType.DRIVER, MdmDbUtils.DRIVER_ID, "100");
        assertEquals(MdmDbUtils.UUID_1, result.get().getSurrogateKey());
    }

    @Test
    public void testRuleService() throws Exception {
        Rule rule = new Rule(true, ApiVersion.API_1_0, KeyManagerVersion.VERSION_1_0, "subs.100",
                EntityType.DRIVER);

        long id = ruleService.insert(rule);
        Optional<Rule> read = ruleService.read(id);
        assertTrue(read.isPresent());
        Rule newRule = read.get();

        newRule.setApiVersion(ApiVersion.API_2_0);
        ruleService.update(newRule);

        Optional<Rule> readOnce = ruleService.read(id);
        assertEquals(ApiVersion.API_2_0, readOnce.get().getApiVersion());

        ruleService.delete(newRule);
        Optional<Rule> readAgain = ruleService.read(id);
        assertFalse(readAgain.isPresent());
    }

    @Test
    public void testEmptyFilter() {
        // should return all records
        List<Rule> all = ruleService.readAll();
        List<Rule> filtered = ruleService.readFiltered(Collections.emptyMap());

        assertTrue(all.size() > 0);
        assertTrue(filtered.size() > 0);
        assertEquals(all.size(), filtered.size());
    }

    @Test
    public void testNonEmptyButUnrelevantFilter() {

        Map<String, Object> filter = new HashMap<>();
        filter.put("somethingVeryStrange", 100);

        // should return all records
        List<Rule> all = ruleService.readAll();
        List<Rule> filtered = ruleService.readFiltered(filter);

        assertTrue(all.size() > 0);
        assertTrue(filtered.size() > 0);
        assertEquals(all.size(), filtered.size());
    }

    @Test
    public void testRelevantFilter() {
        Map<String, Object> filterMapVar = new HashMap<>();
        filterMapVar.put(Rule.Fields.API_VERSION, ApiVersion.API_2_0);
        List<Rule> filtered1 = ruleService.readFiltered(filterMapVar);
        assertEquals(1, filtered1.size());
        assertEquals(KeyManagerVersion.NONE, filtered1.get(0).getVersion());

        filterMapVar = new HashMap<>();
        filterMapVar.put(Rule.Fields.API_VERSION, ApiVersion.API_2_0);
        filterMapVar.put(Rule.Fields.VERSION, KeyManagerVersion.NONE);
        List<Rule> filtered2 = ruleService.readFiltered(filterMapVar);
        assertEquals(1, filtered2.size());
        assertEquals(KeyManagerVersion.NONE, filtered2.get(0).getVersion());

        filterMapVar = new HashMap<>();
        filterMapVar.put(Rule.Fields.API_VERSION, ApiVersion.API_2_0);
        filterMapVar.put(Rule.Fields.VERSION, "doesn't exist");
        List<Rule> filtered3 = ruleService.readFiltered(filterMapVar);
        assertEquals(0, filtered3.size());
    }


    @Test
    public void testStepService() throws Exception {
        Rule rule = new Rule(true, ApiVersion.API_1_0, KeyManagerVersion.VERSION_1_0, "subs.100",
                EntityType.DRIVER);

        long id = ruleService.insert(rule);
        ruleService.read(id);

        List<Step> steps = Arrays.asList(new Step(1, "CUSTOM"), new Step(2, "CUSTOM"), new Step(3, "GENERATE"));
        steps.forEach(e -> {
            e.setParentId(id);
            e.parameter(2, "first param");
        });

        stepService.insert(steps);

        List<Step> stepsRead = stepService.readAll(id);
        assertEquals(steps.size(), stepsRead.size());
    }

    @Test
    public void filtered() {
        Map<String, Object> filter1 = new HashMap<>();
        filter1.put(Rule.Fields.VERSION, KeyManagerVersion.VERSION_1_0);
        filter1.put(Rule.Fields.SUBSCRIPTION, MdmDbUtils.SUBSCRIPTIONS[1]);

        List<Rule> filtered1 = ruleService.readFiltered(filter1);
        assertEquals(1, filtered1.size());
        assertEquals(KeyManagerVersion.VERSION_1_0, filtered1.get(0).getVersion());

        Map<String, Object> filter2 = new HashMap<>();
        filter2.put(Rule.Fields.SUBSCRIPTION, MdmDbUtils.SUBSCRIPTIONS[1]);

        List<Rule> filtered2 = ruleService.readFiltered(filter2);
        assertEquals(3, filtered2.size());
    }


    @Test
    public void insertMany() {
        List<Rule> rules = Arrays.asList(
                new Rule(false, ApiVersion.API_2_0, KeyManagerVersion.VERSION_1_0, MdmDbUtils.SUBSCRIPTIONS[0], EntityType.DRIVER),
                new Rule(false, ApiVersion.API_2_0, KeyManagerVersion.VERSION_1_0, MdmDbUtils.SUBSCRIPTIONS[1], EntityType.DRIVER)
        );
        ruleService.insert(rules);

        List<Rule> read = null;
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put(Rule.Fields.ACTIVE, false);
        tempMap.put(Rule.Fields.VERSION, KeyManagerVersion.VERSION_1_0);
        tempMap.put(Rule.Fields.SUBSCRIPTION, MdmDbUtils.SUBSCRIPTIONS[0]);
        read = ruleService.readFiltered(tempMap);
        assertEquals(1, read.size());

        tempMap = new HashMap<>();
        tempMap.put(Rule.Fields.ACTIVE, false);
        tempMap.put(Rule.Fields.VERSION, KeyManagerVersion.VERSION_1_0);
        tempMap.put(Rule.Fields.SUBSCRIPTION, MdmDbUtils.SUBSCRIPTIONS[1]);
        read = ruleService.readFiltered(tempMap);
        assertEquals(1, read.size());
    }

    @SuppressWarnings("unused")
    @Test
    public void insertEmptyList() {
        ruleService.insert(Collections.emptyList());
    }


    @Test
    public void updateMany() {

        List<Rule> list = ruleService.readAll();
        list.forEach(r -> r.setApiVersion(ApiVersion.NONE)); // just for tests
        ruleService.update(list);

        list.forEach(r -> r.setApiVersion(ApiVersion.NONE));
        ruleService.update(list);

        List<Rule> readAgain = ruleService.readAll();
        boolean actual = readAgain.stream().allMatch(r -> r.getApiVersion().equals(ApiVersion.NONE));

        assertEquals(true, actual);
    }
}
