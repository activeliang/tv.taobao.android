package android.taobao.windvane;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

public class WVCookieManager {
    private static final String TAG = "WVCookieManager";

    public static void onCreate(Context ctx) {
        CookieSyncManager.createInstance(ctx);
        CookieManager.getInstance().setAcceptCookie(true);
    }

    public static String getCookie(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        return CookieManager.getInstance().getCookie(url);
    }

    public static void setCookie(String url, String cookieStr) {
        if (!TextUtils.isEmpty(url) && cookieStr != null) {
            CookieManager.getInstance().setCookie(url, cookieStr);
        }
    }
}
