package com.epam.tcodata.models.datalake.prepared.statics;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.prepared.PreparedEntity;

import java.util.UUID;

@SuppressWarnings("CPD-START")
public class VideoChannelType extends PreparedEntity {

    private static final long serialVersionUID = -2835862852721104737L;

    public enum Values {

        ROAD("02303edc-a453-4741-94b6-81fb5c35884e", "Road – forward looks to road"),
        CAB("7c904890-7fea-48ca-bf74-ed574b378365", "Cab - internal looks into cabin"),
        CAMERA_3("673a517d-3491-478e-9ee8-3d726f55091b", "Camera_3"),
        CAMERA_4("577492d4-2236-46e4-9b19-834a05e1ed91", "Camera_4");

        private String description;
        private UUID uuid;

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
    }

    public static class Fields {
        public static final String CHANNEL_CODE = "channel_code";
        public static final String CHANNEL_DESCRIPTION = "channel_description";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.CHANNEL_CODE)
    private String channelCode;
    @ColumnName(Fields.CHANNEL_DESCRIPTION)
    private String channelDescription;

    public VideoChannelType() {
        /***  Default implementation ***/
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getChannelDescription() {
        return channelDescription;
    }

    public void setChannelDescription(String channelDescription) {
        this.channelDescription = channelDescription;
    }
}
