package com.epam.tcodata.models.datalake.prepared.statics;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.prepared.PreparedEntity;

import java.util.UUID;

@SuppressWarnings("CPD-START")
public class FuelType extends PreparedEntity {

    private static final long serialVersionUID = 6980377371896007677L;

    public enum Values {
        PETROL("3179838d-2f99-49f2-baec-dd0a15226cfb", "Petrol"),
        DIESEL("d00fcf78-91d8-419d-a11f-5357b60ea4b6", "Diesel"),
        LPG("e843910c-8fc8-483f-a3b3-a5405913cdd2", "LPG"),
        OTHER("8cf8d0b3-80f5-4f10-a201-9783cd57cabb", "Other"),
        NONE("cabdb120-fb7c-4346-a527-d7e5bc504cda", "None");

        private UUID uuid;
        private String description;

        Values(String stringUuid, String description) {
            this.uuid = UUID.fromString(stringUuid);
            this.description = description;
        }

        /**
         * Returns string description of current code.
         *
         * @return String
         */
        public String getDescription() {
            return this.description;
        }

        /**
         * Returns UUID as durable key.
         *
         * @return UUID
         */
        public UUID getUuid() {
            return uuid;
        }

        /**
         * Searches the value that corresponds to the given string code.
         *
         * @param code code
         * @return instance of LocationType
         */
        public static Values valueByStringCode(String code) {
            for (Values value : Values.values()) {
                if (value.description.equalsIgnoreCase(code)) {
                    return value;
                }
            }
            return null;
        }
    }

    public static class Fields {
        public static final String FUEL_TYPE_CODE = "fuel_type_code";
        public static final String FUEL_TYPE_DESCRIPTION = "fuel_type_description";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.FUEL_TYPE_CODE)
    private String fuelTypeCode;
    @ColumnName(Fields.FUEL_TYPE_DESCRIPTION)
    private String fuelTypeDescription;

    public FuelType() {
        /***  Default implementation ***/
    }

    public String getFuelTypeCode() {
        return fuelTypeCode;
    }

    public void setFuelTypeCode(String fuelTypeCode) {
        this.fuelTypeCode = fuelTypeCode;
    }

    public String getFuelTypeDescription() {
        return fuelTypeDescription;
    }

    public void setFuelTypeDescription(String fuelTypeDescription) {
        this.fuelTypeDescription = fuelTypeDescription;
    }
}
