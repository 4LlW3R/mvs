package com.epam.tcodata.event.validator.logic.validation.status;



public final class ValidationStatusCode {
    private ValidationStatusCode(){}

    /** Valid status. */
    public static final int VALID = 1;
    /** Suspect status. */
    public static final int SUSPECT = 0;
    /** False positive status. */
    public static final int FALSE_POSITIVE = -1;
}
