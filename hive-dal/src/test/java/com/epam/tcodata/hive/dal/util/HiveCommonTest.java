package com.epam.tcodata.hive.dal.util;

import com.epam.tcodata.common.ResourceUtils;
import com.epam.tcodata.hive.dal.Hive;
import com.epam.tcodata.hive.dal.HiveConfig;
import com.epam.tcodata.hive.dal.IHive;
import com.epam.tcodata.hive.dal.domain.raw.RawAreaEntityType;
import com.epam.tcodata.hive.dal.repository.IHiveRepository;
import com.epam.tcodata.models.datalake.raw.dimension.RawDriver;
import com.epam.tcodata.models.datalake.raw.fact.RawDetectedEvent;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.*;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;
import org.apache.spark.sql.types.StructType;
import org.junit.BeforeClass;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class HiveCommonTest {

    private static final String FACT_DETECTED_EVENT_CREATE_QUERY_HQL_FILE = "create_fact_detected_event.hql";
    private static final String DRIVER_ID = "driver_id";

    private static SparkSession sparkSession;


    @BeforeClass
    public static void setUpClass() throws Exception {
        sparkSession = SparkSession.builder()
                .config(new SparkConf()
                        .setAppName("app-name")
                        .setMaster("local[3]"))
                .enableHiveSupport()
                .getOrCreate();
        sparkSession.sql("CREATE DATABASE IF NOT EXISTS raw");
        sparkSession.sql("USE raw");
        String detectedEventCreateQuery = ResourceUtils.streamAsString(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(FACT_DETECTED_EVENT_CREATE_QUERY_HQL_FILE));
        sparkSession.sql(detectedEventCreateQuery);
    }

//    @Test
    public void getEntitySchema() {
        StructType actualShema = HiveCommon.getEntitySchema(RawDriver.class);
        assertEquals(17, actualShema.size());
        StructType repeatActualShema = HiveCommon.getEntitySchema(RawDriver.class);
        assertSame(actualShema, repeatActualShema);
    }

//    @Test
    public void entityToRow() {
        RawDriver driver = new RawDriver();
        driver.setDriverId(1000L);
        Row row = HiveCommon.entityToRow(driver);
        Object as = row.getAs(RawDriver.Fields.DRIVER_ID);

        assertEquals(1000L, as);

        StructType schema = row.schema();
        List<String> actual = Arrays.asList(schema.fieldNames());
        List<String> expected = Arrays.asList(
                "durable_id",
                "ingested_date_utc",
                "subscription_id",
                "lineage_code",
                "persisted_date_utc",
                "site_id",
                DRIVER_ID,
                "name",
                "image_uri",
                "fm_driver_id",
                "employee_number",
                "system_driver",
                "mobile_number",
                "email",
                "extended_driver_id",
                "extended_driver_id_type",
                "country");

        assertEquals(expected, actual);
    }

//    @Test
    public void rowToEntity() {
        StructType driverSchema = HiveCommon.getEntitySchema(RawDriver.class);
        RawDriver expected = new RawDriver();
        expected.setDriverId(100L);
        Object[] objects = expected.orderedValues();
        GenericRowWithSchema row = new GenericRowWithSchema(objects, driverSchema);
        RawDriver actual = HiveCommon.rowToEntity(row, RawDriver.class);
        assertEquals(expected.orderedValues(), actual.orderedValues());
    }

//    @Test(expected = ConversionException.class)
    public void rowWithoutSchemaToEntity() {
        RawDriver expected = new RawDriver();
        expected.setDriverId(100L);
        Object[] objects = expected.orderedValues();
        Row row = RowFactory.create(objects);
        HiveCommon.rowToEntity(row, RawDriver.class);
    }

//    @Test
    public void entityListToRowList() {
        RawDriver driver1 = new RawDriver();
        driver1.setDriverId(100L);
        RawDriver driver2 = new RawDriver();
        driver2.setDriverId(200L);

        List<Row> rows = HiveCommon.entityListToRowList(Arrays.asList(driver1, driver2));

        assertEquals(rows.size(), 2);
        assertEquals(rows.get(0).<Long>getAs(DRIVER_ID), Long.valueOf(100L));
        assertEquals(rows.get(1).<Long>getAs(DRIVER_ID), Long.valueOf(200L));
    }

//    @Test(expected = ConversionException.class)
    public void rowListToEntityList() {
        RawDriver driver1 = new RawDriver();
        driver1.setDriverId(100L);
        Row row1 = HiveCommon.entityToRow(driver1);
        RawDriver driver2 = new RawDriver();
        driver2.setDriverId(200L);
        Row row2 = RowFactory.create(driver2.orderedValues()); // row without schema

        List<Row> rows = Arrays.asList(row1, row2);
        HiveCommon.rowListToEntityList(rows, RawDriver.class);
    }

//    @Test
    public void entityListToEntityRdd() {
        RawDriver driver1 = new RawDriver();
        driver1.setDriverId(100L);
        RawDriver driver2 = new RawDriver();
        driver2.setDriverId(200L);

        List<RawDriver> expected = Arrays.asList(driver1, driver2);
        JavaRDD<RawDriver> rawDriverJavaRDD = HiveCommon.entityListToEntityRdd(expected, sparkSession);

        List<RawDriver> actual = rawDriverJavaRDD.collect();
        assertEquals(expected, actual);
    }

//    @Test
    public void rowListToRowRdd() {
        RawDriver driver1 = new RawDriver();
        driver1.setDriverId(100L);
        Row row1 = HiveCommon.entityToRow(driver1);
        RawDriver driver2 = new RawDriver();
        driver2.setDriverId(200L);
        Row row2 = HiveCommon.entityToRow(driver2);

        List<Row> expected = Arrays.asList(row1, row2);
        JavaRDD<Row> rawDriverJavaRDD = HiveCommon.rowListToRowRdd(expected, sparkSession);

        List<Row> actual = rawDriverJavaRDD.collect();
        assertEquals(expected, actual);
    }

//    @Test
    public void entityListToRowRdd() {
        RawDriver driver1 = new RawDriver();
        driver1.setDriverId(100L);
        Row row1 = HiveCommon.entityToRow(driver1);
        RawDriver driver2 = new RawDriver();
        driver2.setDriverId(200L);
        Row row2 = HiveCommon.entityToRow(driver2);

        List<Row> expected = Arrays.asList(row1, row2);
        JavaRDD<Row> rawDriverJavaRDD = HiveCommon.entityListToRowRdd(Arrays.asList(driver1, driver2), sparkSession);

        List<Row> actual = rawDriverJavaRDD.collect();
        assertEquals(expected, actual);
    }

//    @Test
    public void rowListToEntityRdd() {
        RawDriver driver1 = new RawDriver();
        driver1.setDriverId(100L);
        Row row1 = HiveCommon.entityToRow(driver1);
        RawDriver driver2 = new RawDriver();
        driver2.setDriverId(200L);
        Row row2 = HiveCommon.entityToRow(driver2);

        List<RawDriver> expected = Arrays.asList(driver1, driver2);
        JavaRDD<RawDriver> rawDriverJavaRDD = HiveCommon.rowListToEntityRdd(Arrays.asList(row1, row2), RawDriver.class, sparkSession);

        List<RawDriver> actual = rawDriverJavaRDD.collect();
        assertEquals(expected, actual);
    }

//    @Test
    public void rowRddToEntityList() {
        RawDriver driver1 = new RawDriver();
        driver1.setDriverId(100L);
        Row row1 = HiveCommon.entityToRow(driver1);
        RawDriver driver2 = new RawDriver();
        driver2.setDriverId(200L);
        Row row2 = HiveCommon.entityToRow(driver2);
        JavaRDD<Row> rowJavaRDD = HiveCommon.rowListToRowRdd(Arrays.asList(row1, row2), sparkSession);

        List<RawDriver> expected = Arrays.asList(driver1, driver2);
        List<RawDriver> actual = HiveCommon.rowRddToEntityList(rowJavaRDD, RawDriver.class);

        assertEquals(expected, actual);
    }

//    @Test
    public void entityRddToRowList() {
        RawDriver driver1 = new RawDriver();
        driver1.setDriverId(100L);
        Row row1 = HiveCommon.entityToRow(driver1);
        RawDriver driver2 = new RawDriver();
        driver2.setDriverId(200L);
        Row row2 = HiveCommon.entityToRow(driver2);
        JavaRDD<RawDriver> rawDriverJavaRDD = HiveCommon.entityListToEntityRdd(Arrays.asList(driver1, driver2), sparkSession);

        List<Row> expected = Arrays.asList(row1, row2);
        List<Row> actual = HiveCommon.entityRddToRowList(rawDriverJavaRDD);

        assertEquals(expected, actual);
    }

//    @Test
    public void rowRddToEntityRdd() {
        RawDriver driver1 = new RawDriver();
        driver1.setDriverId(100L);
        Row row1 = HiveCommon.entityToRow(driver1);
        RawDriver driver2 = new RawDriver();
        driver2.setDriverId(200L);
        Row row2 = HiveCommon.entityToRow(driver2);
        JavaRDD<Row> rowJavaRDD = HiveCommon.rowListToRowRdd(Arrays.asList(row1, row2), sparkSession);

        JavaRDD<RawDriver> actualRdd = HiveCommon.rowRddToEntityRdd(rowJavaRDD, RawDriver.class);

        List<RawDriver> expected = Arrays.asList(driver1, driver2);
        List<RawDriver> actual = actualRdd.collect();
        assertEquals(expected, actual);
    }

//    @Test
    public void rowRddWithArrayToEntityRdd() {
        sparkSession.sql("USE raw");
        sparkSession.sql("TRUNCATE TABLE raw.fact_detected_event");

        RawDetectedEvent event1 = new RawDetectedEvent();
        Integer[] violationIDs = new Integer[2];
        violationIDs[0] = 1;
        violationIDs[1] = 2;
        event1.setViolationIDs(violationIDs);
        Row row1 = HiveCommon.entityToRow(event1);

        IHive hive = new Hive(HiveConfig.RAW, sparkSession);
        IHiveRepository detectedEventRepository = hive.repository(RawAreaEntityType.DETECTED_EVENT);

        JavaRDD<Row> detectedRowRdd = HiveCommon.rowListToRowRdd(Arrays.asList(row1), sparkSession);
        Dataset<Row> detectedRowDataset = HiveCommon.rowRddToRowDataset(detectedRowRdd, RawDetectedEvent.class, sparkSession);
        detectedEventRepository.write(detectedRowDataset, SaveMode.Append);

        Dataset<Row> readDataset = detectedEventRepository.read();
        JavaRDD<RawDetectedEvent> detectedEventRDD = HiveCommon.rowRddToEntityRdd(readDataset.toJavaRDD(), RawDetectedEvent.class);

        List<RawDetectedEvent> expected = Arrays.asList(event1);
        List<RawDetectedEvent> actual = detectedEventRDD.collect();
        assertEquals(expected, actual);
    }

//    @Test
    public void entityRddToRowRdd() {
        RawDriver driver1 = new RawDriver();
        driver1.setDriverId(100L);
        Row row1 = HiveCommon.entityToRow(driver1);
        RawDriver driver2 = new RawDriver();
        driver2.setDriverId(200L);
        Row row2 = HiveCommon.entityToRow(driver2);
        JavaRDD<RawDriver> rawDriverJavaRDD = HiveCommon.entityListToEntityRdd(Arrays.asList(driver1, driver2), sparkSession);

        JavaRDD<Row> actualRdd = HiveCommon.entityRddToRowRdd(rawDriverJavaRDD);

        List<Row> expected = Arrays.asList(row1, row2);
        List<Row> actual = actualRdd.collect();
        assertEquals(expected, actual);
    }

//    @Test
    public void entityRddToEntityDataset() {
        RawDriver driver1 = new RawDriver();
        driver1.setDriverId(100L);
        RawDriver driver2 = new RawDriver();
        driver2.setDriverId(200L);
        JavaRDD<RawDriver> rawDriverJavaRDD = HiveCommon.entityListToEntityRdd(Arrays.asList(driver1, driver2), sparkSession);

        Dataset<RawDriver> rawDriverDataset = HiveCommon.entityRddToEntityDataset(rawDriverJavaRDD, RawDriver.class, sparkSession);

        List<RawDriver> expected = Arrays.asList(driver1, driver2);
        List<RawDriver> actual = rawDriverDataset.javaRDD().collect();
        assertEquals(expected, actual);
    }

//    @Test
    public void rowRddToRowDataset() {
        RawDriver driver1 = new RawDriver();
        driver1.setDriverId(100L);
        Row row1 = HiveCommon.entityToRow(driver1);
        RawDriver driver2 = new RawDriver();
        driver2.setDriverId(200L);
        Row row2 = HiveCommon.entityToRow(driver2);
        JavaRDD<Row> rowJavaRDD = HiveCommon.rowListToRowRdd(Arrays.asList(row1, row2), sparkSession);

        Dataset<Row> rowDataset = HiveCommon.rowRddToRowDataset(rowJavaRDD, RawDriver.class, sparkSession);

        List<Row> expected = Arrays.asList(row1, row2);
        List<Row> actual = rowDataset.javaRDD().collect();
        assertEquals(expected, actual);
    }
}