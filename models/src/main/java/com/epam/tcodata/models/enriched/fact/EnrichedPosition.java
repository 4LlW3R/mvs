package com.epam.tcodata.models.enriched.fact;

import com.epam.tcodata.models.enriched.EnrichedCommon;
import com.epam.tcodata.models.enriched.IEnrichable;
import com.epam.tcodata.models.mix.fact.Position;

import java.sql.Timestamp;

/**
 * Maps to SOAP GPSPositionV2.
 */
public class EnrichedPosition extends Position implements IEnrichable {

    private static final long serialVersionUID = -846678812704279153L;

    private EnrichedCommon enrichedCommon;

    private String driverDurableKey;
    private String vehicleDurableKey;

    public EnrichedPosition() {
        enrichedCommon = new EnrichedCommon();
    }

    /**
     * Main constructor.
     * @param position asset to construct {@link EnrichedPosition}
     */
    public EnrichedPosition(Position position) {
        enrichedCommon = new EnrichedCommon();
        this.setPositionId(position.getPositionId());
        this.setAssetId(position.getAssetId());
        this.setDriverId(position.getDriverId());
        this.setTimestamp(position.getTimestamp());
        this.setLatitude(position.getLatitude());
        this.setLongitude(position.getLongitude());
        this.setSpeedKilometresPerHour(position.getSpeedKilometresPerHour());
        this.setSpeedLimit(position.getSpeedLimit());
        this.setAltitudeMetres(position.getAltitudeMetres());
        this.setHeading(position.getHeading());
        this.setNumberOfSatellites(position.getNumberOfSatellites());
        this.setHdop(position.getHdop());
        this.setVdop(position.getVdop());
        this.setPdop(position.getPdop());
        this.setAgeOfReadingSeconds(position.getAgeOfReadingSeconds());
        this.setDistanceSinceReadingKilometres(position.getDistanceSinceReadingKilometres());
        this.setIgnitionOn(position.getIgnitionOn());
        this.setOdometerKilometres(position.getOdometerKilometres());
        this.setFormattedAddress(position.getFormattedAddress());
        this.setSource(position.getSource());
        this.setAvl(position.getAvl());
    }

    @Override
    public String getDurableId() {
        return enrichedCommon.getDurableId();
    }

    @Override
    public EnrichedPosition setDurableId(String durableId) {
        this.enrichedCommon.setDurableId(durableId);
        return this;
    }

    @Override
    public Timestamp getIngestedDateUtc() {
        return enrichedCommon.getIngestedDateUtc();
    }

    @Override
    public EnrichedPosition setIngestedDateUtc(Timestamp ingestedDateUtc) {
        this.enrichedCommon.setIngestedDateUtc(ingestedDateUtc);
        return this;
    }

    @Override
    public Long getSubscriptionId() {
        return enrichedCommon.getSubscriptionId();
    }

    @Override
    public EnrichedPosition setSubscriptionId(Long subscriptionId) {
        this.enrichedCommon.setSubscriptionId(subscriptionId);
        return this;
    }

    @Override
    public Integer getLineageCode() {
        return enrichedCommon.getLineageCode();
    }

    @Override
    public EnrichedPosition setLineageCode(Integer lineageCode) {
        this.enrichedCommon.setLineageCode(lineageCode);
        return this;
    }

    public String getDriverDurableKey() {
        return driverDurableKey;
    }

    public EnrichedPosition setDriverDurableKey(String driverDurableKey) {
        this.driverDurableKey = driverDurableKey;
        return this;
    }

    public String getVehicleDurableKey() {
        return vehicleDurableKey;
    }

    public EnrichedPosition setVehicleDurableKey(String vehicleDurableKey) {
        this.vehicleDurableKey = vehicleDurableKey;
        return this;
    }

    @Override
    public String toString() {
        return "EnrichedPosition{"
                + super.toString()
                + enrichedCommon.toString()
                + ", driverDurableKey=" + driverDurableKey
                + ", vehicleDurableKey=" + vehicleDurableKey
                + '}';
    }

    /**
     * Method provides structured fields for writing to DataLake.
     *
     * @return Object[] fields.
     */
    public Object[] getOrderedValues() {
        return new Object[] {
                getDurableId(),
                getIngestedDateUtc(),
                getSubscriptionId(),
                getLineageCode(),
                driverDurableKey,
                vehicleDurableKey,
                getTimestamp(),
                getLongitude(),
                getLatitude(),
                getDriverId(),
                getAssetId(),
                getPositionId(),
                getAvl(),
                getSource(),
                getOdometerKilometres(),
                getIgnitionOn(),
                getAgeOfReadingSeconds(),
                getPdop(),
                getVdop(),
                getHdop(),
                getNumberOfSatellites(),
                getHeading(),
                getAltitudeMetres(),
                getSpeedKilometresPerHour(),
                getDistanceSinceReadingKilometres(),
                getFormattedAddress(),
                getSpeedLimit(),
        };
    }
}
