import com.epam.tcodata.analytics.overtaking.violation.detection.entities.OvertakingMetaData;
import com.epam.tcodata.analytics.overtaking.violation.detection.entities.Violation;
import com.epam.tcodata.analytics.overtaking.violation.detection.policies.area.NoOvertakingArea;
import com.epam.tcodata.analytics.overtaking.violation.detection.policies.overtaking.OvertakingDuringCommuteHoursPolicy;
import com.epam.tcodata.analytics.overtaking.violation.detection.policies.overtaking.OvertakingDuringNightPolicy;
import com.epam.tcodata.analytics.overtaking.violation.detection.policies.overtaking.OvertakingInNoOvertakingZone;
import com.epam.tcodata.analytics.overtaking.violation.detection.policies.overtaking.OvertakingPolicyContainer;
import com.epam.tcodata.storage.avro.entities.events.overtaking.OvertakingEventAvro;
import com.epam.tcodata.storage.avro.entities.events.overtaking.OvertakingPathSegmentAvro;
import com.epam.tcodata.storage.avro.entities.events.overtaking.PassingVehicleIdx;
import com.epam.tcodata.storage.events.DomainCode;
import com.epam.tcodata.storage.events.EventType;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ViolationsTestCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(ViolationsTestCase.class);
    private static final String POLYGON_STR_CONST = "/polygons/no_ovt/3.json";

    @Test
    public void testCorrectTimeZoneConversionForCommuteHoursViolations() {
        OvertakingEventAvro violation = getWOViolations();
        violation.setTime(getUTCTime("2017-07-07T13:00:00"));
        OvertakingMetaData meta = new OvertakingMetaData(violation, Collections.emptyList());
        OvertakingPolicyContainer policy = new OvertakingPolicyContainer();
        policy.registerPolicy(new OvertakingDuringCommuteHoursPolicy());
        List<Integer> viols = policy.applyActivePolicies(meta).getViolationIDs();
        assertEquals(1, viols.size());
        assertEquals(Violation.Type.COMMUTE_HOURS_OVERTAKING.getViolationId(), viols.get(0).intValue());
    }

    @Test
    public void testCorrectTimeZoneForNightTimeViolation() {
        OvertakingEventAvro violation = getWOViolations();
        violation.setTime(getUTCTime("2017-07-07T23:00:00"));
        OvertakingMetaData meta = new OvertakingMetaData(violation, Collections.emptyList());
        assertTrue(Violation.Type.NIGHT_TIME_OVERTAKING.getViolationId()
                           == new OvertakingDuringNightPolicy().applyPolicy(meta).getViolationId());
    }

    @Test
    public void testNoViolationsDetected() {
        OvertakingEventAvro noViolations = getWOViolations();
        OvertakingMetaData meta = new OvertakingMetaData(noViolations, Collections.emptyList());
        assertTrue(Violation.NO_VIOLATION.getViolationId()
                           == new OvertakingDuringNightPolicy().applyPolicy(meta).getViolationId());
        assertTrue(Violation.NO_VIOLATION.getViolationId()
                           == new OvertakingDuringCommuteHoursPolicy().applyPolicy(meta).getViolationId());
    }

    @Test
    public void testNoOvertakingZonePolygonViolation() {
        OvertakingEventAvro event = getWOViolations();
        event.setLatitude(46.182429740880735);
        event.setLongitude(53.383332488431286);
        OvertakingMetaData meta = new OvertakingMetaData(event, Collections.emptyList());

        NoOvertakingArea area = new NoOvertakingArea().fromJson(getGeojsonString("/polygons/no_ovt/polygon.json"));

        assertEquals(Violation.Type.NO_OVERTAKING_ZONE_OVERTAKING.getViolationId(),
                new OvertakingInNoOvertakingZone(Collections.singletonList(area)).applyPolicy(meta).getViolationId());
    }

    @Test
    public void testNoOvertakingZoneLineNoViolationDifferentDirection() {
        NoOvertakingArea road5 = new NoOvertakingArea().fromJson(getGeojsonString("/polygons/no_ovt/5.json"));

        OvertakingEventAvro basicCase = getWOViolations();
        basicCase.setLongitude(53.383883705355579);
        basicCase.setLatitude(46.21587727484502);
        basicCase.setTrajectoryA(getPathSegment(53.383618210058003, 46.21575668761426, 53.384123812499446, 46.215981450419875));
        basicCase.setTrajectoryB(getPathSegment(53.383721, 46.215806, 53.384065, 46.215955));
        OvertakingMetaData basicMeta = new OvertakingMetaData(basicCase, Collections.emptyList());

        assertEquals(Violation.Type.NO_VIOLATION.getViolationId(),
                new OvertakingInNoOvertakingZone(Collections.singletonList(road5)).applyPolicy(basicMeta).getViolationId());
    }

    @Test
    public void testNoOvertakingZoneLineViolation() {
        NoOvertakingArea road5 = new NoOvertakingArea().fromJson(getGeojsonString("/polygons/no_ovt/5.json"));

        OvertakingEventAvro basicCase = getWOViolations();
        basicCase.setLongitude(53.383883705355579);
        basicCase.setLatitude(46.21587727484502);
        basicCase.setTrajectoryA(getPathSegment(53.384123812499446, 46.215981450419875, 53.383618210058003, 46.21575668761426));
        basicCase.setTrajectoryB(getPathSegment(53.384065, 46.215955, 53.383721, 46.215806));
        OvertakingMetaData basicMeta = new OvertakingMetaData(basicCase, Collections.emptyList());

        assertEquals(Violation.Type.NO_OVERTAKING_ZONE_OVERTAKING.getViolationId(),
                new OvertakingInNoOvertakingZone(Collections.singletonList(road5)).applyPolicy(basicMeta).getViolationId());
    }

    @Test
    public void testNoOvertakingLineTurn() {
        NoOvertakingArea road3 = new NoOvertakingArea().fromJson(getGeojsonString(POLYGON_STR_CONST));

        OvertakingEventAvro turn = getWOViolations();
        turn.setLongitude(53.379549);
        turn.setLatitude(46.15326);
        turn.setTrajectoryA(getPathSegment(53.379466, 46.155708, 53.382615, 46.152888));
        turn.setTrajectoryB(getPathSegment(53.379466, 46.155409, 53.38213, 46.15292));
        OvertakingMetaData turnMeta = new OvertakingMetaData(turn, Collections.emptyList());

        assertEquals(Violation.Type.NO_OVERTAKING_ZONE_OVERTAKING.getViolationId(),
                new OvertakingInNoOvertakingZone(Collections.singletonList(road3)).applyPolicy(turnMeta).getViolationId());
    }

    @Test
    public void testNoOvertakingZoneLineRoadJunction() {
        NoOvertakingArea road3 = new NoOvertakingArea().fromJson(getGeojsonString(POLYGON_STR_CONST));
        OvertakingEventAvro roadJunction = getWOViolations();

        roadJunction.setLongitude(53.381506099735475);
        roadJunction.setLatitude(46.170075184395607);
        roadJunction.setTrajectoryA(getPathSegment(53.381598624886834, 46.170543082728713, 53.38146586893852, 46.169633501834404));
        roadJunction.setTrajectoryB(getPathSegment(53.381614, 46.170638, 53.381493, 46.169729));
        OvertakingMetaData roadJunctionMeta = new OvertakingMetaData(roadJunction, Collections.emptyList());

        assertEquals(Violation.Type.NO_OVERTAKING_ZONE_OVERTAKING.getViolationId(),
                new OvertakingInNoOvertakingZone(Collections.singletonList(road3)).applyPolicy(roadJunctionMeta).getViolationId());
    }

    @Test
    public void testNoOvertakingZoneLineStartCloseToPolygon() {
        NoOvertakingArea road3 = new NoOvertakingArea().fromJson(getGeojsonString(POLYGON_STR_CONST));
        NoOvertakingArea polygon = new NoOvertakingArea().fromJson(getGeojsonString("/polygons/no_ovt/poly.json"));

        OvertakingEventAvro startCloseToPolygon = getWOViolations();
        startCloseToPolygon.setLongitude(53.383237902457608);
        startCloseToPolygon.setLatitude(46.182112633193711);
        startCloseToPolygon.setTrajectoryA(getPathSegment(53.383263723430076, 46.182405378559309, 53.383190283969313, 46.181841170680705));
        startCloseToPolygon.setTrajectoryB(getPathSegment(53.383307, 46.182609, 53.383165, 46.181695));
        OvertakingMetaData startCloseMeta = new OvertakingMetaData(startCloseToPolygon, Collections.emptyList());

        assertEquals(Violation.Type.NO_OVERTAKING_ZONE_OVERTAKING.getViolationId(),
                new OvertakingInNoOvertakingZone(Arrays.asList(polygon, road3)).applyPolicy(startCloseMeta).getViolationId());
    }

    private OvertakingEventAvro getWOViolations() {
        return OvertakingEventAvro.newBuilder()
                       .setDomain(DomainCode.DOMAIN_OVERTAKING.getDomain())
                       .setEntityType(EventType.OVERTAKING.getEventTypeId())
                       .setSchemaVersion(1)
                       .setId("1_1-2_2")
                       .setVehicleDurableIdA("1")
                       .setVehicleDurableIdB("2")
                       .setDriverDurableIdA("3")
                       .setDriverDurableIdB("3")
                       .setVelocityA(50)
                       .setVelocityB(55)
                       .setTime(getUTCTime("2017-07-07T10:00:00"))
                       .setLatitude(45.86055183410646)
                       .setLongitude(53.1630725860601)
                       .setTrajectoryA(getDefaultPathSegment())
                       .setTrajectoryB(getDefaultPathSegment())
                       .setAOvertookB(PassingVehicleIdx.NOT_DETECTED)
                       .build();
    }

    private DateTime getUTCTime(String dt) {
        DateTime date = DateTime.parse(dt);
        LocalDateTime ldt = new LocalDateTime(date);
        return ldt.toDateTime(DateTimeZone.UTC);
    }

    private OvertakingPathSegmentAvro getDefaultPathSegment() {
        return OvertakingPathSegmentAvro.newBuilder()
                       .setEndTime(new DateTime())
                       .setStartTime(new DateTime())
                       .setEndLongitude(0.0)
                       .setEndLatitude(0.0)
                       .setStartLatitude(0.0)
                       .setStartLongitude(0.0)
                       .build();
    }

    private OvertakingPathSegmentAvro getPathSegment(double stlon, double stlat, double elon, double elat) {
        return OvertakingPathSegmentAvro
                       .newBuilder()
                       .setStartLongitude(stlon)
                       .setStartLatitude(stlat)
                       .setEndLongitude(elon)
                       .setEndLatitude(elat)
                       .setStartTime(DateTime.now(DateTimeZone.UTC))
                       .setEndTime(DateTime.now(DateTimeZone.UTC))
                       .build();
    }

    private String getGeojsonString(String filename) {
        URI path = AreaMappingTestCase.getPathToTestResource(filename);
        String geoJson;
        try {
            geoJson = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            LOGGER.info("Problem with resource files: {}", e);
            geoJson = null;
        }
        return geoJson;
    }


}
