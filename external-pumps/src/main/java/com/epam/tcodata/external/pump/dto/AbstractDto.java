package com.epam.tcodata.external.pump.dto;

import com.epam.tcodata.models.enriched.IEnrichable;
import com.epam.tcodata.models.exception.NonEnrichedEntityException;
import com.epam.tcodata.models.exception.NonMatchedSubTripEnrichedListSizeException;
import com.epam.tcodata.models.mix.Entity;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractDto<T extends Entity> implements Serializable {

    private static final long serialVersionUID = 2734820473041852640L;

    private String accessToken; //requestInfoService
    private List<T> entityList; //mixService
    private int lastSyncResultCode; //mixService
    private long lastSyncElementCount; //mixService
    private Time lastSyncDuration; //mixService
    private long totalElementsCount; //offsetService
    private String lastErrorMessage; //?

    /**
     * Default constructor.
     */
    protected AbstractDto(){}

    /**
     * Constructor for abstract dto.
     */
    protected AbstractDto(String accessToken, Time lastSyncDuration, long totalElementsCount) {
        this.accessToken = accessToken;
        this.lastSyncDuration = lastSyncDuration;
        this.totalElementsCount = totalElementsCount;
    }

    protected AbstractDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public AbstractDto<T> setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public List<T> getEntityList() {
        return entityList;
    }

    public AbstractDto<T> setEntityList(List<T> entityList) {
        this.entityList = entityList;
        return this;
    }

    /**
     * For getting list of enriched entities.
     *
     * @return Enriched entity list.
     */
    public List<IEnrichable> getEnrichedEntityList() {
        List<IEnrichable> enrichedEntityList = new ArrayList<>();
        for (Entity entity : getEntityList()) {
            if (entity instanceof IEnrichable) {
                enrichedEntityList.add((IEnrichable) entity);
            } else {
                throw new NonEnrichedEntityException("Entity field was not enriched");
            }
        }
        return enrichedEntityList;
    }

    /**
     * For setting list of enriched entities.
     *
     * @param enrichedEntityList Enriched entity list.
     */
    public void setEnrichedEntityList(List<IEnrichable> enrichedEntityList) {
        if (enrichedEntityList.size() == getEntityList().size()) {
            for (int i = 0; i < getEntityList().size(); i++) {
                if (enrichedEntityList.get(i) instanceof Entity) {
                    getEntityList().set(i, (T) enrichedEntityList.get(i));
                }
            }
        } else {
            throw new NonMatchedSubTripEnrichedListSizeException("Size of enriched entity list does not match "
                    + "size of non enriched entity list");
        }
    }

    public int getLastSyncResultCode() {
        return lastSyncResultCode;
    }

    public AbstractDto<T> setLastSyncResultCode(int lastSyncResultCode) {
        this.lastSyncResultCode = lastSyncResultCode;
        return this;
    }

    public long getLastSyncElementCount() {
        return lastSyncElementCount;
    }

    public AbstractDto<T> setLastSyncElementCount(long lastSyncElementCount) {
        this.lastSyncElementCount = lastSyncElementCount;
        return this;
    }

    public Time getLastSyncDuration() {
        return lastSyncDuration;
    }

    public AbstractDto<T> setLastSyncDuration(Time lastSyncDuration) {
        this.lastSyncDuration = lastSyncDuration;
        return this;
    }

    public long getTotalElementsCount() {
        return totalElementsCount;
    }

    public AbstractDto<T> setTotalElementsCount(long totalElementsCount) {
        this.totalElementsCount = totalElementsCount;
        return this;
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    public AbstractDto<T> setLastErrorMessage(String lastErrorMessage) {
        this.lastErrorMessage = lastErrorMessage;
        return this;
    }

    @Override
    public String toString() {
        return "AbstractDto{"
                + "accessToken='" + accessToken
                + ", entityList=" + entityList
                + ", lastSyncResultCode=" + lastSyncResultCode
                + ", lastSyncElementCount=" + lastSyncElementCount
                + ", lastSyncDuration=" + lastSyncDuration
                + ", totalElementsCount=" + totalElementsCount
                + ", lastErrorMessage='" + lastErrorMessage
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractDto<?> that = (AbstractDto<?>) o;
        return lastSyncResultCode == that.lastSyncResultCode
                && lastSyncElementCount == that.lastSyncElementCount
                && totalElementsCount == that.totalElementsCount
                && Objects.equals(accessToken, that.accessToken)
                && Objects.equals(entityList, that.entityList)
                && Objects.equals(lastSyncDuration, that.lastSyncDuration)
                && Objects.equals(lastErrorMessage, that.lastErrorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, entityList, lastSyncResultCode, lastSyncElementCount, lastSyncDuration, totalElementsCount, lastErrorMessage);
    }

    public abstract String additionalInfo();

}
