package com.ali.user.sso;

public interface SsoStatesChangedListener {
    void onSsoLogin(String str, String str2);

    void onSsoLogout(String str, String str2);
}
