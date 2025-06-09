package com.epam.tcodata.sql.dal.service.impl.speedlayer;

import com.epam.tcodata.sql.dal.DatabaseConfig;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.IStorable;
import com.epam.tcodata.sql.dal.domain.speedlayer.SpeedLayerPosition;
import com.epam.tcodata.sql.dal.service.AbstractReadWriteService;
import com.epam.tcodata.sql.dal.service.CRUD;
import com.epam.tcodata.sql.dal.service.speedlayer.ISpeedLayerPositionService;
import com.epam.tcodata.sql.dal.service.speedlayer.ISpeedLayerService;

import java.util.List;

public class SpeedLayerPositionService extends AbstractReadWriteService<SpeedLayerPosition>
        implements ISpeedLayerService<SpeedLayerPosition> {

    /**
     * Public main constructor. Queries can be customized via crud builder.
     */
    public SpeedLayerPositionService(IDaoFactory daoFactory) {
        super(daoFactory,
                DatabaseConfig.SPEEDLAYER, "SpeedLayerPosition",
                new CRUD(),
                SpeedLayerPosition.class);
    }

    public int[] insertBatch(List<IStorable> positions) {
        ISpeedLayerPositionService positionService = openHandle().attach(ISpeedLayerPositionService.class);
        return positionService.insertBatch(positions);
    }
}
