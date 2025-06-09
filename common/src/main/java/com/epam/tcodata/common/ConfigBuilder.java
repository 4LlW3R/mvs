package com.epam.tcodata.common;


import org.apache.commons.lang3.Validate;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * Created by Alexander_Kochurin on 9/19/2017.
 *
 * <p>
 *     Universal tool for creating configuration.
 * </p>
 */
public class ConfigBuilder {

    /**
     * Reading modes for parameters.
     */
    public enum ReadModes {
        AS_RESOURCE,
        AS_INPUT_STREAM,
        UNKNOWN
    }


    private final Map<String, Boolean> keys = new HashMap<>();
    private String resourceFileName;
    private InputStream inputStream;
    private ReadModes readMode = ReadModes.UNKNOWN;


    /**
     * Creating config builder.
     */
    public ConfigBuilder() {
        /***  Default implementation ***/
    }


    /**
     * Add parameter for configuration.
     * @param key parameter name;
     * @param mandatory <code>true</code> - if null check is needed, <code>false</code> - in other way.
     * @return current configuration builder.
     */
    public ConfigBuilder addParameter(String key, boolean mandatory) {
        this.keys.put(key, mandatory);
        return this;
    }

    /**
     * Set resource parameters file name.
     * <p>
     *     Also it set read mode from resources.
     * </p>
     * @param fileName parameters file name into resources.
     * @return current configuration builder.
     */
    public ConfigBuilder setResourceParametersFileName(String fileName) {
        this.resourceFileName = fileName;
        this.readMode = ReadModes.AS_RESOURCE;
        return this;
    }

    /**
     * Set input stream for reading parameters.
     * <p>
     *     Also it set read mode from stream.
     * </p>
     * @param inputStream for reading parameters.
     * @return current configuration builder.
     */
    public ConfigBuilder setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        this.readMode = ReadModes.AS_INPUT_STREAM;
        return this;
    }

    /**
     * Build configuration.
     * <p>
     *     Loading parameters, checking parameters and providing it as configuration.
     * </p>
     * @return configuration.
     */
    public Map<String, String> build() {
        Properties properties = loadPropertiesByCurrentMode();
        return buildParametersMap(properties);
    }

    private Properties loadPropertiesByCurrentMode() {
        final Properties properties;

        switch (readMode) {
            case AS_INPUT_STREAM:
                properties = readProperties(this.inputStream);
                break;
            case AS_RESOURCE:
                properties = readProperties(this.resourceFileName);
                break;
            case UNKNOWN:
            default:
                throw new IllegalArgumentException("Unknown read mode: " + readMode);
        }

        return properties;
    }

    private static Properties readProperties(String resourceFileName) {
        Validate.notNull(resourceFileName, "Resource file name");
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceFileName)) {
            return readProperties(inputStream);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    private static Properties readProperties(InputStream inputStream) {
        Validate.notNull(inputStream, "inputStream");
        Properties props = new Properties();
        try {
            props.load(inputStream);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
        return props;
    }

    private Map<String, String> buildParametersMap(Properties properties) {
        Map<String, String> res = new HashMap<>();

        this.keys.entrySet().forEach(bKey -> {
            String key = bKey.getKey();
            String value = properties.getProperty(key);
            if (Boolean.TRUE.equals(bKey.getValue())) {
                Validate.notNull(value, key);
            }
            res.put(key, value);
        });

        return res;
    }

}
