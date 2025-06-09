package com.epam.tcodata.analytics.road.condition.violation.detection.factory.impl;

import com.epam.tcodata.analytics.road.condition.violation.detection.factory.IRoadConditionViolationFactory;
import com.epam.tcodata.eventhub.dal.EventHubInfo;
import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.eventhub.dal.impl.EventHub;
import com.epam.tcodata.redis.dal.IRedis;
import com.epam.tcodata.redis.dal.RedisConfig;
import com.epam.tcodata.redis.dal.impl.Redis;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.factory.ISecretStorageFactory;

import java.rmi.NoSuchObjectException;
import java.util.Properties;

public class RoadConditionViolationFactory implements IRoadConditionViolationFactory {

    private static final long serialVersionUID = 2408835055017359093L;

    private ISecretStorageFactory secretStorageFactory = null;

    @Override
    public ISecretStorage createSecretStorage() throws NoSuchObjectException {
        if (this.secretStorageFactory == null) {
            this.secretStorageFactory = createSecretStorageFactory();
        }
        return secretStorageFactory.createSecretStorage(getScretStorageProperties());
    }

    @Override
    public IEventHub createPositionEventHub(ISecretStorage secretStorage) {
        return new EventHub(EventHubInfo.POSITION, secretStorage);
    }

    @Override
    public IEventHub createRoadConditionEventHub(ISecretStorage secretStorage) {
        return new EventHub(EventHubInfo.ROAD_CONDITION_VIOLATION, secretStorage);
    }

    @Override
    public IRedis createAreaRedis(ISecretStorage secretStorage) {
        return new Redis(RedisConfig.AREAS, secretStorage);
    }


    protected ISecretStorageFactory createSecretStorageFactory() {
        return ISecretStorageFactory.createDefaultFactory();
    }

    protected Properties getScretStorageProperties() {
        return new Properties();
    }

}
