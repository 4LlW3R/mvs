package com.epam.tcodata.sql.dal.domain.mdm;

import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.sql.dal.IStorable;
import com.epam.tcodata.sql.dal.domain.PrimaryKey;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.util.Objects;

/**
 * Raw class that represents a relation to other entity from base one. For example, dimensions in fact entities.
 */
public class Relation implements IStorable {

    public static class Fields {
        public static final String ID = "id";
        public static final String PARENT_ID = "parent_id";
        public static final String ENTITY = "entity";
        public static final String NATURAL_KEY_NAME = "natural_key_name";
        public static final String SURROGATE_KEY_NAME = "surrogate_key_name";
        private Fields(){   /***  Default implementation ***/  }
    }

    @PrimaryKey
    @ColumnName(Fields.ID)
    private long id;

    @ColumnName(Fields.PARENT_ID)
    private long parentId;

    @ColumnName(Fields.ENTITY)
    private EntityType entityType;

    @ColumnName(Fields.NATURAL_KEY_NAME)
    private String naturalKeyName;

    @ColumnName(Fields.SURROGATE_KEY_NAME)
    private String surrogateKeyName;

    public Relation() {
    }

    /**
     * Public constructor.
     *
     * @param ruleId - code of RULE.
     * @param entityType - type of related entity.
     * @param naturalKeyName - name of the field for natural key.
     */
    public Relation(long ruleId, EntityType entityType, String naturalKeyName, String surrogateKeyName) {
        this.id = -1;
        this.parentId = ruleId;
        this.entityType = entityType;
        this.naturalKeyName = naturalKeyName;
        this.surrogateKeyName = surrogateKeyName;
    }

    @Override
    public String toString() {
        return "Relation{"
                + "id=" + id
                + ", parentId=" + parentId
                + ", entity=" + entityType
                + ", naturalKeyName='" + naturalKeyName + '\''
                + ", surrogateKeyName='" + surrogateKeyName + '\''
                + '}';
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
        return this.parentId;
    }

    @Override
    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public String getNaturalKeyName() {
        return naturalKeyName;
    }

    public void setNaturalKeyName(String naturalKeyName) {
        this.naturalKeyName = naturalKeyName;
    }

    public String getSurrogateKeyName() {
        return surrogateKeyName;
    }

    public void setSurrogateKeyName(String surrogateKeyName) {
        this.surrogateKeyName = surrogateKeyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Relation relation = (Relation) o;
        return parentId == relation.parentId
                && entityType == relation.entityType
                && Objects.equals(naturalKeyName, relation.naturalKeyName)
                && Objects.equals(surrogateKeyName, relation.surrogateKeyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parentId, entityType, naturalKeyName, surrogateKeyName);
    }
}
