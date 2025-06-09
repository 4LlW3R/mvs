package com.epam.tcodata.road.condition.violation.stream.datalake;

import com.epam.tcodata.eventhub.dal.EventHubInfo;
import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.eventhub.dal.impl.EventHub;
import com.epam.tcodata.models.avro.util.AvroSerDeUtil;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.factory.ISecretStorageFactory;
import com.epam.tcodata.storage.avro.entities.events.violations.v2.RoadConditionViolationAvro;
import com.microsoft.azure.eventhubs.EventData;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Class for sending test data of type {@link com.epam.tcodata.storage.avro.entities.events.violations.v2.RoadConditionViolationAvro}
 * to road-condition-violation EventHub. Use scripts/road-condition-violation-producer.sh to run.
 */
public class RoadConditionViolationEventHubProducer {

    /**
     * Main class. Can be started with scripts/road-condition-violation-producer.sh.
     * The structure of script may need to be corrected depending on the environment, where it will be started.
     *
     * @param args no args required.
     */
    public static void main(String[] args) {
        ISecretStorageFactory defaultFactory = ISecretStorageFactory.createDefaultFactory();
        ISecretStorage secretStorage = defaultFactory.createSecretStorage(new Properties());
        IEventHub eventHub = new EventHub(EventHubInfo.ROAD_CONDITION_VIOLATION, secretStorage);

        List<RoadConditionViolationAvro> avroList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            RoadConditionViolationAvro avro = new RoadConditionViolationAvro();
            avro.setDomain("domain" + i);
            avro.setEntityType(1);
            avro.setSchemaVersion(2);
            avro.setId(String.valueOf(i));
            avro.setSubscriptionId(1L);
            avro.setVehicleId(String.valueOf(i));
            avro.setDriverId(String.valueOf(i));
            avro.setStartTime(DateTime.now());
            avro.setEndTime(DateTime.now().plus(10000));
            avro.setAverageSpeed(50.0);
            avro.setMaxSpeed(100.0);
            avro.setRoadConditionId(String.valueOf(i));
            avro.setCreationTime(DateTime.now());
            avro.setStartFactGpsId(String.valueOf(i));
            avro.setEndFactGpsId(String.valueOf(i));
            avro.setStartLatitude(20.0);
            avro.setStartLongitude(30.0);
            avro.setEndLatitude(40.0);
            avro.setEndLongitude(50.0);

            avroList.add(avro);
        }

        eventHub.send(avroList.stream()
                .map(AvroSerDeUtil::serialize)
                .map(EventData::create)
                .collect(Collectors.toList()));
    }
}
