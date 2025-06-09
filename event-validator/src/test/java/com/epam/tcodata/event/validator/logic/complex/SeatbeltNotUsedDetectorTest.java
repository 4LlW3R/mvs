package com.epam.tcodata.event.validator.logic.complex;

import com.epam.tcodata.event.validator.domain.EnrichedPreparedEvent;
import com.holdenkarau.spark.testing.JavaDatasetSuiteBase;
import org.apache.spark.sql.Dataset;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class SeatbeltNotUsedDetectorTest extends JavaDatasetSuiteBase {

    private static final long serialVersionUID = -991291238474294587L;

    private static final String SEATBELT_NOT_USED_RESOURCE_PATH = "src/test/resources/complex/seatbelt-not-used/";

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
        SeatbeltNotUsedDetector seatbeltNotUsedDetector = new SeatbeltNotUsedDetector();
        Dataset<EnrichedPreparedEvent> checkedEventDataset = seatbeltNotUsedDetector.detect(eventDataset);
        Dataset<EnrichedPreparedEvent> expectedEventDataset = DetectorUtils.createDatasetFromCsv(
                SEATBELT_NOT_USED_RESOURCE_PATH,
                "filtering/expected.csv",
                jsc(),
                spark());
        assertDatasetEquals(expectedEventDataset, checkedEventDataset);
    }

    @Test
    public void onlySeatbeltNotUsedEventsTest() throws IOException {
        Dataset<EnrichedPreparedEvent> eventDataset = DetectorUtils.createDatasetFromCsv(
                SEATBELT_NOT_USED_RESOURCE_PATH,
                "6.4/input.csv",
                jsc(),
                spark());
        SeatbeltNotUsedDetector seatbeltNotUsedDetector = new SeatbeltNotUsedDetector();
        Dataset<EnrichedPreparedEvent> checkedEventDataset = seatbeltNotUsedDetector.detect(eventDataset);
        Dataset<EnrichedPreparedEvent> expectedEventDataset = DetectorUtils.createDatasetFromCsv(
                SEATBELT_NOT_USED_RESOURCE_PATH,
                "6.4/expected.csv",
                jsc(),
                spark());
        assertDatasetEquals(expectedEventDataset, checkedEventDataset);
    }

    @Test
    public void sameTimePreviousEventVelocityNullTest() throws IOException {
        Dataset<EnrichedPreparedEvent> eventDataset = DetectorUtils.createDatasetFromCsv(
                SEATBELT_NOT_USED_RESOURCE_PATH,
                "6.4a/1/input.csv",
                jsc(),
                spark());
        SeatbeltNotUsedDetector seatbeltNotUsedDetector = new SeatbeltNotUsedDetector();
        Dataset<EnrichedPreparedEvent> checkedEventDataset = seatbeltNotUsedDetector.detect(eventDataset);
        Dataset<EnrichedPreparedEvent> expectedEventDataset = DetectorUtils.createDatasetFromCsv(
                SEATBELT_NOT_USED_RESOURCE_PATH,
                "6.4a/1/expected.csv",
                jsc(),
                spark());
        assertDatasetEquals(expectedEventDataset, checkedEventDataset);
    }

    @Test
    public void sameTimeCurrentPreviousEventVelocityNullTest() throws IOException {
        Dataset<EnrichedPreparedEvent> eventDataset = DetectorUtils.createDatasetFromCsv(
                SEATBELT_NOT_USED_RESOURCE_PATH,
                "6.4a/2/input.csv",
                jsc(),
                spark());
        SeatbeltNotUsedDetector seatbeltNotUsedDetector = new SeatbeltNotUsedDetector();
        Dataset<EnrichedPreparedEvent> checkedEventDataset = seatbeltNotUsedDetector.detect(eventDataset);
        Dataset<EnrichedPreparedEvent> expectedEventDataset = DetectorUtils.createDatasetFromCsv(
                SEATBELT_NOT_USED_RESOURCE_PATH,
                "6.4a/2/expected.csv",
                jsc(),
                spark());
        assertDatasetEquals(expectedEventDataset, checkedEventDataset);
    }

    @Test
    public void sameTimeCurrentEventVelocityNotNullTest() throws IOException {
        Dataset<EnrichedPreparedEvent> eventDataset = DetectorUtils.createDatasetFromCsv(
                SEATBELT_NOT_USED_RESOURCE_PATH,
                "6.4b/input.csv",
                jsc(),
                spark());
        SeatbeltNotUsedDetector seatbeltNotUsedDetector = new SeatbeltNotUsedDetector();
        Dataset<EnrichedPreparedEvent> checkedEventDataset = seatbeltNotUsedDetector.detect(eventDataset);
        Dataset<EnrichedPreparedEvent> expectedEventDataset = DetectorUtils.createDatasetFromCsv(
                SEATBELT_NOT_USED_RESOURCE_PATH,
                "6.4b/expected.csv",
                jsc(),
                spark());
        assertDatasetEquals(expectedEventDataset, checkedEventDataset);
    }

}
