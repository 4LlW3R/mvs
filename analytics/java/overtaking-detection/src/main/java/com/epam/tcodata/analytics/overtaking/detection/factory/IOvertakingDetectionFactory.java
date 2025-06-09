package com.epam.tcodata.analytics.overtaking.detection.factory;

import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;

import java.io.Serializable;

public interface IOvertakingDetectionFactory extends Serializable {

    ISecretStorage createSecretStorage();

    IEventHub createPositionEventHub(ISecretStorage secretStorage);

    IEventHub createOvertakingEventHub(ISecretStorage secretStorage);
}
