package com.ali.user.open.ucc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.callback.MemberCallback;
import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.core.trace.SDKLogger;
import com.ali.user.open.core.util.ParamsConstants;
import com.ali.user.open.oauth.OauthCallback;
import com.ali.user.open.oauth.OauthPlatformConfig;
import com.ali.user.open.oauth.OauthService;
import com.ali.user.open.ucc.biz.UccBizContants;
import com.ali.user.open.ucc.biz.UccOauthLoginPresenter;
import com.ali.user.open.ucc.biz.UccTrustLoginPresenter;
import com.ali.user.open.ucc.biz.UccUnbindPresenter;
import com.ali.user.open.ucc.context.UccContext;
import com.ali.user.open.ucc.model.UccParams;
import com.ali.user.open.ucc.ui.UccActivity;
import com.ali.user.open.ucc.util.UTHitConstants;
import com.ali.user.open.ucc.util.UTHitUtils;
import com.ali.user.open.ucc.util.UccConstants;
import com.ali.user.open.ucc.util.Utils;
import com.taobao.orange.OrangeConfig;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import java.util.HashMap;
import java.util.Map;

public class UccServiceImpl implements UccService {
    public static final String TAG = "UccServiceImpl";
    private UccDataProvider mUccDataProvider;
    private Map<String, UccDataProvider> mUccDataProviderContainer = new HashMap();

    public boolean isLoginUrl(String targetSite, String url) {
        Integer time = UccBizContants.mTrustLoginErrorTime.get(targetSite);
        if (time == null || time.intValue() <= 3) {
            return ((OauthService) AliMemberSDK.getService(OauthService.class)).isLoginUrl(targetSite, url);
        }
        return false;
    }

    public void bind(Activity activity, String userToken, String targetSite, UccCallback uccCallback) {
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("needSession", "0");
        bind(activity, userToken, targetSite, mapParams, uccCallback);
    }

    public void bind(Activity activity, @NonNull String userToken, String targetSite, Map<String, String> params, UccCallback uccCallback) {
        UccParams uccParams = new UccParams();
        uccParams.traceId = Utils.generateTraceId("bind");
        uccParams.bindSite = targetSite;
        uccParams.userToken = userToken;
        Map<String, String> props1 = new HashMap<>();
        if (params != null) {
            uccParams.miniAppId = params.get("miniAppId");
            props1.put("scene", params.get("scene"));
            props1.put("needSession", TextUtils.equals(params.get("needSession"), "1") ? "T" : "F");
            uccParams.sdkVersion = params.get("sdkVersion");
        }
        UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_Invoke", uccParams, props1);
        doBind(activity, uccParams, targetSite, params, uccCallback);
    }

    public void bind(@NonNull String userToken, String targetSite, Map<String, String> params, UccCallback uccCallback) {
        SDKLogger.d(TAG, "bind goUccActivity");
        UccActivity.mUccCallback = uccCallback;
        Intent intent = new Intent();
        intent.putExtra(UccConstants.PARAM_FUNC_TYPE, 2);
        intent.putExtra(UccConstants.PARAM_TARGET_SITE, targetSite);
        intent.putExtra("userToken", userToken);
        if (params != null) {
            intent.putExtra("needSession", params.get("needSession"));
            intent.putExtra(ParamsConstants.Key.PARAM_NEED_AUTOLOGIN, params.get(ParamsConstants.Key.PARAM_NEED_AUTOLOGIN));
            intent.putExtra("scene", params.get("scene"));
        }
        intent.setClass(KernelContext.getApplicationContext(), UccActivity.class);
        intent.setFlags(268435456);
        KernelContext.getApplicationContext().startActivity(intent);
    }

