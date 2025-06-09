package com.epam.tcodata.models.enriched;

import java.io.Serializable;
import java.sql.Timestamp;

public class EnrichedCommon implements Serializable {

    private static final long serialVersionUID = -8830511463214683296L;

    private String durableId;
    private Timestamp ingestedDateUtc;
    private Long subscriptionId;
    private Integer lineageCode;

    public String getDurableId() {
        return durableId;
    }

    public void setDurableId(String durableId) {
        this.durableId = durableId;
    }

    public Timestamp getIngestedDateUtc() {
        return ingestedDateUtc;
    }

    public void setIngestedDateUtc(Timestamp ingestedDateUtc) {
        this.ingestedDateUtc = ingestedDateUtc;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Integer getLineageCode() {
        return lineageCode;
    }

    public void setLineageCode(Integer lineageCode) {
        this.lineageCode = lineageCode;
    }

    @Override
    public String toString() {
        return "EnrichedCommon{"
                + "durableId='" + durableId + '\''
                + ", ingestedDateUtc=" + ingestedDateUtc
                + ", subscriptionId=" + subscriptionId
                + ", lineageCode=" + lineageCode
                + '}';
    }
}
