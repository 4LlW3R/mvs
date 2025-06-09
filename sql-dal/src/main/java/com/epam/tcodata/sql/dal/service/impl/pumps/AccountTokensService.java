package com.epam.tcodata.sql.dal.service.impl.pumps;

import com.epam.tcodata.sql.dal.DatabaseConfig;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.AccountTokens;
import com.epam.tcodata.sql.dal.service.AbstractReadWriteService;
import com.epam.tcodata.sql.dal.service.CRUD;
import com.epam.tcodata.sql.dal.service.pumps.IAccountTokensService;

public class AccountTokensService extends AbstractReadWriteService<AccountTokens> implements IAccountTokensService {

    /**
     * Public main constructor. Queries can be customized via crud builder.
     */
    public AccountTokensService(IDaoFactory daoFactory) {
        super(daoFactory,
                DatabaseConfig.PUMPS, "AccountTokens",
                new CRUD(),
                AccountTokens.class);
    }
}
