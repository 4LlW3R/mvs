package com.epam.tcodata.models.enriched.dimension;

import com.epam.tcodata.models.enriched.EnrichedCommon;
import com.epam.tcodata.models.enriched.IEnrichable;
import com.epam.tcodata.models.mix.dimension.Asset;

import java.sql.Timestamp;

/**
 * Maps to SOAP Vehicle.
 */
public class EnrichedAsset extends Asset implements IEnrichable {

    private static final long serialVersionUID = 1700409866355920612L;

    private EnrichedCommon enrichedCommon;

    public EnrichedAsset() {
        enrichedCommon = new EnrichedCommon();
    }

    /**
     * Main constructor.
     * @param asset asset to construct {@link EnrichedAsset}
     */
    public EnrichedAsset(Asset asset) {
        this.enrichedCommon = new EnrichedCommon();
        this.setAssetId(asset.getAssetId());
        this.setAssetTypeId(asset.getAssetTypeId());
        this.setDescription(asset.getDescription());
        this.setConnectedTrailer(asset.getConnectedTrailer());
        this.setRegistrationNumber(asset.getRegistrationNumber());
        this.setSiteId(asset.getSiteId());
        this.setFuelType(asset.getFuelType());
        this.setTargetFuelConsumption(asset.getTargetFuelConsumption());
        this.setTargetFuelConsumptionUnits(asset.getTargetFuelConsumptionUnits());
        this.setTargetHourlyFuelConsumption(asset.getTargetHourlyFuelConsumption());
        this.setTargetHourlyFuelConsumptionUnits(asset.getTargetHourlyFuelConsumptionUnits());
        this.setFleetNumber(asset.getFleetNumber());
        this.setMake(asset.getMake());
        this.setModel(asset.getModel());
        this.setYear(asset.getYear());
        this.setVinNumber(asset.getVinNumber());
        this.setEngineNumber(asset.getEngineNumber());
        this.setFmVehicleId(asset.getFmVehicleId());
        this.setAdditionalMobileDevice(asset.getAdditionalMobileDevice());
        this.setNotes(asset.getNotes());
        this.setIcon(asset.getIcon());
        this.setIconColour(asset.getIconColour());
        this.setColour(asset.getColour());
        this.setAssetImage(asset.getAssetImage());
        this.setDefaultImage(asset.getDefaultImage());
        this.setAssetImageUrl(asset.getAssetImageUrl());
        this.setUserState(asset.getUserState());
        this.setCreatedBy(asset.getCreatedBy());
        this.setCreatedDate(asset.getCreatedDate());
        this.setOdometer(asset.getOdometer());
        this.setEngineHours(asset.getEngineHours());
        this.setCountry(asset.getCountry());
    }

    @Override
    public String getDurableId() {
        return enrichedCommon.getDurableId();
    }

    @Override
    public EnrichedAsset setDurableId(String durableId) {
        this.enrichedCommon.setDurableId(durableId);
        return this;
    }

    @Override
    public Timestamp getIngestedDateUtc() {
        return enrichedCommon.getIngestedDateUtc();
    }

    @Override
    public EnrichedAsset setIngestedDateUtc(Timestamp ingestedDateUtc) {
        this.enrichedCommon.setIngestedDateUtc(ingestedDateUtc);
        return this;
    }

    @Override
    public Long getSubscriptionId() {
        return enrichedCommon.getSubscriptionId();
    }

    @Override
    public EnrichedAsset setSubscriptionId(Long subscriptionId) {
        this.enrichedCommon.setSubscriptionId(subscriptionId);
        return this;
    }

    @Override
    public Integer getLineageCode() {
        return enrichedCommon.getLineageCode();
    }

    @Override
    public EnrichedAsset setLineageCode(Integer lineageCode) {
        this.enrichedCommon.setLineageCode(lineageCode);
        return this;
    }

    @Override
    public String toString() {
        return "EnrichedAsset{"
                + "enrichedCommon=" + enrichedCommon
                + "} " + super.toString();
    }

    /**
     * Method provides structed fields for writing to datalake.
     *
     * @return Object[] fields.
     */
    public Object[] getOrderedValues() {
        return new Object[] {
                getDurableId(),
                getIngestedDateUtc(),
                getSubscriptionId(),
                getLineageCode(),
                getAssetId(),
                getAssetTypeId(),
                getDescription(),
                getConnectedTrailer(),
                getRegistrationNumber(),
                getSiteId(),
                getFuelType(),
                getTargetFuelConsumption(),
                getTargetFuelConsumptionUnits(),
                getTargetHourlyFuelConsumption(),
                getTargetHourlyFuelConsumptionUnits(),
                getFleetNumber(),
                getMake(),
                getModel(),
                getYear(),
                getVinNumber(),
                getEngineNumber(),
                getFmVehicleId(),
                getAdditionalMobileDevice(),
                getNotes(),
                getIcon(),
                getIconColour(),
                getColour(),
                getAssetImage(),
                getDefaultImage(),
                getAssetImageUrl(),
                getUserState(),
                getCreatedBy(),
                getCreatedDate(),
                getOdometer(),
                getEngineHours(),
                getCountry(),
        };
    }
}
