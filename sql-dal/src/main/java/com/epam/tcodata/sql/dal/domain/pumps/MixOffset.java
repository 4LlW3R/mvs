package com.epam.tcodata.sql.dal.domain.pumps;

import com.epam.tcodata.sql.dal.IStorable;
import com.epam.tcodata.sql.dal.domain.PrimaryKey;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.io.Serializable;
import java.sql.Time;
import java.time.Instant;

@SuppressWarnings("CPD-START")
public class MixOffset implements IStorable, Serializable {

    private static final long serialVersionUID = -3150649191430671288L;

    public static class Fields {
        public static final String ID = "ID";
        public static final String ORGANISATION_GROUP_ID = "OrganisationGroupId";
        public static final String ENTITY_TYPE = "EntityType";
        public static final String ENTITY_SUPER_TYPE = "EntitySuperType";
        public static final String LAST_SYNC_DATE_UTC = "LastSyncDateUtc";
        public static final String LAST_PROCESSED_TIME = "LastProcessedTime";
        public static final String LAST_SYNC_RESULT_CODE = "LastSyncResultCode";
        public static final String LAST_SYNC_ELEMENT_COUNT = "LastSyncElementCount";
        public static final String LAST_SYNC_DURATION = "LastSyncDuration";
        public static final String TOTAL_ELEMENTS_COUNT = "TotalElementsCount";
        public static final String LAST_ERROR_MESSAGE = "LastErrorMessage";
        private Fields(){   /***  Default implementation ***/  }
    }

    @PrimaryKey
    @ColumnName(Fields.ID)
    private long id;

    @ColumnName(Fields.ORGANISATION_GROUP_ID)
    private long organisationGroupId;

    @ColumnName(Fields.ENTITY_TYPE)
    private int entityType;

    @ColumnName(Fields.ENTITY_SUPER_TYPE)
    private int entitySuperType;

    @ColumnName(Fields.LAST_SYNC_DATE_UTC)
    private Instant lastSyncDateUtc;

    @ColumnName(Fields.LAST_PROCESSED_TIME)
    private Instant lastProcessedTime;

    @ColumnName(Fields.LAST_SYNC_RESULT_CODE)
    private int lastSyncResultCode;

    @ColumnName(Fields.LAST_SYNC_ELEMENT_COUNT)
    private long lastSyncElementCount;

    @ColumnName(Fields.LAST_SYNC_DURATION)
    private Time lastSyncDuration;

    @ColumnName(Fields.TOTAL_ELEMENTS_COUNT)
    private long totalElementsCount;

    @ColumnName(Fields.LAST_ERROR_MESSAGE)
    private String lastErrorMessage;

    public MixOffset() {
    }

    /**
     * Main public constructor.
     *
     * @param id                   surrogateId.
     * @param organisationGroupId  organisation group id.
     * @param entityType           entity type (ex: Position).
     * @param entitySuperType      entity class (ex: Fact).
     * @param lastSyncDateUtc      last date when offset was synced.
     * @param lastProcessedTime    last time that data was received from.
     * @param lastSyncResultCode   last result code from request to Mix API.
     * @param lastSyncElementCount last sync element count.
     * @param lastSyncDuration     duration of the last sync.
     * @param totalElementsCount   total elements received for pair (org id, entity type).
     * @param lastErrorMessage     last error message.
     */
    public MixOffset(long id,
                     long organisationGroupId,
                     int entityType,
                     int entitySuperType,
                     Instant lastSyncDateUtc,
                     Instant lastProcessedTime,
                     int lastSyncResultCode,
                     long lastSyncElementCount,
                     Time lastSyncDuration,
                     long totalElementsCount,
                     String lastErrorMessage) {
        this.id = id;
        this.organisationGroupId = organisationGroupId;
        this.entityType = entityType;
        this.entitySuperType = entitySuperType;
        this.lastSyncDateUtc = lastSyncDateUtc;
        this.lastProcessedTime = lastProcessedTime;
        this.lastSyncResultCode = lastSyncResultCode;
        this.lastSyncElementCount = lastSyncElementCount;
        this.lastSyncDuration = lastSyncDuration;
        this.totalElementsCount = totalElementsCount;
        this.lastErrorMessage = lastErrorMessage;
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
        return organisationGroupId;
    }

    public long getOrganisationGroupId() {
        return organisationGroupId;
    }

