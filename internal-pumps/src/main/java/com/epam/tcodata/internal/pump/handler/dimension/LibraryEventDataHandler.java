package com.epam.tcodata.internal.pump.handler.dimension;

import com.epam.tcodata.internal.pump.factory.IInternalFactory;
import com.epam.tcodata.internal.pump.handler.AbstractDataHandler;
import org.apache.spark.sql.SparkSession;

public class LibraryEventDataHandler extends AbstractDataHandler {

    private static final long serialVersionUID = -2996012542785932551L;

    public LibraryEventDataHandler(IInternalFactory entityFactory, SparkSession sparkSession) {
        super(entityFactory, sparkSession);
    }

}
