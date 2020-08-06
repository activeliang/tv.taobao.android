package com.uc.webview.export;

import com.ali.auth.third.core.model.KernelMessageConstants;
import com.uc.webview.export.annotations.Api;
import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.interfaces.ICookieManager;
import com.uc.webview.export.internal.setup.UCAsyncTask;
import java.util.HashMap;

@Api
/* compiled from: ProGuard */
public class CookieManager {
    private static HashMap<Integer, CookieManager> a;
    private ICookieManager b;

    private CookieManager(ICookieManager iCookieManager) {
        this.b = iCookieManager;
    }

    /* access modifiers changed from: protected */
    public Object clone() {
        throw new CloneNotSupportedException("doesn't implement Cloneable");
    }

    public static CookieManager getInstance() {
        return a(((Integer) d.a((int) KernelMessageConstants.PARAM_ERROR, new Object[0])).intValue());
    }

    public static CookieManager getInstance(WebView webView) {
        return a(webView.getCurrentViewCoreType());
    }

    private static synchronized CookieManager a(int i) {
        CookieManager cookieManager;
        synchronized (CookieManager.class) {
            if (a == null) {
                a = new HashMap<>();
            }
            cookieManager = a.get(Integer.valueOf(i));
            if (cookieManager == null) {
                CookieManager cookieManager2 = new CookieManager((ICookieManager) d.a((int) UCAsyncTask.getPercent, Integer.valueOf(i)));
                a.put(Integer.valueOf(i), cookieManager2);
                cookieManager = cookieManager2;
            }
        }
        return cookieManager;
    }

    public void setAcceptCookie(boolean z) {
        this.b.setAcceptCookie(z);
    }

    public boolean acceptCookie() {
        return this.b.acceptCookie();
    }

    public void setCookie(String str, String str2) {
        this.b.setCookie(str, str2);
    }

    public String getCookie(String str) {
        return this.b.getCookie(str);
    }

    public void removeSessionCookie() {
        this.b.removeSessionCookie();
    }

    public void removeAllCookie() {
        this.b.removeAllCookie();
    }

    public boolean hasCookies() {
        return this.b.hasCookies();
    }

    public String toString() {
        return "CookieManager@" + hashCode() + "[" + this.b + "]";
    }
}
