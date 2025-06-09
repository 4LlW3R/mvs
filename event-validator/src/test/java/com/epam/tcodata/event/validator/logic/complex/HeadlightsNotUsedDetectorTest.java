package com.epam.tcodata.event.validator.logic.complex;

import com.epam.tcodata.event.validator.domain.EnrichedPreparedEvent;
import com.holdenkarau.spark.testing.JavaDatasetSuiteBase;
import org.apache.spark.sql.Dataset;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class HeadlightsNotUsedDetectorTest extends JavaDatasetSuiteBase {

    private static final long serialVersionUID = 2483452814312615907L;

    private static final String SEATBELT_NOT_USED_RESOURCE_PATH = "src/test/resources/complex/headlights-not-used/";

    @BeforeClass
    public static void init() {
        /***  Default implementation ***/
    }

    @Test
    public void filteringEventsTest() throws IOException {
        Dataset<EnrichedPreparedEvent> eventDataset = DetectorUtils.createDatasetFromCsv(
                SEATBELT_NOT_USED_RESOURCE_PATH,
                "filtering/input.csv",
                jsc(),
                spark());
        HeadlightsNotUsedDetector headlightsNotUsedDetector = new HeadlightsNotUsedDetector();
        Dataset<EnrichedPreparedEvent> checkedEventDataset = headlightsNotUsedDetector.detect(eventDataset);
        Dataset<EnrichedPreparedEvent> expectedEventDataset = DetectorUtils.createDatasetFromCsv(
                SEATBELT_NOT_USED_RESOURCE_PATH,
                "filtering/expected.csv",
                jsc(),
                spark());
        assertDatasetEquals(expectedEventDataset, checkedEventDataset);
    }

    @Test
    public void onlyHeadlightsNotUsedEventsTest() throws IOException {
        Dataset<EnrichedPreparedEvent> eventDataset = DetectorUtils.createDatasetFromCsv(
                SEATBELT_NOT_USED_RESOURCE_PATH,
                "6.5/input.csv",
                jsc(),
                spark());
        HeadlightsNotUsedDetector headlightsNotUsedDetector = new HeadlightsNotUsedDetector();
        Dataset<EnrichedPreparedEvent> checkedEventDataset = headlightsNotUsedDetector.detect(eventDataset);
        Dataset<EnrichedPreparedEvent> expectedEventDataset = DetectorUtils.createDatasetFromCsv(
                SEATBELT_NOT_USED_RESOURCE_PATH,
                "6.5/expected.csv",
                jsc(),
                spark());
        assertDatasetEquals(expectedEventDataset, checkedEventDataset);
    }

    @Test
    public void sameTimePreviousEventVelocityNullTest() throws IOException {
        Dataset<EnrichedPreparedEvent> eventDataset = DetectorUtils.createDatasetFromCsv(
                SEATBELT_NOT_USED_RESOURCE_PATH,
                "6.5a/1/input.csv",
                jsc(),
                spark());
        HeadlightsNotUsedDetector headlightsNotUsedDetector = new HeadlightsNotUsedDetector();
        Dataset<EnrichedPreparedEvent> checkedEventDataset = headlightsNotUsedDetector.detect(eventDataset);
        Dataset<EnrichedPreparedEvent> expectedEventDataset = DetectorUtils.createDatasetFromCsv(
                SEATBELT_NOT_USED_RESOURCE_PATH,
                "6.5a/1/expected.csv",
                jsc(),
                spark());
        assertDatasetEquals(expectedEventDataset, checkedEventDataset);
    }

    @Test
    public void sameTimeCurrentPreviousEventVelocityNullTest() throws IOException {
        Dataset<EnrichedPreparedEvent> eventDataset = DetectorUtils.createDatasetFromCsv(
                SEATBELT_NOT_USED_RESOURCE_PATH,
                "6.5a/2/input.csv",
                jsc(),
                spark());
        HeadlightsNotUsedDetector headlightsNotUsedDetector = new HeadlightsNotUsedDetector();
        Dataset<EnrichedPreparedEvent> checkedEventDataset = headlightsNotUsedDetector.detect(eventDataset);
        Dataset<EnrichedPreparedEvent> expectedEventDataset = DetectorUtils.createDatasetFromCsv(
                SEATBELT_NOT_USED_RESOURCE_PATH,
                "6.5a/2/expected.csv",
                jsc(),
                spark());
        assertDatasetEquals(expectedEventDataset, checkedEventDataset);
    }

    @Test
    public void sameTimeCurrentEventVelocityNotNullTest() throws IOException {
        Dataset<EnrichedPreparedEvent> eventDataset = DetectorUtils.createDatasetFromCsv(
                SEATBELT_NOT_USED_RESOURCE_PATH,
                "6.5b/input.csv",
                jsc(),
                spark());
        HeadlightsNotUsedDetector headlightsNotUsedDetector = new HeadlightsNotUsedDetector();
        Dataset<EnrichedPreparedEvent> checkedEventDataset = headlightsNotUsedDetector.detect(eventDataset);
        Dataset<EnrichedPreparedEvent> expectedEventDataset = DetectorUtils.createDatasetFromCsv(
                SEATBELT_NOT_USED_RESOURCE_PATH,
                "6.5b/expected.csv",
                jsc(),
                spark());
        assertDatasetEquals(expectedEventDataset, checkedEventDataset);
    }

}
