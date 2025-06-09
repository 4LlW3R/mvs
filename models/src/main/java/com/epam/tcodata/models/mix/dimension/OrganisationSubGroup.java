package com.epam.tcodata.models.mix.dimension;

import com.epam.tcodata.models.mix.Entity;
import com.epam.tcodata.models.mix.util.OrganisationSubGroupDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Objects;

/**
 * Actually external pump receives OrganisationSubGroupCycle entities from MIX, so we use
 * custom OrganisationSubGroupDeserializer in order to get flat entities instead of cyclic.
 */
@JsonDeserialize(using = OrganisationSubGroupDeserializer.class)
public class OrganisationSubGroup extends Entity {

    private static final long serialVersionUID = 980403240066117426L;

    private Long groupId; // nested id of this sub group
    private Long parentOrgId; // nested id of group used for requesting sub groups
    private Long parentSubGroupId; // nested id of parent sub group
    private String name;
    private String type;

    public OrganisationSubGroup() {
        /***  Default implementation ***/
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getParentOrgId() {
        return parentOrgId;
    }

    public void setParentOrgId(Long parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    public Long getParentSubGroupId() {
        return parentSubGroupId;
    }

    public void setParentSubGroupId(Long parentSubGroupId) {
        this.parentSubGroupId = parentSubGroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "OrganisationSubGroup{"
                + "groupId=" + groupId
                + ", parentOrgId=" + parentOrgId
                + ", parentSubGroupId=" + parentSubGroupId
                + ", name='" + name + '\''
                + ", type='" + type + '\''
                + "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganisationSubGroup that = (OrganisationSubGroup) o;
        return Objects.equals(groupId, that.groupId)
                && Objects.equals(parentOrgId, that.parentOrgId)
                && Objects.equals(parentSubGroupId, that.parentSubGroupId)
                && Objects.equals(name, that.name)
                && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, parentOrgId, parentSubGroupId, name, type);
    }


    public static final class OrganisationSubGroupBuilder {
        private Long groupId; // nested id of this sub group
        private Long parentOrgId; // nested id of group used for requesting sub groups
        private Long parentSubGroupId; // nested id of parent sub group
        private String name;
        private String type;

        public OrganisationSubGroupBuilder() {
            /***  Default implementation ***/
        }

        public OrganisationSubGroupBuilder setGroupId(Long groupId) {
            this.groupId = groupId;
            return this;
        }

        public OrganisationSubGroupBuilder setParentOrgId(Long parentOrgId) {
            this.parentOrgId = parentOrgId;
            return this;
        }

        public OrganisationSubGroupBuilder setParentSubGroupId(Long parentSubGroupId) {
            this.parentSubGroupId = parentSubGroupId;
            return this;
        }

        public OrganisationSubGroupBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public OrganisationSubGroupBuilder setType(String type) {
            this.type = type;
            return this;
        }

        /**
         * Build entity with specified parameters.
         * @return new entity.
         */
        public OrganisationSubGroup build() {
            OrganisationSubGroup organisationSubGroup = new OrganisationSubGroup();
            organisationSubGroup.setGroupId(groupId);
            organisationSubGroup.setParentOrgId(parentOrgId);
            organisationSubGroup.setParentSubGroupId(parentSubGroupId);
            organisationSubGroup.setName(name);
            organisationSubGroup.setType(type);
            return organisationSubGroup;
        }
    }
}
