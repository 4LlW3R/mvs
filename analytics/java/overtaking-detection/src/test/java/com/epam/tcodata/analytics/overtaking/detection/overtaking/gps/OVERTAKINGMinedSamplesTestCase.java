package com.epam.tcodata.analytics.overtaking.detection.overtaking.gps;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class OVERTAKINGMinedSamplesTestCase extends OvertakingTestUsingCSVBase {

    @Test
    public void basicOvertaking1() throws IOException {
        assertEquals(1, getOvertakingCount(MINED_POSITIVE_FOLDER + "504-1525_504-731.csv"));
    }

    @Test
    public void basicOvertaking2() throws IOException {
        assertEquals(1, getOvertakingCount(MINED_POSITIVE_FOLDER + "504-746_504-935.csv"));
    }

    @Test
    public void testNoOvertakingOnLowSpeed() throws IOException {
        long ovtCount = getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-1473_504-1039.csv");
        assertEquals(0, ovtCount);
    }

    @Test
    public void testNoOvertakingIfOneDontMove() throws IOException {
        long ovtCount = getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-1177_504-1057.csv");
        assertEquals(0, ovtCount);

    }

    @Test
    public void testNoOvertakingOnEqualSpeed() throws IOException {
        long ovtCount = getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-902_504-1030.csv");
        assertEquals(0, ovtCount);
    }

    @Test
    public void testNoOvertakingWhenStartingPointsAreDense() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-77_4443-32.csv"));
    }

    @Test
    public void testFalsePositive1() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-841_3824-1.csv"));
    }

    @Test
    public void testFalsePositive2() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-843_1248-23.csv"));
    }

    @Test
    public void testFalsePositive3() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-843_1248-39.csv"));
    }

    @Test
    public void testFalsePositive4() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-843_1265-498.csv"));
    }

    @Test
    public void testFalsePositive5() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-843_5131-107.csv"));
    }

    @Test
    public void testFalsePositive6() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-848_1067-61.csv"));
    }

    @Test
    public void testFalsePositive7() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-848_1564-93.csv"));
    }

    @Test
    public void testFalsePositive8() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-848_4412-71.csv"));
    }

    @Test
    public void testFalsePositive9() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-849_1100-374.csv"));
    }

    @Test
    public void testFalsePositive10() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-851_908-96.csv"));
    }

    @Test
    public void testFalsePositive11() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-851_1528-51.csv"));
    }

    @Test
    public void testFalsePositive12() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-851_3100-113.csv"));
    }

    @Test
    public void testFalsePositive13() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-851_4412-78.csv"));
    }

    @Test
    public void testFalsePositive14() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-911_504-826.csv"));
    }

    @Test
    public void testFalsePositive15() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-911_3100-163.csv"));
    }

    @Test
    public void testShouldBeFilteredByLinearInterpolation() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "1528-344_5131-32.csv"));
    }

    @Test
    public void testNoOvertakingOnTheEndOfSegment() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "1067-102_1427-65.csv"));
    }

    @Test
    public void test1() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-77_4781-8.csv"));
    }

    @Test
    public void test2() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-759_3100-221.csv"));
    }

    @Test
    public void test3() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-843_1265-498.csv"));
    }

    @Test
    public void test4() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-848_1562-434.csv"));
    }

    @Test
    public void test5() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-849_1477-81.csv"));
    }

    @Test
    public void test6() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-851_1528-51.csv"));
    }

    @Test
    public void test7() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-851_3100-113.csv"));
    }

    @Test
    public void test8() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-851_4412-78.csv"));
    }

    @Test
    public void test9() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-922_1248-48.csv"));
    }

    @Test
    public void test10() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-926_908-110.csv"));
    }

    @Test
    public void test11() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-926_1249-80.csv"));
    }

    @Test
    public void test12() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-926_4443-72.csv"));
    }

    @Test
    public void test13() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-928_4443-13.csv"));
    }

    @Test
    public void test14() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-1042_1562-440.csv"));
    }

    @Test
    public void test15() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-1120_504-1077.csv"));
    }

    @Test
    public void test16() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-1120_1477-193.csv"));
    }

    @Test
    public void test17() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-1120_4443-13.csv"));
    }

    @Test
    public void test18() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-1206_3100-145.csv"));
    }

    @Test
    public void test19() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-1254_504-928.csv"));
    }

    @Test
    public void test20() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-1254_504-1077.csv"));
    }

    @Test
    public void test21() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-1254_908-280.csv"));
    }

    @Test
    public void test22() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-1254_1477-193.csv"));
    }

    @Test
    public void test23() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-1254_1562-375.csv"));
    }

    @Test
    public void test24() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "504-1254_1592-6.csv"));
    }

    @Test
    public void testSeparateRoads() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "sep_roads.csv"));
    }

    @Test
    public void testSeparateRoads2() throws IOException {
        assertEquals(0, getOvertakingCount(MINED_NEGATIVE_FOLDER + "two_separate_roads_check.csv"));
    }
}
