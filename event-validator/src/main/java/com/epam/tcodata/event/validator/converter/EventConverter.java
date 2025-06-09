package com.epam.tcodata.event.validator.converter;

import com.epam.tcodata.event.validator.domain.EnrichedPreparedEvent;
import com.epam.tcodata.models.datalake.prepared.analytics.PreparedValidatedEvent;
import com.epam.tcodata.models.datalake.prepared.fact.PreparedEvent;

public class EventConverter implements IEventConverter {

    private static final long serialVersionUID = -9184231728248856357L;

    /**
     * Method converts prepared event into enriched prepared event (needs only for calculations).
     *
     * @return EnrichedPreparedEvent
     */
    @Override
    public EnrichedPreparedEvent convertToEnriched(PreparedEvent preparedEvent) {
        EnrichedPreparedEvent enrichedPreparedEvent = new EnrichedPreparedEvent();
        enrichedPreparedEvent.setDurableId(preparedEvent.getDurableId());
        enrichedPreparedEvent.setOrganizationDurableKey(preparedEvent.getOrganizationDurableKey());
        enrichedPreparedEvent.setExternalId(preparedEvent.getExternalId());
        enrichedPreparedEvent.setPersistedDateUtc(preparedEvent.getPersistedDateUtc());
        enrichedPreparedEvent.setDriverDurableKey(preparedEvent.getDriverDurableKey());
        enrichedPreparedEvent.setVehicleDurableKey(preparedEvent.getVehicleDurableKey());
        enrichedPreparedEvent.setTotalOccurances(preparedEvent.getTotalOccurances());
        enrichedPreparedEvent.setTotalTimeSeconds(preparedEvent.getTotalTimeSeconds());
        enrichedPreparedEvent.setEventTypeDurableKey(preparedEvent.getEventTypeDurableKey());
        enrichedPreparedEvent.setEventTypeId(preparedEvent.getEventTypeId());
        enrichedPreparedEvent.setDriverId(preparedEvent.getDriverId());
        enrichedPreparedEvent.setAssetId(preparedEvent.getAssetId());
        enrichedPreparedEvent.setValue(preparedEvent.getValue());
        enrichedPreparedEvent.setEndDateTime(preparedEvent.getEndDateTime());
        enrichedPreparedEvent.setStartDateTime(preparedEvent.getStartDateTime());
        enrichedPreparedEvent.setEventCategory(preparedEvent.getEventCategory());
        enrichedPreparedEvent.setStartOdometerKilometres(preparedEvent.getStartOdometerKilometres());
        enrichedPreparedEvent.setStartPositionTimestamp(preparedEvent.getStartPositionTimestamp());
        enrichedPreparedEvent.setStartPositionLongitude(preparedEvent.getStartPositionLongitude());
        enrichedPreparedEvent.setStartPositionLatitude(preparedEvent.getStartPositionLatitude());
        enrichedPreparedEvent.setStartPositionPositionId(preparedEvent.getStartPositionPositionId());
        enrichedPreparedEvent.setStartPositionSpeedKilometresPerHour(preparedEvent.getStartPositionSpeedKilometresPerHour());
        enrichedPreparedEvent.setEndOdometerKilometres(preparedEvent.getEndOdometerKilometres());
        enrichedPreparedEvent.setEndPositionTimestamp(preparedEvent.getEndPositionTimestamp());
        enrichedPreparedEvent.setEndPositionLongitude(preparedEvent.getEndPositionLongitude());
        enrichedPreparedEvent.setEndPositionLatitude(preparedEvent.getEndPositionLatitude());
        enrichedPreparedEvent.setEndPositionPositionId(preparedEvent.getEndPositionPositionId());
        enrichedPreparedEvent.setEndPositionSpeedKilometresPerHour(preparedEvent.getEndPositionSpeedKilometresPerHour());
        enrichedPreparedEvent.setValueType(preparedEvent.getValueType());
        enrichedPreparedEvent.setValueUnits(preparedEvent.getValueUnits());
        enrichedPreparedEvent.setLocationId(preparedEvent.getLocationId());
        enrichedPreparedEvent.setSpeedLimit(preparedEvent.getSpeedLimit());
        enrichedPreparedEvent.setYear(preparedEvent.getYear());
        enrichedPreparedEvent.setWeekNumber(preparedEvent.getWeekNumber());
        return enrichedPreparedEvent;
    }

