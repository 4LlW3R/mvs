package com.epam.tcodata.sql.dal.service.impl.speedlayer;

import com.epam.tcodata.sql.dal.DatabaseConfig;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.IStorable;
import com.epam.tcodata.sql.dal.domain.speedlayer.SpeedLayerEvent;
import com.epam.tcodata.sql.dal.service.AbstractReadWriteService;
import com.epam.tcodata.sql.dal.service.CRUD;
import com.epam.tcodata.sql.dal.service.speedlayer.ISpeedLayerEventService;
import com.epam.tcodata.sql.dal.service.speedlayer.ISpeedLayerService;

import java.util.List;

public class SpeedLayerEventService extends AbstractReadWriteService<SpeedLayerEvent>
        implements ISpeedLayerService<SpeedLayerEvent> {

    /**
     * Public main constructor. Queries can be customized via crud builder.
     */
    public SpeedLayerEventService(IDaoFactory daoFactory) {
        super(daoFactory,
                DatabaseConfig.SPEEDLAYER, "SpeedLayerEvent",
                new CRUD(),
                SpeedLayerEvent.class);
    }

    public int[] insertBatch(List<IStorable> events) {
        ISpeedLayerEventService eventService = openHandle().attach(ISpeedLayerEventService.class);
        return eventService.insertBatch(events);
    }
}
