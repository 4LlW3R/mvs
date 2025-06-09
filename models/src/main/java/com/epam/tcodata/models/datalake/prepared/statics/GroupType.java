package com.epam.tcodata.models.datalake.prepared.statics;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.prepared.PreparedEntity;

import java.util.UUID;

@SuppressWarnings("CPD-START")
public class GroupType extends PreparedEntity {

    private static final long serialVersionUID = -6780634731774417525L;

    public enum Values {

        DATA_CENTRE("3e6017ae-14f1-4fa8-9c93-f73bc1bf1b29", 0, "DataCentre"),
        RSO_GROUP("50d9d701-35c9-41a5-96fd-ca2a5871052f", 2, "RsoGroup"),
        DEALER_GROUP("21ad712f-67f0-40d5-b78d-0a6d2341e3fc", 3, "DealerGroup"),
        MULTI_LEVEL_ORG("acbd6a7e-a28f-4386-bcfb-35e8c13643d8", 12, "MultiLevelOrg"),
        ORGANISATION_GROUP("bde0875c-8eda-4061-97b1-82f8994cf4ea", 1, "OrganisationGroup"),
        ORGANISATION_SUB_GROUP("59dfd194-0178-49be-be15-5d38a94bba57", 5, "OrganisationSubGroup"),
        SITE_GROUP("d42a4802-ef80-44b8-b0e2-db7e9f147fa0", 4, "SiteGroup"),
        DEFAULT_SITE("05126c10-94c2-469d-84c9-221957529087", 6, "DefaultSite"),
        SECURITY_GROUP("4b18f406-d06c-46fc-8e6f-a4b3ca89d833", 7, "SecurityGroup"),
        NOTIFICATION_GROUP("4446085c-88cb-47c9-9e80-b8ead54496db", 8, "NotificationGroup"),
        NOTIFICATION_ASSETS_GROUP("f324ee82-d06f-4b74-a25d-c96ecf5d9331", 9, "NotificationAssetsGroup"),
        NOTIFICATION_DRIVERS_GROUP("a15f38e0-f1e4-4802-b137-3ceb85c78d1e", 10, "NotificationDriversGroup"),
        NOTIFICATION_EVENTS_GROUP("cb199b56-d128-4ec1-b740-0eef921431e0", 11, "NotificationEventsGroup"),
        MOBILE_DEVICE_ADMIN_COMMISSIONING_GROUP("2ca95ddc-c135-42d0-b25a-ca5149b1ee74", 13, "MobileDeviceAdminCommissioningGroup"),
        DRIVER_USER_GROUP("6b9501bb-6f8e-49e8-a8bc-f673581c66d2", 14, "DriverUserGroup");

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
         * @return instance of GroupType
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
         * @param code string code
         * @return instance of GroupType
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
        public static final String GROUP_TYPE_CODE = "group_type_code";
        public static final String GROUP_TYPE_DESCRIPTION = "group_type_description";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.EXTERNAL_ID)
    private Long externalId;
    @ColumnName(Fields.GROUP_TYPE_CODE)
    private String groupTypeCode;
    @ColumnName(Fields.GROUP_TYPE_DESCRIPTION)
    private String groupTypeDescription;

    public GroupType() {
        /***  Default implementation ***/
    }

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    public String getGroupTypeCode() {
        return groupTypeCode;
    }

    public void setGroupTypeCode(String groupTypeCode) {
        this.groupTypeCode = groupTypeCode;
    }

    public String getGroupTypeDescription() {
        return groupTypeDescription;
    }

    public void setGroupTypeDescription(String groupTypeDescription) {
        this.groupTypeDescription = groupTypeDescription;
    }
}
