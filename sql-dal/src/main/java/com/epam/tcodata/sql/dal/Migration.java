package com.epam.tcodata.sql.dal;

import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.factory.ISecretStorageFactory;
import com.epam.tcodata.sql.dal.impl.mdm.MdmDaoFactory;
import com.epam.tcodata.sql.dal.impl.pumps.PumpsDaoFactory;
import com.epam.tcodata.sql.dal.impl.speedlayer.SpeedLayerDaoFactory;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

public class Migration {

    private static final Logger LOGGER = LoggerFactory.getLogger(Migration.class);

    /**
     * Migration entry point.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        try {
            if (args.length < 2) {
                LOGGER.error("Too few arguments. Usage: {} DatabaseConfig command\n"
                        + "Where:\n"
                        + "    DatabaseConfig - one of {}\n"
                        + "    command - one of {}.\n"
                        + "Commands:{}\n",
                        Migration.class.getSimpleName(), Arrays.toString(DatabaseConfig.values()),
                        Arrays.toString(LiquibaseCommand.values()),
                        Arrays.stream(LiquibaseCommand.values())
                                .map(v -> "    " + v.getDescription())
                                .collect(Collectors.joining("\n"))
                );
                System.exit(1);
            }
            String[] split = args[1].split(":");
            String command = split[0];
            String parameter = split.length > 1 ? split[1] : "";
            LiquibaseCommand liquidBaseCommand = LiquibaseCommand.byName(command);
            if (liquidBaseCommand == null) {
                LOGGER.error("Wrong command [{}]. Only {} are possible.", command, Arrays.toString(LiquibaseCommand.values()));
                System.exit(1);
            }

            DatabaseConfig config = DatabaseConfig.valueOf(args[0]);
            ISecretStorageFactory defaultFactory = ISecretStorageFactory.createDefaultFactory();
            ISecretStorage secretStorage = defaultFactory.createSecretStorage(new Properties());
            IDaoFactory daoFactory = null;
            String changeLogName = null;
            switch (config) {
                case MDM:
                    changeLogName = "mdm_changelog.xml";
                    daoFactory = new MdmDaoFactory(secretStorage);
                    break;

                case PUMPS:
                    changeLogName = "pumps_changelog.xml";
                    daoFactory = new PumpsDaoFactory(secretStorage);
                    break;

                case SPEEDLAYER:
                    changeLogName = "speed_layer_changelog.xml";
                    daoFactory = new SpeedLayerDaoFactory(secretStorage);
                    break;

                default: throw new Exception("Unknown database config: " + config);
            }
            java.sql.Connection connection = config.createConnection(daoFactory);
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(changeLogName,
                    new ClassLoaderResourceAccessor(), database);

            liquidBaseCommand.performAction(liquibase, parameter);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            System.exit(2);
        }
    }
}

enum LiquibaseCommand {
    ROLLBACK("rollback", "rollback:versionTag - Rollbacks the database to the tagged version.") {
        @Override
        public void performAction(Liquibase liquibase, String parameter) throws Exception {
            liquibase.rollback(parameter, new Contexts(), new LabelExpression(), getDefaultOutWriter());
        }
    },
    UPDATE("update", "update - Updates database structure to the latest change set.") {
        @Override
        public void performAction(Liquibase liquibase, String parameter) throws Exception {
            liquibase.update(new Contexts(), new LabelExpression());
        }
    },
    VALIDATE("validate", "validate - Checks changelogs for bad MD5Sums and preconditions.") {
        @Override
        public void performAction(Liquibase liquibase, String parameter) throws Exception {
            liquibase.validate();
        }
    },
    STATUS("status", "status - Prints list of not applied change sets to the STDOUT.") {
        @Override
        public void performAction(Liquibase liquibase, String parameter) throws Exception {
            liquibase.reportStatus(true, new Contexts(), new LabelExpression(), getDefaultOutWriter());
        }
    },
    UPDATE_SQL("updateSQL", "updateSQL - Writes SQL to update database to current version to STDOUT.") {
        @Override
        public void performAction(Liquibase liquibase, String parameter) throws Exception {
            liquibase.update(new Contexts(), new LabelExpression(), getDefaultOutWriter());
        }
    };

    private String name;
    private String description;

    LiquibaseCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public abstract void performAction(Liquibase liquibase, String parameter) throws Exception;

    @Override
    public String toString() {
        return this.name;
    }

    public static LiquibaseCommand byName(String name) {
        for (LiquibaseCommand val : values()) {
            if (val.name.equals(name)) {
                return val;
            }
        }
        return null;
    }

    static Writer getDefaultOutWriter() {
        return new OutputStreamWriter(System.out, StandardCharsets.UTF_8);
    }
}
