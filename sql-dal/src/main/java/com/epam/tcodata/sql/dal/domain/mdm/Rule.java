package com.epam.tcodata.sql.dal.domain.mdm;

import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.sql.dal.IStorable;
import com.epam.tcodata.sql.dal.domain.PrimaryKey;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.util.Objects;


public class Rule implements IStorable {
    public static class Fields {
        public static final String ID = "id";
        public static final String ACTIVE = "active";
        public static final String API_VERSION = "api_version";
        public static final String VERSION = "version";
        public static final String SUBSCRIPTION = "subscription";
        public static final String ENTITY = "entity";
        private Fields(){   /***  Default implementation ***/  }
    }

    @PrimaryKey
    @ColumnName(Fields.ID)
    private long id;

    @ColumnName(Fields.ACTIVE)
    private boolean active;

    @ColumnName(Fields.API_VERSION)
    private ApiVersion apiVersion;

    @ColumnName(Fields.VERSION)
    private KeyManagerVersion version;

    @ColumnName(Fields.SUBSCRIPTION)
    private String subscription;

    @ColumnName(Fields.ENTITY)
    private EntityType entityType;

    public Rule() {
    }

    /**
     * Main public constructor.
     * @param active - a flag that means that this rule is in usage.
     * @param apiVersion - the version of external API.
     * @param version - the version of the key manager.
     * @param subscription - the subscription for that this rule is acting.
     * @param entityType - kind of entity.
     */
    public Rule(boolean active, ApiVersion apiVersion, KeyManagerVersion version, String subscription, EntityType entityType) {

        this.id = -1;
        this.active = active;
        this.apiVersion = apiVersion;
        this.version = version;
        this.subscription = subscription;
        this.entityType = entityType;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ApiVersion getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(ApiVersion apiVersion) {
        this.apiVersion = apiVersion;
    }

    public KeyManagerVersion getVersion() {
        return version;
    }

    public void setVersion(KeyManagerVersion version) {
        this.version = version;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entity) {
        this.entityType = entity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return id == rule.id
                && active == rule.active
                && Objects.equals(apiVersion, rule.apiVersion)
                && Objects.equals(version, rule.version)
                && Objects.equals(subscription, rule.subscription)
                && Objects.equals(entityType, rule.entityType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, active, apiVersion, version, subscription, entityType);
    }

    @Override
    public String toString() {
        return "Rule{"
                + "id=" + id
                + ", active=" + active
                + ", api_version='" + apiVersion + '\''
                + ", version='" + version + '\''
                + ", subscription='" + subscription + '\''
                + ", entity_type='" + entityType + '\''
                + '}';
    }
}
