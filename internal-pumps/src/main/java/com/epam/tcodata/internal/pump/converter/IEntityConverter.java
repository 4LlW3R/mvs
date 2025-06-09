package com.epam.tcodata.internal.pump.converter;

import com.epam.tcodata.models.avro.util.AvroSerDeUtil;
import com.epam.tcodata.models.datalake.raw.RawEntity;
import com.epam.tcodata.models.enriched.IEnrichable;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.temporal.WeekFields;
import java.util.Arrays;

public interface IEntityConverter<T extends SpecificRecordBase, S extends IEnrichable, U extends RawEntity>
        extends Serializable {

    Logger LOGGER = LoggerFactory.getLogger(IEntityConverter.class);

    /**
     * Convert array of bytes into avro entity.
     *
     * @param avroClass avro class
     * @param bytes     bytes array
     * @return avro entity
     */
    default T convertToAvro(Class<T> avroClass, byte[] bytes) {
        T avro = null;
        try {
            avro = AvroSerDeUtil.deserialize(avroClass, bytes);
        } catch (IOException e) {
            LOGGER.error("Can't deserialize bytes to avro. Bytes: {}", Arrays.toString(bytes));
        }
        return avro;
    }

    S convertToEnriched(T avro);

    U convertToRaw(S enriched, Timestamp persistedDate);

    StructType getSchemaForWrite();

    Dataset<Row> convertToDataset(SparkSession sparkSession, JavaRDD<U> dataLakeEntityJavaRDD);

    /**
     * Method converts datetime to timestamp.
     *
     * @return timestamp
     */
    static Timestamp dateTimeToTimestamp(DateTime dateTime) {
        return dateTime == null || dateTime.equals(DateTime.parse("1700-01-01T00:00:00Z"))
                ? null : new Timestamp(dateTime.getMillis());
    }

    /**
     * Method converts timestamp to int year.
     *
     * @return year
     */
    static Integer timestampToYear(Timestamp timestamp) {
        return timestamp.toLocalDateTime().getYear();
    }

    /**
     * Method converts timestamp to int week number.
     *
     * @return week number
     */
    static Integer timestampToWeekNumber(Timestamp timestamp) {
        return timestamp.toLocalDateTime().get(WeekFields.ISO.weekOfWeekBasedYear());
    }

    /**
     * Method converts timestamp to int day of year.
     *
     * @return day of year
     */
    static Integer timestampToDayOfYear(Timestamp timestamp) {
        return timestamp.toLocalDateTime().getDayOfYear();
    }
}
