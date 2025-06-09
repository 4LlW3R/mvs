package com.epam.tcodata.mock.main;

import com.epam.tcodata.mdm.Decision;
import com.epam.tcodata.mdm.IKeyFactory;
import com.epam.tcodata.mdm.IKeyManager;
import com.epam.tcodata.mdm.SearchingResult;
import com.epam.tcodata.mock.mdm.base.impl.MockKeyFactory;
import com.epam.tcodata.mock.secure.storage.dal.factory.impl.MockSecretStorageFactory;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.domain.mdm.ApiVersion;
import com.epam.tcodata.sql.dal.domain.mdm.KeyManagerVersion;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MockKeyManagerStarter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockKeyManagerStarter.class);

    /**
     * Main entry point for MockKeyManagerStarter.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        int exitCode = 0;
        try {
            Option inputOption = new Option("i", "input", true, "input path name.");
            inputOption.setArgName("file");
            inputOption.setRequired(true);

            Option outputOption = new Option("o", "output", true, "output path name.");
            outputOption.setArgName("file");
            outputOption.setRequired(true);

            Option backupOption = new Option("b", "backup", true, "base backup directory name.");
            backupOption.setArgName("dir");
            backupOption.setRequired(true);

            Option verbatimOption = new Option("v", "verbatim", false, "report about all performed tests.");
            verbatimOption.setRequired(false);

            Option versionOption = new Option("ver", "version", true, "version of KeyManager. "
                    + "Possibly values are " + Arrays.toString(KeyManagerVersion.values()));
            versionOption.setArgName("ver");
            versionOption.setRequired(true);

            Option helpOption = new Option("?", "help", false, "Print this message.");

            Options options = new Options();
            options.addOption(verbatimOption);
            options.addOption(inputOption);
            options.addOption(outputOption);
            options.addOption(backupOption);
            options.addOption(versionOption);
            options.addOption(helpOption);
            CommandLineParser parser = new DefaultParser();

            //---------------------------------------------------------------------------------------------------------------------
            // parsing main options that can lead to exit immediately
            //---------------------------------------------------------------------------------------------------------------------
            CommandLine line = parseLine(parser, options, args);

            if (line.hasOption(helpOption.getOpt())) {
                printHelp(options);
                System.exit(0);
            }


            //---------------------------------------------------------------------------------------------------------------------
            // the main work is here
            //---------------------------------------------------------------------------------------------------------------------
            String inputStr = line.getOptionValue(inputOption.getOpt());
            String outputStr = line.getOptionValue(outputOption.getOpt());
            String backupStr = line.getOptionValue(backupOption.getOpt());
            String versionStr = line.getOptionValue(versionOption.getOpt());

            boolean verbatim = line.hasOption(verbatimOption.getOpt());

            KeyManagerVersion version = KeyManagerVersion.valueOf(versionStr);

            exitCode = proceedAllWork(inputStr, outputStr, backupStr, version, verbatim);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            LOGGER.error("Error: {}", e.getMessage());
            exitCode = 2;
        }
        System.exit(exitCode);
    }

    private  static CommandLine parseLine(CommandLineParser parser, Options options, String[] args) {
        CommandLine line = null;
        try {
            // parse the command line arguments
            line = parser.parse(options, args);
        } catch (ParseException exp) {
            // oops, something went wrong
            LOGGER.error("Incorrect parameters: {}", exp.getMessage());

            printHelp(options);

            System.exit(2);
            return line;
        }
        return line;
    }

    private static int proceedAllWork(String inputStr, String outputStr, String backupPath, KeyManagerVersion version,
                                      boolean verbatim) throws Exception {

        int result = 0;
        Counter counter = new Counter();
        Map<String, String> variables = new HashMap<>();

        ISecretStorage secretStorage = new MockSecretStorageFactory(new HashMap<>()).createSecretStorage(new Properties());
        IKeyFactory keyFactory = MockKeyFactory.instance();

        Charset utf8 = StandardCharsets.UTF_8;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputStr), utf8));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputStr), utf8));
                IKeyManager keyManager = keyFactory.createKeyManager(version, secretStorage)) {

            String line = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                counter.increaseLines();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                if (!performOneLine(line, keyManager, variables, writer, counter, verbatim, backupPath)) {
                    result = 1;
                }
            }

            writer.write("=============================================================");
            writer.newLine();
            writer.write("Lines readed: " + counter.getLines());
            writer.newLine();
            writer.write("Tests passed: " + counter.getPassed());
            writer.newLine();
            writer.write("Tests failed: " + counter.getFailed());
            writer.newLine();
        }


        return result;
    }

    private static boolean performOneLine(String line, IKeyManager keyManager, Map<String, String> variables,
                                          BufferedWriter writer, Counter counter, boolean verbatim, String backupPath) throws Exception {

        try {
            line = replaceVars(line, variables);

            String[] parts = line.split(";");
            // The format of these lines as follows:
            // Command;Expected;Relation;Entity;ApiVersion;Subscription;EntityType
            // Commands: V[ariable], B[ackup], R[estore], S[ubscriptions], F[indOrCreate], K[eysSubstitution]
            // Expected: a value that should be compared with actual result, for B and R command just contains additional
            // path where to store/restore a backup of mdm database.
            // Relation: == != < >
            // Entity: json representation of entity
            // ApiVersion: API_1_0, API_2_0
            // Subscription: as is
            // EntityType: POSITION, EVENT, TRIP, SUBTRIP, DRIVER, ASSET, LIBRARY_EVENT, LOCATION

            String command = part(parts, 0, true, null);
            String expected = part(parts, 1, true, null);
            String relation = part(parts, 2, false, "==");
            String entityJson = part(parts, 3, false, "{}");
            String apiVersionStr = part(parts, 4, false, ApiVersion.API_1_0.name());
            String subscription = part(parts, 5, false, "0");
            String entityTypeStr = part(parts, 6, false, "");

            boolean result;
            switch (command) {
                case "V":
                    int index = expected.indexOf('=');
                    if (index < 0) {
                        variables.put(expected, null);
                    } else {
                        String name = expected.substring(0, index);
                        String value = expected.substring(index + 1);
                        variables.put(name, value);
                    }
                    return true;

                case "B":
                    File[] files = Paths.get(backupPath, expected).toFile().listFiles();
                    if (files != null) {
                        Arrays.stream(files).forEach(File::delete);
                    }
                    keyManager.getDaoFactory().backup(Paths.get(backupPath, expected));
                    return true;

                case "R":
                    keyManager.getDaoFactory().restore(Paths.get(backupPath, expected));
                    keyManager.refresh();
                    return true;

                case "S":
                    Set<String> subscriptions = keyManager.subscriptions();
                    Set<String> expectedSet = Arrays.stream(expected.split(",")).collect(Collectors.toSet());
                    result = check(counter.getLines(), line, relation, subscriptions, expectedSet, writer, verbatim);
                    counter.increaseTests(result);
                    return result;

                case "F": {
                    EntityType entityType = EntityType.valueOf(entityTypeStr);
                    Object entity = jsonToEntity(entityJson, entityType.getEntityClass());
                    ApiVersion apiVersion = ApiVersion.valueOf(apiVersionStr);
                    Decision decision = keyManager.findOrCreate(entity, apiVersion, subscription, entityType);
                    Map<String, String> actualMap = new HashMap<>();
                    if (decision != null) {
                        actualMap.put("uuid", decision.getSurrogateKey().toString());
                        actualMap.put("reason", decision.getAssigningReason().toString());
                    }
                    Map<String, String> expectedMap = jsonToEntity(expected, HashMap.class);
                    alignMaps(actualMap, expectedMap);
                    result = check(counter.getLines(), line, relation, actualMap.entrySet(), expectedMap.entrySet(), writer, verbatim);
                    counter.increaseTests(result);
                    return result;
                }

                case "K": {
                    EntityType entityType = EntityType.valueOf(entityTypeStr);
                    Object entity = jsonToEntity(entityJson, entityType.getEntityClass());
                    ApiVersion apiVersion = ApiVersion.valueOf(apiVersionStr);
                    Map<EntityType, List<SearchingResult>> map = keyManager.keysSubstitution(entity, apiVersion, subscription, entityType);
                    Map<String, String> actualMap = map
                            .values()
                            .stream()
                            .flatMap(Collection::stream)
                            .collect(Collectors.toMap(c -> c.getName(), c -> String.valueOf(c.getUuid())));
                    Map<String, String> expectedMap = jsonToEntity(expected, HashMap.class);
                    alignMaps(actualMap, expectedMap);
                    result = check(counter.getLines(), line, relation, actualMap.entrySet(), expectedMap.entrySet(), writer, verbatim);
                    counter.increaseTests(result);
                    return result;
                }

                default:
                    throw new IllegalArgumentException("Unknown command " + command + " in line: " + line);
            }


        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            writer.write("ERROR: " + counter.getLines() + " " + line);
            writer.write(e.getMessage());
            writer.newLine();

            return false;
        }
    }

    private static void alignMaps(Map<String, String> actualMap, Map<String, String> expectedMap) {
        Set<String> actualKeys = actualMap.keySet();
        Set<String> expectedKeys = expectedMap.keySet();

        actualKeys.retainAll(expectedKeys);

        Map<String, String> actual = actualMap.entrySet()
                .stream()
                .filter(e -> actualKeys.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getKey));

        Map<String, String> expected = expectedMap.entrySet()
                .stream()
                .filter(e -> actualKeys.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getKey));
        actualMap.clear();
        actualMap.putAll(actual);
        expectedMap.clear();
        expectedMap.putAll(expected);
    }

    private static String replaceVars(String value, Map<String, String> variables) {
        String regex = "\\$\\{(.*?)\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        StringBuffer sb = new StringBuffer(value.length());
        while (matcher.find()) {
            String text = matcher.group(1);
            String replace = variables.get(text);
            matcher.appendReplacement(sb, replace);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static <T> T jsonToEntity(String json, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        return mapper.readValue(json, clazz);
    }

    private static boolean check(long lineNumber, String sourceLine, String relation, Collection<?> actual, Collection<?> expected, BufferedWriter writer, boolean verbatim) throws IllegalArgumentException, IOException {
        boolean res = false;
        switch (relation) {
            case "==":
                res = expected.equals(actual);
                break;

            case "!=":
                res = !expected.equals(actual);
                break;

            case "<=":
                res = actual.containsAll(expected);
                break;

            case ">=":
                res = expected.containsAll(actual);
                break;

            default:
                throw new IllegalArgumentException("Unknown relation " + relation);
        }
        if (verbatim || !res) {
            writer.write("--- " + (res ? "PASSED" : "FAILED") + " ------------------------------------------------");
            writer.newLine();
            writer.write(lineNumber + " " + sourceLine);
            writer.newLine();
            writer.write("Expected: " + expected);
            writer.newLine();
            writer.write("Relation: " + relation);
            writer.newLine();
            writer.write("Actual:   " + actual);
            writer.newLine();
        }

        return res;
    }

    private static String part(String[] parts, int index, boolean mandatory, String defaultValue) throws MissingArgumentException {
        if (parts.length > index) {
            return parts[index];
        }
        if (!mandatory) {
            return defaultValue;
        }

        throw new MissingArgumentException("Mandatory field with index " + index + " is absent.");
    }

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(
                "java -jar " + MockKeyManagerStarter.class.getSimpleName() + ".jar",
                "Options", options,
                "Exit code:\n"
                        + "   0 : all testcases have passed\n"
                        + "   1 : one or more testcases have failed\n"
                        + "   2 : other errors\n",
                true);
    }


    static class Counter {
        private int passed = 0;
        private int failed = 0;
        private int lines = 0;

        public Counter() {
            /***  Default implementation ***/
        }

        public void increaseLines() {
            this.lines++;
        }

        public void increaseTests(boolean result) {
            if (result) {
                this.passed++;
            } else {
                this.failed++;
            }
        }

        public int getPassed() {
            return this.passed;
        }

        public int getFailed() {
            return this.failed;
        }

        public int getLines() {
            return this.lines;
        }
    }
}

