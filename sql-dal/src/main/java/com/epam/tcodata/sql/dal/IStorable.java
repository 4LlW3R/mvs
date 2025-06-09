package com.epam.tcodata.sql.dal;

/**
 * IStorable interface defines set of methods for support CRUD operations in BaseRepository.
 */
public interface IStorable extends IColumnMapper {

    long getId();

    void setId(long id);

    default long getParentId() {
        return -1;
    }

    default void setParentId(long id) {
    }
}
