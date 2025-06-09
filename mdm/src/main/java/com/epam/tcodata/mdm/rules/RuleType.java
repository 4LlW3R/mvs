package com.epam.tcodata.mdm.rules;

import com.epam.tcodata.mdm.Decision;
import com.epam.tcodata.mdm.IKeyManager;
import com.epam.tcodata.models.EntityType;
import com.epam.tcodata.sql.dal.IDaoFactory;
import com.epam.tcodata.sql.dal.domain.mdm.Step;

import java.util.Map;

/**
 * Static hierarchy that has set of possible rules.
 */
public enum RuleType implements IRuleType {

    /**
     * Dummy rule.
     */
    NONE((k, f, t, e, s) -> Decision.noneKey()),

    /**
     * Custom rule based on body as a class name of real worker class.
     */
    CUSTOM((k, f, t, e, s) -> {
        try {
            Class<?> clazz = Class.forName(s.getBody());
            if (ICustomRuleKind.class.isAssignableFrom(clazz)) {
                ICustomRuleKind kind = (ICustomRuleKind) clazz.getDeclaredConstructor().newInstance();
                return kind.performDecision(k, f, t, e, s);
            }
        }
        catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
        return Decision.noneKey();
    }),

    /**
     * Generates random UUID anyway.
     */
    GENERATE((k, f, t, e, s) -> {
        String keyName = s.getNaturalKeyName();
        String keyValue = String.valueOf(e.get(keyName));
        return Decision.newKey(keyValue, k.newDurableKey(t, keyValue));
    });

    private IRuleType ruleTypeVar;

    @Override
    public Decision performDecision(IKeyManager keyManager,
                                    IDaoFactory daoFactory,
                                    EntityType entityType,
                                    Map<String, Object> entity,
                                    Step step) {

        return this.ruleTypeVar.performDecision(keyManager, daoFactory, entityType, entity, step);
    }

    RuleType(IRuleType ruleTypeVar) {
        this.ruleTypeVar = ruleTypeVar;
    }

}
