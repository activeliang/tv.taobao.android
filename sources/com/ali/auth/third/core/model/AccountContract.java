package com.ali.auth.third.core.model;

public class AccountContract {
    String hash;
    String hash_key;
    String nick;
    String openid;
    String userid;

    public AccountContract(String hash2, String openid2, String userid2, String nick2) {
        this.userid = userid2;
        this.openid = openid2;
        this.nick = nick2;
        this.hash = hash2;
    }

    public String getUserid() {
        return this.userid;
    }

    public void setUserid(String userid2) {
        this.userid = userid2;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String nick2) {
        this.nick = nick2;
    }

    public String getHash() {
        return this.hash;
    }

    public void setHash(String hash2) {
        this.hash = hash2;
    }

    public String getHash_key() {
        return this.hash_key;
    }

    public void setHash_key(String hash_key2) {
        this.hash_key = hash_key2;
    }

    public String getOpenid() {
        return this.openid;
    }

    public void setOpenid(String openid2) {
        this.openid = openid2;
    }
}
