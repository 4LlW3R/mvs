package com.epam.tcodata.models.mix.fact;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TachoInterval implements Serializable {

    private static final long serialVersionUID = 2260976102665519459L;

    private Timestamp intervalDateTime;
    private List<TachoParameterValue> data;

    public TachoInterval() {
        this.data = new ArrayList<>();
    }

    public Timestamp getIntervalDateTime() {
        return intervalDateTime;
    }

    public void setIntervalDateTime(Timestamp intervalDateTime) {
        this.intervalDateTime = intervalDateTime;
    }

    public List<TachoParameterValue> getData() {
        return data;
    }

    public void setData(List<TachoParameterValue> data) {
        this.data.clear();
        this.data.addAll(data);
    }

    @Override
    public String toString() {
        return "TachoInterval{"
                + "intervalDateTime=" + intervalDateTime
                + ", data=" + data
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TachoInterval tachoInterval = (TachoInterval) o;
        return Objects.equals(intervalDateTime, tachoInterval.intervalDateTime)
                && Objects.equals(data, tachoInterval.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(intervalDateTime, data);
    }


    public static final class TachoIntervalBuilder {
        private Timestamp intervalDateTime;
        private List<TachoParameterValue> data;

        public TachoIntervalBuilder() {
            /***  Default implementation ***/
        }

        public TachoIntervalBuilder setIntervalDateTime(Timestamp intervalDateTime) {
            this.intervalDateTime = intervalDateTime;
            return this;
        }

        public TachoIntervalBuilder setData(List<TachoParameterValue> data) {
            this.data = data;
            return this;
        }

        /**
         * Build entity with specified parameters.
         * @return new entity.
         */
        public TachoInterval build() {
            TachoInterval tachoInterval = new TachoInterval();
            tachoInterval.setIntervalDateTime(intervalDateTime);
            tachoInterval.setData(data == null ? new ArrayList<>() : data);
            return tachoInterval;
        }
    }


    private void writeObject(java.io.ObjectOutputStream stream)
            throws IOException {
        stream.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
    }
}
