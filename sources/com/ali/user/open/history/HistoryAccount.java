package com.ali.user.open.history;

public class HistoryAccount {
    public String email;
    public String mobile;
    public String nick;
    public String t;
    public String tokenKey;
    public String userId;

    public HistoryAccount() {
    }

    public HistoryAccount(String id, String token, String nick2, String mobile2, String email2) {
        this.userId = id;
        this.tokenKey = token;
        this.nick = nick2;
        this.mobile = mobile2;
        this.email = email2;
    }

    public boolean equals(Object o) {
        return super.equals(o);
    }

    public String toString() {
        return "" + this.nick;
    }
}
