package com.ali.auth.third.core.model;

public class User {
    public String avatarUrl;
    public String cbuLoginId;
    public String deviceTokenKey;
    public String deviceTokenSalt;
    public String email;
    public String memberId;
    public String nick;
    public String openId;
    public String openSid;
    public String userId;

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("User [userId=");
        builder.append(this.userId);
        builder.append(", openId=");
        builder.append(this.openId);
        builder.append(", openSid= ");
        builder.append(this.openSid);
        builder.append(", nick=");
        builder.append(this.nick);
        builder.append(", email=");
        builder.append(this.email);
        builder.append(",cbuloginId=");
        builder.append(this.cbuLoginId);
        builder.append(",memberId=");
        builder.append(this.memberId);
        builder.append(",deviceTokenKey=");
        builder.append(this.deviceTokenKey + "");
        builder.append(",deviceTokenSalt=");
        builder.append(this.deviceTokenSalt + "");
        builder.append("]");
        return builder.toString();
    }
}
