package com.epam.tcodata.models;

public enum ApplicationSuperType {

    /**
     * Pump.
     */
    PUMP(1),

    /**
     * Analytics.
     */
    ANALYTICS(2),

    /**
     * Persistence.
     */
    PERSISTENCE(3),

    /**
     * Redis manager.
     */
    REDIS(4);

    private int code;

    ApplicationSuperType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
