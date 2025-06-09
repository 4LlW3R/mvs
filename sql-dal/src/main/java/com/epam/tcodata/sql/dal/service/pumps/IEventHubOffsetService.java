package com.epam.tcodata.sql.dal.service.pumps;

import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.sql.dal.domain.pumps.EventHubOffset;
import com.epam.tcodata.sql.dal.service.IReadWriteService;

import java.util.List;

public interface IEventHubOffsetService extends IReadWriteService<EventHubOffset> {

    void updateEventHubOffsets(List<EventHubOffset> eventHubOffsets);

    void deleteEventHubOffsets(EntityType entityType);

}
