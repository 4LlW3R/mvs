package com.epam.tcodata.analytics.overtaking.violation.detection.factory.impl;

import com.epam.tcodata.analytics.overtaking.violation.detection.factory.IOvertakingViolationFactory;
import com.epam.tcodata.eventhub.dal.EventHubInfo;
import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.eventhub.dal.impl.EventHub;
import com.epam.tcodata.redis.dal.IRedis;
import com.epam.tcodata.redis.dal.RedisConfig;
import com.epam.tcodata.redis.dal.impl.Redis;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.factory.ISecretStorageFactory;

import java.util.Properties;

public class OvertakingViolationFactory implements IOvertakingViolationFactory {

    private static final long serialVersionUID = 2408835055017359093L;

    private ISecretStorageFactory secretStorageFactory = null;

    @Override
    public ISecretStorage createSecretStorage() {
        if (this.secretStorageFactory == null) {
            this.secretStorageFactory = createSecretStorageFactory();
        }
        return secretStorageFactory.createSecretStorage(getScretStorageProperties());
    }

    @Override
    public IEventHub createOvertakingEventHub(ISecretStorage secretStorage) {
        return new EventHub(EventHubInfo.OVERTAKING, secretStorage);
    }

    @Override
    public IEventHub createOvertakingViolationEventHub(ISecretStorage secretStorage) {
        return new EventHub(EventHubInfo.OVERTAKING_VIOLATION, secretStorage);
    }

    @Override
    public IRedis createAreaRedis(ISecretStorage secretStorage) {
        return new Redis(RedisConfig.AREAS, secretStorage);
    }

    @Override
    public IRedis createVehicleRedis(ISecretStorage secretStorage) {
        return new Redis(RedisConfig.VEHICLE, secretStorage);
    }


    protected ISecretStorageFactory createSecretStorageFactory() {
        return ISecretStorageFactory.createDefaultFactory();
    }

    protected Properties getScretStorageProperties() {
        return new Properties();
    }

}
