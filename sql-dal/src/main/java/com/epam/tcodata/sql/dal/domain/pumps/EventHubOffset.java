package com.epam.tcodata.sql.dal.domain.pumps;

import com.epam.tcodata.sql.dal.IStorable;
import com.epam.tcodata.sql.dal.domain.PrimaryKey;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.io.Serializable;
import java.time.Instant;

public class EventHubOffset implements IStorable, Serializable {

    private static final long serialVersionUID = 206889430107978651L;

    public static class Fields {
        public static final String ID = "ID";
        public static final String PARTITION_ID = "PartitionId";
        public static final String ENTITY_TYPE = "EntityType";
        public static final String ENTITY_SUPER_TYPE = "EntitySuperType";
        public static final String SEQ_NO = "SeqNo";
        public static final String LAST_SYNC_DATE_UTC = "LastSyncDateUtc";
        public static final String LAST_SYNC_ELEMENT_COUNT = "LastSyncElementCount";
        public static final String TOTAL_ELEMENTS_COUNT = "TotalElementsCount";
        private Fields(){   /***  Default implementation ***/  }
    }

    @PrimaryKey
    @ColumnName(Fields.ID)
    private long id;

    @ColumnName(Fields.PARTITION_ID)
    private String partitionId;

    @ColumnName(Fields.ENTITY_TYPE)
    private int entityType;

    @ColumnName(Fields.ENTITY_SUPER_TYPE)
    private int entitySuperType;

    @ColumnName(Fields.SEQ_NO)
    private long seqNo;

    @ColumnName(Fields.LAST_SYNC_DATE_UTC)
    private Instant lastSyncDateUtc;

    @ColumnName(Fields.LAST_SYNC_ELEMENT_COUNT)
    private long lastSyncElementCount;

    @ColumnName(Fields.TOTAL_ELEMENTS_COUNT)
    private long totalElementsCount;

    public EventHubOffset() {
    }

    /**
     * Create EventHubOffset.
     *
     * @param id                   primary surrogate key
     * @param partitionId          partition id
     * @param entityType           entity type code for entity being tracked
     * @param entitySuperType      entity super type for entity being tracked
     * @param seqNo                offset
     * @param lastSyncDateUtc      date of last synchronization
     * @param lastSyncElementCount count of elements saved to storage during last run.
     * @param totalElementsCount   total stored elements of current type for current subscription
     */
    public EventHubOffset(long id,
                          String partitionId,
                          int entityType,
                          int entitySuperType,
                          long seqNo,
                          Instant lastSyncDateUtc,
                          long lastSyncElementCount,
                          long totalElementsCount) {
        this.id = id;
        this.partitionId = partitionId;
        this.entityType = entityType;
        this.entitySuperType = entitySuperType;
        this.seqNo = seqNo;
        this.lastSyncDateUtc = lastSyncDateUtc;
        this.lastSyncElementCount = lastSyncElementCount;
        this.totalElementsCount = totalElementsCount;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(String partitionId) {
        this.partitionId = partitionId;
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

    public long getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(long seqNo) {
        this.seqNo = seqNo;
    }

    public Instant getLastSyncDateUtc() {
        return lastSyncDateUtc;
    }

    public void setLastSyncDateUtc(Instant lastSyncDateUtc) {
        this.lastSyncDateUtc = lastSyncDateUtc;
    }

    public long getLastSyncElementCount() {
        return lastSyncElementCount;
    }

    public void setLastSyncElementCount(long lastSyncElementCount) {
        this.lastSyncElementCount = lastSyncElementCount;
    }

    public long getTotalElementsCount() {
        return totalElementsCount;
    }

    public void setTotalElementsCount(long totalElementsCount) {
        this.totalElementsCount = totalElementsCount;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventHubOffset that = (EventHubOffset) o;

        if (id != that.id) return false;
        if (entityType != that.entityType) return false;
        if (entitySuperType != that.entitySuperType) return false;
        if (seqNo != that.seqNo) return false;
        if (lastSyncElementCount != that.lastSyncElementCount) return false;
        if (totalElementsCount != that.totalElementsCount) return false;
        if (partitionId != null ? !partitionId.equals(that.partitionId) : that.partitionId != null) return false;
        return lastSyncDateUtc != null ? lastSyncDateUtc.equals(that.lastSyncDateUtc) : that.lastSyncDateUtc == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (partitionId != null ? partitionId.hashCode() : 0);
        result = 31 * result + entityType;
        result = 31 * result + entitySuperType;
        result = 31 * result + (int) (seqNo ^ (seqNo >>> 32));
        result = 31 * result + (lastSyncDateUtc != null ? lastSyncDateUtc.hashCode() : 0);
        result = 31 * result + (int) (lastSyncElementCount ^ (lastSyncElementCount >>> 32));
        result = 31 * result + (int) (totalElementsCount ^ (totalElementsCount >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "EventHubOffset{"
                + "id=" + id
                + ", partitionId=" + partitionId
                + ", entityType=" + entityType
                + ", entitySuperType=" + entitySuperType
                + ", seqNo=" + seqNo
                + ", lastSyncDateUtc=" + lastSyncDateUtc
                + ", lastSyncElementCount=" + lastSyncElementCount
                + ", totalElementsCount=" + totalElementsCount
                + '}';
    }
}
