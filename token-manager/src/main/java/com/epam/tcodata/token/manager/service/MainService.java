package com.epam.tcodata.token.manager.service;

import com.epam.tcodata.token.manager.domain.AccountCredentials;
import com.epam.tcodata.token.manager.exception.AccountTokensException;
import com.epam.tcodata.token.manager.repository.IMixIdentityRepository;
import com.epam.tcodata.secure.storage.dal.ISecretIdentity;
import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.secure.storage.dal.Secret;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.Account;
import com.epam.tcodata.sql.dal.domain.pumps.AccountTokens;
import com.epam.tcodata.sql.dal.service.pumps.IAccountTokensService;
import org.apache.commons.collections4.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MainService implements IMainService, Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainService.class);

    private static final long serialVersionUID = -3329555501231625877L;
    private transient IAccountTokensService accountTokensService;
    private transient IMixIdentityRepository mixIdentityRepository;
    private transient ISecretStorage secretStorage;

    /**
     * Main service, that handle partitions of accounts.
     *
     * @param daoFactory       daoFactory
     * @param secretStorage    secret storage instance
     */
    public MainService(IMixIdentityRepository mixIdentityRepository, IDaoFactory daoFactory, ISecretStorage secretStorage) {
        this.accountTokensService = IDaoFactory.service(daoFactory, AccountTokens.class);
        this.mixIdentityRepository = mixIdentityRepository;
        this.secretStorage = secretStorage;
    }

    @Override
    public void handle(Iterator<Account> accountIterator) {
        List<Account> accounts = IteratorUtils.toList(accountIterator);

        Map<Long, Account> activeAccounts = accounts.stream().filter(Account::isActive).collect(Collectors.toMap(Account::getId, Function.identity()));
        Map<Long, Account> inactiveAccounts = accounts.stream().filter(account -> !account.isActive()).collect(Collectors.toMap(Account::getId, Function.identity()));
        Map<Long, AccountTokens> existingAccountTokens = accountTokensService.readAll().stream().collect(Collectors.toMap(AccountTokens::getAccountId, Function.identity()));

        LOGGER.info("### ACTIVE: {}", String.valueOf(activeAccounts));
        LOGGER.info("### INACTIVE: {}", String.valueOf(inactiveAccounts));
        LOGGER.info("### EXISTING_TOKENS: {}", String.valueOf(existingAccountTokens));

        handleInactiveAccounts(inactiveAccounts, existingAccountTokens);
        handleActiveAccounts(activeAccounts, existingAccountTokens);
    }

    @Override
    public AccountTokens createAccountTokens(Account account) {
        String accountName = account.getAccountName();
        ISecretIdentity secretIdentity = Secret.Mix.name.usingSection(account.getAccountKeyVaultName());
        String secret = this.secretStorage.retrieveSecret(secretIdentity);
        AccountCredentials accountCredentials = new AccountCredentials(accountName, secret);

        return mixIdentityRepository.createAccountTokens(accountCredentials, account.getId());
    }


    private void handleInactiveAccounts(Map<Long, Account> inactiveAccounts, Map<Long, AccountTokens> existingAccountTokens) {
        inactiveAccounts.keySet().forEach(accountId -> {
            if (existingAccountTokens.containsKey(accountId))
                accountTokensService.delete(accountId);
        });
    }

    private void handleActiveAccounts(Map<Long, Account> activeAccounts, Map<Long, AccountTokens> existingAccountTokens) {
        activeAccounts.forEach((accountId, account) -> {
            if (existingAccountTokens.containsKey(accountId)) {
                try {
                    AccountTokens newAccountTokens = updateAccountTokens(existingAccountTokens.get(accountId));

                    LOGGER.info("### OLD_ACCOUNT_TOKENS: {}, NEW_ACCOUNT_TOKENS: {}",
                            String.valueOf(existingAccountTokens.get(accountId)), String.valueOf(newAccountTokens));

                    accountTokensService.update(newAccountTokens);
                } catch (AccountTokensException ex) { //if mix service return error response - create new accountTokens instead updating
                    AccountTokens accountTokens = createAccountTokens(account);
                    accountTokens.setId(existingAccountTokens.get(accountId).getId());

                    LOGGER.info("###IN_CASE_OF_FAILURE### OLD_ACCOUNT_TOKENS: {}, NEW_ACCOUNT_TOKENS: {}",
                            String.valueOf(existingAccountTokens.get(accountId)), String.valueOf(accountTokens));

                    accountTokensService.update(accountTokens);
                }
            } else {
                AccountTokens accountTokens = createAccountTokens(account);
                LOGGER.info("###IN_CASE_OF_ABSENCE### OLD_ACCOUNT_TOKENS: {}, NEW_ACCOUNT_TOKENS: {}",
                        String.valueOf(existingAccountTokens.get(accountId)), String.valueOf(accountTokens));
                accountTokensService.insert(accountTokens);
            }
        });
    }

    private AccountTokens updateAccountTokens(AccountTokens oldAccountTokens) {
        return mixIdentityRepository.updateAccountTokens(oldAccountTokens);
    }
}
