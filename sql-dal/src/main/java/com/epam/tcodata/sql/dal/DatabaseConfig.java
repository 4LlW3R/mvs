package com.epam.tcodata.sql.dal;

import com.epam.tcodata.common.ResourceUtils;
import com.epam.tcodata.secure.storage.dal.ISecretIdentity;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.Secret;
import com.epam.tcodata.sql.dal.exception.WrongCredentialsException;
import com.epam.tcodata.sql.dal.util.SqlCommon;
import org.jdbi.v3.core.Jdbi;

import java.sql.Connection;
import java.util.Properties;

public enum DatabaseConfig {
    MDM("mdm-azure-sql", "dbo"),
    PUMPS("pump-azure-sql", "cfg"),
    SPEEDLAYER("speedlayer-azure-sql", "sl");

    private static final String HOSTNAME = "hostname";
    private static final String PORT = "port";
    private static final String DATABASE = "database";

    private String prefix;
    private String schema;

    private transient Jdbi jdbi = null;

    DatabaseConfig(String prefix, String schema) {
        this.prefix = prefix;
        this.schema = schema;
    }

    /**
     * Jdbi instance that is keeping for every database.
     *
     * @return
     */
    @SuppressWarnings("CPD-START")
    public synchronized Jdbi database(IDaoFactory daoFactory) {
        if (this.jdbi == null) {
            ISecretStorage secretStorage = daoFactory.getSecretStorage();
            Properties properties = SqlCommon.readDefaultProperties();
            ISecretIdentity userIdentity = Secret.Sql.user.usingSection(this.name());
            ISecretIdentity passwordIdentity = Secret.Sql.password.usingSection(this.name());
            String host = ResourceUtils.getProperty(this.prefix, properties, HOSTNAME);
            String port = ResourceUtils.getProperty(this.prefix, properties, PORT);
            String database = ResourceUtils.getProperty(this.prefix, properties, DATABASE);
            synchronized (this) {
                if (this.jdbi == null) {
                    String userName = secretStorage.retrieveSecret(userIdentity);
                    String password = secretStorage.retrieveSecret(passwordIdentity);
                    if (userName.isEmpty()) {
                        throw new WrongCredentialsException("User name in secret storage can't be empty: " + userIdentity.buildSecretFullName());
                    }
                    if (password.isEmpty()) {
                        throw new WrongCredentialsException("User password in secret storage can't be empty: " + passwordIdentity.buildSecretFullName());
                    }

                    this.jdbi = Jdbi.create(
                            daoFactory.buildURL(host, port, database),
                            userName,
                            password);
                }
            }
        }
        return this.jdbi;
    }

    /**
     * Create a standard JDBC Connection.
     *
     * @return connection.
     */
    public Connection createConnection(IDaoFactory daoFactory) {
        Jdbi jdbiVar = database(daoFactory);
        return jdbiVar.open()
                .getConnection();
    }

    public String getSchema() {
        return this.schema;
    }
}
