package com.epam.tcodata.raw.prepared.etl.factory.impl;

import com.epam.tcodata.models.datalake.prepared.fact.PreparedTrip;
import com.epam.tcodata.models.datalake.raw.fact.RawTrip;
import com.epam.tcodata.raw.prepared.etl.ReferenceSupplier;
import com.epam.tcodata.raw.prepared.etl.converter.ISingleDomainModelConverter;
import com.epam.tcodata.raw.prepared.etl.factory.AbstractSDMFactory;

public class TripSDMFactory extends AbstractSDMFactory<RawTrip, PreparedTrip> {

    private static final long serialVersionUID = 5638778980418044610L;

    public TripSDMFactory() {
        super(RawTrip.class, PreparedTrip.class);
    }

    @Override
    public ISingleDomainModelConverter<RawTrip, PreparedTrip> createConverter(ReferenceSupplier referenceSupplier) {
        return raw -> {
            String durableId = referenceSupplier.getGroupDurableId(raw.getSubscriptionId());

            PreparedTrip res = new PreparedTrip();
            res.setDurableId(raw.getDurableId());
            res.setOrganizationDurableKey(durableId);
            res.setExternalId(raw.getTripId());
            res.setPersistedDateUtc(raw.getPersistedDateUtc());
            res.setDriverDurableKey(raw.getDriverDurableKey());
            res.setVehicleDurableKey(raw.getVehicleDurableKey());
            res.setAssetId(raw.getAssetId());
            res.setDriverId(raw.getDriverId());
            res.setTripStart(raw.getTripStart());
            res.setTripEnd(raw.getTripEnd());
            res.setNotes(raw.getNotes());
            res.setPulseParameterName(raw.getPulseParameterName());
            res.setEngineSeconds(raw.getEngineSeconds());
            res.setStartPositionId(raw.getStartPositionId());
            res.setStartPositionTimestamp(raw.getStartPositionTimestamp());
            res.setStartPositionLongitude(raw.getStartPositionLongitude());
            res.setStartPositionLatitude(raw.getStartPositionLatitude());
            res.setStartPositionSpeedKilometresPerHour(raw.getStartPositionSpeedKilometresPerHour());
            res.setEndPositionId(raw.getEndPositionId());
            res.setEndPositionTimestamp(raw.getEndPositionTimestamp());
            res.setEndPositionLongitude(raw.getEndPositionLongitude());
            res.setEndPositionLatitude(raw.getEndPositionLatitude());
            res.setFirstDepart(raw.getFirstDepart());
            res.setLastHalt(raw.getLastHalt());
            res.setDrivingTime(raw.getDrivingTime());
            res.setStandingTime(raw.getStandingTime());
            res.setDuration(raw.getDuration());
            res.setDistanceKilometers(raw.getDistanceKilometers());
            res.setStartOdometerKilometers(raw.getStartOdometerKilometers());
            res.setEndOdometerKilometers(raw.getEndOdometerKilometers());
            res.setStartEngineSeconds(raw.getStartEngineSeconds());
            res.setEndEngineSeconds(raw.getEndEngineSeconds());
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
