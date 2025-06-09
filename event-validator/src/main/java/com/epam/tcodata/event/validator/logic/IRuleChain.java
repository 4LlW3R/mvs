package com.epam.tcodata.event.validator.logic;


/**
 * Chain of rules. It's something like complex rule.
 */
public interface IRuleChain extends IRule {

    /**
     * Add rule to chain.
     * @param rule specified rule.
     */
    IRuleChain addRule(IRule rule);

}
