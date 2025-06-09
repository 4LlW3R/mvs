package com.epam.tcodata.event.validator.domain;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.prepared.fact.PreparedEvent;

import java.util.Objects;

public class EnrichedPreparedEvent extends PreparedEvent {

    private static final long serialVersionUID = 3938526724019221221L;

    public static class Fields {
        public static final String DESCRIPTION = "description";
        public static final String VALIDATION_CODE = "validation_code";
        public static final String PROBLEM_VEHICLE = "problem_vehicle";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.DESCRIPTION)
    private String description;
    @ColumnName(Fields.VALIDATION_CODE)
    private Integer validationCode;
    @ColumnName(Fields.PROBLEM_VEHICLE)
    private Integer problemVehicle;

    /**
     * Create enriched validated event.
     */
    public EnrichedPreparedEvent() {
        /***  Default implementation ***/
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getValidationCode() {
        return validationCode;
    }

    public void setValidationCode(Integer validationCode) {
        this.validationCode = validationCode;
    }

    public Integer getProblemVehicle() {
        return problemVehicle;
    }

    public void setProblemVehicle(Integer problemVehicle) {
        this.problemVehicle = problemVehicle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnrichedPreparedEvent that = (EnrichedPreparedEvent) o;
        return Objects.equals(description, that.description)
                && Objects.equals(validationCode, that.validationCode)
                && Objects.equals(problemVehicle, that.problemVehicle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, validationCode, problemVehicle);
    }
}
