package com.epam.tcodata.mock.hive.dal;

import com.epam.tcodata.hive.dal.HiveConfig;
import com.epam.tcodata.hive.dal.IHive;
import com.epam.tcodata.hive.dal.domain.raw.RawAreaEntityType;
import com.epam.tcodata.hive.dal.repository.IHiveRepository;
import com.epam.tcodata.models.datalake.raw.dimension.RawDriver;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@Ignore
public class MockHiveTest {

    private static IHive hive;

    @BeforeClass
    public static void setUpClass() throws Exception {
        SparkSession sparkSession = SparkSession.builder()
                .config(new SparkConf()
                        .setAppName("app-name")
                        .setMaster("local[3]"))
                .config("hive.exec.dynamic.partition", "true")
                .config("hive.exec.dynamic.partition.mode", "nonstrict")
                .enableHiveSupport()
                .getOrCreate();

        hive = MockHive.instance(HiveConfig.RAW, sparkSession);
    }

    @Before
    public void setUp() {
        hive.entityTypes().forEach(n -> hive.repository(n).clear());
    }

    @Test
    public void databaseNameTest() {
        String databaseName = hive.getDatabaseName();
        assertEquals("raw", databaseName);
    }

    @Test
    public void tableNamesTest() {
        Set<String> tableNames = hive.getTableNames();
        Set<String> expected = new HashSet<>();
        expected.addAll(Arrays.asList(
                "fact_subtrip",
                "dim_location",
                "fact_position",
                "dim_library_event",
                "dim_driver_norm",
                "dim_organisation_group",
                "dim_organisation_group_norm",
                "dim_organisation_subgroup",
                "dim_organisation_subgroup_norm",
                "dim_driver",
                "dim_asset_norm",
                "fact_trip",
                "fact_tacho",
                "fact_event",
                "dim_library_event_norm",
                "dim_asset",
                "dim_location_norm",
                "fact_detected_event"));
        assertEquals(expected, tableNames);
    }

    @Test(expected = NullPointerException.class)
    public void nonExistingRepositoryTest() {
        hive.repository(null);
    }

    @Test
    public void existingRepositoryTest() {
        IHiveRepository repository = hive.repository(RawAreaEntityType.DRIVER);
        assertNotNull(repository);

        boolean allExist = hive.entityTypes().stream().map(hive::repository).allMatch(r -> r != null);
        assertTrue(allExist);
    }

    @Test
    public void repositoryReadWriteDatasetTest() {
//        SparkSession sparkSession = hive.getSparkSession();
//        IHiveRepository repository = hive.repository(RawAreaEntityType.DRIVER);
//
////        Dataset<RawDriver> dataset = repository.read();
////        List<RawDriver> table = dataset.collectAsList();
////        assertTrue(table.isEmpty());
////
//        UUID uuid1 = UUID.randomUUID();
//        UUID uuid2 = UUID.randomUUID();
//        RawDriver driver1 = createDriver(100L, uuid1.toString(), "Robb Stark", "100-2");
//        RawDriver driver2 = createDriver(101L, uuid2.toString(), "Jon Snow", "888-1");
//        List<RawDriver> writing = Arrays.asList(driver1, driver2);
//
//        Dataset<Row> dataset1 = sparkSession.createDataFrame(writing, RawDriver.class);
//        repository.write(dataset1, SaveMode.Append);
////
////        dataset = repository.read();
////        table = dataset.collectAsList();
////        assertEquals(2, table.size());
////        assertEquals(uuid1.toString(), table.get(0).getDurableId());
////        assertEquals(uuid2.toString(), table.get(1).getDurableId());
    }

