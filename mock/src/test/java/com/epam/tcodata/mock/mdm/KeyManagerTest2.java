package com.epam.tcodata.mock.mdm;

import com.epam.tcodata.mdm.IKeyFactory;
import com.epam.tcodata.mdm.IKeyManager;
import com.epam.tcodata.mdm.base.impl.KeyManager;
import com.epam.tcodata.mdm.rules.DriverDimensionRule;
import com.epam.tcodata.mdm.rules.RuleType;
import com.epam.tcodata.mdm.rules.VehicleDimensionRule;
import com.epam.tcodata.mock.mdm.base.impl.MockKeyFactory;
import com.epam.tcodata.mock.sql.dal.impl.mdm.MockMdmDaoFactory;
import com.epam.tcodata.mock.util.MockUtils;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.mdm.*;
import com.epam.tcodata.sql.dal.service.mdm.IStepService;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.epam.tcodata.sql.dal.IDaoFactory.service;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class KeyManagerTest2 {
    private KeyManager keyManager = null;
    private IKeyFactory keyFactory = null;
    private ISecretStorage secretStorage = null;
    private IDaoFactory daoFactory = null;

    Rule rule1 = null;
    Rule rule2 = null;
    Rule rule3 = null;

    private List<Step> expected1 = null;
    private List<Step> expected2 = null;
    private List<Step> expected3 = null;
    private List<Relation> expectedRelations = null;

    @Before
    public void setUp() throws Exception {
        this.keyFactory = MockKeyFactory.instance();
        this.secretStorage = MockUtils.createDefaultMockSecretStorage();
        this.daoFactory = new MockMdmDaoFactory(this.secretStorage);

        MdmDbUtils.clearTables(this.daoFactory);
        this.rule1 = new Rule(true, ApiVersion.API_1_0, KeyManagerVersion.VERSION_1_0, MdmDbUtils.SUBSCRIPTIONS[0],
                EntityType.DRIVER);
        Step step1 = new Step(-1, RuleType.CUSTOM.name(), MdmDbUtils.DRIVER_ID, 1, DriverDimensionRule.class.getName(), MdmDbUtils.DRIVER_ID, MdmDbUtils.DRIVER_DURABLE_KEY);
        Step step2 = new Step(-1, RuleType.GENERATE.name(), MdmDbUtils.DRIVER_ID, 2, "");
        this.rule2 = new Rule(true, ApiVersion.API_1_0, KeyManagerVersion.VERSION_1_0, MdmDbUtils.SUBSCRIPTIONS[1],
                EntityType.ASSET);
        Step step3 = new Step(-1, RuleType.CUSTOM.name(), MdmDbUtils.VEHICLE_ID, 1, VehicleDimensionRule.class.getName());
        Step step4 = new Step(-1, RuleType.GENERATE.name(), MdmDbUtils.VEHICLE_ID, 2, "");

        this.rule3 = new Rule(true, ApiVersion.API_2_0, KeyManagerVersion.VERSION_1_0, IKeyManager.ANY_SUBS,
                EntityType.ASSET);
        Step step5 = new Step(-1, RuleType.CUSTOM.name(), MdmDbUtils.VEHICLE_ID, 4, VehicleDimensionRule.class.getName());
        Step step6 = new Step(-1, RuleType.GENERATE.name(), MdmDbUtils.VEHICLE_ID, 5, "");

        Relation relation5 = new Relation(-1, EntityType.DRIVER, MdmDbUtils.DRIVER_ID, MdmDbUtils.DRIVER_DURABLE_KEY);
        Relation relation6 = new Relation(-1, EntityType.ASSET, MdmDbUtils.VEHICLE_ID, MdmDbUtils.VEHICLE_DURABLE_KEY);

        MdmDbUtils.populateOneRule(this.daoFactory, rule1, Arrays.asList(step1, step2), Arrays.asList());
        MdmDbUtils.populateOneRule(this.daoFactory, rule2, Arrays.asList(step3, step4), Arrays.asList());
        MdmDbUtils.populateOneRule(this.daoFactory, rule3, Arrays.asList(step5, step6), Arrays.asList(relation5, relation6));

        this.expected1 = Arrays.asList(step1, step2);
        this.expected2 = Arrays.asList(step3, step4);
        this.expected3 = Arrays.asList(step5, step6);

        this.expectedRelations = Arrays.asList(relation5, relation6);

        this.keyManager = new KeyManager(KeyManagerVersion.VERSION_1_0, this.daoFactory);
    }

    @Test
    public void db() {
        IStepService stepService = service(this.daoFactory, Step.class);
        List<Step> all = stepService.readAll();
        assertEquals(6, all.size());

        List<Step> steps1 = stepService.readAll(this.rule1.getId());
        assertEquals(2, steps1.size());

        List<Step> steps2 = stepService.readAll(this.rule2.getId());
        assertEquals(2, steps2.size());
    }

    @Test
    public void steps1() {
        List<Step> steps = this.keyManager.steps(ApiVersion.API_1_0, MdmDbUtils.SUBSCRIPTIONS[0], EntityType.DRIVER);
        assertEquals(this.expected1, steps);
    }

    @Test
    public void steps2() {
        List<Step> steps = this.keyManager.steps(ApiVersion.API_1_0, MdmDbUtils.SUBSCRIPTIONS[1], EntityType.ASSET);
        assertEquals(this.expected2, steps);
    }

    @Test
    public void steps3() {
        List<Step> steps = this.keyManager.steps(ApiVersion.API_1_0, MdmDbUtils.SUBSCRIPTIONS[1], EntityType.DRIVER);
        assertNull(steps);
    }

    @Test
    public void steps4() {
        List<Step> steps = this.keyManager.steps(ApiVersion.API_1_0, MdmDbUtils.SUBSCRIPTIONS[0], EntityType.ASSET);
        assertNull(steps);
    }

    @Test
    public void steps6() {
        List<Step> steps = this.keyManager.steps(ApiVersion.API_2_0, MdmDbUtils.SUBSCRIPTIONS[0], EntityType.DRIVER);
        assertNull(steps);
    }

    @Test
    public void steps7() {
        List<Step> steps = this.keyManager.steps(ApiVersion.API_2_0, MdmDbUtils.SUBSCRIPTIONS[0], EntityType.ASSET);
        assertEquals(this.expected3, steps);
    }

    @Test
    public void steps8() {
        List<Step> steps = this.keyManager.steps(ApiVersion.API_2_0, MdmDbUtils.SUBSCRIPTIONS[1], EntityType.ASSET);
        assertEquals(this.expected3, steps);
    }

    @Test
    public void steps9() {
        List<Step> steps = this.keyManager.steps(ApiVersion.API_2_0, "777", EntityType.ASSET);
        assertEquals(this.expected3, steps);
    }


    @Test
    public void relations0() {
        List<Relation> relations = this.keyManager.relations(ApiVersion.API_2_0, MdmDbUtils.SUBSCRIPTIONS[0], EntityType.DRIVER);
        assertNull(relations);
    }

    @Test
    public void relations1() {
        List<Relation> relations = this.keyManager.relations(ApiVersion.API_2_0, MdmDbUtils.SUBSCRIPTIONS[0], EntityType.ASSET);
        assertEquals(this.expectedRelations, relations);
    }

    @Test
    public void relations2() {
        List<Relation> relations = this.keyManager.relations(ApiVersion.API_2_0, MdmDbUtils.SUBSCRIPTIONS[1], EntityType.ASSET);
        assertEquals(this.expectedRelations, relations);
    }

    @Test
    public void relations3() {
        List<Relation> relations = this.keyManager.relations(ApiVersion.API_2_0, "777", EntityType.ASSET);
        assertEquals(this.expectedRelations, relations);
    }
}