    /**
     * Method converts enriched prepared event into prepared validated event.
     *
     * @return PreparedValidatedEvent
     */
    @Override
    public PreparedValidatedEvent convertToPreparedValidated(EnrichedPreparedEvent enrichedPreparedEvent) {
        PreparedValidatedEvent preparedValidatedEvent = new PreparedValidatedEvent();
        preparedValidatedEvent.setDurableId(enrichedPreparedEvent.getDurableId());
        preparedValidatedEvent.setOrganizationDurableKey(enrichedPreparedEvent.getOrganizationDurableKey());
        preparedValidatedEvent.setExternalId(enrichedPreparedEvent.getExternalId());
        preparedValidatedEvent.setPersistedDateUtc(enrichedPreparedEvent.getPersistedDateUtc());
        preparedValidatedEvent.setDriverDurableKey(enrichedPreparedEvent.getDriverDurableKey());
        preparedValidatedEvent.setVehicleDurableKey(enrichedPreparedEvent.getVehicleDurableKey());
        preparedValidatedEvent.setTotalOccurances(enrichedPreparedEvent.getTotalOccurances());
        preparedValidatedEvent.setTotalTimeSeconds(enrichedPreparedEvent.getTotalTimeSeconds());
        preparedValidatedEvent.setEventTypeDurableKey(enrichedPreparedEvent.getEventTypeDurableKey());
        preparedValidatedEvent.setEventTypeId(enrichedPreparedEvent.getEventTypeId());
        preparedValidatedEvent.setDriverId(enrichedPreparedEvent.getDriverId());
        preparedValidatedEvent.setAssetId(enrichedPreparedEvent.getAssetId());
        preparedValidatedEvent.setValue(enrichedPreparedEvent.getValue());
        preparedValidatedEvent.setEndDateTime(enrichedPreparedEvent.getEndDateTime());
        preparedValidatedEvent.setStartDateTime(enrichedPreparedEvent.getStartDateTime());
        preparedValidatedEvent.setEventCategory(enrichedPreparedEvent.getEventCategory());
        preparedValidatedEvent.setStartOdometerKilometres(enrichedPreparedEvent.getStartOdometerKilometres());
        preparedValidatedEvent.setStartPositionTimestamp(enrichedPreparedEvent.getStartPositionTimestamp());
        preparedValidatedEvent.setStartPositionLongitude(enrichedPreparedEvent.getStartPositionLongitude());
        preparedValidatedEvent.setStartPositionLatitude(enrichedPreparedEvent.getStartPositionLatitude());
        preparedValidatedEvent.setStartPositionPositionId(enrichedPreparedEvent.getStartPositionPositionId());
        preparedValidatedEvent.setStartPositionSpeedKilometresPerHour(enrichedPreparedEvent.getStartPositionSpeedKilometresPerHour());
        preparedValidatedEvent.setEndOdometerKilometres(enrichedPreparedEvent.getEndOdometerKilometres());
        preparedValidatedEvent.setEndPositionTimestamp(enrichedPreparedEvent.getEndPositionTimestamp());
        preparedValidatedEvent.setEndPositionLongitude(enrichedPreparedEvent.getEndPositionLongitude());
        preparedValidatedEvent.setEndPositionLatitude(enrichedPreparedEvent.getEndPositionLatitude());
        preparedValidatedEvent.setEndPositionPositionId(enrichedPreparedEvent.getEndPositionPositionId());
        preparedValidatedEvent.setEndPositionSpeedKilometresPerHour(enrichedPreparedEvent.getEndPositionSpeedKilometresPerHour());
        preparedValidatedEvent.setValueType(enrichedPreparedEvent.getValueType());
        preparedValidatedEvent.setValueUnits(enrichedPreparedEvent.getValueUnits());
        preparedValidatedEvent.setLocationId(enrichedPreparedEvent.getLocationId());
        preparedValidatedEvent.setSpeedLimit(enrichedPreparedEvent.getSpeedLimit());
        preparedValidatedEvent.setValidationCode(enrichedPreparedEvent.getValidationCode());
        preparedValidatedEvent.setProblemVehicle(enrichedPreparedEvent.getProblemVehicle());
        preparedValidatedEvent.setYear(enrichedPreparedEvent.getYear());
        preparedValidatedEvent.setWeekNumber(enrichedPreparedEvent.getWeekNumber());
        return preparedValidatedEvent;
    }

}