    @Test
    public void repositoryReadWriteRDDTest() {
//        SparkSession sparkSession = hive.getSparkSession();
//        IHiveRepository repository = hive.repository(RawAreaEntityType.DRIVER);
//        Dataset<RawDriver> dataset = repository.read();
//        List<RawDriver> table = dataset.collectAsList();
//        assertTrue(table.isEmpty());
//
//        UUID uuid1 = UUID.randomUUID();
//        UUID uuid2 = UUID.randomUUID();
//        RawDriver driver1 = createDriver(100L, uuid1.toString(), "Robb Stark", "100-2");
//        RawDriver driver2 = createDriver(101L, uuid2.toString(), "Jon Snow", "888-1");
//        List<RawDriver> writing = Arrays.asList(driver1, driver2);
//        JavaRDD<RawDriver> enrichedJavaRDD = new JavaSparkContext(sparkSession.sparkContext()).parallelize(writing);
//
//        repository.write(enrichedJavaRDD, SaveMode.Append);
//
//        dataset = repository.read();
//        table = dataset.collectAsList();
//
//        assertEquals(2, table.size());
//        assertEquals(uuid1.toString(), table.get(0).getDurableId());
//        assertEquals(uuid2.toString(), table.get(1).getDurableId());
    }

    @Test
    public void repositoryReadWithConditionTest() {
//        SparkSession sparkSession = hive.getSparkSession();
//
//        IHiveRepository repository = hive.repository(RawAreaEntityType.DRIVER);
//        repository.clear();
//
//        UUID uuid1 = UUID.randomUUID();
//        UUID uuid2 = UUID.randomUUID();
//        RawDriver driver1 = createDriver(100L, uuid1.toString(), "Robb Stark", "100-2");
//        RawDriver driver2 = createDriver(101L, uuid2.toString(), "Jon Snow", "888-1");
//        List<RawDriver> writing = Arrays.asList(driver1, driver2);
//        JavaRDD<RawDriver> enrichedJavaRDD = new JavaSparkContext(sparkSession.sparkContext()).parallelize(writing);
//
//
//
//
//
//        repository.write(enrichedJavaRDD, SaveMode.Append);
//
//        // now read only Robb Stark record
//        Dataset<RawDriver> read0 = repository.read();
//read0.show();
//        List list0 = read0.collectAsList();
//        System.out.println(">> " + list0);
//        assertEquals(2, list0.size());
//
//        // now read only Robb Stark record
//        Dataset<RawDriver> read = repository.read(new Column(RawDriver.Fields.NAME).$eq$eq$eq("Robb Stark"));
//        List list = read.collectAsList();
//        System.out.println(">> " + list);
//        assertEquals(1, list.size());
//
//        // now try find non-existing record
//        Dataset<RawDriver> read1 = repository.read(new Column(RawDriver.Fields.NAME).$eq$eq$eq("Nobody"));
//        List list1 = read1.collectAsList();
//        System.out.println(">> " + list1);
//        assertEquals(0, list1.size());
//
//        // complex condition
//        Dataset<RawDriver> read2 = repository.read(
//                new Column(RawDriver.Fields.NAME).$eq$eq$eq("Robb Stark")
//                .and(new Column(RawDriver.Fields.DRIVER_ID).$eq$eq$eq(100L)));
//        List list2 = read2.collectAsList();
//        System.out.println(">> " + list2);
//        assertEquals(1, list2.size());
    }

    private static RawDriver createDriver(long driverId, String durableId, String name, String employeeNumber) {
        RawDriver driver = new RawDriver();
        driver.setIngestedDateUtc(Timestamp.from(Instant.now()));
        driver.setPersistedDateUtc(Timestamp.from(Instant.now()));
        driver.setDriverId(driverId);
        driver.setDurableId(durableId);
        driver.setName(name);
        driver.setEmployeeNumber(employeeNumber);
        driver.setCountry("Russia");
        driver.setEmail("vasy@gmail.com");
        driver.setExtendedDriverId("extended");
        driver.setFmDriverId(100000L);
        driver.setExtendedDriverIdType("extended type");
        driver.setImageUri("http://noname.jpg");
        driver.setMobileNumber("8-800-8000-0000");
        driver.setSiteId(123L);
        driver.setSystemDriver(false);
        driver.setLineageCode(100);
        driver.setSubscriptionId(567L);
        return driver;
    }
}