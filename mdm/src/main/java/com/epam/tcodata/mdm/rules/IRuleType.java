package com.epam.tcodata.mdm.rules;

import com.epam.tcodata.mdm.Decision;
import com.epam.tcodata.mdm.IKeyManager;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.mdm.Step;

import java.util.Map;

@FunctionalInterface
public interface IRuleType {
    /**
     * Make a decision about assigning durable key. The work itself is performed by rules, that implement
     * this interface.
     *
     * @param keyManager    key manager that uses this rule
     * @param daoFactory    DAO factory to access to the storage
     * @param entityType    entity type
     * @param entity        incoming parsed raw object
     * @param step          all parameters of current step
     * @return
     */
    Decision performDecision(
            IKeyManager keyManager,
            IDaoFactory daoFactory,
            EntityType entityType,
            Map<String, Object> entity,
            Step step
    );
}
