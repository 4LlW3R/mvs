package com.epam.tcodata.mock.mdm;

import com.epam.tcodata.mdm.rules.DriverDimensionRule;
import com.epam.tcodata.mdm.rules.RuleType;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.mdm.*;
import com.epam.tcodata.sql.dal.service.mdm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.epam.tcodata.sql.dal.IDaoFactory.service;

public class MdmDbUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MdmDbUtils.class);

    public static final String[] SUBSCRIPTIONS = new String[] {"101", "102"};
    public static final Set<String> SUBS_SET = new HashSet<>(Arrays.asList(SUBSCRIPTIONS));

    public static final String UUID_1 = "00000000-0000-0001-0001-000000000001";
    public static final String UUID_2 = "00000000-0000-0002-0002-000000000002";
    public static final String UUID_3 = "00000000-0000-0003-0003-000000000003";
    public static final String UUID_4 = "00000000-0000-0004-0004-000000000004";
    public static final String UUID_5 = "00000000-0000-0005-0005-000000000005";
    public static final String NATURAL_100 = "100";
    public static final String NATURAL_200 = "200";
    public static final String NATURAL_300 = "300";
    public static final String NATURAL_400 = "400";
    public static final String NATURAL_500 = "500";
    public static final String NATURAL_600 = "600";

    public static final String BADGE_555 = "555";
    public static final String BADGE_666 = "666";

    public static final String VEHICLE_ID = "assetId";
    public static final String DRIVER_ID = "driverId";
    public static final String EMPLOYEE_NUMBER = "employeeNumber";


    public static final String VEHICLE_DURABLE_KEY = "vehicleDurableKey";
    public static final String DRIVER_DURABLE_KEY = "driverDurableKey";

    private MdmDbUtils() {
    }

    public static void clearTables(IDaoFactory factory) {

        IStepService stepService = service(factory, Step.class);
        IRelationService relationService = service(factory, Relation.class);
        IRuleService ruleService = service(factory, Rule.class);
        IKeyMappingService keyMappingService = service(factory, KeyMapping.class);

        stepService.deleteAll();
        relationService.deleteAll();
        ruleService.deleteAll();
        keyMappingService.deleteAll();
    }

    public static void populateTables(IDaoFactory daoFactory, Class<?> driverDimensioRuleClass) {

        Rule rule1 = new Rule(true, ApiVersion.API_1_0, KeyManagerVersion.VERSION_1_0, "*", EntityType.DRIVER);
        Rule rule2 = new Rule(true, ApiVersion.API_1_0, KeyManagerVersion.VERSION_1_0, SUBSCRIPTIONS[1], EntityType.POSITION);
        Rule rule3 = new Rule(true, ApiVersion.API_1_0, KeyManagerVersion.NONE, SUBSCRIPTIONS[1], EntityType.DRIVER);
        Rule rule4 = new Rule(true, ApiVersion.API_2_0, KeyManagerVersion.NONE, SUBSCRIPTIONS[1], EntityType.ASSET);
        Rule rule5 = new Rule(true, ApiVersion.API_1_0, KeyManagerVersion.VERSION_1_0, "*", EntityType.ASSET);

        populateOneRule(daoFactory, rule1,
                Arrays.asList(
                        new Step(-1, RuleType.CUSTOM.name(), DRIVER_ID, 1, driverDimensioRuleClass.getName(), DRIVER_ID, EMPLOYEE_NUMBER),
                        new Step(-1, RuleType.GENERATE.name(), DRIVER_ID, 2, "")),
                Arrays.asList()
        );
        populateOneRule(daoFactory, rule2,
                Arrays.asList(), // we don't generate surrogate keys for facts yet
                Arrays.asList(
                        new Relation(-1, EntityType.DRIVER, DRIVER_ID, DRIVER_DURABLE_KEY),
                        new Relation(-1, EntityType.ASSET, VEHICLE_ID, VEHICLE_DURABLE_KEY)
                )
        );

        populateOneRule(daoFactory, rule3, Collections.emptyList(), Collections.emptyList());
        populateOneRule(daoFactory, rule4, Collections.emptyList(), Collections.emptyList());
        populateOneRule(daoFactory, rule5, Collections.emptyList(), Collections.emptyList());

        KeyMapping map1 = new KeyMapping(EntityType.DRIVER, DRIVER_ID, NATURAL_100, UUID_1);
        KeyMapping map2 = new KeyMapping(EntityType.DRIVER, DRIVER_ID, NATURAL_200, UUID_1);
        KeyMapping map3 = new KeyMapping(EntityType.ASSET, VEHICLE_ID, NATURAL_300, UUID_2);
        KeyMapping map4 = new KeyMapping(EntityType.ASSET, VEHICLE_ID, NATURAL_400, UUID_3);
        KeyMapping map5 = new KeyMapping(EntityType.DRIVER, DRIVER_ID, NATURAL_500, UUID_4);
        KeyMapping map6 = new KeyMapping(EntityType.DRIVER, DRIVER_ID, NATURAL_600, UUID_5);
        KeyMapping map7 = new KeyMapping(EntityType.DRIVER, EMPLOYEE_NUMBER, BADGE_555, UUID_4);

        populateMapping(daoFactory, map1);
        populateMapping(daoFactory, map2);
        populateMapping(daoFactory, map3);
        populateMapping(daoFactory, map4);
        populateMapping(daoFactory, map5);
        populateMapping(daoFactory, map6);
        populateMapping(daoFactory, map7);
    }

    public static long populateOneRule(IDaoFactory factory, Rule rule, List<Step> steps, List<Relation> relations) {
        IRuleService ruleService = service(factory, Rule.class);
        long id = ruleService.insert(rule);

        for (Step step : steps) {
            step.setParentId(id);
        }
        IStepService stepService = service(factory, Step.class);
        stepService.insert(steps);

        for (Relation relation : relations) {
            relation.setParentId(id);
        }
        IRelationService relationService = service(factory, Relation.class);
        relationService.insert(relations);

        return id;
    }

    public static long populateMapping(IDaoFactory factory, KeyMapping keyMapping) {
        IKeyMappingService mappingService = service(factory, KeyMapping.class);
        long id = mappingService.insert(keyMapping);
        return id;
    }

    public static int existsInMapping(IDaoFactory daoFactory, String naturalKeyName, String naturalKey, UUID uuid) {
        IKeyMappingService mappingService = service(daoFactory, KeyMapping.class);
        List<KeyMapping> history = mappingService.readFiltered(new HashMap<String, Object>() {{
            put(KeyMapping.Fields.SURROGATE_KEY, String.valueOf(uuid));
            put(KeyMapping.Fields.KEY_NAME, naturalKeyName);
            put(KeyMapping.Fields.NATURAL_KEY, naturalKey);
        }});
        return history.size();
    }
}
