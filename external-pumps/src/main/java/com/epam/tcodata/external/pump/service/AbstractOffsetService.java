package com.epam.tcodata.external.pump.service;

import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.dto.fact.FactDto;
import com.epam.tcodata.external.pump.factory.IExternalFactory;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.enriched.IEnrichable;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.IStorable;
import com.epam.tcodata.sql.dal.domain.pumps.MixOffset;
import com.epam.tcodata.sql.dal.service.pumps.IMixOffsetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static com.epam.tcodata.sql.dal.IDaoFactory.service;


public abstract class AbstractOffsetService<T extends IEnrichable> implements IOffsetService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractOffsetService.class);

    private static final Long SECONDS_IN_WEEK = 604800L;
    private static final Long SECONDS_IN_HALF_AN_HOUR = 1800L; // added to SECONDS_IN_WEEK to avoid corner cases
    private static final Long SECONDS_IN_FIVE_MIN = 300L; // added to SECONDS_IN_WEEK to decrease tolerated time-gap for sinceToken

    private IExternalFactory externalFactory;
    private IMixOffsetService mixOffsetService;
    private EntityType entityType;


    /**
     * Performs operations with offsets that are common for all entities.
     *
     * @param externalFactory externalFactory to get current moment
     * @param daoFactory      factory for creating services to work with sql database.
     * @param entityType      entity type.
     */
    protected AbstractOffsetService(IExternalFactory externalFactory, IDaoFactory daoFactory, EntityType entityType) {
        this.externalFactory = externalFactory;
        this.mixOffsetService = service(daoFactory, MixOffset.class);
        this.entityType = entityType;
    }

    /**
     * Creates map of offsets (tracked entity endpoints) for set of organisation groups.
     *
     * @param idSet ids of organisation groups / assets (for Tacho).
     * @return map of offsets with its' it group ids.
     */
    public Map<Long, IStorable> getOrCreateOffsets(Set<Long> idSet) {
        Map<Long, Optional<MixOffset>> offsetMapOpt =
                mixOffsetService.readMixOffsetMap(idSet, entityType);

        if (fillAbsentOffsets(offsetMapOpt)) {
            offsetMapOpt = mixOffsetService.readMixOffsetMap(idSet, entityType);
        }

        Map<Long, MixOffset> offsets = offsetMapOpt
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Entry::getKey,
                        offsetOpt -> offsetOpt.getValue().get()));

        offsets.forEach((key, offset) -> fixOverdueOffset(key, offset, this.externalFactory));

        return offsets
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    /**
     * Fix overdue offset (reduce lastProcessedTime if more than 7 days according to MIX restrictions).
     *
     * @param orgId  organisation id
     * @param offset mix offset.
     */
    static void fixOverdueOffset(Long orgId, MixOffset offset, IExternalFactory externalFactory) {
        Instant now = externalFactory.getCurrentMoment();
        Instant oldLastProcessedTime = offset.getLastProcessedTime();
        if (oldLastProcessedTime == null) {
            throw new IllegalArgumentException("LastProcessedTime(SinceToken) should not be null");
        }
        if (oldLastProcessedTime.compareTo(now.minusSeconds(SECONDS_IN_WEEK).plusSeconds(SECONDS_IN_FIVE_MIN)) < 0) {
            Instant newLastProcessedTime = now.minusSeconds(SECONDS_IN_WEEK).plusSeconds(SECONDS_IN_HALF_AN_HOUR);
            LOGGER.warn("SinceToken for organisation id {} is overdue. SinceToken changed from {} to {}",
                    orgId,
                    oldLastProcessedTime,
                    newLastProcessedTime);
            offset.setLastProcessedTime(newLastProcessedTime);
        }
    }

    /**
     * Fill absent offsets if there're any.
     *
     * @param offsetMapOpt map of Optional[TrackedEntityEndpoint].
     * @return true if there's any absents offsets, else false.
     */
    private boolean fillAbsentOffsets(Map<Long, Optional<MixOffset>> offsetMapOpt) {

        Set<Long> absentOffsetsIdSet = new HashSet<>();
        boolean isAnyAbsent = false;

        for (Entry<Long, Optional<MixOffset>> offsetEntry : offsetMapOpt.entrySet()) {
            if (!offsetEntry.getValue().isPresent()) {
                isAnyAbsent = true;
                absentOffsetsIdSet.add(offsetEntry.getKey());
            }
        }

        mixOffsetService.insert(createAbsentOffsets(absentOffsetsIdSet));

        return isAnyAbsent;
    }

    private List<MixOffset> createAbsentOffsets(Set<Long> idSet) {
        List<MixOffset> offsetList = new ArrayList<>();
        for (Long id : idSet) {
            offsetList.add(
                    new MixOffset(
                            0,
                            id,
                            entityType.getCode(),
                            entityType.getSuperType().getCode(),
                            Instant.now().truncatedTo(ChronoUnit.SECONDS), //TO DO
                            Instant.now().truncatedTo(ChronoUnit.SECONDS).minusSeconds(1800), //TO DO
                            0,
                            0,
                            Time.valueOf("00:00:00"),
                            0,
                            null
                    )
            );
        }
        return offsetList;
    }

    /**
     * Updates offsets (tracked entity endpoint) in database.
     *
     * @param dtoList data transfer objects.
     */
    public void updateOffsets(List<AbstractDto> dtoList) {
        List<FactDto> dtos = dtoList.stream()
                .map(dto -> (FactDto) dto).collect(Collectors.toList());

        Set<Long> groupSurrogateIdSet = dtos.stream()
                .map(t -> t.getOrgGroupSurrogateId())
                .collect(Collectors.toSet());

        Map<Long, MixOffset> offsetMap = getOffsets(groupSurrogateIdSet, entityType);

        mixOffsetService.update(getUpdatedOffsets(dtos, offsetMap));
    }

    private Map<Long, MixOffset> getOffsets(Set<Long> groupSurrogateIdSet, EntityType entityCode) {
        Map<Long, Optional<MixOffset>> offsetMap =
                mixOffsetService.readMixOffsetMap(groupSurrogateIdSet, entityCode);

        return offsetMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Entry::getKey,
                        entry -> entry.getValue().get()));
    }

    private List<MixOffset> getUpdatedOffsets(List<FactDto> dtoList,
                                              Map<Long, MixOffset> offsetMap) {
        List<MixOffset> updatedOffsets = new ArrayList<>();

        for (FactDto dto : dtoList) {
            long orgGroupSurrogateId = dto.getOrgGroupSurrogateId();

            MixOffset offset =
                    offsetMap.get(orgGroupSurrogateId);

            enrichOffsetWithAdditionalInfo(offset, dto);

            updatedOffsets.add(offset);
        }

        return updatedOffsets;
    }

    private void enrichOffsetWithAdditionalInfo(MixOffset offset, FactDto dto) {
        offset.setLastSyncDateUtc(Instant.now());
        offset.setLastProcessedTime(dto.getNextSinceToken());
        offset.setLastSyncResultCode(dto.getLastSyncResultCode());
        offset.setLastSyncElementCount(dto.getLastSyncElementCount());
        offset.setLastSyncDuration(dto.getLastSyncDuration());
        offset.setTotalElementsCount(dto.getTotalElementsCount());
        offset.setLastErrorMessage(dto.getLastErrorMessage());
    }
}
