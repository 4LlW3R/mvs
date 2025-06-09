package com.epam.tcodata.sql.dal.service.impl.pumps;

import com.epam.tcodata.sql.dal.DatabaseConfig;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.HiveOffset;
import com.epam.tcodata.sql.dal.service.AbstractReadWriteService;
import com.epam.tcodata.sql.dal.service.CRUD;
import com.epam.tcodata.sql.dal.service.pumps.IHiveOffsetService;

public class HiveOffsetService extends AbstractReadWriteService<HiveOffset>
        implements IHiveOffsetService {

    /**
     * Public main constructor. Queries can be customized via crud builder.
     */
    public HiveOffsetService(IDaoFactory daoFactory) {
        super(daoFactory,
                DatabaseConfig.PUMPS, "HiveOffset",
                new CRUD(),
                HiveOffset.class);
    }

}
