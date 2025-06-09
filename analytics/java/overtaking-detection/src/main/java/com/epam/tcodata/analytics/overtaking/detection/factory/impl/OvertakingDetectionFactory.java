package com.epam.tcodata.analytics.overtaking.detection.factory.impl;

import com.epam.tcodata.analytics.overtaking.detection.factory.IOvertakingDetectionFactory;
import com.epam.tcodata.eventhub.dal.EventHubInfo;
import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.eventhub.dal.impl.EventHub;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.factory.ISecretStorageFactory;

import java.util.Properties;

public class OvertakingDetectionFactory implements IOvertakingDetectionFactory {

    private static final long serialVersionUID = 9003312294194068098L;

    private ISecretStorageFactory secretStorageFactory = null;

    @Override
    public ISecretStorage createSecretStorage() {
        if (this.secretStorageFactory == null) {
            this.secretStorageFactory = createSecretStorageFactory();
        }
        return secretStorageFactory.createSecretStorage(getSecretStorageProperties());
    }

    @Override
    public IEventHub createPositionEventHub(ISecretStorage secretStorage) {
        return new EventHub(EventHubInfo.POSITION, secretStorage);
    }

    @Override
    public IEventHub createOvertakingEventHub(ISecretStorage secretStorage) {
        return new EventHub(EventHubInfo.OVERTAKING, secretStorage);
    }

    protected ISecretStorageFactory createSecretStorageFactory() {
        return ISecretStorageFactory.createDefaultFactory();
    }

    protected Properties getSecretStorageProperties() {
        return new Properties();
    }
}
