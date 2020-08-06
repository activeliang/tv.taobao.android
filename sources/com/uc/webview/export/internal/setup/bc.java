package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.setup.UCSetupTask;

/* compiled from: ProGuard */
final class bc implements ValueCallback<CALLBACK_TYPE> {
    final /* synthetic */ UCSetupTask.a a;

    bc(UCSetupTask.a aVar) {
        this.a = aVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        UCSetupTask.a aVar = this.a;
        try {
            aVar.a.delete();
        } catch (Throwable th) {
        }
        try {
            aVar.c.delete();
        } catch (Throwable th2) {
        }
    }
}
