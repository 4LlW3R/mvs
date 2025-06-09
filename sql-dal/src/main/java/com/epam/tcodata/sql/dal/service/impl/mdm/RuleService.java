package com.epam.tcodata.sql.dal.service.impl.mdm;

import com.epam.tcodata.sql.dal.DatabaseConfig;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.mdm.Rule;
import com.epam.tcodata.sql.dal.service.AbstractReadWriteService;
import com.epam.tcodata.sql.dal.service.CRUD;
import com.epam.tcodata.sql.dal.service.mdm.IRuleService;

/**
 * An implementation of service for Rule entity.
 */
public class RuleService extends AbstractReadWriteService<Rule> implements IRuleService {

    /**
     * Public main constructor.
     */
    public RuleService(IDaoFactory factory) {
        super(factory, DatabaseConfig.MDM, "key_rule",
                new CRUD(),
                Rule.class);
    }

}
