package com.epam.tcodata.analytics.overtaking.violation.detection.policies.overtaking;

import com.epam.tcodata.analytics.overtaking.violation.detection.driver.repositories.AvroUtils;
import com.epam.tcodata.analytics.overtaking.violation.detection.entities.OvertakingMetaData;
import com.epam.tcodata.analytics.overtaking.violation.detection.entities.Violation;
import com.epam.tcodata.analytics.overtaking.violation.detection.policies.IPolicy;
import com.epam.tcodata.storage.avro.entities.events.overtaking.OvertakingEventWithViolationsAvro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OvertakingPolicyContainer implements Serializable {
    private static final long serialVersionUID = -5987358116160666235L;
    private Set<IPolicy<OvertakingMetaData>> activePolicies = new HashSet<>();

    public OvertakingPolicyContainer() {
        /***  Default implementation ***/
    }


    public void registerPolicy(IPolicy<OvertakingMetaData> policy) {
        activePolicies.add(policy);
    }

    public void unregisterPolicy(IPolicy<OvertakingMetaData> policy) {
        activePolicies.remove(policy);
    }

    /**
     * Apply all currently registered policies to an {@link com.epam.tcodata.storage.avro.entities.events.overtaking.OvertakingEventAvro}.
     *
     * @param overtakingMetaData - {@link com.epam.tcodata.storage.avro.entities.events.overtaking.OvertakingEventAvro}.
     * @return - {@link OvertakingEventWithViolationsAvro}
     */
    public OvertakingEventWithViolationsAvro applyActivePolicies(final OvertakingMetaData overtakingMetaData) {
        List<Violation> violations = new ArrayList<>();
        activePolicies.stream()
                .map(policy -> policy.applyPolicy(overtakingMetaData))
                .filter(Violation::filterViolations)
                .forEach(violations::add);
        return AvroUtils.enrichWithViolations(overtakingMetaData.getEvent(), violations);
    }

    public Set<IPolicy<OvertakingMetaData>> getActivePolicies() {
        return activePolicies;
    }

    public void setActivePolicies(Set<IPolicy<OvertakingMetaData>> activePolicies) {
        this.activePolicies = new HashSet<>(activePolicies);
    }
}
