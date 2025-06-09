package com.epam.tcodata.raw.prepared.etl.factory.impl;

import com.epam.tcodata.models.datalake.prepared.fact.PreparedSubTrip;
import com.epam.tcodata.models.datalake.raw.fact.RawSubTrip;
import com.epam.tcodata.raw.prepared.etl.ReferenceSupplier;
import com.epam.tcodata.raw.prepared.etl.converter.ISingleDomainModelConverter;
import com.epam.tcodata.raw.prepared.etl.factory.AbstractSDMFactory;

public class SubTripSDMFactory extends AbstractSDMFactory<RawSubTrip, PreparedSubTrip> {

    private static final long serialVersionUID = -4898533245331624745L;

    public SubTripSDMFactory() {
        super(RawSubTrip.class, PreparedSubTrip.class);
    }

    @Override
    public ISingleDomainModelConverter<RawSubTrip, PreparedSubTrip> createConverter(ReferenceSupplier referenceSupplier) {
        return raw -> {
            String organizationDurableKey = referenceSupplier.getGroupDurableId(raw.getSubscriptionId());

            PreparedSubTrip res = new PreparedSubTrip();
            res.setDurableId(raw.getDurableId());
            res.setOrganizationDurableKey(organizationDurableKey);
            res.setExternalId(raw.getSubTripId());
            res.setPersistedDateUtc(raw.getPersistedDateUtc());
            res.setParentTripKey(raw.getParentTripKey());
            res.setSubTripStart(raw.getSubTripStart());
            res.setStartPositionId(raw.getStartPositionId());
            res.setStartPositionTimestamp(raw.getStartPositionTimestamp());
            res.setStartPositionLongitude(raw.getStartPositionLongitude());
            res.setStartPositionLatitude(raw.getStartPositionLatitude());
            res.setStartPositionSpeedKilometresPerHour(raw.getStartPositionSpeedKilometresPerHour());
            res.setDepart(raw.getDepart());
            res.setHalt(raw.getHalt());
            res.setSubTripEnd(raw.getSubTripEnd());
            res.setEndPositionId(raw.getEndPositionId());
            res.setEndPositionTimestamp(raw.getEndPositionTimestamp());
            res.setEndPositionLongitude(raw.getEndPositionLongitude());
            res.setEndPositionLatitude(raw.getEndPositionLatitude());
            res.setDrivingTime(raw.getDrivingTime());
            res.setStandingTime(raw.getStandingTime());
            res.setDuration(raw.getDuration());
            res.setDistanceKilometres(raw.getDistanceKilometres());
            res.setStartOdometerKilometres(raw.getStartOdometerKilometres());
            res.setEndOdometerKilometres(raw.getEndOdometerKilometres());
            res.setStartEngineSeconds(raw.getStartEngineSeconds());
            res.setEndEngineSeconds(raw.getEndEngineSeconds());
            res.setEngineSeconds(raw.getEngineSeconds());
            res.setPulseValue(raw.getPulseValue());
            res.setFuelUsedLitres(raw.getFuelUsedLitres());
            res.setMaxSpeedKilometersPerHour(raw.getMaxSpeedKilometersPerHour());
            res.setMaxAccelerationKilometersPerHourPerSecond(raw.getMaxAccelerationKilometersPerHourPerSecond());
            res.setMaxDecelerationKilometersPerHourPerSecond(raw.getMaxDecelerationKilometersPerHourPerSecond());
            res.setMaxRpm(raw.getMaxRpm());
            res.setYear(raw.getYear());
            res.setWeekNumber(raw.getWeekNumber());

            return res;
        };
    }
}
