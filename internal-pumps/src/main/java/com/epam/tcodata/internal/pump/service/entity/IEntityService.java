package com.epam.tcodata.internal.pump.service.entity;


import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.io.Serializable;


/**
 * Service data layer.
 */
public interface IEntityService extends Serializable {

    /**
     * Writes given data set into repository with some transformation.
     *
     * @param dataset given data set.
     */
    void write(Dataset<Row> dataset);

}
