package com.epam.tcodata.models.mix.fact;

import java.io.Serializable;
import java.util.Objects;

public class TachoParameterDefinition implements Serializable {

    private static final long serialVersionUID = 2996445442763654265L;

    public Integer key;
    public Long parameterId;
    public Long deviceId;
    public String lineName;

    public TachoParameterDefinition() {
        /***  Default implementation ***/
    }

    public Integer getKey() {
        return key;
    }

    public TachoParameterDefinition setKey(Integer key) {
        this.key = key;
        return this;
    }

    public Long getParameterId() {
        return parameterId;
    }

    public TachoParameterDefinition setParameterId(Long parameterId) {
        this.parameterId = parameterId;
        return this;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public TachoParameterDefinition setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public String getLineName() {
        return lineName;
    }

    public TachoParameterDefinition setLineName(String lineName) {
        this.lineName = lineName;
        return this;
    }

    @Override
    public String toString() {
        return "TachoParameterDefinition{"
                + "key=" + key
                + ", parameterId=" + parameterId
                + ", deviceId=" + deviceId
                + ", lineName='" + lineName + '\''
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TachoParameterDefinition that = (TachoParameterDefinition) o;
        return Objects.equals(key, that.key)
                && Objects.equals(parameterId, that.parameterId)
                && Objects.equals(deviceId, that.deviceId)
                && Objects.equals(lineName, that.lineName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, parameterId, deviceId, lineName);
    }
}
