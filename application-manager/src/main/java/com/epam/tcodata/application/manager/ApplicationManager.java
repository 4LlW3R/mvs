package com.epam.tcodata.application.manager;

import com.epam.tcodata.application.manager.service.SignalService;
import com.epam.tcodata.models.ApplicationSuperType;
import com.epam.tcodata.models.ApplicationType;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.SignalType;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.factory.ISecretStorageFactory;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.Signal;
import com.epam.tcodata.sql.dal.impl.pumps.PumpsDaoFactory;
import com.epam.tcodata.sql.dal.service.pumps.ISignalService;
import com.google.common.base.Strings;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Module that provides managing possibilities for all applications.
 */
public class ApplicationManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationManager.class);

    private static final String APPLICATIONS = "applications";
    private static final String ENTITIES = "entities";
    private static final String SIGNAL = "signal";
    private static final String MESSAGE_CAP = "message";
    private static final String DELIMITER = ",";

    private static Set<ApplicationType> applicationTypes;
    private static Set<EntityType> entityTypes;
    private static SignalType signalType;
    private static String message;

    /**
     * Run application manager.
     *
     * @param args expected arguments (applications, entities, signal, message)
     */
    public static void main(String[] args) {
        LOGGER.info("Input args: {}", Arrays.toString(args));
        LOGGER.info("Input args validating...");
        validateArgs(parseInputArgs(args));

        LOGGER.info("Creating signals...");
        SignalService signalManager = new SignalService();
        List<Signal> signals = signalManager.prepareSignals(
                applicationTypes,
                entityTypes,
                signalType,
                message);

        LOGGER.info(signals.size() + " signal(s) created. Sending...");
        ISecretStorageFactory secretStorageFactory = ISecretStorageFactory.createDefaultFactory();
        ISecretStorage secretStorage = secretStorageFactory.createSecretStorage(new Properties());
        try (IDaoFactory daoFactory = new PumpsDaoFactory(secretStorage)) {
            ISignalService mngSignalService = IDaoFactory.service(daoFactory, Signal.class);
            mngSignalService.insert(signals);
        } catch (Exception e) {
            String msg = "Can't send signals.";
            LOGGER.info(msg, e);
        }
        LOGGER.info("Done!");
    }

    /**
     * Method for validating input arguments.
     *
     * @param commandLine args
     */
    public static void validateArgs(CommandLine commandLine) {
        String applications = commandLine.getOptionValue(APPLICATIONS);
        if (!Strings.isNullOrEmpty(applications)) {
            applicationTypes = getApplicationTypes(applications);
        }

        if (isArgsContainPumps(applicationTypes)) {
            String entities = commandLine.getOptionValue(ENTITIES);
            if (!Strings.isNullOrEmpty(entities)) {
                entityTypes = getEntityTypes(entities);
            } else {
                String msg = "Pump application(s) provided without their entities.";
                throw new IllegalArgumentException(msg);
            }
        }

        String signal = commandLine.getOptionValue(SIGNAL);
        if (!Strings.isNullOrEmpty(signal)) {
            signalType = getSignalType(signal);
        }

        message = commandLine.getOptionValue(MESSAGE_CAP);
    }

    private static boolean isArgsContainPumps(Set<ApplicationType> applicationTypes) {
        return applicationTypes.stream()
                .anyMatch(applicationType -> applicationType.getSuperType() == ApplicationSuperType.PUMP);
    }

    /**
     * Parse input arguments.
     *
     * @param args input arguments.
     * @return command line.
     */
    public static CommandLine parseInputArgs(String[] args) {
        Options options = new Options();

        Option applications = new Option("a", APPLICATIONS, true, "affected applications, ex: "
                + Arrays.toString(ApplicationType.values()));
        applications.setRequired(true);
        options.addOption(applications);

        Option entities = new Option("e", ENTITIES, true, "affected entities, ex: "
                + Arrays.toString(EntityType.values()));
        entities.setRequired(false);
        options.addOption(entities);

        Option signal = new Option("s", SIGNAL, true, "signal, ex: "
                + Arrays.toString(SignalType.values()));
        signal.setRequired(true);
        options.addOption(signal);

        Option message = new Option("m", MESSAGE_CAP, true, "ex: planned stopping");
        message.setRequired(false);
        options.addOption(message);

        CommandLineParser clParser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        CommandLine cmd;
        try {
            cmd = clParser.parse(options, args);
        } catch (ParseException | NullPointerException e) {
            formatter.printHelp("Application Manager usage:", options);
            throw new IllegalArgumentException(e);
        }
        return cmd;
    }

    /**
     * Method returns list of affected applications.
     *
     * @param applications applications as a string
     * @return set of ApplicationType
     * @throws IllegalArgumentException if unknown enum provided.
     */
    public static Set<ApplicationType> getApplicationTypes(String applications) {
        try {
            Set<ApplicationType> applicationTypes = new HashSet<>();
            for (String app : applications.split(DELIMITER)) {
                applicationTypes.add(ApplicationType.valueOf(app));
            }
            return applicationTypes;
        } catch (IllegalArgumentException e) {
            String msg = "Unknown application provided. Available applications:" + Arrays.asList(ApplicationType.values());
            throw new IllegalArgumentException(msg, e);
        }
    }

    /**
     * Method returns list of affected entities.
     *
     * @param entities entities as a string
     * @return set of EntityType
     * @throws IllegalArgumentException if unknown enum provided.
     */
    public static Set<EntityType> getEntityTypes(String entities) {
        try {
            Set<EntityType> entityTypes = new HashSet<>();
            for (String entity : entities.split(DELIMITER)) {
                entityTypes.add(EntityType.valueOf(entity));
            }
            return entityTypes;
        } catch (IllegalArgumentException e) {
            String msg = "Unknown entities provided. Available entities:" + Arrays.asList(EntityType.values());
            throw new IllegalArgumentException(msg, e);
        }
    }

    /**
     * Method returns SignalType according to input signal.
     *
     * @param signal input signal
     * @return SignalType
     * @throws IllegalArgumentException if unknown enum provided.
     */
    public static SignalType getSignalType(String signal) {
        try {
            return SignalType.valueOf(signal);
        } catch (IllegalArgumentException e) {
            String msg = "Unknown signal provided. Available signals:" + Arrays.asList(SignalType.values());
            throw new IllegalArgumentException(msg, e);
        }
    }

}
