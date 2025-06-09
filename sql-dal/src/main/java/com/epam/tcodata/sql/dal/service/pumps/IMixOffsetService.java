package com.epam.tcodata.sql.dal.service.pumps;

import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.sql.dal.IStorable;
import com.epam.tcodata.sql.dal.service.IReadWriteService;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface IMixOffsetService<T extends IStorable> extends IReadWriteService<T> {

    Map<Long, Optional<T>> readMixOffsetMap(Set<Long> orgGroupIdList, EntityType typeCode);
}
