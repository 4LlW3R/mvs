package com.epam.tcodata.mdm.base.impl;

import com.epam.tcodata.mdm.Decision;
import com.epam.tcodata.mdm.base.AbstractKeyManager;
import com.epam.tcodata.mdm.base.IMapper;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.models.mdm.AssigningReason;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.mdm.KeyManagerVersion;
import com.epam.tcodata.sql.dal.domain.mdm.KeyMapping;
import com.epam.tcodata.sql.dal.service.mdm.IKeyMappingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static com.epam.tcodata.sql.dal.IDaoFactory.service;

/**
 * Implementation of IKeyManager.
 */
public class KeyManager extends AbstractKeyManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(KeyManager.class);

    private Map<EntityType, IMapper> mappers = new HashMap<>();

    private IKeyMappingService mappingService;

    /**
     * Main public constructor.
     *
     * @param factory    - an instance of IDaoFactory for storing and restoring needed data.
     */
    public KeyManager(KeyManagerVersion version, IDaoFactory factory) {
        super(version, factory);

        for (EntityType kind : EntityType.values()) {
            this.mappers.put(kind, new ReflectionMapper());
        }

        this.mappingService = service(factory, KeyMapping.class);
    }

    @Override
    protected Map<DalRequestData, UUID> findByNaturalKey(List<DalRequestData> dalRequestDataList) {
        Map<DalRequestData, UUID> res = new HashMap<>();
        if (dalRequestDataList == null || dalRequestDataList.isEmpty()) {
            return res;
        }

        // Just get first. It should be a single entity for each.
        EntityType entityType = dalRequestDataList.get(0).getDimEntityType();
        String naturalKeyName = dalRequestDataList.get(0).getNaturalKeyName();
        Set<String> naturalKeys = dalRequestDataList.stream()
                .map(entity -> entity.getNaturalKey()).collect(Collectors.toSet());

        // Request for all entities.
        List<KeyMapping> founds = mappingService.findByNaturalKey(entityType, naturalKeyName, naturalKeys);

        // Construct result.
        // The map is needed just for quick access naturalKey -> KeyMapping. See code after.
        Map<String, KeyMapping> naturalKeysMap = new HashMap<>();
        founds.forEach(found -> naturalKeysMap.put(found.getNaturalKey(), found));

        dalRequestDataList.forEach(dalRequestData -> {
            String naturalKey = dalRequestData.getNaturalKey();
            KeyMapping keyMappingForSurrogateKey = naturalKeysMap.get(naturalKey);
            UUID uuid = keyMappingForSurrogateKey == null ? null : UUID.fromString(keyMappingForSurrogateKey.getSurrogateKey());
            res.put(dalRequestData, uuid);
        });

        return res;
    }

    @Override
    protected IMapper giveMapper(EntityType kind) {
        return this.mappers.getOrDefault(kind, o -> Collections.emptyMap());
    }

    @Override
    protected void registerDecision(Decision decision, EntityType kind, String naturalKeyName, Date date) {
        try {
            this.mappingService.beginTransaction();

            KeyMapping keyMapping = new KeyMapping();
            keyMapping.setEntity(kind);
            keyMapping.setKeyName(naturalKeyName);
            keyMapping.setNaturalKey(decision.getKey());
            keyMapping.setSurrogateKey(String.valueOf(decision.getSurrogateKey()));
            keyMapping.setSpeculative(decision.getAssigningReason() == AssigningReason.SPECULATIVE_KEY);
            switch (decision.getAssigningReason()) {
                case INITIAL_KEY:
                case NEW_KEY:
                    this.mappingService.insert(keyMapping);
                    break;

                case SPECULATIVE_KEY:
                    keyMapping.setSpeculative(true);
                    this.mappingService.insert(keyMapping);
                    break;

                case RENEW_KEY:
                    keyMapping.setId(decision.getKeyMappingId());
                    this.mappingService.update(keyMapping);
                    break;

                case NONE:
                case FOUND_KEY:
                default:    // do nothing
            }

            this.mappingService.commitTransaction();

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            this.mappingService.rollbackTransaction();
        }
    }
}
