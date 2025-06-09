package com.epam.tcodata.sql.dal;

import com.epam.tcodata.sql.dal.domain.PrimaryKey;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.lang.reflect.Field;
import java.sql.JDBCType;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * IStorable interface defines set of methods for support CRUD operations in BaseRepository.
 * In all cases, when pairs key-value are used you need to keep in mind following:
 * Fields names should match to the names in the table, not fields names in the object.
 * For example, if there is a field in an entity:
 *     \@ColumnName("api_version")
 *     private String apiVersion;
 * so, you need to use name "api_version", as the name in the table.
 */
public interface IColumnMapper {

    String ID = "id";

    /**
     * Returns a list of columns names for the entity.
     *
     * @param withId - determines if ID column need to be included into the list.
     * @return list of columns names. Null is not allowed. Empty list should be returned instead.
     */
    default List<String> columns(boolean withId) {
        return columns(getClass(), withId);
    }

    /**
     * Returns a list of fields names for the entity.
     *
     * @param withId - determines if ID column need to be included into the list.
     * @return list of columns names. Null is not allowed. Empty list should be returned instead.
     */
    default List<String> fields(boolean withId) {
        return fields(getClass(), withId);
    }

    /**
     * Returns a list of values for the entity.
     *
     * @param withId - determines if ID column need to be included into the list.
     * @return list of values. Null is not allowed. Empty list should be returned instead.
     */
    default List<Object> values(boolean withId) {

        Field[] fields = getClass().getDeclaredFields();
        return Arrays.stream(fields)
                .filter(f -> withId || !f.getName().equals(ID))
                .filter(f -> f.getAnnotation(ColumnName.class) != null)
                .map(f -> {
                    Object res = null;
                    try {
                        f.setAccessible(true);
                        res = f.get(this);
                    } catch (Throwable t) {
                        throw new RuntimeException(t);
                    }
                    return res;
                })
                .collect(Collectors.toList());
    }

    /**
     * Binds columns names and their values together into a map.
     *
     * @param withId - determines if ID column need to be included into the map.
     * @return map of pairs. Null is not allowed. Empty map should be returned instead.
     */
    default Map<String, Object> bind(boolean withId) {

        final List<String> columns = columns(withId);
        final List<Object> values = values(withId);
        final Map<String, Object> map = new HashMap<>();
        IntStream.range(0, Math.min(columns.size(), values.size()))
                .boxed()
                .forEach(i -> map.put(columns.get(i), values.get(i)));

        return map;
    }

    /**
     * A predicate that determines if the current entity matches to the given map.
     * All fields that absent in current object are ignored. Also, empty map means that no filter.
     * So matches(Collections.emptyMap()) always return true.
     *
     * @param fields - given map of key-value pairs.
     * @return true if the current entity matches.
     */
    default boolean matches(Map<String, Object> fields) {
        Map<String, Object> thisObject = this.bind(true);

        Map<String, Object> existing = fields.entrySet()
                .stream()
                .filter(e -> thisObject.containsKey(e.getKey()))
                .collect(HashMap::new, (m, v) -> m.put(v.getKey(), v.getValue()), HashMap::putAll);

        return thisObject.entrySet().containsAll(existing.entrySet());
    }


