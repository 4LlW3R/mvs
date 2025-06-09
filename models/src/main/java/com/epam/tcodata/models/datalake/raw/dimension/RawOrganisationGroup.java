package com.epam.tcodata.models.datalake.raw.dimension;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.raw.RawEntity;

@SuppressWarnings("CPD-START")
public class RawOrganisationGroup extends RawEntity {

    private static final long serialVersionUID = 1214977366098814010L;

    public static class Fields {
        public static final String GROUP_ID = "group_id";
        public static final String TYPE = "type";
        public static final String DISPLAY_TIME_ZONE = "display_time_zone";
        public static final String NAME = "name";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.GROUP_ID)
    private Long groupId;
    @ColumnName(Fields.TYPE)
    private String type;
    @ColumnName(Fields.DISPLAY_TIME_ZONE)
    private String displayTimeZone;
    @ColumnName(Fields.NAME)
    private String name;

    public RawOrganisationGroup() {
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

}
