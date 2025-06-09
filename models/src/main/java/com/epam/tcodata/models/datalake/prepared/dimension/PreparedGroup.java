package com.epam.tcodata.models.datalake.prepared.dimension;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.prepared.PreparedEntity;

import java.sql.Timestamp;

public class PreparedGroup extends PreparedEntity {

    private static final long serialVersionUID = 3965234044632895619L;

    public static class Fields {
        public static final String EXTERNAL_ID = "external_id";
        public static final String PERSISTED_DATE_UTC = "persisted_date_utc";
        public static final String NAME = "name";
        public static final String GROUP_TYPE_DURABLE_KEY = "group_type_durable_key";
        public static final String GROUP_TYPE_CODE = "group_type_code";
        public static final String DISPLAY_TIME_ZONE = "display_time_zone";
        public static final String PARENT_GROUP_DURABLE_KEY = "parent_group_durable_key";
        public static final String PARENT_GROUP_ID = "parent_group_id";
        public static final String FM_ORG_GROUP_ID = "fm_org_group_id";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.EXTERNAL_ID)
    private Long externalId;
    @ColumnName(Fields.PERSISTED_DATE_UTC)
    private Timestamp persistedDateUtc;
    @ColumnName(Fields.NAME)
    private String name;
    @ColumnName(Fields.GROUP_TYPE_DURABLE_KEY)
    private String groupTypeDurableKey;
    @ColumnName(Fields.GROUP_TYPE_CODE)
    private String groupTypeCode;
    @ColumnName(Fields.DISPLAY_TIME_ZONE)
    private String displayTimeZone;
    @ColumnName(Fields.PARENT_GROUP_DURABLE_KEY)
    private String parentGroupDurableKey;
    @ColumnName(Fields.PARENT_GROUP_ID)
    private Long parentGroupId;
    @ColumnName(Fields.FM_ORG_GROUP_ID)
    private Long fmOrgGroupId;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupTypeDurableKey() {
        return groupTypeDurableKey;
    }

    public void setGroupTypeDurableKey(String groupTypeDurableKey) {
        this.groupTypeDurableKey = groupTypeDurableKey;
    }

    public String getGroupTypeCode() {
        return groupTypeCode;
    }

    public void setGroupTypeCode(String groupTypeCode) {
        this.groupTypeCode = groupTypeCode;
    }

    public String getDisplayTimeZone() {
        return displayTimeZone;
    }

    public void setDisplayTimeZone(String displayTimeZone) {
        this.displayTimeZone = displayTimeZone;
    }

    public String getParentGroupDurableKey() {
        return parentGroupDurableKey;
    }

    public void setParentGroupDurableKey(String parentGroupDurableKey) {
        this.parentGroupDurableKey = parentGroupDurableKey;
    }

    public Long getParentGroupId() {
        return parentGroupId;
    }

    public void setParentGroupId(Long parentGroupId) {
        this.parentGroupId = parentGroupId;
    }

    public Long getFmOrgGroupId() {
        return fmOrgGroupId;
    }

    public void setFmOrgGroupId(Long fmOrgGroupId) {
        this.fmOrgGroupId = fmOrgGroupId;
    }
}
