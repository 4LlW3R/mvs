package com.epam.tcodata.internal.pump.handler.fact;

import com.epam.tcodata.internal.pump.factory.IInternalFactory;
import com.epam.tcodata.internal.pump.handler.AbstractDataHandler;
import org.apache.spark.sql.SparkSession;

public class TachoDataHandler extends AbstractDataHandler {

    private static final long serialVersionUID = -1385395160134004662L;

    public TachoDataHandler(IInternalFactory entityFactory, SparkSession sparkSession) {
        super(entityFactory, sparkSession);
    }
}
