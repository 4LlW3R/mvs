package com.epam.tcodata.event.validator.logic;


import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRuleChain implements IRuleChain {

    private List<IRule> rules = new ArrayList<>();


    /**
     * Add rule to chain.
     * @param rule specified rule.
     */
    public IRuleChain addRule(IRule rule) {
        rules.add(rule);
        return this;
    }

    @Override
    public int apply() {
        for (IRule rule: this.rules) {
            if (rule.isApplied()) {
                return rule.apply();
            }
        }
        // TO DO add default rule.
        return 1;
    }

    @Override
    public boolean isApplied() {
        for (IRule rule: this.rules) {
            if (rule.isApplied()) {
                return true;
            }
        }
        return false;
    }

    public List<IRule> getRules() {
        return rules;
    }

    public void setRules(List<IRule> rules) {
        this.rules = rules;
    }
}
