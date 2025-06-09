package com.epam.tcodata.external.pump.dto.maker.fact;

import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.dto.fact.FactDto;
import com.epam.tcodata.external.pump.dto.maker.IDtoMaker;
import com.epam.tcodata.external.pump.factory.IExternalFactory;
import com.epam.tcodata.external.pump.service.IOffsetService;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.mix.Entity;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.IStorable;
import com.epam.tcodata.sql.dal.domain.pumps.AccountTokens;
import com.epam.tcodata.sql.dal.domain.pumps.MixOffset;
import com.epam.tcodata.sql.dal.domain.pumps.OrganisationGroup;
import com.epam.tcodata.sql.dal.service.pumps.IAccountTokensService;
import com.epam.tcodata.sql.dal.service.pumps.IOrganisationGroupService;
import org.apache.spark.api.java.JavaRDD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.epam.tcodata.sql.dal.IDaoFactory.service;

@SuppressWarnings("CPD-START")
public class FactDtoMaker<T extends Entity> implements IDtoMaker<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FactDtoMaker.class);

    private final IDaoFactory daoFactory;

    /**
     * Service for constructing request info dto.
     */
    public FactDtoMaker(IDaoFactory daoFactory) {
        this.daoFactory = daoFactory;

    }

    /**
     * Constructs and returns list of dto (Dto) filled with request info
     * for all active organisation groups for certain type of entities.
     *
     * @return list of External Pump Dto.
     */
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
            dtoList.add(new FactDto<>()
                    .setOrgGroupSurrogateId(group.getId())
                    .setOrgGroupId(group.getGroupId())
                    .setAccountId(group.getAccountId()));
        }
    }

    @Override
    public JavaRDD<AbstractDto<T>> fillNonStaticInfo(JavaRDD<AbstractDto<T>> rdd,
                                                     EntityType entityType,
                                                     IExternalFactory factory) {
        IOrganisationGroupService organisationGroupService = service(this.daoFactory, OrganisationGroup.class);
        Map<Long, OrganisationGroup> activeOrgGroupMap = getActiveOrgGroupMap(organisationGroupService);
        IOffsetService offsetService = factory.createOffsetService(this.daoFactory);
        Map<Long, IStorable> offsetMap = offsetService.getOrCreateOffsets(activeOrgGroupMap.keySet());
        LOGGER.info("#MIX-OFFSET-MAP# {}", offsetMap);

        IAccountTokensService accountTokenService = service(this.daoFactory, AccountTokens.class);
        Map<Long, AccountTokens> accountTokensMap = getAccountTokensMap(accountTokenService);
        LOGGER.info("#ACCOUNT-TOKENS-MAP# ");

        return rdd.map(dto -> {
            IStorable mixOffset = offsetMap.get(((FactDto) dto).getOrgGroupSurrogateId());
            AccountTokens accountTokens = accountTokensMap.get(((FactDto) dto).getAccountId());
            dto.setAccessToken(accountTokens.getAccessToken());
            dto.setTotalElementsCount(((MixOffset) mixOffset).getTotalElementsCount());
            ((FactDto) dto).setSinceToken(((MixOffset) mixOffset).getLastProcessedTime());
            dto.setLastSyncDuration(Time.valueOf(LocalTime.now()));
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

    private Map<Long, OrganisationGroup> getActiveOrgGroupMap(IOrganisationGroupService organisationGroupService) {
        return organisationGroupService
                .getActiveOrganisationGroupsForActiveAccounts()
                .stream()
                .collect(Collectors.toMap(
                        OrganisationGroup::getId,
                        orgGroup -> orgGroup));
    }
}
