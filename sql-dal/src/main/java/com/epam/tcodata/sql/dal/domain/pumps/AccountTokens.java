package com.epam.tcodata.sql.dal.domain.pumps;

import com.epam.tcodata.sql.dal.IStorable;
import com.epam.tcodata.sql.dal.domain.PrimaryKey;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.io.Serializable;
import java.time.Instant;

public class AccountTokens implements IStorable, Serializable {

    private static final long serialVersionUID = -6465646726040731441L;

    public static class Fields {
        public static final String ID = "ID";
        public static final String ACCOUNT_ID = "AccountId";
        public static final String ACCESS_TOKEN = "AccessToken";
        public static final String REFRESH_TOKEN = "RefreshToken";
        public static final String LAST_SYNC_DATE_UTC = "LastSyncDateUtc";
        public static final String EXPIRATION_DATE_UTC = "ExpirationDateUtc";
        private Fields(){   /***  Default implementation ***/  }
    }

    @PrimaryKey
    @ColumnName(Fields.ID)
    private long id;

    @ColumnName(Fields.ACCOUNT_ID)
    private long accountId;

    @ColumnName(Fields.ACCESS_TOKEN)
    private String accessToken;

    @ColumnName(Fields.REFRESH_TOKEN)
    private String refreshToken;

    @ColumnName(Fields.LAST_SYNC_DATE_UTC)
    private Instant lastSyncDateUtc;

    @ColumnName(Fields.EXPIRATION_DATE_UTC)
    private Instant expirationDateUtc;

    public AccountTokens() {

    }

    /**
     * Create account token.
     *
     * @param id                surrogate id.
     * @param accountId         account id.
     * @param accessToken       token for accessing data.
     * @param refreshToken      token for refreshing tokens.
     * @param lastSyncDateUtc   last synchronization date.
     * @param expirationDateUtc date of expiration.
     */
    public AccountTokens(long id, long accountId, String accessToken, String refreshToken,
                         Instant lastSyncDateUtc, Instant expirationDateUtc) {
        this.id = id;
        this.accountId = accountId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.lastSyncDateUtc = lastSyncDateUtc;
        this.expirationDateUtc = expirationDateUtc;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getParentId() {
        return accountId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Instant getLastSyncDateUtc() {
        return lastSyncDateUtc;
    }

    public void setLastSyncDateUtc(Instant lastSyncDateUtc) {
        this.lastSyncDateUtc = lastSyncDateUtc;
    }

    public Instant getExpirationDateUtc() {
        return expirationDateUtc;
    }

    public void setExpirationDateUtc(Instant expirationDateUtc) {
        this.expirationDateUtc = expirationDateUtc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountTokens that = (AccountTokens) o;

        if (id != that.id) return false;
        if (accountId != that.accountId) return false;
        if (accessToken != null ? !accessToken.equals(that.accessToken) : that.accessToken != null) return false;
        if (refreshToken != null ? !refreshToken.equals(that.refreshToken) : that.refreshToken != null) return false;
        if (lastSyncDateUtc != null ? !lastSyncDateUtc.equals(that.lastSyncDateUtc) : that.lastSyncDateUtc != null)
            return false;
        return expirationDateUtc != null ? expirationDateUtc.equals(that.expirationDateUtc) : that.expirationDateUtc == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (accountId ^ (accountId >>> 32));
        result = 31 * result + (accessToken != null ? accessToken.hashCode() : 0);
        result = 31 * result + (refreshToken != null ? refreshToken.hashCode() : 0);
        result = 31 * result + (lastSyncDateUtc != null ? lastSyncDateUtc.hashCode() : 0);
        result = 31 * result + (expirationDateUtc != null ? expirationDateUtc.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AccountTokens{"
                + "id=" + id
                + ", accountId=" + accountId
                + ", accessToken='" + accessToken + '\''
                + ", refreshToken='" + refreshToken + '\''
                + ", lastSyncDateUtc=" + lastSyncDateUtc
                + ", expirationDateUtc=" + expirationDateUtc
                + '}';
    }
}
