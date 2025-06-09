package com.epam.tcodata.sql.dal.service.impl.pumps;

import com.epam.tcodata.sql.dal.DatabaseConfig;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.ValidatedEventTachoOffset;
import com.epam.tcodata.sql.dal.service.AbstractReadWriteService;
import com.epam.tcodata.sql.dal.service.CRUD;
import com.epam.tcodata.sql.dal.service.pumps.IValidatedEventTachoOffsetService;

public class ValidatedEventTachoOffsetService extends AbstractReadWriteService<ValidatedEventTachoOffset>
        implements IValidatedEventTachoOffsetService {

    /**
     * Public main constructor. Queries can be customized via crud builder.
     */
    public ValidatedEventTachoOffsetService(IDaoFactory daoFactory) {
        super(daoFactory,
                DatabaseConfig.PUMPS, "ValidatedEventTachoOffset",
                new CRUD(),
                ValidatedEventTachoOffset.class);
    }

}
