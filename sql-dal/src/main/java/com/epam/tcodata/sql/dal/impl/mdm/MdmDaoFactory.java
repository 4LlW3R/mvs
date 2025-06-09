package com.epam.tcodata.sql.dal.impl.mdm;

import com.epam.tcodata.secure.storage.dal.ISecretStorage;
import com.epam.tcodata.sql.dal.AbstractDaoFactory;
import com.epam.tcodata.sql.dal.domain.mdm.KeyMapping;
import com.epam.tcodata.sql.dal.domain.mdm.Relation;
import com.epam.tcodata.sql.dal.domain.mdm.Rule;
import com.epam.tcodata.sql.dal.domain.mdm.Step;
import com.epam.tcodata.sql.dal.service.impl.mdm.KeyMappingService;
import com.epam.tcodata.sql.dal.service.impl.mdm.RelationService;
import com.epam.tcodata.sql.dal.service.impl.mdm.RuleService;
import com.epam.tcodata.sql.dal.service.impl.mdm.StepService;

public class MdmDaoFactory extends AbstractDaoFactory {

    /**
     * Main pubic constructor.
     */
    public MdmDaoFactory(ISecretStorage secretStorage) {
        super(secretStorage);

        register(Rule.class, new RuleService(this));
        register(Step.class, new StepService(this));
        register(Relation.class, new RelationService(this));
        register(KeyMapping.class, new KeyMappingService(this));
    }
}
