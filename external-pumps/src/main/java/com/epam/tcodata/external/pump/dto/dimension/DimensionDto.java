package com.epam.tcodata.external.pump.dto.dimension;

import com.epam.tcodata.external.pump.dto.AbstractDto;
import com.epam.tcodata.models.mix.Entity;

import java.util.Objects;

public class DimensionDto<T extends Entity> extends AbstractDto {

    private static final long serialVersionUID = 5765601343631242420L;

    private long orgGroupSurrogateId; //requestInfoService
    private long orgGroupId;
    private long accountId;

    public DimensionDto() {
    }

    /**
     * Constructor for dimension pumps.
     *
     * @param orgGroupId  organisation group id.
     * @param accessToken access token.
     */
    public DimensionDto(long orgGroupId,
                        String accessToken) {
        super(accessToken);
        this.orgGroupId = orgGroupId;
    }

    public long getOrgGroupSurrogateId() {
        return orgGroupSurrogateId;
    }

    public DimensionDto<T> setOrgGroupSurrogateId(long orgGroupSurrogateId) {
        this.orgGroupSurrogateId = orgGroupSurrogateId;
        return this;
    }

    public long getOrgGroupId() {
        return orgGroupId;
    }

    public DimensionDto<T> setOrgGroupId(long orgGroupId) {
        this.orgGroupId = orgGroupId;
        return this;
    }

    public long getAccountId() {
        return accountId;
    }

    public DimensionDto<T> setAccountId(long accountId) {
        this.accountId = accountId;
        return this;
    }

    @Override
    public String toString() {
        return "DimensionDto{"
                + super.toString()
                + ", orgGroupSurrogateId=" + orgGroupSurrogateId
                + ", orgGroupId=" + orgGroupId
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DimensionDto<?> that = (DimensionDto<?>) o;
        return orgGroupSurrogateId == that.orgGroupSurrogateId
                && orgGroupId == that.orgGroupId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), orgGroupSurrogateId, orgGroupId);
    }

    @Override
    public String additionalInfo() {
        return "DimensionDto{"
                + "orgGroupSurrogateId=" + orgGroupSurrogateId
                + ", orgGroupId=" + orgGroupId
                + "}";
    }
}
