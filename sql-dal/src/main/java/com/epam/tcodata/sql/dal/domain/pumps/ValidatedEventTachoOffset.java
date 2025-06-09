package com.epam.tcodata.sql.dal.domain.pumps;

import com.epam.tcodata.sql.dal.IStorable;
import com.epam.tcodata.sql.dal.domain.PrimaryKey;
import com.epam.tcodata.sql.dal.util.SqlCommon;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Objects;

@SuppressWarnings("CPD-START")
public class ValidatedEventTachoOffset implements IStorable, Serializable {

    private static final long serialVersionUID = 7055853247775080229L;

    public static class Fields {
        public static final String ID = "ID";
        public static final String SYNC_DATE_UTC = "SyncDateUtc";
        public static final String FROM_PERSISTED_DATE_UTC = "FromPersistedDateUtc";
        public static final String TO_PERSISTED_DATE_UTC = "ToPersistedDateUtc";
        public static final String ELEMENT_COUNT = "ElementCount";
        public static final String SYNC_DURATION = "SyncDuration";
        private Fields(){   /***  Default implementation ***/  }
    }

    @PrimaryKey
    @ColumnName(Fields.ID)
    private long id;

    @ColumnName(Fields.SYNC_DATE_UTC)
    private Timestamp syncDateUtc;

    @ColumnName(Fields.FROM_PERSISTED_DATE_UTC)
    private Timestamp fromPersistedDateUtc;

    @ColumnName(Fields.TO_PERSISTED_DATE_UTC)
    private Timestamp toPersistedDateUtc;

    @ColumnName(Fields.ELEMENT_COUNT)
    private long elementCount;

    @ColumnName(Fields.SYNC_DURATION)
    private Time syncDuration;

    public ValidatedEventTachoOffset() {
    }

    /**
     * Main public constructor.
     *
     * @param id                   surrogateId.
     * @param syncDateUtc          date when offset was synced.
     * @param fromPersistedDateUtc the "from" persisted date of validated events.
     * @param toPersistedDateUtc   the "to" persisted date of validated events.
     * @param elementCount         handled element count
     * @param syncDuration         sync duration
     */
    public ValidatedEventTachoOffset(
            long id,
            Timestamp syncDateUtc,
            Timestamp fromPersistedDateUtc,
            Timestamp toPersistedDateUtc,
            long elementCount,
            Time syncDuration) {
        this.id = id;
        this.syncDateUtc = SqlCommon.clone(syncDateUtc);
        this.fromPersistedDateUtc = SqlCommon.clone(fromPersistedDateUtc);
        this.toPersistedDateUtc = SqlCommon.clone(toPersistedDateUtc);
        this.elementCount = elementCount;
        this.syncDuration = syncDuration;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getSyncDateUtc() {
        return SqlCommon.clone(syncDateUtc);
    }

    public void setSyncDateUtc(Timestamp syncDateUtc) {
        this.syncDateUtc = SqlCommon.clone(syncDateUtc);
    }

    public Timestamp getFromPersistedDateUtc() {
        return SqlCommon.clone(fromPersistedDateUtc);
    }

    public void setFromPersistedDateUtc(Timestamp fromPersistedDateUtc) {
        this.fromPersistedDateUtc = SqlCommon.clone(fromPersistedDateUtc);
    }

    public Timestamp getToPersistedDateUtc() {
        return SqlCommon.clone(toPersistedDateUtc);
    }

    public void setToPersistedDateUtc(Timestamp toPersistedDateUtc) {
        this.toPersistedDateUtc = SqlCommon.clone(toPersistedDateUtc);
    }

    public long getElementCount() {
        return elementCount;
    }

    public void setElementCount(long elementCount) {
        this.elementCount = elementCount;
    }

    public Time getSyncDuration() {
        return syncDuration;
    }

    public void setSyncDuration(Time syncDuration) {
        this.syncDuration = syncDuration;
    }

    @Override
    public String toString() {
        return "ValidatedEventTachoOffset{"
                + "id=" + id
                + ", syncDateUtc=" + syncDateUtc
                + ", fromPersistedDateUtc=" + fromPersistedDateUtc
                + ", toPersistedDateUtc=" + toPersistedDateUtc
                + ", elementCount=" + elementCount
                + ", syncDuration=" + syncDuration
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidatedEventTachoOffset that = (ValidatedEventTachoOffset) o;
        return id == that.id
                && elementCount == that.elementCount
                && Objects.equals(syncDateUtc, that.syncDateUtc)
                && Objects.equals(fromPersistedDateUtc, that.fromPersistedDateUtc)
                && Objects.equals(toPersistedDateUtc, that.toPersistedDateUtc)
                && Objects.equals(syncDuration, that.syncDuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, syncDateUtc, fromPersistedDateUtc, toPersistedDateUtc, elementCount, syncDuration);
    }
}
