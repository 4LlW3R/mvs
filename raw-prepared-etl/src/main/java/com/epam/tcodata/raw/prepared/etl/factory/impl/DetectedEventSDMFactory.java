package com.epam.tcodata.raw.prepared.etl.factory.impl;

import com.epam.tcodata.models.datalake.prepared.fact.PreparedEvent;
import com.epam.tcodata.models.datalake.raw.fact.RawDetectedEvent;
import com.epam.tcodata.raw.prepared.etl.ReferenceSupplier;
import com.epam.tcodata.raw.prepared.etl.converter.ISingleDomainModelConverter;
import com.epam.tcodata.raw.prepared.etl.factory.AbstractSDMFactory;
import org.apache.spark.api.java.JavaRDD;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

@SuppressWarnings("CPD-START")
public class DetectedEventSDMFactory extends AbstractSDMFactory<RawDetectedEvent, PreparedEvent> {

    private static final long serialVersionUID = -3315817211822990949L;

    public DetectedEventSDMFactory() {
        super(RawDetectedEvent.class, PreparedEvent.class);
    }

    @Override
    public ISingleDomainModelConverter<RawDetectedEvent, PreparedEvent> createConverter(ReferenceSupplier referenceSupplier) {
        return new ISingleDomainModelConverter<RawDetectedEvent, PreparedEvent>() {

            private static final long serialVersionUID = 5812913985508855610L;

            @Override
            public PreparedEvent convert(RawDetectedEvent raw) {
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
                res.setOvertakingSourceType(raw.getSourceType());
                res.setOvertakingInterpolatedLatitude(raw.getInterpolatedPointLatitude());
                res.setOvertakingInterpolatedLongitude(raw.getInterpolatedPointLongitude());
                res.setRoadConditionId(raw.getRoadConditionId());
                res.setYear(raw.getYear());
                res.setWeekNumber(raw.getWeekNumber());

                return res;
            }

            @Override
            public JavaRDD<PreparedEvent> convertRDD(JavaRDD<RawDetectedEvent> rawRDD) {
                return rawRDD.flatMap(raw -> {
                            if (raw.getViolationIDs() != null && raw.getViolationIDs().length != 0) {
                                return Arrays.stream(raw.getViolationIDs()).map(i -> {
                                    PreparedEvent res = convert(raw);
                                    res.setDurableId(UUID.randomUUID().toString());
                                    res.setViolationID(i);
                                    return res;
                                }).iterator();
                            }
                            return Collections.singletonList(convert(raw)).iterator();
                        }
                );
            }
        };
    }
}