    public void bind(Activity activity, String targetSite, UccCallback uccCallback) {
        final UccParams uccParams = new UccParams();
        uccParams.traceId = Utils.generateTraceId("bind");
        uccParams.bindSite = targetSite;
        Map<String, String> props1 = new HashMap<>();
        props1.put("needSession", "F");
        UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_Invoke", uccParams, props1);
        if (this.mUccDataProvider == null) {
            Map<String, String> props = new HashMap<>();
            props.put("bindSite", targetSite);
            UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_UserTokenNIL", (UccParams) null, props);
            if (uccCallback != null) {
                uccCallback.onFail(targetSite, 1003, "data provider为空");
                return;
            }
            return;
        }
        final Activity activity2 = activity;
        final String str = targetSite;
        final UccCallback uccCallback2 = uccCallback;
        this.mUccDataProvider.getUserToken(targetSite, new MemberCallback<String>() {
            public void onSuccess(String userToken) {
                uccParams.userToken = userToken;
                UccServiceImpl.this.doBind(activity2, uccParams, str, (Map<String, String>) null, uccCallback2);
            }

            public void onFailure(int code, String msg) {
                Map<String, String> props = new HashMap<>();
                props.put("bindSite", str);
                UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_UserTokenNIL", (UccParams) null, props);
                if (uccCallback2 != null) {
                    UccCallback uccCallback = uccCallback2;
                    String str = str;
                    if (TextUtils.isEmpty(msg)) {
                        msg = "userToken为空";
                    }
                    uccCallback.onFail(str, 1004, msg);
                }
            }
        });
    }

    public void bind(Activity activity, String targetSite, Map<String, String> params, UccCallback uccCallback) {
        final UccParams uccParams = new UccParams();
        uccParams.traceId = Utils.generateTraceId("bind");
        uccParams.bindSite = targetSite;
        if (params != null && !TextUtils.isEmpty(params.get("scene"))) {
            uccParams.scene = params.get("scene");
        }
        Map<String, String> props1 = new HashMap<>();
        if (params != null) {
            uccParams.miniAppId = params.get("miniAppId");
            props1.put("scene", params.get("scene"));
            props1.put("needSession", TextUtils.equals(params.get("needSession"), "1") ? "T" : "F");
            uccParams.sdkVersion = params.get("sdkVersion");
        }
        UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_Invoke", uccParams, props1);
        if (this.mUccDataProvider == null) {
            Map<String, String> props = new HashMap<>();
            props.put("bindSite", targetSite);
            UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_UserTokenNIL", (UccParams) null, props);
            if (uccCallback != null) {
                uccCallback.onFail(targetSite, 1003, "data provider为空");
                return;
            }
            return;
        }
        final Activity activity2 = activity;
        final String str = targetSite;
        final Map<String, String> map = params;
        final UccCallback uccCallback2 = uccCallback;
        this.mUccDataProvider.getUserToken(targetSite, new MemberCallback<String>() {
            public void onSuccess(String userToken) {
                uccParams.userToken = userToken;
                UccServiceImpl.this.doBind(activity2, uccParams, str, map, uccCallback2);
            }

            public void onFailure(int code, String msg) {
                Map<String, String> props = new HashMap<>();
                props.put("bindSite", str);
                UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_UserTokenNIL", (UccParams) null, props);
                if (uccCallback2 != null) {
                    UccCallback uccCallback = uccCallback2;
                    String str = str;
                    if (TextUtils.isEmpty(msg)) {
                        msg = "userToken为空";
                    }
                    uccCallback.onFail(str, 1004, msg);
                }
            }
        });
    }

