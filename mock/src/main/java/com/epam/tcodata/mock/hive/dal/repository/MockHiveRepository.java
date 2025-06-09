package com.epam.tcodata.mock.hive.dal.repository;

import com.epam.tcodata.hive.dal.IHive;
import com.epam.tcodata.hive.dal.domain.IHiveEntityType;
import com.epam.tcodata.hive.dal.repository.AbstractHiveRepository;
import com.epam.tcodata.hive.dal.util.HiveCommon;
import com.epam.tcodata.models.datalake.IDataLakeEntity;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.StructType;

import java.util.*;
import java.util.stream.Collectors;

public class MockHiveRepository<T extends IDataLakeEntity> extends AbstractHiveRepository<T> {
    private static final long serialVersionUID = 2784186580005264892L;

    private List<Row> list = Collections.synchronizedList(new ArrayList<>());

    public MockHiveRepository(IHiveEntityType entityType, IHive hive) {
        super(entityType, hive);
    }

    @Override
    protected synchronized void use(SparkSession sparkSession, String name) {
        /***  Default implementation ***/
    }

    @Override
    protected synchronized void write(Dataset<Row> dataset, SparkSession sparkSession, IHiveEntityType hiveEntityType, SaveMode saveMode) {
        List<Row> rowList = dataset.collectAsList();
        dataset.printSchema();
        switch (saveMode) {
            case Append:
                this.list.addAll(rowList);
                break;

            case Overwrite:
                list.clear();
                this.list.addAll(rowList);
                break;

            case Ignore:
                if (this.list.isEmpty()) {
                    this.list.addAll(rowList);
                }
                break;

            case ErrorIfExists:
                if (list.isEmpty()) {
                    this.list.addAll(rowList);
                } else {
                    throw new IllegalStateException("Table " + hiveEntityType + " isn't empty.");
                }
                break;
        }
    }

    @Override
    protected synchronized void merge(Dataset<Row> dataset, SparkSession sparkSession, IHiveEntityType hiveEntityType, String partitionColumn, String orderColumn) {
        Class<? extends IDataLakeEntity> entityClazz = hiveEntityType.getEntityClazz();
        Dataset<? extends IDataLakeEntity> dataset1 = HiveCommon.rowDatasetToEntityDataset(dataset, entityClazz, sparkSession);
        List<Row> union = HiveCommon.entityRddToRowList(dataset1.javaRDD());
        union.addAll(this.list);

        Map<Object, List<Row>> map = union.stream()
                .collect(Collectors.groupingBy(post -> post.getAs(partitionColumn)));
        List<Row> collect = map.entrySet().stream().map(e -> maxRowByField(e.getValue(), orderColumn)).collect(Collectors.toList());

        this.list.clear();
        this.list.addAll(collect);
    }

    @Override
    protected synchronized Dataset<Row> read(SparkSession sparkSession, IHiveEntityType hiveEntityType, Column where) {
        Class<T> entityClazz = (Class<T>) hiveEntityType.getEntityClazz();
        StructType entitySchema = HiveCommon.getEntitySchema(entityClazz);
        Dataset<Row> dataFrame = sparkSession.createDataFrame(this.list, entitySchema);
        if (where != null) {
            return dataFrame.where(where);
        }
        return dataFrame;
    }

    @Override
    protected synchronized void clear(SparkSession sparkSession) {
        this.list.clear();
    }

    @Override
    protected synchronized void makeBackup(String backupName, SparkSession sparkSession, IHiveEntityType hiveEntityType) {
        // nothing to do
    }

    private Row maxRowByField(List<Row> list, String field) {

        Comparator<Row> comparator = Comparator.comparing(row -> row.getAs(field));
        ArrayList<Row> res = new ArrayList<>();
        res.addAll(list);
        res.sort(comparator.reversed());
        return res.get(0);
    }
}
