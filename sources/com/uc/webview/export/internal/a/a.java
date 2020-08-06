package com.uc.webview.export.internal.a;

import android.webkit.CookieManager;
import com.uc.webview.export.internal.interfaces.ICookieManager;

/* compiled from: ProGuard */
public class a implements ICookieManager {
    private CookieManager a = CookieManager.getInstance();

    public synchronized void setAcceptCookie(boolean z) {
        this.a.setAcceptCookie(z);
    }

    public synchronized boolean acceptCookie() {
        return this.a.acceptCookie();
    }

    public void setCookie(String str, String str2) {
        this.a.setCookie(str, str2);
    }

    public synchronized String getCookie(String str) {
        return this.a.getCookie(str);
    }

    public void removeSessionCookie() {
        this.a.removeSessionCookie();
    }

    public void removeAllCookie() {
        this.a.removeAllCookie();
    }

    public synchronized boolean hasCookies() {
        return this.a.hasCookies();
    }
}
