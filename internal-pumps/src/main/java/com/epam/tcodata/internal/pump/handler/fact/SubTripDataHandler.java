package com.epam.tcodata.internal.pump.handler.fact;

import com.epam.tcodata.internal.pump.converter.IEntityConverter;
import com.epam.tcodata.internal.pump.factory.IInternalFactory;
import com.epam.tcodata.internal.pump.handler.AbstractDataHandler;
import com.epam.tcodata.internal.pump.service.entity.IEntityService;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.datalake.IDataLakeEntity;
import com.epam.tcodata.models.enriched.IEnrichable;
import com.epam.tcodata.sql.dal.service.pumps.IHiveOffsetService;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;

import java.sql.Timestamp;

public class SubTripDataHandler extends AbstractDataHandler {

    private static final long serialVersionUID = 2003446886468029136L;

    public SubTripDataHandler(IInternalFactory entityFactory, SparkSession sparkSession) {
        super(entityFactory, sparkSession);
    }

    @Override
    protected void handle(JavaRDD<IEnrichable> enrichedJavaRDD,
                          SparkSession sparkSession,
                          Timestamp persistedDateUtc,
                          IHiveOffsetService hiveOffsetService,
                          EntityType entityType) {

        IEntityService subTripService = super.internalFactory.createEntityService(sparkSession);
        IEntityConverter subTripConverter = super.internalFactory.createConverter();

        JavaRDD<IDataLakeEntity> dataLakeSubTripRDD = enrichedJavaRDD
                .map(enriched -> subTripConverter.convertToRaw(enriched, persistedDateUtc));


        subTripService.write(subTripConverter.convertToDataset(sparkSession, dataLakeSubTripRDD));

        long count = dataLakeSubTripRDD.count();
        insertHiveOffsetRecord(hiveOffsetService, EntityType.SUBTRIP, persistedDateUtc, (int) count);
    }
}
