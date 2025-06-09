package com.epam.tcodata.sql.dal.service.impl.pumps;

import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.sql.dal.DatabaseConfig;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.pumps.MixOffset;
import com.epam.tcodata.sql.dal.impl.pumps.PumpQueries;
import com.epam.tcodata.sql.dal.service.AbstractReadWriteService;
import com.epam.tcodata.sql.dal.service.CRUD;
import com.epam.tcodata.sql.dal.service.pumps.IMixOffsetService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class MixOffsetService extends AbstractReadWriteService<MixOffset> implements IMixOffsetService<MixOffset> {

    private static final String ORGANISATION_GROUP_ID = "OrganisationGroupId";
    private static final String ENTITY_TYPE = "EntityType";

    /**
     * Public main constructor. Queries can be customized via crud builder.
     */
    public MixOffsetService(IDaoFactory daoFactory) {
        super(daoFactory,
                DatabaseConfig.PUMPS, "MixOffset",
                new CRUD(),
                MixOffset.class);
    }

    @Override
    public Map<Long, Optional<MixOffset>> readMixOffsetMap(Set<Long> orgGroupIdList, EntityType typeCode) {
        Map<Long, Optional<MixOffset>> trackedEntityEPMap = new HashMap<>();
        for (Long orgGroupId : orgGroupIdList) {
            trackedEntityEPMap.put(
                    orgGroupId,
                    openHandle().createQuery(PumpQueries.SELECT_MIX_OFFSET_BY_GROUP_ID_AND_ENTITY_TYPE_CODE.query())
                            .bind(ORGANISATION_GROUP_ID, orgGroupId)
                            .bind(ENTITY_TYPE, typeCode.getCode())
                            .mapToBean(MixOffset.class)
                            .findFirst());
        }
        return trackedEntityEPMap;
    }
}
