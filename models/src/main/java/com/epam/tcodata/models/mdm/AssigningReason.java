package com.epam.tcodata.models.mdm;

public enum AssigningReason {

    /**
     * Guardian value.
     */
    NONE,

    /**
     * Initial value. This value was defined by a batch process to initialize all keys in the very beginning.
     */
    INITIAL_KEY,

    /**
     * New key is generated.
     */
    NEW_KEY,

    /**
     * For some old natural key old surrogate key was reassigned. This situation appears when KeyManager can
     * recognize that a physical entity is the same with new identity. For example a driver changed his/her job and
     * joined to another company and got a new token.
     */
    RENEW_KEY,

    /**
     * It is well known entity for KeyManager. It could found its natural key in its mapping.
     * Nothing to do is needed.
     */
    FOUND_KEY,

    /**
     * Such key is assigned temporally until a real entity will come.
     */
    SPECULATIVE_KEY
}
