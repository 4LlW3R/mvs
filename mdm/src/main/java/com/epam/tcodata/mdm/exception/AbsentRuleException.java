package com.epam.tcodata.mdm.exception;

public class AbsentRuleException extends RuntimeException {

    public AbsentRuleException() {
    }

    public AbsentRuleException(String message) {
        super(message);
    }
}
