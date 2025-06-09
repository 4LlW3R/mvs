package com.epam.tcodata.external.pump.service.impl;

import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.external.pump.dto.fact.impl.TachoDto;
import com.epam.tcodata.external.pump.factory.IExternalFactory;
import com.epam.tcodata.external.pump.service.AbstractOffsetService;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.enriched.fact.EnrichedTacho;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.IStorable;
import com.epam.tcodata.sql.dal.domain.pumps.ValidatedEventTachoOffset;
import com.epam.tcodata.sql.dal.service.pumps.IValidatedEventTachoOffsetService;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.epam.tcodata.sql.dal.IDaoFactory.service;

public class TachoOffsetService extends AbstractOffsetService<EnrichedTacho> {

    private final IValidatedEventTachoOffsetService validatedEventTachoOffsetService;

    /**
     * Constructor.
     */
    public TachoOffsetService(IExternalFactory externalFactory, IDaoFactory daoFactory) {
        super(externalFactory, daoFactory, EntityType.TACHO);
        this.validatedEventTachoOffsetService = service(daoFactory, ValidatedEventTachoOffset.class);
    }

    @Override
    public Map<Long, IStorable> getOrCreateOffsets(Set<Long> idSet) {
        return validatedEventTachoOffsetService.readAll().stream()
                .collect(Collectors.toMap(ValidatedEventTachoOffset::getId, o -> o));
    }

    @Override
    public void updateOffsets(List<AbstractDto> dtoList) {
        List<TachoDto> dtos = dtoList.stream()
                .map(dto -> (TachoDto) dto)
                .collect(Collectors.toList());

        if (!dtos.isEmpty()) {
            Timestamp fromPersistedDateUtc =
                    Collections.min(dtos, Comparator.comparing(TachoDto::getPersistedDateUtc)).getPersistedDateUtc();

            Timestamp toPersistedDateUtc =
                    Collections.max(dtos, Comparator.comparing(TachoDto::getPersistedDateUtc)).getPersistedDateUtc();

            int elementCount = dtos.stream().mapToInt(dto -> dto.getEnrichedEntityList().size()).sum();

            Time lastSyncDuration =
                    Collections.max(dtos, Comparator.comparing(TachoDto::getLastSyncDuration)).getLastSyncDuration();

            ValidatedEventTachoOffset offset = new ValidatedEventTachoOffset(
                    0,
                    Timestamp.from(Instant.now()),
                    fromPersistedDateUtc,
                    toPersistedDateUtc,
                    elementCount,
                    lastSyncDuration
            );
            validatedEventTachoOffsetService.insert(offset);
        }
    }
}
