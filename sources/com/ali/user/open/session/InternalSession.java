package com.ali.user.open.session;

import com.bftv.fui.constantplugin.Constant;
import java.util.Map;

public class InternalSession {
    public String autoLoginToken;
    public String avatarUrl;
    public String bindToken;
    public String deviceTokenKey;
    public String deviceTokenSalt;
    public String email;
    public long expireIn;
    public String[] externalCookies;
    public String loginId;
    public long loginTime;
    public String mobile;
    public String nick;
    public String openId;
    public String openSid;
    public Map<String, Object> otherInfo;
    public String sid;
    public String site;
    public String topAccessToken;
    public String topAuthCode;
    public String topExpireTime;
    public String userId;

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("InternalSession [sid=");
        builder.append(this.sid);
        builder.append(", expireIn=");
        builder.append(this.expireIn);
        builder.append(", loginTime=");
        builder.append(this.loginTime);
        builder.append(", autoLoginToken=");
        builder.append(this.autoLoginToken);
        builder.append(",topAccessToken=");
        builder.append(this.topAccessToken);
        builder.append(",topAuthCode");
        builder.append(this.topAuthCode);
        builder.append(",topExpireTime");
        builder.append(this.topExpireTime);
        builder.append(", otherInfo=");
        builder.append(this.otherInfo);
        builder.append(", cookies=");
        if (this.externalCookies != null) {
            for (String entry : this.externalCookies) {
                builder.append(entry);
            }
        } else {
            builder.append(Constant.NULL);
        }
        builder.append("]");
        return builder.toString();
    }
}
