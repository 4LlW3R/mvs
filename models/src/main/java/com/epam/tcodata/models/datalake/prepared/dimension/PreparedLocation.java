package com.epam.tcodata.models.datalake.prepared.dimension;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.prepared.PreparedEntity;

import java.sql.Timestamp;

@SuppressWarnings("CPD-START")
public class PreparedLocation extends PreparedEntity {

    private static final long serialVersionUID = -3043989622557226179L;

    public static class Fields {
        public static final String ORGANIZATION_DURABLE_KEY = "organization_durable_key";
        public static final String EXTERNAL_ID = "external_id";
        public static final String PERSISTED_DATE_UTC = "persisted_date_utc";
        public static final String ORG_GROUP_DURABLE_KEY = "org_group_durable_key";
        public static final String NAME = "name";
        public static final String ADDRESS = "address";
        public static final String LOCATION_TYPE_DURABLE_KEY = "location_type_durable_key";
        public static final String LOCATION_TYPE_CODE = "location_type_code";
        public static final String SHAPE_TYPE_DURABLE_ID = "shape_type_durable_id";
        public static final String SHAPE_TYPE_CODE = "shape_type_code";
        public static final String RADIUS = "radius";
        public static final String SHAPE_WKT = "shape_wkt";
        public static final String DELETED = "deleted";
        public static final String COLOUR_ON_MAP = "colour_on_map";
        public static final String OPACITY_ON_MAP = "opacity_on_map";
        public static final String TEMPORARY = "temporary";
        public static final String EXTERNAL_REFERENCE = "external_reference";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.ORGANIZATION_DURABLE_KEY)
    private String organizationDurableKey;
    @ColumnName(Fields.EXTERNAL_ID)
    private Long externalId;
    @ColumnName(Fields.PERSISTED_DATE_UTC)
    private Timestamp persistedDateUtc;
    @ColumnName(Fields.ORG_GROUP_DURABLE_KEY)
    private String orgGroupDurableKey;
    @ColumnName(Fields.NAME)
    private String name;
    @ColumnName(Fields.ADDRESS)
    private String address;
    @ColumnName(Fields.LOCATION_TYPE_DURABLE_KEY)
    private String locationTypeDurableKey;
    @ColumnName(Fields.LOCATION_TYPE_CODE)
    private String locationTypeCode;
    @ColumnName(Fields.SHAPE_TYPE_DURABLE_ID)
    private String shapeTypeDurableId;
    @ColumnName(Fields.SHAPE_TYPE_CODE)
    private String shapeTypeCode;
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

    public PreparedLocation() {
        /***  Default implementation ***/
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getOrganizationDurableKey() {
        return organizationDurableKey;
    }

    public void setOrganizationDurableKey(String organizationDurableKey) {
        this.organizationDurableKey = organizationDurableKey;
    }

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    public Timestamp getPersistedDateUtc() {
        return persistedDateUtc;
    }

    public void setPersistedDateUtc(Timestamp persistedDateUtc) {
        this.persistedDateUtc = persistedDateUtc;
    }

    public String getOrgGroupDurableKey() {
        return orgGroupDurableKey;
    }

    public void setOrgGroupDurableKey(String orgGroupDurableKey) {
        this.orgGroupDurableKey = orgGroupDurableKey;
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

    public String getLocationTypeDurableKey() {
        return locationTypeDurableKey;
    }

    public void setLocationTypeDurableKey(String locationTypeDurableKey) {
        this.locationTypeDurableKey = locationTypeDurableKey;
    }

    public String getLocationTypeCode() {
        return locationTypeCode;
    }

    public void setLocationTypeCode(String locationTypeCode) {
        this.locationTypeCode = locationTypeCode;
    }

    public String getShapeTypeDurableId() {
        return shapeTypeDurableId;
    }

    public void setShapeTypeDurableId(String shapeTypeDurableId) {
        this.shapeTypeDurableId = shapeTypeDurableId;
    }

    public String getShapeTypeCode() {
        return shapeTypeCode;
    }

    public void setShapeTypeCode(String shapeTypeCode) {
        this.shapeTypeCode = shapeTypeCode;
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
