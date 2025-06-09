package com.epam.tcodata.sql.dal.domain.mdm;

import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.sql.dal.IStorable;
import com.epam.tcodata.sql.dal.domain.PrimaryKey;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

public class KeyMapping implements IStorable {

    public static class Fields {
        public static final String ID = "id";
        public static final String ENTITY = "entity";
        public static final String KEY_NAME = "key_name";
        public static final String NATURAL_KEY = "natural_key";
        public static final String SURROGATE_KEY = "surrogate_key";
        public static final String SPECULATIVE = "speculative";
        private Fields(){   /***  Default implementation ***/  }
    }

    @PrimaryKey
    @ColumnName(Fields.ID)
    private long id;

    @ColumnName(Fields.ENTITY)
    private EntityType entity;

    @ColumnName(Fields.KEY_NAME)
    private String keyName;

    @ColumnName(Fields.NATURAL_KEY)
    private String naturalKey;

    @ColumnName(Fields.SURROGATE_KEY)
    private String surrogateKey;

    @ColumnName(Fields.SPECULATIVE)
    private boolean speculative;

    /**
     * Public default constructor.
     */
    public KeyMapping() {
    }

    /**
     * Public main constructor.
     *
     * @param entity       - kind of entity.
     * @param naturalKey   - new value of natural key.
     * @param surrogateKey - surrogate key.
     */
    public KeyMapping(EntityType entity, String keyName, String naturalKey, String surrogateKey) {
        this.entity = entity;
        this.keyName = keyName;
        this.surrogateKey = surrogateKey;
        this.naturalKey = naturalKey;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public EntityType getEntity() {
        return entity;
    }

    public void setEntity(EntityType entity) {
        this.entity = entity;
    }

    public String getKeyName() {
        return this.keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getSurrogateKey() {
        return surrogateKey;
    }

    public void setSurrogateKey(String surrogateKey) {
        this.surrogateKey = surrogateKey;
    }

    public String getNaturalKey() {
        return naturalKey;
    }

    public void setNaturalKey(String naturalKey) {
        this.naturalKey = naturalKey;
    }

    public boolean isSpeculative() {
        return this.speculative;
    }

    public void setSpeculative(boolean speculative) {
        this.speculative = speculative;
    }

    @Override
    public String toString() {
        return "KeyMapping{"
                + "id=" + id
                + ", entity=" + entity
                + ", keyName=" + keyName
                + ", naturalKey='" + naturalKey + '\''
                + ", surrogateKey='" + surrogateKey + '\''
                + ", speculative='" + speculative + '\''
                + '}';
    }
}
