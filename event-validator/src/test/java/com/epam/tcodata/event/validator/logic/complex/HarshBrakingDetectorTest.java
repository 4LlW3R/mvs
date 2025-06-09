package com.epam.tcodata.event.validator.logic.complex;

import com.epam.tcodata.event.validator.domain.EnrichedPreparedEvent;
import com.holdenkarau.spark.testing.JavaDatasetSuiteBase;
import org.apache.spark.sql.Dataset;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class HarshBrakingDetectorTest extends JavaDatasetSuiteBase {

    private static final long serialVersionUID = 3590237119409254506L;

    private static final String HARSH_BRAKING_RESOURCE_PATH = "src/test/resources/complex/harsh-braking/";

    @BeforeClass
    public static void init() {
        /***  Default implementation ***/
    }

    @Test
    public void filteringEventsTest() throws IOException {
        Dataset<EnrichedPreparedEvent> eventDataset = DetectorUtils.createDatasetFromCsv(
                HARSH_BRAKING_RESOURCE_PATH,
                "filtering/input.csv",
                jsc(),
                spark());
        HarshBrakingDetector harshBrakingDetector = new HarshBrakingDetector();
        Dataset<EnrichedPreparedEvent> checkedEventDataset = harshBrakingDetector.detect(eventDataset);
        Dataset<EnrichedPreparedEvent> expectedEventDataset = DetectorUtils.createDatasetFromCsv(
                HARSH_BRAKING_RESOURCE_PATH,
                "filtering/expected.csv",
                jsc(),
                spark());
        assertDatasetEquals(expectedEventDataset, checkedEventDataset);
    }

    @Test
    public void onlyHarshBrakingEventsTest() throws IOException {
        Dataset<EnrichedPreparedEvent> eventDataset = DetectorUtils.createDatasetFromCsv(
                HARSH_BRAKING_RESOURCE_PATH,
                "6.1/input.csv",
                jsc(),
                spark());
        HarshBrakingDetector harshBrakingDetector = new HarshBrakingDetector();
        Dataset<EnrichedPreparedEvent> checkedEventDataset = harshBrakingDetector.detect(eventDataset);
        Dataset<EnrichedPreparedEvent> expectedEventDataset = DetectorUtils.createDatasetFromCsv(
                HARSH_BRAKING_RESOURCE_PATH,
                "6.1/expected.csv",
                jsc(),
                spark());
        assertDatasetEquals(expectedEventDataset, checkedEventDataset);
    }

    @Test
    public void sameTimePreviousEventVelocityNullTest() throws IOException {
        Dataset<EnrichedPreparedEvent> eventDataset = DetectorUtils.createDatasetFromCsv(
                HARSH_BRAKING_RESOURCE_PATH,
                "6.1a/1/input.csv",
                jsc(),
                spark());
        HarshBrakingDetector harshBrakingDetector = new HarshBrakingDetector();
        Dataset<EnrichedPreparedEvent> checkedEventDataset = harshBrakingDetector.detect(eventDataset);
        Dataset<EnrichedPreparedEvent> expectedEventDataset = DetectorUtils.createDatasetFromCsv(
                HARSH_BRAKING_RESOURCE_PATH,
                "6.1a/1/expected.csv",
                jsc(),
                spark());
        assertDatasetEquals(expectedEventDataset, checkedEventDataset);
    }

    @Test
    public void sameTimeCurrentPreviousEventVelocityNullTest() throws IOException {
        Dataset<EnrichedPreparedEvent> eventDataset = DetectorUtils.createDatasetFromCsv(
                HARSH_BRAKING_RESOURCE_PATH,
                "6.1a/2/input.csv",
                jsc(),
                spark());
        HarshBrakingDetector harshBrakingDetector = new HarshBrakingDetector();
        Dataset<EnrichedPreparedEvent> checkedEventDataset = harshBrakingDetector.detect(eventDataset);
        Dataset<EnrichedPreparedEvent> expectedEventDataset = DetectorUtils.createDatasetFromCsv(
                HARSH_BRAKING_RESOURCE_PATH,
                "6.1a/2/expected.csv",
                jsc(),
                spark());
        assertDatasetEquals(expectedEventDataset, checkedEventDataset);
    }

    @Test
    public void sameTimeCurrentEventVelocityNotNullTest() throws IOException {
        Dataset<EnrichedPreparedEvent> eventDataset = DetectorUtils.createDatasetFromCsv(
                HARSH_BRAKING_RESOURCE_PATH,
                "6.1b/input.csv",
                jsc(),
                spark());
        HarshBrakingDetector harshBrakingDetector = new HarshBrakingDetector();
        Dataset<EnrichedPreparedEvent> checkedEventDataset = harshBrakingDetector.detect(eventDataset);
        Dataset<EnrichedPreparedEvent> expectedEventDataset = DetectorUtils.createDatasetFromCsv(
                HARSH_BRAKING_RESOURCE_PATH,
                "6.1b/expected.csv",
                jsc(),
                spark());
        assertDatasetEquals(expectedEventDataset, checkedEventDataset);
    }

}
