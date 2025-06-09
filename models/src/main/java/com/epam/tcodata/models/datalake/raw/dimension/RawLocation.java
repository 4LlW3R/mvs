package com.epam.tcodata.models.datalake.raw.dimension;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.raw.RawEntity;

@SuppressWarnings("CPD-START")
public class RawLocation extends RawEntity {

    private static final long serialVersionUID = -4255755527893753686L;

    public static class Fields {
        public static final String LOCATION_ID = "location_id";
        public static final String GROUP_ID = "group_id";
        public static final String NAME = "name";
        public static final String ADDRESS = "address";
        public static final String LOCATION_TYPE = "location_type";
        public static final String SHARE_TYPE = "shape_type";
        public static final String RADIUS = "radius";
        public static final String SHAPE_WKT = "shape_wkt";
        public static final String DELETED = "deleted";
        public static final String COLOUR_ON_MAP = "colour_on_map";
        public static final String OPACITY_ON_MAP = "opacity_on_map";
        public static final String TEMPORARY = "temporary";
        public static final String EXTERNAL_REFERENCE = "external_reference";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.LOCATION_ID)
    private Long locationId;
    @ColumnName(Fields.GROUP_ID)
    private Long groupId;
    @ColumnName(Fields.NAME)
    private String name;
    @ColumnName(Fields.ADDRESS)
    private String address;
    @ColumnName(Fields.LOCATION_TYPE)
    private String locationType;
    @ColumnName(Fields.SHARE_TYPE)
    private String shapeType;
    @ColumnName(Fields.RADIUS)
    private Double radius;
    @ColumnName(Fields.SHAPE_WKT)
    private String shapeWkt;
    @ColumnName(Fields.DELETED)
    private Boolean deleted;
    @ColumnName(Fields.COLOUR_ON_MAP)
    private String colourOnMap;
    @ColumnName(Fields.OPACITY_ON_MAP)
    private Double opacityOnMap;
    @ColumnName(Fields.TEMPORARY)
    private Boolean temporary;
    @ColumnName(Fields.EXTERNAL_REFERENCE)
    private String externalReference;

    public RawLocation() {
        /***  Default implementation ***/
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getShapeType() {
        return shapeType;
    }

    public void setShapeType(String shapeType) {
        this.shapeType = shapeType;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public String getShapeWkt() {
        return shapeWkt;
    }

    public void setShapeWkt(String shapeWkt) {
        this.shapeWkt = shapeWkt;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getColourOnMap() {
        return colourOnMap;
    }

    public void setColourOnMap(String colourOnMap) {
        this.colourOnMap = colourOnMap;
    }

    public Double getOpacityOnMap() {
        return opacityOnMap;
    }

    public void setOpacityOnMap(Double opacityOnMap) {
        this.opacityOnMap = opacityOnMap;
    }

    public Boolean getTemporary() {
        return temporary;
    }

    public void setTemporary(Boolean temporary) {
        this.temporary = temporary;
    }

    public String getExternalReference() {
        return externalReference;
    }

    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }
}
