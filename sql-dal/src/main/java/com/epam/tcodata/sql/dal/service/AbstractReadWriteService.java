package com.epam.tcodata.sql.dal.service;

import com.epam.tcodata.sql.dal.DatabaseConfig;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.IStorable;
import com.epam.tcodata.sql.dal.repository.BaseReadWriteRepository;
import com.epam.tcodata.sql.dal.repository.IReadWriteRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Base implementation of IReadWriteService that bring the most functionality using a repository parameterized
 * with the given entity class.
 *
 * @param <T> entity class.
 */
public abstract class AbstractReadWriteService<T extends IStorable> extends JdbiService<T> implements IReadWriteService<T> {

    protected IReadWriteRepository<T> repository;

    /**
     * Public constructor with default mapper. In this case a reflection mapper will be used.
     *
     * @param factory - factory that produced this service.
     * @param databaseConfig - database config instance that keeps parameters to the needed database.
     * @param tableName - a table name where given entity should be stored.
     * @param crud - an entity with set of operations providers, especially for crud operations.
     * @param clazz - entity class.
     */
    protected AbstractReadWriteService(IDaoFactory factory, DatabaseConfig databaseConfig, String tableName, CRUD crud, Class<T> clazz) {
        super(factory, databaseConfig, clazz);
        this.repository = new BaseReadWriteRepository<>(databaseConfig.getSchema(), tableName, crud, clazz);
    }

    @Override
    public void createTable() {
        this.repository.createTable(openHandle());
    }

    @Override
    public List<T> readFiltered(Map<String, Object> fields) {
        return retryGet(handle -> this.repository.readFiltered(handle, fields));
    }

    @Override
    public Optional<T> read(Long id) {
        return retryGet(handle -> this.repository.read(handle, id));
    }

    @Override
    public List<T> readAll() {
        return retryGet(handle -> this.repository.readAll(handle));
    }

    @Override
    public List<T> readAll(Long parentId) {
        return retryGet(handle -> this.repository.readAllByParent(handle, parentId));
    }

    @Override
    public void update(T entity) {
        retry(handle -> this.repository.update(handle, entity));
    }

    @Override
    public void update(List<T> list) {
        retry(handle -> this.repository.update(handle, list));
    }

    @Override
    public void delete(long id) {
        retry(handle -> this.repository.delete(handle, Arrays.asList(id)));
    }

    @Override
    public void delete(T entity) {
        delete(entity.getId());
    }

    @Override
    public void deleteAll(List<T> list) {
        retry(handle -> this.repository.delete(handle, list.stream()
                .map(e -> Long.valueOf(e.getId()))
                .collect(Collectors.toCollection(ArrayList::new))));
    }

    @Override
    public void deleteAll() {
        retry(handle -> this.repository.deleteAll(handle));
    }

    @Override
    public long insert(T entity) {
        int id  = retryGet(handle -> this.repository.insert(handle, entity, false));
        entity.setId(id);
        return id;
    }

    @Override
    public void insert(List<T> list) {
        retry(handle -> this.repository.insert(handle, list, false));
    }

    @Override
    public void restore(List<T> list) {
        retry(handle -> this.repository.insert(handle, list, true));
    }
}
