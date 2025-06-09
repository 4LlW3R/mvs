package com.epam.tcodata.models.datalake.prepared.statics;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.prepared.PreparedEntity;

import java.util.UUID;

@SuppressWarnings("CPD-START")
public class VehicleType extends PreparedEntity {

    private static final long serialVersionUID = -1789693698781570576L;

    public enum Values {

        MOTORCYCLE("cc808430-405c-4460-a741-599aae3dadd6", 1, "Motorcycle"),
        TRAILER("7427f1ed-091c-4623-9f8f-c92a056a38c3", 2, "Trailer"),
        BOAT("7a3d4d20-9913-41dc-8213-8dc7252601bd", 4, "Boat"),
        MOBILE_PLANT_EQUIPMENT("7a252948-9337-4cc6-a341-bfd21f0fa513", 5, "Mobile Plant Equipment"),
        STATIONARY_PLANT_EQUIPMENT("f050e222-5276-4674-ab19-aa0b0202ae1b", 6, "Stationary Plant Equipment"),
        EMERGENCY_SERVICE_VEHICLE("84a9efe7-eb28-43d1-9bce-7fdac495237c", 7, "Emergency Service Vehicle"),
        DANGEROUS_GOODS_VEHICLE("8870f58d-e8cb-4e6e-a060-e9df7cc8352c", 8, "Dangerous Goods Vehicle"),
        PASSENGER_VEHICLE("8b54c61f-9b26-4b2c-a524-65f6600c9eb8", 9, "Passenger Vehicle"),
        LIGHT_PASSENGER_VEHICLE_MINIBUS("6bd056b0-bb58-4733-bff3-9f7b4b407337", 10, "Light Passenger Vehicle - Minibus"),
        HEAVY_PASSENGER_VEHICLE_BUS_ARTICULATED("0f58c517-f659-4d19-abc6-03177874f8be", 11, "Heavy Passenger Vehicle - Bus - Articulated"),
        HEAVY_PASSENGER_VEHICLE_BUS_SINGLE_DECKER("3a5557dc-3372-4738-97ee-4b6c95bd2a30", 12, "Heavy Passenger Vehicle - Bus - Single Decker"),
        HEAVY_PASSENGER_VEHICLE_BUS_DOUBLE_DECKER("b915d0b4-bf81-4c19-b2ce-c8ec0ef6cadb", 13, "Heavy Passenger Vehicle - Bus - Double Decker"),
        HEAVY_VEHICLE_ARTICULATED("66670091-df55-4faf-9f3b-2ee26ee79b78", 14, "Heavy Vehicle - Articulated"),
        HEAVY_VEHICLE_NON_ARTICULATED("c9650da2-7ef0-4919-8579-35973fabc6a9", 15, "Heavy Vehicle - Non-Articulated"),
        HEAVY_VEHICLE_REFRIGERATED_TRANSPORT("960f1b1a-4eec-4829-ba97-5b50e469c2e5", 16, "Heavy Vehicle - Refrigerated Transport"),
        LIGHT_VEHICLE("5ab6f6f4-36c7-4530-9474-84a271d1a2e1", 17, "Light Vehicle"),
        FLUID_TRANSPORT_VEHICLE("f190e8f4-92fc-4262-8d63-b4f922211943", 18, "Fluid Transport Vehicle"),
        OTHER("dbee5950-2f39-4b48-8119-3776279a5692", 20, "Other"),
        TRAIN("83915fc6-6ec4-4a5f-b98a-b6e636c47fbf", 21, "Train"),
        LIGHT_DELIVERY_VEHICLE("aa6477d2-2496-4ad4-a6a2-374be55571d2", 22, "Light Delivery Vehicle"),
        OFF_ROAD_VEHICLE("51db66dc-efb1-464f-860a-75d3725e541e", 24, "Off-Road Vehicle"),
        MEDIUM_COMMERCIAL_VEHICLE("da8245bd-b154-4929-a33e-2d8b05879889", 25, "Medium Commercial Vehicle"),
        NON_POWERED_ASSET("4a21fc7d-cb34-42f1-86af-a77302d3f995", 26, "Non-Powered Asset");

        private int intCode;
        private String stringCode;
        private UUID uuid;

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
         * @return instance of AssetType
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
        public static final String VEHICLE_TYPE_CODE = "vehicle_type_code";
        public static final String VEHICLE_TYPE_DESCRIPTION = "vehicle_type_description";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.EXTERNAL_ID)
    private Long externalId;
    @ColumnName(Fields.VEHICLE_TYPE_CODE)
    private String vehicleTypeCode;
    @ColumnName(Fields.VEHICLE_TYPE_DESCRIPTION)
    private String vehicleTypeDescription;

    public VehicleType() {
        /***  Default implementation ***/
    }

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    public String getVehicleTypeCode() {
        return vehicleTypeCode;
    }

    public void setVehicleTypeCode(String vehicleTypeCode) {
        this.vehicleTypeCode = vehicleTypeCode;
    }

    public String getVehicleTypeDescription() {
        return vehicleTypeDescription;
    }

    public void setVehicleTypeDescription(String vehicleTypeDescription) {
        this.vehicleTypeDescription = vehicleTypeDescription;
    }
}
