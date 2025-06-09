package com.epam.tcodata.hive.dal.domain;

import com.epam.tcodata.hive.dal.HiveConfig;
import com.epam.tcodata.models.datalake.IDataLakeEntity;

import java.util.List;

/**
 * This interface is needed for describing tables into a hive metastore.
 * The best way to use it is an enum that implements this interface. Elements of the enum should
 * match to each separate table into metastore.
 */
public interface IHiveEntityType {

    /**
     * Enumerates all elements (instances of derivative enum).
     * Use predefined method values() from enum for this purpose.
     * @return list of elements.
     */
    List<IHiveEntityType> entityTypes();

    /**
     * Class of entity of elements that stored into a table.
     *
     * @return Class
     */
    Class<? extends IDataLakeEntity> getEntityClazz();

    /**
     * Unique name of the entity.
     * Use method name() from enum for this purpose.
     *
     * @return name.
     */
    String entityName();

    /**
     * Each table is contained into some database (metastore). Each database is represented by HiveConfig element.
     * This is the element of HiveConfig which this table belongs to.
     *
     * @return
     */
    HiveConfig config();

    /**
     * It is an attribute, if the table is normalized.
     *
     * @return true if normalized.
     */
    boolean isNorm();

    /**
     * Name of table (file) in which entities of such kind are stored.
     *
     * @return file name.
     */
    default String tableName() {
        return config().tableByEntityType(this);
    }

    /**
     * Names of columns by which this table is partitioned.
     *
     * @return array of String;
     */
    String[] partitions();

    /**
     * Unique value for each entity kind/table.
     * Use either your own codes or just ordinal() method.
     *
     * @return
     */
    int getCode();
}
