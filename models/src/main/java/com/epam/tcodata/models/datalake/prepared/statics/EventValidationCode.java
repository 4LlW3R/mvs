package com.epam.tcodata.models.datalake.prepared.statics;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.prepared.PreparedEntity;

import java.util.UUID;

@SuppressWarnings("CPD-START")
public class EventValidationCode extends PreparedEntity {

    private static final long serialVersionUID = 6703730852500784734L;

    public enum Values {

        VALID("dc018d95-898d-4f64-bc5f-e2099e5aacb0", 1, "Valid"),
        SUSPECT("4587cd2c-2776-4558-a60d-4e780a2e2305", 0, "Suspect"),
        FALSE_POSITIVE("55d89649-9b4c-4ab7-a710-e346d6b86ca0", -1, "False positive");

        private int intCode;
        private UUID uuid;
        private String stringCode;

        Values(String stringUuid, int intCode, String stringCode) {
            this.intCode = intCode;
            this.uuid = UUID.fromString(stringUuid);
            this.stringCode = stringCode;
        }

        /**
         * Returns numeric representation of current code.
         *
         * @return int
         */
        public int getIntCode() {
            return this.intCode;
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
         * Returns string representation of current code.
         *
         * @return String
         */
        public String getStringCode() {
            return this.stringCode;
        }

        /**
         * Searches the value that corresponds to the given int code.
         *
         * @param code code
         * @return instance of LocationType
         */
        public static Values valueByIntCode(long code) {
            for (Values value : Values.values()) {
                if (value.intCode == code) {
                    return value;
                }
            }
            return null;
        }
    }

    public static class Fields {
        public static final String EXTERNAL_ID = "external_id";
        public static final String DESCRIPTION = "description";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.EXTERNAL_ID)
    private String externalId;
    @ColumnName(Fields.DESCRIPTION)
    private String shapeTypeDescription;

    public EventValidationCode() {
        /***  Default implementation ***/
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getShapeTypeDescription() {
        return shapeTypeDescription;
    }

    public void setShapeTypeDescription(String shapeTypeDescription) {
        this.shapeTypeDescription = shapeTypeDescription;
    }
}
