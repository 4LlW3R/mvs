package com.epam.tcodata.hive.dal.repository;

import com.epam.tcodata.hive.dal.IHive;
import com.epam.tcodata.hive.dal.domain.IHiveEntityType;
import com.epam.tcodata.models.datalake.IDataLakeEntity;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.spark.sql.*;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;
import org.apache.spark.storage.StorageLevel;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.first;

public class HiveRepository<T extends IDataLakeEntity> extends AbstractHiveRepository<T> {
    private static final long serialVersionUID = 2784186580005264892L;

    private static final String ORDER_FIELD_MAX = "order_field_max";

    public HiveRepository(IHiveEntityType hiveEntityType, IHive hive) {
        super(hiveEntityType, hive);
    }

    @Override
    protected void use(SparkSession sparkSession, String name) {
        sparkSession.sql("use " + name);
    }

    @Override
    protected void write(Dataset<Row> dataset, SparkSession sparkSession, IHiveEntityType hiveEntityType, SaveMode saveMode) {
        dataset.write()
                .mode(saveMode)
                .insertInto(hiveEntityType.tableName());
    }

    @Override
    protected void merge(Dataset<Row> dataset, SparkSession sparkSession, IHiveEntityType hiveEntityType, String partitionColumn, String orderColumn) {
        sparkSession.sql("DROP TABLE IF EXISTS "
                + getHiveEntityType().tableName() + "_temp");
        sparkSession.sql("create table "
                + getHiveEntityType().tableName() + "_temp as select * from "
                + getHiveEntityType().tableName());

        //get current dimensions from temporary table
        Dataset<Row> current = sparkSession
                .table(getHiveEntityType().tableName() + "_temp")
                .persist(StorageLevel.MEMORY_AND_DISK_SER());

        //union new dimensions with current
        Dataset<Row> union = current.union(dataset);

        WindowSpec windowSpec = Window
                .partitionBy(partitionColumn)
                .orderBy(col(orderColumn).desc());

        Dataset<Row> normDataset = union.withColumn(ORDER_FIELD_MAX, first(orderColumn).over(windowSpec))
                .select("*")
                .filter(col(ORDER_FIELD_MAX).equalTo(col(orderColumn)))
                .drop(ORDER_FIELD_MAX);

        Dataset<Row> normCoalescedDataset = normDataset.coalesce(2);

        //write normalized data
        write(normCoalescedDataset, SaveMode.Overwrite);

        current.unpersist();
    }

    @Override
    protected Dataset<Row> read(SparkSession sparkSession, IHiveEntityType hiveEntityType, Column where) {
        sparkSession.sql("REFRESH TABLE " + hiveEntityType.tableName());
        Dataset<Row> tableAsDataset = sparkSession.table(hiveEntityType.tableName());
        if (where != null) {
            return tableAsDataset.where(where);
        }
        return tableAsDataset;
    }

    @Override
    protected void clear(SparkSession sparkSession) {
        throw new NotImplementedException("clear method is not implemented");
    }

    @Override
    protected void makeBackup(String backupName, SparkSession sparkSession, IHiveEntityType hiveEntityType) {
        sparkSession.sql("DROP TABLE IF EXISTS " + backupName);
        sparkSession.sql("CREATE TABLE "
                + backupName + " as select * from "
                + hiveEntityType.tableName());
    }
}
