package com.epam.tcodata.event.validator.logic.complex;

import com.epam.tcodata.event.validator.domain.EnrichedPreparedEvent;
import com.holdenkarau.spark.testing.JavaDatasetSuiteBase;
import org.apache.spark.sql.Dataset;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class HarshAccelerationDetectorTest extends JavaDatasetSuiteBase {

    private static final long serialVersionUID = 3590237119409254506L;

    private static final String HARSH_ACCELERATION_RESOURCE_PATH = "src/test/resources/complex/harsh-acceleration/";

    @BeforeClass
    public static void init() {
        /***  Default implementation ***/
    }

    @Test
    public void filteringEventsTest() throws IOException {
        Dataset<EnrichedPreparedEvent> eventDataset = DetectorUtils.createDatasetFromCsv(
                HARSH_ACCELERATION_RESOURCE_PATH,
                "filtering/input.csv",
                jsc(),
                spark());
        HarshAccelerationDetector harshAccelerationDetector = new HarshAccelerationDetector();
        Dataset<EnrichedPreparedEvent> checkedEventDataset = harshAccelerationDetector.detect(eventDataset);
        Dataset<EnrichedPreparedEvent> expectedEventDataset = DetectorUtils.createDatasetFromCsv(
                HARSH_ACCELERATION_RESOURCE_PATH,
                "filtering/expected.csv",
                jsc(),
                spark());
        assertDatasetEquals(expectedEventDataset, checkedEventDataset);
    }

    @Test
    public void onlyHarshAccelerationEventsTest() throws IOException {
        Dataset<EnrichedPreparedEvent> eventDataset = DetectorUtils.createDatasetFromCsv(
                HARSH_ACCELERATION_RESOURCE_PATH,
                "6.2/input.csv",
                jsc(),
                spark());
        HarshAccelerationDetector harshAccelerationDetector = new HarshAccelerationDetector();
        Dataset<EnrichedPreparedEvent> checkedEventDataset = harshAccelerationDetector.detect(eventDataset);
        Dataset<EnrichedPreparedEvent> expectedEventDataset = DetectorUtils.createDatasetFromCsv(
                HARSH_ACCELERATION_RESOURCE_PATH,
                "6.2/expected.csv",
                jsc(),
                spark());
        assertDatasetEquals(expectedEventDataset, checkedEventDataset);
    }

    @Test
    public void sameTimePreviousEventVelocityNullTest() throws IOException {
        Dataset<EnrichedPreparedEvent> eventDataset = DetectorUtils.createDatasetFromCsv(
                HARSH_ACCELERATION_RESOURCE_PATH,
                "6.2a/1/input.csv",
                jsc(),
                spark());
        HarshAccelerationDetector harshAccelerationDetector = new HarshAccelerationDetector();
        Dataset<EnrichedPreparedEvent> checkedEventDataset = harshAccelerationDetector.detect(eventDataset);
        Dataset<EnrichedPreparedEvent> expectedEventDataset = DetectorUtils.createDatasetFromCsv(
                HARSH_ACCELERATION_RESOURCE_PATH,
                "6.2a/1/expected.csv",
                jsc(),
                spark());
        assertDatasetEquals(expectedEventDataset, checkedEventDataset);
    }

    @Test
    public void sameTimeCurrentPreviousEventVelocityNullTest() throws IOException {
        Dataset<EnrichedPreparedEvent> eventDataset = DetectorUtils.createDatasetFromCsv(
                HARSH_ACCELERATION_RESOURCE_PATH,
                "6.2a/2/input.csv",
                jsc(),
                spark());
        HarshAccelerationDetector harshAccelerationDetector = new HarshAccelerationDetector();
        Dataset<EnrichedPreparedEvent> checkedEventDataset = harshAccelerationDetector.detect(eventDataset);
        Dataset<EnrichedPreparedEvent> expectedEventDataset = DetectorUtils.createDatasetFromCsv(
                HARSH_ACCELERATION_RESOURCE_PATH,
                "6.2a/2/expected.csv",
                jsc(),
                spark());
        assertDatasetEquals(expectedEventDataset, checkedEventDataset);
    }

    @Test
    public void sameTimeCurrentEventVelocityNotNullTest() throws IOException {
        Dataset<EnrichedPreparedEvent> eventDataset = DetectorUtils.createDatasetFromCsv(
                HARSH_ACCELERATION_RESOURCE_PATH,
                "6.2b/input.csv",
                jsc(),
                spark());
        HarshAccelerationDetector harshAccelerationDetector = new HarshAccelerationDetector();
        Dataset<EnrichedPreparedEvent> checkedEventDataset = harshAccelerationDetector.detect(eventDataset);
        Dataset<EnrichedPreparedEvent> expectedEventDataset = DetectorUtils.createDatasetFromCsv(
                HARSH_ACCELERATION_RESOURCE_PATH,
                "6.2b/expected.csv",
                jsc(),
                spark());
        assertDatasetEquals(expectedEventDataset, checkedEventDataset);
    }

}
