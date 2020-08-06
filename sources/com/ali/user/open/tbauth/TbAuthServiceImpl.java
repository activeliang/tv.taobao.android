package com.ali.user.open.tbauth;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import com.ali.user.open.callback.LoginCallback;
import com.ali.user.open.cookies.CookieManagerWrapper;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.WebViewProxy;
import com.ali.user.open.core.callback.MemberCallback;
import com.ali.user.open.core.config.AuthOption;
import com.ali.user.open.core.config.ConfigManager;
import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.core.device.DeviceInfo;
import com.ali.user.open.core.service.StorageService;
import com.ali.user.open.core.service.UserTrackerService;
import com.ali.user.open.core.trace.SDKLogger;
import com.ali.user.open.core.util.CommonUtils;
import com.ali.user.open.core.util.JSONUtils;
import com.ali.user.open.core.util.ParamsConstants;
import com.ali.user.open.core.util.ResourceUtils;
import com.ali.user.open.service.SessionService;
import com.ali.user.open.service.impl.SessionManager;
import com.ali.user.open.session.Session;
import com.ali.user.open.tbauth.callback.LogoutCallback;
import com.ali.user.open.tbauth.context.TbAuthContext;
import com.ali.user.open.tbauth.task.LogoutTask;
import com.ali.user.open.tbauth.task.RpcPresenter;
import com.ali.user.open.tbauth.ui.TbAuthActivity;
import com.ali.user.open.tbauth.ui.context.CallbackContext;
import com.ut.mini.UTHitBuilders;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class TbAuthServiceImpl implements TbAuthService {
    private static final String TAG = "TbAuthService";
    private transient Pattern[] mLoginPatterns;
    private transient Pattern[] mLogoutPatterns;

    public boolean isLoginUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        if (this.mLoginPatterns == null && !TextUtils.isEmpty(ConfigManager.LOGIN_URLS)) {
            String[] loginUrls = ConfigManager.LOGIN_URLS.split("[,]");
            this.mLoginPatterns = new Pattern[loginUrls.length];
            int length = this.mLoginPatterns.length;
            for (int i = 0; i < length; i++) {
                this.mLoginPatterns[i] = Pattern.compile(loginUrls[i]);
            }
        }
        for (Pattern pattern : this.mLoginPatterns) {
            if (pattern.matcher(url).matches()) {
                return true;
            }
        }
        return false;
    }

    public boolean isLogoutUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        if (this.mLogoutPatterns == null && !TextUtils.isEmpty(ConfigManager.LOGOUT_URLS)) {
            String[] logoutUrls = ConfigManager.LOGOUT_URLS.split("[,]");
            this.mLogoutPatterns = new Pattern[logoutUrls.length];
            int length = this.mLogoutPatterns.length;
            for (int i = 0; i < length; i++) {
                this.mLogoutPatterns[i] = Pattern.compile(logoutUrls[i]);
            }
        }
        for (Pattern pattern : this.mLogoutPatterns) {
            if (pattern.matcher(url).matches()) {
                return true;
            }
        }
        return false;
    }

    public void auth(LoginCallback loginCallback) {
        Map<String, String> params = new HashMap<>();
        params.put(ParamsConstants.Key.PARAM_NEED_AUTOLOGIN, "0");
        params.put("needSession", "0");
        auth(params, loginCallback);
    }

    public void auth(Map<String, String> params, final LoginCallback loginCallback) {
        String traceId;
        SDKLogger.d(TAG, "auth start");
        Map<String, String> props = new HashMap<>();
        if (params == null || TextUtils.isEmpty(params.get(ParamsConstants.Key.PARAM_TRACE_ID))) {
            traceId = "oauth" + DeviceInfo.deviceId + (System.currentTimeMillis() / 1000);
        } else {
            traceId = params.get(ParamsConstants.Key.PARAM_TRACE_ID);
        }
        props.put(UTHitBuilders.UTHitBuilder.FIELD_ARG2, traceId);
        TbAuthContext.traceId = traceId;
        ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).send("Page_TaobaoOauth", "Page_TaobaoOauth_Invoke", props);
        if (!isAuthEnvironmentValid(loginCallback)) {
            SDKLogger.d(TAG, "AuthEnvironment invalid");
            return;
        }
        String needSession = "0";
        String needAutoLogin = "0";
        String isOnlyAuthCode = "0";
        String isBind = "0";
        TbAuthContext.sSceneCode = "";
        TbAuthContext.sIBB = "";
        if (params != null) {
            needSession = params.get("needSession");
            needAutoLogin = params.get(ParamsConstants.Key.PARAM_NEED_AUTOLOGIN);
            isOnlyAuthCode = params.get(ParamsConstants.Key.PARAM_ONLY_AUTHCODE);
            TbAuthContext.sSceneCode = params.get(ParamsConstants.Key.PARAM_SCENE_CODE);
            TbAuthContext.sIBB = params.get("ibb");
            isBind = params.get(ParamsConstants.Key.PARAM_IS_BIND);
            if ("1".equals(params.get(ParamsConstants.Key.PARAM_H5ONLY))) {
                TbAuthContext.h5Only = true;
            } else {
                TbAuthContext.h5Only = false;
            }
        }
        TbAuthContext.needSession = TextUtils.equals(needSession, "1");
        TbAuthContext.onlyAuthCode = TextUtils.equals(isOnlyAuthCode, "1");
        TbAuthContext.isBind = TextUtils.equals(isBind, "1");
        if (TextUtils.equals(needAutoLogin, "1")) {
            autoLogin(new LoginCallback() {
                public void onSuccess(Session session) {
                    if (loginCallback != null) {
                        loginCallback.onSuccess(TbAuthServiceImpl.this.getSession());
                    }
                }

                public void onFailure(int code, String msg) {
                    TbAuthServiceImpl.this.goLogin(loginCallback);
                }
            });
        } else {
            goLogin(loginCallback);
        }
    }

    public void logout(LogoutCallback logoutCallback) {
        try {
            Map<String, String> json = new HashMap<>();
            json.put("withNoActivity", "true");
            ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).send(BaseConfig.INTENT_KEY_LOGOUT.toUpperCase(), json);
        } catch (Exception e) {
        }
        ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).send(BaseConfig.INTENT_KEY_LOGOUT.toUpperCase(), (Map<String, String>) null);
        new LogoutTask(logoutCallback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public boolean isAppAuthSurpport(Context context) {
        if (KernelContext.sOneTimeAuthOption == AuthOption.H5ONLY) {
            return false;
        }
        if (KernelContext.authOption == AuthOption.H5ONLY) {
            return false;
        }
        Intent intent = new Intent();
        intent.setAction("com.taobao.open.intent.action.GETWAY");
        intent.setData(Uri.parse("tbopen://m.taobao.com/getway/oauth?" + "&appkey=" + ((StorageService) AliMemberSDK.getService(StorageService.class)).getAppKey() + "&pluginName=taobao.oauth.code.create" + "&apkSign=" + "" + "&sign=" + ""));
        if (context == null) {
            context = KernelContext.getApplicationContext();
        }
        return context.getPackageManager().queryIntentActivities(intent, 0).size() > 0;
    }

    /* access modifiers changed from: private */
    public void goLogin(LoginCallback loginCallback) {
        SDKLogger.d(TAG, "auth goLogin");
        CallbackContext.loginCallback = loginCallback;
        Intent intent = new Intent();
        intent.setClass(KernelContext.getApplicationContext(), TbAuthActivity.class);
        intent.setFlags(268435456);
        KernelContext.getApplicationContext().startActivity(intent);
    }

    public Session getSession() {
        return ((SessionService) AliMemberSDK.getService(SessionService.class)).getSession();
    }

    public boolean checkSessionValid() {
        return ((SessionService) AliMemberSDK.getService(SessionService.class)).isSessionValid();
    }

    public void tokenLogin(int site, String scene, String loginToken, String h5QueryString, LoginCallback loginCallback) {
        TbAuthContext.needSession = true;
        CallbackContext.loginCallback = loginCallback;
        Intent intent = new Intent();
        intent.setClass(KernelContext.getApplicationContext(), TbAuthActivity.class);
        intent.putExtra("login_type", 1);
        intent.putExtra("site", site);
        intent.putExtra("scene", scene);
        intent.putExtra(TbAuthConstants.LOGIN_TOKEN, loginToken);
        intent.putExtra(TbAuthConstants.H5_QUERY_STR, h5QueryString);
        intent.setFlags(268435456);
        KernelContext.getApplicationContext().startActivity(intent);
    }

    public void refreshCookie(MemberCallback callback) {
        new RefreshTask(callback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[0]);
    }

    public void setLoginCallback(LoginCallback loginCallback) {
        CallbackContext.mGlobalLoginCallback = loginCallback;
    }

    public void setWebViewProxy(WebViewProxy webViewProxy) {
        KernelContext.mWebViewProxy = webViewProxy;
    }

    class RefreshTask extends AsyncTask<Object, Void, Void> {
        MemberCallback callback;

        RefreshTask(MemberCallback c) {
            this.callback = c;
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(Object... params) {
            CookieManagerWrapper.INSTANCE.refreshCookie(".taobao.com");
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void aVoid) {
            this.callback.onSuccess(aVoid);
        }
    }

    private void goQrCodeLogin(Map<String, String> params, LoginCallback loginCallback) {
        SDKLogger.d(TAG, "goQrCodeLogin start");
        CallbackContext.loginCallback = loginCallback;
        Intent intent = new Intent();
        intent.setClass(KernelContext.getApplicationContext(), TbAuthActivity.class);
        intent.setFlags(268435456);
        intent.putExtra("login_type", 4);
        intent.putExtra("params", params == null ? "" : JSONUtils.toJsonObject(params).toString());
        KernelContext.getApplicationContext().startActivity(intent);
    }

    public void autoLogin(final LoginCallback loginCallback) {
        if (!TextUtils.isEmpty(SessionManager.INSTANCE.getInternalSession().autoLoginToken) && SessionManager.INSTANCE.getInternalSession() != null && !TextUtils.isEmpty(SessionManager.INSTANCE.getInternalSession().userId)) {
            SDKLogger.d(TAG, "auth auto login");
            RpcPresenter.loginByRefreshToken(new LoginCallback() {
                public void onSuccess(Session session) {
                    SDKLogger.d(TbAuthServiceImpl.TAG, "auth auto login success");
                    if (loginCallback != null) {
                        loginCallback.onSuccess(TbAuthServiceImpl.this.getSession());
                    }
                    if (CallbackContext.mGlobalLoginCallback != null) {
                        CallbackContext.mGlobalLoginCallback.onSuccess(TbAuthServiceImpl.this.getSession());
                    }
                }

                public void onFailure(int code, String msg) {
                    SDKLogger.d(TbAuthServiceImpl.TAG, "auth auto login success");
                    if (loginCallback != null) {
                        loginCallback.onFailure(code, msg);
                    }
                }
            });
        } else if (loginCallback != null) {
            loginCallback.onFailure(-1, "auto login token is empty");
        }
    }

    private boolean isAuthEnvironmentValid(LoginCallback loginCallback) {
        if (!KernelContext.checkServiceValid()) {
            SDKLogger.d(TAG, "auth static field is null");
            if (loginCallback == null) {
                return false;
            }
            loginCallback.onFailure(10098, "服务不存在");
            return false;
        } else if (CommonUtils.isNetworkAvailable()) {
            return true;
        } else {
            SDKLogger.d(TAG, "auth network not available");
            loginCallback.onFailure(10099, ResourceUtils.getString("member_sdk_network_not_available_message"));
            return false;
        }
    }
}
