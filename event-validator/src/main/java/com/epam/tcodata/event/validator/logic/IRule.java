package com.epam.tcodata.event.validator.logic;


import java.io.Serializable;


/**
 * Rule interface.
 */
public interface IRule extends Serializable {

    /**
     * Get result rules.
     * @return <code>result code</code> - result of rule implementation.
     */
    int apply();

    /**
     * The rule can be applied.
     * @return <code>true</code> - the rule can be applied, <code>false</code> - the rule cannot be applied.
     */
    boolean isApplied();

}
