package com.epam.tcodata.internal.pump.handler.dimension;

import com.epam.tcodata.internal.pump.factory.IInternalFactory;
import com.epam.tcodata.internal.pump.handler.AbstractDataHandler;
import org.apache.spark.sql.SparkSession;

public class AssetDataHandler extends AbstractDataHandler {

    private static final long serialVersionUID = -5974121276678284582L;

    public AssetDataHandler(IInternalFactory entityFactory, SparkSession sparkSession) {
        super(entityFactory, sparkSession);
    }

}
