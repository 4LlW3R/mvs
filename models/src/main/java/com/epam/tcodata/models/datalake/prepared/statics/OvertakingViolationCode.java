package com.epam.tcodata.models.datalake.prepared.statics;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.prepared.PreparedEntity;

import java.util.UUID;

@SuppressWarnings("CPD-START")
public class OvertakingViolationCode extends PreparedEntity {

    private static final long serialVersionUID = 3904610826325147214L;

    public enum Values {

        NO_VIOLATION("492799ba-a72e-44cc-bce7-3eeda187a449", 1, "No violation"),
        NIGHT_TIME_OVERTAKING("5130c642-452d-4caa-a88e-9e7df8c0b9b2", 2, "Night time overtaking"),
        COMMUTE_HOURS_OVERTAKING("9048d99e-a49d-43ff-9930-2ddf9e17bdaa", 3, "Commute hours overtaking"),
        VEHICLE_IN_FRONT_IS_FAST_OVERTAKING("7669813e-ce86-4306-9bf5-fe788f35ee80", 4, "Vehicle in front is fast overtaking"),
        SPEED_LIMIT_EXCEEDED_OVERTAKING("02e88ad2-2bae-40b4-9bc2-e29cb38b3ee4", 5, "Speed limit exceeded overtaking"),
        OVERTAKING_DURING_ROAD_CONDITION("ee45c23f-0071-4065-a771-74ccab3c2502", 6, "Overtaking during road condition"),
        NO_OVERTAKING_ZONE_OVERTAKING("535e7b71-d216-4570-9e0a-9ecf11f9f9c6", 7, "No overtaking zone overtaking"),
        BUS_OVERTAKING("6059bc9c-e6a9-4309-b84a-9a7482ee5b7a", 8, "Bus overtaking");

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
    private Long externalId;
    @ColumnName(Fields.DESCRIPTION)
    private String shapeTypeDescription;

    public OvertakingViolationCode() {
        /***  Default implementation ***/
    }

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    public String getShapeTypeDescription() {
        return shapeTypeDescription;
    }

    public void setShapeTypeDescription(String shapeTypeDescription) {
        this.shapeTypeDescription = shapeTypeDescription;
    }
}
