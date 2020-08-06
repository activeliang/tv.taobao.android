package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;

/* compiled from: ProGuard */
final class i implements ValueCallback<u> {
    final /* synthetic */ b a;

    i(b bVar) {
        this.a = bVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        u uVar = (u) obj;
        if (UCSetupTask.getTotalLoadedUCM() != null) {
            uVar.stop();
        }
    }
}
