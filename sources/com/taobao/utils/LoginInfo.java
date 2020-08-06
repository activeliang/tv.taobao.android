package com.taobao.utils;

import android.text.TextUtils;
import com.alibaba.fastjson.annotation.JSONField;
import com.taobao.tao.remotebusiness.login.LoginContext;
import java.io.Serializable;

public class LoginInfo implements Serializable {
    @JSONField(name = "nickname")
    public String nickname;
    @JSONField(name = "user_id")
    public String userId;

    public static LoginInfo from(LoginContext loginContext) {
        LoginInfo loginInfo = new LoginInfo();
        if (loginContext != null) {
            loginInfo.nickname = loginContext.nickname;
            loginInfo.userId = loginContext.userId;
        }
        return loginInfo;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof LoginInfo)) {
            return false;
        }
        LoginInfo loginInfo = (LoginInfo) obj;
        return TextUtils.equals(loginInfo.nickname, this.nickname) && TextUtils.equals(loginInfo.userId, this.userId);
    }

    @JSONField(serialize = false)
    public boolean isValid() {
        return !TextUtils.isEmpty(this.userId) && !TextUtils.isEmpty(this.nickname);
    }
}
