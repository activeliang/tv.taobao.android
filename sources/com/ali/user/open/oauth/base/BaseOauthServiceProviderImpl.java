package com.ali.user.open.oauth.base;

import android.content.Context;
import android.content.Intent;
import com.ali.user.open.oauth.OauthCallback;
import com.ali.user.open.oauth.OauthServiceProvider;
import java.util.Map;

public abstract class BaseOauthServiceProviderImpl implements OauthServiceProvider {
    public static final String TAG = "oa.DamaiOauthServiceProviderImpl";

    public boolean isAppAuthSurpport(Context context) {
        return false;
    }

    public void refreshWhenLogin(String loginReturnDataStr, boolean cookieOnly) {
    }

    public void tokenLogin(String scene, String loginToken, String h5QueryString, Map<String, String> map, OauthCallback oauthCallback) {
    }

    public void cleanUp() {
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }
}
