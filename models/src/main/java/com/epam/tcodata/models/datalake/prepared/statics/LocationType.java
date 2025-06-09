package com.epam.tcodata.models.datalake.prepared.statics;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.prepared.PreparedEntity;

import java.util.UUID;

@SuppressWarnings("CPD-START")
public class LocationType extends PreparedEntity {

    private static final long serialVersionUID = -529800589951929428L;

    public enum Values {

        CUSTOMER("0d470c0e-6938-48ee-89fd-72b37fe4657c", 1, "Customer"),
        NO_GO_ZONE("a3738ab4-af9b-429a-b340-763b54b90508", 2, "NoGoZone"),
        SITE("cbf197a2-b92a-4192-9a96-0427989687de", 3, "Site"),
        OTHER("6f2b873b-62c4-4a44-8fcf-b53d2c50850b", 4, "Other"),
        STREET_POLY_LINE("59ab58dc-34ec-473d-a126-d35f923d711b", 5, "StreetPolyLine"),
        ROUTE_POLY_LINE("7b5678f5-48df-4d3d-a462-5da2317b90d6", 6, "RoutePolyLine"),
        FUEL_STOP("64f3c005-04a8-4ecb-b6f4-795791a68ce6", 7, "FuelStop"),
        REST_STOP("b0bbb5ba-4ae9-4432-b9fd-f89d432bdf71", 8, "RestStop"),
        SPEED_ZONE("779c9b8b-b3b7-4fff-9f6d-21608e7cccc4", 9, "SpeedZone"),
        BUSINESS("f767947d-615c-4567-9f39-b78f4c49bcbd", 10, "Business"),
        PRIVATE("a0af6a49-f890-4999-8e2d-9c5bfc08d707", 12, "Private"),
        UN_CLASSIFIED("e42c66b2-cd5e-4714-934a-46d9f3f46a4b", 14, "UnClassified");

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
        public static final String LOCATION_TYPE_CODE = "location_type_code";
        public static final String LOCATION_TYPE_DESCRIPTION = "location_type_description";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.EXTERNAL_ID)
    private Long externalId;
    @ColumnName(Fields.LOCATION_TYPE_CODE)
    private String locationTypeCode;
    @ColumnName(Fields.LOCATION_TYPE_DESCRIPTION)
    private String locationTypeDescription;

    public LocationType() {
        /***  Default implementation ***/
    }

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    public String getLocationTypeCode() {
        return locationTypeCode;
    }

    public void setLocationTypeCode(String locationTypeCode) {
        this.locationTypeCode = locationTypeCode;
    }

    public String getLocationTypeDescription() {
        return locationTypeDescription;
    }

    public void setLocationTypeDescription(String locationTypeDescription) {
        this.locationTypeDescription = locationTypeDescription;
    }
}
