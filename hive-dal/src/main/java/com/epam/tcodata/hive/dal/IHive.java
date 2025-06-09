package com.epam.tcodata.hive.dal;

import com.epam.tcodata.hive.dal.domain.IHiveEntityType;
import com.epam.tcodata.hive.dal.repository.IHiveRepository;
import org.apache.spark.sql.SparkSession;

import java.io.Serializable;
import java.util.Set;

/**
 * This interface represents a separate hive database. Usually, instances of such interface are created by
 * a factory depending on test or production code is needed to run.
 *
 */
public interface IHive extends Serializable {

    /**
     * All tables names that belong to the database.
     *
     * @return set of strings.
     */
    Set<String> getTableNames();

    /**
     * Database name, as it is in metastore.
     *
     * @return name.
     */
    String getDatabaseName();

    /**
     * Instance of HiveConfig (element of enum) which is used to create this instance.
     *
     * @return HiveConfig element.
     */
    HiveConfig databaseConfig();

    /**
     * All entities types that can be stored into matched tables in this database.
     *
     * @return set of elements of enum.
     */
    Set<IHiveEntityType> entityTypes();

    /**
     * Returns an instance of repository for the given entity type. For every entity type the only one repository
     * is used.
     *
     * @param hiveEntityType entity type.
     * @return repository.
     */
    IHiveRepository repository(IHiveEntityType hiveEntityType);

    /**
     * Returns the spark session object that was used for creating this instance. The whole work for storing and restoring
     * of all data is performed via such object.
     *
     * @return spark session.
     */
    SparkSession getSparkSession();
}

