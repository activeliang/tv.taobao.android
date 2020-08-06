package com.uc.webview.export.internal.a;

import android.content.Context;
import android.webkit.WebViewDatabase;
import com.uc.webview.export.internal.interfaces.IWebViewDatabase;

/* compiled from: ProGuard */
public final class r implements IWebViewDatabase {
    private WebViewDatabase a;

    public r(Context context) {
        this.a = WebViewDatabase.getInstance(context);
    }

    public final boolean hasUsernamePassword() {
        return this.a.hasUsernamePassword();
    }

    public final void clearUsernamePassword() {
        this.a.clearUsernamePassword();
    }

    public final boolean hasHttpAuthUsernamePassword() {
        return this.a.hasHttpAuthUsernamePassword();
    }

    public final void clearHttpAuthUsernamePassword() {
        this.a.clearHttpAuthUsernamePassword();
    }

    public final boolean hasFormData() {
        return this.a.hasFormData();
    }

    public final void clearFormData() {
        this.a.clearFormData();
    }
}
