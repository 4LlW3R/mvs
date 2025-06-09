package com.epam.tcodata.analytics.road.condition.violation.detection;

import com.epam.tcodata.analytics.road.condition.violation.detection.domain.RoadConditionArea;
import com.epam.tcodata.analytics.road.condition.violation.detection.redis.RoadConditionConverter;
import com.epam.tcodata.analytics.road.condition.violation.detection.domain.RoadConditionType;
import com.epam.tcodata.analytics.road.condition.violation.detection.domain.RoadConditionViolation;
import com.epam.tcodata.storage.avro.entities.events.violations.v2.RoadConditionViolationAvro;
import com.esri.core.geometry.GeoJsonImportFlags;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.OperatorBuffer;
import com.esri.core.geometry.OperatorImportFromGeoJson;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.Feature;
import org.geojson.GeoJsonObject;
import org.geojson.GeometryCollection;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AreaTestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(AreaTestCase.class);
    private static final double ONE_METER_IN_DEGREES = 1.0 / 111159.0;
    private static final double LINE_BUFFER_RADIUS = 10.0 * ONE_METER_IN_DEGREES;

    public static RoadConditionArea getExampleArea() {
        RoadConditionArea area = new RoadConditionArea();
        area.setId("1");
        area.setGeometries(getCollection());
        assertNotNull(area.getGeometries());
        area.setRoadConditionType(RoadConditionType.AMBER);
        area.setStartDateTime(DateTime.parse("2000-01-01T22:45:07Z"));
        area.setEndDateTime(DateTime.parse("2000-01-02T14:48:32Z"));
        return area;
    }

    private static List<Geometry> getCollection() {
        String geojson = readResourceAsString("example_geojson_data.json");
        try {
            Feature feature = new ObjectMapper().readValue(geojson, Feature.class);
            GeometryCollection collection = (GeometryCollection) feature.getGeometry();
            List<GeoJsonObject> json = collection.getGeometries();
            List<Geometry> geoms = new ArrayList<>(json.size());
            for (GeoJsonObject js : json) {
                String s = new ObjectMapper().writeValueAsString(js);
                Geometry g = processGeometry(s);
                if (g != null) {
                    geoms.add(g);
                }
            }
            return geoms;
        } catch (IOException e) {
            LOGGER.warn("Error parsing geojson: ", e);
            return null;
        }
    }

    private static String readResourceAsString(String filename) {
        String result = "";
        try {
            result = new String(
                    Files.readAllBytes(
                            Paths.get(
                                    AreaTestCase.class.getResource("/" + filename).toURI()
                            )
                    )
            );
        } catch (URISyntaxException | IOException e) {
            LOGGER.warn("Error parsing test resource: ", e);
        }
        return result;
    }

    private static Geometry processGeometry(String s) {
        Geometry g = OperatorImportFromGeoJson
                             .local()
                             .execute(GeoJsonImportFlags.geoJsonImportDefaults, Geometry.Type.Unknown, s, null)
                             .getGeometry();

        if (g instanceof Polygon) {
            return g;
        } else if (g instanceof Polyline) {
            return OperatorBuffer.local().execute(g, SpatialReference.create(4326), LINE_BUFFER_RADIUS, null);
        } else {
            return null;
        }
    }

    @Test
    public void testCorrectParsingOfArea() {
        RoadConditionArea expected = getExampleArea();
        RoadConditionArea actual = RoadConditionArea.fromJson(readResourceAsString("example_road_condition.json"));
        assertEquals(expected, actual);
    }

    @Test
    public void testCorrectTimestampConversion() {
        DateTime expected = new DateTime("2000-01-01T01:01:01.000Z", DateTimeZone.UTC);
        RoadConditionViolation v = TestData.createViolation(0.0, new Timestamp(1), new Timestamp(1), "1");
        v.setMaxSpeed(0.0);
        v.setCreationTimeUTC(new Timestamp(expected.getMillis()));
        RoadConditionViolationAvro event = RoadConditionConverter.convertToAvro(Arrays.asList(v));
        assertEquals(expected, event.getCreationTime());
    }

    @Test
    public void testCorrectTimestampGetSet() {
        long expectedEpoch = 1;
        Timestamp ts1 = new Timestamp(expectedEpoch);
        Timestamp ts2 = new Timestamp(ts1.getTime());
        assertEquals(expectedEpoch, ts2.getTime());
    }
}
