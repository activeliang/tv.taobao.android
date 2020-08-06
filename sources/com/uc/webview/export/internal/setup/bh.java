package com.uc.webview.export.internal.setup;

import android.util.Pair;
import android.webkit.ValueCallback;
import com.uc.webview.export.utility.download.UpdateTask;

/* compiled from: ProGuard */
final class bh implements ValueCallback<UpdateTask> {
    final /* synthetic */ ap a;
    final /* synthetic */ bd b;

    bh(bd bdVar, ap apVar) {
        this.b = bdVar;
        this.a = apVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        ap apVar = this.a;
        synchronized (apVar) {
            apVar.a = new Pair<>(0, (Object) null);
            try {
                apVar.notify();
            } catch (Exception e) {
            }
        }
    }
}
