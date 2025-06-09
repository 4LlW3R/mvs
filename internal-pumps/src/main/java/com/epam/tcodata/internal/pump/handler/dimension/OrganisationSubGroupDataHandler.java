package com.epam.tcodata.internal.pump.handler.dimension;

import com.epam.tcodata.internal.pump.factory.IInternalFactory;
import com.epam.tcodata.internal.pump.handler.AbstractDataHandler;
import org.apache.spark.sql.SparkSession;

public class OrganisationSubGroupDataHandler extends AbstractDataHandler {

    private static final long serialVersionUID = -7617666942746489311L;

    public OrganisationSubGroupDataHandler(IInternalFactory entityFactory, SparkSession sparkSession) {
        super(entityFactory, sparkSession);
    }

}
