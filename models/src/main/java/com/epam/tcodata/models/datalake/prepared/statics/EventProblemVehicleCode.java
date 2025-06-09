package com.epam.tcodata.models.datalake.prepared.statics;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.prepared.PreparedEntity;

import java.util.UUID;

@SuppressWarnings("CPD-START")
public class EventProblemVehicleCode extends PreparedEntity {

    private static final long serialVersionUID = 3904610826325147214L;

    public enum Values {

        VALID("31770080-0b43-4179-8acd-5e27bad38f56", 1, "Valid"),
        GPS_PROBLEM("a6ec00eb-912b-418e-a250-b2d376cf5489", 2, "Gps problem"),
        SPEED_SENDER_PROBLEM("9f9144c1-3320-477b-a090-4bbd4eb0eb89", 3, "Speed sender problem"),
        NO_GPS_DATA_AVAILABLE("e423055b-6e13-4de0-bc7b-341364f86e6c", 4, "No gps data available"),
        DUPLICATE_EVENTS("31f875d2-c68d-4727-9b82-14543a22545a", 5, "Duplicate events"),
        BRACKING_RATE_VALUE_HIGH("68786551-a3ef-49b1-82ed-9fb1011697ff", 6, "Bracking rate value high"),
        BRACKING_RATE_VALUE_HIGH_COMPARE_WITH_GPS("247413ae-dd2f-4bef-b113-065791d9f7ef", 7, "Bracking rate value high compare with gps"),
        ACCELERATION_RATE_HIGH("cbd3a97d-6d8d-4158-abbe-ff68ecd2e104", 8, "Acceleration rate high"),
        ACCELERATION_RATE_COMPARE_WITH_GPS("d823c4de-7a41-4622-9146-4cc5109bef9b", 9, "Acceleration rate compare with gps"),
        SPEED_SENSOR_SPIKE("aec2d040-52d6-4918-8c95-4539c0953c72", 10, "Speed sensor spike"),
        INVALID_SPEED_VALUE("eb00dc3c-a812-413f-a0c4-c807c1866938", 11, "Invalid speed value"),
        VELOCITY_ISSUE("482778ea-f689-49c3-a507-eeaa5202f433", 12, "Velocity issue");

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
        public static final String SHAPE_TYPE_CODE = "shape_type_code";
        public static final String DESCRIPTION = "description";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.EXTERNAL_ID)
    private Long externalId;
    @ColumnName(Fields.SHAPE_TYPE_CODE)
    private String shapeTypeCode;
    @ColumnName(Fields.DESCRIPTION)
    private String shapeTypeDescription;

    public EventProblemVehicleCode() {
        /***  Default implementation ***/
    }

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    public String getShapeTypeCode() {
        return shapeTypeCode;
    }

    public void setShapeTypeCode(String shapeTypeCode) {
        this.shapeTypeCode = shapeTypeCode;
    }

    public String getShapeTypeDescription() {
        return shapeTypeDescription;
    }

    public void setShapeTypeDescription(String shapeTypeDescription) {
        this.shapeTypeDescription = shapeTypeDescription;
    }
}