    /**
     * Return a list of columns names for the given glass, not entity.
     *
     * @param clazz  - entity class, that contains fields with ColumnName attributes.
     * @param withId - determines if ID column need to be included into the list.
     * @return a columns names list. Null is not allowed. Empty list should be returned instead.
     */
    static List<String> columns(Class<?> clazz, boolean withId) {

        Field[] fields = clazz.getDeclaredFields();
        return Arrays.stream(fields)
                .filter(f -> withId || !f.getName().equals(ID))
                .map(f -> f.getAnnotation(ColumnName.class))
                .filter(Objects::nonNull)
                .map(ColumnName::value)
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of columns names with corresponding sql type  for the given glass, not entity.
     * This list probably will be used in CREATE TABLE clause.
     *
     * @param clazz  - entity class, that contains fields with ColumnName attributes.
     * @return a columns names list. Null is not allowed. Empty list should be returned instead.
     */
    static List<String> columnsDefinitions(Class<?> clazz) {

        Field[] fields = clazz.getDeclaredFields();
        return Arrays.stream(fields)
                .filter(f -> f.getAnnotation(ColumnName.class) != null)
                .map(f -> f.getAnnotation(ColumnName.class).value()
                        + " " + getSqlTypeName(f.getType())
                        + (f.getAnnotation(PrimaryKey.class) != null ? " PRIMARY KEY AUTO_INCREMENT " : ""))
                .collect(Collectors.toList());
    }

    /**
     * Returns the name of SQL type corresponding to the given java type.
     *
     * @param clazz java type
     * @return name
     */
    static String getSqlTypeName(Class<?> clazz) {
        if (clazz == Integer.class || clazz == int.class) {
            return JDBCType.INTEGER.getName();
        }
        if (clazz == Long.class || clazz == long.class) {
            return JDBCType.BIGINT.getName();
        }
        if (clazz == Float.class || clazz == Double.class || clazz == float.class || clazz == double.class) {
            return JDBCType.NUMERIC.getName();
        }
        if (clazz == Boolean.class || clazz == boolean.class) {
            return JDBCType.BOOLEAN.getName();
        }
        if (clazz == Timestamp.class) {
            return JDBCType.TIMESTAMP.getName();
        }
        if (clazz == Time.class) {
            return JDBCType.TIME.getName();
        }
        return JDBCType.VARCHAR.getName();
    }

    /**
     * Return a list of fields names for the given glass, not entity.
     *
     * @param clazz  - entity class, that contains fields with ColumnName attributes.
     * @param withId - determines if ID column need to be included into the list.
     * @return a columns names list. Null is not allowed. Empty list should be returned instead.
     */
    static List<String> fields(Class<?> clazz, boolean withId) {

        Field[] fields = clazz.getDeclaredFields();
        return Arrays.stream(fields)
                .filter(f -> withId || !f.getName().equals(ID))
                .filter(f -> Objects.nonNull(f.getAnnotation(ColumnName.class)))
                .map(f -> ":" + f.getName())
                .collect(Collectors.toList());
    }

    /**
     * Assembles a list of conditions for given class that should be given to WHERE phrase in SELECT statement.
     *
     * @param clazz   - entity class, that contains fields with ColumnName attributes.
     * @param filters - fields names set to filter which fields should be included into the result list.
     * @return a list of conditions.
     */
    static List<String> conditions(Class<?> clazz, Map<String, Object> filters) {
        Field[] fields = clazz.getDeclaredFields();
        return Arrays.stream(fields)
                .map(f -> new Pair(f, f.getAnnotation(ColumnName.class)))
                .filter(p -> p.attr != null)
                .filter(p -> filters.containsKey(p.attr.value()))
                .map(p -> p.attr.value() + (filters.get(p.attr.value()) == null ? " IS NULL" : "=:" + p.field.getName()))
                .collect(Collectors.toList());
    }

    /**
     * Filters and converts only columns that contain in the table and convert field name into java field name.
     * Usually this method is needed to pass its result to binding. So, names should match to java fields of the entity.
     * Example:
     * Assume there is a class
     * class Entity {
     *     \@ColumnName("existing")
     *     private int existing;
     *     \@ColumnName("another_existing")
     *     private int anotherExisting;
     *     \@ColumnName("one_more")
     *     private int oneMore;
     * }
     * so this function will convert that map:
     * Map("existing":1, "non_existing":2, "another_existing")
     * into this
     * Map("existing":1, "anotherExisting")
     *
     * @param clazz - entity class.
     * @param filters - given map.
     * @return filtered map.
     */
    static Map<String, Object> mapToExisting(Class<?> clazz, Map<String, Object> filters) {
        Field[] fields = clazz.getDeclaredFields();

        Map<String, Object> result = Arrays.stream(fields)
                .map(f -> new Pair(f, f.getAnnotation(ColumnName.class)))
                .filter(a -> a.attr != null)
                .filter(p -> filters.containsKey(p.attr.value()))
                .collect(LinkedHashMap::new,
                        (map, item) -> map.put(item.field.getName(), filters.get(item.attr.value())),
                        Map::putAll);
        return result;
    }

    /**
     * Just auxiliary class to keep field-attribute pairs.
     */
    class Pair {
        Field field;
        ColumnName attr;

        Pair(Field field, ColumnName attr) {
            this.field = field;
            this.attr = attr;
        }
    }

}
