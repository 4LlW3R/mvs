package com.epam.tcodata.sql.dal.service.impl.mdm;

import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.sql.dal.DatabaseConfig;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.mdm.KeyMapping;
import com.epam.tcodata.sql.dal.impl.mdm.MdmQueries;
import com.epam.tcodata.sql.dal.repository.BaseReadWriteRepository;
import com.epam.tcodata.sql.dal.service.AbstractReadWriteService;
import com.epam.tcodata.sql.dal.service.CRUD;
import com.epam.tcodata.sql.dal.service.mdm.IKeyMappingService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * An implementation of service for KeyMapping entity.
 */
public class KeyMappingService extends AbstractReadWriteService<KeyMapping> implements IKeyMappingService {


    /**
     * Public main constructor.
     */
    public KeyMappingService(IDaoFactory factory) {
        super(factory, DatabaseConfig.MDM, "key_mapping",
                new CRUD(),
                KeyMapping.class);
    }

    /**
     * An example of a special operation for only IKeyMappingService.
     */
    @Override
    public Optional<KeyMapping> findByNaturalKey(EntityType entityType, String naturalKeyName, String naturalKeyValue) {
        return openHandle().createQuery(MdmQueries.KEY_MAPPING_SELECT.query())
                .define(BaseReadWriteRepository.TABLE, super.repository.tableName())
                .bind(KeyMapping.Fields.ENTITY, entityType)
                .bind(KeyMapping.Fields.KEY_NAME, naturalKeyName)
                .bind(KeyMapping.Fields.NATURAL_KEY, naturalKeyValue)
                .mapTo(KeyMapping.class)
                .findFirst();
    }

    @Override
    public List<KeyMapping> findByNaturalKey(EntityType entityType, String naturalKeyName, Set<String> naturalKeyValues) {
        List<KeyMapping> keyMappers = openHandle().createQuery(MdmQueries.KEYS_MAPPING_SELECT.query())
                .define(BaseReadWriteRepository.TABLE, super.repository.tableName())
                .bind(KeyMapping.Fields.ENTITY, entityType)
                .bind(KeyMapping.Fields.KEY_NAME, naturalKeyName)
                .bindList(KeyMapping.Fields.NATURAL_KEY, new ArrayList<>(naturalKeyValues))
                .mapTo(KeyMapping.class)
                .collect(Collectors.toList());

        return keyMappers;
    }
}

