package com.epam.tcodata.sql.dal.service.impl.mdm;

import com.epam.tcodata.sql.dal.DatabaseConfig;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.mdm.Relation;
import com.epam.tcodata.sql.dal.service.AbstractReadWriteService;
import com.epam.tcodata.sql.dal.service.CRUD;
import com.epam.tcodata.sql.dal.service.mdm.IRelationService;

/**
 * An implementation of service for Relation entity.
 */
public class RelationService extends AbstractReadWriteService<Relation> implements IRelationService {

    /**
     * Public main constructor.
     */
    public RelationService(IDaoFactory factory) {
        super(factory, DatabaseConfig.MDM, "key_rule_relations",
                new CRUD(),
                Relation.class);
    }


}
