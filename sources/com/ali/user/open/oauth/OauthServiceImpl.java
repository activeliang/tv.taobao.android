package com.ali.user.open.oauth;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.Site;
import com.ali.user.open.core.device.DeviceInfo;
import com.ali.user.open.core.service.UserTrackerService;
import com.ali.user.open.core.util.ParamsConstants;
import com.ali.user.open.oauth.alipay3.AlipayOauthServiceProviderImpl;
import com.ali.user.open.oauth.damai.DamaiOauthServiceProviderImpl;
import com.ali.user.open.oauth.eleme.ElemeOauthServiceProviderImpl;
import com.ali.user.open.oauth.icbu.IcbuOauthServiceProviderImpl;
import com.ali.user.open.oauth.taobao.TaobaoOauthServiceProviderImpl;
import java.util.HashMap;
import java.util.Map;

public class OauthServiceImpl implements OauthService {
    private Map<String, OauthServiceProvider> mServiceProviderMap = new HashMap();

    public boolean isLoginUrl(String oauthSite, String url) {
        if (getOauthServiceProvider(oauthSite) != null) {
            return getOauthServiceProvider(oauthSite).isLoginUrl(url);
        }
        return false;
    }

    public void oauth(Activity activity, String oauthSite, OauthCallback oauthCallback) {
        Map<String, String> params = new HashMap<>();
        params.put(ParamsConstants.Key.PARAM_NEED_AUTOLOGIN, "0");
        params.put("needSession", "0");
        oauth(activity, oauthSite, params, oauthCallback);
    }

    public void oauth(Activity activity, String oauthSite, Map<String, String> params, OauthCallback oauthCallback) {
        Map<String, String> props = new HashMap<>();
        props.put("oauthsite", oauthSite);
        if (params == null || TextUtils.isEmpty(params.get(ParamsConstants.Key.PARAM_TRACE_ID))) {
            String traceId = "oauth" + DeviceInfo.deviceId + (System.currentTimeMillis() / 1000);
            props.put(ParamsConstants.Key.PARAM_TRACE_ID, traceId);
            if (params == null) {
                params = new HashMap<>();
            }
            params.put(ParamsConstants.Key.PARAM_TRACE_ID, traceId);
        } else {
            props.put(ParamsConstants.Key.PARAM_TRACE_ID, params.get(ParamsConstants.Key.PARAM_TRACE_ID));
        }
        ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).send("Page_UccOauth", "Page_UccOauth_Invoke", props);
        AppCredential appCredential = OauthPlatformConfig.getOauthConfigByPlatform(oauthSite);
        if (getOauthServiceProvider(oauthSite) != null) {
            getOauthServiceProvider(oauthSite).oauth(activity, oauthSite, appCredential, params, oauthCallback);
        }
    }

    public boolean isAppAuthSurpport(Context context, String oauthSite) {
        if (getOauthServiceProvider(oauthSite) != null) {
            return getOauthServiceProvider(oauthSite).isAppAuthSurpport(context);
        }
        return false;
    }

    public void refreshWhenLogin(String oauthSite, String loginReturnData, boolean cookieOnly) {
        if (getOauthServiceProvider(oauthSite) != null) {
            getOauthServiceProvider(oauthSite).refreshWhenLogin(loginReturnData, cookieOnly);
        }
    }

    public void tokenLogin(String oauthSite, String scene, String loginToken, String h5QueryString, Map<String, String> params, OauthCallback oauthCallback) {
        if (getOauthServiceProvider(oauthSite) != null) {
            getOauthServiceProvider(oauthSite).tokenLogin(scene, loginToken, h5QueryString, params, oauthCallback);
        }
    }

    public void logout(Context context, String oauthSite) {
        if (getOauthServiceProvider(oauthSite) != null) {
            getOauthServiceProvider(oauthSite).logout(context);
        }
    }

    public void logoutAll(Context context) {
        if (getOauthServiceProvider(Site.TAOBAO) != null) {
            getOauthServiceProvider(Site.TAOBAO).logout(context);
        }
        if (getOauthServiceProvider("alipay") != null) {
            getOauthServiceProvider("alipay").logout(context);
        }
        if (getOauthServiceProvider(Site.ELEME) != null) {
            getOauthServiceProvider(Site.ELEME).logout(context);
        }
        if (getOauthServiceProvider(Site.DAMAI) != null) {
            getOauthServiceProvider(Site.DAMAI).logout(context);
        }
        if (getOauthServiceProvider(Site.YOUKU) != null) {
            getOauthServiceProvider(Site.YOUKU).logout(context);
        }
        if (getOauthServiceProvider(Site.STARBUCKS) != null) {
            getOauthServiceProvider(Site.STARBUCKS).logout(context);
        }
        if (getOauthServiceProvider(Site.XIAMI) != null) {
            getOauthServiceProvider(Site.XIAMI).logout(context);
        }
        if (getOauthServiceProvider(Site.LAIFENG) != null) {
            getOauthServiceProvider(Site.LAIFENG).logout(context);
        }
    }

    private OauthServiceProvider getOauthServiceProvider(String oauthSite) {
        if (TextUtils.equals(oauthSite, "alipay")) {
            if (this.mServiceProviderMap.get("alipay") == null) {
                this.mServiceProviderMap.put("alipay", new AlipayOauthServiceProviderImpl());
            }
            return this.mServiceProviderMap.get("alipay");
        } else if (TextUtils.equals(oauthSite, Site.TAOBAO)) {
            if (this.mServiceProviderMap.get(Site.TAOBAO) == null) {
                this.mServiceProviderMap.put(Site.TAOBAO, new TaobaoOauthServiceProviderImpl());
            }
            return this.mServiceProviderMap.get(Site.TAOBAO);
        } else if (TextUtils.equals(oauthSite, Site.ELEME)) {
            if (this.mServiceProviderMap.get(Site.ELEME) == null) {
                this.mServiceProviderMap.put(Site.ELEME, new ElemeOauthServiceProviderImpl());
            }
            return this.mServiceProviderMap.get(Site.ELEME);
        } else if (TextUtils.equals(oauthSite, Site.ICBU)) {
            if (this.mServiceProviderMap.get(Site.ICBU) == null) {
                this.mServiceProviderMap.put(Site.ICBU, new IcbuOauthServiceProviderImpl());
            }
            return this.mServiceProviderMap.get(Site.ICBU);
        } else if (!TextUtils.equals(oauthSite, Site.DAMAI)) {
            return null;
        } else {
            if (this.mServiceProviderMap.get(Site.DAMAI) == null) {
                this.mServiceProviderMap.put(Site.DAMAI, new DamaiOauthServiceProviderImpl());
            }
            return this.mServiceProviderMap.get(Site.DAMAI);
        }
    }

    public void cleanUp() {
        this.mServiceProviderMap.clear();
    }
}
