package com.epam.tcodata.external.pump.dto.maker.dimension;

import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.dto.dimension.DimensionDto;
import com.epam.tcodata.external.pump.dto.maker.IDtoMaker;
import com.epam.tcodata.external.pump.factory.IExternalFactory;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.mix.Entity;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.AccountTokens;
import com.epam.tcodata.sql.dal.domain.pumps.OrganisationGroup;
import com.epam.tcodata.sql.dal.service.pumps.IAccountTokensService;
import com.epam.tcodata.sql.dal.service.pumps.IOrganisationGroupService;
import org.apache.spark.api.java.JavaRDD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.epam.tcodata.sql.dal.IDaoFactory.service;

@SuppressWarnings("CPD-START")
public class DimensionDtoMaker<T extends Entity> implements IDtoMaker<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DimensionDtoMaker.class);

    private final IDaoFactory daoFactory;

    /**
     * Constructs request info dto.
     */
    public DimensionDtoMaker(IDaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public List<AbstractDto<T>> makeDtoList() {
        IOrganisationGroupService organisationGroupService =
                service(this.daoFactory, OrganisationGroup.class);
        List<OrganisationGroup> activeGroups =
                organisationGroupService.getActiveOrganisationGroupsForActiveAccounts();
        List<AbstractDto<T>> dtoList = new ArrayList<>();
        fillStaticInfo(dtoList, activeGroups);
        LOGGER.info("#STATIC-DTO-LIST# {}", dtoList);
        return dtoList;
    }

    private void fillStaticInfo(List<AbstractDto<T>> dtoList,
                                List<OrganisationGroup> groups) {
        for (OrganisationGroup group : groups) {
            dtoList.add(new DimensionDto<>()
                    .setOrgGroupId(group.getGroupId())
                    .setAccountId(group.getAccountId()));
        }
    }

    @Override
    public JavaRDD<AbstractDto<T>> fillNonStaticInfo(JavaRDD<AbstractDto<T>> rdd,
                                                     EntityType entityType,
                                                     IExternalFactory factory) {
        IAccountTokensService accountTokenService = service(this.daoFactory, AccountTokens.class);
        Map<Long, AccountTokens> accountTokensMap = getAccountTokensMap(accountTokenService);
        LOGGER.info("#ACCOUNT-TOKENS-MAP# ");

        return rdd.map(dto -> {
            AccountTokens accountTokens = accountTokensMap.get(((DimensionDto) dto).getAccountId());
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
