package com.epam.tcodata.analytics.overtaking.detection.overtaking.gps;

import com.epam.tcodata.analytics.overtaking.detection.overtaking.gps.types.GPSPoint;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.epam.tcodata.analytics.overtaking.detection.overtaking.gps.OvertakingTestUsingCSVBase.MINED_NEGATIVE_FOLDER;
import static com.epam.tcodata.analytics.overtaking.detection.overtaking.gps.OvertakingTestUsingCSVBase.REAL_OVERTAKINGS_FOLDER;
import static org.junit.Assert.assertEquals;

/**
 * Duplicated tests needed to make sure everything works before refactoring pipeline.
 */
public class IntegrationSparkTestSuite {

    private SparkSession spark = null;

    /**
     * Sets up shared spark session for testing.
     */
    @Before
    public void setUp() {
        spark = SparkSession
                .builder()
                .appName("OvertakingIntegrationTests")
                .master("local[*]")
                .getOrCreate();
        spark.sparkContext().setLogLevel("WARN");
    }

    @After
    public void tearDown() {
        spark.stop();
        spark = null;
    }

    @Test
    public void testNoOvertakingOnLowSpeed() {
        long ovtCount = runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-1473_504-1039.csv");
        assertEquals(0, ovtCount);
    }

    @Test
    public void testNoOvertakingIfOneDontMove() {
        long ovtCount = runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-1177_504-1057.csv");
        assertEquals(0, ovtCount);

    }

    @Test
    public void testNoOvertakingOnEqualSpeed() {
        long ovtCount = runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-902_504-1030.csv");
        assertEquals(0, ovtCount);
    }

