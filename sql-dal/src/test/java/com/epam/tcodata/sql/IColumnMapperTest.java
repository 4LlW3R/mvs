package com.epam.tcodata.sql;

import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.sql.dal.IColumnMapper;
import com.epam.tcodata.sql.dal.domain.mdm.ApiVersion;
import com.epam.tcodata.sql.dal.domain.mdm.KeyManagerVersion;
import com.epam.tcodata.sql.dal.domain.mdm.Rule;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static junit.framework.TestCase.*;

public class IColumnMapperTest {

    private Rule rule = null;
    private static final String SUBSCRIPTION = "subscription";
    private static final String VERSION = "=:version";
    private static final String ACTIVE = "active";
    private static final String API_VERSION = "apiVersion";

    @Before
    public void init() throws Exception {
        this.rule = new Rule(true, ApiVersion.API_1_0, KeyManagerVersion.VERSION_1_0,
                SUBSCRIPTION, EntityType.DRIVER);
        this.rule.setId(111);

    }

    @Test
    public void valuesWithIdTest() {
        List<Object> actual = this.rule.values(true);
        List<Object> expected = Arrays.asList(111L, true, ApiVersion.API_1_0, KeyManagerVersion.VERSION_1_0, SUBSCRIPTION, EntityType.DRIVER);
        assertEquals(expected, actual);
    }

    @Test
    public void valuesWithoutIdTest() {
        List<Object> actual = this.rule.values(false);
        List<Object> expected = Arrays.asList(true, ApiVersion.API_1_0, KeyManagerVersion.VERSION_1_0, SUBSCRIPTION, EntityType.DRIVER);
        assertEquals(expected, actual);
    }


    @Test
    public void bindWithIdTest() {
        Map<String, Object> expected = this.rule.bind(true);
        Map<String, Object> actual = new HashMap<>();
        actual.put(Rule.Fields.ID, 111L);
        actual.put(Rule.Fields.ACTIVE, true);
        actual.put(Rule.Fields.API_VERSION, ApiVersion.API_1_0);
        actual.put(Rule.Fields.VERSION, KeyManagerVersion.VERSION_1_0);
        actual.put(Rule.Fields.SUBSCRIPTION, SUBSCRIPTION);
        actual.put(Rule.Fields.ENTITY, EntityType.DRIVER);
        assertEquals(actual, expected);
    }

    @Test
    public void bindWithoutIdTest() {
        Map<String, Object> expected = this.rule.bind(false);
        Map<String, Object> actual = new HashMap<>();
        actual.put(Rule.Fields.ACTIVE, true);
        actual.put(Rule.Fields.API_VERSION, ApiVersion.API_1_0);
        actual.put(Rule.Fields.VERSION, KeyManagerVersion.VERSION_1_0);
        actual.put(Rule.Fields.SUBSCRIPTION, SUBSCRIPTION);
        actual.put(Rule.Fields.ENTITY, EntityType.DRIVER);
        assertEquals(actual, expected);
    }

    @Test
    public void matchesTest() {
        assertTrue(this.rule.matches(Collections.emptyMap()));
        Map<String, Object> filteredMap = new HashMap<>();
        filteredMap.put(Rule.Fields.ACTIVE, true);
        filteredMap.put(Rule.Fields.API_VERSION, ApiVersion.API_1_0);
        assertTrue(this.rule.matches(filteredMap));

        filteredMap = new HashMap<>();
        filteredMap.put(Rule.Fields.ACTIVE, true);
        filteredMap.put(Rule.Fields.API_VERSION, ApiVersion.NONE);
        assertFalse(this.rule.matches(filteredMap));

        filteredMap = new HashMap<>();
        filteredMap.put(Rule.Fields.ACTIVE, true);
        filteredMap.put(Rule.Fields.API_VERSION, ApiVersion.API_1_0);
        filteredMap.put("non_existing_field", true);
        assertTrue(this.rule.matches(filteredMap));
    }

    @Test
    public void columnsWithIdTest() {
        List<String> actual1 = this.rule.columns(true);
        List<String> actual2 = IColumnMapper.columns(Rule.class, true);
        List<String> expected = Arrays.asList(Rule.Fields.ID, Rule.Fields.ACTIVE, Rule.Fields.API_VERSION,
                Rule.Fields.VERSION, Rule.Fields.SUBSCRIPTION, Rule.Fields.ENTITY);
        assertEquals(expected, actual1);
        assertEquals(expected, actual2);
    }


