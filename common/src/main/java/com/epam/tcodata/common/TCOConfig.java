package com.epam.tcodata.common;


import java.util.Map;


/**
 * Created by Alexander_Kochurin on 10/6/2017.
 * Simple configuration.
 * <p>
 *     It contains typical key - value structure.
 * </p>
 */
public class TCOConfig {

    private final Map<String, String> params;


    /**
     * Create configurations.
     * @param params specified parameters.
     */
    public TCOConfig(Map<String, String> params) {
        this.params = params;
    }

    /**
     * Get parameter as string.
     * @param parameter name of parameter.
     * @return parameter or null if it's absent.
     */
    public String getProperty(String parameter) {
        return this.params.get(parameter);
    }

    /**
     * Get all parameters as Map.
     * @return parameters as map.
     */
    public Map<String, String> getParams() {
        return this.params;
    }

}
