package com.uc.webview.export.internal.a;

import com.uc.webview.export.JsPromptResult;

/* compiled from: ProGuard */
final class e implements JsPromptResult {
    private android.webkit.JsPromptResult a;

    e(android.webkit.JsPromptResult jsPromptResult) {
        this.a = jsPromptResult;
    }

    public final void cancel() {
        this.a.cancel();
    }

    public final void confirm() {
        this.a.confirm();
    }

    public final void confirm(String str) {
        this.a.confirm(str);
    }
}
