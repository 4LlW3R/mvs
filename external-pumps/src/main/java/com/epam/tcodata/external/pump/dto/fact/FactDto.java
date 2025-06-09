package com.epam.tcodata.external.pump.dto.fact;

import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.models.mix.Entity;

import java.sql.Time;
import java.time.Instant;
import java.util.Objects;

public class FactDto<T extends Entity> extends AbstractDto {

    private static final long serialVersionUID = 4630384757455469200L;

    private Instant sinceToken;
    private Instant nextSinceToken;
    private long orgGroupSurrogateId; //requestInfoService
    private long orgGroupId;
    private long accountId;

    /**
     * Default constructor.
     */
    public FactDto(){}

    /**
     * Constructor for fact pumps.
     *
     * @param sinceToken          since token.
     * @param orgGroupSurrogateId surrogate id.
     * @param orgGroupId          organisation group id.
     * @param accessToken         access token.
     * @param totalElementsCount  total elements count.
     */
    public FactDto(Instant sinceToken,
                   long orgGroupSurrogateId,
                   long orgGroupId,
                   String accessToken,
                   Time lastSyncDuration,
                   long totalElementsCount) {
        super(accessToken, lastSyncDuration, totalElementsCount);
        this.sinceToken = sinceToken;
        this.orgGroupSurrogateId = orgGroupSurrogateId;
        this.orgGroupId = orgGroupId;
    }

    public Instant getSinceToken() {
        return sinceToken;
    }

    public Instant getNextSinceToken() {
        return nextSinceToken;
    }

    public FactDto<T> setNextSinceToken(Instant nextSinceToken) {
        this.nextSinceToken = nextSinceToken;
        return this;
    }

    public FactDto<T> setSinceToken(Instant sinceToken) {
        this.sinceToken = sinceToken;
        return this;
    }

    public long getOrgGroupSurrogateId() {
        return orgGroupSurrogateId;
    }

    public FactDto<T> setOrgGroupSurrogateId(long orgGroupSurrogateId) {
        this.orgGroupSurrogateId = orgGroupSurrogateId;
        return this;
    }

    public long getOrgGroupId() {
        return orgGroupId;
    }

    public FactDto<T> setOrgGroupId(long orgGroupId) {
        this.orgGroupId = orgGroupId;
        return this;
    }

    public long getAccountId() {
        return accountId;
    }

    public FactDto<T> setAccountId(long accountId) {
        this.accountId = accountId;
        return this;
    }

    @Override
    public String toString() {
        return "FactDto{"
                + "sinceToken=" + sinceToken
                + ", nextSinceToken=" + nextSinceToken
                + ", orgGroupSurrogateId=" + orgGroupSurrogateId
                + ", orgGroupId=" + orgGroupId
                + ", accountId=" + accountId
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FactDto<?> factDto = (FactDto<?>) o;
        return orgGroupSurrogateId == factDto.orgGroupSurrogateId
                && orgGroupId == factDto.orgGroupId
                && accountId == factDto.accountId
                && Objects.equals(sinceToken, factDto.sinceToken)
                && Objects.equals(nextSinceToken, factDto.nextSinceToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sinceToken, nextSinceToken, orgGroupSurrogateId, orgGroupId, accountId);
    }

    @Override
    public String additionalInfo() {
        return "FactDto{"
                + "sinceToken=" + sinceToken
                + ", nextSinceToken=" + nextSinceToken
                + ", orgGroupSurrogateId=" + orgGroupSurrogateId
                + ", orgGroupId=" + orgGroupId
                + "}";
    }
}
