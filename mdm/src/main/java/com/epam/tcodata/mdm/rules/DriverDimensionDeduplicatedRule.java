package com.epam.tcodata.mdm.rules;

import com.epam.tcodata.mdm.Decision;
import com.epam.tcodata.mdm.IKeyManager;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.mdm.KeyMapping;
import com.epam.tcodata.sql.dal.domain.mdm.Step;
import com.epam.tcodata.sql.dal.service.mdm.IKeyMappingService;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.epam.tcodata.sql.dal.IDaoFactory.service;

/**
 * This rule is a specific rule for drivers. It matches all drivers with equal employeeNumber as one.
 */
public class DriverDimensionDeduplicatedRule implements ICustomRuleKind {
    private static final String EMPLOYEE_NUM = "employeeNumber";

    /**
     * We need a default constructor.
     */
    public DriverDimensionDeduplicatedRule() {
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

        // natural key name (like "DriverId": 1127989754763337833) cannot be null
        String naturalKeyName = step.getNaturalKeyName();
        String naturalKey = String.valueOf(entity.get(naturalKeyName)).trim();

        // employee number (aka badge number) can be not filled
        Object employeeNumb = entity.get(EMPLOYEE_NUM);
        String employeeNumbStr = String.valueOf(employeeNumb).trim();

        IKeyMappingService mappingService = service(daoFactory, KeyMapping.class);

        Optional<KeyMapping> employeeMapping = employeeNumb == null
                ? Optional.empty()
                : mappingService.findByNaturalKey(EntityType.DRIVER, EMPLOYEE_NUM, employeeNumbStr);
        Optional<KeyMapping>  naturalMapping = mappingService.findByNaturalKey(EntityType.DRIVER, naturalKeyName, naturalKey);

        // founded mapping for natural key and for badge number, it they exist
        KeyMapping natural = naturalMapping.orElse(null);
        KeyMapping employee = employeeMapping.orElse(null);

        // lets compare badge numbers at first
        if (employeeMapping.isPresent()) {
            if (naturalMapping.isPresent()) {
                if (Objects.equals(natural.getSurrogateKey(), employee.getSurrogateKey())) {
                    // best match
                    return Decision.foundKey(naturalKey, UUID.fromString(employee.getSurrogateKey()), natural.getId(), natural.isSpeculative());
                } else {
                    // the same badge but different natural key - renew it
                    return Decision.renewKey(naturalKey, UUID.fromString(employee.getSurrogateKey()), natural.getId());
                }
            } else {
                // it's a new double by badge number
                return Decision.newKey(naturalKey, UUID.fromString(employee.getSurrogateKey()));
            }
        } else {
            if (naturalMapping.isPresent()) {
                // badges are different (or maybe null - check it)
                if (employeeNumb == null) {
                    // it is the same driver but without badge
                    return Decision.foundKey(naturalKey, UUID.fromString(natural.getSurrogateKey()), natural.getId(), natural.isSpeculative());
                } else {
                    // it is the same driver but with different badge, just register new badge
                    KeyMapping newEmployeeMapping = new KeyMapping(EntityType.DRIVER, EMPLOYEE_NUM, employeeNumbStr, natural.getSurrogateKey());
                    mappingService.insert(newEmployeeMapping);

                    return Decision.foundKey(naturalKey, UUID.fromString(natural.getSurrogateKey()), natural.getId(), natural.isSpeculative());
                }
            }
        }
        // it is totally new driver ...
        if (employeeNumb != null) {
            // ... but we need to register his badge
            UUID newSurrogateKey = keyManager.newDurableKey(EntityType.DRIVER, naturalKey);
            KeyMapping newEmployeeMapping = new KeyMapping(EntityType.DRIVER, EMPLOYEE_NUM, employeeNumbStr, newSurrogateKey.toString());
            mappingService.insert(newEmployeeMapping);
        }
        return Decision.noneKey();
    }
}
