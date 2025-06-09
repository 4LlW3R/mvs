package com.epam.tcodata.hive.dal.util;

import com.epam.tcodata.common.ResourceUtils;
import com.epam.tcodata.hive.dal.exception.ConversionException;
import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.IDataLakeEntity;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.*;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static java.lang.reflect.Array.newInstance;
import static org.apache.spark.sql.types.DataTypes.createStructField;
import static org.apache.spark.sql.types.DataTypes.createStructType;

/**
 * A utility class with bunch of useful methods.
 */
public final class HiveCommon {

    private static final Logger LOGGER = LoggerFactory.getLogger(HiveCommon.class);

    private static final String PROPERTIES_FILE = "hive.properties";

    private static final ConcurrentMap<Class<?>, StructType> ENTITY_SCHEMA_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentMap<Class<?>, Map<String, Field>> FIELDS = new ConcurrentHashMap<>();

    private HiveCommon() {
    }

    /**
     * Reads default properties from application settings file.
     *
     * @return property object.
     */
    public static Properties readDefaultProperties() {
        return ResourceUtils.readProperties(PROPERTIES_FILE);
    }

    /**
     * Creates Schema for an entity using reflection.
     *
     * @return schema.
     */
    public static StructType getEntitySchema(Class<?> clazz) {
        return ENTITY_SCHEMA_CACHE.computeIfAbsent(clazz, c -> createDataLakeSchema(c));
    }

