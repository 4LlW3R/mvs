package com.epam.tcodata.analytics.overtaking.violation.detection.factory;

import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.redis.dal.IRedis;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;

import java.io.Serializable;

public interface IOvertakingViolationFactory extends Serializable {

    ISecretStorage createSecretStorage();

    IEventHub createOvertakingEventHub(ISecretStorage secretStorage);

    IEventHub createOvertakingViolationEventHub(ISecretStorage secretStorage);

    IRedis createAreaRedis(ISecretStorage secretStorage);

    IRedis createVehicleRedis(ISecretStorage secretStorage);
}

