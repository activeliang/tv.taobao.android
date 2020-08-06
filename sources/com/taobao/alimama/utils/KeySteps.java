package com.taobao.alimama.utils;

import android.text.TextUtils;
import com.taobao.alimama.global.Constants;
import com.taobao.alimama.login.LoginManager;
import com.taobao.muniontaobaosdk.util.TaoLog;
import com.taobao.utils.ILoginInfoGetter;
import com.taobao.utils.LoginInfo;
import mtopsdk.common.util.SymbolExpUtil;

public final class KeySteps {
    public static void mark(String str, String... strArr) {
        LoginInfo lastLoginUserInfo;
        String str2 = "";
        String str3 = "";
        ILoginInfoGetter loginInfoGetter = LoginManager.getLoginInfoGetter();
        if (!(loginInfoGetter == null || (lastLoginUserInfo = loginInfoGetter.getLastLoginUserInfo()) == null || !lastLoginUserInfo.isValid())) {
            str2 = lastLoginUserInfo.nickname;
            str3 = lastLoginUserInfo.userId;
        }
        TaoLog.Logi(Constants.TAG, String.format("[step=%s,th=%s,ver=%s,user=%s,uid=%s,bkt=%s] args: %s", new Object[]{str, Thread.currentThread().getName(), "5.0.2-proguard", str2, str3, TextUtils.join(SymbolExpUtil.SYMBOL_SEMICOLON, BucketTools.a()), TextUtils.join(",", strArr)}));
        UserTrackLogs.trackDebugLog(str, strArr);
    }
}
