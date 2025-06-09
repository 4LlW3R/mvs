package com.epam.tcodata.external.pump.dto.maker;

import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.factory.IExternalFactory;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.mix.Entity;
import org.apache.spark.api.java.JavaRDD;

import java.util.List;

public interface IDtoMaker<T extends Entity> {

    List<AbstractDto<T>> makeDtoList();

    JavaRDD<AbstractDto<T>> fillNonStaticInfo(JavaRDD<AbstractDto<T>> rdd,
                                              EntityType entityType,
                                              IExternalFactory factory);
}
