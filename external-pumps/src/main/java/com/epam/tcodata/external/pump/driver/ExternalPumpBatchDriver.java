package com.epam.tcodata.external.pump.driver;

import com.epam.tcodata.common.FactoryUtil;
import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.dto.maker.IDtoMaker;
import com.epam.tcodata.external.pump.factory.IExternalFactory;
import com.epam.tcodata.external.pump.source.IMixSource;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.mix.Entity;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.IDaoFactory;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.util.LongAccumulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Map;

import static com.epam.tcodata.external.pump.util.DriverUtils.*;

public class ExternalPumpBatchDriver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalPumpBatchDriver.class);

    /**
     * Rest Pump Drivers.
     *
     * @param args expected arguments (appName, batchIntervalSeconds, factoryClassName)
     * @throws InterruptedException for spark context.
     */
    public static void main(String[] args) throws Exception {
        Options options = prepareOptions();
        CommandLine commandLine = parseAsCommandLine(args, options);
        LOGGER.info("CommandLine options");
        for (Option option : commandLine.getOptions()) {
            LOGGER.info("{} - {}", option.getOpt(), option.getValue());
        }
        String appName = commandLine.getOptionValue(APP_NAME);
        String factoryClassName = commandLine.getOptionValue(FACTORY_CLASS_NAME);
        String dumpRddDirectory = null;
        if (commandLine.hasOption(DUMP_RDD_DIRECTORY)) {
            dumpRddDirectory = commandLine.getOptionValue(DUMP_RDD_DIRECTORY);
        }
        String additionalLoggingStr = commandLine.getOptionValue(ADDITIONAL_LOGGING);

        IExternalFactory externalFactory = FactoryUtil.loadFactory(IExternalFactory.class, factoryClassName);

        if (commandLine.hasOption(CURRENT_MOMENT)) {
            externalFactory.setCurrentMoment(Instant.parse(commandLine.getOptionValue(CURRENT_MOMENT)));
        }
        if (commandLine.hasOption(TEST_FACTORY_ABILITIES)) {
            Map<String, String> parameters = extractTestFactoryAbilities(commandLine);
            externalFactory.setInitParameters(parameters);
        }

        SparkSession sparkSession = getSparkSession(appName);

        boolean additionalLogging = Boolean.parseBoolean(additionalLoggingStr);

        try (JavaSparkContext sparkContext = new JavaSparkContext(sparkSession.sparkContext())) {
            handle(sparkContext, sparkSession, externalFactory, externalFactory.getEntityType(), dumpRddDirectory, additionalLogging);
        }
    }

    /**
     * Starts performing batch.
     */
    public static <T extends Entity> void handle(JavaSparkContext sparkContext,
                                                 SparkSession sparkSession,
                                                 IExternalFactory factory,
                                                 EntityType entityType,
                                                 String dumpRddDirectory,
                                                 boolean additionalLogging) throws Exception {
        JavaRDD<AbstractDto<T>> dtoRdd = createDtoRdd(sparkContext, sparkSession, factory, entityType);

        LongAccumulator mixRequestCount = sparkContext.sc().longAccumulator("MIX REQUEST COUNT");
        LongAccumulator mixSuccessfulRequest = sparkContext.sc().longAccumulator("MIX SUCCESSFUL REQUEST COUNT");
        LongAccumulator mixReceivedRecords = sparkContext.sc().longAccumulator("MIX RECEIVED RECORDS");
        LongAccumulator preparedToSendToEHRecords = sparkContext.sc().longAccumulator("PREPARED_TO_SEND_TO_EH_RECORDS");

        String driverHost = SparkSession.builder().getOrCreate().sparkContext().getConf().get("spark.driver.host");
        IMixSource mixSource = factory.createMixSource();
        JavaRDD<AbstractDto<T>> mixDtoRdd = dtoRdd.mapPartitions(requestData(mixSource,
                driverHost,
                factory,
                mixRequestCount,
                mixSuccessfulRequest,
                mixReceivedRecords));

        JavaRDD<AbstractDto<T>> enrichedDtoRdd = mixDtoRdd.mapPartitions(enrichDto(factory));

        handleEnrichedDto(enrichedDtoRdd,
                factory,
                dumpRddDirectory,
                sparkSession,
                entityType,
                additionalLogging,
                preparedToSendToEHRecords);

    }

    /**
     * Creates RDD by paralleling DTOs, containing information which is needed
     * to make request to MIX REST API.
     *
     * @param sparkContext javaSparkContext.
     * @param sparkSession sparkSession.
     * @return AbstractDto Rdd
     * @throws Exception exception.
     */
    private static <T extends Entity> JavaRDD<AbstractDto<T>> createDtoRdd(JavaSparkContext sparkContext,
                                                                           SparkSession sparkSession,
                                                                           IExternalFactory factory,
                                                                           EntityType entityType) throws Exception {
        ISecretStorage secretStorage = factory.createSecretStorage();
        try (IDaoFactory daoFactory = factory.createPumpDaoFactory(secretStorage)) {
            IDtoMaker<T> dtoMaker = factory.createDtoMaker(daoFactory, sparkSession);
            JavaRDD<AbstractDto<T>> rdd = sparkContext.parallelize(dtoMaker.makeDtoList());
            return dtoMaker.fillNonStaticInfo(rdd, entityType, factory);
        }
    }

}
