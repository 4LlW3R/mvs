package com.epam.tcodata.models.mix.fact;

import java.io.Serializable;
import java.util.Objects;

public class TachoParameterValue implements Serializable {

    private static final long serialVersionUID = -711595420978435605L;

    public Integer value;
    public Integer key;

    public TachoParameterValue() {
        /***  Default implementation ***/
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "TachoParameterValue{"
                + "value=" + value
                + ", key=" + key
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TachoParameterValue tachoParameterValue = (TachoParameterValue) o;
        return Objects.equals(value, tachoParameterValue.value)
                && Objects.equals(key, tachoParameterValue.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, key);
    }


    public static final class TachoParameterValueBuilder {
        public Integer value;
        public Integer key;

        public TachoParameterValueBuilder() {
            /***  Default implementation ***/
        }

        public TachoParameterValueBuilder setValue(Integer value) {
            this.value = value;
            return this;
        }

        public TachoParameterValueBuilder setKey(Integer key) {
            this.key = key;
            return this;
        }

        /**
         * Build entity with specified parameters.
         * @return new entity.
         */
        public TachoParameterValue build() {
            TachoParameterValue tachoParameterValue = new TachoParameterValue();
            tachoParameterValue.setValue(value);
            tachoParameterValue.setKey(key);
            return tachoParameterValue;
        }
    }
}
