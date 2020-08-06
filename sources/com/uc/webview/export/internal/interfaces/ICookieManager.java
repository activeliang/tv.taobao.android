package com.uc.webview.export.internal.interfaces;

import com.uc.webview.export.annotations.Interface;

@Interface
/* compiled from: ProGuard */
public interface ICookieManager {
    boolean acceptCookie();

    String getCookie(String str);

    boolean hasCookies();

    void removeAllCookie();

    void removeSessionCookie();

    void setAcceptCookie(boolean z);

    void setCookie(String str, String str2);
}
