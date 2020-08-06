package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.interfaces.IWaStat;

/* compiled from: ProGuard */
final class j implements ValueCallback<u> {
    final /* synthetic */ b a;

    j(b bVar) {
        this.a = bVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        this.a.callStatException(IWaStat.SETUP_EXTRA_EXCEPTION, ((u) obj).getException());
        this.a.b.start();
    }
}
