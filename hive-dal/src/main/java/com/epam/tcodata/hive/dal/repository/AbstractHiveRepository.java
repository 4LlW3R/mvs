package com.epam.tcodata.hive.dal.repository;


import com.epam.tcodata.hive.dal.IHive;
import com.epam.tcodata.hive.dal.domain.IHiveEntityType;
import com.epam.tcodata.models.datalake.IDataLakeEntity;
import org.apache.spark.sql.*;

import java.io.IOException;

public abstract class AbstractHiveRepository<T extends IDataLakeEntity> implements IHiveRepository<T> {
    private static final long serialVersionUID = 2784186580005264892L;

    private final IHiveEntityType hiveEntityType;
    private final IHive hive;

    /**
     * Public main constructor.
     *
     * @param hiveEntityType entity type for what this repository will work.
     * @param hive           the hive as database owner.
     */
    protected AbstractHiveRepository(IHiveEntityType hiveEntityType, IHive hive) {
        this.hiveEntityType = hiveEntityType;
        this.hive = hive;
    }

    @Override
    public IHiveEntityType getHiveEntityType() {
        return this.hiveEntityType;
    }

    @Override
    public final void write(Dataset<Row> dataset, SaveMode saveMode) {
        SparkSession sparkSession = this.hive.getSparkSession();
        use(sparkSession, this.hive.getDatabaseName());
        write(dataset, sparkSession, this.hiveEntityType, saveMode);
    }

    @Override
    public void merge(Dataset<Row> dataset, String partitionColumn, String orderColumn) {
        SparkSession sparkSession = this.hive.getSparkSession();
        use(sparkSession, this.hive.getDatabaseName());
        merge(dataset, sparkSession, hiveEntityType, partitionColumn, orderColumn);
    }

    @Override
    public final Dataset<Row> read() {
        SparkSession sparkSession = this.hive.getSparkSession();
        use(sparkSession, this.hive.getDatabaseName());
        return read(sparkSession, this.hiveEntityType, null);
    }

    @Override
    public final Dataset<Row> read(Column where) {
        SparkSession sparkSession = this.hive.getSparkSession();
        use(sparkSession, this.hive.getDatabaseName());
        return read(sparkSession, this.hiveEntityType, where);
    }

    @Override
    public final void clear() {
        SparkSession sparkSession = this.hive.getSparkSession();
        use(sparkSession, this.hive.getDatabaseName());
        clear(sparkSession);
    }

    @Override
    public void makeBackup(String backupName) {
        SparkSession sparkSession = this.hive.getSparkSession();
        use(sparkSession, this.hive.getDatabaseName());
        makeBackup(backupName, sparkSession, this.hiveEntityType);
    }

    protected abstract void use(SparkSession sparkSession, String name);

    protected abstract void write(Dataset<Row> dataset, SparkSession sparkSession, IHiveEntityType hiveEntityType, SaveMode saveMode);

    protected abstract void merge(Dataset<Row> dataset, SparkSession sparkSession, IHiveEntityType hiveEntityType, String partitionColumn, String orderColumn);

    protected abstract Dataset<Row> read(SparkSession sparkSession, IHiveEntityType hiveEntityType, Column where);

    protected abstract void clear(SparkSession sparkSession);

    protected abstract void makeBackup(String backupName, SparkSession sparkSession, IHiveEntityType hiveEntityType);

    private void writeObject(java.io.ObjectOutputStream stream)
            throws IOException {
        stream.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
    }
}
