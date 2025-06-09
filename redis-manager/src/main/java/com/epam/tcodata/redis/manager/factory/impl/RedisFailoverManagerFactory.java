package com.epam.tcodata.redis.manager.factory.impl;

import com.epam.tcodata.eventhub.dal.EventHubInfo;
import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.eventhub.dal.impl.EventHub;
import com.epam.tcodata.hive.dal.Hive;
import com.epam.tcodata.hive.dal.HiveConfig;
import com.epam.tcodata.hive.dal.IHive;
import com.epam.tcodata.redis.dal.IRedis;
import com.epam.tcodata.redis.dal.RedisConfig;
import com.epam.tcodata.redis.dal.impl.Redis;
import com.epam.tcodata.redis.manager.factory.IRedisFailoverManagerFactory;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.factory.ISecretStorageFactory;
import org.apache.spark.sql.SparkSession;

import java.util.Properties;

public class RedisFailoverManagerFactory implements IRedisFailoverManagerFactory {

    private static final long serialVersionUID = 3803539527849442715L;

    private ISecretStorageFactory defaultFactory = ISecretStorageFactory.createDefaultFactory();

    public RedisFailoverManagerFactory() {
        /***  Default implementation ***/
    }

    @Override
    public IHive createRawHive(SparkSession sparkSession) {
        return new Hive(HiveConfig.RAW, sparkSession);
    }

    @Override
    public ISecretStorage createSecretStorage() {
        Properties emptyProperty = new Properties();
        return defaultFactory.createSecretStorage(emptyProperty);
    }

    @Override
    public IEventHub createdDriverEventHub(ISecretStorage secretStorage) {
        return new EventHub(EventHubInfo.DRIVER, secretStorage);
    }

    @Override
    public IEventHub createdAssetEventHub(ISecretStorage secretStorage) {
        return new EventHub(EventHubInfo.ASSET, secretStorage);
    }

    @Override
    public IRedis createDriverRedis(ISecretStorage secretStorage) {
        return new Redis(RedisConfig.DRIVER, secretStorage);
    }

    @Override
    public IRedis createVehicleRedis(ISecretStorage secretStorage) {
        return new Redis(RedisConfig.VEHICLE, secretStorage);
    }
}
