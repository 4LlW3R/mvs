package com.epam.tcodata.sql.dal.repository;

import com.epam.tcodata.sql.dal.IColumnMapper;
import org.jdbi.v3.core.Handle;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This repository should work with entities of type T. It can only read such entities from a storage.
 *
 * @param <T> entity type
 */
public interface IReadOnlyRepository<T extends IColumnMapper> extends IRepository {

    /**
     * Selects list of entities that matches to filters.
     * (Filter is map of type: String - name of column, Object - value in that column).
     *
     * @param handle  object that represents connection.
     * @param filters filter to read from db.
     * @return List T.
     */
    List<T> readFiltered(Handle handle, Map<String, Object> filters);

    Optional<T> read(Handle handle, long id);

    List<T> readAll(Handle handle);

    List<T> readAllByParent(Handle handle, long id);
}

