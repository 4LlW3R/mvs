package com.epam.tcodata.models.enriched.dimension;

import com.epam.tcodata.models.enriched.EnrichedCommon;
import com.epam.tcodata.models.enriched.IEnrichable;
import com.epam.tcodata.models.mix.dimension.Driver;

import java.sql.Timestamp;

/**
 * Maps to SOAP EnrichedDriver.
 */
public class EnrichedDriver extends Driver implements IEnrichable {

    private static final long serialVersionUID = -3682439558201071861L;

    private EnrichedCommon enrichedCommon;

    public EnrichedDriver() {
        enrichedCommon = new EnrichedCommon();
    }

    /**
     * Main constructor.
     * @param driver asset to construct {@link EnrichedDriver}
     */
    public EnrichedDriver(Driver driver) {
        this.enrichedCommon = new EnrichedCommon();
        this.setSiteId(driver.getSiteId());
        this.setDriverId(driver.getDriverId());
        this.setName(driver.getName());
        this.setImageUri(driver.getImageUri());
        this.setFmDriverId(driver.getFmDriverId());
        this.setEmployeeNumber(driver.getEmployeeNumber());
        this.setSystemDriver(driver.getSystemDriver());
        this.setMobileNumber(driver.getMobileNumber());
        this.setEmail(driver.getEmail());
        this.setExtendedDriverId(driver.getExtendedDriverId());
        this.setExtendedDriverIdType(driver.getExtendedDriverIdType());
        this.setCountry(driver.getCountry());
    }

    @Override
    public String getDurableId() {
        return enrichedCommon.getDurableId();
    }

    @Override
    public EnrichedDriver setDurableId(String durableId) {
        this.enrichedCommon.setDurableId(durableId);
        return this;
    }

    @Override
    public Timestamp getIngestedDateUtc() {
        return enrichedCommon.getIngestedDateUtc();
    }

    @Override
    public EnrichedDriver setIngestedDateUtc(Timestamp ingestedDateUtc) {
        this.enrichedCommon.setIngestedDateUtc(ingestedDateUtc);
        return this;
    }

    @Override
    public Long getSubscriptionId() {
        return enrichedCommon.getSubscriptionId();
    }

    @Override
    public EnrichedDriver setSubscriptionId(Long subscriptionId) {
        this.enrichedCommon.setSubscriptionId(subscriptionId);
        return this;
    }

    @Override
    public Integer getLineageCode() {
        return enrichedCommon.getLineageCode();
    }

    @Override
    public EnrichedDriver setLineageCode(Integer lineageCode) {
        this.enrichedCommon.setLineageCode(lineageCode);
        return this;
    }

    @Override
    public String toString() {
        return "EnrichedDriver{"
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
                getSiteId(),
                getDriverId(),
                getName(),
                getImageUri(),
                getFmDriverId(),
                getEmployeeNumber(),
                getSystemDriver(),
                getMobileNumber(),
                getEmail(),
                getExtendedDriverId(),
                getExtendedDriverIdType(),
                getCountry()
        };
    }
}
