package com.epam.tcodata.internal.pump.converter;

import com.epam.tcodata.models.enriched.IEnrichable;
import com.epam.tcodata.sql.dal.domain.speedlayer.ISpeedLayerEntity;

public interface ISpeedLayerConverter<T extends IEnrichable, S extends ISpeedLayerEntity> {

    S convertToSpeedLayer(T enriched);

}
