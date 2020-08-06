package com.ali.auth.third.offline.login;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import com.ali.auth.third.core.WebViewProxy;
import com.ali.auth.third.core.broadcast.LoginAction;
import com.ali.auth.third.core.callback.CommonCallback;
import com.ali.auth.third.core.callback.LoginCallback;
import com.ali.auth.third.core.config.ConfigManager;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.cookies.CookieManagerWrapper;
import com.ali.auth.third.core.model.Session;
import com.ali.auth.third.core.model.SystemMessageConstants;
import com.ali.auth.third.core.service.UserTrackerService;
import com.ali.auth.third.core.service.impl.CredentialManager;
import com.ali.auth.third.core.trace.SDKLogger;
import com.ali.auth.third.core.util.CommonUtils;
import com.ali.auth.third.core.util.JSONUtils;
import com.ali.auth.third.core.util.ResourceUtils;
import com.ali.auth.third.offline.LoginActivity;
import com.ali.auth.third.offline.context.CallbackContext;
import com.ali.auth.third.offline.login.callback.LogoutCallback;
import com.ali.auth.third.offline.login.context.LoginContext;
import com.ali.auth.third.offline.login.task.LoginByAlipayTokenTask;
import com.ali.auth.third.offline.login.task.LoginByReTokenTask;
import com.ali.auth.third.offline.login.task.LoginBySSOTokenTask;
import com.ali.auth.third.offline.login.task.LogoutTask;
import com.ali.auth.third.offline.login.task.ReleaseContractTask;
import com.ali.auth.third.offline.login.util.LoginStatus;
import com.ali.auth.third.offline.model.ContractModel;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class LoginServiceImpl implements LoginService {
    private static final String LOG_TAG = "LoginServiceImpl";
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

    public void logout(Activity activity, LogoutCallback logoutCallback) {
        ((UserTrackerService) KernelContext.getService(UserTrackerService.class)).send(BaseConfig.INTENT_KEY_LOGOUT.toUpperCase(), (Map<String, String>) null);
        new LogoutTask(activity, logoutCallback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public void auth(Activity activity, LoginCallback loginCallback) {
        SDKLogger.d("login", "auth start");
        auth(loginCallback);
    }

    public void auth(final LoginCallback loginCallback) {
        if (isAuthEnvironmentValid(loginCallback)) {
            autoLogin(new LoginCallback() {
                public void onSuccess(Session session) {
                    if (loginCallback != null) {
                        loginCallback.onSuccess(LoginServiceImpl.this.getSession());
                    }
                }

                public void onFailure(int code, String msg) {
                    LoginServiceImpl.this.ssoTokenLogin(new LoginCallback() {
                        public void onSuccess(Session session) {
                            if (loginCallback != null) {
                                loginCallback.onSuccess(LoginServiceImpl.this.getSession());
                            }
                            ConfigManager.getInstance().setSsoToken((String) null);
                        }

                        public void onFailure(int code, String msg) {
                            LoginServiceImpl.this.goLogin(loginCallback);
                            ConfigManager.getInstance().setSsoToken((String) null);
                        }
                    });
                }
            });
        }
    }

    public void logout(LogoutCallback logoutCallback) {
        try {
            Map<String, String> json = new HashMap<>();
            json.put("withNoActivity", "true");
            ((UserTrackerService) KernelContext.getService(UserTrackerService.class)).send(BaseConfig.INTENT_KEY_LOGOUT.toUpperCase(), json);
        } catch (Exception e) {
        }
        logout((Activity) null, logoutCallback);
    }

    public void goLogin(LoginCallback loginCallback) {
        SDKLogger.d("login", "auth goLogin");
        CallbackContext.loginCallback = loginCallback;
        Intent intent = new Intent();
        intent.setClass(KernelContext.getApplicationContext(), LoginActivity.class);
        intent.setFlags(268435456);
        KernelContext.getApplicationContext().startActivity(intent);
    }

    public Session getSession() {
        return LoginContext.credentialService.getSession();
    }

    public boolean checkSessionValid() {
        if (isAuthEnvironmentValid((LoginCallback) null)) {
            Boolean result = null;
            try {
                result = Boolean.valueOf(LoginContext.credentialService.isSessionValid());
            } catch (Throwable t) {
                t.printStackTrace();
            }
            if (result != null) {
                return result.booleanValue();
            }
        }
        return false;
    }

    public void refreshCookie(CommonCallback callback) {
        new RefreshTask(callback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[0]);
    }

    public void setLoginCallback(LoginCallback loginCallback) {
        CallbackContext.mGlobalLoginCallback = loginCallback;
    }

    public void setWebViewProxy(WebViewProxy webViewProxy) {
        KernelContext.mWebViewProxy = webViewProxy;
    }

    class RefreshTask extends AsyncTask<Object, Void, Void> {
        CommonCallback callback;

        RefreshTask(CommonCallback c) {
            this.callback = c;
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(Object... params) {
            CookieManagerWrapper.INSTANCE.refreshCookie();
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void aVoid) {
            this.callback.onSuccess();
        }
    }

    public void showQrCodeLogin(final Map<String, String> params, final LoginCallback loginCallback) {
        if (isAuthEnvironmentValid(loginCallback)) {
            autoLogin(new LoginCallback() {
                public void onSuccess(Session session) {
                    if (loginCallback != null) {
                        loginCallback.onSuccess(LoginServiceImpl.this.getSession());
                    }
                }

                public void onFailure(int code, String msg) {
                    LoginServiceImpl.this.goQrCodeLogin(params, loginCallback);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void goQrCodeLogin(Map<String, String> params, LoginCallback loginCallback) {
        SDKLogger.d("login", "goQrCodeLogin start");
        CallbackContext.loginCallback = loginCallback;
        Intent intent = new Intent();
        intent.setClass(KernelContext.getApplicationContext(), LoginActivity.class);
        intent.setFlags(268435456);
        intent.putExtra("login_type", 1);
        intent.putExtra("params", params == null ? "" : JSONUtils.toJsonObject(params).toString());
        KernelContext.getApplicationContext().startActivity(intent);
    }

    public void autoLogin(final LoginCallback loginCallback) {
        if (isAuthEnvironmentValid(loginCallback)) {
            if (!TextUtils.isEmpty(CredentialManager.INSTANCE.getInternalSession().autoLoginToken) && CredentialManager.INSTANCE.getInternalSession().user != null && !TextUtils.isEmpty(CredentialManager.INSTANCE.getInternalSession().user.userId)) {
                SDKLogger.d("login", "auth auto login");
                new LoginByReTokenTask((Activity) null, new LoginCallback() {
                    public void onSuccess(Session session) {
                        SDKLogger.d("login", "auth auto login success");
                        if (loginCallback != null) {
                            loginCallback.onSuccess(LoginServiceImpl.this.getSession());
                        }
                        if (CallbackContext.mGlobalLoginCallback != null) {
                            CallbackContext.mGlobalLoginCallback.onSuccess(LoginServiceImpl.this.getSession());
                        }
                        CommonUtils.sendBroadcast(LoginAction.NOTIFY_LOGIN_SUCCESS);
                    }

                    public void onFailure(int code, String msg) {
                        SDKLogger.d("login", "auth auto login success");
                        if (loginCallback != null) {
                            loginCallback.onFailure(code, msg);
                        }
                    }
                }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
            } else if (loginCallback != null) {
                loginCallback.onFailure(-1, "param invalid");
            }
        }
    }

    /* access modifiers changed from: private */
    public void ssoTokenLogin(final LoginCallback loginCallback) {
        if (!TextUtils.isEmpty(ConfigManager.getInstance().getSsoToken())) {
            new LoginBySSOTokenTask((Activity) null, new LoginCallback() {
                public void onSuccess(Session session) {
                    SDKLogger.d("login", "auth sso login success");
                    if (loginCallback != null) {
                        loginCallback.onSuccess(LoginServiceImpl.this.getSession());
                    }
                    if (CallbackContext.mGlobalLoginCallback != null) {
                        CallbackContext.mGlobalLoginCallback.onSuccess(LoginServiceImpl.this.getSession());
                    }
                    CommonUtils.sendBroadcast(LoginAction.NOTIFY_LOGIN_SUCCESS);
                }

                public void onFailure(int code, String msg) {
                    SDKLogger.d("login", "auth sso login success");
                    if (loginCallback != null) {
                        loginCallback.onFailure(code, msg);
                    }
                }
            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
        } else if (loginCallback != null) {
            loginCallback.onFailure(-1, "param invalid");
        }
    }

    private boolean isAuthEnvironmentValid(LoginCallback loginCallback) {
        if (!KernelContext.checkServiceValid()) {
            SDKLogger.d("login", "auth static field is null");
            if (loginCallback != null) {
                loginCallback.onFailure(10098, "NullPointException");
            }
            return false;
        }
        try {
            Map<String, String> json = new HashMap<>();
            json.put("withNoActivity", "true");
            ((UserTrackerService) KernelContext.getService(UserTrackerService.class)).send("auth".toUpperCase(), json);
        } catch (Exception e) {
        }
        if (CommonUtils.isNetworkAvailable() || KernelContext.supportOfflineLogin) {
            return true;
        }
        SDKLogger.d("login", "auth network not available");
        if (loginCallback != null) {
            loginCallback.onFailure(10099, ResourceUtils.getString("com_taobao_tae_sdk_network_not_available_message"));
        }
        LoginStatus.resetLoginFlag();
        return false;
    }

    public void loginWithAuthCode(Map<String, String> params, LoginCallback loginCallback) {
        if ((params == null || TextUtils.isEmpty(params.get("token"))) && loginCallback != null) {
            loginCallback.onFailure(SystemMessageConstants.INVALID_PARAM, ResourceUtils.getString("com_taobao_login_param_invlaid"));
            LoginStatus.resetLoginFlag();
            return;
        }
        CallbackContext.loginCallback = loginCallback;
        new LoginByAlipayTokenTask().execute(new String[]{params.get("token")});
    }

    public void releaseContract(ContractModel model, CommonCallback callback) {
        if (model != null) {
            new ReleaseContractTask(callback).execute(new ContractModel[]{model});
        } else if (callback != null) {
            callback.onFailure(SystemMessageConstants.INVALID_PARAM, ResourceUtils.getString("com_taobao_login_param_invlaid"));
        }
    }

    public void loginBySsoToken(String ssoToken, LoginCallback loginCallback) {
        if (!TextUtils.isEmpty(ssoToken)) {
            goSsoTokenLogin(ssoToken, loginCallback);
        } else if (loginCallback != null) {
            loginCallback.onFailure(-1, "sso token is empty");
        }
    }

    private void goSsoTokenLogin(String ssoToken, LoginCallback loginCallback) {
        SDKLogger.d("login", "goSsoTokenLogin start");
        CallbackContext.loginCallback = loginCallback;
        Intent intent = new Intent();
        intent.setClass(KernelContext.getApplicationContext(), LoginActivity.class);
        intent.setFlags(268435456);
        intent.putExtra("login_type", 5);
        intent.putExtra("token", ssoToken);
        KernelContext.getApplicationContext().startActivity(intent);
    }
}
