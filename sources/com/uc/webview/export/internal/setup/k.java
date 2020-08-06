package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import java.io.File;

/* compiled from: ProGuard */
final class k implements ValueCallback<u> {
    final /* synthetic */ File a;
    final /* synthetic */ b b;

    k(b bVar, File file) {
        this.b = bVar;
        this.a = file;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        u uVar = (u) obj;
        uVar.setException(new UCSetupException(4005, String.format("Multi crash detected in [%s].", new Object[]{this.a.getAbsolutePath()})));
        uVar.onEvent("die", (ValueCallback) null);
    }
}
