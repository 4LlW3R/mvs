package com.epam.tcodata.internal.pump.service.entity;

import com.epam.tcodata.hive.dal.IHive;
import com.epam.tcodata.hive.dal.domain.IHiveEntityType;
import com.epam.tcodata.hive.dal.repository.IHiveRepository;
import com.epam.tcodata.internal.pump.factory.IInternalFactory;
import com.epam.tcodata.models.datalake.IDataLakeEntity;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.storage.StorageLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FactService implements IEntityService {
    private static final long serialVersionUID = 2352305688936708973L;
    private static final Logger LOGGER = LoggerFactory.getLogger(FactService.class);

    private IHiveRepository repository;

    /**
     * Main Constructor.
     *
     * @param internalFactory factory that created this instance.
     * @param sparkSession    spark session.
     */
    public FactService(IInternalFactory internalFactory, SparkSession sparkSession) {
        IHive hive = internalFactory.createHive(sparkSession);
        Class<? extends IDataLakeEntity> dataLakeClass = (Class<? extends IDataLakeEntity>) internalFactory.getEntityType().getRawDataLakeClass();
        IHiveEntityType hiveEntityType = hive.databaseConfig().entityTypeByEntity(dataLakeClass, false);
        this.repository = hive.repository(hiveEntityType);
    }

    @Override
    public void write(Dataset<Row> dataset) {
        LOGGER.info("Writing dataset to hive...");
        Dataset<Row> newDataset = dataset.persist(StorageLevel.MEMORY_AND_DISK_SER());
        Dataset<Row> coalesce = newDataset.coalesce(2);

        this.repository.write(coalesce, SaveMode.Append);

        newDataset.unpersist();
    }

}
