package com.epam.tcodata.analytics.overtaking.violation.detection.policies;

import com.epam.tcodata.analytics.overtaking.violation.detection.entities.Violation;

import java.io.Serializable;

public interface IPolicy<T extends Serializable> extends Serializable {

    /**
     * Applies given policy to some kind of entity (overtaking event, GPS position, etc).
     *
     * @param entity - entity to which this policy should be applied.
     * @return Integer ID of violation, if any. There is special ID for no violation.
     */
    Violation applyPolicy(T entity);
}
