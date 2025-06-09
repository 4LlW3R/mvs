package com.epam.tcodata.sql.dal.service.speedlayer;

import com.epam.tcodata.sql.dal.IStorable;
import com.epam.tcodata.sql.dal.service.IReadWriteService;

import java.util.List;

public interface ISpeedLayerService<T> extends IReadWriteService<T> {

    int[] insertBatch(List<IStorable> entities);
}
