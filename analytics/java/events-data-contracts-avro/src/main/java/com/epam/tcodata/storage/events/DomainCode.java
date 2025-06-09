package com.epam.tcodata.storage.events;

/**
 * Decodes the domain of the event. The domain consists of two parts:
 * - general source (e.g. 'analytics', 'external', 'guardian' etc)
 * - specific producer (e.g. 'overtaking', 'congestion', 'amber' etc)
 */
public enum DomainCode {

    /**
     * Domain for all overtaking detection related events, enriched or not.
     */
    DOMAIN_OVERTAKING("analytics.overtaking"),
    DOMAIN_PROXIMITY("analytics.proximity"),
    DOMAIN_TRAJECTORY("analytics.trajectory"),
    DOMAIN_ROAD_CONDITION("analytics.violations.road.condition");

    private final String domain;

    DomainCode(String domain) {
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }
}
