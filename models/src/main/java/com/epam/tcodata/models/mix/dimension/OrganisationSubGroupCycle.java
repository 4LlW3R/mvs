package com.epam.tcodata.models.mix.dimension;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * We use this cyclic POJO only for receiving response from MIX and convert it into flat entities.
 */
public class OrganisationSubGroupCycle implements Serializable {

    private static final long serialVersionUID = 7696703231862080990L;

    private Long groupId;
    private String name;
    private String type;
    @JsonProperty("SubGroups")
    private List<OrganisationSubGroupCycle> organisationSubGroupCycleList;

    public OrganisationSubGroupCycle() {
        this.organisationSubGroupCycleList = new ArrayList<>();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<OrganisationSubGroupCycle> getOrganisationSubGroupCycleList() {
        return organisationSubGroupCycleList;
    }

    public void setOrganisationSubGroupCycleList(List<OrganisationSubGroupCycle> organisationSubGroupCycleList) {
        this.organisationSubGroupCycleList.clear();
        this.organisationSubGroupCycleList.addAll(organisationSubGroupCycleList);
    }

    @Override
    public String toString() {
        return "OrganisationSubGroupCycle{"
                + "groupId=" + groupId
                + ", name='" + name + '\''
                + ", type='" + type + '\''
                + ", organisationSubGroupCycleList=" + organisationSubGroupCycleList
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganisationSubGroupCycle that = (OrganisationSubGroupCycle) o;
        return Objects.equals(groupId, that.groupId)
                && Objects.equals(name, that.name)
                && Objects.equals(type, that.type)
                && Objects.equals(organisationSubGroupCycleList, that.organisationSubGroupCycleList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, name, type, organisationSubGroupCycleList);
    }


    public static final class OrganisationSubGroupCycleBuilder {
        private Long groupId;
        private String name;
        private String type;
        private List<OrganisationSubGroupCycle> organisationSubGroupCycleList;

        public OrganisationSubGroupCycleBuilder() {
            /***  Default implementation ***/
        }

        public OrganisationSubGroupCycleBuilder setGroupId(Long groupId) {
            this.groupId = groupId;
            return this;
        }

        public OrganisationSubGroupCycleBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public OrganisationSubGroupCycleBuilder setType(String type) {
            this.type = type;
            return this;
        }

        public OrganisationSubGroupCycleBuilder setOrganisationSubGroupCycleList(List<OrganisationSubGroupCycle> organisationSubGroupCycleList) {
            this.organisationSubGroupCycleList = organisationSubGroupCycleList;
            return this;
        }

        /**
         * Build entity with specified parameters.
         * @return new entity.
         */
        public OrganisationSubGroupCycle build() {
            OrganisationSubGroupCycle organisationSubGroupCycle = new OrganisationSubGroupCycle();
            organisationSubGroupCycle.setGroupId(groupId);
            organisationSubGroupCycle.setName(name);
            organisationSubGroupCycle.setType(type);
            organisationSubGroupCycle.setOrganisationSubGroupCycleList(organisationSubGroupCycleList == null ? new ArrayList<>() : organisationSubGroupCycleList);
            return organisationSubGroupCycle;
        }
    }
}
