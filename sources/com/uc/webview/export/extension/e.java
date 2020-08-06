package com.uc.webview.export.extension;

import android.os.Handler;
import android.os.Looper;
import android.webkit.ValueCallback;
import com.uc.webview.export.utility.download.UpdateTask;
import java.util.Map;

/* compiled from: ProGuard */
final class e implements ValueCallback<UpdateTask> {
    final /* synthetic */ Map a;
    private int b = 3;

    e(Map map) {
        this.a = map;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        ValueCallback valueCallback;
        UpdateTask updateTask = (UpdateTask) obj;
        if (!(this.a == null || (valueCallback = (ValueCallback) this.a.get("exception")) == null)) {
            try {
                valueCallback.onReceiveValue((Object) null);
            } catch (Throwable th) {
            }
        }
        int i = this.b;
        this.b = i - 1;
        if (i > 0) {
            new Handler(Looper.getMainLooper()).postDelayed(new f(this, updateTask), 60000);
        }
    }
}
