package com.ali.user.open.ucc.eleme;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.ali.user.open.ucc.base.BaseUccServiceProvider;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import mtopsdk.common.util.SymbolExpUtil;

public class ElemeUccServiceProviderImpl extends BaseUccServiceProvider {
    public static final String TAG = "AlipayUccServiceProviderImpl";

    /* access modifiers changed from: protected */
    public boolean isAuthByNative(Context context, String targetSite, Map<String, String> map) {
        return false;
    }

    public void cleanUp(Context context) {
        String[] elemeCookieArray;
        try {
            for (String domain : new ArrayList<String>() {
                {
                    add("ele.me");
                }
            }) {
                String elemeCookie = CookieManager.getInstance().getCookie(domain);
                if (!TextUtils.isEmpty(elemeCookie) && (elemeCookieArray = elemeCookie.split(SymbolExpUtil.SYMBOL_SEMICOLON)) != null && elemeCookieArray.length > 0) {
                    for (String cookie : elemeCookieArray) {
                        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
                        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                        CookieManager.getInstance().setCookie("http://" + domain, cookie + "; Domain=." + domain + "; Expires=" + DATE_FORMAT.format(1000));
                    }
                    if (Build.VERSION.SDK_INT >= 21) {
                        CookieManager.getInstance().flush();
                    } else {
                        CookieSyncManager.createInstance(context).sync();
                    }
                }
            }
        } catch (Throwable th) {
        }
    }
}