    public void setOrganisationGroupId(long organisationGroupId) {
        this.organisationGroupId = organisationGroupId;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public int getEntitySuperType() {
        return entitySuperType;
    }

    public void setEntitySuperType(int entitySuperType) {
        this.entitySuperType = entitySuperType;
    }

    public Instant getLastSyncDateUtc() {
        return lastSyncDateUtc;
    }

    public void setLastSyncDateUtc(Instant lastSyncDateUtc) {
        this.lastSyncDateUtc = lastSyncDateUtc;
    }

    public Instant getLastProcessedTime() {
        return lastProcessedTime;
    }

    public void setLastProcessedTime(Instant lastProcessedTime) {
        this.lastProcessedTime = lastProcessedTime;
    }

    public int getLastSyncResultCode() {
        return lastSyncResultCode;
    }

    public void setLastSyncResultCode(int lastSyncResultCode) {
        this.lastSyncResultCode = lastSyncResultCode;
    }

    public long getLastSyncElementCount() {
        return lastSyncElementCount;
    }

    public void setLastSyncElementCount(long lastSyncElementCount) {
        this.lastSyncElementCount = lastSyncElementCount;
    }

    public Time getLastSyncDuration() {
        return lastSyncDuration;
    }

    public void setLastSyncDuration(Time lastSyncDuration) {
        this.lastSyncDuration = lastSyncDuration;
    }

    public long getTotalElementsCount() {
        return totalElementsCount;
    }

    public void setTotalElementsCount(long totalElementsCount) {
        this.totalElementsCount = totalElementsCount;
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    public void setLastErrorMessage(String lastErrorMessage) {
        this.lastErrorMessage = lastErrorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MixOffset mixOffset = (MixOffset) o;

        if (id != mixOffset.id) return false;
        if (organisationGroupId != mixOffset.organisationGroupId) return false;
        if (entityType != mixOffset.entityType) return false;
        if (entitySuperType != mixOffset.entitySuperType) return false;
        if (lastSyncResultCode != mixOffset.lastSyncResultCode) return false;
        if (lastSyncElementCount != mixOffset.lastSyncElementCount) return false;
        if (totalElementsCount != mixOffset.totalElementsCount) return false;
        if (lastSyncDateUtc != null ? !lastSyncDateUtc.equals(mixOffset.lastSyncDateUtc) : mixOffset.lastSyncDateUtc != null)
            return false;
        if (lastProcessedTime != null ? !lastProcessedTime.equals(mixOffset.lastProcessedTime) : mixOffset.lastProcessedTime != null)
            return false;
        if (lastSyncDuration != null ? !lastSyncDuration.equals(mixOffset.lastSyncDuration) : mixOffset.lastSyncDuration != null)
            return false;
        return lastErrorMessage != null ? lastErrorMessage.equals(mixOffset.lastErrorMessage) : mixOffset.lastErrorMessage == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (organisationGroupId ^ (organisationGroupId >>> 32));
        result = 31 * result + entityType;
        result = 31 * result + entitySuperType;
        result = 31 * result + (lastSyncDateUtc != null ? lastSyncDateUtc.hashCode() : 0);
        result = 31 * result + (lastProcessedTime != null ? lastProcessedTime.hashCode() : 0);
        result = 31 * result + lastSyncResultCode;
        result = 31 * result + (int) (lastSyncElementCount ^ (lastSyncElementCount >>> 32));
        result = 31 * result + (lastSyncDuration != null ? lastSyncDuration.hashCode() : 0);
        result = 31 * result + (int) (totalElementsCount ^ (totalElementsCount >>> 32));
        result = 31 * result + (lastErrorMessage != null ? lastErrorMessage.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MixOffset{"
                + "id=" + id
                + ", organisationGroupId=" + organisationGroupId
                + ", entityType=" + entityType
                + ", entitySuperType=" + entitySuperType
                + ", lastSyncDateUtc=" + lastSyncDateUtc
                + ", lastProcessedTime=" + lastProcessedTime
                + ", lastSyncResultCode=" + lastSyncResultCode
                + ", lastSyncElementCount=" + lastSyncElementCount
                + ", lastSyncDuration=" + lastSyncDuration
                + ", totalElementsCount=" + totalElementsCount
                + ", lastException=" + lastErrorMessage
                + "}\n";
    }
}
