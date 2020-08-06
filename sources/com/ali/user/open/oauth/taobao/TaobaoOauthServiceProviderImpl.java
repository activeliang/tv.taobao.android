package com.ali.user.open.oauth.taobao;

import android.app.Activity;
import android.content.Context;
import com.ali.user.open.callback.LoginCallback;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.Site;
import com.ali.user.open.core.model.LoginReturnData;
import com.ali.user.open.oauth.AppCredential;
import com.ali.user.open.oauth.OauthCallback;
import com.ali.user.open.oauth.base.BaseOauthServiceProviderImpl;
import com.ali.user.open.service.SessionService;
import com.ali.user.open.service.impl.SessionManager;
import com.ali.user.open.session.Session;
import com.ali.user.open.tbauth.TbAuthService;
import com.ali.user.open.tbauth.callback.LogoutCallback;
import com.alibaba.fastjson.JSON;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.xstate.util.XStateConstants;

public class TaobaoOauthServiceProviderImpl extends BaseOauthServiceProviderImpl {
    public static final String TAG = "oa.AlipayOauthServiceProviderImpl";

    public boolean isLoginUrl(String url) {
        return ((TbAuthService) AliMemberSDK.getService(TbAuthService.class)).isLoginUrl(url);
    }

    public void oauth(Activity activity, String oauthSite, AppCredential appCredential, Map<String, String> params, OauthCallback oauthCallback) {
        authTask(activity, oauthSite, appCredential, params, oauthCallback);
    }

    private void authTask(Activity activity, final String oauthSite, AppCredential appCredential, Map<String, String> params, final OauthCallback oauthCallback) {
        ((TbAuthService) AliMemberSDK.getService(TbAuthService.class)).auth(params, new LoginCallback() {
            public void onSuccess(Session session) {
                Map<String, String> resultparams = new HashMap<>();
                resultparams.put("openId", session.openId);
                resultparams.put(XStateConstants.KEY_ACCESS_TOKEN, session.topAccessToken);
                resultparams.put("authCode", session.topAuthCode);
                if (Site.DING.equals(AliMemberSDK.getMasterSite())) {
                    resultparams.put("userId", SessionManager.INSTANCE.getInternalSession().userId);
                    resultparams.put("sid", SessionManager.INSTANCE.getInternalSession().sid);
                }
                resultparams.put("openSid", session.openSid);
                oauthCallback.onSuccess(oauthSite, resultparams);
            }

            public void onFailure(int code, String msg) {
                oauthCallback.onFail(oauthSite, code, msg);
            }
        });
    }

    public void logout(Context context) {
        ((TbAuthService) AliMemberSDK.getService(TbAuthService.class)).logout((LogoutCallback) null);
    }

    public boolean isAppAuthSurpport(Context context) {
        return ((TbAuthService) AliMemberSDK.getService(TbAuthService.class)).isAppAuthSurpport(context);
    }

    public void refreshWhenLogin(String loginReturnDataStr, boolean cookieOnly) {
        LoginReturnData loginReturnData = (LoginReturnData) JSON.parseObject(loginReturnDataStr, LoginReturnData.class);
        if (cookieOnly) {
            ((SessionService) AliMemberSDK.getService(SessionService.class)).refreshCookie(".taobao.com", loginReturnData);
        } else {
            ((SessionService) AliMemberSDK.getService(SessionService.class)).refreshWhenLogin(Site.TAOBAO, loginReturnData);
        }
    }

    public void tokenLogin(String scene, String loginToken, String h5QueryString, Map<String, String> map, final OauthCallback oauthCallback) {
        ((TbAuthService) AliMemberSDK.getService(TbAuthService.class)).tokenLogin(0, scene, loginToken, h5QueryString, new LoginCallback() {
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
