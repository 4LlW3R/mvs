package com.epam.tcodata.internal.pump.handler.fact;

import com.epam.tcodata.internal.pump.factory.IInternalFactory;
import com.epam.tcodata.internal.pump.handler.AbstractDataHandler;
import org.apache.spark.sql.SparkSession;

public class PositionDataHandler extends AbstractDataHandler {

    private static final long serialVersionUID = 4097012523931984980L;

    public PositionDataHandler(IInternalFactory entityFactory, SparkSession sparkSession) {
        super(entityFactory, sparkSession);
    }
}
