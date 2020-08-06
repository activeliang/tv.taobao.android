package com.uc.webview.export.internal.a;

import android.os.Build;
import android.webkit.ValueCallback;
import android.webkit.WebStorage;
import com.uc.webview.export.WebStorage;
import com.uc.webview.export.internal.interfaces.IWebStorage;
import java.util.HashMap;
import java.util.Map;

/* compiled from: ProGuard */
public final class o implements IWebStorage {
    private WebStorage a = WebStorage.getInstance();

    /* compiled from: ProGuard */
    private class a implements ValueCallback<Map> {
        private ValueCallback<Map> b;

        public final /* synthetic */ void onReceiveValue(Object obj) {
            Map map = (Map) obj;
            if (this.b == null) {
                return;
            }
            if (Build.VERSION.SDK_INT >= 11) {
                ValueCallback<Map> valueCallback = this.b;
                HashMap hashMap = new HashMap();
                for (Map.Entry entry : map.entrySet()) {
                    WebStorage.Origin origin = (WebStorage.Origin) entry.getValue();
                    hashMap.put(entry.getKey(), new WebStorage.Origin(origin.getOrigin(), origin.getQuota(), origin.getUsage()));
                }
                valueCallback.onReceiveValue(hashMap);
                return;
            }
            this.b.onReceiveValue(map);
        }

        public a(ValueCallback<Map> valueCallback) {
            this.b = valueCallback;
        }
    }

    public final void getOrigins(ValueCallback<Map> valueCallback) {
        this.a.getOrigins(new a(valueCallback));
    }

    public final void getUsageForOrigin(String str, ValueCallback<Long> valueCallback) {
        this.a.getUsageForOrigin(str, valueCallback);
    }

    public final void getQuotaForOrigin(String str, ValueCallback<Long> valueCallback) {
        this.a.getQuotaForOrigin(str, valueCallback);
    }

    @Deprecated
    public final void setQuotaForOrigin(String str, long j) {
        this.a.setQuotaForOrigin(str, j);
    }

    public final void deleteOrigin(String str) {
        this.a.deleteOrigin(str);
    }

    public final void deleteAllData() {
        this.a.deleteAllData();
    }
}
