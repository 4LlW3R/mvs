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

public class ServiceFactoryIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceFactoryIT.class);

    private static IDaoFactory daoFactory = null;
    private static IRuleService ruleService = null;
    private static IStepService stepService = null;
    private static IRelationService relationService = null;
    private static final String SUBS_100 = "subs.100";

    @BeforeClass
    public static void setUp() throws Exception {
        LOGGER.info("init...");
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
        MdmDbUtils.clearTables(daoFactory);
        MdmDbUtils.populateTables(daoFactory);
    }

    @After
    public void reset() throws Exception {
        /***  Default implementation ***/
    }

    @Test
    public void connection() {
        boolean opened = ruleService.checkConnection();
        assertEquals(true, opened);
    }

    @Test
    public void testKeyMappingService() {
        IKeyMappingService mappingService = service(daoFactory, KeyMapping.class);
        Optional<KeyMapping> result = mappingService.findByNaturalKey(EntityType.DRIVER, MdmDbUtils.DRIVER_ID, "100");
        assertEquals(MdmDbUtils.UUID_1, result.get().getSurrogateKey());
    }


    @Test
    public void testRuleService() throws Exception {
        Rule rule = new Rule(true, ApiVersion.API_1_0, KeyManagerVersion.VERSION_1_0, SUBS_100,
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
    public void testStepService() throws Exception {
        Rule rule = new Rule(true, ApiVersion.API_1_0, KeyManagerVersion.VERSION_1_0, SUBS_100,
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
    public void testRelationService() throws Exception {
        Rule rule = new Rule(true, ApiVersion.API_1_0, KeyManagerVersion.VERSION_1_0, SUBS_100,
                EntityType.POSITION);

        long id = ruleService.insert(rule);
        ruleService.read(id);

        List<Relation> relations = Arrays.asList(
                new Relation(id, EntityType.DRIVER, "driver", ""),
                new Relation(id, EntityType.ASSET, "vehicle", "")
        );

        relationService.insert(relations);

        List<Relation> relationsRead = relationService.readAll(id);
        assertEquals(relations.size(), relationsRead.size());
    }


    @Test
    public void readMany() {
        List<Rule> rules = ruleService.readAll();
        assertTrue(rules.size() > 0);
    }

    @Test
    public void deleteMany() {
        MdmDbUtils.clearTables(daoFactory);
        List<Rule> rules = ruleService.readAll();
        assertEquals(0, rules.size());
    }


    @Test
    public void filtered1() {
        Map<String, Object> filter1 = new HashMap<>();
        filter1.put(Rule.Fields.SUBSCRIPTION, MdmDbUtils.SUBSCRIPTIONS[1]);
        filter1.put(Rule.Fields.VERSION, KeyManagerVersion.VERSION_1_0);

        List<Rule> filtered1 = ruleService.readFiltered(filter1);

        assertEquals(1, filtered1.size());
        assertEquals(EntityType.ASSET, filtered1.get(0).getEntityType());
    }



    @Test
    public void filtered2() {
        Map<String, Object> filter2 = new HashMap<>();
        filter2.put(Rule.Fields.SUBSCRIPTION, MdmDbUtils.SUBSCRIPTIONS[1]);

        List<Rule> filtered2 = ruleService.readFiltered(filter2);
        assertEquals(3, filtered2.size());
    }
}
