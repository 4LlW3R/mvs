package com.epam.tcodata.token.manager.driver;

import com.epam.tcodata.common.FactoryUtil;
import com.epam.tcodata.models.mix.Entity;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.Account;
import com.epam.tcodata.sql.dal.domain.pumps.AccountTokens;
import com.epam.tcodata.sql.dal.service.pumps.IAccountService;
import com.epam.tcodata.sql.dal.service.pumps.IAccountTokensService;
import com.epam.tcodata.token.manager.AccountInputStream;
import com.epam.tcodata.token.manager.factory.ITokenManagerFactory;
import com.epam.tcodata.token.manager.service.IMainService;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.reflect.ClassManifestFactory$;
import scala.reflect.ClassTag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.epam.tcodata.token.manager.util.DriverUtil.*;

public class TokenManagerStreamDriver {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenManagerStreamDriver.class);

    /**
     * Rest Token Manager.
     *
     * @param args expected arguments (appName, factoryClassName, batchIntervalSeconds).
     * @throws InterruptedException for spark context.
     */
    public static void main(String[] args) throws Exception {
        Options options = prepareOptions();
        options.addOption(createMandatoryOption(BATCH_INTERVAL_SECONDS, BATCH_INTERVAL_SECONDS));
        CommandLine commandLine = parseAsCommandLine(args, options);

        String factoryClassName = commandLine.getOptionValue(FACTORY_CLASS_NAME);
        long batchIntervalSeconds = Long.parseLong(commandLine.getOptionValue(BATCH_INTERVAL_SECONDS));
        String appName = commandLine.getOptionValue(APP_NAME);

        ITokenManagerFactory factory = FactoryUtil.loadFactory(ITokenManagerFactory.class, factoryClassName);
        ISecretStorage secretStorage = factory.createSecretStorage();

        long launchTime = 0;
        if (commandLine.hasOption(LAUNCH_TIME)) {
            launchTime = MILLIS_IN_SECOND * Long.parseLong(commandLine.getOptionValue(LAUNCH_TIME));
        }
        if (commandLine.hasOption(TEST_FACTORY_ABILITIES)) {
            Map<String, String> parameters = extractTestFactoryAbilities(commandLine);
            factory.setInitParameters(parameters);
        }

        LOGGER.info("Clean accountTokens table and create accountTokens for active accounts...");
        try (IDaoFactory daoFactory = factory.createPumpDaoFactory(secretStorage)) {
            fillAccountTokensTableBeforeStreamingStarts(factory, daoFactory, secretStorage);
        }

        SparkSession sparkSession = getSparkSession(appName);
        JavaSparkContext sparkContext = new JavaSparkContext(sparkSession.sparkContext());

        try (JavaStreamingContext jsc = new JavaStreamingContext(sparkContext, Durations.seconds(batchIntervalSeconds))) {
            handleJavaStreamingContext(jsc, factory);
            jsc.start();
            if (launchTime > 0) {
                jsc.awaitTerminationOrTimeout(launchTime);
            } else {
                jsc.awaitTermination();
            }
        }
    }

    private static void handleJavaStreamingContext(JavaStreamingContext jsc,
                                                   ITokenManagerFactory factory) throws Exception {
        JavaInputDStream<Account> inputDStream = createJavaInputDStream(jsc, factory);
        inputDStream.foreachRDD(rdd -> rdd.foreachPartition(handleAccountTokens(factory)));
    }

    /**
     * Fills accountTokens table once before streaming job starts.
     *
     * @param factory       factory.
     * @param daoFactory    dao factory to work with sql database.
     * @param secretStorage secret storage to access key vault.
     * @throws Exception exception.
     */
    private static void fillAccountTokensTableBeforeStreamingStarts(
            ITokenManagerFactory factory,
            IDaoFactory daoFactory,
            ISecretStorage secretStorage) throws Exception {

        IAccountService accountService = IDaoFactory.service(daoFactory, Account.class);
        IAccountTokensService accountTokensService = IDaoFactory.service(daoFactory, AccountTokens.class);
        accountTokensService.deleteAll();

        Map<String, Object> activeAccountFilter = new HashMap<>();
        activeAccountFilter.put(Account.Fields.IS_ACTIVE, true);
        List<Account> activeAccounts = accountService.readFiltered(activeAccountFilter);
        IMainService mainService = factory.createMainService(daoFactory, secretStorage);

        List<AccountTokens> accountTokensList = activeAccounts.stream()
                .map(mainService::createAccountTokens)
                .collect(Collectors.toList());
        accountTokensService.insert(accountTokensList);
    }

    private static <T extends Entity> JavaInputDStream<Account> createJavaInputDStream(JavaStreamingContext jsc,
                                                                                       ITokenManagerFactory factory) throws Exception {
        ClassTag<Account> classTag = ClassManifestFactory$.MODULE$.fromClass(Account.class);

        ISecretStorage secretStorage = factory.createSecretStorage();
        try (IDaoFactory daoFactory = factory.createPumpDaoFactory(secretStorage)) {
            IAccountService accountService = IDaoFactory.service(daoFactory, Account.class);
            AccountInputStream accountInputStream = new AccountInputStream(jsc, classTag, accountService);
            return new JavaInputDStream<Account>(accountInputStream, classTag);
        }
    }
}
