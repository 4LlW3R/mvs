package com.epam.tcodata.token.manager.driver;

import com.epam.tcodata.common.FactoryUtil;
import com.epam.tcodata.models.mix.Entity;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.Account;
import com.epam.tcodata.sql.dal.service.pumps.IAccountService;
import com.epam.tcodata.token.manager.factory.ITokenManagerFactory;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import java.util.Map;

import static com.epam.tcodata.token.manager.util.DriverUtil.*;

public class TokenManagerBatchDriver {

    /**
     * Rest Token Manager.
     *
     * @param args expected arguments (appName, factoryClassName).
     * @throws InterruptedException for spark context.
     */
    public static void main(String[] args) throws Exception {
        Options options = prepareOptions();
        CommandLine commandLine = parseAsCommandLine(args, options);

        String factoryClassName = commandLine.getOptionValue(FACTORY_CLASS_NAME);
        String appName = commandLine.getOptionValue(APP_NAME);

        ITokenManagerFactory factory = FactoryUtil.loadFactory(ITokenManagerFactory.class, factoryClassName);

        if (commandLine.hasOption(TEST_FACTORY_ABILITIES)) {
            Map<String, String> parameters = extractTestFactoryAbilities(commandLine);
            factory.setInitParameters(parameters);
        }

        SparkSession sparkSession = getSparkSession(appName);

        try (JavaSparkContext sparkContext = new JavaSparkContext(sparkSession.sparkContext())) {
            handle(sparkContext, factory);
        }
    }

    private static void handle(JavaSparkContext sparkContext,
                               ITokenManagerFactory factory) throws Exception {
        JavaRDD<Account> accountRdd = createAccountRdd(sparkContext, factory);
        accountRdd.foreachPartition(handleAccountTokens(factory));
    }

    private static <T extends Entity> JavaRDD<Account> createAccountRdd(JavaSparkContext sparkContext,
                                                                        ITokenManagerFactory factory) throws Exception {
        ISecretStorage secretStorage = factory.createSecretStorage();
        try (IDaoFactory daoFactory = factory.createPumpDaoFactory(secretStorage)) {
            IAccountService accountService = IDaoFactory.service(daoFactory, Account.class);
            return sparkContext.parallelize(accountService.readAll());
        }
    }
}
