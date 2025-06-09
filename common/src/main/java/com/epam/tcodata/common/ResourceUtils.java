package com.epam.tcodata.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A utility class with bunch of useful methods.
 */
public final class ResourceUtils {
    private ResourceUtils() {
    }

    /**
     * Returns a value of property with given name from Property object.
     *
     * @param prefix - name prefix to separate subsystems.
     * @param properties - property object.
     * @param parameter - parameter name.
     * @return a value.
     */
    public static String getProperty(String prefix, Properties properties, String parameter) {
        String name = prefix == null ? parameter : prefix + "." + parameter;
        return properties.getProperty(name, "");
    }

    /**
     * Returns a set of values of this property object.
     * Only properties that start from given prefix are collected.
     *
     * @param prefix start prefix.
     * @param properties property object
     * @return a set.
     */
    public static Set<String> getProperties(String prefix, Properties properties) {
        Set<String> names = properties.stringPropertyNames();
        return names.stream()
                .filter(n -> n.startsWith(prefix))
                .map(properties::getProperty)
                .collect(Collectors.toSet());
    }

    /**
     * Read properties from the given resource name.
     * Returns empty property if the resource name == null
     *
     * @param resourceFileName - resource name.
     * @return property object.
     */
    public static Properties readProperties(String resourceFileName) {
        Properties props = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (resourceFileName != null) {
            if (classLoader.getResource(resourceFileName) != null) {
                try (InputStream inputStream = classLoader.getResourceAsStream(resourceFileName)) {
                    props.load(inputStream);
                } catch (IOException e) {
                    throw new IllegalArgumentException(e.getMessage(), e);
                }
            } else {
                try (InputStream inputStream = new FileInputStream(resourceFileName)) {
                    props.load(inputStream);
                } catch (IOException e) {
                    throw new IllegalArgumentException(e.getMessage(), e);
                }
            }
        }
        return props;
    }


    /**
     * Reads the whole stream as set of lines and combines their into one string.
     *
     * @param is input stream.
     * @return combined string.
     * @throws IOException
     */
    public static String streamAsString(InputStream is) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.defaultCharset()))) {
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }

            return sb.toString();
        }
    }

    /**
     * Generic function of load data of given class from the stream.
     *
     * @param entityClass class object of class entity.
     * @param is input stream.
     * @param <T> entity class.
     * @return list of entities.
     * @throws IOException
     */
    public static <T> List<T> loadData(Class<T> entityClass, InputStream is) throws IOException {
        String line = streamAsString(is);
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));
        JavaType type = mapper.getTypeFactory()
                .constructParametricType(
                        ArrayList.class,
                        entityClass);


        return mapper.readValue(line, type);
    }

    /**
     * Generic function of load data of given class from the file.
     *
     * @param entityClass class object of class entitiy.
     * @param fileName file name.
     * @param <T> entity class.
     * @return list of entities.
     * @throws IOException
     */
    public static <T> List<T> loadData(Class<T> entityClass, String fileName) throws IOException {
        try (InputStream inputStream = new FileInputStream(fileName)) {
            return loadData(entityClass, inputStream);
        }
    }

    /**
     * Generic function of saving data of given class to the file.
     *
     * @param entityClass class object of class entity.
     * @param data list of entities.
     * @param fileName file name.
     * @param <T> entity class.
     * @throws IOException
     */
    public static <T> void saveData(Class<T> entityClass, List<T> data, String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        JavaType type = mapper.getTypeFactory()
                .constructParametricType(
                        ArrayList.class,
                        entityClass);
        mapper.constructType(type);
        mapper.writeValue(new File(fileName), data);
    }

    /**
     * Generic function of load single object of given class from the stream.
     *
     * @param entityClass class object of class entity.
     * @param is input stream.
     * @param <T> entity class.
     * @return list of entities.
     * @throws IOException
     */
    public static <T> T loadObject(Class<T> entityClass, InputStream is) throws IOException {
        String line = streamAsString(is);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mapper.setVisibility(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));

        JavaType type = mapper.getTypeFactory()
                .constructType(entityClass);

        return mapper.readValue(line, type);
    }

    /**
     * Generic function of load object of given class from the file.
     *
     * @param entityClass class object of class entitiy.
     * @param fileName file name.
     * @param <T> entity class.
     * @return list of entities.
     * @throws IOException
     */
    public static <T> T loadObject(Class<T> entityClass, String fileName) throws IOException {
        try (InputStream inputStream = new FileInputStream(fileName)) {
            return loadObject(entityClass, inputStream);
        }
    }
}
