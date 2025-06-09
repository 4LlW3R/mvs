package com.epam.tcodata.event.validator.logic.complex;

import com.epam.tcodata.event.validator.domain.EnrichedPreparedEvent;
import com.holdenkarau.spark.testing.JavaDatasetSuiteBase;
import org.apache.spark.sql.Dataset;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class OverSpeedDetectorTest extends JavaDatasetSuiteBase {

    private static final long serialVersionUID = 3590237119409254506L;

    private static final String OVER_SPEED_RESOURCE_PATH = "src/test/resources/complex/over-speed/";

    @BeforeClass
    public static void init() {
        /***  Default implementation ***/
    }

    @Test
    public void filteringEventsTest() throws IOException {
        Dataset<EnrichedPreparedEvent> eventDataset = DetectorUtils.createDatasetFromCsv(
                OVER_SPEED_RESOURCE_PATH,
                "filtering/input.csv",
                jsc(),
                spark());
        OverSpeedDetector overSpeedDetector = new OverSpeedDetector();
        Dataset<EnrichedPreparedEvent> checkedEventDataset = overSpeedDetector.detect(eventDataset);
        Dataset<EnrichedPreparedEvent> expectedEventDataset = DetectorUtils.createDatasetFromCsv(
                OVER_SPEED_RESOURCE_PATH,
                "filtering/expected.csv",
                jsc(),
                spark());
        assertDatasetEquals(expectedEventDataset, checkedEventDataset);
    }

    @Test
    public void onlyOverSpeedEventsTest() throws IOException {
        Dataset<EnrichedPreparedEvent> eventDataset = DetectorUtils.createDatasetFromCsv(
                OVER_SPEED_RESOURCE_PATH,
                "6.3/input.csv",
                jsc(),
                spark());
        OverSpeedDetector overSpeedDetector = new OverSpeedDetector();
        Dataset<EnrichedPreparedEvent> checkedEventDataset = overSpeedDetector.detect(eventDataset);
        Dataset<EnrichedPreparedEvent> expectedEventDataset = DetectorUtils.createDatasetFromCsv(
                OVER_SPEED_RESOURCE_PATH,
                "6.3/expected.csv",
                jsc(),
                spark());
        assertDatasetEquals(expectedEventDataset, checkedEventDataset);
    }

    @Test
    public void sameTimePreviousEventVelocityNullTest() throws IOException {
        Dataset<EnrichedPreparedEvent> eventDataset = DetectorUtils.createDatasetFromCsv(
                OVER_SPEED_RESOURCE_PATH,
                "6.3a/1/input.csv",
                jsc(),
                spark());
        OverSpeedDetector overSpeedDetector = new OverSpeedDetector();
        Dataset<EnrichedPreparedEvent> checkedEventDataset = overSpeedDetector.detect(eventDataset);
        Dataset<EnrichedPreparedEvent> expectedEventDataset = DetectorUtils.createDatasetFromCsv(
                OVER_SPEED_RESOURCE_PATH,
                "6.3a/1/expected.csv",
                jsc(),
                spark());
        assertDatasetEquals(expectedEventDataset, checkedEventDataset);
    }

    @Test
    public void sameTimeCurrentPreviousEventVelocityNullTest() throws IOException {
        Dataset<EnrichedPreparedEvent> eventDataset = DetectorUtils.createDatasetFromCsv(
                OVER_SPEED_RESOURCE_PATH,
                "6.3a/2/input.csv",
                jsc(),
                spark());
        OverSpeedDetector overSpeedDetector = new OverSpeedDetector();
        Dataset<EnrichedPreparedEvent> checkedEventDataset = overSpeedDetector.detect(eventDataset);
        Dataset<EnrichedPreparedEvent> expectedEventDataset = DetectorUtils.createDatasetFromCsv(
                OVER_SPEED_RESOURCE_PATH,
                "6.3a/2/expected.csv",
                jsc(),
                spark());
        assertDatasetEquals(expectedEventDataset, checkedEventDataset);
    }

    @Test
    public void sameTimeCurrentEventVelocityNotNullTest() throws IOException {
        Dataset<EnrichedPreparedEvent> eventDataset = DetectorUtils.createDatasetFromCsv(
                OVER_SPEED_RESOURCE_PATH,
                "6.3b/input.csv",
                jsc(),
                spark());
        OverSpeedDetector overSpeedDetector = new OverSpeedDetector();
        Dataset<EnrichedPreparedEvent> checkedEventDataset = overSpeedDetector.detect(eventDataset);
        Dataset<EnrichedPreparedEvent> expectedEventDataset = DetectorUtils.createDatasetFromCsv(
                OVER_SPEED_RESOURCE_PATH,
                "6.3b/expected.csv",
                jsc(),
                spark());
        assertDatasetEquals(expectedEventDataset, checkedEventDataset);
    }

}
