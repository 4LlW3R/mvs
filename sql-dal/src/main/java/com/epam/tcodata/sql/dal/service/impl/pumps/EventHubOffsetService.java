package com.epam.tcodata.sql.dal.service.impl.pumps;

import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.sql.dal.DatabaseConfig;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.EventHubOffset;
import com.epam.tcodata.sql.dal.impl.pumps.PumpQueries;
import com.epam.tcodata.sql.dal.service.AbstractReadWriteService;
import com.epam.tcodata.sql.dal.service.CRUD;
import com.epam.tcodata.sql.dal.service.pumps.IEventHubOffsetService;
import org.jdbi.v3.core.statement.PreparedBatch;
import org.jdbi.v3.core.transaction.TransactionIsolationLevel;

import java.util.List;

public class EventHubOffsetService extends AbstractReadWriteService<EventHubOffset> implements IEventHubOffsetService {

    private static final String ENTITY_TYPE = "EntityType";

    /**
     * Public main constructor. Queries can be customized via crud builder.
     */
    public EventHubOffsetService(IDaoFactory daoFactory) {
        super(daoFactory,
                DatabaseConfig.PUMPS, "EventHubOffset",
                new CRUD(),
                EventHubOffset.class);
    }

    @Override
    public void updateEventHubOffsets(List<EventHubOffset> eventHubOffsets) {
        openHandle().useTransaction(TransactionIsolationLevel.SERIALIZABLE, handle -> {
            PreparedBatch preparedBatch =
                    handle.prepareBatch(PumpQueries.UPDATE_EVENT_HUB_OFFSETS_BY_ENTITY_TYPE_CODE_AND_PARTITION_ID.query());
            eventHubOffsets.forEach(bean -> preparedBatch.bindBean(bean).add());
            preparedBatch.execute();
        });
    }

    @Override
    public void deleteEventHubOffsets(EntityType entityType) {
        openHandle().createUpdate(PumpQueries.DELETE_EVENT_HUB_OFFSETS_BY_ENTITY_TYPE_CODE.query())
                .bind(ENTITY_TYPE, entityType.getCode())
                .execute();
    }

}
