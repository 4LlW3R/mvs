package com.epam.tcodata.models.datalake.raw.dimension;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.raw.RawEntity;

@SuppressWarnings("CPD-START")
public class RawOrganisationSubGroup extends RawEntity {

    private static final long serialVersionUID = -9171869467354281394L;

    public static class Fields {
        public static final String GROUP_ID = "group_id";
        public static final String PARENT_ORG_ID = "parent_org_id";
        public static final String PARENT_SUB_GROUP_ID = "parent_subgroup_id";
        public static final String NAME = "name";
        public static final String TYPE = "type";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.GROUP_ID)
    private Long groupId; // nested id of this sub group
    @ColumnName(Fields.PARENT_ORG_ID)
    private Long parentOrgId; // nested id of group used for requesting sub groups
    @ColumnName(Fields.PARENT_SUB_GROUP_ID)
    private Long parentSubGroupId; // nested id of parent sub group
    @ColumnName(Fields.NAME)
    private String name;
    @ColumnName(Fields.TYPE)
    private String type;

    public RawOrganisationSubGroup() {
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
}
