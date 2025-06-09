package com.epam.tcodata.sql.dal.service.impl.pumps;

import com.epam.tcodata.sql.dal.DatabaseConfig;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.Signal;
import com.epam.tcodata.sql.dal.service.AbstractReadWriteService;
import com.epam.tcodata.sql.dal.service.CRUD;
import com.epam.tcodata.sql.dal.service.pumps.ISignalService;

public class SignalService extends AbstractReadWriteService<Signal>
        implements ISignalService {

    /**
     * Public main constructor. Queries can be customized via crud builder.
     */
    public SignalService(IDaoFactory daoFactory) {
        super(daoFactory,
                DatabaseConfig.PUMPS, "Signal",
                new CRUD(),
                Signal.class);
    }

}
