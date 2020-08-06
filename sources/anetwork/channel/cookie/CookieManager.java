package anetwork.channel.cookie;

import android.content.Context;
import android.os.Build;
import android.webkit.CookieSyncManager;
import anet.channel.util.ALog;
import anet.channel.util.HttpConstant;
import anetwork.channel.http.NetworkSdkSetting;
import java.util.List;
import java.util.Map;

public class CookieManager {
    public static final String TAG = "anet.CookieManager";
    private static boolean isCookieValid = true;
    private static volatile boolean isSetup = false;
    private static android.webkit.CookieManager webkitCookMgr;

    public static synchronized void setup(Context context) {
        synchronized (CookieManager.class) {
            if (!isSetup) {
                try {
                    if (Build.VERSION.SDK_INT < 21) {
                        CookieSyncManager.createInstance(context);
                    }
                    webkitCookMgr = android.webkit.CookieManager.getInstance();
                    webkitCookMgr.setAcceptCookie(true);
                    if (Build.VERSION.SDK_INT < 21) {
                        webkitCookMgr.removeExpiredCookie();
                    }
                } catch (Throwable e) {
                    isCookieValid = false;
                    ALog.e(TAG, "Cookie Manager setup failed!!!", (String) null, e, new Object[0]);
                }
                isSetup = true;
            }
        }
        return;
    }

    private static boolean checkSetup() {
        if (!isSetup && NetworkSdkSetting.getContext() != null) {
            setup(NetworkSdkSetting.getContext());
        }
        return isSetup;
    }

    public static synchronized void setCookie(String url, String cookieStr) {
        synchronized (CookieManager.class) {
            if (checkSetup() && isCookieValid) {
                try {
                    webkitCookMgr.setCookie(url, cookieStr);
                    if (Build.VERSION.SDK_INT < 21) {
                        CookieSyncManager.getInstance().sync();
                    } else {
                        webkitCookMgr.flush();
                    }
                } catch (Throwable t) {
                    ALog.e(TAG, "set cookie failed. url=" + url + " cookies=" + cookieStr, (String) null, t, new Object[0]);
                }
            }
        }
        return;
    }

    public static void setCookie(String url, Map<String, List<String>> headers) {
        if (url != null && headers != null) {
            try {
                for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                    String key = entry.getKey();
                    if (key != null && (key.equalsIgnoreCase("Set-Cookie") || key.equalsIgnoreCase(HttpConstant.SET_COOKIE2))) {
                        for (String cookieStr : entry.getValue()) {
                            setCookie(url, cookieStr);
                        }
                    }
                }
            } catch (Exception e) {
                ALog.e(TAG, "set cookie failed", (String) null, e, "url", url, "\nheaders", headers);
            }
        }
    }

    public static synchronized String getCookie(String url) {
        String cookieString = null;
        synchronized (CookieManager.class) {
            if (checkSetup() && isCookieValid) {
                cookieString = null;
                try {
                    cookieString = webkitCookMgr.getCookie(url);
                } catch (Throwable t) {
                    ALog.e(TAG, "get cookie failed. url=" + url, (String) null, t, new Object[0]);
                }
            }
        }
        return cookieString;
    }
}
