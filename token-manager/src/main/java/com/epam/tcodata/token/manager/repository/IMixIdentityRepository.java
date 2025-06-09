package com.epam.tcodata.token.manager.repository;

import com.epam.tcodata.token.manager.domain.AccountCredentials;
import com.epam.tcodata.sql.dal.domain.pumps.AccountTokens;

public interface IMixIdentityRepository {

    AccountTokens createAccountTokens(AccountCredentials accountCredentials, Long accountId);

    AccountTokens updateAccountTokens(AccountTokens oldAccountTokens);

    String getMixIdentityTokenEndpoint();

    String getMixScopes();
}
