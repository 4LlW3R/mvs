package com.epam.tcodata.internal.pump.service;

import com.epam.tcodata.eventhub.dal.IEventHub;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.EventHubOffset;
import com.epam.tcodata.sql.dal.service.pumps.IEventHubOffsetService;
import org.apache.spark.eventhubs.rdd.OffsetRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EventHubOffsetService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventHubOffsetService.class);

    private IEventHub eventHub;
    private IEventHubOffsetService eventHubOffsetServiceVar;
    private EntityType entityType;
    private int partitionCount;

    /**
     * This class provides mechanisms regarding EventHubOffsets.
     *
     * @param eventHub   eventHub.
     * @param entityType entityType.
     * @param daoFactory daoFactory.
     */
    public EventHubOffsetService(IEventHub eventHub, EntityType entityType, IDaoFactory daoFactory) {
        this.eventHub = eventHub;
        this.entityType = entityType;
        this.eventHubOffsetServiceVar = IDaoFactory.service(daoFactory, EventHubOffset.class);
        this.partitionCount = this.eventHub.getPartitionCount();
    }

    /**
     * Method returns offsets by entityType.
     *
     * @return map with offsets (Integer - partitionID, Long - seqNo)
     */
    public Map<String, OffsetRange> getOffsets() {
        Map<String, Long> offsetsFromDatabase = getOffsetsFromDatabase();
        Map<String, OffsetRange> offsetsFromEventHub = eventHub.getPossibleOffsets();

        if (offsetsFromDatabase.size() != partitionCount) {
            LOGGER.info("Number of EventHubOffsets from database is {}. However, number of partitions  is {}. So, use EventHubOffsets from Event Hub.", offsetsFromDatabase.size(), partitionCount);
            eventHubOffsetServiceVar.deleteEventHubOffsets(entityType);
            List<EventHubOffset> eventHubOffsets = convertToEventHubOffsets(offsetsFromEventHub);
            eventHubOffsetServiceVar.insert(eventHubOffsets);
            LOGGER.info("Offsets from Event Hub: {}", offsetsFromEventHub);
            return offsetsFromEventHub;
        } else {
            LOGGER.info("Number of EventHubOffsets from database is the same as number of partitions: {}. So, compare them with EventHubOffsets from Event Hub.", offsetsFromDatabase.size());
            Map<String, OffsetRange> comparedOffsets = compareOffsets(offsetsFromDatabase, offsetsFromEventHub);
            List<EventHubOffset> eventHubOffsets = convertToEventHubOffsets(comparedOffsets);
            eventHubOffsetServiceVar.updateEventHubOffsets(eventHubOffsets);
            return comparedOffsets;
        }
    }

    /**
     * Method compares offsets from EH and database using some rules.
     * It uses only once, after job starts. If EH seqNo more than seqNo from database, it means that we lost
     * some part of data (because of the job's fail) and can't get it from EH.
     * At this situation we have to use offsets from EH.
     *
     * @param offsetsFromDatabase offsetsFromDatabase
     * @param offsetsFromEventHub offsetsFromEventHub
     * @return map with offsets
     */
    private Map<String, OffsetRange> compareOffsets(
            Map<String, Long> offsetsFromDatabase,
            Map<String, OffsetRange> offsetsFromEventHub) {
        Map<String, OffsetRange> resultOffsets = new HashMap<>();
        offsetsFromDatabase.forEach((partitionId, seqNo) -> {
                    long seqNoFromEventHub = offsetsFromEventHub.get(partitionId).fromSeqNo();
                    if (seqNo < seqNoFromEventHub) {
                        LOGGER.warn("Some data for partition {}} is lost because of seqNo discrepancy. Use seqNo from EH...", partitionId);
                        OffsetRange currentOffsetRange = offsetsFromEventHub.get(partitionId);
                        OffsetRange newOffsetRange = new OffsetRange(currentOffsetRange.nameAndPartition(),
                                seqNoFromEventHub,
                                currentOffsetRange.untilSeqNo(),
                                currentOffsetRange.preferredLoc());
                        resultOffsets.put(partitionId, newOffsetRange);
                    } else {
                        LOGGER.info("SeqNo for partition {} in database identical or more than SeqNo in EH. Use seqNo from database...", partitionId);
                        OffsetRange currentOffsetRange = offsetsFromEventHub.get(partitionId);
                        OffsetRange newOffsetRange = new OffsetRange(currentOffsetRange.nameAndPartition(),
                                seqNo,
                                currentOffsetRange.untilSeqNo(),
                                currentOffsetRange.preferredLoc());
                        resultOffsets.put(partitionId, newOffsetRange);
                    }
                }
        );
        LOGGER.info("Result offsets: {}", resultOffsets);
        return resultOffsets;
    }

    /**
     * Method returns current offsets from database.
     *
     * @return map with offsets
     */
    private Map<String, Long> getOffsetsFromDatabase() {
        Map<String, Object> eventHubOffsetByEntityTypeFilter = new HashMap<>();
        eventHubOffsetByEntityTypeFilter.put(EventHubOffset.Fields.ENTITY_TYPE, entityType.getCode());
        List<EventHubOffset> eventHubOffsets = eventHubOffsetServiceVar.readFiltered(eventHubOffsetByEntityTypeFilter);

        Map<String, Long> offsets = new HashMap<>();
        if (eventHubOffsets != null) {
            offsets = eventHubOffsets.stream()
                    .collect(Collectors.toMap(
                            EventHubOffset::getPartitionId,
                            EventHubOffset::getSeqNo));
        }
        LOGGER.info("EventHubOffsets for {}, from database {}", entityType, offsets);
        return offsets;
    }

    /**
     * Method updates offset in database.
     *
     * @param offsetRanges offsetRanges
     */
    public void updateOffsets(OffsetRange[] offsetRanges) {
        LOGGER.info("Updating offsets in database...");
        List<EventHubOffset> eventHubOffsets = new ArrayList<>();
        for (OffsetRange offsetRange : offsetRanges) {
            eventHubOffsets.add(new EventHubOffset(
                    0,
                    String.valueOf(offsetRange.partitionId()),
                    entityType.getCode(),
                    entityType.getSuperType().getCode(),
                    offsetRange.untilSeqNo(),
                    Instant.now(),
                    offsetRange.count(),
                    0
            ));
        }
        eventHubOffsetServiceVar.updateEventHubOffsets(eventHubOffsets);
        LOGGER.info("New offsets: {} ", eventHubOffsets);
    }

    private List<EventHubOffset> convertToEventHubOffsets(Map<String, OffsetRange> offsets) {
        List<EventHubOffset> eventHubOffsets = new ArrayList<>();
        offsets.forEach((partitionId, offsetRange) ->
                eventHubOffsets.add(new EventHubOffset(
                        0,
                        partitionId,
                        entityType.getCode(),
                        entityType.getSuperType().getCode(),
                        offsetRange.fromSeqNo(),
                        Instant.now(),
                        0,
                        0
                )));
        return eventHubOffsets;
    }

}
