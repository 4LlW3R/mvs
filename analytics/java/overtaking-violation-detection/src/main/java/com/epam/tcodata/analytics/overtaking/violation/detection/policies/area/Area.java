package com.epam.tcodata.analytics.overtaking.violation.detection.policies.area;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Polygon;

import java.io.Serializable;

public abstract class Area implements Serializable {

    public static final Area AREA_WITHOUT_RESTRICTION = new ConstantArea(new Polygon(), "Area without restrictions");
    public static final Area ERRONEOUS_AREA = new ConstantArea(new Polygon(), "Erroneous area: error appeared during parsing");

    private static final long serialVersionUID = -3161064720661066602L;


    public static class ConstantArea extends Area {
        public ConstantArea(Geometry geometry, String id) {
            this.geometry = geometry;
            this.id = id;
        }
    }


    Geometry geometry;
    String id;


    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
