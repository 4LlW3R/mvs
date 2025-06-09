package com.epam.tcodata.raw.prepared.etl.converter;

import com.epam.tcodata.models.datalake.prepared.PreparedEntity;
import com.epam.tcodata.models.datalake.raw.RawEntity;
import org.apache.spark.api.java.JavaRDD;

import java.io.Serializable;

@FunctionalInterface
public interface ISingleDomainModelConverter<T extends RawEntity, U extends PreparedEntity>
        extends Serializable {

    U convert(T raw);

    default JavaRDD<U> convertRDD(JavaRDD<T> rawRDD) {
        return rawRDD.map(this::convert);
    }
}
