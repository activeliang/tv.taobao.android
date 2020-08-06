package com.ali.auth.third.offline.login;

import android.app.Activity;
import com.ali.auth.third.core.WebViewProxy;
import com.ali.auth.third.core.callback.CommonCallback;
import com.ali.auth.third.core.callback.LoginCallback;
import com.ali.auth.third.core.model.Session;
import com.ali.auth.third.offline.login.callback.LogoutCallback;
import com.ali.auth.third.offline.model.ContractModel;
import java.util.Map;

public interface LoginService {
    public static final String TAG = "login";

    void auth(Activity activity, LoginCallback loginCallback);

    void auth(LoginCallback loginCallback);

    void autoLogin(LoginCallback loginCallback);

    boolean checkSessionValid();

    Session getSession();

    boolean isLoginUrl(String str);

    boolean isLogoutUrl(String str);

    void loginBySsoToken(String str, LoginCallback loginCallback);

    void loginWithAuthCode(Map<String, String> map, LoginCallback loginCallback);

    void logout(Activity activity, LogoutCallback logoutCallback);

    void logout(LogoutCallback logoutCallback);

    void refreshCookie(CommonCallback commonCallback);

    void releaseContract(ContractModel contractModel, CommonCallback commonCallback);

    void setLoginCallback(LoginCallback loginCallback);

    void setWebViewProxy(WebViewProxy webViewProxy);

    void showQrCodeLogin(Map<String, String> map, LoginCallback loginCallback);
}
