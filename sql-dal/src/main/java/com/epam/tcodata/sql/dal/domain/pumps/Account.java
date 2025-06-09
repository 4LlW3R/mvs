package com.epam.tcodata.sql.dal.domain.pumps;

import com.epam.tcodata.sql.dal.IStorable;
import com.epam.tcodata.sql.dal.domain.PrimaryKey;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.io.Serializable;

public class Account implements IStorable, Serializable {

    private static final long serialVersionUID = 3424063103683439024L;

    public static class Fields {
        public static final String ID = "ID";
        public static final String ACCOUNT_NAME = "AccountName";
        public static final String ACCOUNT_KEY_VAULT_NAME = "AccountKeyVaultName";
        public static final String IS_ACTIVE = "IsActive";
        private Fields(){   /***  Default implementation ***/  }
    }

    @PrimaryKey
    @ColumnName(Fields.ID)
    private long id;

    @ColumnName(Fields.ACCOUNT_NAME)
    private String accountName;

    @ColumnName(Fields.ACCOUNT_KEY_VAULT_NAME)
    private String accountKeyVaultName;

    @ColumnName(Fields.IS_ACTIVE)
    private boolean active;

    public Account() {
    }

    /**
     * Main public constructor.
     *
     * @param id                  surrogate id.
     * @param accountName         account name.
     * @param accountKeyVaultName name of account in key vault.
     * @param isActive            account status.
     */
    public Account(long id, String accountName, String accountKeyVaultName, boolean isActive) {
        this.id = id;
        this.accountName = accountName;
        this.accountKeyVaultName = accountKeyVaultName;
        this.active = isActive;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountKeyVaultName() {
        return accountKeyVaultName;
    }

    public void setAccountKeyVaultName(String accountKeyVaultName) {
        this.accountKeyVaultName = accountKeyVaultName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (id != account.id) return false;
        if (active != account.active) return false;
        if (accountName != null ? !accountName.equals(account.accountName) : account.accountName != null) return false;
        return accountKeyVaultName != null ? accountKeyVaultName.equals(account.accountKeyVaultName) : account.accountKeyVaultName == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (accountName != null ? accountName.hashCode() : 0);
        result = 31 * result + (accountKeyVaultName != null ? accountKeyVaultName.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Account{"
                + "id=" + id
                + ", accountName='" + accountName + '\''
                + ", accountKeyVaultName='" + accountKeyVaultName + '\''
                + ", isActive=" + active
                + '}';
    }
}
