package com.epam.tcodata.sql.dal.service;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.config.ConfigRegistry;
import org.jdbi.v3.core.result.ResultProducers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static junit.framework.TestCase.assertNotSame;
import static junit.framework.TestCase.assertTrue;

/**
 * Container for unit tests covering {@link JdbiService} class behaviour processing SqlExceptions caused by remote server.
 * <p/>
 * See details at bug#651276: SQL error while internal pumps are working.
 * <p/>
 * Method name format within the test class: UnitOfWork_StateUnderTest_ExpectedBehavior
 * e.g., AddUser_ValidUserDetails_UserCanBeLoggedIn
 */
public class JdbiServiceTest {
    private static final String SQL_QUERY_SAMPLE = "SELECT 1 as fooField";

    private ConfigRegistry jdbiConfigRegistry = null;
    private Jdbi faultyJdbi = null;
    private Handle faultyHandle = null;

    @Before
    public void init() throws SQLException {
        jdbiConfigRegistry = new ConfigRegistry();
        ResultProducers jdbiResultProducersCfg = jdbiConfigRegistry.get(ResultProducers.class);
        jdbiResultProducersCfg.allowNoResults(true);

        Connection faultySqlConnectin = getFaultyConnectionMock();
        faultyJdbi = getJdbiWithFaultySqlConnection(faultySqlConnectin);
        faultyHandle = getHandleWithFaultySqlConnection(faultySqlConnectin);
    }

    @Test
    public void shouldRecreateHandle_handleIsNotNullAndNotClosedAndSqlConnectionIsFaulty_returnsTrue() {
        //arrange
        JdbiServiceFake jdbiService = new JdbiServiceFake(faultyJdbi, faultyHandle);

        //act
        boolean actualResult = jdbiService.shouldRecreateHandle();

        //assert
        assertTrue(actualResult);
    }

    @Test
    public void testDoubleTryGet_handleIsNotNullAndNotClosedAndSqlConnectionIsFaulty_recreatesHandleOnFault() {
        //arrange
        JdbiServiceFake jdbiService = new JdbiServiceFake(faultyJdbi, faultyHandle);
        Handle handleBeforeTest = jdbiService.getHandle();

        //act
        jdbiService.testReadWithFaultyPrepareStatementViaDoubleTryGet();

        //assert
        Handle handleAfterTest = jdbiService.getHandle();
        assertNotSame(handleBeforeTest, handleAfterTest);
    }

    private Handle getHandleWithFaultySqlConnection(final Connection faultyConnection) {
        Handle fakeHandle = Mockito.mock(Handle.class);

        Mockito.when(fakeHandle.getConnection()).thenReturn(faultyConnection);
        Mockito.when(fakeHandle.getConfig()).thenReturn(jdbiConfigRegistry);
        Mockito.when(fakeHandle.isClosed()).thenReturn(false);

        return fakeHandle;
    }

    private static Jdbi getJdbiWithFaultySqlConnection(final Connection faultyConnection) {
        Jdbi instance = Jdbi.create(() -> faultyConnection);
        instance.getConfig(ResultProducers.class).allowNoResults(true);

        return instance;
    }

    private static Connection getFaultyConnectionMock() throws SQLException {
        Connection fakeConnection = Mockito.mock(Connection.class);
        PreparedStatement fakePreparedStatement = Mockito.mock(PreparedStatement.class);

        Mockito
                .when(fakeConnection.prepareStatement(SQL_QUERY_SAMPLE, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY))
                .thenThrow(new SQLException("The connection is closed."))
                .thenReturn(fakePreparedStatement);

        Mockito.when(fakeConnection.isClosed())
                .thenThrow(new SQLException("The connection is closed."))
                .thenReturn(false);

        return fakeConnection;
    }

    static class JdbiServiceFake extends JdbiService<String> {
        /**
         * {@inheritDoc}
         */
        JdbiServiceFake(Jdbi initializedJdbiProvider, Handle handle) {
            super(initializedJdbiProvider, handle, 1, 1);
        }

        void testReadWithFaultyPrepareStatementViaDoubleTryGet() {
            @SuppressWarnings("unused")
            Optional<String> probe = retryGet(h -> h
                    .select(SQL_QUERY_SAMPLE)
                    .mapTo(String.class)
                    .findFirst()
            );
        }

        Handle getHandle() {
            return this.handle;
        }
    }
}