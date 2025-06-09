package com.epam.tcodata.redis.dal;

import java.util.List;
import java.util.Map;

public interface IRedis {

    /**
     * Getter for entities  placed into Redis.
     *
     * @param key String key of the entity
     * @return String value of the key
     */
    String get(String key);

    /**
     * Method setting key-value pair in Redis DB.
     *
     * @param key   String key
     * @param value String value
     * @return
     */
    String set(String key, String value);

    /**
     * Method setting K-V values into Redis Hash.
     *
     * @param key   Key of Map
     * @param value K-V pairs of properties
     * @return Return OK or Exception if hash is empty
     */
    String set(String key, Map<String, String> value);

    /**
     * Method for deleting key-value pair.
     *
     * @param key String key
     * @return
     */
    boolean drop(String key);

    /**
     * Method deleting all keys in Redis DB.
     */
    void dropAll();

    /**
     * Getting list values for corresponding key.
     *
     * @param key String key
     * @return list of values
     */
    List<String> getList(String key);

    /**
     * Getting list values for corresponding key.
     *
     * @param key   String representation of key
     * @param start Start index of element
     * @param end   End index of element
     * @return list of values
     */
    List<String> getList(String key, long start, long end);


    /**
     * Getting MapLike value from Hash.
     *
     * @param key Key of Map in Redis.
     * @return Map object with properties.
     */
    Map<String, String> getMap(String key);
}
