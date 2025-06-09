package com.epam.tcodata.models;

/**
 * Signal types and codes.
 */
public enum SignalType {

    /**
     * Stop immediately, code 1.
     */
    STOP(1),

    /**
     * Stop after batch complete, code 2.
     */
    ONE_BATCH_STOP(2);

    private int code;

    SignalType(int code) {
        this.code = code;
    }

    /**
     * Returns numeric representation of current code.
     *
     * @return int
     */
    public int getCode() {
        return code;
    }

    /**
     * Returns signal type by code.
     *
     * @param code int code
     * @return {@link SignalType}
     */
    public static SignalType byCode(int code) {
        for (SignalType value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Wrong code: " + code);
    }
}
