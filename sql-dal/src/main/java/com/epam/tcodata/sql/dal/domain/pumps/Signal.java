package com.epam.tcodata.sql.dal.domain.pumps;

import com.epam.tcodata.sql.dal.IStorable;
import com.epam.tcodata.sql.dal.domain.PrimaryKey;
import com.epam.tcodata.sql.dal.util.SqlCommon;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.io.Serializable;
import java.sql.Timestamp;

public class Signal implements IStorable, Serializable {

    private static final long serialVersionUID = -1585814634882435840L;

    public static class Fields {
        public static final String ID = "ID";
        public static final String APPLICATION_TYPE = "ApplicationType";
        public static final String APPLICATION_SUPER_TYPE = "ApplicationSuperType";
        public static final String ENTITY_TYPE = "EntityType";
        public static final String ENTITY_SUPER_TYPE = "EntitySuperType";
        public static final String TIMESTAMP = "Timestamp";
        public static final String SIGNAL_TYPE = "SignalType";
        public static final String MESSAGE = "Message";
        private Fields(){   /***  Default implementation ***/  }
    }

    @PrimaryKey
    @ColumnName(Fields.ID)
    private long id;

    @ColumnName(Fields.APPLICATION_TYPE)
    private Integer applicationType;

    @ColumnName(Fields.APPLICATION_SUPER_TYPE)
    private Integer applicationSuperType;

    @ColumnName(Fields.ENTITY_TYPE)
    private Integer entityType;

    @ColumnName(Fields.ENTITY_SUPER_TYPE)
    private Integer entitySuperType;

    @ColumnName(Fields.TIMESTAMP)
    private Timestamp timestamp;

    @ColumnName(Fields.SIGNAL_TYPE)
    private Integer signalType;

    @ColumnName(Fields.MESSAGE)
    private String message;

    public Signal() {
    }

    /**
     * Main public constructor.
     *
     * @param id                   surrogateId.
     * @param applicationType      application type.
     * @param applicationSuperType application super type.
     * @param entityType           entity type (ex: Position).
     * @param entitySuperType      entity class (ex: Fact).
     * @param timestamp            ingested date utc.
     * @param signalType           signal type.
     * @param message              message.
     */
    public Signal(
            long id,
            Integer applicationType,
            Integer applicationSuperType,
            Integer entityType,
            Integer entitySuperType,
            Timestamp timestamp,
            Integer signalType,
            String message) {
        this.id = id;
        this.applicationType = applicationType;
        this.applicationSuperType = applicationSuperType;
        this.entityType = entityType;
        this.entitySuperType = entitySuperType;
        this.timestamp = SqlCommon.clone(timestamp);
        this.signalType = signalType;
        this.message = message;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public Integer getApplicationType() {
        return applicationType;
    }

    public Signal setApplicationType(Integer applicationType) {
        this.applicationType = applicationType;
        return this;
    }

    public Integer getApplicationSuperType() {
        return applicationSuperType;
    }

    public Signal setApplicationSuperType(Integer applicationSuperType) {
        this.applicationSuperType = applicationSuperType;
        return this;
    }

    public Integer getEntityType() {
        return entityType;
    }

    public Signal setEntityType(Integer entityType) {
        this.entityType = entityType;
        return this;
    }

    public Integer getEntitySuperType() {
        return entitySuperType;
    }

    public Signal setEntitySuperType(Integer entitySuperType) {
        this.entitySuperType = entitySuperType;
        return this;
    }

    public Timestamp getTimestamp() {
        return SqlCommon.clone(timestamp);
    }

    public Signal setTimestamp(Timestamp timestamp) {
        this.timestamp = SqlCommon.clone(timestamp);
        return this;
    }

    public Integer getSignalType() {
        return signalType;
    }

    public Signal setSignalType(Integer signalType) {
        this.signalType = signalType;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Signal setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public String toString() {
        return "Signal{"
                + "id=" + id
                + ", applicationType=" + applicationType
                + ", applicationSuperType=" + applicationSuperType
                + ", entityType=" + entityType
                + ", entitySuperType=" + entitySuperType
                + ", timestamp=" + timestamp
                + ", signalType=" + signalType
                + ", message='" + message + '\''
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Signal signal = (Signal) o;

        if (id != signal.id) return false;
        if (applicationType != null ? !applicationType.equals(signal.applicationType) : signal.applicationType != null)
            return false;
        if (applicationSuperType != null ? !applicationSuperType.equals(signal.applicationSuperType) : signal.applicationSuperType != null)
            return false;
        if (entityType != null ? !entityType.equals(signal.entityType) : signal.entityType != null) return false;
        if (entitySuperType != null ? !entitySuperType.equals(signal.entitySuperType) : signal.entitySuperType != null)
            return false;
        if (timestamp != null ? !timestamp.equals(signal.timestamp) : signal.timestamp != null) return false;
        if (signalType != null ? !signalType.equals(signal.signalType) : signal.signalType != null) return false;
        return message != null ? message.equals(signal.message) : signal.message == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (applicationType != null ? applicationType.hashCode() : 0);
        result = 31 * result + (applicationSuperType != null ? applicationSuperType.hashCode() : 0);
        result = 31 * result + (entityType != null ? entityType.hashCode() : 0);
        result = 31 * result + (entitySuperType != null ? entitySuperType.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + (signalType != null ? signalType.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}
