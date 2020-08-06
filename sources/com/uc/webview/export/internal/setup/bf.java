package com.uc.webview.export.internal.setup;

import android.util.Pair;
import android.webkit.ValueCallback;
import com.uc.webview.export.utility.download.UpdateTask;

/* compiled from: ProGuard */
final class bf implements ValueCallback<UpdateTask> {
    final /* synthetic */ ap a;
    final /* synthetic */ bd b;

    bf(bd bdVar, ap apVar) {
        this.b = bdVar;
        this.a = apVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        ap apVar = this.a;
        synchronized (apVar) {
            apVar.a = new Pair<>(4, (Object) null);
            try {
                apVar.notify();
            } catch (Exception e) {
            }
        }
    }
}
