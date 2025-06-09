package com.epam.tcodata.models.datalake;

import com.epam.tcodata.models.ColumnName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AbstractDataLakeEntity implements IDataLakeEntity {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDataLakeEntity.class);
    private static final long serialVersionUID = 7455195170033525537L;

    @Override
    public String toString() {

        Class<? extends AbstractDataLakeEntity> clazz = getClass();
        StringBuilder sb = new StringBuilder(clazz.getSimpleName());
        sb.append('{');
        addFields(clazz, sb);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public final Object[] orderedValues() {
        List<Object> list = new ArrayList<>();
        collectOrderedValues(getClass(), list);
        return list.toArray();
    }

    private void collectOrderedValues(Class<?> clazz, List<Object> list) {
        if (clazz.getSuperclass() != null) {
            collectOrderedValues(clazz.getSuperclass(), list);
        }
        Field[] fields = clazz.getDeclaredFields();
        Arrays.stream(fields)
                .filter(f -> f.getAnnotation(ColumnName.class) != null)
                .map(f -> value(f))
                .forEach(list::add);
    }


    private void addFields(Class<?> clazz, StringBuilder sb) {
        if (clazz.getSuperclass() != null) {
            addFields(clazz.getSuperclass(), sb);
        }
        Field[] fields = clazz.getDeclaredFields();
        String collect = Arrays.stream(fields)
                .filter(f -> f.getAnnotation(ColumnName.class) != null)
                .map(f -> f.getName() + "='" + value(f) + "'")
                .collect(Collectors.joining(", "));
        sb.append(collect);
    }

    private Object value(Field field) {
        field.setAccessible(true);
        try {
            return field.get(this);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

}
