package com.uc.webview.export;

import android.webkit.ValueCallback;
import com.ali.auth.third.core.model.KernelMessageConstants;
import com.uc.webview.export.annotations.Api;
import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.interfaces.IGeolocationPermissions;
import com.uc.webview.export.internal.setup.UCAsyncTask;
import java.util.HashMap;
import java.util.Set;

@Api
/* compiled from: ProGuard */
public class GeolocationPermissions implements IGeolocationPermissions {
    private static HashMap<Integer, GeolocationPermissions> b;
    private IGeolocationPermissions a;

    @Api
    /* compiled from: ProGuard */
    public interface Callback {
        void invoke(String str, boolean z, boolean z2);
    }

    private GeolocationPermissions(IGeolocationPermissions iGeolocationPermissions) {
        this.a = iGeolocationPermissions;
    }

    public static GeolocationPermissions getInstance() {
        return a(((Integer) d.a((int) KernelMessageConstants.PARAM_ERROR, new Object[0])).intValue());
    }

    public static GeolocationPermissions getInstance(WebView webView) {
        return a(webView.getCurrentViewCoreType());
    }

    private static synchronized GeolocationPermissions a(int i) {
        GeolocationPermissions geolocationPermissions;
        synchronized (GeolocationPermissions.class) {
            if (b == null) {
                b = new HashMap<>();
            }
            geolocationPermissions = b.get(Integer.valueOf(i));
            if (geolocationPermissions == null) {
                GeolocationPermissions geolocationPermissions2 = new GeolocationPermissions((IGeolocationPermissions) d.a((int) UCAsyncTask.isPaused, Integer.valueOf(i)));
                b.put(Integer.valueOf(i), geolocationPermissions2);
                geolocationPermissions = geolocationPermissions2;
            }
        }
        return geolocationPermissions;
    }

    public void getOrigins(ValueCallback<Set<String>> valueCallback) {
        this.a.getOrigins(valueCallback);
    }

    public void getAllowed(String str, ValueCallback<Boolean> valueCallback) {
        this.a.getAllowed(str, valueCallback);
    }

    public void clear(String str) {
        this.a.clear(str);
    }

    public void allow(String str) {
        this.a.allow(str);
    }

    public void clearAll() {
        this.a.clearAll();
    }

    public String toString() {
        return "GeolocationPermissions@" + hashCode() + "[" + this.a + "]";
    }
}
