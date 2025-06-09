package com.epam.tcodata.internal.pump.converter;


import com.epam.tcodata.models.datalake.raw.RawEntity;
import com.epam.tcodata.models.enriched.IEnrichable;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;


/**
 * This class is responsible for common behaviour for converters.
 *
 * @param <T> avro class
 * @param <S> MIX entity class
 * @param <U> Raw data lake entity class
 */
public abstract class AbstractEntityConverter<T extends SpecificRecordBase, S extends IEnrichable, U extends RawEntity>
        implements IEntityConverter<T, S, U> {

    private static final long serialVersionUID = 162379426777962181L;

    protected AbstractEntityConverter() { }

    @Override
    public final Dataset<Row> convertToDataset(SparkSession sparkSession, JavaRDD<U> rawEntityJavaRDD) {
        JavaRDD<Row> rows = rawEntityJavaRDD.map(rawEntity ->
                RowFactory.create(rawEntity.orderedValues()));
        StructType dataLakeSchema = getSchemaForWrite();

        return sparkSession.createDataFrame(rows, dataLakeSchema);
    }
}
