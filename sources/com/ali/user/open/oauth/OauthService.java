package com.ali.user.open.oauth;

import android.app.Activity;
import android.content.Context;
import java.util.Map;

public interface OauthService {
    void cleanUp();

    boolean isAppAuthSurpport(Context context, String str);

    boolean isLoginUrl(String str, String str2);

    void logout(Context context, String str);

    void logoutAll(Context context);

    void oauth(Activity activity, String str, OauthCallback oauthCallback);

    void oauth(Activity activity, String str, Map<String, String> map, OauthCallback oauthCallback);

    void refreshWhenLogin(String str, String str2, boolean z);

    void tokenLogin(String str, String str2, String str3, String str4, Map<String, String> map, OauthCallback oauthCallback);
}
