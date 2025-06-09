package com.epam.tcodata.models.mix.dimension;

import com.epam.tcodata.models.mix.Entity;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Maps to SOAP Location.
 */
public class Location extends Entity {

    private static final long serialVersionUID = 8708146058709731246L;

    private Long locationId;
    private Long organisationId;
    private Long groupId;
    private String name;
    private String address;
    private String locationType;
    private String shapeType;
    private Double radius;
    private String shapeWkt;
    @JsonProperty("IsDeleted")
    private Boolean deleted;
    private String colourOnMap;
    private Double opacityOnMap;
    @JsonProperty("IsTemporary")
    private Boolean temporary;
    private String externalReference;

    public Location() {
        /***  Default implementation ***/
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(Long organisationId) {
        this.organisationId = organisationId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (locationId != null ? !locationId.equals(location.locationId) : location.locationId != null) return false;
        if (organisationId != null ? !organisationId.equals(location.organisationId) : location.organisationId != null)
            return false;
        if (groupId != null ? !groupId.equals(location.groupId) : location.groupId != null) return false;
        if (name != null ? !name.equals(location.name) : location.name != null) return false;
        if (address != null ? !address.equals(location.address) : location.address != null) return false;
        if (locationType != null ? !locationType.equals(location.locationType) : location.locationType != null)
            return false;
        if (shapeType != null ? !shapeType.equals(location.shapeType) : location.shapeType != null) return false;
        if (radius != null ? !radius.equals(location.radius) : location.radius != null) return false;
        if (shapeWkt != null ? !shapeWkt.equals(location.shapeWkt) : location.shapeWkt != null) return false;
        if (deleted != null ? !deleted.equals(location.deleted) : location.deleted != null) return false;
        if (colourOnMap != null ? !colourOnMap.equals(location.colourOnMap) : location.colourOnMap != null)
            return false;
        if (opacityOnMap != null ? !opacityOnMap.equals(location.opacityOnMap) : location.opacityOnMap != null)
            return false;
        if (temporary != null ? !temporary.equals(location.temporary) : location.temporary != null)
            return false;
        return externalReference != null ? externalReference.equals(location.externalReference) : location.externalReference == null;
    }

    @Override
    public int hashCode() {
        int result = locationId != null ? locationId.hashCode() : 0;
        result = 31 * result + (organisationId != null ? organisationId.hashCode() : 0);
        result = 31 * result + (groupId != null ? groupId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (locationType != null ? locationType.hashCode() : 0);
        result = 31 * result + (shapeType != null ? shapeType.hashCode() : 0);
        result = 31 * result + (radius != null ? radius.hashCode() : 0);
        result = 31 * result + (shapeWkt != null ? shapeWkt.hashCode() : 0);
        result = 31 * result + (deleted != null ? deleted.hashCode() : 0);
        result = 31 * result + (colourOnMap != null ? colourOnMap.hashCode() : 0);
        result = 31 * result + (opacityOnMap != null ? opacityOnMap.hashCode() : 0);
        result = 31 * result + (temporary != null ? temporary.hashCode() : 0);
        result = 31 * result + (externalReference != null ? externalReference.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Location{"
                + "locationId=" + locationId
                + ", organisationId=" + organisationId
                + ", groupId=" + groupId
                + ", name='" + name + '\''
                + ", address='" + address + '\''
                + ", locationType=" + locationType
                + ", shapeType=" + shapeType
                + ", radius=" + radius
                + ", shapeWkt='" + shapeWkt + '\''
                + ", deleted=" + deleted
                + ", colourOnMap='" + colourOnMap + '\''
                + ", opacityOnMap=" + opacityOnMap
                + ", temporary=" + temporary
                + ", externalReference='" + externalReference + '\''
                + "} " + super.toString();
    }


    public static final class LocationBuilder {
        private Long locationId;
        private Long organisationId;
        private Long groupId;
        private String name;
        private String address;
        private String locationType;
        private String shapeType;
        private Double radius;
        private String shapeWkt;
        private Boolean deleted;
        private String colourOnMap;
        private Double opacityOnMap;
        private Boolean temporary;
        private String externalReference;

        public LocationBuilder() {
            /***  Default implementation ***/
        }

        public LocationBuilder setLocationId(Long locationId) {
            this.locationId = locationId;
            return this;
        }

        public LocationBuilder setOrganisationId(Long organisationId) {
            this.organisationId = organisationId;
            return this;
        }

        public LocationBuilder setGroupId(Long groupId) {
            this.groupId = groupId;
            return this;
        }

        public LocationBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public LocationBuilder setAddress(String address) {
            this.address = address;
            return this;
        }

        public LocationBuilder setLocationType(String locationType) {
            this.locationType = locationType;
            return this;
        }

        public LocationBuilder setShapeType(String shapeType) {
            this.shapeType = shapeType;
            return this;
        }

        public LocationBuilder setRadius(Double radius) {
            this.radius = radius;
            return this;
        }

        public LocationBuilder setShapeWkt(String shapeWkt) {
            this.shapeWkt = shapeWkt;
            return this;
        }

        public LocationBuilder setDeleted(Boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        public LocationBuilder setColourOnMap(String colourOnMap) {
            this.colourOnMap = colourOnMap;
            return this;
        }

        public LocationBuilder setOpacityOnMap(Double opacityOnMap) {
            this.opacityOnMap = opacityOnMap;
            return this;
        }

        public LocationBuilder setTemporary(Boolean temporary) {
            this.temporary = temporary;
            return this;
        }

        public LocationBuilder setExternalReference(String externalReference) {
            this.externalReference = externalReference;
            return this;
        }

        /**
         * Build entity with specified parameters.
         * @return new entity.
         */
        public Location build() {
            Location location = new Location();
            location.setLocationId(locationId);
            location.setOrganisationId(organisationId);
            location.setGroupId(groupId);
            location.setName(name);
            location.setAddress(address);
            location.setLocationType(locationType);
            location.setShapeType(shapeType);
            location.setRadius(radius);
            location.setShapeWkt(shapeWkt);
            location.setDeleted(deleted);
            location.setColourOnMap(colourOnMap);
            location.setOpacityOnMap(opacityOnMap);
            location.setTemporary(temporary);
            location.setExternalReference(externalReference);
            return location;
        }
    }
}
