package com.epam.tcodata.models.mix.dimension;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class DriverAdditionalDetailField implements Serializable {

    private static final long serialVersionUID = 2478840833802933670L;

    private Integer id;
    private String label;
    private String helpText;
    @JsonProperty("IsRequired")
    private Boolean required;
    private String value;

    public DriverAdditionalDetailField() {
        /***  Default implementation ***/
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DriverAdditionalDetailField that = (DriverAdditionalDetailField) o;
        return Objects.equals(id, that.id)
                && Objects.equals(label, that.label)
                && Objects.equals(helpText, that.helpText)
                && Objects.equals(required, that.required)
                && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, label, helpText, required, value);
    }

    @Override
    public String toString() {
        return "DriverAdditionalDetailField{"
                + "id=" + id
                + ", label='" + label + '\''
                + ", helpText='" + helpText + '\''
                + ", required=" + required
                + ", value='" + value + '\''
                + '}';
    }


    public static final class DriverAdditionalDetailFieldBuilder {
        private Integer id;
        private String label;
        private String helpText;
        private Boolean required;
        private String value;

        public DriverAdditionalDetailFieldBuilder() {
            /***  Default implementation ***/
        }

        public DriverAdditionalDetailFieldBuilder setId(Integer id) {
            this.id = id;
            return this;
        }

        public DriverAdditionalDetailFieldBuilder setLabel(String label) {
            this.label = label;
            return this;
        }

        public DriverAdditionalDetailFieldBuilder setHelpText(String helpText) {
            this.helpText = helpText;
            return this;
        }

        public DriverAdditionalDetailFieldBuilder setRequired(Boolean required) {
            this.required = required;
            return this;
        }

        public DriverAdditionalDetailFieldBuilder setValue(String value) {
            this.value = value;
            return this;
        }

        /**
         * Build entity with specified parameters.
         * @return new entity.
         */
        public DriverAdditionalDetailField build() {
            DriverAdditionalDetailField driverAdditionalDetailField = new DriverAdditionalDetailField();
            driverAdditionalDetailField.setId(id);
            driverAdditionalDetailField.setLabel(label);
            driverAdditionalDetailField.setHelpText(helpText);
            driverAdditionalDetailField.setRequired(required);
            driverAdditionalDetailField.setValue(value);
            return driverAdditionalDetailField;
        }
    }
}

