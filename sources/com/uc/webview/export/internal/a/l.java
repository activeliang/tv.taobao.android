package com.uc.webview.export.internal.a;

import android.net.Uri;
import android.webkit.ValueCallback;

/* compiled from: ProGuard */
final class l implements ValueCallback<Uri[]> {
    final /* synthetic */ ValueCallback a;
    final /* synthetic */ j b;

    l(j jVar, ValueCallback valueCallback) {
        this.b = jVar;
        this.a = valueCallback;
    }

    public final /* bridge */ /* synthetic */ void onReceiveValue(Object obj) {
        Uri[] uriArr = (Uri[]) obj;
        this.a.onReceiveValue(uriArr == null ? null : uriArr[0]);
    }
}
