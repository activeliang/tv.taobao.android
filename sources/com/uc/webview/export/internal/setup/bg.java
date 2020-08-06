package com.uc.webview.export.internal.setup;

import android.util.Pair;
import android.webkit.ValueCallback;
import com.uc.webview.export.utility.download.UpdateTask;

/* compiled from: ProGuard */
final class bg implements ValueCallback<UpdateTask> {
    final /* synthetic */ ap a;
    final /* synthetic */ bd b;

    bg(bd bdVar, ap apVar) {
        this.b = bdVar;
        this.a = apVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        UpdateTask updateTask = (UpdateTask) obj;
        updateTask.delete();
        ap apVar = this.a;
        Throwable exception = updateTask.getException();
        synchronized (apVar) {
            apVar.a = new Pair<>(3, exception);
            try {
                apVar.notify();
            } catch (Exception e) {
            }
        }
    }
}
