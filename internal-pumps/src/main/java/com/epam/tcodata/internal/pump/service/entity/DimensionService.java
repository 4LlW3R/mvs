package com.epam.tcodata.internal.pump.service.entity;

import com.epam.tcodata.hive.dal.IHive;
import com.epam.tcodata.hive.dal.domain.IHiveEntityType;
import com.epam.tcodata.hive.dal.repository.IHiveRepository;
import com.epam.tcodata.internal.pump.factory.IInternalFactory;
import com.epam.tcodata.models.datalake.IDataLakeEntity;
import com.epam.tcodata.models.datalake.raw.RawEntity;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.storage.StorageLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DimensionService implements IEntityService {
    private static final long serialVersionUID = 341464844010821847L;
    private static final Logger LOGGER = LoggerFactory.getLogger(DimensionService.class);

    private IHiveRepository<?> repository;
    private IHiveRepository<?> normRepository;
    private Class<? extends IDataLakeEntity> dataLakeClass;

    /**
     * Main Constructor.
     *
     * @param internalFactory factory that created this instance.
     * @param sparkSession    spark session.
     */
    public DimensionService(IInternalFactory internalFactory, SparkSession sparkSession) {
        this.dataLakeClass = (Class<? extends IDataLakeEntity>) internalFactory.getEntityType().getRawDataLakeClass();
        IHive hive = internalFactory.createHive(sparkSession);
        IHiveEntityType hiveEntityType = hive.databaseConfig().entityTypeByEntity(this.dataLakeClass, false);
        IHiveEntityType hiveNormEntityType = hive.databaseConfig().entityTypeByEntity(this.dataLakeClass, true);
        this.repository = hive.repository(hiveEntityType);
        this.normRepository = hive.repository(hiveNormEntityType);
    }

    @Override
    public void write(Dataset<Row> dataset) {
        LOGGER.info("Writing dataset to hive...");

        //write historical data
        Dataset<Row> newDataset = dataset.persist(StorageLevel.MEMORY_AND_DISK_SER());
        Dataset<Row> newDatasetCoalesced = newDataset.coalesce(2);

        repository.write(newDatasetCoalesced, SaveMode.Append);
        normRepository.merge(newDataset, RawEntity.Fields.DURABLE_ID, RawEntity.Fields.INGESTED_DATE_UTC);

        newDataset.unpersist();
    }
}
