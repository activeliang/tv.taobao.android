package com.ali.user.open.core.webview;

import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.ali.user.open.core.WebViewProxy;
import com.ali.user.open.core.context.KernelContext;

public class DefaultWebViewProxy implements WebViewProxy {
    private static volatile DefaultWebViewProxy instance = null;

    private DefaultWebViewProxy() {
    }

    public static DefaultWebViewProxy getInstance() {
        if (instance == null) {
            synchronized (DefaultWebViewProxy.class) {
                if (instance == null) {
                    instance = new DefaultWebViewProxy();
                }
            }
        }
        return instance;
    }

    public void setAcceptCookie(boolean accept) {
        CookieManager.getInstance().setAcceptCookie(accept);
    }

    public void setCookie(String url, String value) {
        CookieManager.getInstance().setCookie(url, value);
    }

    public String getCookie(String url) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        return cookieManager.getCookie(".taobao.com");
    }

    public void flush() {
        try {
            if (Build.VERSION.SDK_INT >= 21) {
                CookieManager.getInstance().flush();
            } else {
                CookieSyncManager.createInstance(KernelContext.getApplicationContext()).sync();
            }
        } catch (Throwable th) {
        }
    }

    public void removeSessionCookie() {
        CookieManager.getInstance().removeSessionCookie();
    }

    public void removeAllCookie() {
        CookieManager.getInstance().removeAllCookie();
    }

    public void removeExpiredCookie() {
        CookieManager.getInstance().removeExpiredCookie();
    }
}
