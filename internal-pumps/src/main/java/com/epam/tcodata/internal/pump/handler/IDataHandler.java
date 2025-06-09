package com.epam.tcodata.internal.pump.handler;

import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.sql.dal.service.pumps.IHiveOffsetService;
import com.microsoft.azure.eventhubs.EventData;
import org.apache.spark.api.java.JavaRDD;

import java.io.Serializable;

/**
 * Defines data performing for given RDD.
 */
public interface IDataHandler extends Serializable {

    /**
     * Performs raw data (factually array of bytes).
     *
     * @param eventDataJavaRDD RDD with raw data.
     * @param hiveOffsetService hive offset service.
     */
    void handle(JavaRDD<EventData> eventDataJavaRDD, IHiveOffsetService hiveOffsetService, EntityType entityType);
}
