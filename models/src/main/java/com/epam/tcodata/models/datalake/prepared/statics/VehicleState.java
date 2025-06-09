package com.epam.tcodata.models.datalake.prepared.statics;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.prepared.PreparedEntity;

import java.util.UUID;

@SuppressWarnings("CPD-START")
public class VehicleState extends PreparedEntity {

    private static final long serialVersionUID = 2023396894379609615L;

    public enum Values {

        AVAILABLE("9866a2ce-eb38-4607-9014-617f0e77e44a", "Available"),
        UNAVAILABLE("76926fb2-9ebf-42da-b4b0-426cba2963bf", "Unavailable"),
        ACCIDENT("ae0fd0c7-0f8e-4128-ad6c-639098bf6e89", "Accident"),
        ACTIVE_MESSAGE_DEACTIVATED("2765377d-bbf9-4b04-9392-0b58f961957a", "Active message deactivated"),
        AWAITING_FEEDBACK("8f6c6e64-9a29-4865-bfaf-8fba9a72be02", "Awaiting feedback"),
        BUZZER_DEACTIVATED("b79b6bb9-0313-461b-b090-8410427598eb", "Buzzer deactivated"),
        CONFIRMED_STANDING("64598f72-456e-465b-b5e9-b7cb5f62cc79", "Confirmed standing"),
        DE_INSTALLED("d6000ee3-2edc-4f1a-aae9-67e3e322d6a9", "De-Installed"),
        IMMOBILIZER_BYPASSED("b44c74ba-a3f8-4434-8239-c0c5a714dbdc", "Immobilizer bypassed"),
        NEW_INSTALLATION("004d2909-81a3-48e4-95f9-df1a7f695f78", "New installation"),
        OPERATIONAL_NOT_DOWNLOADING("30172a62-0239-4c11-add5-344e5668abd7", "Operational - Not downloading"),
        SOLD("d7878d17-9b46-4142-8d01-eda4914a0dcc", "Sold"),
        VEHICLE_OFF_ROAD("62f4c4c5-e5b2-4b32-ab4b-60f7484de90a", "Vehicle off road"),
        WORKSHOP("5de34510-0397-440f-b729-8b8834eab888", "Workshop"),
        OTHER("6049f532-3717-4072-8595-af0ac8874d73", "Other"),
        DECOMMISSIONED("f4b1fe36-9904-45a1-b9cb-90e76949332e", "Decommissioned");

        private UUID uuid;
        private String description;

        Values(String stringUuid, String description) {
            this.uuid = UUID.fromString(stringUuid);
            this.description = description;
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
         * Returns string description of current code.
         *
         * @return String
         */
        public String getDescription() {
            return this.description;
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
        public static final String STATE_CODE = "state_code";
        public static final String STATE_DESCRIPTION = "state_description";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.STATE_CODE)
    private String stateCode;
    @ColumnName(Fields.STATE_DESCRIPTION)
    private String stateDescription;

    public VehicleState() {
        /***  Default implementation ***/
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateDescription() {
        return stateDescription;
    }

    public void setStateDescription(String stateDescription) {
        this.stateDescription = stateDescription;
    }
}
