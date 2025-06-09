import com.epam.tcodata.analytics.overtaking.violation.detection.policies.area.Area;
import com.epam.tcodata.analytics.overtaking.violation.detection.policies.area.GeoUtils;
import com.epam.tcodata.analytics.overtaking.violation.detection.policies.area.NoOvertakingArea;
import com.epam.tcodata.analytics.overtaking.violation.detection.policies.area.RoadConditionArea;
import com.epam.tcodata.analytics.overtaking.violation.detection.policies.area.SpeedZoneArea;
import com.epam.tcodata.analytics.overtaking.violation.detection.policies.overtaking.Utils;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.OperatorEquals;
import com.esri.core.geometry.OperatorImportFromJson;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.epam.tcodata.analytics.overtaking.violation.detection.policies.area.GeoUtils.DEFAULT_SPATIAL_REFERENCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AreaMappingTestCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(AreaMappingTestCase.class);
    private static final String PROBLEM_WITH_RESOURCE_FILES_STR_CONST = "Problem with resource files: {}";

    private static Geometry getTestPolygon() {
        Polygon poly = new Polygon();
        poly.startPath(0, 0);
        poly.lineTo(-0.5, 0.5);
        poly.lineTo(0.5, 1);
        poly.lineTo(1, 0.5);
        poly.lineTo(0.5, 0);
        return poly;
    }

    private static List<SpeedZoneArea> getIntersectingSpeedZones() {
        List<SpeedZoneArea> areas = new ArrayList<>();
        Polygon poly1 = new Polygon();
        poly1.startPath(53.424554, 46.317826);
        poly1.lineTo(53.43255, 46.320321);
        poly1.lineTo(53.433904, 46.316312);
        poly1.lineTo(53.42594, 46.314464);
        poly1.lineTo(53.424554, 46.317826);

        Polygon poly2 = new Polygon();
        poly2.startPath(53.42336, 46.318984);
        poly2.lineTo(53.42536, 46.320855);
        poly2.lineTo(53.430583, 46.320454);
        poly2.lineTo(53.428197, 46.317648);
        poly2.lineTo(53.42336, 46.318984);

        Polygon poly3 = new Polygon();
        poly3.startPath(53.425327, 46.305889);
        poly3.lineTo(53.420813, 46.308361);
        poly3.lineTo(53.425327, 46.310254);
        poly3.lineTo(53.427423, 46.307715);
        poly3.lineTo(53.425327, 46.305889);

        areas.add(new SpeedZoneArea(poly1, "1", 0));
        areas.add(new SpeedZoneArea(poly2, "2", 0));
        areas.add(new SpeedZoneArea(poly3, "3", 0));
        return areas;
    }

    static URI getPathToTestResource(String filename) {
        try {
            return AreaMappingTestCase.class.getResource(filename).toURI();
        } catch (URISyntaxException e) {
            LOGGER.info(PROBLEM_WITH_RESOURCE_FILES_STR_CONST, e);
        }
        return null;
    }

    @Test
    public void testErroneousAreaWhenNoId() {
        String geojson = "{\"type\": \"Feature\",\"geometry\":{\"type\": \"Point\",\"coordinates\": [0.0, 0.0]}}";
        assertEquals(Area.ERRONEOUS_AREA.getId(), new SpeedZoneArea().fromJson(geojson).getId());
    }

    @Test
    public void testPointInArea() {
        Geometry polygon = getTestPolygon();
        // point is inside polygon
        assertTrue(GeoUtils.pointIsInArea(0.5, 0.5, polygon));
        // point is on the boundary
        assertTrue(GeoUtils.pointIsInArea(0.5, 0.0, polygon));
    }

    @Test
    public void testPointNotInArea() {
        assertFalse(GeoUtils.pointIsInArea(1, 1, getTestPolygon()));
    }

    @Test
    public void testPointMapsToSpeedZonePolygon() {
        List<SpeedZoneArea> area = Collections.singletonList(new SpeedZoneArea().fromJson(getTestAreaPolygonStringRepresentation("/polygons/test_polygon.json")));
        // point should be in area
        assertEquals(
                area.get(0).getId(),
                Utils.getCorrectSpeedZone(53.383298873902426, 45.87590980529791, area).getId());
        // point should not be in this area
        assertEquals(Area.AREA_WITHOUT_RESTRICTION.getId(), Utils.getCorrectSpeedZone(53.383298873902426, 46.87590980529791, area).getId());
    }

    @Test
    public void testPointMapsToSpeedZoneMultiPolygon() {
        List<SpeedZoneArea> area = Collections.singletonList(new SpeedZoneArea().fromJson(getTestAreaPolygonStringRepresentation("/polygons/test_multipolygon.json")));
        // point should be in area
        assertEquals(
                area.get(0).getId(),
                Utils.getCorrectSpeedZone(53.573884963991055, 45.92300605773929, area).getId());
        // point should not be in this area
        assertEquals(Area.AREA_WITHOUT_RESTRICTION.getId(), Utils.getCorrectSpeedZone(54.383298873902426, 46.87590980529791, area).getId());
    }

    @Test
    public void testEsriJson() {
        URI path = getPathToTestResource("/polygons/esri.json");
        String geoJson;
        try {
            geoJson = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            LOGGER.info(PROBLEM_WITH_RESOURCE_FILES_STR_CONST, e);
            geoJson = null;
        }
        Geometry geometry = OperatorImportFromJson
                .local()
                .execute(Geometry.Type.Unknown, geoJson)
                .getGeometry();
        assertEquals(Geometry.Type.Polygon, geometry.getType());
    }

    @Test
    public void testMappingToMultiplePolygons() {
        String actual = Utils.getCorrectSpeedZone(53.427842, 46.318717, getIntersectingSpeedZones()).getId();
        assertEquals("1", actual);
    }

    /**
     * Bug 61580: Bug: OVT detection job: crash on empty speed limits collection
     */
    @Test
    public void testMappingOfEmptyCollectionBug61580() {
        //arrange
        double inputAnyLat = 53.0;
        double inputAnyLongitude = 46.0;

        List<SpeedZoneArea> inputEmptySpeedLimitsList = new ArrayList<>();

        //act
        String actual = Utils.getCorrectSpeedZone(inputAnyLat, inputAnyLongitude, inputEmptySpeedLimitsList).getId();

        //assert
        //actually - there should not be exception "java.util.NoSuchElementException: No value present"
        assertEquals(Area.AREA_WITHOUT_RESTRICTION.getId(), actual);
    }

    @Test
    public void testCorrectParsingOfReversedPolyline() {
        NoOvertakingArea area = new NoOvertakingArea().fromJson(getTestAreaPolygonStringRepresentation("/polygons/no_ovt/reversed_polyline.json"));
        assertEquals("3", area.getId());
        assertTrue(area.getGeometry() instanceof Polyline);
        Polyline line = (Polyline) area.getGeometry();
        OperatorEquals eq = OperatorEquals.local();
        assertTrue(eq.execute(line.getPoint(0), new Point(53.38322954597226, 46.1824380265098), DEFAULT_SPATIAL_REFERENCE, null));
        assertTrue(eq.execute(line.getPoint(line.getPointCount() - 1), new Point(53.40510146997796, 46.151349144962474), DEFAULT_SPATIAL_REFERENCE, null));
    }

    @Test
    public void testFailureForIncorrectPolyline() {
        NoOvertakingArea area = new NoOvertakingArea().fromJson(getTestAreaPolygonStringRepresentation("/polygons/no_ovt/incorrect_polyline.json"));
        assertEquals(area.getId(), Area.ERRONEOUS_AREA.getId());
    }

    @Test
    public void correctParsingOfUuidFromRc() {
        RoadConditionArea area = new RoadConditionArea().fromJson(getTestAreaPolygonStringRepresentation("/polygons/rc/correct_rc.json"));
        assertEquals("80108", area.getRoadConditionId());
        assertEquals("827cf095-6cdb-4287-b744-6af8bc57c890", area.getUuid());
    }

    private String getTestAreaPolygonStringRepresentation(String filename) {
        URI path = getPathToTestResource(filename);
        String geoJson;
        try {
            geoJson = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            LOGGER.info(PROBLEM_WITH_RESOURCE_FILES_STR_CONST, e);
            geoJson = null;
        }
        return geoJson;
    }
}
