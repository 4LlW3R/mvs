package com.epam.tcodata.sql.dal.service.impl.mdm;

import com.epam.tcodata.sql.dal.DatabaseConfig;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.mdm.Step;
import com.epam.tcodata.sql.dal.service.AbstractReadWriteService;
import com.epam.tcodata.sql.dal.service.CRUD;
import com.epam.tcodata.sql.dal.service.mdm.IStepService;

/**
 * An implementation of service for Step entity.
 */
public class StepService extends AbstractReadWriteService<Step> implements IStepService {

    /**
     * Public main constructor.
     */
    public StepService(IDaoFactory factory) {
        super(factory, DatabaseConfig.MDM, "key_rule_steps",
                new CRUD(),
                Step.class);
    }


}
