/*
 * *******************************************************************************
 *         文 件：User.java     模 块：app      项 目：OneClean
 *         当前修改时间：2019年09月25日 11:03:46
 *         上次修改时间：2019年09月20日 18:22:49
 *         作者：liutaos@qq.com        Copyright (c) 2019
 * *******************************************************************************
 */

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
