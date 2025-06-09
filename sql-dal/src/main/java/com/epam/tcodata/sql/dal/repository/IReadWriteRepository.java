package com.epam.tcodata.sql.dal.repository;

import com.epam.tcodata.sql.dal.IColumnMapper;
import org.jdbi.v3.core.Handle;

import java.util.List;

/**
 * This repository add possibility of writing to base type of repository.
 *
 * @param <T> entity type.
 */
public interface IReadWriteRepository<T extends IColumnMapper> extends IReadOnlyRepository<T> {

    void createTable(Handle handle);

    int insert(Handle handle, T entity, boolean keepIds);

    void insert(Handle handle, List<T> entities, boolean keepIds);

    void update(Handle handle, T entity);

    void update(Handle handle, List<T> entities);

    void delete(Handle handle, List<Long> id);

    void deleteAll(Handle handle);

    String  tableName();
}
