package com.epam.tcodata.redis.dal;

import com.epam.tcodata.common.ResourceUtils;

import java.util.Properties;

public enum RedisConfig {
    DRIVER,
    VEHICLE,
    AREAS;

    private static final String PROPERTIES_FILE = "redis.properties";
    private static final String HOST_PROPERTY = "redis.host";
    private static final String PORT_PROPERTY = "redis.port";
    private static final String USE_SSL_PROPERTY = "redis.use.ssl";
    private static final String CONNECTIONS_PROPERTY = "redis.connections";
    private static final String INDEX_CAP = "index";

    private final int index;

    private static final String HOST;
    private static final int PORT;
    private static final boolean USE_SSL;
    private static final int CONNECTIONS;

    static {
        Properties properties = ResourceUtils.readProperties(PROPERTIES_FILE);
        HOST = ResourceUtils.getProperty(null, properties, HOST_PROPERTY);
        PORT = Integer.parseInt(ResourceUtils.getProperty(null, properties, PORT_PROPERTY));
        USE_SSL = Boolean.parseBoolean(ResourceUtils.getProperty(null, properties, USE_SSL_PROPERTY));
        CONNECTIONS = Integer.parseInt(ResourceUtils.getProperty(null, properties, CONNECTIONS_PROPERTY));
    }

    RedisConfig() {
        Properties properties = ResourceUtils.readProperties(PROPERTIES_FILE);
        this.index = Integer.parseInt(ResourceUtils.getProperty(INDEX_CAP, properties, name()));
    }

    public int getIndex() {
        return this.index;
    }

    public static String getHost() {
        return HOST;
    }

    public static int getPort() {
        return PORT;
    }

    public static boolean isUseSsl() {
        return USE_SSL;
    }

    public static int getConnections() {
        return CONNECTIONS;
    }
}
