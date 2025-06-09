package com.epam.tcodata.models;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This attribute is for marking fields that should be stored in Hive for instance.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ColumnName {
    /**
     * Name of column in hive table.
     *
     * @return name
     */
    String value();

    /**
     * Sign if this columns is nullable.
     *
     * @return nullable
     */
    boolean nullable() default true;
}
