package com.epam.tcodata.sql.dal.domain.pumps;

import com.epam.tcodata.sql.dal.IStorable;
import com.epam.tcodata.sql.dal.domain.PrimaryKey;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrganisationGroup implements IStorable, Serializable {

    private static final long serialVersionUID = 3415009981916752024L;

    public static class Fields {
        public static final String ID = "ID";
        public static final String ACCOUNT_ID = "AccountId";
        public static final String GROUP_ID = "GroupId";
        public static final String TYPE = "Type";
        public static final String DISPLAY_TIME_ZONE = "DisplayTimeZone";
        public static final String NAME = "Name";
        public static final String IS_ACTIVE = "IsActive";
        private Fields(){   /***  Default implementation ***/  }
    }

    @PrimaryKey
    @ColumnName(Fields.ID)
    private long id;

    @ColumnName(Fields.ACCOUNT_ID)
    private long accountId;

    @ColumnName(Fields.GROUP_ID)
    private long groupId;

    @ColumnName(Fields.TYPE)
    private String type;

    @ColumnName(Fields.DISPLAY_TIME_ZONE)
    private String displayTimeZone;

    @ColumnName(Fields.NAME)
    private String name;

    @ColumnName(Fields.IS_ACTIVE)
    private boolean active;

    private List<MixOffset> mixOffsetList = new ArrayList<>();


    public OrganisationGroup() {
    }

    /**
     * Main public constructor.
     *
     * @param id              record id.
     * @param accountId       accountId.
     * @param groupId         id of organization group.
     * @param type            organisation group type.
     * @param displayTimeZone display time zone???.
     * @param name            name of organization group.
     * @param isActive        subscription activity flag.
     */
    public OrganisationGroup(
            long id,
            long accountId,
            long groupId,
            String type,
            String displayTimeZone,
            String name,
            boolean isActive) {
        this.id = id;
        this.accountId = accountId;
        this.groupId = groupId;
        this.type = type;
        this.displayTimeZone = displayTimeZone;
        this.name = name;
        this.active = isActive;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getParentId() {
        return accountId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<MixOffset> getMixOffsetList() {
        return mixOffsetList;
    }

    public void setMixOffsetList(List<MixOffset> mixOffsetList) {
        this.mixOffsetList = mixOffsetList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrganisationGroup that = (OrganisationGroup) o;

        if (id != that.id) return false;
        if (accountId != that.accountId) return false;
        if (groupId != that.groupId) return false;
        if (active != that.active) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (displayTimeZone != null ? !displayTimeZone.equals(that.displayTimeZone) : that.displayTimeZone != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return mixOffsetList != null ? mixOffsetList.equals(that.mixOffsetList) : that.mixOffsetList == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (accountId ^ (accountId >>> 32));
        result = 31 * result + (int) (groupId ^ (groupId >>> 32));
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (displayTimeZone != null ? displayTimeZone.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + (mixOffsetList != null ? mixOffsetList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OrganisationGroup{"
                + "id=" + id
                + ", accountId=" + accountId
                + ", groupId=" + groupId
                + ", type='" + type + '\''
                + ", displayTimeZone='" + displayTimeZone + '\''
                + ", name='" + name + '\''
                + ", isActive=" + active
                + ", mixOffsetList=" + mixOffsetList
                + '}';
    }
}
