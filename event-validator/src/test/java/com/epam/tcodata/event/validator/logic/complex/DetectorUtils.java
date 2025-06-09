package com.epam.tcodata.event.validator.logic.complex;

import com.epam.tcodata.event.validator.domain.EnrichedPreparedEvent;
import com.epam.tcodata.hive.dal.util.HiveCommon;
import com.epam.tcodata.models.datalake.prepared.fact.PreparedEvent;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public final class DetectorUtils {

    private DetectorUtils(){

    }

    public static Dataset<EnrichedPreparedEvent> createDatasetFromCsv(
            String filePath,
            String fileName,
            JavaSparkContext jsc,
            SparkSession sparkSession) throws IOException {
        List<EnrichedPreparedEvent> eventList = readFromCsvFile(filePath, fileName);
        return HiveCommon.entityRddToEntityDataset(jsc.parallelize(eventList), EnrichedPreparedEvent.class, sparkSession);
    }

    private static List<EnrichedPreparedEvent> readFromCsvFile(String filePath, String fileName) throws IOException {
        File file = new File(filePath + fileName);
        CSVParser parser = CSVParser.parse(file, StandardCharsets.UTF_8,
                CSVFormat.DEFAULT.withHeader().withIgnoreHeaderCase());
        List<CSVRecord> records = parser.getRecords();
        return records.stream()
                .map(DetectorUtils::parseCSVRecord)
                .collect(Collectors.toList());
    }

    private static EnrichedPreparedEvent parseCSVRecord(CSVRecord csvRecord) {

        String subscriptionId = csvRecord.get(PreparedEvent.Fields.ORGANIZATION_DURABLE_KEY);
        String vehicleId = csvRecord.get(PreparedEvent.Fields.VEHICLE_DURABLE_KEY);
        String start = csvRecord.get(PreparedEvent.Fields.START_DATE_TIME);
        String problemVehicle = csvRecord.get(EnrichedPreparedEvent.Fields.PROBLEM_VEHICLE);
        String validationCode = csvRecord.get(EnrichedPreparedEvent.Fields.VALIDATION_CODE);
        String velocity = csvRecord.get(PreparedEvent.Fields.START_POSITION_SPEED_KILOMETRES_PER_HOUR);
        String description = csvRecord.get(EnrichedPreparedEvent.Fields.DESCRIPTION);

        EnrichedPreparedEvent enrichedPreparedEvent = new EnrichedPreparedEvent();
        enrichedPreparedEvent.setOrganizationDurableKey(subscriptionId);
        enrichedPreparedEvent.setVehicleDurableKey(vehicleId);
        enrichedPreparedEvent.setStartDateTime(Timestamp.valueOf(start));
        enrichedPreparedEvent.setProblemVehicle(Integer.valueOf(problemVehicle));
        enrichedPreparedEvent.setValidationCode(Integer.valueOf(validationCode));
        enrichedPreparedEvent.setStartPositionSpeedKilometresPerHour(velocity.equals("") ? null : Double.valueOf(velocity));
        enrichedPreparedEvent.setDescription(description);

        return enrichedPreparedEvent;
    }

}
