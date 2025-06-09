package com.epam.tcodata.mdm.rules;

import com.epam.tcodata.mdm.Decision;
import com.epam.tcodata.mdm.IKeyManager;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.mdm.KeyMapping;
import com.epam.tcodata.sql.dal.domain.mdm.Step;
import com.epam.tcodata.sql.dal.service.mdm.IKeyMappingService;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.epam.tcodata.sql.dal.IDaoFactory.service;

public class LibraryEventDimensionRule implements ICustomRuleKind {

    /**
     * We need a default constructor.
     */
    public LibraryEventDimensionRule() {
        /***  Default implementation ***/
    }

    /**
     * It is quite naive implementation such method for DriverDimension.

     *
     * @param daoFactory - factory
     * @param entity     - coming entity
     * @param step step
     * @return the decision.
     */
    @Override
    public Decision performDecision(IKeyManager keyManager, IDaoFactory daoFactory, EntityType entityType,
                                    Map<String, Object> entity, Step step) {

        String naturalKeyName = step.getNaturalKeyName();
        String naturalKey = String.valueOf(entity.get(naturalKeyName));
        IKeyMappingService mappingService = service(daoFactory, KeyMapping.class);
        Optional<KeyMapping>  surrogateKey = mappingService.findByNaturalKey(EntityType.LIBRARY_EVENT, naturalKeyName, naturalKey);

        if (surrogateKey.isPresent()) {
            KeyMapping key = surrogateKey.get();
            return Decision.foundKey(naturalKey, UUID.fromString(key.getSurrogateKey()), key.getId(), key.isSpeculative());
        }

        return Decision.noneKey();
    }
}
