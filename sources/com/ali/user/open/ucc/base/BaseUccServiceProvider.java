package com.ali.user.open.ucc.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.Site;
import com.ali.user.open.core.model.LoginDataModel;
import com.ali.user.open.core.model.LoginReturnData;
import com.ali.user.open.core.util.ParamsConstants;
import com.ali.user.open.oauth.AppCredential;
import com.ali.user.open.oauth.OauthCallback;
import com.ali.user.open.oauth.OauthService;
import com.ali.user.open.service.SessionService;
import com.ali.user.open.ucc.UccCallback;
import com.ali.user.open.ucc.UccServiceProvider;
import com.ali.user.open.ucc.biz.UccBindPresenter;
import com.ali.user.open.ucc.biz.UccH5Presenter;
import com.ali.user.open.ucc.model.UccParams;
import com.ali.user.open.ucc.util.UTHitConstants;
import com.ali.user.open.ucc.util.UTHitUtils;
import com.alibaba.fastjson.JSON;
import com.yunos.tvtaobao.payment.utils.TvTaoSharedPerference;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.common.util.HttpHeaderConstant;
import mtopsdk.xstate.util.XStateConstants;

public abstract class BaseUccServiceProvider implements UccServiceProvider {
    public static final String TAG = "TaobaoUccServiceProviderImpl";

    /* access modifiers changed from: protected */
    public abstract boolean isAuthByNative(Context context, String str, Map<String, String> map);

    public void bind(Activity activity, UccParams uccParams, AppCredential appCredential, Map<String, String> params, UccCallback uccCallback) {
        if (uccParams == null || TextUtils.isEmpty(uccParams.userToken)) {
            UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_InvalidParams", uccParams, (Map<String, String>) null);
            if (uccCallback != null) {
                uccCallback.onFail(uccParams.bindSite, 102, "参数错误");
                return;
            }
            return;
        }
        boolean h5Only = false;
        if (params != null && TextUtils.equals(params.get(ParamsConstants.Key.PARAM_H5ONLY), "1")) {
            h5Only = true;
        }
        if (h5Only || !isAuthByNative(activity, uccParams.bindSite, params)) {
            UccH5Presenter.showH5BindPage(activity, uccParams, params, uccCallback);
            return;
        }
        Map<String, String> props = new HashMap<>();
        props.put("from", "bind");
        if (params != null) {
            props.put("scene", params.get("scene"));
            props.put("needSession", TextUtils.equals(params.get("needSession"), "1") ? "T" : "F");
        }
        UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_GoOauthBindAction", uccParams, props);
        authByNatvie(activity, uccParams, appCredential, params, uccCallback);
    }

    private void authByNatvie(Activity activity, UccParams uccParams, AppCredential appCredential, Map<String, String> mapParams, UccCallback uccCallback) {
        if (mapParams == null) {
            mapParams = new HashMap<>();
        }
        mapParams.put(ParamsConstants.Key.PARAM_ONLY_AUTHCODE, "1");
        mapParams.put(ParamsConstants.Key.PARAM_IS_BIND, "1");
        final Map<String, String> finalMapParams = mapParams;
        finalMapParams.put(ParamsConstants.Key.PARAM_TRACE_ID, uccParams.traceId);
        final Activity activity2 = activity;
        final UccParams uccParams2 = uccParams;
        final UccCallback uccCallback2 = uccCallback;
        ((OauthService) AliMemberSDK.getService(OauthService.class)).oauth(activity, uccParams.bindSite, finalMapParams, new OauthCallback() {
            public void onSuccess(String targetSite, Map params) {
                String bindToken;
                resultHit("3000");
                String authCode = (String) params.get("authCode");
                String accessToken = (String) params.get(XStateConstants.KEY_ACCESS_TOKEN);
                if (TextUtils.isEmpty(accessToken)) {
                    bindToken = authCode;
                } else {
                    bindToken = accessToken;
                }
                UccBindPresenter.getInstance().bindByNativeAuth(activity2, uccParams2, bindToken, TextUtils.isEmpty(accessToken) ? "oauthcode" : HttpHeaderConstant.KEY_EXTDATA_ACCESSTOKEN, finalMapParams, uccCallback2);
            }

            public void onFail(String targetSite, int code, String msg) {
                resultHit(code + "");
                if (code == 10003 || code == 15) {
                    UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_Cancel", uccParams2, new HashMap<>());
                } else if (code == 1011) {
                    Map<String, String> mapParams = new HashMap<>();
                    mapParams.put("needSession", "1");
                    UccBindPresenter.getInstance().getUserInfo(activity2, uccParams2, uccParams2.userToken, "userToken", "native", mapParams, uccCallback2);
                }
                if (uccCallback2 != null) {
                    uccCallback2.onFail(targetSite, code, msg);
                }
            }

            private void resultHit(String code) {
                Map<String, String> props = new HashMap<>();
                props.put("code", code);
                UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_GoOauthResult", uccParams2, props);
            }
        });
    }

