package com.epam.tcodata.external.pump.dto.maker.fact.impl;

import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.dto.fact.impl.TachoDto;
import com.epam.tcodata.external.pump.dto.maker.IDtoMaker;
import com.epam.tcodata.external.pump.factory.IExternalFactory;
import com.epam.tcodata.external.pump.service.IOffsetService;
import com.epam.tcodata.hive.dal.Hive;
import com.epam.tcodata.hive.dal.HiveConfig;
import com.epam.tcodata.hive.dal.IHive;
import com.epam.tcodata.hive.dal.domain.IHiveEntityType;
import com.epam.tcodata.hive.dal.repository.IHiveRepository;
import com.epam.tcodata.hive.dal.util.HiveCommon;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.datalake.prepared.PreparedEntity;
import com.epam.tcodata.models.datalake.prepared.dimension.PreparedEventDescription;
import com.epam.tcodata.models.datalake.prepared.fact.PreparedEvent;
import com.epam.tcodata.models.datalake.raw.dimension.RawAsset;
import com.epam.tcodata.models.mix.Entity;
import com.epam.tcodata.models.mix.fact.Tacho;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.AccountTokens;
import com.epam.tcodata.sql.dal.domain.pumps.ValidatedEventTachoOffset;
import com.epam.tcodata.sql.dal.service.pumps.IAccountTokensService;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.storage.StorageLevel;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

public class TachoDtoMaker<T extends Entity> implements IDtoMaker<T> {

    private IHive rawHive;
    private IHive preparedHive;

    // Other services
    private final IDaoFactory daoFactory;
    IExternalFactory factory;

    /**
     * Constructs tacho dto.
     */
    public TachoDtoMaker(IDaoFactory daoFactory, IExternalFactory factory, SparkSession sparkSession) {
        this.daoFactory = daoFactory;
        this.factory = factory;
        this.rawHive = new Hive(HiveConfig.RAW, sparkSession);
        this.preparedHive = new Hive(HiveConfig.PREPARED, sparkSession);
    }

