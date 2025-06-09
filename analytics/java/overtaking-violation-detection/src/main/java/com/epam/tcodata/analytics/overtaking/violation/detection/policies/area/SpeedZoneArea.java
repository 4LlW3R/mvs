package com.epam.tcodata.analytics.overtaking.violation.detection.policies.area;

import com.epam.tcodata.analytics.overtaking.violation.detection.policies.overtaking.Utils;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.ogc.OGCGeometry;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.Feature;
import org.geojson.GeoJsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;


public class SpeedZoneArea extends Area implements Serializable, IGeoJsonReadable<SpeedZoneArea> {
    private static final long serialVersionUID = 9164062030195051389L;

    private static final String JSON_PROPERTY_NAME_SPEED_LIMIT = "SpeedLimit";
    private static final Logger LOGGER = LoggerFactory.getLogger(SpeedZoneArea.class);


    private double speedLimit;

    public SpeedZoneArea() {
    }


    /**
     * Full arguments constructor.
     *
     * @param geometry   - {@link Geometry} that defines area.
     * @param id         - String id of that area (by default, it is a "name" field in original shapefile.
     * @param speedLimit - Speed limit in that area.
     */
    public SpeedZoneArea(Geometry geometry, String id, double speedLimit) {
        this.geometry = geometry;
        this.id = id;
        this.speedLimit = speedLimit;
    }

    /**
     * For the erroneous and default areas with no restriction on speed.
     *
     * @param area either {@link Area#AREA_WITHOUT_RESTRICTION} or {@link Area#ERRONEOUS_AREA}.
     */
    private SpeedZoneArea(Area area) {
        this.geometry = area.getGeometry();
        this.id = area.getId();
        this.speedLimit = Utils.DEFAULT_SPEED_LIMIT;
    }


    /**
     * Parses GEOJson "geometry" field to {@link SpeedZoneArea}.
     * @param jsonGeometry GEOJson with polygon definition and meta-data
     * @return newly instantiated {@link SpeedZoneArea}
     */
    public SpeedZoneArea fromJson(String jsonGeometry) {
        try {
            GeoJsonObject geojsonPolygon = new ObjectMapper().readValue(jsonGeometry, GeoJsonObject.class);
            if (geojsonPolygon instanceof Feature) {
                Feature poly = (Feature) geojsonPolygon;
                String geomGeojson = new ObjectMapper().writeValueAsString(poly.getGeometry());
                Geometry geometry = OGCGeometry.fromGeoJson(geomGeojson).getEsriGeometry();
                double speedLimitDoubleVal = (double) poly.getProperties().getOrDefault(JSON_PROPERTY_NAME_SPEED_LIMIT, Utils.DEFAULT_SPEED_LIMIT);
                String id = poly.getId();
                return id == null ? new SpeedZoneArea(ERRONEOUS_AREA) : new SpeedZoneArea(geometry, id, speedLimitDoubleVal);
            } else {
                throw new JsonParseException(
                        String.format("Incorrect GeoJSON type: should be Feature with property \"%s\"",
                                JSON_PROPERTY_NAME_SPEED_LIMIT),
                        JsonLocation.NA);
            }

        } catch (IOException e) {
            LOGGER.info("Error parsing GeoJson geometry for area: {}", e);
            return new SpeedZoneArea(ERRONEOUS_AREA);
        }
    }

    public static SpeedZoneArea getDefaultSpeedZoneArea() {
        return new SpeedZoneArea(Area.AREA_WITHOUT_RESTRICTION);
    }

    public double getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(double speedLimit) {
        this.speedLimit = speedLimit;
    }


}
