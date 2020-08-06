package com.ali.user.open.oauth.icbu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.ali.user.open.callback.LoginCallback;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.Site;
import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.core.model.LoginReturnData;
import com.ali.user.open.core.trace.SDKLogger;
import com.ali.user.open.oauth.AppCredential;
import com.ali.user.open.oauth.OauthCallback;
import com.ali.user.open.oauth.base.BaseOauthServiceProviderImpl;
import com.ali.user.open.service.SessionService;
import com.ali.user.open.service.impl.SessionManager;
import com.ali.user.open.session.Session;
import com.ali.user.open.tbauth.TbAuthService;
import com.ali.user.open.tbauth.callback.LogoutCallback;
import com.ali.user.open.tbauth.ui.ICBUAuthActivity;
import com.ali.user.open.tbauth.ui.context.CallbackContext;
import com.alibaba.fastjson.JSON;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.xstate.util.XStateConstants;

public class IcbuOauthServiceProviderImpl extends BaseOauthServiceProviderImpl {
    public String TAG = "login.icbuOauthImpl";

    public boolean isLoginUrl(String url) {
        return false;
    }

    public void showH5Login(Activity activity) {
        SDKLogger.d(this.TAG, "open H5 login");
        Intent intent = new Intent(activity, ICBUAuthActivity.class);
        intent.setFlags(268435456);
        KernelContext.getApplicationContext().startActivity(intent);
    }

    public void oauth(Activity activity, final String oauthSite, AppCredential appCredential, Map<String, String> map, final OauthCallback oauthCallback) {
        showH5Login(activity);
        CallbackContext.loginCallback = new LoginCallback() {
            public void onSuccess(Session session) {
                Map<String, String> resultparams = new HashMap<>();
                resultparams.put("openId", session.openId);
                resultparams.put("bindToken", session.bindToken);
                resultparams.put(XStateConstants.KEY_ACCESS_TOKEN, session.topAccessToken);
                resultparams.put("authCode", session.topAuthCode);
                resultparams.put("hid", session.hid);
                oauthCallback.onSuccess(oauthSite, resultparams);
            }

            public void onFailure(int code, String msg) {
                oauthCallback.onFail(oauthSite, code, msg);
            }
        };
    }

    public void logout(Context context) {
        ((TbAuthService) AliMemberSDK.getService(TbAuthService.class)).logout((LogoutCallback) null);
    }

    public boolean isAppAuthSurpport(Context context) {
        return false;
    }

    public void refreshWhenLogin(String loginReturnDataStr, boolean cookieOnly) {
        LoginReturnData loginReturnData = (LoginReturnData) JSON.parseObject(loginReturnDataStr, LoginReturnData.class);
        if (cookieOnly) {
            ((SessionService) AliMemberSDK.getService(SessionService.class)).refreshCookie(".alibaba.com", loginReturnData);
        } else {
            ((SessionService) AliMemberSDK.getService(SessionService.class)).refreshWhenLogin(Site.ICBU, loginReturnData);
        }
    }

    public void tokenLogin(String scene, String loginToken, String h5QueryString, Map<String, String> map, final OauthCallback oauthCallback) {
        ((TbAuthService) AliMemberSDK.getService(TbAuthService.class)).tokenLogin(4, scene, loginToken, h5QueryString, new LoginCallback() {
            public void onSuccess(Session session) {
                Map<String, String> map = new HashMap<>();
                if (Site.DING.equals(AliMemberSDK.getMasterSite())) {
                    map.put("userId", SessionManager.INSTANCE.getInternalSession().userId);
                    map.put("sid", SessionManager.INSTANCE.getInternalSession().sid);
                }
                oauthCallback.onSuccess(Site.TAOBAO, map);
            }

            public void onFailure(int code, String msg) {
                oauthCallback.onFail(Site.TAOBAO, code, msg);
            }
        });
    }
}
