package com.epam.tcodata.sql.dal.service;

import java.util.List;

public interface IReadWriteService<T> extends IReadOnlyService<T> {

    default void createTable() { }

    long insert(T entity);

    void insert(List<T> list);

    void restore(List<T> list);

    void update(T entity);

    void update(List<T> list);

    void delete(long id);

    void delete(T entity);

    void deleteAll();

    default void deleteAll(List<T> list)  { list.forEach(this::delete); }
}
