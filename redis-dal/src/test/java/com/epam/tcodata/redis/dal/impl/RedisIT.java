package com.epam.tcodata.redis.dal.impl;

import com.epam.tcodata.redis.dal.IRedis;
import com.epam.tcodata.redis.dal.RedisConfig;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.factory.ISecretStorageFactory;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class RedisIT {
    private static IRedis redis;
    private static final String ROAD_CONDITIONS_HM = "roadConditionsHashMap";

    @BeforeClass
    public static void setUp() throws Exception {
        ISecretStorageFactory factory = ISecretStorageFactory.createDefaultFactory();
        ISecretStorage secretStorage = factory.createSecretStorage(new Properties());

        redis = new Redis(RedisConfig.AREAS, secretStorage);
    }

    @Test
    @Ignore
    public void dropTest() {
        redis.drop(ROAD_CONDITIONS_HM);
    }

    @Test
    @Ignore
    public void setMapTest() {
        Map<String, String> map = new HashMap<>();
        String value = "{\n" +
                "\t\"Domain\": \"rcdb.tengizchevroil.com\",\n" +
                "\t\"EntityType\": \"RCDB\",\n" +
                "\t\"Id\": \"cea96e47-c0e8-48da-bea2-a5cacf91b83c\",\n" +
                "\t\"SchemaVersion\": 1,\n" +
                "\t\"data\": {\n" +
                "\t\t\"type\": \"Feature\",\n" +
                "\t\t\"crs\": {\n" +
                "\t\t\t\"type\": \"name\",\n" +
                "\t\t\t\"properties\": {\n" +
                "\t\t\t\t\"name\": \"epsg:4326\"\n" +
                "\t\t\t}\n" +
                "\t\t},\n" +
                "\t\t\"geometry\": {\n" +
                "\t\t\t\"type\": \"GeometryCollection\",\n" +
                "\t\t\t\"geometries\": [{\n" +
                "\t\t\t\t\t\"type\": \"Polygon\",\n" +
                "\t\t\t\t\t\"coordinates\": [[[44.384766, 56.65574], [43.962892, 37.467182], [94.166018, 37.299575], [91.986326, 56.810018], [44.384766, 56.65574]]]\n" +
                "\t\t\t\t}\n" +
                "\t\t\t]\n" +
                "\t\t},\n" +
                "\t\t\"properties\": {\n" +
                "\t\t\t\"uuid\": \"cea96e47-c0e8-48da-bea2-a5cacf91b83c\",\n" +
                "\t\t\t\"domain\": \"rcdb.tengizchevroil.com\",\n" +
                "\t\t\t\"version\": 1,\n" +
                "\t\t\t\"RoadConditionId\": 36,\n" +
                "\t\t\t\"RoadConditionType\": \"Amber\",\n" +
                "\t\t\t\"RoadConditionEventType\": \"Updated\",\n" +
                "\t\t\t\"StartDateTime\": \"2018-05-04T00:00:00Z\",\n" +
                "\t\t\t\"EffectiveDateTime\": \"2018-05-04T00:00:00Z\",\n" +
                "\t\t\t\"EndDateTime\": \"2024-01-01T01:14:00Z\",\n" +
                "\t\t\t\"Graphic\": {\n" +
                "\t\t\t\t\"attributes\": {\n" +
                "\t\t\t\t\t\"center\": {\n" +
                "\t\t\t\t\t\t\"lat\": 47.907505,\n" +
                "\t\t\t\t\t\t\"lng\": 78.3457\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t\"zoom\": 4\n" +
                "\t\t\t\t}\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}\n";
        map.put("cea96e47-c0e8-48da-bea2-a5cacf91b83c", value);
        redis.set(ROAD_CONDITIONS_HM, map);
    }

    @Test
    @Ignore
    public void getMapTest() {
        Map<String, String> roadConditionsHashMap = redis.getMap(ROAD_CONDITIONS_HM);
        System.out.println(roadConditionsHashMap);
    }
}