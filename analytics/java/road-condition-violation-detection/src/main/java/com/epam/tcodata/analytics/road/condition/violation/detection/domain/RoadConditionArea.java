package com.epam.tcodata.analytics.road.condition.violation.detection.domain;

import com.esri.core.geometry.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.Feature;
import org.geojson.GeoJsonObject;
import org.geojson.GeometryCollection;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class RoadConditionArea implements Serializable {

    private static final long serialVersionUID = -6532107133802382606L;

    private static final Logger LOGGER = LoggerFactory.getLogger(RoadConditionArea.class);
    private static final SpatialReference DEFAULT_SPATIAL_REFERENCE = SpatialReference.create(4326);
    private static final double ONE_METER_IN_DEGREES = 1.0 / 111159.0;
    private static final double LINE_BUFFER_RADIUS = 10.0 * ONE_METER_IN_DEGREES;
    private static final OperatorSimpleRelation DISJOINT_OP = OperatorDisjoint.local();
    private static final String ROAD_CONDITION_ID_FIELD = "RoadConditionId";
    private static final String ROAD_CONDITION_START_DATE_FIELD = "EffectiveDateTime";
    private static final String ROAD_CONDITION_END_DATE_FIELD = "EndDateTime";
    private static final String ROAD_CONDITION_TYPE_FIELD = "RoadConditionType";
    private static final RoadConditionArea DEFAULT_ROAD_CONDITION = getDefault();


    private String id;
    private List<Geometry> geometries;
    private RoadConditionType roadConditionType;
    private DateTime startDateTime;
    private DateTime endDateTime;

    /**
     * Parse json without validation. Gets geojson data from "data" attribute.
     *
     * @param s -  json string
     */
    public static RoadConditionArea fromJson(String s) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(s);
            String data = root.get("data").toString();

            Feature feature = mapper.readValue(data, Feature.class);
            RoadConditionArea area = parseProperties(feature.getProperties());

            List<Geometry> geometries = parseGeometry(feature);
            area.setGeometries(geometries);

            return area;
        } catch (IOException e) {
            LOGGER.warn("Exception parsing geojson: ", e);
            return DEFAULT_ROAD_CONDITION;
        }
    }

    private static RoadConditionArea parseProperties(Map<String, Object> props) {
        RoadConditionArea area = new RoadConditionArea();
        area.setId(props.get(ROAD_CONDITION_ID_FIELD).toString());
        area.setStartDateTime(new DateTime(props.get(ROAD_CONDITION_START_DATE_FIELD), DateTimeZone.UTC));
        DateTime endDateTime = props.get(ROAD_CONDITION_END_DATE_FIELD) == null ? null
                : new DateTime(props.get(ROAD_CONDITION_END_DATE_FIELD), DateTimeZone.UTC);
        area.setEndDateTime(endDateTime);
        area.setRoadConditionType(RoadConditionType.valueOf(((String) props.get(ROAD_CONDITION_TYPE_FIELD)).toUpperCase(Locale.ENGLISH)));
        return area;
    }

    private static List<Geometry> parseGeometry(Feature feature) {
        try {
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
        } catch (ClassCastException e) {
            LOGGER.info("Error: geometry {} is not a GeometryCollection", feature.getGeometry());
            LOGGER.warn("Not a GeometryCollection geo json, skipping...");
            return Collections.emptyList();
        } catch (JsonProcessingException e) {
            LOGGER.warn("Error parsing json: {0}", e);
            return Collections.emptyList();
        }
    }

    private static Geometry processGeometry(String s) throws JsonMappingException {
        Geometry g = OperatorImportFromGeoJson
                .local()
                .execute(GeoJsonImportFlags.geoJsonImportDefaults, Geometry.Type.Unknown, s, null)
                .getGeometry();

        if (g instanceof Polygon) {
            return g;
        } else if (g instanceof Polyline) {
            return OperatorBuffer.local().execute(g, DEFAULT_SPATIAL_REFERENCE, LINE_BUFFER_RADIUS, null);
        } else {
            throw new JsonMappingException("Unexpected type of json geometry object.");
        }
    }

    private static RoadConditionArea getDefault() {
        RoadConditionArea area = new RoadConditionArea();
        area.setId("DEFAULT_AREA");
        area.setRoadConditionType(RoadConditionType.AMBER);
        area.setEndDateTime(null);
        area.setStartDateTime(new DateTime(0L));
        area.setGeometries(Collections.emptyList());
        return area;
    }

    /**
     * Return true if point is in one of the areas specified by this road condition.
     */
    public boolean isPointInArea(double lat, double lon) {
        Point p = new Point(lon, lat);
        boolean pointInArea = false;
        for (Geometry g : geometries) {
            pointInArea = pointInArea || !DISJOINT_OP.execute(g, p, DEFAULT_SPATIAL_REFERENCE, null);
        }
        return pointInArea;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Geometry> getGeometries() {
        return geometries;
    }

    public void setGeometries(List<Geometry> geometries) {
        this.geometries = geometries;
    }

    public RoadConditionType getRoadConditionType() {
        return roadConditionType;
    }

    public void setRoadConditionType(RoadConditionType roadConditionType) {
        this.roadConditionType = roadConditionType;
    }

    public DateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(DateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public DateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(DateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoadConditionArea that = (RoadConditionArea) o;
        return Objects.equals(id, that.id)
                && Objects.equals(geometries, that.geometries)
                && roadConditionType == that.roadConditionType
                && Objects.equals(startDateTime, that.startDateTime)
                && Objects.equals(endDateTime, that.endDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, geometries, roadConditionType, startDateTime, endDateTime);
    }

    @Override
    public String toString() {
        return "RoadConditionArea{"
                + "id='" + id + '\''
                + ", geometries=" + geometries
                + ", roadConditionType=" + roadConditionType
                + ", startDateTime=" + startDateTime
                + ", endDateTime=" + endDateTime
                + '}';
    }
}
