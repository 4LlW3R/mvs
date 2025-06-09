package com.epam.tcodata.mdm.base.impl;

import com.epam.tcodata.mdm.base.IMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ReflectionMapper implements IMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionMapper.class);
    @Override
    public Map<String, Object> map(Object object) {
        Map<String, Object> map = new HashMap<>();
        collectFields(map, object.getClass(), object);
        return map;
    }

    private void collectFields(Map<String, Object> map, Class<?> clazz, Object object) {
        if (clazz ==  null) {
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        Arrays.stream(fields).forEach(f -> {
            try {
                f.setAccessible(true);
                String name = f.getName();
                Object value = f.get(object);
                map.put(name, value);
            } catch (Exception e) {
                // TO DO log it
                LOGGER.error(e.getMessage());
            }
        });
        collectFields(map, clazz.getSuperclass(), object);
    }
}
