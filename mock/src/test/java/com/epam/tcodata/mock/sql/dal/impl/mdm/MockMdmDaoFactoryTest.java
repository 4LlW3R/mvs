package com.epam.tcodata.mock.sql.dal.impl.mdm;

import com.epam.tcodata.mdm.rules.DriverDimensionRule;
import com.epam.tcodata.mock.mdm.MdmDbUtils;
import com.epam.tcodata.mock.util.MockUtils;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.mdm.Rule;
import com.epam.tcodata.sql.dal.domain.mdm.*;
import com.epam.tcodata.sql.dal.service.mdm.IKeyMappingService;
import com.epam.tcodata.sql.dal.service.mdm.IRelationService;
import com.epam.tcodata.sql.dal.service.mdm.IRuleService;
import com.epam.tcodata.sql.dal.service.mdm.IStepService;
import junit.framework.TestCase;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static com.epam.tcodata.sql.dal.IDaoFactory.service;
import static junit.framework.TestCase.*;

public class MockMdmDaoFactoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockMdmDaoFactoryTest.class);

    private static final Path TEMP_DIR = Paths.get("temp", "test");
    private static final String EXT = ".json";

    private static IDaoFactory daoFactory = null;
    private static IRuleService ruleService = null;
    private static IStepService stepService = null;
    private static IRelationService relationService = null;


    @BeforeClass
    public static void setUp() throws Exception {
        ISecretStorage defaultMockSecretStorage = MockUtils.createDefaultMockSecretStorage();
        daoFactory = new MockMdmDaoFactory(defaultMockSecretStorage);
        ruleService = service(daoFactory, Rule.class);
        stepService = service(daoFactory, Step.class);
        relationService = service(daoFactory, Relation.class);
    }

    @AfterClass
    public static void cleanUp() throws Exception {
        ruleService.close();
        stepService.close();
        daoFactory.close();
    }

    @Before
    public void init() throws Exception {
        MdmDbUtils.clearTables(daoFactory);
        MdmDbUtils.populateTables(daoFactory, DriverDimensionRule.class);
    }

    @After
    public void reset() throws Exception {
        /***  Default implementation ***/
    }

    @Test
    public void daoFactoryWithProperSecretStorageTest() {
        ISecretStorage defaultMockSecretStorage = MockUtils.createDefaultMockSecretStorage();
        IDaoFactory testFactory2 = new MockMdmDaoFactory(defaultMockSecretStorage);
        assertNotNull(testFactory2);
    }

    @Test
    public void keyMappingServiceTest() {
        IKeyMappingService mappingService = service(daoFactory, KeyMapping.class);
        Optional<KeyMapping> result = mappingService.findByNaturalKey(EntityType.DRIVER, MdmDbUtils.DRIVER_ID, "100");
        TestCase.assertEquals(MdmDbUtils.UUID_1, result.get().getSurrogateKey());
    }

    @Test
    public void keyMappingServiceTest_multiplyKeys() {
        IKeyMappingService mappingService = service(daoFactory, KeyMapping.class);
        KeyMapping map1 = new KeyMapping(EntityType.DRIVER, "driverId", "1000", MdmDbUtils.UUID_1);
        KeyMapping map2 = new KeyMapping(EntityType.DRIVER, "driverId", "1000", MdmDbUtils.UUID_2);
        mappingService.insert(Arrays.asList(map1, map2));

        Set<String> naturalKeyValues = new HashSet<>(Arrays.asList("1000", "2000"));
        List<KeyMapping> result = mappingService.findByNaturalKey(EntityType.DRIVER, MdmDbUtils.DRIVER_ID, naturalKeyValues);

        List<KeyMapping> keyMappings = mappingService.readAll();

        TestCase.assertEquals(Arrays.asList(MdmDbUtils.UUID_1, MdmDbUtils.UUID_2),
                result.stream().map(m -> m.getSurrogateKey()).collect(Collectors.toList()));
    }

    @Test
    public void ruleServiceTest() throws Exception {
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
    public void emptyFilterTest() {
        // should return all records
        List<Rule> all = ruleService.readAll();
        List<Rule> filtered = ruleService.readFiltered(Collections.emptyMap());

        assertTrue(all.size() > 0);
        assertTrue(filtered.size() > 0);
        assertEquals(all.size(), filtered.size());
    }

    @Test
    public void nonEmptyButUnrelevantFilterTest() {

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
    public void stepServiceTest() throws Exception {
        Rule rule = new Rule(true, ApiVersion.API_1_0, KeyManagerVersion.VERSION_1_0, "subs.100",
                EntityType.DRIVER);

        long id = ruleService.insert(rule);
        Optional<Rule> read = ruleService.read(id);

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
    public void filteredTest() {
        Map<String, Object> filter1 = new HashMap<>();
        filter1.put(Rule.Fields.API_VERSION, ApiVersion.API_2_0);
        filter1.put(Rule.Fields.SUBSCRIPTION, MdmDbUtils.SUBSCRIPTIONS[1]);
        List<Rule> filtered1 = ruleService.readFiltered(filter1);
        assertEquals(1, filtered1.size());
        assertEquals(KeyManagerVersion.NONE, filtered1.get(0).getVersion());

        Map<String, Object> filter2 = new HashMap<>();
        filter2.put(Rule.Fields.SUBSCRIPTION, MdmDbUtils.SUBSCRIPTIONS[1]);
        List<Rule> filtered2 = ruleService.readFiltered(filter2);
        assertEquals(3, filtered2.size());
    }

    @Test
    public void updateManyTest() {

        List<Rule> list = ruleService.readAll();
        list.forEach(r -> r.setApiVersion(ApiVersion.API_2_0));
        ruleService.update(list);

        List<Rule> readAgain = ruleService.readAll();
        boolean actual = readAgain.stream().allMatch(r -> r.getApiVersion().equals(ApiVersion.API_2_0));

        assertEquals(true, actual);
    }


    @Test
    public void backupTest() throws Exception {
        File temp = TEMP_DIR.toFile();
        if (!temp.exists()) {
            temp.mkdir();
        }

        File[] files = TEMP_DIR.toFile().listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
        files = TEMP_DIR.toFile().listFiles();
        assertTrue(files == null || files.length == 0);
        daoFactory.backup(TEMP_DIR);

        files = TEMP_DIR.toFile().listFiles();
        List<String> actual = Arrays.stream(files)
                .map(f -> f.getName())
                .sorted()
                .collect(Collectors.toList());

        List<String> expected = Arrays.asList(
                KeyMapping.class.getName() + EXT,
                Relation.class.getName() + EXT,
                Rule.class.getName() + EXT,
                Step.class.getName() + EXT
        );
        assertEquals(expected, actual);
    }

    @Test
    public void restoreTest() throws Exception {
        File temp = TEMP_DIR.toFile();
        if (!temp.exists()) {
            temp.mkdir();
        }
        daoFactory.backup(TEMP_DIR);

        List<Relation> relationsBefore = relationService.readAll();
        List<Rule> rulesBefore = ruleService.readAll();
        List<Step> stepsBefore = stepService.readAll();

        MdmDbUtils.clearTables(daoFactory);

        daoFactory.restore(TEMP_DIR);

        List<Relation> relationsAfter = relationService.readAll();
        List<Rule> rulesAfter = ruleService.readAll();
        List<Step> stepsAfter = stepService.readAll();

        assertEquals(toStringsList(relationsBefore), toStringsList(relationsAfter));
        assertEquals(toStringsList(rulesBefore), toStringsList(rulesAfter));
        assertEquals(toStringsList(stepsBefore), toStringsList(stepsAfter));
    }

    private static List<String> toStringsList(List<?> list) {
        return list.stream().map(Object::toString).collect(Collectors.toList());
    }
}
