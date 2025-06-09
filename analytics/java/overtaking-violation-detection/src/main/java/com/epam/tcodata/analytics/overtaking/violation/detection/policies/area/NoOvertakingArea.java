package com.epam.tcodata.analytics.overtaking.violation.detection.policies.area;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.JsonGeometryException;
import com.esri.core.geometry.OperatorDensifyByLength;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.ogc.OGCGeometry;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.math3.util.Precision;
import org.geojson.Feature;
import org.geojson.GeoJsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;

public class NoOvertakingArea extends Area implements Serializable, IGeoJsonReadable<NoOvertakingArea> {
    private static final long serialVersionUID = -363422201588406016L;

    private static final Logger LOGGER = LoggerFactory.getLogger(NoOvertakingArea.class);

    private static final String ROAD_FIELD_NAME = "road";
    private static final String START_LAT_FIELD_NAME = "start_lat";
    private static final String START_LON_FIELD_NAME = "start_lon";
    private static final String END_LAT_FIELD_NAME = "end_lat";
    private static final String END_LON_FIELD_NAME = "end_lon";
    private static final Double LENGTH_PRECISION_FOR_ROAD_SNAPPING = 5 * GeoUtils.ONE_METER_IN_DEGREES;
    private static final double DECIMAL_DEGREES_PRECISION = 1e-6;

    private boolean hasDirection;
    // TO DO: change to road ID when corresponding schemas will be implemented
    private String roadName;

    public NoOvertakingArea() {}

    /**
     * All arguments constructor.
     */
    public NoOvertakingArea(Geometry geometry, String id, boolean hasDirection, String roadName) {
        this.geometry = geometry;
        this.id = id;
        this.hasDirection = hasDirection;
        this.roadName = roadName;
    }

    private NoOvertakingArea(Area area) {
        this.geometry = area.getGeometry();
        this.id = area.getId();
        this.hasDirection = false;
    }

    /**
     * Parses GeoJSON representation of No Overtaking zone.
     *
     * @param geojson string with valid GeoJSON
     * @return {@link NoOvertakingArea}
     */
    public NoOvertakingArea fromJson(String geojson) {
        try {
            GeoJsonObject jsonFeature = new ObjectMapper().readValue(geojson, GeoJsonObject.class);
            if (jsonFeature instanceof Feature) {
                Feature poly = (Feature) jsonFeature;
                String geomGeojson = new ObjectMapper().writeValueAsString(poly.getGeometry());
                Geometry geometry = OGCGeometry.fromGeoJson(geomGeojson).getEsriGeometry();

                boolean hasDirectionFlag;
                if (geometry instanceof Polyline) {
                    geometry = initPolyline(poly, geometry);
                    hasDirectionFlag = true;
                    // densification for future analysis (snapping to the road, choosing correct segment
                    geometry = OperatorDensifyByLength.local().execute(geometry, LENGTH_PRECISION_FOR_ROAD_SNAPPING, null);
                } else if (geometry instanceof Polygon) {
                    hasDirectionFlag = false;
                } else {
                    throw new JsonGeometryException("Incorrect geometry type. Should be LineString, MultiLine or Polygon.");
                }
                String road = poly.getProperty(ROAD_FIELD_NAME);
                String id = poly.getId();
                return id == null ? new NoOvertakingArea(ERRONEOUS_AREA) : new NoOvertakingArea(geometry, id, hasDirectionFlag, road);
            } else {
                throw new JsonParseException(
                        String.format("Incorrect GeoJSON type: should be Feature with property \"%s\"", ROAD_FIELD_NAME),
                        JsonLocation.NA);
            }
        } catch (IOException | JsonGeometryException e) {
            LOGGER.info("Error parsing GeoJson geometry for area: {}", e);
            return new NoOvertakingArea(ERRONEOUS_AREA);
        }
    }

    private static Geometry initPolyline(Feature feature, Geometry geometry) {
        double startLat = feature.getProperty(START_LAT_FIELD_NAME);
        double startLon = feature.getProperty(START_LON_FIELD_NAME);
        double endLat = feature.getProperty(END_LAT_FIELD_NAME);
        double endLon = feature.getProperty(END_LON_FIELD_NAME);

        Polyline line = (Polyline) geometry;
        boolean isValid = checkPolylinePointOrder(startLat, startLon, endLat, endLon, line);

        if (isValid) {
            return line;
        } else {
            line.reverseAllPaths();
            if (checkPolylinePointOrder(startLat, startLon, endLat, endLon, line)) return line;
            else throw new JsonGeometryException("Incorrect polyline definition - startLatitude and startLongitude "
                                                         + "doesn't correspond to start of the polyline nor end of polyline.");
        }
    }

    private static boolean checkPolylinePointOrder(double startLat, double startLon, double endLat, double endLon, Polyline geometry) {
        double stLineLat = geometry.getPoint(0).getY();
        double stLineLon = geometry.getPoint(0).getX();
        double endLineLat = geometry.getPoint(geometry.getPointCount() - 1).getY();
        double endLineLon = geometry.getPoint(geometry.getPointCount() - 1).getX();

        return Precision.equals(startLat, stLineLat, DECIMAL_DEGREES_PRECISION)
                       && Precision.equals(startLon, stLineLon, DECIMAL_DEGREES_PRECISION)
                       && Precision.equals(endLat, endLineLat, DECIMAL_DEGREES_PRECISION)
                       && Precision.equals(endLon, endLineLon, DECIMAL_DEGREES_PRECISION);
    }

    public static NoOvertakingArea getDefaultArea() {
        return new NoOvertakingArea(Area.AREA_WITHOUT_RESTRICTION);
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public boolean hasDirection() {
        return hasDirection;
    }

    public void setHasDirection(boolean hasDirection) {
        this.hasDirection = hasDirection;
    }
}
