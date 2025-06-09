package com.epam.tcodata.external.pump.dto.maker.dimension.impl;

import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.dto.dimension.DimensionDto;
import com.epam.tcodata.external.pump.dto.maker.IDtoMaker;
import com.epam.tcodata.external.pump.factory.IExternalFactory;
import com.epam.tcodata.hive.dal.Hive;
import com.epam.tcodata.hive.dal.HiveConfig;
import com.epam.tcodata.hive.dal.domain.IHiveEntityType;
import com.epam.tcodata.hive.dal.repository.IHiveRepository;
import com.epam.tcodata.hive.dal.util.HiveCommon;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.datalake.IDataLakeEntity;
import com.epam.tcodata.models.datalake.raw.dimension.RawOrganisationGroup;
import com.epam.tcodata.models.mix.Entity;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.AccountTokens;
import com.epam.tcodata.sql.dal.service.pumps.IAccountTokensService;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;

import java.util.*;

public class OrganisationSubGroupDtoMaker<T extends Entity> implements IDtoMaker<T> {

    private final IDaoFactory daoFactory;

    private IHiveRepository normRepository;
    private Class<? extends IDataLakeEntity> dataLakeClass = RawOrganisationGroup.class;

    /**
     * Constructs request info dto.
     */
    public OrganisationSubGroupDtoMaker(IDaoFactory daoFactory, SparkSession sparkSession) {
        this.daoFactory = daoFactory;

        Hive hive = new Hive(HiveConfig.RAW, sparkSession);
        IHiveEntityType hiveNormEntityType = hive.databaseConfig().entityTypeByEntity(this.dataLakeClass, true);
        this.normRepository = hive.repository(hiveNormEntityType);
    }

    @Override
    public List<AbstractDto<T>> makeDtoList() {
        JavaRDD<RawOrganisationGroup> organisationGroupJavaRDD =
                HiveCommon.rowRddToEntityRdd(this.normRepository.read().javaRDD(), this.dataLakeClass);

        Set<Long> organisationGroupIds = new HashSet<>(
                organisationGroupJavaRDD.map(RawOrganisationGroup::getGroupId)
                        .filter(Objects::nonNull)
                        .collect());

        List<AbstractDto<T>> dtoList = new ArrayList<>();
        organisationGroupIds.forEach(organisationGroupId ->
                dtoList.add(new DimensionDto<>()
                        .setOrgGroupId(organisationGroupId)));
        return dtoList;
    }

    @Override
    public JavaRDD<AbstractDto<T>> fillNonStaticInfo(JavaRDD<AbstractDto<T>> rdd,
                                                     EntityType entityType,
                                                     IExternalFactory factory) {
        IAccountTokensService accountTokenService = IDaoFactory.service(this.daoFactory, AccountTokens.class);
        AccountTokens accountTokens = accountTokenService.readAll()
                .stream()
                .findFirst()
                .get();

        return rdd.map(dto -> {
            dto.setAccessToken(accountTokens.getAccessToken());
            return dto;
        });
    }
}