    public void bind(final String targetSite, final Map<String, String> params, final UccCallback uccCallback) {
        UccDataProvider uccDataProvider = null;
        if (params != null && !TextUtils.isEmpty(params.get("site"))) {
            uccDataProvider = this.mUccDataProviderContainer.get(params.get("site"));
        }
        if (uccDataProvider == null && this.mUccDataProvider == null) {
            Map<String, String> props = new HashMap<>();
            props.put("bindSite", targetSite);
            UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_UserTokenNIL", (UccParams) null, props);
            if (uccCallback != null) {
                uccCallback.onFail(targetSite, 1003, "data provider为空");
                return;
            }
            return;
        }
        if (uccDataProvider == null) {
            uccDataProvider = this.mUccDataProvider;
        }
        uccDataProvider.getUserToken(targetSite, new MemberCallback<String>() {
            public void onSuccess(String userToken) {
                UccServiceImpl.this.bind(userToken, targetSite, (Map<String, String>) params, uccCallback);
            }

            public void onFailure(int code, String msg) {
                Map<String, String> props = new HashMap<>();
                props.put("bindSite", targetSite);
                UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_UserTokenNIL", (UccParams) null, props);
                if (uccCallback != null) {
                    UccCallback uccCallback = uccCallback;
                    String str = targetSite;
                    if (TextUtils.isEmpty(msg)) {
                        msg = "userToken为空";
                    }
                    uccCallback.onFail(str, 1004, msg);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void doBind(Activity activity, UccParams uccParams, String targetSite, Map<String, String> params, UccCallback uccCallback) {
        UccBizContants.mBusyControlMap.put(targetSite, 0L);
        UccServiceProviderFactory.getInstance().getUccServiceProvider(targetSite).bind(activity, uccParams, OauthPlatformConfig.getOauthConfigByPlatform(targetSite), params, uccCallback);
    }

    public void setUccDataProvider(UccDataProvider uccDataProvider) {
        this.mUccDataProvider = uccDataProvider;
    }

    public void setUccDataProvider(String site, UccDataProvider uccDataProvider) {
        this.mUccDataProviderContainer.put(site, uccDataProvider);
    }

    public UccDataProvider getUccDataProvider() {
        return this.mUccDataProvider;
    }

    public void unbind(String targetSite, UccCallback uccCallback) {
        unbind(targetSite, (Map<String, String>) null, uccCallback);
    }

    public void unbind(final String targetSite, Map<String, String> params, final UccCallback uccCallback) {
        UccBizContants.mBusyControlMap.put(targetSite, 0L);
        final UccParams uccParams = new UccParams();
        uccParams.traceId = Utils.generateTraceId("unbind");
        uccParams.bindSite = targetSite;
        UTHitUtils.send(UTHitConstants.PageUccUnBind, "UccUnbind_Invoke", uccParams, (Map<String, String>) null);
        if (uccCallback != null) {
            if (TextUtils.isEmpty(targetSite)) {
                UTHitUtils.send(UTHitConstants.PageUccUnBind, "UccUnbind_InvalidParams", uccParams, (Map<String, String>) null);
                uccCallback.onFail(targetSite, 1009, "参数错误");
                return;
            }
            UccDataProvider uccDataProvider = null;
            if (params != null && !TextUtils.isEmpty(params.get("site"))) {
                uccDataProvider = this.mUccDataProviderContainer.get(params.get("site"));
            }
            if (uccDataProvider == null && this.mUccDataProvider == null) {
                Map<String, String> props = new HashMap<>();
                props.put("bindSite", targetSite);
                UTHitUtils.send(UTHitConstants.PageUccUnBind, "UccUnBind_UserTokenNIL", (UccParams) null, props);
                uccCallback.onFail(targetSite, 1003, "data provider为空");
                return;
            }
            if (uccDataProvider == null) {
                uccDataProvider = this.mUccDataProvider;
            }
            uccDataProvider.getUserToken(targetSite, new MemberCallback<String>() {
                public void onSuccess(String userToken) {
                    uccParams.userToken = userToken;
                    uccParams.site = AliMemberSDK.getMasterSite();
                    uccParams.bindSite = targetSite;
                    uccParams.userToken = userToken;
                    UccUnbindPresenter.getInstance().doUnbind(uccParams, targetSite, uccCallback);
                }

                public void onFailure(int code, String msg) {
                    Map<String, String> props = new HashMap<>();
                    props.put("bindSite", targetSite);
                    UTHitUtils.send(UTHitConstants.PageUccUnBind, "UccUnBind_UserTokenNIL", (UccParams) null, props);
                    UccCallback uccCallback = uccCallback;
                    String str = targetSite;
                    if (TextUtils.isEmpty(msg)) {
                        msg = "userToken为空";
                    }
                    uccCallback.onFail(str, 1004, msg);
                }
            });
        }
    }

    public void trustLogin(Activity activity, String targetSite, UccCallback uccCallback) {
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("needSession", "1");
        trustLogin(activity, targetSite, mapParams, uccCallback);
    }

    private long getLoginLimitInterval(String bindSite) {
        try {
            return Long.parseLong(OrangeConfig.getInstance().getConfig("login4android", bindSite + "LoginInterval", "-1"));
        } catch (Throwable th) {
            return -1;
        }
    }

    public void trustLogin(Activity activity, String targetSite, Map<String, String> params, UccCallback uccCallback) {
        final UccParams uccParams = new UccParams();
        uccParams.traceId = Utils.generateTraceId("login");
        uccParams.bindSite = targetSite;
        Map<String, String> props = new HashMap<>();
        if (params != null) {
            uccParams.miniAppId = params.get("miniAppId");
            props.put("scene", params.get("scene"));
            uccParams.sdkVersion = params.get("sdkVersion");
        }
        long threshold = getLoginLimitInterval(targetSite);
        if (threshold == -1) {
            threshold = 1000;
        }
        Long lastLoginTime = UccBizContants.mBusyControlMap.get(targetSite);
        if (lastLoginTime == null) {
            lastLoginTime = 0L;
        }
        if (System.currentTimeMillis() - lastLoginTime.longValue() < threshold) {
            UTHitUtils.send(UTHitConstants.PageUccLogin, "UccLogin_Busy", uccParams, props);
            if (uccCallback != null) {
                uccCallback.onFail(targetSite, UccResultCode.BIND_BUSY_CONTROL, "");
                return;
            }
            return;
        }
        UTHitUtils.send(UTHitConstants.PageUccLogin, "UccLogin_Invoke", uccParams, props);
        if (uccCallback != null) {
            boolean needUI = true;
            if (params != null && TextUtils.equals(params.get(ParamsConstants.Key.PARAM_NEED_UI), "0")) {
                needUI = false;
            }
            if ((activity != null || !needUI) && !TextUtils.isEmpty(targetSite)) {
                UccDataProvider uccDataProvider = null;
                if (params != null && !TextUtils.isEmpty(params.get("site"))) {
                    uccDataProvider = this.mUccDataProviderContainer.get(params.get("site"));
                }
                if (uccDataProvider == null && this.mUccDataProvider == null) {
                    Map<String, String> props1 = new HashMap<>();
                    props1.put("bindSite", targetSite);
                    UTHitUtils.send(UTHitConstants.PageUccLogin, "UccLogin_UserTokenNIL", (UccParams) null, props1);
                    uccCallback.onFail(targetSite, 1003, "data provider为空");
                    return;
                }
                if (uccDataProvider == null) {
                    uccDataProvider = this.mUccDataProvider;
                }
                final Map<String, String> map = params;
                final String str = targetSite;
                final Activity activity2 = activity;
                final UccCallback uccCallback2 = uccCallback;
                uccDataProvider.getUserToken(targetSite, new MemberCallback<String>() {
                    public void onSuccess(String userToken) {
                        uccParams.userToken = userToken;
                        if (map == null || TextUtils.isEmpty((CharSequence) map.get("site"))) {
                            uccParams.site = AliMemberSDK.getMasterSite();
                        } else {
                            uccParams.site = (String) map.get("site");
                        }
                        uccParams.bindSite = str;
                        uccParams.userToken = userToken;
                        if (map != null && !TextUtils.isEmpty((CharSequence) map.get("scene"))) {
                            uccParams.scene = (String) map.get("scene");
                        }
                        UccTrustLoginPresenter.getInstance().doTrustLogin(activity2, uccParams, str, map, uccCallback2);
                    }

                    public void onFailure(int code, String msg) {
                        Map<String, String> props = new HashMap<>();
                        props.put("bindSite", str);
                        UTHitUtils.send(UTHitConstants.PageUccLogin, "UccLogin_UserTokenNIL", (UccParams) null, props);
                        uccCallback2.onFail(str, 1004, msg);
                    }
                });
                return;
            }
            UTHitUtils.send(UTHitConstants.PageUccLogin, "UccLogin_InvalidParams", uccParams, (Map<String, String>) null);
            uccCallback.onFail(targetSite, 1002, "参数错误");
        }
    }

    public void trustLogin(String targetSite, Map<String, String> params, UccCallback uccCallback) {
        SDKLogger.d(TAG, "trustLogin goUccActivity");
        UccActivity.mUccCallback = uccCallback;
        if (params == null || !TextUtils.equals(params.get(ParamsConstants.Key.PARAM_NEED_UI), "0")) {
            Intent intent = new Intent();
            intent.putExtra(UccConstants.PARAM_FUNC_TYPE, 1);
            intent.putExtra(UccConstants.PARAM_TARGET_SITE, targetSite);
            if (params != null) {
                intent.putExtra("needSession", "1");
                intent.putExtra(ParamsConstants.Key.PARAM_NEED_LOCAL_COOKIE_ONLY, params.get(ParamsConstants.Key.PARAM_NEED_LOCAL_COOKIE_ONLY));
                intent.putExtra(ParamsConstants.Key.PARAM_NEED_AUTOLOGIN, params.get(ParamsConstants.Key.PARAM_NEED_AUTOLOGIN));
                intent.putExtra("scene", params.get("scene"));
                intent.putExtra("site", params.get("site"));
            }
            intent.setClass(KernelContext.getApplicationContext(), UccActivity.class);
            intent.setFlags(268435456);
            KernelContext.getApplicationContext().startActivity(intent);
            return;
        }
        trustLogin((Activity) null, targetSite, params, uccCallback);
    }

    public void uccOAuthLogin(Activity activity, String targetSite, Map<String, String> map, UccCallback uccCallback) {
        final UccParams uccParams = new UccParams();
        uccParams.traceId = Utils.generateTraceId("login");
        uccParams.bindSite = targetSite;
        UTHitUtils.send(UTHitConstants.PageUccOAuthLogin, "UccOAuthLogin_Invoke", uccParams, (Map<String, String>) null);
        if (uccCallback != null) {
            if (activity == null || TextUtils.isEmpty(targetSite)) {
                UTHitUtils.send(UTHitConstants.PageUccOAuthLogin, "UccOAuthLogin_InvalidParams", uccParams, (Map<String, String>) null);
                uccCallback.onFail(targetSite, 1002, "参数错误");
                return;
            }
            final String str = targetSite;
            final Activity activity2 = activity;
            final UccCallback uccCallback2 = uccCallback;
            ((OauthService) AliMemberSDK.getService(OauthService.class)).oauth(activity, targetSite, new OauthCallback() {
                public void onSuccess(String oauthSite, Map params) {
                    uccParams.site = str;
                    if (params == null || TextUtils.isEmpty(params.get("site") + "")) {
                        uccParams.site = AliMemberSDK.getMasterSite();
                    } else {
                        uccParams.site = (String) params.get("site");
                    }
                    uccParams.bindUserToken = (String) params.get("authCode");
                    uccParams.bindUserTokenType = "oauthcode";
                    UccOauthLoginPresenter.getInstance().doUccOAuthLogin(activity2, uccParams, params, uccCallback2);
                }

                public void onFail(String oauthSite, int code, String msg) {
                    if (uccCallback2 != null) {
                        uccCallback2.onFail(str, code, msg);
                    }
                }
            });
        }
    }

    public void logout(Context context, String targetSite) {
        UccParams uccParams = new UccParams();
        uccParams.traceId = Utils.generateTraceId(BaseConfig.INTENT_KEY_LOGOUT);
        uccParams.bindSite = targetSite;
        if (TextUtils.isEmpty(targetSite)) {
            UTHitUtils.send(UTHitConstants.PageUccLogout, "UccLogout_InvalidParams", uccParams, (Map<String, String>) null);
            return;
        }
        UTHitUtils.send(UTHitConstants.PageUccLogout, "UccLogout_Invoke", uccParams, (Map<String, String>) null);
        ((OauthService) AliMemberSDK.getService(OauthService.class)).logout(context, targetSite);
    }

    public void logoutAll(Context context) {
        ((OauthService) AliMemberSDK.getService(OauthService.class)).logoutAll(context);
    }

    public void cleanUp() {
        UccServiceProviderFactory.getInstance().cleanUp();
    }

    public void bindWithIBB(Activity activity, String targetSite, String ibb, Map<String, String> params, UccCallback uccCallback) {
        UccParams uccParams = new UccParams();
        uccParams.traceId = Utils.generateTraceId("bindWithIbb");
        uccParams.bindSite = targetSite;
        uccParams.site = AliMemberSDK.getMasterSite();
        Map<String, String> props1 = new HashMap<>();
        if (params != null) {
            uccParams.miniAppId = params.get("miniAppId");
            props1.put("scene", params.get("scene"));
            props1.put("needSession", TextUtils.equals(params.get("needSession"), "1") ? "T" : "F");
        }
        UTHitUtils.send(UTHitConstants.PageUccBind, "UccBindWithIbb_Invoke", uccParams, props1);
        if (params == null) {
            params = new HashMap<>();
        }
        params.put(ParamsConstants.Key.PARAM_NEED_LOCAL_SESSION, "0");
        params.put("scene", ParamsConstants.UrlConstant.NEW_YOUKU_UPGRADE);
        params.put("needSession", "1");
        params.put(ParamsConstants.Key.PARAM_ONLY_AUTHCODE, "1");
        params.put(ParamsConstants.Key.PARAM_IS_BIND, "1");
        UccServiceProviderFactory.getInstance().getUccServiceProvider(targetSite).bindWithIBB(activity, uccParams, ibb, params, uccCallback);
    }

    public void setBindComponentProxy(BindComponentProxy bindComponentProxy) {
        UccContext.setBindComponentProxy(bindComponentProxy);
    }
}
