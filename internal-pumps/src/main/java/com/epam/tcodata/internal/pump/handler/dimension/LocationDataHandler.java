package com.epam.tcodata.internal.pump.handler.dimension;

import com.epam.tcodata.internal.pump.factory.IInternalFactory;
import com.epam.tcodata.internal.pump.handler.AbstractDataHandler;
import org.apache.spark.sql.SparkSession;

public class LocationDataHandler extends AbstractDataHandler {

    private static final long serialVersionUID = -7752647175185450464L;

    public LocationDataHandler(IInternalFactory entityFactory, SparkSession sparkSession) {
        super(entityFactory, sparkSession);
    }

}
