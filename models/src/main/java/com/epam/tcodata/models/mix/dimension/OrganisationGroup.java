package com.epam.tcodata.models.mix.dimension;

import com.epam.tcodata.models.mix.Entity;

public class OrganisationGroup extends Entity {

    private static final long serialVersionUID = 421749280663924668L;

    private Long groupId;
    private String type;
    private String displayTimeZone;
    private String name;

    public OrganisationGroup() {
        /***  Default implementation ***/
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDisplayTimeZone() {
        return displayTimeZone;
    }

    public void setDisplayTimeZone(String displayTimeZone) {
        this.displayTimeZone = displayTimeZone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrganisationGroup that = (OrganisationGroup) o;

        if (groupId != null ? !groupId.equals(that.groupId) : that.groupId != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (displayTimeZone != null ? !displayTimeZone.equals(that.displayTimeZone) : that.displayTimeZone != null)
            return false;
        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        int result = groupId != null ? groupId.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (displayTimeZone != null ? displayTimeZone.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OrganisationGroup{"
                + "groupId=" + groupId
                + ", type='" + type + '\''
                + ", displayTimeZone='" + displayTimeZone + '\''
                + ", name='" + name + '\''
                + "} " + super.toString();
    }


    public static final class OrganisationGroupBuilder {
        private Long groupId;
        private String type;
        private String displayTimeZone;
        private String name;

        public OrganisationGroupBuilder() {
            /***  Default implementation ***/
        }

        public OrganisationGroupBuilder setGroupId(Long groupId) {
            this.groupId = groupId;
            return this;
        }

        public OrganisationGroupBuilder setType(String type) {
            this.type = type;
            return this;
        }

        public OrganisationGroupBuilder setDisplayTimeZone(String displayTimeZone) {
            this.displayTimeZone = displayTimeZone;
            return this;
        }

        public OrganisationGroupBuilder setName(String name) {
            this.name = name;
            return this;
        }

        /**
         * Build entity with specified parameters.
         * @return new entity.
         */
        public OrganisationGroup build() {
            OrganisationGroup organisationGroup = new OrganisationGroup();
            organisationGroup.setGroupId(groupId);
            organisationGroup.setType(type);
            organisationGroup.setDisplayTimeZone(displayTimeZone);
            organisationGroup.setName(name);
            return organisationGroup;
        }
    }
}