    /**
     * Constructs and returns list of dto (Dto) filled with request info
     * for all assets.
     *
     * @return list of External Pump Dto.
     */
    @Override
    public List<AbstractDto<T>> makeDtoList() {
        IOffsetService tachoOffsetService = this.factory.createOffsetService(this.daoFactory);
        List<ValidatedEventTachoOffset> offsets = tachoOffsetService.getOrCreateOffsets(null).values()
                .stream()
                .map(storable -> (ValidatedEventTachoOffset) storable)
                .collect(Collectors.toList());

        Dataset<PreparedEventDescription> preparedEventDescriptionDataset = receivePreparedEventDescriptions(this.preparedHive);
        Set<String> eventDescriptions = convertToSet(preparedEventDescriptionDataset);

        Dataset<RawAsset> rawAssetDataset = receiveAssets(this.rawHive);
        Map<Long, Long> assetIdSubscriptionIdMap = convertToMap(rawAssetDataset);

        List<PreparedEvent> preparedEventList = receivePreparedEvents(offsets, this.preparedHive).javaRDD()
                .filter(preparedEvent -> eventDescriptions.contains(preparedEvent.getEventTypeDurableKey()))
                .collect();

        List<AbstractDto<T>> dtoList = new ArrayList<>();

        preparedEventList.forEach(preparedEvent -> {
                    Long subscriptionId = assetIdSubscriptionIdMap.get(preparedEvent.getAssetId());

                    TachoDto<Tacho> dto = new TachoDto<>();
                    dto.setFrom(preparedEvent.getStartDateTime().toInstant().minusSeconds(300))
                            .setTo(preparedEvent.getStartDateTime().toInstant().plusSeconds(300))
                            .setAssetId(preparedEvent.getAssetId())
                            .setOrgGroupId(subscriptionId)
                            .setPersistedDateUtc(preparedEvent.getPersistedDateUtc())
                            .setTotalElementsCount(0);
                }
        );

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
            dto.setLastSyncDuration(Time.valueOf(LocalTime.now()));
            return dto;
        });
    }

    private static Dataset<PreparedEvent> receivePreparedEvents(List<ValidatedEventTachoOffset> offsets, IHive preparedHive) {
        IHiveEntityType eventEntityType = preparedHive.databaseConfig().entityTypeByEntity(PreparedEvent.class, false);
        IHiveRepository eventRepository = preparedHive.repository(eventEntityType);

        Column condition = null;
        if (!offsets.isEmpty()) {
            ValidatedEventTachoOffset latest =
                    Collections.max(offsets, Comparator.comparing(ValidatedEventTachoOffset::getToPersistedDateUtc));

            Timestamp minPersistedDateUtc = latest.getToPersistedDateUtc();

            Column persistedDateUtcColumn = new Column(PreparedEvent.Fields.PERSISTED_DATE_UTC);
            Column yearColumn = new Column(PreparedEvent.Fields.YEAR);
            Column weekNumberColumn = new Column(PreparedEvent.Fields.WEEK_NUMBER);

            condition = persistedDateUtcColumn.$greater(minPersistedDateUtc)
                    .and(yearColumn.$greater$eq(minPersistedDateUtc.toLocalDateTime().getYear()))
                    .and(weekNumberColumn.$greater$eq(minPersistedDateUtc.toLocalDateTime().get(WeekFields.ISO.weekOfWeekBasedYear())));
        }

        return HiveCommon.rowDatasetToEntityDataset(
                eventRepository.read(condition),
                PreparedEvent.class,
                preparedHive.getSparkSession())
                .persist(StorageLevel.MEMORY_AND_DISK());
    }

    private static Dataset<PreparedEventDescription> receivePreparedEventDescriptions(IHive hive) {
        IHiveEntityType eventDescriptionEntityType = hive.databaseConfig().entityTypeByEntity(PreparedEventDescription.class, false);
        IHiveRepository eventDescriptionRepository = hive.repository(eventDescriptionEntityType);
        return HiveCommon.rowDatasetToEntityDataset(
                eventDescriptionRepository.read(),
                PreparedEventDescription.class,
                hive.getSparkSession())
                .filter((FilterFunction<PreparedEventDescription>) preparedEventDescription ->
                        isHandledDescription(preparedEventDescription.getDescription()));
    }

    private static Set<String> convertToSet(Dataset<PreparedEventDescription> preparedEventDescriptionDataset) {
        return new HashSet<>(preparedEventDescriptionDataset.collectAsList())
                .stream()
                .map(PreparedEntity::getDurableId)
                .collect(Collectors.toSet());
    }

    private static Dataset<RawAsset> receiveAssets(IHive hive) {
        IHiveEntityType eventEntityType = hive.databaseConfig().entityTypeByEntity(RawAsset.class, true);
        IHiveRepository assetRepository = hive.repository(eventEntityType);

        return HiveCommon.rowDatasetToEntityDataset(
                assetRepository.read(),
                RawAsset.class,
                hive.getSparkSession())
                .persist(StorageLevel.MEMORY_AND_DISK());
    }

    private static Map<Long, Long> convertToMap(Dataset<RawAsset> rawAssetDataset) {
        return rawAssetDataset.collectAsList().stream()
                .collect(Collectors.toMap(RawAsset::getAssetId, RawAsset::getSubscriptionId));
    }

    /**
     * Checking description for analyzing.
     *
     * @param description specified description.
     * @return <code>true</code> - is supported description, <code>false</code> - not supported.
     */
    private static boolean isHandledDescription(String description) {
        return isHarshBraking(description) || isHarshAcceleration(description);
    }

    /**
     * Check description for harsh braking.
     *
     * @param description description as string.
     * @return <code>true</code> - harsh braking, <code>false</code> - not harsh braking.
     */
    private static boolean isHarshBraking(String description) {
        String lowerCase = description.toLowerCase();
        return lowerCase.contains("harsh braking") || lowerCase.contains("harshbraking");
    }

    /**
     * Check description for harsh acceleration.
     *
     * @param description description as string.
     * @return <code>true</code> - harsh acceleration, <code>false</code> - not harsh acceleration.
     */
    private static boolean isHarshAcceleration(String description) {
        String lowerCase = description.toLowerCase();
        return lowerCase.contains("harsh acceleration") || lowerCase.contains("harshacceleration");
    }

}
