package com.epam.tcodata.models.datalake.prepared.fact;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.prepared.PreparedEntity;

import java.sql.Timestamp;

@SuppressWarnings("CPD-START")
public class PreparedEventVideo extends PreparedEntity {

    private static final long serialVersionUID = 8573577377022430174L;

    public static class Fields {
        public static final String ORGANIZATION_DURABLE_KEY = "organization_durable_key";
        public static final String EXTERNAL_ID = "external_id";
        public static final String PERSISTED_DATE_UTC = "persisted_date_utc";
        public static final String PARENT_FACT_EVENT_DURABLE_KEY = "parent_fact_event_durable_key";
        public static final String VIDEO_CHANNEL_TYPE_DURABLE_KEY = "video_channel_type_durable_key";
        public static final String VIDEO_CHANNEL_TYPE_CODE = "video_channel_type_code";
        public static final String MEDIA_URL = "media_url";
        public static final String YEAR = "year";
        public static final String WEEK_NUMBER = "week_number";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.ORGANIZATION_DURABLE_KEY)
    private String organizationDurableKey;
    @ColumnName(Fields.EXTERNAL_ID)
    private Long externalId;
    @ColumnName(Fields.PERSISTED_DATE_UTC)
    private Timestamp persistedDateUtc;
    @ColumnName(Fields.PARENT_FACT_EVENT_DURABLE_KEY)
    private String parentFactEventDurableKey;
    @ColumnName(Fields.VIDEO_CHANNEL_TYPE_DURABLE_KEY)
    private String videoChannelTypeDurableKey;
    @ColumnName(Fields.VIDEO_CHANNEL_TYPE_CODE)
    private String videoChannelTypeCode;
    @ColumnName(Fields.MEDIA_URL)
    private String mediaUrl;
    @ColumnName(Fields.YEAR)
    private Integer year;
    @ColumnName(Fields.WEEK_NUMBER)
    private Integer weekNumber;

    public PreparedEventVideo() {
        /***  Default implementation ***/
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getOrganizationDurableKey() {
        return organizationDurableKey;
    }

    public void setOrganizationDurableKey(String organizationDurableKey) {
        this.organizationDurableKey = organizationDurableKey;
    }

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    public Timestamp getPersistedDateUtc() {
        return persistedDateUtc;
    }

    public void setPersistedDateUtc(Timestamp persistedDateUtc) {
        this.persistedDateUtc = persistedDateUtc;
    }

    public String getParentFactEventDurableKey() {
        return parentFactEventDurableKey;
    }

    public void setParentFactEventDurableKey(String parentFactEventDurableKey) {
        this.parentFactEventDurableKey = parentFactEventDurableKey;
    }

    public String getVideoChannelTypeDurableKey() {
        return videoChannelTypeDurableKey;
    }

    public void setVideoChannelTypeDurableKey(String videoChannelTypeDurableKey) {
        this.videoChannelTypeDurableKey = videoChannelTypeDurableKey;
    }

    public String getVideoChannelTypeCode() {
        return videoChannelTypeCode;
    }

    public void setVideoChannelTypeCode(String videoChannelTypeCode) {
        this.videoChannelTypeCode = videoChannelTypeCode;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }
}
