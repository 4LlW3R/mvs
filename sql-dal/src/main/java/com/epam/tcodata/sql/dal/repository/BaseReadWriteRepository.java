package com.epam.tcodata.sql.dal.repository;

import com.epam.tcodata.sql.dal.IColumnMapper;
import com.epam.tcodata.sql.dal.IQuerySupplier;
import com.epam.tcodata.sql.dal.IStorable;
import com.epam.tcodata.sql.dal.domain.PrimaryKey;
import com.epam.tcodata.sql.dal.exception.OperationIsNotSupportedException;
import com.epam.tcodata.sql.dal.service.CRUD;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.result.ResultBearing;
import org.jdbi.v3.core.statement.PreparedBatch;
import org.jdbi.v3.core.statement.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A generic class that support base operations like CRUD for the given entity class.
 *
 * @param <T> - entity class.
 */
public class BaseReadWriteRepository<T extends IStorable> implements IReadWriteRepository<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseReadWriteRepository.class);

    public static final String ID = "id";
    public static final String IDS = "ids";
    public static final String PARENT_ID = "parent_id";
    public static final String SCHEMA_CAP = "schema";
    public static final String TABLE = "table";
    public static final String COLUMNS = "columns";
    public static final String VALUES = "values";
    public static final String PAIR_LIST = "pairList";
    public static final String CONDITIONS = "conditions";

    public static final IQuerySupplier CREATE_SCHEMA_QUERY = () -> "CREATE SCHEMA IF NOT EXISTS <schema> ";
    public static final IQuerySupplier CREATE_TABLE_QUERY = () -> "CREATE TABLE IF NOT EXISTS <table> (<columns>) ";
    public static final IQuerySupplier READ_QUERY = () -> "SELECT * FROM <table>  WHERE id = :id ";
    public static final IQuerySupplier READ_ALL_QUERY = () -> "SELECT * FROM <table> ";
    public static final IQuerySupplier READ_ALL_SUB_QUERY = () -> "SELECT * FROM <table> WHERE parent_id = :parent_id";
    public static final IQuerySupplier DELETE_QUERY = () -> "DELETE FROM <table> WHERE id in (<ids>) ";
    public static final IQuerySupplier DELETE_ALL_QUERY = () -> "DELETE FROM <table> ";
    public static final IQuerySupplier INSERT_QUERY = () -> "INSERT INTO <table> (<columns>) VALUES (<values>)";
    public static final IQuerySupplier UPDATE_QUERY = () -> "UPDATE <table> SET <pairList> WHERE id = :id ";
    public static final IQuerySupplier FILTERED_QUERY = () -> "SELECT * FROM <table>  WHERE 1=1 <conditions>";
    private static final String IS_NOT_SUPPORTED = " is not supported.";

    private String schema;
    private String tableName;
    private CRUD crud;
    private Class<T> clazz;

    /**
     * Public main constructor.
     *
     * @param tableName - table name where data will be stored.
     * @param crud      - an instance of operations provider.
     * @param clazz     - an entity class.
     */
    public BaseReadWriteRepository(String schema, String tableName, CRUD crud, Class<T> clazz) {
        this.schema = schema;
        this.tableName = tableName;
        this.crud = crud;
        this.clazz = clazz;
    }

    @Override
    public void createTable(Handle handle) {
        try {
            Update createSchemaQuery = handle.createUpdate(CREATE_SCHEMA_QUERY.query())
                    .define(SCHEMA_CAP, this.schema);
            createSchemaQuery.execute();

            List<String> columns = IColumnMapper.columnsDefinitions(this.clazz);
            Update createTableQuery = handle.createUpdate(CREATE_TABLE_QUERY.query())
                    .define(TABLE, tableName())
                    .defineList(COLUMNS, columns);
            createTableQuery.execute();

        } catch (Exception e) {
            LOGGER.error(getClass().getSimpleName() + ".createTable({})", handle);
            throw e;
        }
    }

    @Override
    public List<T> readFiltered(Handle handle, Map<String, Object> filters) {

        try {
            Map<String, Object> existing = IColumnMapper.mapToExisting(this.clazz, filters);

            List<String> conditions = IColumnMapper.conditions(this.clazz, filters);
            String strConditions = String.join(" ", conditions.stream().map(a -> " AND " + a).collect(Collectors.toList()));
            Optional<IQuerySupplier> optional = Optional.ofNullable(this.crud.getFilteredQuerySupplier());

            existing = existing.entrySet()
                    .stream()
                    .filter(e -> e.getValue() != null)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            if (existing.isEmpty()) {
                return handle.createQuery(optional.orElse(FILTERED_QUERY).query())
                        .define(TABLE, tableName())
                        .define(CONDITIONS, strConditions)
                        .mapTo(this.clazz)
                        .list();
            }
            return handle.createQuery(optional.orElse(FILTERED_QUERY).query())
                    .define(TABLE, tableName())
                    .define(CONDITIONS, strConditions)
                    .bindMap(existing)
                    .mapTo(this.clazz)
                    .list();
        } catch (Exception e) {
            LOGGER.error(getClass().getSimpleName() + ".readFiltered({}, {})", handle, filters);
            throw e;
        }
    }

    @Override
    public Optional<T> read(Handle handle, long id) {
        if (!entityHasPrimaryKey()) {
            throw new OperationIsNotSupportedException("Primary key is not presented. Read with ID for entity " + this.clazz.getSimpleName()
                    + IS_NOT_SUPPORTED);
        }
        try {
            Optional<IQuerySupplier> optional = Optional.ofNullable(this.crud.getReadQuerySupplier());
            return handle.createQuery(optional.orElse(READ_QUERY).query())
                    .define(TABLE, tableName())
                    .bind(ID, id)
                    .mapTo(this.clazz)
                    .findFirst();
        } catch (Exception e) {
            LOGGER.error(getClass().getSimpleName() + ".read({}, {})", handle, id);
            throw e;
        }
    }

    @Override
    public List<T> readAll(Handle handle) {

        try {
            Optional<IQuerySupplier> optional = Optional.ofNullable(this.crud.getReadAllQuerySupplier());
            return handle.createQuery(optional.orElse(READ_ALL_QUERY).query())
                    .define(TABLE, tableName())
                    .mapTo(this.clazz)
                    .list();
        } catch (Exception e) {
            LOGGER.error(getClass().getSimpleName() + ".readAll({})", handle);
            throw e;
        }
    }

    @Override
    public List<T> readAllByParent(Handle handle, long id) {

        try {
            Optional<IQuerySupplier> optional = Optional.ofNullable(this.crud.getReadAllByParentQuerySupplier());
            return handle.createQuery(optional.orElse(READ_ALL_SUB_QUERY).query())
                    .define(TABLE, tableName())
                    .bind(PARENT_ID, id)
                    .mapTo(this.clazz)
                    .list();
        } catch (Exception e) {
            LOGGER.error(getClass().getSimpleName() + ".readAllByParent({}, {})", handle, id);
            throw e;
        }
    }

    @Override
    public int insert(Handle handle, T entity, boolean keepIds) {
        try {
            Optional<IQuerySupplier> optional = Optional.ofNullable(this.crud.getInsertQuerySupplier());
            Update update = handle.createUpdate(optional.orElse(INSERT_QUERY).query())
                    .define(TABLE, tableName())
                    .defineList(COLUMNS, entity.columns(keepIds))
                    .defineList(VALUES, entity.fields(keepIds))
                    .bindBean(entity);

            if (entityHasPrimaryKey()) {
                ResultBearing result = update.executeAndReturnGeneratedKeys(ID);

                Object[] keys = result.mapToMap().findOnly().values().toArray();
                Object key = keys.length == 0 ? null : keys[0];
                if (key instanceof Number) {
                    return ((Number) key).intValue();
                }
            } else {
                update.execute();
            }
            return -1;
        } catch (Exception e) {
            LOGGER.error("Single Entity Insert failed");
            throw e;
        }
    }

    @Override
    public void insert(Handle handle, List<T> entities, boolean keepIds) {
        try {
            if (entities.isEmpty()) {
                return;
            }
            Optional<IQuerySupplier> querySupplierOpt = Optional.ofNullable(this.crud.getInsertQuerySupplier());

            PreparedBatch preparedBatch =
                    handle.prepareBatch(querySupplierOpt.orElse(INSERT_QUERY).query())
                            .define(TABLE, tableName())
                            .defineList(COLUMNS, IColumnMapper.columns(this.clazz, keepIds));

            entities.forEach(entity -> preparedBatch
                    .defineList(VALUES, entity.fields(keepIds))
                    .bindBean(entity)
                    .add()
            );

            long executionStart = System.currentTimeMillis();
            preparedBatch.execute();
            long executionEnd = System.currentTimeMillis();
            LOGGER.info("#Batch insert time#: {} ms", executionEnd - executionStart);
        } catch (Exception e) {
            LOGGER.error("List of Entities' Insert failed");
            throw e;
        }
    }

    @Override
    public void update(Handle handle, T entity) {
        if (!entityHasPrimaryKey()) {
            throw new OperationIsNotSupportedException("Primary key is not presented. Update for entity " + this.clazz.getSimpleName()
                    + IS_NOT_SUPPORTED);
        }
        try {
            List<String> columns = entity.columns(false);
            List<String> fields = entity.fields(false);

            List<String> pairList = IntStream.range(0, columns.size())
                    .boxed()
                    .map(a -> columns.get(a) + "= " + fields.get(a))
                    .collect(Collectors.toList());

            Optional<IQuerySupplier> optional = Optional.ofNullable(this.crud.getUpdateQuerySupplier());
            int count = handle.createUpdate(optional.orElse(UPDATE_QUERY).query())
                    .define(TABLE, tableName())
                    .defineList(PAIR_LIST, pairList)
                    .bindBean(entity)
                    .execute();
            if (count == 0) {
                throw new IllegalStateException("Update failed");
            }
        } catch (Exception e) {
            LOGGER.error("Update failed");
            throw e;
        }
    }

    @Override
    public void update(Handle handle, List<T> entities) {
        if (!entityHasPrimaryKey()) {
            throw new OperationIsNotSupportedException("Primary key is not presented. Update for entity list of " + this.clazz.getSimpleName()
                    + IS_NOT_SUPPORTED);
        }
        try {
            if (entities.isEmpty()) {
                return;
            }

            Optional<IQuerySupplier> querySupplierOpt = Optional.ofNullable(this.crud.getUpdateQuerySupplier());
            PreparedBatch preparedBatch =
                    handle.prepareBatch(querySupplierOpt.orElse(UPDATE_QUERY).query())
                            .define(TABLE, tableName())
                            .defineList(COLUMNS, IColumnMapper.columns(this.clazz, false));


            entities.forEach(entity -> {

                List<String> columns = entity.columns(false);
                List<String> fields = entity.fields(false);

                List<String> pairList = IntStream.range(0, columns.size())
                        .boxed()
                        .map(a -> columns.get(a) + "= " + fields.get(a))
                        .collect(Collectors.toList());

                preparedBatch
                        .bindList(VALUES, entity.values(false))
                        .defineList(PAIR_LIST, pairList)
                        .bindBean(entity)
                        .add();

            });

            preparedBatch.execute();
        } catch (Exception e) {
            LOGGER.error(getClass().getSimpleName() + ".update({}, {})", handle, entities);
            throw e;
        }
    }

    @Override
    public void delete(Handle handle, List<Long> ids) {
        if (!entityHasPrimaryKey()) {
            throw new OperationIsNotSupportedException("Primary key is not presented. Delete for list of " + this.clazz.getSimpleName()
                    + IS_NOT_SUPPORTED);
        }
        try {
            if (ids.isEmpty()) {
                return;
            }
            Optional<IQuerySupplier> optional = Optional.ofNullable(this.crud.getDeleteQuerySupplier());
            handle.createUpdate(optional.orElse(DELETE_QUERY).query())
                    .define(TABLE, tableName())
                    .bindList(IDS, ids)
                    .execute();

        } catch (Exception e) {
            LOGGER.error(getClass().getSimpleName() + ".delete({}, {})", handle, ids);
            throw e;
        }
    }

    @Override
    public void deleteAll(Handle handle) {
        try {
            Optional<IQuerySupplier> optional = Optional.ofNullable(this.crud.getDeleteAllQuerySupplier());
            handle.createUpdate(optional.orElse(DELETE_ALL_QUERY).query())
                    .define(TABLE, tableName())
                    .execute();
        } catch (Exception e) {
            LOGGER.error(getClass().getSimpleName() + ".deleteAll({})", handle);
            throw e;
        }
    }

    @Override
    public String tableName() {
        return this.schema + "." + this.tableName;
    }

    public boolean entityHasPrimaryKey() {
        return Arrays.stream(this.clazz.getDeclaredFields())
                .anyMatch(f -> f.getAnnotation(PrimaryKey.class) != null);
    }
}
