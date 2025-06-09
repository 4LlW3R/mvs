package com.epam.tcodata.models.mix.fact;

import com.epam.tcodata.models.mix.Entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Tacho extends Entity {

    private static final long serialVersionUID = 7053821420491034014L;

    public Long assetId;
    public List<TachoParameterDefinition> parameterDefinitions;
    public List<TachoInterval> intervals;
    public Timestamp startDateTime;
    public Timestamp endDateTime;

    public Tacho() {
        this.parameterDefinitions = new ArrayList<>();
        this.intervals = new ArrayList<>();
    }

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public List<TachoParameterDefinition> getParameterDefinitions() {
        return parameterDefinitions;
    }

    public void setParameterDefinitions(List<TachoParameterDefinition> parameterDefinitions) {
        this.parameterDefinitions.clear();
        this.parameterDefinitions.addAll(parameterDefinitions);
    }

    public List<TachoInterval> getIntervals() {
        return intervals;
    }

    public void setIntervals(List<TachoInterval> intervals) {
        this.intervals.clear();
        this.intervals.addAll(intervals);
    }

    public Timestamp getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Timestamp startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Timestamp getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Timestamp endDateTime) {
        this.endDateTime = endDateTime;
    }

    @Override
    public String toString() {
        return "Tacho{"
                + "assetId=" + assetId
                + ", parameterDefinitions=" + parameterDefinitions
                + ", intervals=" + intervals
                + ", startDateTime=" + startDateTime
                + ", endDateTime=" + endDateTime
                + "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tacho tacho = (Tacho) o;
        return Objects.equals(assetId, tacho.assetId)
                && Objects.equals(parameterDefinitions, tacho.parameterDefinitions)
                && Objects.equals(intervals, tacho.intervals)
                && Objects.equals(startDateTime, tacho.startDateTime)
                && Objects.equals(endDateTime, tacho.endDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assetId, parameterDefinitions, intervals, startDateTime, endDateTime);
    }


    public static final class TachoBuilder {
        public Long assetId;
        public List<TachoParameterDefinition> parameterDefinitions;
        public List<TachoInterval> intervals;
        public Timestamp startDateTime;
        public Timestamp endDateTime;

        public TachoBuilder() {
            /***  Default implementation ***/
        }

        public TachoBuilder setAssetId(Long assetId) {
            this.assetId = assetId;
            return this;
        }

        public TachoBuilder setParameterDefinitions(List<TachoParameterDefinition> parameterDefinitions) {
            this.parameterDefinitions = parameterDefinitions;
            return this;
        }

        public TachoBuilder setIntervals(List<TachoInterval> intervals) {
            this.intervals = intervals;
            return this;
        }

        public TachoBuilder setStartDateTime(Timestamp startDateTime) {
            this.startDateTime = startDateTime;
            return this;
        }

        public TachoBuilder setEndDateTime(Timestamp endDateTime) {
            this.endDateTime = endDateTime;
            return this;
        }

        /**
         * Build entity with specified parameters.
         * @return new entity.
         */
        public Tacho build() {
            Tacho tacho = new Tacho();
            tacho.setAssetId(assetId);
            tacho.setParameterDefinitions(parameterDefinitions == null ? new ArrayList<>() : parameterDefinitions);
            tacho.setIntervals(intervals == null ? new ArrayList<>() : intervals);
            tacho.setStartDateTime(startDateTime);
            tacho.setEndDateTime(endDateTime);
            return tacho;
        }
    }
}