    @Test
    public void columnsWithoutIdTest() {
        List<String> actual1 = this.rule.columns(false);
        List<String> actual2 = IColumnMapper.columns(Rule.class, false);
        List<String> expected = Arrays.asList(Rule.Fields.ACTIVE, Rule.Fields.API_VERSION,
                Rule.Fields.VERSION, Rule.Fields.SUBSCRIPTION, Rule.Fields.ENTITY);
        assertEquals(expected, actual1);
        assertEquals(expected, actual2);
    }

    @Test
    public void fieldsWithIdTest() {
        List<String> actual1 = this.rule.fields(true);
        List<String> actual2 = IColumnMapper.fields(Rule.class, true);
        List<String> expected = Arrays.asList(":id", ":active", ":apiVersion", ":version", ":subscription", ":entityType");
        assertEquals(expected, actual1);
        assertEquals(expected, actual2);
    }


    @Test
    public void fieldsWithoutIdTest() {
        List<String> actual1 = this.rule.fields(false);
        List<String> actual2 = IColumnMapper.fields(Rule.class, false);
        List<String> expected = Arrays.asList(":active", ":apiVersion", ":version", ":subscription", ":entityType");
        assertEquals(expected, actual1);
        assertEquals(expected, actual2);
    }


    @Test
    public void conditionsTest() {
        Map<String, Object>  filteredMap = new HashMap<>();
        filteredMap.put(Rule.Fields.API_VERSION, 1L);
        filteredMap.put(Rule.Fields.VERSION, 2L);
        List<String> actual1 = IColumnMapper.conditions(Rule.class, filteredMap);
        List<String> expected1 = Arrays.asList(Rule.Fields.API_VERSION + "=:apiVersion", Rule.Fields.VERSION + VERSION);
        assertEquals(expected1, actual1);

        filteredMap = new HashMap<>();
        filteredMap.put("non_existing", 1L);
        filteredMap.put(Rule.Fields.VERSION, 2L);
        List<String> actual2 = IColumnMapper.conditions(Rule.class, filteredMap);
        List<String> expected2 = Arrays.asList(Rule.Fields.VERSION + VERSION);
        assertEquals(expected2, actual2);

        filteredMap = new HashMap<>();
        filteredMap.put(Rule.Fields.API_VERSION, null);
        filteredMap.put(Rule.Fields.VERSION, KeyManagerVersion.VERSION_1_0);
        List<String> actual3 = IColumnMapper.conditions(Rule.class, filteredMap);
        List<String> expected3 = Arrays.asList(Rule.Fields.API_VERSION + " IS NULL", Rule.Fields.VERSION + VERSION);
        assertEquals(expected3, actual3);
    }

    @Test
    public void mapToExistingTest() {
        Map<String, Object> filteredMap = new LinkedHashMap<>();
        filteredMap.put(Rule.Fields.ACTIVE, false);
        filteredMap.put(Rule.Fields.API_VERSION, ApiVersion.API_1_0);
        filteredMap.put(Rule.Fields.ENTITY, EntityType.POSITION);

        Map<String, Object> expectedMap = new LinkedHashMap<>();
        expectedMap.put(ACTIVE, false);
        expectedMap.put(API_VERSION, ApiVersion.API_1_0);
        expectedMap.put("entityType", EntityType.POSITION);
        Map<String, Object> actual1 = IColumnMapper.mapToExisting(Rule.class, filteredMap);
        assertEquals(expectedMap, actual1);

        filteredMap = new LinkedHashMap<>();
        filteredMap.put(Rule.Fields.ACTIVE, false);
        filteredMap.put(Rule.Fields.API_VERSION, ApiVersion.API_1_0);
        filteredMap.put(Rule.Fields.ENTITY, EntityType.POSITION);
        filteredMap.put("non_existing", 100L);
        expectedMap = new LinkedHashMap<>();
        expectedMap.put(ACTIVE, false);
        expectedMap.put(API_VERSION, ApiVersion.API_1_0);
        expectedMap.put("entityType", EntityType.POSITION);
        Map<String, Object> actual2 = IColumnMapper.mapToExisting(Rule.class, filteredMap);
        assertEquals(expectedMap, actual2);

        filteredMap = new LinkedHashMap<>();
        filteredMap.put(Rule.Fields.ACTIVE, false);
        filteredMap.put(Rule.Fields.API_VERSION, null);
        expectedMap = new LinkedHashMap<>();
        expectedMap.put(ACTIVE, false);
        expectedMap.put(API_VERSION, null);
        Map<String, Object> actual3 = IColumnMapper.mapToExisting(Rule.class, filteredMap);
        assertEquals(expectedMap, actual3);
    }
}
