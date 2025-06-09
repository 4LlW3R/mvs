package com.epam.tcodata.tools.eventhub.producer;

import com.epam.tcodata.eventhub.dal.EventHubInfo;
import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.eventhub.dal.impl.EventHub;
import com.epam.tcodata.models.avro.util.AvroSerDeUtil;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.factory.ISecretStorageFactory;
import com.microsoft.azure.eventhubs.EventData;
import org.apache.avro.specific.SpecificRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static com.epam.tcodata.tools.eventhub.producer.ConverterToAvro.overtakingViolationCsvToAvro;
import static com.epam.tcodata.tools.eventhub.producer.ConverterToAvro.positionCsvToAvro;

/**
 * Command line tool to push data to EH.
 * Following environment variable must be set to get EH accessKey from KeyVault:
 * VAULT_BASE_URL, CLIENT_ID, CLIENT_SECRET, ENCRYPTION_KEY_ID.
 */
public class EventHubProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventHubProducer.class);

    /**
     * Main method. Used to send json/csv data(specified by filePath/fileType cli args)
     * to EventHub(specified by eventHubInfo cli arg)
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            LOGGER.error( "Too few arguments. Usage: {}\n"
                            + "Where:\n"
                            + "    fileType - json/csv\n"
                            + "    filePath - path to json\n"
                            + "    eventHubInfo - one of {}"
                            + "    for now POSITION/OVERTAKING_VIOLATION are supported",
                        EventHubProducer.class.getSimpleName(), Arrays.toString(EventHubInfo.values()));
            System.exit(1);
        }
        String fileType = args[0];
        String filePath = args[1];
        String eventHubInfoStr = args[2];
        EventHubInfo eventHubInfo = EventHubInfo.valueOf(eventHubInfoStr);

        ISecretStorageFactory factory = ISecretStorageFactory.createDefaultFactory();
        ISecretStorage secretStorage = factory.createSecretStorage(new Properties());

        IEventHub eventHub = new EventHub(eventHubInfo, secretStorage);

        switch (fileType.toUpperCase()) {
            case "JSON":
                byte[] jsonBytes = Files.readAllBytes(Paths.get(filePath));
                EventData eventData = EventData.create(jsonBytes);
                eventHub.send(eventData);
                break;
            case "CSV":
                File file = new File(filePath);
                try (Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
                    Iterable<CSVRecord> csvRecords = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase(true).parse(reader);
                    List<SpecificRecord> avros = new ArrayList<>();
                    switch (eventHubInfo.name().toUpperCase()) {
                        case "POSITION":
                            csvRecords.iterator().forEachRemaining(csvRecord -> avros.add(positionCsvToAvro(csvRecord)));
                            break;
                        case "OVERTAKING_VIOLATION":
                            csvRecords.iterator().forEachRemaining(csvRecord -> avros.add(overtakingViolationCsvToAvro(csvRecord)));
                            break;
                        default:
                            LOGGER.info("For now only POSITION/OVERTAKING_VIOLATION are supported");
                            System.exit(1);
                            break;
                    }
                    List<EventData> eventDatas = avros.stream()
                            .map(AvroSerDeUtil::serialize)
                            .map(EventData::create)
                            .collect(Collectors.toList());
                    eventHub.send(eventDatas);
                }
                break;
            default:
                LOGGER.info("We don't know your type");
                System.exit(1);
                break;
        }
    }
}
