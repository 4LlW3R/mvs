package com.epam.tcodata.analytics.overtaking.detection.overtaking.gps;

import com.epam.tcodata.analytics.overtaking.detection.overtaking.common.types.OvertakingEvent;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class PassingPassedVehicleTestCase extends OvertakingTestUsingCSVBase {
    private static final String ID_FOR_NOT_DETECTED_CASE = String.valueOf(Integer.MIN_VALUE);
    private static final String CORRECT_LOOKUP_FILENAME = "overtaking_vehicles_list.csv";
    private static final String FILE_PREFIX_STR_CONST = "File ";

    private static final Map<String, VehicleKey> CORRECT = getCorrect();

    private static Map<String, VehicleKey> getCorrect() {
        String path = PassingPassedVehicleTestCase.class.getResource(MINED_POSITIVE_FOLDER + CORRECT_LOOKUP_FILENAME).getPath();
        File file = new File(path);
        try {
            CSVParser parser = CSVParser.parse(file, StandardCharsets.UTF_8, CSVFormat.DEFAULT.withHeader().withIgnoreHeaderCase());
            List<CSVRecord> records = parser.getRecords();
            return records
                    .stream()
                    .collect(Collectors.toMap(
                            x -> x.get("file"),
                            x -> new VehicleKey(x.get("overtakingVehicle")))
                    );
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid file.");
        }
    }

    //<editor-fold desc="Real overtaking cases that model failed to detect">
    @Ignore
    @Test
    public void testCase4() throws IOException {
        testCorrectPassingReturn("gps_4.csv", "1100_215");
    }

    @Ignore
    @Test
    public void testCase14() throws IOException {
        testCorrectPassingReturn("gps_14.csv", "504_1196");
    }

    @Ignore
    @Test
    public void testCase26() throws IOException {
        testCorrectPassingReturn("gps_26.csv", "3931_23");
    }

    @Ignore
    @Test
    public void testCase29() throws IOException {
        testCorrectPassingReturn("gps_29.csv", "3100_58");
    }
    //</editor-fold>

    //<editor-fold desc="Mined tests from QA testing of overtaking algorithm. All results are correct with respect to linear interpolation.">
    @Test
    public void testCorrectPassingForSamples() throws IOException, URISyntaxException {
        Path path = Paths.get(PassingPassedVehicleTestCase.class.getResource(MINED_POSITIVE_FOLDER).toURI());
        for (Path fn : Files.newDirectoryStream(path)) {
            if (!CORRECT_LOOKUP_FILENAME.equals(fn.getFileName().toString())) {
                testCorrectPassingReturn(fn.getFileName().toString());
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="Real cases successfully detected by model">
    @Test
    public void testCase1() throws IOException {
        testCorrectPassingReturn("gps_1.csv", "1562_231");
    }

    @Test
    public void testCase2() throws IOException {
        testCorrectPassingReturn("gps_2.csv", "4412_52");
    }

    @Test
    public void testCase3() throws IOException {
        testCorrectPassingReturn("gps_3.csv", "4412_26");
    }

    @Test
    public void testCase5() throws IOException {
        testCorrectPassingReturn("gps_5.csv", "1568_65");
    }

    @Test
    public void testCase6() throws IOException {
        testCorrectPassingReturn("gps_6.csv", "3931_118");
    }

    @Test
    public void testCase7() throws IOException {
        testCorrectPassingReturn("gps_7.csv", "1265_252");
    }

    @Test
    public void testCase8() throws IOException {
        testCorrectPassingReturn("gps_8.csv", "3931_249");
    }

    @Test
    public void testCase9() throws IOException {
        testCorrectPassingReturn("gps_9.csv", "3931_118");
    }

    @Test
    public void testCase21() throws IOException {
        testCorrectPassingReturn("gps_21.csv", "1248_55");
    }

    @Test
    public void testCase22() throws IOException {
        testCorrectPassingReturn("gps_22.csv", "3931_79");
    }

    @Test
    public void testCase31() throws IOException {
        testCorrectPassingReturn("gps_31.csv", "1562_231");
    }
    //</editor-fold>

    private boolean isCorrectPassing(VehicleKey key, com.epam.tcodata.storage.avro.entities.events.overtaking.OvertakingEventAvro event) {
        CharSequence actualVehicleId;
        switch (event.getAOvertookB()) {
            case A_OVERTOOK_B:
                actualVehicleId = event.getVehicleDurableIdA();
                break;
            case B_OVERTOOK_A:
                actualVehicleId = event.getVehicleDurableIdB();
                break;
            case NOT_DETECTED:
                actualVehicleId = ID_FOR_NOT_DETECTED_CASE;
                break;
            default:
                throw new IllegalArgumentException("Illegal state of overtaking event: not initialized");

        }
        return actualVehicleId.equals(key.vehicleId);
    }

    private void testCorrectPassingReturn(String fn) throws IOException {
        VehicleKey expected = CORRECT.get(fn);
        if (expected == null) {
            throw new AssertionError(FILE_PREFIX_STR_CONST + fn + " doesn't exists is labelled data.");
        }
        List<OvertakingEvent> events = runDetection(MINED_POSITIVE_FOLDER + fn);
        assertTrue(FILE_PREFIX_STR_CONST + fn + ": overtaking not detected", 1 == events.size());
        assertTrue(FILE_PREFIX_STR_CONST + fn + ": incorrect passing", isCorrectPassing(expected, events.get(0).toAvro()));
    }

    private void testCorrectPassingReturn(String fn, String overtakingVehicle) throws IOException {
        VehicleKey expected = new VehicleKey(overtakingVehicle);
        List<OvertakingEvent> events = runDetection(REAL_OVERTAKINGS_FOLDER + fn);
        assertTrue(FILE_PREFIX_STR_CONST + fn + ": overtaking not detected", 1 == events.size());
        assertTrue(FILE_PREFIX_STR_CONST + fn + ": incorrect passing", isCorrectPassing(expected, events.get(0).toAvro()));
    }

    private static class VehicleKey {
        int subscriptionId;
        String vehicleId;

        VehicleKey(int subscriptionId, String vehicleId) {
            this.subscriptionId = subscriptionId;
            this.vehicleId = vehicleId;
        }

        VehicleKey(String key) {
            String[] splitted = key.split("_");
            this.subscriptionId = Integer.parseInt(splitted[0]);
            this.vehicleId = String.valueOf(splitted[1]);
        }
    }
}
