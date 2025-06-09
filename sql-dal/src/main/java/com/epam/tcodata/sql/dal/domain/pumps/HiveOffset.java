package com.epam.tcodata.sql.dal.domain.pumps;

import com.epam.tcodata.sql.dal.IStorable;
import com.epam.tcodata.sql.dal.domain.PrimaryKey;
import com.epam.tcodata.sql.dal.util.SqlCommon;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class HiveOffset implements IStorable, Serializable {

    private static final long serialVersionUID = -2867739362948582874L;

    public static class Fields {
        public static final String ID = "ID";
        public static final String ENTITY_TYPE = "EntityType";
        public static final String PERSISTED_DATE_UTC = "PersistedDateUtc";
        public static final String PREPARED_DATE_UTC = "PreparedDateUtc";
        public static final String VALIDATED_DATE_UTC = "ValidatedDateUtc";
        public static final String ELEMENT_COUNT = "ElementCount";
        private Fields(){   /***  Default implementation ***/  }
    }

    @PrimaryKey
    @ColumnName(Fields.ID)
    private long id;

    @ColumnName(Fields.ENTITY_TYPE)
    private int entityType;

    @ColumnName(Fields.PERSISTED_DATE_UTC)
    private Timestamp persistedDateUtc;

    @ColumnName(Fields.PREPARED_DATE_UTC)
    private Timestamp preparedDateUtc;

    @ColumnName(Fields.VALIDATED_DATE_UTC)
    private Timestamp validatedDateUtc;

    @ColumnName(Fields.ELEMENT_COUNT)
    private long elementCount;

    public HiveOffset() {
        /***  Default implementation ***/
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public Timestamp getPersistedDateUtc() {
        return SqlCommon.clone(persistedDateUtc);
    }

    public void setPersistedDateUtc(Timestamp persistedDateUtc) {
        this.persistedDateUtc = SqlCommon.clone(persistedDateUtc);
    }

    public Timestamp getPreparedDateUtc() {
        return SqlCommon.clone(preparedDateUtc);
    }

    public void setPreparedDateUtc(Timestamp preparedDateUtc) {
        this.preparedDateUtc = SqlCommon.clone(preparedDateUtc);
    }

    public Timestamp getValidatedDateUtc() {
        return SqlCommon.clone(validatedDateUtc);
    }

    public void setValidatedDateUtc(Timestamp validatedDateUtc) {
        this.validatedDateUtc = SqlCommon.clone(validatedDateUtc);
    }

    public long getElementCount() {
        return elementCount;
    }

    public void setElementCount(long elementCount) {
        this.elementCount = elementCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HiveOffset that = (HiveOffset) o;
        return id == that.id
                && entityType == that.entityType
                && elementCount == that.elementCount
                && Objects.equals(persistedDateUtc, that.persistedDateUtc)
                && Objects.equals(preparedDateUtc, that.preparedDateUtc)
                && Objects.equals(validatedDateUtc, that.validatedDateUtc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entityType, persistedDateUtc, preparedDateUtc, validatedDateUtc, elementCount);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + "{"
                + "id=" + id
                + ", entityType=" + entityType
                + ", persistedDateUtc=" + persistedDateUtc
                + ", preparedDateUtc=" + preparedDateUtc
                + ", validatedDateUtc=" + validatedDateUtc
                + ", elementCount=" + elementCount
                + '}';
    }
}
