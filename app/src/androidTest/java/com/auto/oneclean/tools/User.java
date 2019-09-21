package com.auto.oneclean.tools;

public class User {

    private String token;

    private String money;

    private String loginName;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMoney() {
        return this.money;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginName() {
        return this.loginName;
    }
}
