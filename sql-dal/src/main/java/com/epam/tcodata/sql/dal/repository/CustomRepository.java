package com.epam.tcodata.sql.dal.repository;

import com.epam.tcodata.sql.dal.IColumnMapper;
import com.epam.tcodata.sql.dal.IQuerySupplier;
import org.jdbi.v3.core.Handle;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A generic repository that supports of query given as supplier to select data from several tables.
 * All results are represented as a collection of T type entities.
 *
 * @param <T> - entity class.
 */
public class CustomRepository<T extends IColumnMapper> implements ICustomRepository<T> {

    private IQuerySupplier query;
    private Class<T> clazz;

    /**
     * Public main constructor.
     *
     * @param clazz - an entity class.
     * @param querySupplier - query that will be performed.
     */
    public CustomRepository(Class<T> clazz, IQuerySupplier querySupplier) {
        this.query = querySupplier;
        this.clazz = clazz;
    }

    @Override
    public List<T> readMany(Handle handle, Map<String, Object> filters) {

        return handle.createQuery(this.query.query())
                .bindMap(filters)
                .mapTo(this.clazz)
                .list();
    }

    @Override
    public Optional<T> readOne(Handle handle, Map<String, Object> filters) {

        return handle.createQuery(this.query.query())
                .bindMap(filters)
                .mapTo(this.clazz)
                .findFirst();
    }
}
