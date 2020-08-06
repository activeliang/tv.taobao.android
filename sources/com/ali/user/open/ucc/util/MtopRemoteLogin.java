package com.ali.user.open.ucc.util;

import com.ali.user.open.callback.LoginCallback;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.Site;
import com.ali.user.open.session.Session;
import com.ali.user.open.ucc.UccCallback;
import com.ali.user.open.ucc.UccService;
import java.util.Map;

public class MtopRemoteLogin {
    public static void login(final LoginCallback callback) {
        ((UccService) AliMemberSDK.getService(UccService.class)).trustLogin(Site.TAOBAO, (Map<String, String>) null, (UccCallback) new UccCallback() {
            public void onSuccess(String targetSite, Map params) {
                if (callback != null) {
                    callback.onSuccess((Session) null);
                }
            }

            public void onFail(String targetSite, int code, String msg) {
                if (callback != null) {
                    callback.onFailure(code, msg);
                }
            }
        });
    }
}
