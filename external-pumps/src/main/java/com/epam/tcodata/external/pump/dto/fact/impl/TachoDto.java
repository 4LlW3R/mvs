package com.epam.tcodata.external.pump.dto.fact.impl;

import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.models.mix.Entity;
import com.epam.tcodata.sql.dal.util.SqlCommon;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

public class TachoDto<T extends Entity> extends AbstractDto {

    private static final long serialVersionUID = -1170881490269718078L;

    private Instant from;
    private Instant to;
    private long assetId;
    private long orgGroupId;

    private Timestamp persistedDateUtc; //needs for updating offsets after batch handling

    public TachoDto() {
    }

    /**
     * Constructor for fact Tacho.
     *
     * @param from               from time.
     * @param to                 to time.
     * @param assetId            asset id
     * @param orgGroupId         orgGroup id
     * @param persistedDateUtc   persisted date from ValidatedEvent
     * @param accessToken        access token.
     * @param totalElementsCount total elements count.
     */
    public TachoDto(Instant from,
                    Instant to,
                    long assetId,
                    long orgGroupId,
                    Timestamp persistedDateUtc,
                    String accessToken,
                    Time lastSyncDuration,
                    long totalElementsCount) {
        super(accessToken, lastSyncDuration, totalElementsCount);
        this.from = from;
        this.to = to;
        this.assetId = assetId;
        this.orgGroupId = orgGroupId;
        this.persistedDateUtc = SqlCommon.clone(persistedDateUtc);
    }

    public Instant getFrom() {
        return from;
    }

    public TachoDto<T> setFrom(Instant from) {
        this.from = from;
        return this;
    }

    public Instant getTo() {
        return to;
    }

    public TachoDto<T> setTo(Instant to) {
        this.to = to;
        return this;
    }

    public long getAssetId() {
        return assetId;
    }

    public TachoDto<T> setAssetId(long assetId) {
        this.assetId = assetId;
        return this;
    }

    public long getOrgGroupId() {
        return orgGroupId;
    }

    public TachoDto<T> setOrgGroupId(long orgGroupId) {
        this.orgGroupId = orgGroupId;
        return this;
    }

    public Timestamp getPersistedDateUtc() {
        return SqlCommon.clone(persistedDateUtc);
    }

    public TachoDto<T> setPersistedDateUtc(Timestamp persistedDateUtc) {
        this.persistedDateUtc = SqlCommon.clone(persistedDateUtc);
        return this;
    }

    @Override
    public String toString() {
        return "TachoDto{"
                + super.toString()
                + ", from=" + from
                + ", to=" + to
                + ", assetId=" + assetId
                + ", orgGroupId=" + orgGroupId
                + ", persistedDateUtc=" + persistedDateUtc
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TachoDto<?> tachoDto = (TachoDto<?>) o;
        return assetId == tachoDto.assetId
                && Objects.equals(from, tachoDto.from)
                && Objects.equals(to, tachoDto.to)
                && Objects.equals(persistedDateUtc, tachoDto.persistedDateUtc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), from, to, assetId, persistedDateUtc);
    }

    @Override
    public String additionalInfo() {
        return "TachoDto{"
                + ", from=" + from
                + ", to=" + to
                + ", assetId=" + assetId
                + ", persistedDateUtc=" + persistedDateUtc
                + "}";
    }
}
