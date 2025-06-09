package com.epam.tcodata.models.enriched;

import java.io.Serializable;
import java.sql.Timestamp;

public interface IEnrichable extends Serializable {

    String getDurableId();

    IEnrichable setDurableId(String durableId);

    Timestamp getIngestedDateUtc();

    IEnrichable setIngestedDateUtc(Timestamp ingestedDateUtc);

    Long getSubscriptionId();

    IEnrichable setSubscriptionId(Long subscriptionId);

    Integer getLineageCode();

    IEnrichable setLineageCode(Integer lineageCode);
}
