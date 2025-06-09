package com.epam.tcodata.models.datalake.prepared.statics;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.prepared.PreparedEntity;

import java.util.UUID;

@SuppressWarnings("CPD-START")
public class LocationShapeType extends PreparedEntity {

    private static final long serialVersionUID = 7483789804470658989L;

    public enum Values {

        CIRCLE("4dcae378-17c2-483a-81c5-8faefcff95dc", 0, "Circle"),
        POLYGON("6b0ce8dc-0a0d-475f-a4cc-8328df638cff", 1, "Polygon"),
        RECTANGLE("86806b04-7f65-4978-9936-471983628cbe", 2, "Rectangle"),
        POLYLINE("b79fb55a-81f1-444d-b9e8-4a46c657678a", 3, "PolyLine");

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

        /**
         * Searches the value that corresponds to the given string code.
         *
         * @param code code
         * @return instance of LocationType
         */
        public static Values valueByStringCode(String code) {
            for (Values value : Values.values()) {
                if (value.stringCode.equalsIgnoreCase(code)) {
                    return value;
                }
            }
            return null;
        }
    }

    public static class Fields {
        public static final String EXTERNAL_ID = "external_id";
        public static final String SHAPE_TYPE_CODE = "shape_type_code";
        public static final String SHAPE_TYPE_DESCRIPTION = "shape_type_description";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.EXTERNAL_ID)
    private Long externalId;
    @ColumnName(Fields.SHAPE_TYPE_CODE)
    private String shapeTypeCode;
    @ColumnName(Fields.SHAPE_TYPE_DESCRIPTION)
    private String shapeTypeDescription;

    public LocationShapeType() {
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
