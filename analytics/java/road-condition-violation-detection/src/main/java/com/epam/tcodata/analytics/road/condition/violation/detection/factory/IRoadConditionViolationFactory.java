package com.epam.tcodata.analytics.road.condition.violation.detection.factory;

import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.redis.dal.IRedis;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;

import java.io.Serializable;
import java.rmi.NoSuchObjectException;

public interface IRoadConditionViolationFactory extends Serializable {

    ISecretStorage createSecretStorage() throws NoSuchObjectException;

    IEventHub createPositionEventHub(ISecretStorage secretStorage);

    IEventHub createRoadConditionEventHub(ISecretStorage secretStorage);

    IRedis createAreaRedis(ISecretStorage secretStorage);
}

