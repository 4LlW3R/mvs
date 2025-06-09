package com.epam.tcodata.redis.dal;

import com.epam.tcodata.secure.storage.dal.ISecretStorage;

public abstract class AbstractRedis implements IRedis {

    private RedisConfig redisConfig;

    public AbstractRedis(RedisConfig redisConfig, ISecretStorage secretStorage) {
        this.redisConfig = redisConfig;
        secretStorage.getClass();
    }

    public RedisConfig getRedisConfig() {
        return redisConfig;
    }


}
