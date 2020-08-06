package com.ali.user.sso;

public interface SsoResultListener {
    public static final String NO_ACCOUNT_MATCHED = "sso:no_acount_matched";

    void onFailed(String str);

    void onSuccess(UserInfo userInfo);
}