    private void authByNatvieWithIbb(Activity activity, UccParams uccParams, Map<String, String> mapParams, UccCallback uccCallback) {
        if (mapParams == null) {
            mapParams = new HashMap<>();
        }
        final Map<String, String> finalMapParams = mapParams;
        finalMapParams.put(ParamsConstants.Key.PARAM_TRACE_ID, uccParams.traceId);
        final Activity activity2 = activity;
        final UccParams uccParams2 = uccParams;
        final UccCallback uccCallback2 = uccCallback;
        ((OauthService) AliMemberSDK.getService(OauthService.class)).oauth(activity, uccParams.bindSite, finalMapParams, new OauthCallback() {
            public void onSuccess(String targetSite, Map params) {
                resultHit("3000");
                UccBindPresenter.getInstance().getUserInfo(activity2, uccParams2, (String) params.get("authCode"), "oauthcode", "native", finalMapParams, uccCallback2);
            }

            public void onFail(String targetSite, int code, String msg) {
                resultHit(code + "");
                if (code == 10003 || code == 15) {
                    UTHitUtils.send(UTHitConstants.PageUccBind, "UccBindWithIbb_Cancel", uccParams2, (Map<String, String>) null);
                } else if (code == 1011) {
                    UTHitUtils.send(UTHitConstants.PageUccBind, "UccBindWithIbb_NativeSkip", uccParams2, (Map<String, String>) null);
                }
                if (uccCallback2 != null) {
                    uccCallback2.onFail(targetSite, code, msg);
                }
            }

            private void resultHit(String code) {
                Map<String, String> props = new HashMap<>();
                props.put("code", code);
                UTHitUtils.send(UTHitConstants.PageUccBind, "UccBindWithIbb_GoOauthResult", uccParams2, props);
            }
        });
    }

    public void refreshWhenLogin(String targetSite, String loginReturnData, boolean cookieOnly) {
        String domain = ".taobao.com";
        if (TextUtils.equals(targetSite, "alipay")) {
            domain = ".alipay.com";
        } else if (TextUtils.equals(targetSite, Site.DAMAI)) {
            domain = ".damai.cn";
        } else if (TextUtils.equals(targetSite, Site.ELEME)) {
            domain = ".ele.me";
        }
        ((SessionService) AliMemberSDK.getService(SessionService.class)).refreshCookie(domain, (LoginReturnData) JSON.parseObject(loginReturnData, LoginReturnData.class));
    }

    public void logout(Context context) {
    }

    public void cleanUp(Context context) {
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public void bindWithIBB(Activity activity, UccParams uccParams, String ibb, Map<String, String> params, UccCallback uccCallback) {
        if (uccParams == null || TextUtils.isEmpty(ibb)) {
            UTHitUtils.send(UTHitConstants.PageUccBind, "UccBindWithIbb_InvalidParams", uccParams, (Map<String, String>) null);
            if (uccCallback != null) {
                uccCallback.onFail(uccParams.bindSite, 102, "参数错误");
            }
        } else if (isAuthByNative(activity, uccParams.bindSite, params)) {
            Map<String, String> props = new HashMap<>();
            props.put("from", "bind");
            if (params != null) {
                props.put("scene", params.get("scene"));
                props.put("needSession", TextUtils.equals(params.get("needSession"), "1") ? "T" : "F");
            }
            UTHitUtils.send(UTHitConstants.PageUccBind, "UccBindWithIbb_GoOauthBindAction", uccParams, props);
            if (params == null) {
                params = new HashMap<>();
            }
            params.put(ParamsConstants.Key.PARAM_SCENE_CODE, "10010");
            params.put("ibb", ibb);
            authByNatvieWithIbb(activity, uccParams, params, uccCallback);
        } else {
            UccH5Presenter.showH5BindPageFoeNewBind(activity, uccParams, ibb, params, uccCallback);
        }
    }

    public Map buildSessionInfo(String site, String loginResultData) {
        LoginDataModel loginDataModel;
        Map sessionInfo = new HashMap();
        if (Site.isHavanaSite(site)) {
            try {
                LoginReturnData loginReturnData = (LoginReturnData) JSON.parseObject(loginResultData, LoginReturnData.class);
                if (!(loginReturnData == null || (loginDataModel = (LoginDataModel) JSON.parseObject(loginReturnData.data, LoginDataModel.class)) == null)) {
                    sessionInfo.put("openId", loginDataModel.openId);
                    sessionInfo.put("bindToken", loginDataModel.bindToken);
                    sessionInfo.put(XStateConstants.KEY_ACCESS_TOKEN, loginDataModel.topAccessToken);
                    sessionInfo.put("authCode", loginDataModel.topAuthCode);
                    sessionInfo.put("userId", loginDataModel.userId);
                    sessionInfo.put("sid", loginDataModel.sid);
                    sessionInfo.put(TvTaoSharedPerference.NICK, loginDataModel.nick);
                    sessionInfo.put("avatarUrl", loginDataModel.headPicLink);
                    sessionInfo.put("openSid", loginDataModel.openSid);
                }
            } catch (Throwable th) {
            }
        }
        return sessionInfo;
    }
}
