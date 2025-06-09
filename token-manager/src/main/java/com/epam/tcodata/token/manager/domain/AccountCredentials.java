package com.epam.tcodata.token.manager.domain;

import java.io.Serializable;

public class AccountCredentials implements Serializable {

    private static final long serialVersionUID = -884816263887824953L;
    private String username;
    private String pwd;

    public AccountCredentials(String username, String pwd) {
        this.username = username;
        this.pwd = pwd;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "AccountCredentials{"
                + "username='" + username + '\''
                + ", p**swd='*******'"
                + '}';
    }
}
