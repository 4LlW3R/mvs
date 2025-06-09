package com.epam.tcodata.sql.dal.service;

import com.epam.tcodata.sql.dal.DatabaseConfig;
import com.epam.tcodata.sql.dal.IDaoFactory;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.JdbiException;
import org.jdbi.v3.core.mapper.reflect.FieldMapper;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.Duration;
import java.util.function.Consumer;
import java.util.function.Function;


public class JdbiService<T> implements IService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbiService.class);

    private static final int MAX_RETRIES = 3;
    private static final int DELAY_SECONDS = 60;

    private int maxRetries;
    private int delaySeconds;
    private Jdbi jdbi;
    protected Handle handle;
    private IDaoFactory factory;

    /**
     * Main public constructor.
     *
     * @param factory        - factory that produced this service.
     * @param databaseConfig - database configuration.
     * @param clazz          - entity class.
     */
    public JdbiService(IDaoFactory factory, DatabaseConfig databaseConfig, Class<T> clazz) {
        this.factory = factory;
        this.jdbi = databaseConfig.database(factory).installPlugin(new SqlObjectPlugin());
        this.jdbi.registerRowMapper(clazz, FieldMapper.of(clazz));
        this.maxRetries = MAX_RETRIES;
        this.delaySeconds = DELAY_SECONDS;
    }

    /**
     * Testing constructor. Ingests both already configured Jdbi and Handle instances.
     *
     * @param initializedJdbiProvider lambda producing initialized {@link Jdbi} instance
     * @param handle                  given handle (could be null for testing purposes)
     */
    JdbiService(Jdbi initializedJdbiProvider, Handle handle, int maxRetries, int delaySeconds) {
        this.factory = null;
        this.jdbi = initializedJdbiProvider;
        this.handle = handle;
        this.maxRetries = maxRetries;
        this.delaySeconds = delaySeconds;
    }

    @Override
    public void close() {
        if (this.handle != null && !this.handle.isClosed()) {
            try {
                this.handle.close();
            } catch (JdbiException ex) {
                LOGGER.error(ex.getMessage(), ex);
            } finally {
                this.handle = null;
            }
        }
    }

    @Override
    public IDaoFactory factory() {
        return this.factory;
    }

    @Override
    public boolean checkConnection() {
        return !openHandle().isClosed();
    }

    @Override
    public void beginTransaction() {
        openHandle().begin();
    }

    @Override
    public void commitTransaction() {
        openHandle().commit();
    }

    @Override
    public void rollbackTransaction() {
        openHandle().rollback();
    }

    protected Handle openHandle() {
        if (shouldRecreateHandle()) {
            this.handle = this.jdbi.open();
        }
        return this.handle;
    }

    boolean shouldRecreateHandle() {
        boolean result = this.handle == null
                || this.handle.isClosed()
                || this.handle.getConnection() == null;
        if (!result) {
            try {
                result = this.handle.getConnection().isClosed();
            } catch (SQLException ex) {
                LOGGER.error(ex.getMessage(), ex);
                result = true;
            }
        }
        return result;
    }

    @SuppressWarnings("CPD-START")
    <R> R retryGet(Function<Handle, R> function) {
        RetryPolicy<R> retryPolicy = new RetryPolicy<R>();
        mixinHandle(retryPolicy);
        return Failsafe.with(retryPolicy).get(() -> function.apply(openHandle()));
    }

    @SuppressWarnings("CPD-START")
    void retry(Consumer<Handle> consumer) {
        RetryPolicy retryPolicy = new RetryPolicy();
        mixinHandle(retryPolicy);
        Failsafe.with(retryPolicy).run(() -> consumer.accept(openHandle()));
    }

    private void mixinHandle(RetryPolicy<?> policy) {
        policy.handle(JdbiException.class, SQLException.class)
                .onRetry(h -> close())
                .withMaxRetries(maxRetries)
                .withDelay(Duration.ofSeconds(delaySeconds))
                .onFailedAttempt(e -> LOGGER.error("Connection attempt failed", e.getLastFailure()))
                .onRetry(e -> LOGGER.warn("Failure #{}. Retrying.", e.getAttemptCount()))
                .onRetriesExceeded(e -> LOGGER.warn("Failed to connect. Max retries exceeded."))
                .onAbort(e -> LOGGER.warn("Connection retry aborted due to {}.", e.getFailure()));
    }
}
