package com.epam.tcodata.sql.dal.impl.speedlayer;

import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.AbstractDaoFactory;
import com.epam.tcodata.sql.dal.domain.speedlayer.SpeedLayerEvent;
import com.epam.tcodata.sql.dal.domain.speedlayer.SpeedLayerPosition;
import com.epam.tcodata.sql.dal.service.impl.speedlayer.SpeedLayerEventService;
import com.epam.tcodata.sql.dal.service.impl.speedlayer.SpeedLayerPositionService;

public class SpeedLayerDaoFactory extends AbstractDaoFactory {

    /**
     * Main pubic constructor.
     */
    public SpeedLayerDaoFactory(ISecretStorage secretStorage) {
        super(secretStorage);

        register(SpeedLayerPosition.class, new SpeedLayerPositionService(this));
        register(SpeedLayerEvent.class, new SpeedLayerEventService(this));
    }
}
