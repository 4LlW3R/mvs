package com.epam.tcodata.sql.dal.service.mdm;

import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.sql.dal.domain.mdm.KeyMapping;
import com.epam.tcodata.sql.dal.service.IReadWriteService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IKeyMappingService extends IReadWriteService<KeyMapping> {

    Optional<KeyMapping> findByNaturalKey(EntityType entityType, String naturalKeyName, String naturalKeyValue);

    List<KeyMapping> findByNaturalKey(EntityType entityType, String naturalKeyName, Set<String> naturalKeyValues);
}
