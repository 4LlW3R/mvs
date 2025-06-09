package com.epam.tcodata.models;


/**
 * Data pump result codes.
 */
public enum DataPumpResultCode {
    /**
     * Initialization guard.
     */
    UNKNOWN(0),
    /**
     * Indicates that batch did not get any data.
     */
    NO_DATA(1),
    /**
     * Indicates that batch got maximum number elements per batch, next non-scheduled run is needed.
     */
    GOT_ALL_DATA(2),
    /**
     * Indicates that batch got less then maximum number elements per patch and can wait for next scheduled run.
     */
    GOT_PART_OF_DATA(3),
    /**
     * Indicates that there was a fault during pumping.
     */
    GOT_FAULT(4);


    private int code;


    DataPumpResultCode(int code) {
        this.code = code;
    }

    /**
     * Returns numeric representation of current code.
     *
     * @return code
     */
    public int getCode() {
        return this.code;
    }

    /**
     * Get data pum result code by code.
     *
     * @param code for data pump.
     * @return data pump result code.
     */
    public static DataPumpResultCode getDataPumpResultCodeByCode(int code) {
        DataPumpResultCode result;
        switch (code) {
            case 0:
                result = UNKNOWN;
                break;
            case 1:
                result = NO_DATA;
                break;
            case 2:
                result = GOT_ALL_DATA;
                break;
            case 3:
                result = GOT_PART_OF_DATA;
                break;
            case 4:
                result = GOT_FAULT;
                break;
            default:
                throw new IllegalArgumentException("Unknown entity code: \'" + code + "\'");
        }
        return result;
    }

}
