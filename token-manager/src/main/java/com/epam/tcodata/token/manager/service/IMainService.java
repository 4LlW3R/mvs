package com.epam.tcodata.token.manager.service;

import com.epam.tcodata.sql.dal.domain.pumps.Account;
import com.epam.tcodata.sql.dal.domain.pumps.AccountTokens;

import java.util.Iterator;

/**
 * Main service, that handle partitions of accounts.
 */
public interface IMainService {

    /**
     * Method handles partition on account.
     *
     * @param accountIterator iterator
     */
    void handle(Iterator<Account> accountIterator);

    /**
     * Method creates new accountTokens.
     *
     * @param account account
     * @return AccountTokens
     */
    AccountTokens createAccountTokens(Account account);
}
