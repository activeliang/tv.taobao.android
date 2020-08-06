package com.uc.webview.export.internal.a;

import com.uc.webview.export.JsResult;

/* compiled from: ProGuard */
final class f implements JsResult {
    private android.webkit.JsResult a;

    f(android.webkit.JsResult jsResult) {
        this.a = jsResult;
    }

    public final void cancel() {
        this.a.cancel();
    }

    public final void confirm() {
        this.a.confirm();
    }
}
