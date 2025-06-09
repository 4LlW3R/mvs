package com.epam.tcodata.sql.dal.util;

import com.epam.tcodata.common.ResourceUtils;

import java.sql.Timestamp;
import java.util.Properties;

/**
 * A utility class with bunch of useful methods.
 */
public class SqlCommon {
    private static final String PROPERTIES_FILE         = "dal.properties";

    private SqlCommon() {
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
     * Clones given timestamp value.
     *
     * @param timestamp given value.
     * @return
     */
    public static Timestamp clone(Timestamp timestamp) {
        return timestamp == null
                ? null
                : new Timestamp(timestamp.getTime());
    }
}
