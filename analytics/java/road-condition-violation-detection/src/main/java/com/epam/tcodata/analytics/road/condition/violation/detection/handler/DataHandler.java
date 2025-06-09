package com.epam.tcodata.analytics.road.condition.violation.detection.handler;

import com.epam.tcodata.analytics.road.condition.violation.detection.domain.*;
import com.epam.tcodata.analytics.road.condition.violation.detection.factory.IRoadConditionViolationFactory;
import com.epam.tcodata.analytics.road.condition.violation.detection.redis.RoadConditionConverter;
import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.models.avro.fact.AvroPosition;
import com.epam.tcodata.models.avro.util.AvroSerDeUtil;
import com.epam.tcodata.redis.dal.IRedis;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.storage.avro.entities.events.violations.v2.RoadConditionViolationAvro;
import com.microsoft.azure.eventhubs.EventData;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class DataHandler {
    private DataHandler(){}
    private static final Logger LOGGER = LoggerFactory.getLogger(DataHandler.class);

    private static final String REDIS_ROAD_CONDITION_AREAS_KEY = "roadConditionsHashMap";
    private static final String KEY_TEMPLATE_SUBSCRIPTION_VEHICLE_TYPE = "%s-%s-%s";
    private static final String RED_STATUS_MARKER = RoadConditionType.RED.getName();
    private static final int SPEED_LIMIT_IN_AMBER = 40;

    /**
     * Main handler for the rcdb violation stream.
     *
     * @param eventDataDStream - {@link JavaDStream} of {@link EventData}
     */
    public static void handle(JavaDStream<EventData> eventDataDStream, IRoadConditionViolationFactory factory) throws Exception {
        JavaDStream<AvroPosition> avroPositionRDD = eventDataDStream
                .map(EventData::getBytes)
                .map(bytes -> AvroSerDeUtil.deserialize(AvroPosition.class, bytes))
                .persist(StorageLevel.MEMORY_AND_DISK_SER());

        JavaDStream<GPSPointWithArea> violStream = avroPositionRDD
                .map(RoadConditionConverter::convertToGPSPoint)
                .mapPartitions(getPointsInRoadConditionAreas(factory));

        AtomicLong count = new AtomicLong(0);
        violStream.foreachRDD(rdd -> {
            long rddCount = rdd.count();
            count.addAndGet(rddCount);
        });
        LOGGER.info("####### 1 ######### RC Violation Stream size: {}", count.get());
        System.out.println("####### 2 ######### RC Violation Stream size: " + count.get());
        LOGGER.info("####### 3 ######### RC Violation Stream size: {}", violStream.countByValue().count());
        System.out.println("####### 4 ######### RC Violation Stream size: " + violStream.countByValue().count());
        violStream.foreachRDD(rdd -> validateAgainstViolationPolicies(rdd, factory));
    }

    private static FlatMapFunction<Iterator<GPSPoint>, GPSPointWithArea> getPointsInRoadConditionAreas(IRoadConditionViolationFactory factory) {
        return iter -> {
            ISecretStorage secretStorage = factory.createSecretStorage();
            IRedis redis = factory.createAreaRedis(secretStorage);
            List<RoadConditionArea> areas = getRoadConditionAreas(redis);
            LOGGER.info("####### 5 ######### Areas to String size: {} and list is : {}", areas.size(), areas);
            System.out.println("####### 6 ######### Areas to String: " + areas.size() + " ----> " + areas);

            List<GPSPointWithArea> result = new LinkedList<>();
            iter.forEachRemaining(point -> {
                LOGGER.info("####### 7 ######### Point to String is : {}", point.toString());
                System.out.println("####### 8 ######### Point to String is : " + point.toString());

                GPSPointWithArea p = mapToArea(point, areas);
                if (!GPSPointWithArea.DEFAULT_AREA_TYPE.equals(p.getAreaType())) {
                    result.add(p);
                }
            });
            LOGGER.info("Points in area in this partition: {}", result.size());
            return result.iterator();
        };
    }

    private static List<RoadConditionArea> getRoadConditionAreas(IRedis redis) {
        Map<String, String> rcAreas = redis.getMap(REDIS_ROAD_CONDITION_AREAS_KEY);
        LOGGER.info("Areas from redis loaded: {}", rcAreas.size());
        return rcAreas
                .values()
                .stream()
                .map(RoadConditionArea::fromJson)
                .collect(Collectors.toList());
    }

    private static GPSPointWithArea mapToArea(GPSPoint point, List<RoadConditionArea> policyAreas) {
        for (RoadConditionArea area : policyAreas) {
            long pointTs = point.getTime().getTime();
            boolean areaContainsPoint = area.isPointInArea(point.getLatitude(), point.getLongitude());
            LOGGER.info("####### 9 ######### mapToArea.areaContainsPoint: {}", areaContainsPoint);
            System.out.println("####### 10 ######### mapToArea.areaContainsPoint: " + areaContainsPoint);

            boolean pointAfterConditionStarted = pointTs > area.getStartDateTime().getMillis();
            boolean pointBeforeConditionEnded = area.getEndDateTime() == null
                    || pointTs < area.getEndDateTime().getMillis();
            if (areaContainsPoint && pointAfterConditionStarted && pointBeforeConditionEnded) {
                return new GPSPointWithArea(point, area.getId(), area.getRoadConditionType().getName());
            }
        }
        LOGGER.info("####### 11 ######### mapToArea NOT IN POLICY");
        System.out.println("####### 12 ######### mapToArea NOT IN POLICY");
        return new GPSPointWithArea(point);
    }

    private static void validateAgainstViolationPolicies(JavaRDD<GPSPointWithArea> pointsInRoadConditionAreasRDD,
                                                         IRoadConditionViolationFactory factory) {
        pointsInRoadConditionAreasRDD
                .groupBy(groupByVehicleAndAreaType())
                .flatMapValues(aggregateViolatingTrajectories())
                .values()
                .map(RoadConditionConverter::convertToAvro)
                .foreachPartition(sendToEventHub(factory));
    }

    private static VoidFunction<Iterator<RoadConditionViolationAvro>> sendToEventHub(IRoadConditionViolationFactory factory) {
        return iter -> {
            ISecretStorage secretStorage = factory.createSecretStorage();
            IEventHub eventHub = factory.createRoadConditionEventHub(secretStorage);
            List<RoadConditionViolationAvro> avroList = IteratorUtils.toList(iter);
            avroList.removeIf(Objects::isNull);
            LOGGER.info("#RC VIOLATION DETECTION# avro list: {}", avroList.toString());
            if (!avroList.isEmpty()) {
                eventHub.send(avroList.stream()
                        .map(avro -> AvroSerDeUtil.serialize(avro))
                        .map(EventData::create)
                        .collect(Collectors.toList()));
            }
        };
    }

    private static Function<GPSPointWithArea, String> groupByVehicleAndAreaType() {
        return point -> String.format(
                KEY_TEMPLATE_SUBSCRIPTION_VEHICLE_TYPE,
                point.getSubscriptionId(),
                point.getVehicleId(),
                point.getAreaType());
    }
    private static FlatMapFunction<Iterable<GPSPointWithArea>, Iterable<RoadConditionViolation>> aggregateViolatingTrajectories() {
        return iter -> {
            Iterator<GPSPointWithArea> iterator = iter.iterator();
            List<RoadConditionViolation> traj = aggregateTrajectory(iterator);
            Iterable<RoadConditionViolation> itt = (Iterable<RoadConditionViolation>) traj;
            Iterator<Iterable<RoadConditionViolation>>
                    res = Arrays.asList(itt)
                    .iterator();
            return  res;
        };
    }

    /**
     * Sorts trajectory creating state. Storing all data in the memory to correctly handle late data.
     */
    public static List<RoadConditionViolation> aggregateTrajectory(Iterator<GPSPointWithArea> iter) throws Exception {
        List<GPSPointWithArea> trajectory = createTrajectory(iter, new ArrayList<>());
        if (trajectory.isEmpty()) {
            return Collections.emptyList();
        } else {
            String areaType = trajectory.get(0).getAreaType();
            boolean isInRed = RED_STATUS_MARKER.equals(areaType);
            return createFromTrajectory(trajectory, isInRed);
        }
    }

    private static List<GPSPointWithArea> createTrajectory(Iterator<GPSPointWithArea> iter, List<GPSPointWithArea> prev) {
        List<GPSPointWithArea> trajectory = new ArrayList<>(prev);
        iter.forEachRemaining(trajectory::add);
        trajectory.sort(Comparator.comparing(GPSPointWithArea::getTime));
        return trajectory;
    }

    private static List<RoadConditionViolation> createFromTrajectory(List<GPSPointWithArea> trajectory, boolean isInRedZone) {
        if (isInRedZone) {
            return Collections.singletonList(RoadConditionViolation.fromTrajectory(trajectory, Timestamp.from(Instant.now())));
        } else {
            int[] separatorIndices = getSeparatingIndices(trajectory);
            return segmentTrajectoryByIndices(trajectory, separatorIndices);
        }
    }

    private static int[] getSeparatingIndices(List<GPSPointWithArea> trajectory) {
        return Stream
                .of(IntStream.of(-1),
                        IntStream
                                .range(0, trajectory.size())
                                .filter(i -> trajectory.get(i).getVelocity() < SPEED_LIMIT_IN_AMBER),
                        IntStream.of(trajectory.size()))
                .flatMapToInt(s -> s)
                .toArray();
    }

    private static List<RoadConditionViolation> segmentTrajectoryByIndices(List<GPSPointWithArea> trajectory, int[] indices) {
        return IntStream
                .range(0, indices.length - 1)
                .mapToObj(i -> {
                    List<GPSPointWithArea> part = trajectory.subList(indices[i] + 1, indices[i + 1]);
                    if (!part.isEmpty()) {
                        return RoadConditionViolation.fromTrajectory(part, Timestamp.from(Instant.now()));
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