    @Test
    public void testNoOvertakingWhenStartingPointsAreDense() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-77_4443-32.csv"));
    }

    @Test
    public void testFalsePositive1() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-841_3824-1.csv"));
    }

    @Test
    public void testFalsePositive2() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-843_1248-23.csv"));
    }

    @Test
    public void testFalsePositive3() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-843_1248-39.csv"));
    }

    @Test
    public void testFalsePositive4() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-843_1265-498.csv"));
    }

    @Test
    public void testFalsePositive5() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-843_5131-107.csv"));
    }

    @Test
    public void testFalsePositive6() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-848_1067-61.csv"));
    }

    @Test
    public void testFalsePositive7() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-848_1564-93.csv"));
    }

    @Test
    public void testFalsePositive8() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-848_4412-71.csv"));
    }

    @Test
    public void testFalsePositive9() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-849_1100-374.csv"));
    }

    @Test
    public void testFalsePositive10() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-851_908-96.csv"));
    }

    @Test
    public void testFalsePositive11() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-851_1528-51.csv"));
    }

    @Test
    public void testFalsePositive12() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-851_3100-113.csv"));
    }

    @Test
    public void testFalsePositive13() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-851_4412-78.csv"));
    }

    @Test
    public void testFalsePositive14() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-911_504-826.csv"));
    }

    @Test
    public void testFalsePositive15() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-911_3100-163.csv"));
    }

    @Test
    public void testShouldBeFilteredByLinearInterpolation() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "1528-344_5131-32.csv"));
    }

    @Test
    public void test1() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-77_4781-8.csv"));
    }

    @Test
    public void test2() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-759_3100-221.csv"));
    }

    @Test
    public void test3() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-843_1265-498.csv"));
    }

    @Test
    public void test4() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-848_1562-434.csv"));
    }

    @Test
    public void test5() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-849_1477-81.csv"));
    }

    @Test
    public void test6() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-851_1528-51.csv"));
    }

    @Test
    public void test7() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-851_3100-113.csv"));
    }

    @Test
    public void test8() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-851_4412-78.csv"));
    }

    @Test
    public void test9() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-922_1248-48.csv"));
    }

    @Test
    public void test10() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-926_908-110.csv"));
    }

    @Test
    public void test11() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-926_1249-80.csv"));
    }

    @Test
    public void test12() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-926_4443-72.csv"));
    }

    @Test
    public void test13() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-928_4443-13.csv"));
    }

    @Test
    public void test14() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-1042_1562-440.csv"));
    }

    @Test
    public void test15() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-1120_504-1077.csv"));
    }

    @Test
    public void test16() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-1120_1477-193.csv"));
    }

    @Test
    public void test17() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-1120_4443-13.csv"));
    }

    @Test
    public void test18() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-1206_3100-145.csv"));
    }

    @Test
    public void test19() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-1254_504-928.csv"));
    }

    @Test
    public void test20() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-1254_504-1077.csv"));
    }

    @Test
    public void test21() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-1254_908-280.csv"));
    }

    @Test
    public void test22() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-1254_1477-193.csv"));
    }

    @Test
    public void test23() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-1254_1562-375.csv"));
    }

    @Test
    public void test24() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "504-1254_1592-6.csv"));
    }

    @Test
    public void testSeparateRoads() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "sep_roads.csv"));
    }

    @Test
    public void testSeparateRoads2() {
        assertEquals(0, runOvertakingDetection(MINED_NEGATIVE_FOLDER + "two_separate_roads_check.csv"));
    }

    @Test
    public void testCase1() {
        assertEquals(1, runOvertakingDetection(REAL_OVERTAKINGS_FOLDER + "gps_1.csv"));
    }

    @Test
    public void testCase3() {
        assertEquals(1, runOvertakingDetection(REAL_OVERTAKINGS_FOLDER + "gps_3.csv"));
    }

    @Test
    public void testCase6() {
        assertEquals(1, runOvertakingDetection(REAL_OVERTAKINGS_FOLDER + "gps_6.csv"));
    }

    @Test
    public void testCase7() {
        assertEquals(1, runOvertakingDetection(REAL_OVERTAKINGS_FOLDER + "gps_7.csv"));
    }

    @Test
    public void testCase8() {
        assertEquals(1, runOvertakingDetection(REAL_OVERTAKINGS_FOLDER + "gps_8.csv"));
    }

    @Test
    public void testCase9() {
        assertEquals(1, runOvertakingDetection(REAL_OVERTAKINGS_FOLDER + "gps_9.csv"));
    }

    @Test
    public void testCase21() {
        assertEquals(1, runOvertakingDetection(REAL_OVERTAKINGS_FOLDER + "gps_21.csv"));

    }

    @Test
    public void testCase22() {
        assertEquals(1, runOvertakingDetection(REAL_OVERTAKINGS_FOLDER + "gps_22.csv"));
    }

    @Test
    public void testCase31() {
        assertEquals(1, runOvertakingDetection(REAL_OVERTAKINGS_FOLDER + "gps_31.csv"));
    }

    private long runOvertakingDetection(String filename) {
        Dataset<GPSPoint> gpsDF = getGpsDfFromCsv(getPathToTestResource(filename));
        return PureGPSProcessing.processData(gpsDF).count();
    }

    private Dataset<GPSPoint> getGpsDfFromCsv(String path) {
        return spark.read()
                .schema(getCSVSchema())
                .option("header", true)
                .option("timestampFormat", "yyyy-MM-dd HH:mm:ss.SSS")
                .csv(path)
                .as(Encoders.bean(GPSPoint.class));
    }

    private String getPathToTestResource(String filename) {
        return IntegrationSparkTestSuite.class.getResource(filename).getPath();
    }

    /**
     * Creates CSV schema to read test csv files. Can't use "inferSchema" options because of incorrect encoding.
     */
    private StructType getCSVSchema() {
        List<StructField> fields = new ArrayList<>();
        fields.add(DataTypes.createStructField("VehicleDurableId", DataTypes.StringType, false));
        fields.add(DataTypes.createStructField("DriverDurableId", DataTypes.StringType, false));
        fields.add(DataTypes.createStructField("Time", DataTypes.TimestampType, false));
        fields.add(DataTypes.createStructField("Latitude", DataTypes.DoubleType, false));
        fields.add(DataTypes.createStructField("Longitude", DataTypes.DoubleType, false));
        fields.add(DataTypes.createStructField("Velocity", DataTypes.DoubleType, false));
        return DataTypes.createStructType(fields);
    }
}
