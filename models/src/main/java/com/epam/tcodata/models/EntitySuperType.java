package com.epam.tcodata.models;


/**
 * Entity global codification codes.
 */
public enum EntitySuperType {

    /** Entity is dimension. */
    DIMENSION(1),
    /** Entity is fact. */
    FACT(2);


    private int code;


    EntitySuperType(int code) {
        this.code = code;
    }

    /**
     * Returns numeric representation of current code.
     * @return code
     */
    public int getCode() {
        return this.code;
    }
}
