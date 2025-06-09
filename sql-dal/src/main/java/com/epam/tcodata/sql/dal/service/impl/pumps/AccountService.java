package com.epam.tcodata.sql.dal.service.impl.pumps;

import com.epam.tcodata.sql.dal.DatabaseConfig;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.Account;
import com.epam.tcodata.sql.dal.service.AbstractReadWriteService;
import com.epam.tcodata.sql.dal.service.CRUD;
import com.epam.tcodata.sql.dal.service.pumps.IAccountService;

public class AccountService extends AbstractReadWriteService<Account> implements IAccountService {

    /**
     * Public main constructor. Queries can be customized via crud builder.
     */
    public AccountService(IDaoFactory daoFactory) {
        super(daoFactory,
                DatabaseConfig.PUMPS, "Account",
                new CRUD(),
                Account.class);
    }

}
