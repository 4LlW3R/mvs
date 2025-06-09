package com.epam.tcodata.internal.pump.handler.dimension;

import com.epam.tcodata.internal.pump.factory.IInternalFactory;
import com.epam.tcodata.internal.pump.handler.AbstractDataHandler;
import org.apache.spark.sql.SparkSession;

public class DriverDataHandler extends AbstractDataHandler {

    private static final long serialVersionUID = 882106127842085868L;

    public DriverDataHandler(IInternalFactory entityFactory, SparkSession sparkSession) {
        super(entityFactory, sparkSession);
    }

}
