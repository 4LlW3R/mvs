package com.epam.tcodata.external.pump.dto.maker.dimension.impl;

import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.dto.dimension.DimensionDto;
import com.epam.tcodata.external.pump.dto.maker.IDtoMaker;
import com.epam.tcodata.external.pump.factory.IExternalFactory;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.mix.Entity;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.Account;
import com.epam.tcodata.sql.dal.domain.pumps.AccountTokens;
import com.epam.tcodata.sql.dal.service.pumps.IAccountService;
import com.epam.tcodata.sql.dal.service.pumps.IAccountTokensService;
import org.apache.spark.api.java.JavaRDD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.epam.tcodata.sql.dal.IDaoFactory.service;

public class OrganisationGroupDtoMaker<T extends Entity> implements IDtoMaker<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganisationGroupDtoMaker.class);

    private final IDaoFactory daoFactory;
    private final String accountName;

    /**
     * Constructs request info dto.
     */
    public OrganisationGroupDtoMaker(IDaoFactory daoFactory, String accountName) {
        this.daoFactory = daoFactory;
        this.accountName = accountName;
    }

    @Override
    public List<AbstractDto<T>> makeDtoList() {
        List<AbstractDto<T>> dtoList = new ArrayList<>();
        dtoList.add(new DimensionDto<>().setOrgGroupId(0));
        return dtoList;
    }

    @Override
    public JavaRDD<AbstractDto<T>> fillNonStaticInfo(JavaRDD<AbstractDto<T>> rdd,
                                                     EntityType entityType,
                                                     IExternalFactory factory) {
        IAccountService accountService = service(this.daoFactory, Account.class);
        Account account = accountService.readAll().stream()
                .filter(acc -> acc.getAccountName().equals(this.accountName))
                .findFirst()
                .get();

        IAccountTokensService accountTokenService = service(this.daoFactory, AccountTokens.class);
        Map<Long, AccountTokens> accountTokensMap = getAccountTokensMap(accountTokenService);
        LOGGER.info("#ACCOUNT-TOKENS-MAP# ");

        return rdd.map(dto -> {
            AccountTokens accountTokens = accountTokensMap.get(account.getId());
            dto.setAccessToken(accountTokens.getAccessToken());
            return dto;
        });
    }

    private static Map<Long, AccountTokens> getAccountTokensMap(IAccountTokensService accountTokensService) {
        return accountTokensService.readAll()
                .stream()
                .collect(Collectors.toMap(
                        AccountTokens::getAccountId,
                        token -> token));
    }
}
