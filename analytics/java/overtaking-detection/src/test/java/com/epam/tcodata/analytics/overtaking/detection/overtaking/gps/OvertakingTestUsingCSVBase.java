package com.epam.tcodata.analytics.overtaking.detection.overtaking.gps;

import com.epam.tcodata.analytics.overtaking.detection.overtaking.common.types.GPSTripPoint;
import com.epam.tcodata.analytics.overtaking.detection.overtaking.common.types.OvertakingEvent;
import com.epam.tcodata.analytics.overtaking.detection.overtaking.gps.types.GPSTrajectory;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

abstract class OvertakingTestUsingCSVBase {
    static final String MINED_POSITIVE_FOLDER = "/mined/positive/";
    static final String MINED_NEGATIVE_FOLDER = "/mined/negative/";
    static final String REAL_OVERTAKINGS_FOLDER = "/real/";

    /**
     * Returns a GPSTrajectory parsed from CSV file.
     *
     * @param filename - CSV file with GPS positions
     * @return - List of {@link GPSTrajectory}
     * @throws IOException if file not found
     */
    private List<GPSTrajectory> parseGPSTrajectoriesFromCSV(String filename) throws IOException {
        String path = getPathToTestResource(filename);
        File file = new File(path);
        CSVParser parser = CSVParser.parse(file, StandardCharsets.UTF_8, CSVFormat.DEFAULT.withHeader().withIgnoreHeaderCase());
        List<CSVRecord> records = parser.getRecords();
        return records.stream()
                .map(item -> new AbstractMap.SimpleEntry<>(
                        String.valueOf(item.get("VehicleDurableId")),
                        parseCSVRecord(item)))
                .collect(Collectors.groupingBy(AbstractMap.SimpleEntry::getKey, Collectors.toList()))
                .entrySet()
                .stream()
                .map(p -> new GPSTrajectory(
                                p.getValue().stream().map(AbstractMap.SimpleEntry::getValue).toArray(GPSTripPoint[]::new),
                                p.getKey()
                        )
                )
                .collect(Collectors.toList());
    }

    int getOvertakingCount(String filename) throws IOException {
        return runDetection(filename).size();
    }

    List<OvertakingEvent> runDetection(String filename) throws IOException {
        List<GPSTrajectory> trajs = parseGPSTrajectoriesFromCSV(filename);
        List<OvertakingEvent> result = new ArrayList<>();
        for (int i = 0; i < trajs.size() - 1; i++) {
            result.addAll(trajs.get(i).findOvertake(trajs.get(i + 1)));
        }
        return result;
    }

    private String getPathToTestResource(String filename) {
        return OVERTAKINGMinedSamplesTestCase.class.getResource(filename).getPath();
    }

    private static GPSTripPoint parseCSVRecord(CSVRecord csvRecord) {

        return new GPSTripPoint(
                Timestamp.valueOf(csvRecord.get("Time")),
                Double.valueOf(csvRecord.get("Latitude")),
                Double.valueOf(csvRecord.get("Longitude")),
                Double.valueOf(csvRecord.get("Velocity")),
                String.valueOf(csvRecord.get("DriverDurableId")));
    }
}
