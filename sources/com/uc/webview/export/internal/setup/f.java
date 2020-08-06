package com.uc.webview.export.internal.setup;

import android.util.Pair;
import android.webkit.ValueCallback;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.utility.SetupTask;
import java.util.HashMap;
import java.util.Map;

/* compiled from: ProGuard */
final class f implements ValueCallback<SetupTask> {
    final /* synthetic */ ValueCallback a;
    final /* synthetic */ b b;

    f(b bVar, ValueCallback valueCallback) {
        this.b = bVar;
        this.a = valueCallback;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        SetupTask setupTask = (SetupTask) obj;
        if (setupTask.getStat().second != null && this.a != null) {
            this.a.onReceiveValue(setupTask);
            if (Log.sPrintLog) {
                Pair<String, HashMap<String, String>> stat = setupTask.getStat();
                if (Log.sPrintLog) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("ev_ac=").append((String) stat.first);
                    for (Map.Entry entry : ((HashMap) stat.second).entrySet()) {
                        sb.append("`").append((String) entry.getKey()).append("=").append((String) entry.getValue());
                    }
                    Log.d("SDKWaStat", sb.toString());
                }
            }
        }
    }
}
