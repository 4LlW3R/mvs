package com.epam.tcodata.external.pump.service;

import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.sql.dal.IStorable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IOffsetService {

    Map<Long, IStorable> getOrCreateOffsets(Set<Long> orgGroupIdSet);

    void updateOffsets(List<AbstractDto> dtoList);
}
