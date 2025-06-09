package com.epam.tcodata.hive.dal.repository;

import com.epam.tcodata.hive.dal.domain.IHiveEntityType;
import com.epam.tcodata.hive.dal.domain.raw.RawAreaEntityType;
import com.epam.tcodata.hive.dal.util.HiveCommon;
import com.epam.tcodata.models.datalake.raw.dimension.RawDriver;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.*;
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder;
import org.apache.spark.sql.catalyst.encoders.RowEncoder;
import org.apache.spark.sql.types.StructType;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class HiveRepositoryTest {

    private static final String FORMAT = "orc";
    private static final String URL = "temp/";

    private Encoder<RawDriver> encoder;
    private StructType entitySchema;
    private StructType rowSchema;
    private IHiveEntityType hiveEntityType = RawAreaEntityType.DRIVER;
    private List<RawDriver> list;
    private UUID uuid1 = UUID.randomUUID();
    private UUID uuid2 = UUID.randomUUID();

    private static SparkSession sparkSession;

//    @BeforeClass
//    public static void setUpClass() throws Exception {
//        sparkSession = SparkSession.builder()
//                .config(new SparkConf()
//                        .setAppName("app-name")
//                        .setMaster("local[3]"))
//                .getOrCreate();
//    }
//
//    @Before
//    public void setUp() {
//        this.entitySchema =  HiveCommon.getEntitySchema(hiveEntityType.getEntityClazz());
//        this.rowSchema =  HiveCommon.getRowSchema(hiveEntityType.getEntityClazz());
//        this.encoder = Encoders.bean((Class<RawDriver>) hiveEntityType.getEntityClazz());
//        RawDriver driver1 = createDriver(100L, uuid1.toString(), "Robb Stark", "100-2");
//        RawDriver driver2 = createDriver(101L, uuid2.toString(), "Jon Snow", "888-1");
//        this.list = Arrays.asList(driver1, driver2);
//    }
//
//    @Test
//    public void readWrite() {
//
//        JavaRDD<RawDriver> enrichedJavaRDD = new JavaSparkContext(sparkSession.sparkContext()).parallelize(list);
//        Encoder<RawDriver> bean = Encoders.bean(RawDriver.class);
//        Dataset<RawDriver> dataset = sparkSession.createDataset(enrichedJavaRDD.rdd(), bean);
//
//        dataset.write()
//                .format(FORMAT)
//                .mode(SaveMode.Overwrite)
//                .save(URL + hiveEntityType.tableName());
//
//        Dataset<RawDriver> as = sparkSession.read()
//                .orc(URL + hiveEntityType.tableName())
//                .as(encoder);
//
//        List<RawDriver> rawDrivers = as.collectAsList();
//
//        assertEquals(2, rawDrivers.size());
//        assertEquals(uuid1.toString(), rawDrivers.get(0).getDurableId());
//        assertEquals(uuid2.toString(), rawDrivers.get(1).getDurableId());
//    }
//
//    @Test
//    public void readWriteRows() {
//
//        ExpressionEncoder<Row> enc = RowEncoder.apply(rowSchema);
//        List<Row> rows = HiveCommon.convertToRowList(this.list, entitySchema);
//        Dataset<Row> dataset = sparkSession.createDataset(rows, enc);
//
//        Dataset<RawDriver> rawDriverDataset = HiveCommon.convertToEntityDataset(dataset, this.hiveEntityType, this.encoder);
//        rawDriverDataset.write()
//                .format(FORMAT)
//                .mode(SaveMode.Overwrite)
//                .partitionBy("country", "email")
//                .save(URL + hiveEntityType.tableName());
//
//        Column where = new Column(RawDriver.Fields.NAME).$eq$eq$eq("Robb Stark");
//        Dataset<RawDriver> as = sparkSession.read()
//                .orc(URL + hiveEntityType.tableName())
//                .as(encoder)
//                .where(where);
//
//        List<RawDriver> rawDrivers = as.collectAsList();
//
//        assertEquals(1, rawDrivers.size());
//        assertEquals(uuid1.toString(), rawDrivers.get(0).getDurableId());
//    }
//
//    private static RawDriver createDriver(long driverId, String durableId, String name, String employeeNumber) {
//        RawDriver driver = new RawDriver();
//        driver.setIngestedDateUtc(null);
////        driver.setIngestedDateUtc(Timestamp.from(Instant.now()));
//        driver.setPersistedDateUtc(Timestamp.from(Instant.now()));
//        driver.setDriverId(driverId);
//        driver.setDurableId(durableId);
//        driver.setName(name);
//        driver.setEmployeeNumber(employeeNumber);
//        driver.setCountry("Russia");
//        driver.setEmail("vasy@gmail.com");
//        driver.setExtendedDriverId("extended");
//        driver.setFmDriverId(100000L);
//        driver.setExtendedDriverIdType("extended type");
//        driver.setImageUri("http://noname.jpg");
//        driver.setMobileNumber("8-800-8000-0000");
//        driver.setSiteId(123L);
//        driver.setSystemDriver(false);
//        driver.setLineageCode(100);
//        driver.setSubscriptionId(567L);
//        return driver;
//    }

}