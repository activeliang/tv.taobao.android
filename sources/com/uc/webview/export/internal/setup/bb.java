package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.setup.UCSetupTask;

/* compiled from: ProGuard */
final class bb implements ValueCallback<CALLBACK_TYPE> {
    final /* synthetic */ UCSetupTask.a a;

    bb(UCSetupTask.a aVar) {
        this.a = aVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        UCSetupTask.a.a(this.a);
        UCSetupTask.a aVar = this.a;
        try {
            if (!aVar.a.exists()) {
                aVar.a.createNewFile();
            } else if (!aVar.c.exists()) {
                aVar.c.createNewFile();
            } else if (!aVar.b.exists()) {
                aVar.b.createNewFile();
            }
        } catch (Throwable th) {
        }
    }
}
