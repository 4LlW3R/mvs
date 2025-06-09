package com.epam.tcodata.overtaking.violation.stream.datalake;

import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.models.avro.util.AvroSerDeUtil;
import com.epam.tcodata.overtaking.violation.stream.datalake.factory.ConfirmedOvertakingFactory;
import com.epam.tcodata.overtaking.violation.stream.datalake.factory.IConfirmedOvertakingFactory;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.storage.avro.entities.events.overtaking.otdb.AvroConfirmedOvertakingViolation;
import com.epam.tcodata.storage.avro.entities.events.overtaking.otdb.AvroOvertakingParticipant;
import com.epam.tcodata.storage.avro.entities.events.overtaking.otdb.AvroOvertakingPathSegment;
import com.epam.tcodata.storage.avro.entities.events.overtaking.otdb.SourceTypeCode;
import com.microsoft.azure.eventhubs.EventData;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class for sending test data of type {@link AvroConfirmedOvertakingViolation}
 * to confirmed-overtaking-violation EventHub. Use scripts/confirmed-overtaking-violation-producer.sh to run.
 */
public class ConfirmedOvertakingViolationEventHubProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmedOvertakingViolationEventHubProducer.class);

    /**
     * Main class. Can be started with scripts/confirmed-overtaking-violation-producer.sh.
     * The structure of script may need to be corrected depending on the environment, where it will be started.
     *
     * @param args no args required.
     */
    public static void main(String[] args) {
        try {
            IConfirmedOvertakingFactory factory = new ConfirmedOvertakingFactory();
            ISecretStorage secretStorage = factory.createSecretStorage();
            IEventHub eventHub = factory.createEventHub(secretStorage);

            List<AvroConfirmedOvertakingViolation> avroViolationList = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                AvroConfirmedOvertakingViolation avroViolation = new AvroConfirmedOvertakingViolation();
                avroViolation.setDomain("domain" + i);
                avroViolation.setEntityType(1);
                avroViolation.setSchemaVersion(1);
                avroViolation.setId(String.valueOf(i));
                avroViolation.setPolicyVersion(1);
                avroViolation.setSourceType(SourceTypeCode.DETECTED);

                AvroOvertakingParticipant overtakingParticipantA = new AvroOvertakingParticipant();
                overtakingParticipantA.setVehicleDurableID("1");
                overtakingParticipantA.setDriverDurableID("1");
                overtakingParticipantA.setInterpolatedOvertakingVelocity(60.0);
                avroViolation.setPassingParticipant(overtakingParticipantA);

                AvroOvertakingParticipant overtakingParticipantB = new AvroOvertakingParticipant();
                overtakingParticipantB.setVehicleDurableID("2");
                overtakingParticipantB.setDriverDurableID("2");
                overtakingParticipantB.setInterpolatedOvertakingVelocity(60.0);
                avroViolation.setPassedParticipant(overtakingParticipantB);

                AvroOvertakingPathSegment overtakingPathSegmentA = new AvroOvertakingPathSegment();
                overtakingPathSegmentA.setStartLatitude(10.0);
                overtakingPathSegmentA.setStartLongitude(10.0);
                overtakingPathSegmentA.setStartTime(DateTime.now());
                overtakingPathSegmentA.setEndLatitude(20.0);
                overtakingPathSegmentA.setEndLongitude(20.0);
                overtakingPathSegmentA.setEndTime(DateTime.now());
                avroViolation.setPassingTrajectory(overtakingPathSegmentA);

                AvroOvertakingPathSegment overtakingPathSegmentB = new AvroOvertakingPathSegment();
                overtakingPathSegmentB.setStartLatitude(10.0);
                overtakingPathSegmentB.setStartLongitude(10.0);
                overtakingPathSegmentB.setStartTime(DateTime.now());
                overtakingPathSegmentB.setEndLatitude(20.0);
                overtakingPathSegmentB.setEndLongitude(20.0);
                overtakingPathSegmentB.setEndTime(DateTime.now());
                avroViolation.setPassedTrajectory(overtakingPathSegmentB);

                avroViolation.setTime(DateTime.now());
                avroViolation.setLatitude(10.0);
                avroViolation.setLongitude(10.0);

                List<CharSequence> policyAreaIDList = new ArrayList<>();
                policyAreaIDList.add("policy1");
                policyAreaIDList.add("policy2");
                avroViolation.setPolicyAreaIds(policyAreaIDList);

                List<Integer> violationIDList = new ArrayList<>();
                violationIDList.add(1);
                violationIDList.add(2);
                avroViolation.setViolationIDs(violationIDList);

                avroViolation.setValidationComment("comment" + i);
                avroViolation.setUnclassifiedViolationDescription("description" + i);

                avroViolationList.add(avroViolation);
            }

            eventHub.send(avroViolationList.stream()
                    .map(AvroSerDeUtil::serialize)
                    .map(EventData::create)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
