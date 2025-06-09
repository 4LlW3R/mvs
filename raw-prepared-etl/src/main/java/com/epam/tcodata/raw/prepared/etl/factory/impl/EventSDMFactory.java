package com.epam.tcodata.raw.prepared.etl.factory.impl;

import com.epam.tcodata.models.datalake.prepared.fact.PreparedEvent;
import com.epam.tcodata.models.datalake.raw.fact.RawEvent;
import com.epam.tcodata.raw.prepared.etl.ReferenceSupplier;
import com.epam.tcodata.raw.prepared.etl.converter.ISingleDomainModelConverter;
import com.epam.tcodata.raw.prepared.etl.factory.AbstractSDMFactory;

@SuppressWarnings("CPD-START")
public class EventSDMFactory extends AbstractSDMFactory<RawEvent, PreparedEvent> {

    private static final long serialVersionUID = -4398229223508550922L;

    public EventSDMFactory() {
        super(RawEvent.class, PreparedEvent.class);
    }

    @Override
    public ISingleDomainModelConverter<RawEvent, PreparedEvent> createConverter(ReferenceSupplier referenceSupplier) {
        return raw -> {
            String durableId = referenceSupplier.getGroupDurableId(raw.getSubscriptionId());
            String eventTypeDurableKey = referenceSupplier.getLibraryEventDurableId(raw.getEventTypeId());

            PreparedEvent res = new PreparedEvent();
            res.setDurableId(raw.getDurableId());
            res.setOrganizationDurableKey(durableId);
            res.setExternalId(raw.getEventId());
            res.setPersistedDateUtc(raw.getPersistedDateUtc());
            res.setDriverDurableKey(raw.getDriverDurableKey());
            res.setVehicleDurableKey(raw.getVehicleDurableKey());
            res.setTotalOccurances(raw.getTotalOccurances());
            res.setTotalTimeSeconds(raw.getTotalTimeSeconds());
            res.setEventTypeDurableKey(eventTypeDurableKey);
            res.setEventTypeId(raw.getEventTypeId());
            res.setDriverId(raw.getDriverId());
            res.setAssetId(raw.getAssetId());
            res.setValue(raw.getValue());
            res.setEndDateTime(raw.getEndDateTime());
            res.setStartDateTime(raw.getStartDateTime());
            res.setEventCategory(raw.getEventCategory());
            res.setStartOdometerKilometres(raw.getStartOdometerKilometres());
            res.setStartPositionTimestamp(raw.getStartPositionTimestamp());
            res.setStartPositionLongitude(raw.getStartPositionLongitude());
            res.setStartPositionLatitude(raw.getStartPositionLatitude());
            res.setStartPositionPositionId(raw.getStartPositionPositionId());
            res.setStartPositionSpeedKilometresPerHour(raw.getStartPositionSpeedKilometresPerHour());
            res.setEndOdometerKilometres(raw.getEndOdometerKilometres());
            res.setEndPositionTimestamp(raw.getEndPositionTimestamp());
            res.setEndPositionLongitude(raw.getEndPositionLongitude());
            res.setEndPositionLatitude(raw.getEndPositionLatitude());
            res.setEndPositionPositionId(raw.getEndPositionPositionId());
            res.setEndPositionSpeedKilometresPerHour(raw.getEndPositionSpeedKilometresPerHour());
            res.setValueType(raw.getValueType());
            res.setValueUnits(raw.getValueUnits());
            res.setLocationId(raw.getLocationId());
            res.setSpeedLimit(raw.getSpeedLimit());
            res.setYear(raw.getYear());
            res.setWeekNumber(raw.getWeekNumber());

            return res;
        };
    }
}
