package com.epam.tcodata.redis.dal.impl;

import com.epam.tcodata.redis.dal.AbstractRedis;
import com.epam.tcodata.redis.dal.RedisConfig;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.Secret;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

import java.util.List;
import java.util.Map;

/**
 * Wrapper for Redis client {@link Jedis}.
 */
public class Redis extends AbstractRedis {

    private final Jedis jedisClient;
    private static final int DEF_TIMEOUT = 30000;

    /**
     * Fully parametrized constructor.
     *
     * @param redisConfig redis config instance that points to the needed Redis database.
     * @param secretStorage secret storage to get needed sensitive data.
     */
    public Redis(RedisConfig redisConfig, ISecretStorage secretStorage) {
        super(redisConfig, secretStorage);

        String accessKey = secretStorage.retrieveSecret(Secret.Redis.COMMON.accessKey);

        JedisShardInfo shard = new JedisShardInfo(RedisConfig.getHost(), RedisConfig.getPort(), DEF_TIMEOUT, RedisConfig.isUseSsl());
        shard.setPassword(accessKey);
        this.jedisClient = new Jedis(shard);
        this.jedisClient.select(redisConfig.getIndex());
    }

    @Override
    public String get(String key) {
        return this.jedisClient.get(key);
    }

    @Override
    public String set(String key, String value) {
        return this.jedisClient.set(key, value);
    }

    @Override
    public String set(String key, Map<String, String> value) {
        return this.jedisClient.hmset(key, value);
    }

    @Override
    public boolean drop(String key) {
        return this.jedisClient.del(key) > 0;
    }

    @Override
    public void dropAll() {
        this.jedisClient.keys("*").forEach(this::drop);
    }

    @Override
    public List<String> getList(String key) {
        return getList(key, 0, Long.MAX_VALUE);
    }

    @Override
    public List<String> getList(String key, long start, long end) {
        return this.jedisClient.lrange(key, start, end);
    }

    @Override
    public Map<String, String> getMap(String key) {
        return this.jedisClient.hgetAll(key);
    }
}