    /**
     * Converts Row to Entity using reflection.
     * If the row object doesn't contain schema, it transformed into null value.
     *
     * @param row   Row object.
     * @param clazz entity class to which row should be converted.
     * @param <T>   goal type.
     * @return a new object, created by reflection.
     */
    public static <T extends IDataLakeEntity> T rowToEntity(Row row, Class<T> clazz) {

        Map<String, Field> fieldMap = FIELDS.computeIfAbsent(clazz, c -> createFieldMap(c));
        try {
            T entity = clazz.getDeclaredConstructor().newInstance();

            fieldMap.entrySet().forEach(e -> {
                String name = e.getKey();
                Field field = e.getValue();
                Object as = getValue(row, name, field.getType());
                try {
                    field.set(entity, as);
                } catch (Exception ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            });
            return entity;

        } catch (Exception e) {
            throw new ConversionException("Conversion to class " + clazz + " is impossible", e);
        }
    }


    /**
     * Converts single entity to Row object, using schema for class of given object.
     *
     * @param entity given object, null is not allowed.
     * @param <T>    entity type. Entity must implements IDataLakeEntity interface.
     * @return converted Row object.
     */
    public static <T extends IDataLakeEntity> Row entityToRow(T entity) {
        Class<? extends IDataLakeEntity> aClass = entity.getClass();
        StructType entitySchema = getEntitySchema(aClass);
        return new GenericRowWithSchema(entity.orderedValues(), entitySchema);
    }

    /**
     * Converts list of Row into list of entities. This method works for rows, that don't contain schemas.
     *
     * @param objects list of rows
     * @param clazz   class of destination entities
     * @param <T>     entity type. Entity must implements IDataLakeEntity interface.
     * @return list of converted entities.
     */
    public static <T extends IDataLakeEntity> List<T> rowListToEntityList(List<Row> objects, Class<T> clazz) {
        return objects
                .stream()
                .map(o -> rowToEntity(o, clazz))
                .collect(Collectors.toList());
    }

    /**
     * Converts entity list into Row list.
     *
     * @param objects list of entities.
     * @param <T>     entity type. Entity must implements IDataLakeEntity interface.
     * @return list of converted rows.
     */
    public static <T extends IDataLakeEntity> List<Row> entityListToRowList(List<T> objects) {
        return objects
                .stream()
                .map(HiveCommon::entityToRow)
                .collect(Collectors.toList());
    }

    /**
     * Converts given entities list into entity RDD.
     *
     * @param objects      list of entities.
     * @param sparkSession SparkSession object.
     * @param <T>          entity type. Entity must implements IDataLakeEntity interface.
     * @return rdd with given entities.
     */
    public static <T extends IDataLakeEntity> JavaRDD<T> entityListToEntityRdd(List<T> objects, SparkSession sparkSession) {
        return new JavaSparkContext(sparkSession.sparkContext()).parallelize(objects);
    }

    /**
     * Converts given rows into row RDD.
     *
     * @param objects      list of rows.
     * @param sparkSession SparkSession object.
     * @return rdd with given rows.
     */
    public static JavaRDD<Row> rowListToRowRdd(List<Row> objects, SparkSession sparkSession) {
        return new JavaSparkContext(sparkSession.sparkContext()).parallelize(objects);
    }

    /**
     * Converts entity list into row RDD.
     *
     * @param objects      list of entities.
     * @param sparkSession SparkSession object.
     * @param <T>          entity type. Entity must implements IDataLakeEntity interface.
     * @return rdd with transformed entities.
     */
    public static <T extends IDataLakeEntity> JavaRDD<Row> entityListToRowRdd(List<T> objects, SparkSession sparkSession) {
        List<Row> rows = entityListToRowList(objects);
        return new JavaSparkContext(sparkSession.sparkContext()).parallelize(rows);
    }

    /**
     * Converts row list into entity RDD.
     *
     * @param objects      list of rows.
     * @param sparkSession SparkSession object.
     * @param <T>          entity type. Entity must implements IDataLakeEntity interface.
     * @return rdd with transformed rows.
     */
    public static <T extends IDataLakeEntity> JavaRDD<T> rowListToEntityRdd(List<Row> objects, Class<T> clazz, SparkSession sparkSession) {
        List<T> entities = rowListToEntityList(objects, clazz);
        return new JavaSparkContext(sparkSession.sparkContext()).parallelize(entities);
    }


    /**
     * Converts row RDD into entity list.
     *
     * @param rowJavaRDD rdd from rows.
     * @param clazz      class of destination entities
     * @param <T>        entity type. Entity must implements IDataLakeEntity interface.
     * @return list of entities.
     */
    public static <T extends IDataLakeEntity> List<T> rowRddToEntityList(JavaRDD<Row> rowJavaRDD, Class<T> clazz) {
        List<Row> rows = rowJavaRDD.collect();
        return rowListToEntityList(rows, clazz);
    }

    /**
     * Converts entity RDD into row list.
     *
     * @param entityJavaRDD rdd from entities.
     * @param <T>           entity type. Entity must implements IDataLakeEntity interface.
     * @return list of rows.
     */
    public static <T extends IDataLakeEntity> List<Row> entityRddToRowList(JavaRDD<T> entityJavaRDD) {
        List<T> entities = entityJavaRDD.collect();
        return entityListToRowList(entities);
    }

    /**
     * Converts row RDD into entity RDD.
     *
     * @param rowJavaRDD rdd from rows.
     * @param <T>        entity type. Entity must implements IDataLakeEntity interface.
     * @return rdd from entities.
     */
    public static <T extends IDataLakeEntity> JavaRDD<T> rowRddToEntityRdd(JavaRDD<Row> rowJavaRDD, Class<T> clazz) {
        return rowJavaRDD
                .map(row -> rowToEntity(row, clazz));
    }

    /**
     * Converts entity RDD into row RDD.
     *
     * @param entityJavaRDD rdd from entities.
     * @param <T>           entity type. Entity must implements IDataLakeEntity interface.
     * @return rdd from rows.
     */
    public static <T extends IDataLakeEntity> JavaRDD<Row> entityRddToRowRdd(JavaRDD<T> entityJavaRDD) {
        return entityJavaRDD
                .map(entity -> entityToRow(entity));
    }


    /**
     * Converts entity RDD into entity Dataset.
     *
     * @param entityJavaRDD rdd from entities.
     * @param clazz         entity class
     * @param sparkSession  SparkSession object.
     * @param <T>           entity type. Entity must implements IDataLakeEntity interface.
     * @return data set.
     */
    public static <T extends IDataLakeEntity> Dataset<T> entityRddToEntityDataset(JavaRDD<T> entityJavaRDD, Class<T> clazz, SparkSession sparkSession) {
        Encoder<T> bean = Encoders.bean(clazz);
        return sparkSession.createDataset(entityJavaRDD.rdd(), bean);
    }

    /**
     * Converts row Dataset into entity Dataset.
     *
     * @param rowDataset   dataset from rows.
     * @param clazz        entity class
     * @param sparkSession SparkSession object.
     * @param <T>          entity type. Entity must implements IDataLakeEntity interface.
     * @return data set.
     */
    public static <T extends IDataLakeEntity> Dataset<T> rowDatasetToEntityDataset(Dataset<Row> rowDataset, Class<T> clazz, SparkSession sparkSession) {
        return entityRddToEntityDataset(rowRddToEntityRdd(rowDataset.javaRDD(), clazz), clazz, sparkSession);
    }

    /**
     * Converts entity Dataset into row Dataset.
     *
     * @param entityDataset dataset from entities.
     * @param clazz         entity class
     * @param sparkSession  SparkSession object.
     * @param <T>           entity type. Entity must implements IDataLakeEntity interface.
     * @return data set.
     */
    public static <T extends IDataLakeEntity> Dataset<Row> entityDatasetToRowDataset(Dataset<T> entityDataset, Class<T> clazz, SparkSession sparkSession) {
        JavaRDD<Row> rows = HiveCommon.entityRddToRowRdd(entityDataset.javaRDD());
        return HiveCommon.rowRddToRowDataset(rows, clazz, sparkSession);
    }

    /**
     * Converts row RDD into row Dataset (DataFrame).
     *
     * @param rowJavaRDD   rdd from rows.
     * @param clazz        underlying entity class. Needed to keep in account the schema.
     * @param sparkSession SparkSession object.
     * @param <T>          entity type. Entity must implements IDataLakeEntity interface.
     * @return data frame.
     */
    public static <T extends IDataLakeEntity> Dataset<Row> rowRddToRowDataset(JavaRDD<Row> rowJavaRDD, Class<T> clazz, SparkSession sparkSession) {
        StructType entitySchema = getEntitySchema(clazz);
        return sparkSession.createDataFrame(rowJavaRDD, entitySchema);
    }


    private static <T> Object getValue(Row row, String name, Class<?> type) {
        int i = row.fieldIndex(name);

        if (type == String.class) {
            return row.<String>getAs(i);
        }
        if (type == Long.class) {
            return row.<Long>getAs(i);
        }
        if (type == Date.class) {
            return row.<Date>getAs(i);
        }
        if (type == Timestamp.class) {
            return row.<Timestamp>getAs(i);
        }
        if (type == Double.class) {
            return row.<Double>getAs(i);
        }
        if (type == Integer.class) {
            return row.<Integer>getAs(i);
        }
        if (type == Boolean.class) {
            return row.<Boolean>getAs(i);
        }
        if (type == Byte.class) {
            return row.<Byte>getAs(i);
        }
        if (type == Float.class) {
            return row.<Float>getAs(i);
        }
        if (type == Short.class) {
            return row.<Short>getAs(i);
        }
        if (type.isArray()) {
            Class<?> componentType = type.getComponentType();
            List<Object> list = row.getList(i);
            T[] array = (T[]) newInstance(componentType, list.size());
            return list.toArray(array);
        }

        return row.getAs(i);
    }

    private static Map<String, Field> createFieldMap(Class<?> clazz) {
        Map<String, Field> fieldMap = new HashMap<>();
        collectFields(clazz, fieldMap);
        return fieldMap;
    }

    private static void collectFields(Class<?> clazz, Map<String, Field> fieldMap) {
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            collectFields(superclass, fieldMap);
        }

        Field[] fields = clazz.getDeclaredFields();
        Arrays.stream(fields)
                .filter(f -> f.getAnnotation(ColumnName.class) != null)
                .map(f -> {
                    f.setAccessible(true);
                    return f;
                })
                .forEach(f -> fieldMap.put(f.getAnnotation(ColumnName.class).value(), f));
    }

