package com.epam.tcodata.analytics.overtaking.violation.detection.driver;

import com.epam.tcodata.analytics.overtaking.violation.detection.entities.OvertakingMetaData;
import com.epam.tcodata.analytics.overtaking.violation.detection.factory.IOvertakingViolationFactory;
import com.epam.tcodata.analytics.overtaking.violation.detection.policies.area.AreasLookup;
import com.epam.tcodata.analytics.overtaking.violation.detection.policies.area.SpeedZoneArea;
import com.epam.tcodata.analytics.overtaking.violation.detection.policies.overtaking.*;
import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.models.avro.util.AvroSerDeUtil;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.storage.avro.entities.events.overtaking.OvertakingEventAvro;
import com.epam.tcodata.storage.avro.entities.events.overtaking.OvertakingEventWithViolationsAvro;
import com.microsoft.azure.eventhubs.EventData;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.spark.streaming.api.java.JavaDStream;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OvertakingViolationDetectionStreamDriver {

    private OvertakingViolationDetectionStreamDriver(){}

    /**
     * Stream Handler according to DataFactory contract.
     *
     * @param factory
     * @param overtakingEventDataDStream - {@link JavaDStream} of {@link EventData}
     */
    public static void handleOvertakingEventDataDStream(IOvertakingViolationFactory factory,
                                                        JavaDStream<EventData> overtakingEventDataDStream) {
        overtakingEventDataDStream
                .map(EventData::getBytes)
                .map(bytes -> AvroSerDeUtil.deserialize(OvertakingEventAvro.class, bytes))
                .filter(Objects::nonNull)
                .foreachRDD(overtakingEventAvroRDD -> overtakingEventAvroRDD.foreachPartition(
                        iterator -> processOvertakingEvents(iterator, factory)));
    }

    private static void processOvertakingEvents(Iterator<OvertakingEventAvro> overtakingEventAvroIterator,
                                                IOvertakingViolationFactory factory)  {
        AreasLookup lookup = new AreasLookup(factory);
        List<SpeedZoneArea> speedZones = lookup.getSpeedLimitsAreas();
        List<OvertakingEventAvro> overtakingEvents = IteratorUtils.toList(overtakingEventAvroIterator);
        List<OvertakingMetaData> events = overtakingEvents.stream()
                .map(event -> new OvertakingMetaData(event, speedZones))
                .collect(Collectors.toList());
        OvertakingPolicyContainer policies = new OvertakingPolicyContainer();
        registerPolicies(policies, lookup, factory);

        List<OvertakingEventWithViolationsAvro> overtakingEventWithViolationsAvroList = events
                .stream()
                .map(policies::applyActivePolicies)
                .collect(Collectors.toList());

        List<EventData> overtakingWithViolationEventDataList = overtakingEventWithViolationsAvroList.stream()
                .map(AvroSerDeUtil::serialize)
                .map(EventData::create)
                .collect(Collectors.toList());
        ISecretStorage secretStorage = factory.createSecretStorage();
        IEventHub overtakingViolationsEventHub = factory.createOvertakingViolationEventHub(secretStorage);
        if (!overtakingWithViolationEventDataList.isEmpty()) {
            overtakingViolationsEventHub.send(overtakingWithViolationEventDataList);
        }
    }

    private static void registerPolicies(OvertakingPolicyContainer policyContainer, AreasLookup lookup, IOvertakingViolationFactory factory)  {
        policyContainer.registerPolicy(new OvertakingDuringNightPolicy());
        policyContainer.registerPolicy(new OvertakingDuringCommuteHoursPolicy());
        policyContainer.registerPolicy(new SpeedLimitExceededViolation());
        policyContainer.registerPolicy(new VehicleInFrontIsFastViolation());
        policyContainer.registerPolicy(new BusOvertaking(factory));
        policyContainer.registerPolicy(new OvertakingInRoadConditionZone(lookup.getRoadConditionAreas()));
        //  NOSONAR TO DO: register again when all the data will be in Redis
        // NOSONAR  policyContainer.registerPolicy(new OvertakingInNoOvertakingZone(lookup.getNoOvertakingAreas()));
    }
}
