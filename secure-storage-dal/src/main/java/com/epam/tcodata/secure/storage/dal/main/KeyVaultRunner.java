package com.epam.tcodata.secure.storage.dal.main;

import com.epam.tcodata.secure.storage.dal.ISecretIdentity;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.Secret;
import com.epam.tcodata.secure.storage.dal.factory.ISecretStorageFactory;
import com.epam.tcodata.secure.storage.dal.factory.impl.KeyVaultSecretStorageFactory;
import com.epam.tcodata.secure.storage.dal.impl.KeyVaultSecretStorage;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class KeyVaultRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(KeyVaultRunner.class);

    private enum Command {
        SET(true, "set all given secrets"),
        GET(true, "get all given secrets by its names"),
        LIST(false, "list all possible secret names");

        private boolean needsValues;
        private String description;

        Command(boolean needsValues, String description) {
            this.needsValues = needsValues;
            this.description = description;
        }

        public boolean isNeedsValues() {
            return this.needsValues;
        }

        public static String createFullDescription() {

            return Arrays.stream(values())
                    .map(v -> "  " + v.name() + ": " + v.description)
                    .collect(Collectors.joining("\n"));
        }
    }


    /**
     * Main entry point for KeyVaultRunner.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        int exitCode = 0;
        try {
            Options options = new Options();
            Option vaultBaseUrlOption = addOption(options, false, "vaultBaseUrl", "URL",
                    "vault base URL");
            Option clientIdOption = addOption(options, false, "clientId", "UUID",
                    "client id to access KeyVault");
            Option clientSecretOption = addOption(options, false, "clientSecret", "secret",
                    "client secret to access KeyVault");
            Option encryptionKeyIdOption = addOption(options, false, "encryptionKeyId", "ID",
                    "id to encrypt/decrypt values to/from KeyVault");
            Option valuesOption = addOption(options, false, "values", "key:value",
                    "set of key-value pairs separated by comma");
            Option helpOption = addOption(options, false, "help", null,
                    "Print this message.");

            int size = Secret.collectAllIdentifiers().size();
            valuesOption.setValueSeparator(',');
            valuesOption.setArgs(size);

            CommandLineParser parser = new DefaultParser();
            CommandLine line = null;

            //---------------------------------------------------------------------------------------------------------------------
            // parsing main options that can lead to exit immediately
            //---------------------------------------------------------------------------------------------------------------------
            try {
                // parse the command line arguments
                line = parser.parse(options, args);
            } catch (ParseException exp) {
                // oops, something went wrong
                LOGGER.error("Incorrect parameters: {}", exp.getMessage());
                printHelp(options);
                System.exit(1);
            }

            if (line.hasOption(helpOption.getOpt())) {
                printHelp(options);
                System.exit(0);
            }

            //---------------------------------------------------------------------------------------------------------------------
            // prepare all parameters
            //---------------------------------------------------------------------------------------------------------------------
            String vaultBaseUrl = getOptionValue(line, vaultBaseUrlOption.getOpt(), KeyVaultSecretStorageFactory.SYSTEM_VAR_VAULT_BASE_URL);
            String clientId = getOptionValue(line, clientIdOption.getOpt(), KeyVaultSecretStorageFactory.SYSTEM_VAR_CLIENT_ID);
            String clientSecret = getOptionValue(line, clientSecretOption.getOpt(), KeyVaultSecretStorageFactory.SYSTEM_VAR_CLIENT_SECRET);
            String encryptionKeyId = getOptionValue(line, encryptionKeyIdOption.getOpt(), KeyVaultSecretStorageFactory.SYSTEM_VAR_ENCRYPTION_KEY_ID);

            String[] commands = line.getArgs();

            if (commands.length != 1) {
                LOGGER.error("Exactly one command should be used");
                LOGGER.error("Found command set: {}", Arrays.toString(commands));
                printHelp(options);
                System.exit(1);
            }

            Command command = Command.valueOf(commands[0]);
            if (command.isNeedsValues() && !line.hasOption(valuesOption.getOpt())) {
                LOGGER.error("Command {} needs '--{}' option", command.name(), valuesOption.getOpt());
                System.exit(1);
            }

            String[] values = line.getOptionValues(valuesOption.getOpt());

            Map<String, String> stringStringMap = collectAsMap(values);

            //---------------------------------------------------------------------------------------------------------------------
            // create SecretStorage
            //---------------------------------------------------------------------------------------------------------------------
            Properties properties = new Properties();
            properties.setProperty(KeyVaultSecretStorage.VAULT_BASE_URL, vaultBaseUrl);
            properties.setProperty(KeyVaultSecretStorage.CLIENT_ID, clientId);
            properties.setProperty(KeyVaultSecretStorage.CLIENT_SECRET, clientSecret);
            properties.setProperty(KeyVaultSecretStorage.ENCRYPTION_KEY_ID, encryptionKeyId);

            ISecretStorageFactory factory = new KeyVaultSecretStorageFactory();
            ISecretStorage secretStorage = factory.createSecretStorage(properties);

            boolean result = performCommand(secretStorage, command, stringStringMap);
            exitCode = result ? 0 : 1;

        } catch (Exception e) {
            LOGGER.error("Error: {}", e.getMessage());
            exitCode = 1;
        }
        System.exit(exitCode);
    }

    private static boolean performCommand(ISecretStorage secretStorage, Command command, Map<String, String> secrets) {
        LOGGER.info("Perform " + command);
        switch (command) {
            case GET:
                if (!checkSecretNames(secrets)) {
                    return false;
                }
//                secrets.keySet().stream()
//                        .map(key -> Secret.selectByName(key))
//                        .forEach(key -> LOGGER.info(key.buildSecretFullName() + " = " + secretStorage.retrieveSecret(key)));
                break;

            case SET:
                if (!checkSecretNames(secrets)) {
                    return false;
                }
                secrets.entrySet().stream()
                        .collect(LinkedHashMap::new,
                                (map, entry) -> map.put(Secret.selectByName(entry.getKey()), entry.getValue()),
                                Map<ISecretIdentity, String>::putAll)
                        .entrySet().forEach(entry -> {
//                            LOGGER.info("Setting secret value: " + entry.getKey().buildSecretFullName() + " = " + entry.getValue());
                            secretStorage.storeSecret(entry.getKey(), entry.getValue());
                            LOGGER.info("... done");
                        });
                break;

            case LIST:
                Set<ISecretIdentity> secretSet = secretStorage.collectPossibleIdentities();
                secretSet.forEach(secret -> LOGGER.info(secret.buildSecretFullName()));
                break;

            default:
                throw new EnumConstantNotPresentException(Command.class, command.name());
        }
        return true;
    }

    private static boolean checkSecretNames(Map<String, String> secrets) {
        boolean res = true;
        for (String name : secrets.keySet()) {
            if (Secret.selectByName(name) == null) {
//                LOGGER.error("Secret name '" + name + "' is incorrect.");
                LOGGER.error("Secret name is incorrect.");
                res = false;
            }
        }
        return res;
    }

    private static Map<String, String> collectAsMap(String[] values) {
        if (values == null) {
            return Collections.emptyMap();
        }

        return Arrays.stream(values)
                .map(v -> v.split(":"))
                .filter(a -> a.length >= 1)
                .collect(LinkedHashMap::new,
                        (map, item) -> map.put(item[0], item.length >= 2 ? item[1] : null),
                        Map::putAll);
    }

    private static String getOptionValue(CommandLine line, String optionName, String systemVarName) {
        String value = System.getenv(systemVarName);
        if (line.hasOption(optionName)) {
            value = line.getOptionValue(optionName);
        }
        return value;
    }

    private static Option addOption(Options options, boolean required, String name, String parameter, String description) {
        Option option = new Option(name, name, parameter != null, description);
        if (parameter != null) {
            option.setArgName(parameter);
        }
        option.setRequired(required);
        options.addOption(option);
        return option;
    }

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(
                "java -jar secure-storage-dal-{version}.jar " + "command",
                "Options", options,
                "command:\n"
                        + Command.createFullDescription()
                        + "\nExit code:\n"
                        + "   0 : command performed successfully\n"
                        + "   1 : command performed with errors\n",
                true);
    }
}
