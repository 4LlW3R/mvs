package com.epam.tcodata.models.datalake.raw;

import com.epam.tcodata.models.ColumnName;
import com.epam.tcodata.models.datalake.AbstractDataLakeEntity;

import java.sql.Timestamp;

@SuppressWarnings("Duplicates")
public class RawEntity extends AbstractDataLakeEntity {

    private static final long serialVersionUID = 714190982786442967L;


    public static class Fields {
        public static final String DURABLE_ID = "durable_id";
        public static final String INGESTED_DATE_UTC = "ingested_date_utc";
        public static final String SUBSCRIPTION_ID = "subscription_id";
        public static final String LINEAGE_CODE = "lineage_code";
        public static final String PERSISTED_DATE_UTC = "persisted_date_utc";
        private Fields(){   /***  Default implementation ***/  }
    }

    @ColumnName(Fields.DURABLE_ID)
    private String durableId;
    /**
     * Time of ingestion to EventHub.
     */
    @ColumnName(Fields.INGESTED_DATE_UTC)
    private Timestamp ingestedDateUtc;
    @ColumnName(Fields.SUBSCRIPTION_ID)
    private Long subscriptionId;
    @ColumnName(Fields.LINEAGE_CODE)
    private Integer lineageCode;
    /**
     * Time of persisting to DataLake.
     */
    @ColumnName(Fields.PERSISTED_DATE_UTC)
    private Timestamp persistedDateUtc;

    public RawEntity() {
        /***  Default implementation ***/
    }

    public String getDurableId() {
        return durableId;
    }

    public void setDurableId(String durableId) {
        this.durableId = durableId;
    }

    public Timestamp getIngestedDateUtc() {
        return ingestedDateUtc == null ? null : new Timestamp(ingestedDateUtc.getTime());
    }

    public void setIngestedDateUtc(Timestamp ingestedDateUtc) {
        this.ingestedDateUtc = ingestedDateUtc == null ? null : new Timestamp(ingestedDateUtc.getTime());
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Integer getLineageCode() {
        return lineageCode;
    }

    public void setLineageCode(Integer lineageCode) {
        this.lineageCode = lineageCode;
    }

    public Timestamp getPersistedDateUtc() {
        return persistedDateUtc == null ? null : new Timestamp(persistedDateUtc.getTime());
    }

    public void setPersistedDateUtc(Timestamp persistedDateUtc) {
        this.persistedDateUtc = persistedDateUtc == null ? null : new Timestamp(persistedDateUtc.getTime());
    }
}
