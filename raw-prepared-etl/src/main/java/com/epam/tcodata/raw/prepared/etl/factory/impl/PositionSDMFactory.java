package com.epam.tcodata.raw.prepared.etl.factory.impl;

import com.epam.tcodata.models.datalake.prepared.fact.PreparedPosition;
import com.epam.tcodata.models.datalake.raw.fact.RawPosition;
import com.epam.tcodata.raw.prepared.etl.ReferenceSupplier;
import com.epam.tcodata.raw.prepared.etl.converter.ISingleDomainModelConverter;
import com.epam.tcodata.raw.prepared.etl.factory.AbstractSDMFactory;

public class PositionSDMFactory extends AbstractSDMFactory<RawPosition, PreparedPosition> {

    private static final long serialVersionUID = 6158883511519565639L;

    public PositionSDMFactory() {
        super(RawPosition.class, PreparedPosition.class);
    }

    @Override
    public ISingleDomainModelConverter<RawPosition, PreparedPosition> createConverter(ReferenceSupplier referenceSupplier) {
        return raw -> {
            String durableId = referenceSupplier.getGroupDurableId(raw.getSubscriptionId());

            PreparedPosition res = new PreparedPosition();
            res.setDurableId(raw.getDurableId());
            res.setOrganizationDurableKey(durableId);
            res.setExternalId(raw.getPositionId());
            res.setPersistedDateUtc(raw.getPersistedDateUtc());
            res.setDriverDurableKey(raw.getDriverDurableKey());
            res.setVehicleDurableKey(raw.getVehicleDurableKey());
            res.setTimestamp(raw.getTimestamp());
            res.setLongitude(raw.getLongitude());
            res.setLatitude(raw.getLatitude());
            res.setDriverId(raw.getDriverId());
            res.setAssetId(raw.getAssetId());
            res.setAvl(raw.getAvl());
            res.setSource(raw.getSource());
            res.setOdometerKilometres(raw.getOdometerKilometres());
            res.setIgnitionOn(raw.getIgnitionOn());
            res.setAgeOfReadingSeconds(raw.getAgeOfReadingSeconds());
            res.setPdop(raw.getPdop());
            res.setVdop(raw.getVdop());
            res.setHdop(raw.getHdop());
            res.setNumberOfSatellites(raw.getNumberOfSatellites());
            res.setHeading(raw.getHeading());
            res.setAltitudeMetres(raw.getAltitudeMetres());
            res.setSpeedKilometresPerHour(raw.getSpeedKilometresPerHour());
            res.setDistanceSinceReadingKilometres(raw.getDistanceSinceReadingKilometres());
            res.setFormattedAddress(raw.getFormattedAddress());
            res.setSpeedLimit(raw.getSpeedLimit());
            res.setYear(raw.getYear());
            res.setWeekNumber(raw.getWeekNumber());

            return res;
        };
    }
}
