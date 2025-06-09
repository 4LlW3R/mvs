package com.epam.tcodata.hive.dal.repository;

import com.epam.tcodata.hive.dal.domain.IHiveEntityType;
import com.epam.tcodata.models.datalake.IDataLakeEntity;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;

import java.io.Serializable;

/**
 * This interface defines set of methods that allow write and read entities of one type into/from
 * orc files. All these methods work via spark session.
 *
 * @param <T> entitye type.
 */
public interface IHiveRepository<T extends IDataLakeEntity> extends Serializable {

    /**
     * Entity type which this repository can work with.
     *
     * @return element of enum, matched to the entity.
     */
    IHiveEntityType getHiveEntityType();

    /**
     * Writes Dataset of dedicated entity type to orc file with given mode.
     *
     * @param dataset  Dataset
     * @param saveMode define behaviour of writing - add, replace etc.
     */
    void write(Dataset<Row> dataset, SaveMode saveMode);

    /**
     * Merge data from the given data set and current table.
     *
     * @param dataset given data set
     * @param partitionColumn column by which grouping is made
     * @param orderColumn column by max value of which the only one row will be got
     */
    void merge(Dataset<Row> dataset, String partitionColumn, String orderColumn);

    /**
     * Reads the whole data from the repository as dataset.
     *
     * @return dataset of rows with the schema.
     */
    Dataset<Row> read();

    /**
     * Reads data with condition. The condition can be complex.
     *
     * @param where condition like where clause into SQS queries.
     * @return dataset of rows with the schema.
     */
    Dataset<Row> read(Column where);

    /**
     * Removes all data. Shouldn't work for 'real' repository, only for mock repository.
     */
    void clear();

    /**
     * Make copy of the table with given name.
     *
     * @param backupName name of backup table
     */
    void makeBackup(String backupName);
}
