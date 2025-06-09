package com.epam.tcodata.sql.dal.repository;

import com.epam.tcodata.sql.dal.IColumnMapper;
import org.jdbi.v3.core.Handle;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This repository most dedicated to selecting data from several sources into a flat table. Each row of the table
 * is represented by object of type T. It is suitable for complex SELECTs with JOINs. Type T usually is synthetic type
 * only for representation of all columns of resulting SELECT.
 *
 * @param <T> type to that results are mapped.
 */
public interface ICustomRepository<T extends IColumnMapper> extends IRepository {

    List<T> readMany(Handle handle, Map<String, Object> filters);

    Optional<T> readOne(Handle handle, Map<String, Object> filters);
}
