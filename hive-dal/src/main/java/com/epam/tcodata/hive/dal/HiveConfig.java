package com.epam.tcodata.hive.dal;

import com.epam.tcodata.common.ResourceUtils;
import com.epam.tcodata.hive.dal.domain.IHiveEntityType;
import com.epam.tcodata.hive.dal.domain.prepared.PreparedAreaEntityType;
import com.epam.tcodata.hive.dal.domain.raw.RawAreaEntityType;
import com.epam.tcodata.hive.dal.util.HiveCommon;
import com.epam.tcodata.models.datalake.IDataLakeEntity;

import java.util.*;

/**
 * This enum contains elements that match to hive databases. Each database stores its own tables into Hive metastore.
 * To each element of this enum the implementation of interface IHiveEntityType is given. Via this implementation
 * the list of accessible tables are passed to each HiveConvig element. The reaal table names are stored into
 * property file, that is read on initializing of HiveConfig enum.
 */
public enum HiveConfig {

    RAW("raw", RawAreaEntityType.class),
    PREPARED("prepared", PreparedAreaEntityType.class);

    private static final String DATABASE_CAP = "database";
    private static final String ENTITY = "entity";


    private String database;
    private Map<IHiveEntityType, String> tables;

    /**
     * Main constructor of each element of this enum.
     *
     * @param prefix prefix of properties in property file and at the same time it is a database name.
     * @param clazz class of enum, that implements IHiveEntityType interface, that contains as many elements as
     *              there are tables into database.
     */
    HiveConfig(String prefix, Class<? extends IHiveEntityType> clazz) {

        IHiveEntityType[] enumConstants = clazz.getEnumConstants();

        Properties properties = HiveCommon.readDefaultProperties();

        this.database = ResourceUtils.getProperty(prefix, properties, DATABASE_CAP);
        this.tables = new HashMap<>();
        for (IHiveEntityType entityType : enumConstants) {
            String property = ResourceUtils.getProperty(prefix, properties, ENTITY + "." + entityType.entityName());
            if (property != null) {
                this.tables.put(entityType, property);
            }
        }
    }

    /**
     * Returns database name according settings.
     *
     * @return
     */
    public String database() {
        return this.database;
    }

    /**
     * Returns table names for the given database.
     *
     * @return
     */
    public Set<String> tableNames() {
        return new HashSet<>(this.tables.values());
    }

    /**
     * Returns entity type set according settings.
     *
     * @return
     */
    public Set<IHiveEntityType> entityTypes() {
        return this.tables.keySet();
    }

    /**
     * Returns entity type by its class and norm attribute.
     *
     * @param clazz class of entity type.
     * @param norm attribute.
     * @return IHiveEntityType instance.
     */
    public IHiveEntityType entityTypeByEntity(Class<? extends IDataLakeEntity> clazz, boolean norm) {
        Optional<IHiveEntityType> iHiveEntityType = this.tables.keySet()
                .stream()
                .filter(et -> et.isNorm() == norm && et.getEntityClazz() == clazz)
                .findFirst();
        IHiveEntityType iHiveEntityTypeItem = null;
        if (iHiveEntityType.isPresent()) {
            iHiveEntityTypeItem = iHiveEntityType.get();
        }
        return iHiveEntityTypeItem;
    }

    /**
     * Returns table name by entity type according settings.
     *
     * @param entityType
     * @return
     */
    public String tableByEntityType(IHiveEntityType entityType) {
        if (entityType == null) {
            return null;
        }
        return this.tables.get(entityType);
    }
}