    private static DataType toDataType(Class<?> type, Class<?> keyType, Class<?> elementType) {

        if (type == byte.class || type == Byte.class) {
            return DataTypes.ByteType;
        }
        if (type == short.class || type == Short.class) {
            return DataTypes.ShortType;
        }
        if (type == int.class || type == Integer.class) {
            return DataTypes.IntegerType;
        }
        if (type == long.class || type == Long.class) {
            return DataTypes.LongType;
        }
        if (type == float.class || type == Float.class) {
            return DataTypes.FloatType;
        }
        if (type == double.class || type == Double.class) {
            return DataTypes.DoubleType;
        }
        if (type == boolean.class || type == Boolean.class) {
            return DataTypes.BooleanType;
        }
        if (type == BigDecimal.class) {
            return DataTypes.createDecimalType();
        }
        if (type == String.class) {
            return DataTypes.StringType;
        }
        if (type == Timestamp.class) {
            return DataTypes.TimestampType;
        }
        if (type == Date.class) {
            return DataTypes.DateType;
        }
        if (type == byte[].class) {
            return DataTypes.BinaryType;
        }
        if (type.isArray()) {
            DataType elementDataType = toDataType(type.getComponentType(), null, null);
            return DataTypes.createArrayType(elementDataType, false);
        }
        if (type == List.class && elementType != null) { // TO DO check correctness
            DataType elementDataType = toDataType(elementType, null, null);
            return DataTypes.createArrayType(elementDataType, false);
        }
        if (type == Map.class && keyType != null && elementType != null) { // TO DO check correctness
            DataType keyDataType = toDataType(keyType, null, null);
            DataType elementDataType = toDataType(elementType, null, null);
            return DataTypes.createMapType(keyDataType, elementDataType, false);
        }

        return DataTypes.NullType;
    }


    private static StructType createDataLakeSchema(Class<?> clazz) {
        List<StructField> structFields = new ArrayList<>();
        collectStructFields(clazz, structFields);
        return createStructType(structFields);
    }

    private static void collectStructFields(Class<?> clazz, List<StructField> structFields) {
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            collectStructFields(superclass, structFields);
        }

        Field[] fields = clazz.getDeclaredFields();
        Arrays.stream(fields)
                .filter(f -> f.getAnnotation(ColumnName.class) != null)
                .map(f -> createStructField(
                        f.getAnnotation(ColumnName.class).value(),
                        toDataType(f.getType(), null, null),
                        f.getAnnotation(ColumnName.class).nullable()))
                .forEach(structFields::add);
    }

}
